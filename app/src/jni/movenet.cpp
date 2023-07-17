// Tencent is pleased to support the open source community by making ncnn available.
//
// Copyright (C) 2021 THL A29 Limited, a Tencent company. All rights reserved.
//
// Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
// in compliance with the License. You may obtain a copy of the License at
//
// https://opensource.org/licenses/BSD-3-Clause
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

#include "movenet.h"

#include "utils.cpp"
#include <ctime>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "cpu.h"
#include <iostream>
#include <string>
#include<android/log.h>
#include<cmath>

#define TAG "myDemo-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型



MoveNet::MoveNet() : joint_filters()
{
    num_joints = 17;
    sport_kind = 1;
    count_number = 0;
    count_lock = false;

    blob_pool_allocator.set_size_compare_ratio(0.f);
    workspace_pool_allocator.set_size_compare_ratio(0.f);
}

int MoveNet::load(const char* modeltype, int _target_size, const float* _mean_vals, const float* _norm_vals, bool use_gpu, int sport_id)
{
    poseNet.clear();
    blob_pool_allocator.clear();
    workspace_pool_allocator.clear();

    ncnn::set_cpu_powersave(2);
    ncnn::set_omp_num_threads(ncnn::get_big_cpu_count());

    poseNet.opt = ncnn::Option();

#if NCNN_VULKAN
    poseNet.opt.use_vulkan_compute = use_gpu;
#endif

    poseNet.opt.num_threads = ncnn::get_big_cpu_count();
    poseNet.opt.blob_allocator = &blob_pool_allocator;
    poseNet.opt.workspace_allocator = &workspace_pool_allocator;

    char parampath[256];
    char modelpath[256];
    sprintf(parampath, "%s.param", modeltype);
    sprintf(modelpath, "%s.bin", modeltype);

    poseNet.load_param(parampath);
    poseNet.load_model(modelpath);

    target_size = _target_size;
    mean_vals[0] = _mean_vals[0];
    mean_vals[1] = _mean_vals[1];
    mean_vals[2] = _mean_vals[2];
    norm_vals[0] = _norm_vals[0];
    norm_vals[1] = _norm_vals[1];
    norm_vals[2] = _norm_vals[2];

    sport_kind = sport_id;
    count_number = 0;
    count_lock = false;

    return 0;
}

int MoveNet::load(AAssetManager* mgr, const char* modeltype, int _target_size, const float* _mean_vals, const float* _norm_vals, bool use_gpu, int sport_id)
{
    poseNet.clear();
    blob_pool_allocator.clear();
    workspace_pool_allocator.clear();

    ncnn::set_cpu_powersave(2);
    ncnn::set_omp_num_threads(ncnn::get_big_cpu_count());

    poseNet.opt = ncnn::Option();

#if NCNN_VULKAN
    poseNet.opt.use_vulkan_compute = use_gpu;
#endif

    poseNet.opt.num_threads = ncnn::get_big_cpu_count();
    poseNet.opt.blob_allocator = &blob_pool_allocator;
    poseNet.opt.workspace_allocator = &workspace_pool_allocator;
    char parampath[256];
    char modelpath[256];
    sprintf(parampath, "%s.param", modeltype);
    sprintf(modelpath, "%s.bin", modeltype);

    poseNet.load_param(mgr,parampath);
    poseNet.load_model(mgr,modelpath);

    target_size = _target_size;  // should be 192
    feature_size = 48;
    mean_vals[0] = _mean_vals[0];
    mean_vals[1] = _mean_vals[1];
    mean_vals[2] = _mean_vals[2];
    norm_vals[0] = _norm_vals[0];
    norm_vals[1] = _norm_vals[1];
    norm_vals[2] = _norm_vals[2];

    sport_kind = sport_id;
    count_number = 0;
    count_lock = false;

    return 0;
}

void MoveNet::preprocess(cv::Mat &rgb, ncnn::Mat& in_processed, int& hpad, int& wpad, float& scale) {
    int w = rgb.cols;
    int h = rgb.rows;
    if (w > h)
    {
        scale = (float)target_size / w;
        w = target_size;
        h = h * scale;
    }
    else
    {
        scale = (float)target_size / h;
        h = target_size;
        w = w * scale;
    }

    ncnn::Mat in = ncnn::Mat::from_pixels_resize(rgb.data, ncnn::Mat::PIXEL_RGB, rgb.cols, rgb.rows, w, h);
    wpad = target_size - w;
    hpad = target_size - h;
    ncnn::Mat in_pad;
    ncnn::copy_make_border(in, in_processed, hpad / 2, hpad - hpad / 2, wpad / 2, wpad - wpad / 2, ncnn::BORDER_CONSTANT, 0.f);

}

