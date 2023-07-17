package com.example.aaapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.utils.my_Process;

public class main4_sub4 extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView timeText;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_sub4);

        // 用户退出
        // 用户点击后，返回上一层页面
        Button btn_exist = (Button)findViewById(R.id.main4_sub2_exist);
        btn_exist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main4_sub4.this,Main4_fit_0.class);
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
            }
        };

        countDownTimer.start(); // 启动倒计时

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