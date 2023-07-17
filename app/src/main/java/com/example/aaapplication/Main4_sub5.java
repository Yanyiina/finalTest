package com.example.aaapplication;

import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aaapplication.R;

public class Main4_sub5 extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView timeText;
    private CountDownTimer countDownTimer;

    private final long totalTime = 60000; // 总计时时间（毫秒）
    private final long interval = 1000; // 倒计时时间间隔（毫秒）
    private final int progressMax = 100; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_sub5);

        Button btn_exist = (Button) findViewById(R.id.main4_sub2_exist);
        btn_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4_sub5.this, Main4_fit_0.class);
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

}