void MoveNet::detect(cv::Mat &rgb, std::vector<keypoint> &points)
{
    ncnn::Mat in;
    int hpad, wpad;
    float scale;
    preprocess(rgb, in, hpad, wpad, scale);
    in.substract_mean_normalize(mean_vals, norm_vals);

    ncnn::Extractor ex = poseNet.create_extractor();
    ex.input("input", in);

    ncnn::Mat regress, offset, heatmap, center;
    ex.extract("regress", regress);
    ex.extract("offset", offset);
    ex.extract("heatmap", heatmap);
    ex.extract("center", center);

    // get top index from center map (flattened)
    float* center_data = (float*)center.data;
    int ct_top_idx = int(argmax(center_data, center_data + center.total()));
    int ct_y = ct_top_idx / feature_size;
    int ct_x = ct_top_idx % feature_size;

    // get regressed value of the top index for each joint
    std::vector<float> regressed_y(num_joints), regressed_x(num_joints);
    float* regress_data_y = (float*)regress.channel(0).row(ct_y) + ct_x;
    float* regress_data_x = (float*)regress.channel(num_joints).row(ct_y) + ct_x;
    for (size_t i = 0; i < num_joints; ++i)
    {
        regressed_y[i] = regress_data_y[0] + (float)ct_y;
        regressed_x[i] = regress_data_x[0] + (float)ct_x;

        regress_data_y += regress.cstep;
        regress_data_x += regress.cstep;
    }

    // compute scores for each `pixel` based on heatmap and its distance between the regress point
    ncnn::Mat kpt_scores = ncnn::Mat(feature_size * feature_size, num_joints, sizeof(float));
    float* scores_data = (float*)kpt_scores.data;
    for (int c = 0; c < num_joints; ++c) {
        for (int y = 0; y < feature_size; ++y) {
            for (int x = 0; x < feature_size; ++x) {
                float dist_weight = std::sqrt(std::pow(y - regressed_y[c], 2) + std::pow(x - regressed_x[c], 2)) + 1.8;
                scores_data[c* feature_size * feature_size + y * feature_size + x] = heatmap.channel(c).row(y)[x] / dist_weight;
            }
        }
    }

    // get rough joints
    std::vector<std::pair<int, int>> rough_kpts;
    for (int i = 0; i < num_joints; ++i) {
        int score_top_idx = int(argmax(scores_data + feature_size * feature_size *i, scores_data + feature_size * feature_size *(i+1)));
        int score_top_y = score_top_idx / feature_size;
        int score_top_x = score_top_idx % feature_size;
        rough_kpts.push_back({score_top_y, score_top_x});
    }

    // add offset for refined joints and map to rgb size
    points.clear();
    for (int i = 0; i < num_joints; ++i)
    {
        int rough_y = rough_kpts[i].first, rough_x = rough_kpts[i].second;
        float kpt_offset_y = offset.channel(2 * i).row(rough_y)[rough_x];
        float kpt_offset_x = offset.channel(2 * i + 1).row(rough_y)[rough_x];

        float refined_y = (rough_y + kpt_offset_y);
        float refined_x = (rough_x + kpt_offset_x);

        // the heatmap is downsampled by 4
        keypoint kpt;
        kpt.x = (refined_x * 4 - (wpad / 2)) / scale;
        kpt.y = (refined_y * 4 - (hpad / 2)) / scale;
        kpt.score = heatmap.channel(i).row(rough_y)[rough_x];
        points.push_back(kpt);
    }

    // filter joint jitter
    for (int i = 0; i < num_joints; ++i) {
//        points[i].y = joint_filters.filters[i].first.filter(clock(), points[i].y);
//        points[i].x = joint_filters.filters[i].second.filter(clock(), points[i].x);

        points[i].y = joint_filters.filters[i].first.filter(points[i].y);
        points[i].x = joint_filters.filters[i].second.filter(points[i].x);
    }
}

