package com.example.aaapplication;

import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uplaod.ChildInfo;
import com.example.uplaod.Master;
import com.example.uplaod.Result;
import com.example.utils.OssUploader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main4_sub5 extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView timeText;
    private CountDownTimer countDownTimer;

    private final long totalTime = 60000; // 总计时时间（毫秒）
    private final long interval = 1000; // 倒计时时间间隔（毫秒）
    private final int progressMax = 100; //

//    private AsyncTask<String, Void, CompletableFuture<Void>> task1Future;
//    private AsyncTask<String, Void, CompletableFuture<Void>> task2Future;
    private CompletableFuture<Void> task1Future;
    private CompletableFuture<Void> task2Future;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_sub5);

        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

//
        executorService = Executors.newFixedThreadPool(2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            task1Future = CompletableFuture.runAsync(() -> {
                String imageUrl = student.getPositive_url();
                String parm = "0";
                String parm2 = student.getPositive_url();
                OssUploader.segmentImage(imageUrl, parm, parm2);
            }, executorService);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            task2Future = CompletableFuture.runAsync(() -> {
                String imageUrl = student.getSide_url();
                String parm = "1";
                String parm2 = student.getSide_url();
                OssUploader.segmentImage(imageUrl, parm, parm2);
            }, executorService);
        }

        // 在这里执行任务完成后的逻辑
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            task1Future.thenAccept(result -> {
                System.out.println("任务1执行完毕");
                // 可以在这里更新UI或执行其他操作
                // 例如，你可以在这里检查任务2是否也执行完毕

                if (task2Future.isDone()) {
                    System.out.println("所有任务执行完毕");
                    Runnable uploadTask = new Runnable() {
                        @Override
                        public void run() {
                            // 上传操作代码，同样将网络请求和IO操作放在这里
                            // ...（原本的上传代码）
                            ChildInfo childInfo = new ChildInfo();
                            childInfo.setChildId(student.getID());

                            Master master = new Master();
                            master.setPositivePoints(student.getPositive_points());
                            master.setLocationPositiveHeight(Double.parseDouble(student.getPositive_height()));
                            String url_String = "https://reliable-b-custom.oss-cn-qingdao.aliyuncs.com/" + student.getPositive_url();
                            master.setPositiveImg(url_String);

                            // 侧面
                            master.setSidePoints(student.getSide_points());
                            master.setLocationSideHeight(Double.parseDouble(student.getSide_height()));
                            String url_String1 = "https://reliable-b-custom.oss-cn-qingdao.aliyuncs.com/" + student.getSide_url();
                            master.setSideImg(url_String1);
                            childInfo.setMaster(master);

                            Result[] results = new Result[2];
                            Result result1 = new Result();
                            result1.setProjectId(student.getPositive_ID());
                            result1.setMeasuredResult( student.getHeight().intValue());
                            results[0] = result1;
                            System.out.println("tctctc" + result1);

                            Result result2 = new Result();
                            result2.setProjectId(student.getSide_ID());
                            result2.setMeasuredResult(student.getWeight().intValue());
                            results[1] = result2;
                            System.out.println("tctctc" + result2);

                            childInfo.setResult(results);

                            ObjectMapper objectMapper = new ObjectMapper();
                            String jsonString = null;
                            try {
                                jsonString = objectMapper.writeValueAsString(childInfo);
                                System.out.println(jsonString);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            String url = "https://transferapi.imreliable.com/hardware/test/submitPosture";
//                            try {
//                                URL apiUrl = new URL(url);
//                                HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
//                                conn.setRequestMethod("POST");
//                                conn.setRequestProperty("Content-Type", "application/json");
//                                conn.setDoOutput(true);
//
//                                // 将JSON对象写入请求体
//                                try (OutputStream os = conn.getOutputStream()) {
//                                    assert jsonString != null;
//                                    byte[] input = jsonString.getBytes("utf-8");
//                                    os.write(input, 0, input.length);
//                                }
//
//                                // 获取响应
//                                int responseCode = conn.getResponseCode();
//                                System.out.println("Response Code: " + responseCode);
//
//                                // 处理响应内容
//                                // ...
//
//                                conn.disconnect();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            String finalJsonString = jsonString;
                            CompletableFuture<Void> serverRequestFuture = CompletableFuture.runAsync(() -> {
                                // 向服务器发送请求的代码
                                // ...（服务器请求的代码）
                                String url = "https://transferapi.imreliable.com/hardware/test/submitPosture";
                                try {
                                    URL apiUrl = new URL(url);
                                    HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setRequestProperty("Content-Type", "application/json");
                                    conn.setDoOutput(true);


                                    // 将JSON对象写入请求体
                                    try (OutputStream os = conn.getOutputStream()) {
                                        assert finalJsonString != null;
                                        byte[] input = finalJsonString.getBytes("utf-8");
                                        os.write(input, 0, input.length);
                                    }

                                    // 获取响应
                                    int responseCode = conn.getResponseCode();
                                    System.out.println("Response Code: " + responseCode);

                                    conn.disconnect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });


                            serverRequestFuture.thenAccept(result -> {
                                System.out.println("服务器请求完成");
                                // 可以在这里更新UI或执行其他操作
                                // ...（服务器请求完成后的处理代码）
                                System.out.println("111111");
                                CompletableFuture<Void> serverRequestFuture1 = CompletableFuture.runAsync(() -> {
                                    // 向服务器发送GET请求的代码
                                    String baseUrl = "https://transferapi.imreliable.com/hardware/test/reportViewUrl";
                                    String queryParams = "?child_id=" + student.getID() + "&report_type=34";
                                    String fullUrl = baseUrl + queryParams;

                                    try {
                                        URL apiUrl = new URL(fullUrl);
                                        HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
                                        conn.setRequestMethod("GET");
                                        conn.setRequestProperty("Content-Type", "application/json");

                                        // 获取响应
                                        int responseCode = conn.getResponseCode();
                                        System.out.println("Response Code: " + responseCode);

                                        // 解析响应内容
                                        if (responseCode == HttpURLConnection.HTTP_OK) {
                                            // 读取服务器返回的数据
                                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                                                StringBuilder response = new StringBuilder();
                                                String line;
                                                while ((line = reader.readLine()) != null) {
                                                    response.append(line);
                                                }

                                                // 处理JSON数据
                                                String jsonString1 = response.toString();
                                                System.out.println("JSON Response: " + jsonString1);

                                                // 在这里执行处理JSON数据的逻辑
                                                // ...
                                                JSONObject json = new JSONObject(response.toString());
                                                String specification = json.getJSONObject("data").getString("url");
                                                System.out.println("specification+++++" + specification);
                                                student.setReportUrl(specification);
                                            }
                                        } else {
                                            // 处理请求失败的情况
                                            System.out.println("Server request failed with response code: " + responseCode);
                                        }
                                        conn.disconnect();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });

                                serverRequestFuture1.thenAccept(result3 -> {
                                    System.out.println("服务器请求完成");
                                    // 可以在这里更新UI或执行其他操作
                                    // ...（服务器请求完成后的处理代码）
                                    System.out.println("111111");
                                });
                            });
                        }
                    };
                    // 创建一个新的线程来执行上传操作
                    Thread uploadThread = new Thread(uploadTask);
                    uploadThread.start();
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            task2Future.thenAccept(result -> {
                System.out.println("任务2执行完毕");
                // 可以在这里更新UI或执行其他操作
                // 例如，你可以在这里检查任务1是否也执行完毕
                if (task1Future.isDone()) {
                    System.out.println("所有任务执行完毕");
                    // 在这里执行上传操作

                    // 上传
                    Runnable uploadTask = new Runnable() {
                        @Override
                        public void run() {
                            // 上传操作代码，同样将网络请求和IO操作放在这里
                            // ...（原本的上传代码）
                            ChildInfo childInfo = new ChildInfo();
                            childInfo.setChildId(student.getID());

                            Master master = new Master();
                            master.setPositivePoints(student.getPositive_points());
                            master.setLocationPositiveHeight(Double.parseDouble(student.getPositive_height()));
                            String url_String = "https://reliable-b-custom.oss-cn-qingdao.aliyuncs.com/" + student.getPositive_url();
                            master.setPositiveImg(url_String);

                            // 侧面
                            master.setSidePoints(student.getSide_points());
                            master.setLocationSideHeight(Double.parseDouble(student.getSide_height()));
                            String url_String1 = "https://reliable-b-custom.oss-cn-qingdao.aliyuncs.com/" + student.getSide_url();
                            master.setSideImg(url_String1);
                            childInfo.setMaster(master);

                            Result[] results = new Result[2];
                            Result result1 = new Result();
                            result1.setProjectId(student.getPositive_ID());
                            result1.setMeasuredResult( student.getHeight().intValue());
                            results[0] = result1;
                            System.out.println("tctctc" + result1);

                            Result result2 = new Result();
                            result2.setProjectId(student.getSide_ID());
                            result2.setMeasuredResult(student.getWeight().intValue());
                            results[1] = result2;
                            System.out.println("tctctc" + result2);

                            childInfo.setResult(results);

                            ObjectMapper objectMapper = new ObjectMapper();
                            String jsonString = null;
                            try {
                                jsonString = objectMapper.writeValueAsString(childInfo);
                                System.out.println(jsonString);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String finalJsonString = jsonString;
                            CompletableFuture<Void> serverRequestFuture = CompletableFuture.runAsync(() -> {
                                // 向服务器发送请求的代码
                                // ...（服务器请求的代码）
                                String url = "https://transferapi.imreliable.com/hardware/test/submitPosture";
                                try {
                                    URL apiUrl = new URL(url);
                                    HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setRequestProperty("Content-Type", "application/json");
                                    conn.setDoOutput(true);


                                    // 将JSON对象写入请求体
                                    try (OutputStream os = conn.getOutputStream()) {
                                        assert finalJsonString != null;
                                        byte[] input = finalJsonString.getBytes("utf-8");
                                        os.write(input, 0, input.length);
                                    }

                                    // 获取响应
                                    int responseCode = conn.getResponseCode();
                                    System.out.println("Response Code: " + responseCode);

                                    conn.disconnect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                            serverRequestFuture.thenAccept(result -> {
                                System.out.println("服务器请求完成");
                                // 可以在这里更新UI或执行其他操作
                                // ...（服务器请求完成后的处理代码）
                                System.out.println("111111");
                                CompletableFuture<Void> serverRequestFuture1 = CompletableFuture.runAsync(() -> {
                                    // 向服务器发送GET请求的代码
                                    String baseUrl = "https://transferapi.imreliable.com/hardware/test/reportViewUrl";
                                    String queryParams = "?child_id=" + student.getID() + "&report_type=34";
                                    String fullUrl = baseUrl + queryParams;

                                    try {
                                        URL apiUrl = new URL(fullUrl);
                                        HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
                                        conn.setRequestMethod("GET");
                                        conn.setRequestProperty("Content-Type", "application/json");

                                        // 获取响应
                                        int responseCode = conn.getResponseCode();
                                        System.out.println("Response Code: " + responseCode);

                                        // 解析响应内容
                                        if (responseCode == HttpURLConnection.HTTP_OK) {
                                            // 读取服务器返回的数据
                                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                                                StringBuilder response = new StringBuilder();
                                                String line;
                                                while ((line = reader.readLine()) != null) {
                                                    response.append(line);
                                                }

                                                // 处理JSON数据
                                                String jsonString1 = response.toString();
                                                System.out.println("JSON Response: " + jsonString1);

                                                // 在这里执行处理JSON数据的逻辑
                                                // ...
                                                JSONObject json = new JSONObject(response.toString());
                                                String specification = json.getJSONObject("data").getString("url");
                                                System.out.println("specification+++++" + specification);
                                                student.setReportUrl(specification);
                                            }
                                        } else {
                                            // 处理请求失败的情况
                                            System.out.println("Server request failed with response code: " + responseCode);
                                        }
                                        conn.disconnect();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });

                                serverRequestFuture1.thenAccept(result3 -> {
                                    System.out.println("服务器请求完成");
                                    // 可以在这里更新UI或执行其他操作
                                    // ...（服务器请求完成后的处理代码）
                                    System.out.println("111111");
                                });
                            });
                        }
                    };
                    // 创建一个新的线程来执行上传操作
                    Thread uploadThread = new Thread(uploadTask);
                    uploadThread.start();
                }
            });
        }




        TextView btn_exist = findViewById(R.id.main4_sub5_exist);
        btn_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4_sub5.this, Main4_fit_0.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        });


        progressBar = findViewById(R.id.customProgressBar);
        timeText = findViewById(R.id.countTextView);

        // 设置进度条的最大值为 60（1分钟）
        progressBar.setMax(60);

        // 创建并启动倒计时定时器
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 每秒更新进度条和时间文本
                int progress = (int) (60000 - millisUntilFinished) / 1000;
                progressBar.setProgress(progress);
                int count = (int)Math.ceil((100.0 / 60.0) * progress) ;
                timeText.setText( count + "%");
            }

            @Override
            public void onFinish() {
                // 定时器结束时的操作
                progressBar.setProgress(60);
                timeText.setText("01:00");


                Intent intent = new Intent(Main4_sub5.this, Main4_sub6.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        };

        // 启动倒计时定时器
        countDownTimer.start();
    }

    private class NetworkTask extends AsyncTask<String, Void, CompletableFuture<Void>> {
        @Override
        protected CompletableFuture<Void> doInBackground(String... urls) {
            String imageUrl = urls[0];
            String parm = urls[1];
            String parm2 = urls[2];
            // 在后台线程中执行网络请求
            return OssUploader.segmentImage(imageUrl, parm, parm2);
        }

        @Override
        protected void onPostExecute(CompletableFuture<Void> result) {
            // 在任务执行完毕后处理结果
            if (result != null) {
                // 处理返回的结果
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    result.thenAccept(res -> {
                        System.out.println("执行完毕");
                        // 在这里执行任务完成后的逻辑
                        // 例如，可以在这里更新UI或执行其他操作
                        // 任务1和任务2都执行完后，会执行到这里
                    }).exceptionally(ex -> {
                        // 处理任务失败的情况
                        ex.printStackTrace();
                        return null;
                    });
                }
            } else {
                // 处理请求失败的情况
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止倒计时定时器
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // 停止线程池
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}




