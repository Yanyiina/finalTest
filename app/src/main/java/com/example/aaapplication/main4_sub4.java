package com.example.aaapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera;
import android.os.*;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.my_Process;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.utils.OssUploader.uploadImageToOSS;

public class main4_sub4 extends AppCompatActivity implements SurfaceHolder.Callback {
    private ProgressBar progressBar;
    private TextView timeText;
    private CountDownTimer countDownTimer;
    //
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private CountDownTimer mCountDownTimer;
    private SurfaceHolder mSurfaceHolder;

    // 全局变量，保存体资体态识别结果；
    private String positive_img;
    private String positive_points = null;
    private String positive_heights = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_sub4);
        /**
         * @description:
         * @param savedInstanceState:
         * @return
         * @author: Tian
         */
        //
        mSurfaceView = findViewById(R.id.surfaceView1);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        //
        /**
         * @description:
         * @param savedInstanceState:
         * @return
         * @author: Tian
         */


        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        // 用户退出
        // 用户点击后，返回上一层页面
        TextView btn_exist = findViewById(R.id.main4_sub4_exist);
        btn_exist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main4_sub4.this,Main4_fit_0.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        });

        // 开始时将进度条和时间隐藏
        final View viewById = findViewById(R.id.process_and_time);
        viewById.setVisibility(View.GONE);

        timeText = findViewById(R.id.countTextView);
        final CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 在每个计时间隔结束时触发
                long secondsRemaining = millisUntilFinished / 1000;
                if (secondsRemaining == 10) {
                    timeText.setText("Start");
                } else if (secondsRemaining > 0) {
                    timeText.setText(String.valueOf(secondsRemaining));
                }
            }

            @Override
            public void onFinish() {
                // 倒计时完成时触发
                viewById.setVisibility(View.VISIBLE);
                View viewById1 = findViewById(R.id.countdown);
                viewById1.setVisibility(View.INVISIBLE);

                // 启动进度条和时间
                my_Process my_process = new my_Process();
                progressBar = findViewById(R.id.customProgressBar);
                timeText = findViewById(R.id.timeText);
                my_process.start_process(progressBar, timeText);
                //
                takePhotoWithDelay(5);


                    student.setPositive_height(positive_heights);
                    student.setPositive_points(positive_points);
                    System.out.println("+++++1" + positive_points);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(main4_sub4.this, Main4_sub5.class);
                            intent.putExtra("student", student);
                            startActivity(intent);
                            finish();
                        }
                    }, 60000);
                }
        };

        countDownTimer.start(); // 启动倒计时

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera==null){
            mCamera=getCamera();//初始化mCamera
            if(mSurfaceHolder!=null){
                setStartPreview(mCamera,mSurfaceHolder);//开始预览，这样打开这个activity就直接显示预览画面
            }
        }
    }
    //预览
    private void setStartPreview(Camera camera,SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);//绑定camera和holder
            camera.setDisplayOrientation(90);//系统camera预览角度默认为横屏，旋转90°可变为竖屏
            camera.startPreview();//开始在Surfaceview组件中预览camera获取到的内容
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //打开相机
    private Camera getCamera(){
        Camera camera;
        try {
            camera=Camera.open(0);//获取camera对象
        }catch (Exception e){
            camera=null;//发生异常将camera=null
        }
        return camera;
    }

    //activity生命周期结束
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    //停止相机预览，释放相机资源
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);//将摄像头的回调置空，取消mcamera和mholder的关联
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera,mSurfaceHolder);//开始预览
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();//停止取景
        setStartPreview(mCamera,mSurfaceHolder);//开始预览
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    private static File savePhoto(byte[] data) {
        // 创建保存图片的目录
        File pictureDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyPictures");
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs();
        }
        // 格式化日期作为图片文件名
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String fileName = "IMG_text.png";

        // 创建图片文件
        File pictureFile = new File(pictureDirectory, fileName);
        System.out.println("imagePath路径："+pictureFile);

        try {
            // 保存图片数据到文件
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Log.i("Camera", "图片保存成功: " + pictureFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("Camera", "图片保存失败", e);
        }
        return pictureFile;
    }
    private void takePhotoWithDelay(int delaySeconds) {
        mCountDownTimer = new CountDownTimer(delaySeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                // 更新倒计时显示，例如在界面上显示秒数
//                int secondsRemaining = (int) millisUntilFinished / 1000;
//                // 这里你需要根据自己的实际情况更新界面上的倒计时显示
//                tvCountdown.setText(String.valueOf(secondsRemaining));
            }
            @Override
            public void onFinish() {
                takePictureAndStopCamera();
            }
        };
        mCountDownTimer.start();
    }
    private void takePictureAndStopCamera() {
        mCamera.takePicture(null, null,
                new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        // 处理照片保存逻辑
                        savePhoto(data);
                        upload(data);
                        //倒计时停止
                        mCountDownTimer.cancel();
                        // 相机预览
                        camera.startPreview();
                    }
                });
    }
    /**
     //     * 上传照片
     //     */
    public void upload(final byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String imagePath = "/storage/emulated/0/Pictures/MyPictures/IMG_20230708_143644.png";
//                uploadImageToOSS(imagePath);
//                String imagePath = String.valueOf(savePhoto(data));
//                String[] back = uploadImageToOSS(imagePath);
//                System.out.println("+++++++" + back);
//                positive_points = back[0];
//
//                //将back2转为JSON格式，获取它的height属性
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode jsonNode = null;
//                try {
//                    jsonNode = objectMapper.readTree(back[1]);
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
//                // 获取"lag2"后面的值
//                positive_heights = jsonNode.get("lag2").toString();
                String imagePath = String.valueOf(savePhoto(data));
                uploadImageToOSS(imagePath);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止倒计时定时器
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}