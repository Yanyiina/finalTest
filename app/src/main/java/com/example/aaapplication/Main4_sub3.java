package com.example.aaapplication;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;



public class Main4_sub3 extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView timeText;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_sub3);

        // 用户退出
        // 用户点击后，返回上一层页面
        Button btn_exist = (Button)findViewById(R.id.main4_sub2_exist);
        btn_exist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4_sub3.this,Main4_fit_0.class);
                startActivity(intent);
                finish();
            }
        });

        progressBar = findViewById(R.id.customProgressBar);
        timeText = findViewById(R.id.timeText);

        // 设置进度条的最大值为 60（1分钟）
        progressBar.setMax(60);

        // 创建并启动倒计时定时器
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 每秒更新进度条和时间文本
                int progress = (int) (60000 - millisUntilFinished) / 1000;
                progressBar.setProgress(progress);
                timeText.setText(formatTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                // 定时器结束时的操作
                progressBar.setProgress(60);
                timeText.setText("01:00");

                // 跳转下一页面
                Intent intent = new Intent(Main4_sub3.this, main4_sub4.class);
                startActivity(intent);
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

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