void MoveNet::draw(cv::Mat& rgb, std::vector<keypoint> &points)
{
    int skele_index[][2] = {
        {0,1},{0,2},{1,3},{2,4},{0,5},
        {0,6},{5,6},{5,7},{7,9},{6,8},
        {8,10},{11,12},{5,11},{11,13},
        {13,15},{6,12},{12,14},{14,16}
    };

    int color_index[][3] = {
        {255, 0, 0},{0, 0, 255},{255, 0, 0},{0, 0, 255},{255, 0, 0},
        {0, 0, 255},{0, 255, 0},{255, 0, 0},{255, 0, 0},{0, 0, 255},
        {0, 0, 255},{0, 255, 0},{255, 0, 0},{255, 0, 0},
        {255, 0, 0},{0, 0, 255},{0, 0, 255},{0, 0, 255},
        };

    for (int i = 0; i < 18; i++)
    {
        if(points[skele_index[i][0]].score > 0.3 && points[skele_index[i][1]].score > 0.3)
            cv::line(rgb,
                    cv::Point(points[skele_index[i][0]].x, points[skele_index[i][0]].y),
                    cv::Point(points[skele_index[i][1]].x, points[skele_index[i][1]].y),
                    cv::Scalar(color_index[i][0],color_index[i][1], color_index[i][2]), 2
                    );
    }
    for (int i = 0; i < num_joints; i++)
    {
        if (points[i].score > 0.3)
            cv::circle(rgb,
                    cv::Point(points[i].x,points[i].y),
                    3, cv::Scalar(100, 255, 150), -1
                    );
    }
}

