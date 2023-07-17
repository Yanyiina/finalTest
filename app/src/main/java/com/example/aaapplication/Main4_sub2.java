package com.example.aaapplication;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;



public class Main4_sub2 extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView timeText;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_sub2);

        // 用户退出
        // 用户点击后，返回上一层页面
        Button btn_exist = (Button)findViewById(R.id.main4_sub2_exist);
        btn_exist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4_sub2.this,Main4_fit_0.class);
                startActivity(intent);
                finish();
            }
        });

        // 设置前端的数字,数字是后端给
        // 程序控制逻辑：若count=10，跳转至下一界面；或count不等于10，但计时器结束，跳转
        // 若是count不等于10，时间等于01:00，则直接返回至初始页面
        int count = 5;
        TextView viewById = findViewById(R.id.countTextView);
        viewById.setText(count + "/10");


        // 设置进度条和后边的时间
        progressBar = findViewById(R.id.customProgressBar);
        timeText = findViewById(R.id.timeText);

        // 设置进度条的最大值为 60（1分钟）
        progressBar.setMax(60);

        // 创建并启动倒计时定时器
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 每秒更新进度条和时间文本
//                int progress = (int) (60000 - millisUntilFinished) / 1000;
                int progress = (int) ((60000 - millisUntilFinished) * 60 / 60000);
                progressBar.setProgress(progress);
                timeText.setText(formatTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                // 定时器结束时的操作
                progressBar.setProgress(60);
                timeText.setText("01:00");
                Intent intent = new Intent(Main4_sub2.this, Main4_sub3.class);
                startActivity(intent);
                finish();
            }
        };

        // 启动倒计时定时器
        countDownTimer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止倒计时定时器
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // 辅助方法：将时间毫秒值格式化为 mm:ss 的字符串
    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }



}