void MoveNet::count(std::vector<keypoint>& points) {
    /* The map from joint index to joint:
     * 0 : neck; 1 & 2 : eyes; 3 & 4 : ears
     * 5 & 6 : shoulders; 7 & 8 : elbows; 9 & 10 : hands
     * 11 & 12 : hips; 13 & 14 : knees;
     * 15 & 16 : feet
     * */
    //原地踏步
    if (sport_kind == 3) {
        bool valid = (points[15].score > 0.3 && points[16].score > 0.3);
        if (!valid) return;

        if (!count_lock) {
            count_lock = true;
            start = points[16].y;
        }
        if (count_lock = true && (points[15].y > start + 35 || points[16].y > start + 35)) {
            count_number += 1;
        }
    }
    //仰卧起坐
    if (sport_kind == 0) {
        bool valid = (points[0].score > 0.3 && points[11].score > 0.3 && points[15].score > 0.3);
        if (!valid) return;

        float neck_hip_foot_angle = angle(points[0], points[11], points[15]);
        if(abs(abs(points[0].x) - abs(points[10].x)) < 0.5){
            //双手抱头
        }
        if (!count_lock && neck_hip_foot_angle < 120) {
            count_lock = true;
        }

        if (count_lock && neck_hip_foot_angle > 150) {
            count_lock = false;
            count_number += 1;
        }
    }
    std::string str = "point[7].x------->" + std::to_string(points[0].x);
    std::string str1 = "point[7].y------->" + std::to_string(points[0].y);
    LOGI("%s", str.c_str());
    LOGI("%s", str1.c_str());
    //原地开合跳
    if (sport_kind == 1) {
        bool valid = (points[5].score > 0.3 && points[6].score > 0.3 && points[7].score > 0.3);
        if (!valid) return;
        float elbow_shoulder_shoulder_angle = angle(points[7], points[5], points[6]);
        if (!count_lock && elbow_shoulder_shoulder_angle > 150 && points[5].y < points[7].y) {
            count_lock = true;
        }
        if (count_lock && elbow_shoulder_shoulder_angle < 120 && points[5].y > points[7].y) {
            count_lock = false;
            count_number += 1;
        }

    }
    //俯卧撑
    if (sport_kind == 2) {
        bool valid = (points[0].score > 0.3 && points[11].score > 0.3 && points[15].score > 0.3);
        if (!valid) return;
        float neck_hip_foot_angle = angle(points[0], points[11], points[15]);
        if(neck_hip_foot_angle < 160){
            //提示用户在测试过程中，头部、背部、腿部要保持在一条直线上。
        }
        //18为阈值设定，当points[5].y - points[7].y) < 18时，说明肩膀和肘部已经离的很近了
        if (!count_lock && neck_hip_foot_angle > 160 && ((points[5].y - points[7].y) < 18 || (points[6].y - points[8].y) < 18)) {
            count_lock = true;
        }
        if (count_lock && ((points[5].y - points[7].y) > 18|| (points[6].y - points[8].y) > 18)) {
            count_lock = false;
            count_number += 1;
        }
    }
    //纵跳
    if (sport_kind == 3) {
        bool valid = (points[15].score > 0.3 && points[16].score > 0.3);
        if (!valid) return;

        if ((abs(points[15].y) - abs(points[16].y)) > 100) {
            //请双脚起跳
        }
        if (!count_lock) {
            count_lock = true;
            start = points[16].y;
        }
        if (count_lock = true && points[16].y > start + 35 && (abs(points[15].y) - abs(points[16].y)) <100) {
            double q = abs(points[16].y - start);
            temp = std::max(temp, q);
            double f = temp * 0.26;
            //double f = 6.7998;
            double t1;
            int n;
            n = 1;//保留n位有效数字。
            t1 = f - (int)f;//取小数部分。
            t1*=pow(10, n);//小数点左移。
            if(t1-(int)t1 >= 0.5) t1+=1; //四舍五入
            t1 = (int)t1; //取整数部分。

            t1/=pow(10, n);//小数点右移。
            t1+=(int)f;//加上原本的整数部分。
            count_number = t1;
        }
    }
    //身体支撑
    if (sport_kind == 4) {
        bool valid = (points[0].score > 0.3 && points[5].score > 0.3 && points[7].score > 0.3);
        if (!valid) return;
        float neck_shoulder_elbow_angle = angle(points[0], points[5], points[7]);
        if (!count_lock) {
            count_lock = true;
            start = points[16].y;
        }
        if (count_lock && points[16].y > start +18 && neck_shoulder_elbow_angle > 160) {
            prop_timeflag = 1;

        }
        if (prop_timeflag == 1 && points[16].y < start + 18 && neck_shoulder_elbow_angle < 160){
            prop_timeflag = 2;
        }
        }
    //闭眼单脚站
    if (sport_kind == 5) {
        bool valid = (points[0].score > 0.3 && points[5].score > 0.3 && points[7].score > 0.3);
        if (!valid) return;
        clock_t first;
        clock_t last;
        float L_hands_shoulder_hip_angle = angle(points[9], points[5], points[11]);
        float R_hands_shoulder_hip_angle = angle(points[10], points[6], points[12]);
        //count_number = distance(points[11],points[15]);
//        std::tm* local_time;
//        std::tm* local_time1;
        if (!count_lock && L_hands_shoulder_hip_angle > 70 && R_hands_shoulder_hip_angle > 70 &&  ((points[11].y - points[13].y) < 18 || (points[12].y - points[14].y) < 18)) {
        //if (!count_lock && ((points[11].y - points[13].y) < 0.2 || (points[12].y - points[14].y) < 0.2)) {

        //if (!count_lock && L_hands_shoulder_hip_angle > 70 && R_hands_shoulder_hip_angle > 70) {
            count_lock = true;
            //获取时间
            // 获取当前系统时间
//            std::time_t now = std::time(nullptr);
//            // 将当前系统时间转换为本地时间
//            local_time = std::localtime(&now);
            //first = clock();
            count_number = 11;
        }
        //if (count_lock && ((points[11].y - points[13].y) > 18 || (points[12].y - points[14].y) > 18)){
        if (count_lock &&  (L_hands_shoulder_hip_angle < 70 || R_hands_shoulder_hip_angle < 70 || (points[11].y - points[13].y) > 18) || (points[12].y - points[14].y) > 18) {
            //std::time_t end = std::time(nullptr);
            // 将当前系统时间转换为本地时间
            //local_time1 = std::localtime(&end);
            //int hour = local_time1->tm_hour - local_time->tm_hour;
            //int min  = local_time1->tm_min - local_time->tm_min;
            //int sec  = local_time1->tm_sec - local_time->tm_sec;
            //last = clock();
            //count_number = hour * 3600 + min *60 + sec;
            count_lock = false;
            count_number = 12;
        }

    }
    //
//    if (sport_kind == 6) {
//        bool valid = (points[0].score > 0.3 && points[5].score > 0.3 && points[7].score > 0.3);
//        if (!valid) return;
//        float neck_shoulder_elbow_angle = angle(points[0], points[5], points[7]);
//        count_number = points[11].y;
//
//    }
//    if (sport_kind == 7) {
//        bool valid = (points[0].score > 0.3 && points[5].score > 0.3 && points[7].score > 0.3);
//        if (!valid) return;
//        float neck_shoulder_elbow_angle = angle(points[0], points[5], points[7]);
//        count_number = points[13].y;
//
//    }
    if (sport_kind == 6) {
        float test = angle(points[3], points[1], points[0]);
        if(test >90){
            count_number+=1;
        }

    }
}
