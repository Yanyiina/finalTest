package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class Main6_sub2_2 extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView countdownText;
    private CountDownTimer countDownTimer;

    private final long totalTime = 11000; // 总计时时间（毫秒）
    private final long interval = 1000; // 倒计时时间间隔（毫秒）
    private final int progressMax = 100; // 进度条最大值

    //    正向计时
    private TextView elapsedTimeText;

    private Handler elapsedHandler;
    private Runnable elapsedRunnable;
    private int totalTime_2 = 10; // 正计时总时间（秒）    与倒计时相差一
    private int currentTime_2 = 0; // 当前正计时时间（秒）

    //    视屏播放
    private VideoView videoView;
    private TextView main6_sub2_2_exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_sub2_2);

        //        自动播放视频
        //读取放在 raw 目录下的文件
        videoView = (VideoView) findViewById(R.id.main6_sub2_2_videoView);
        videoView.setVideoURI(Uri.parse("android.resource://com.example.aaapplication/" + R.raw.main5_dantui));
        videoView.setMediaController(new MediaController(this));

        //        设置 隐藏视频自带暂停开始健
        MediaController mediaCtlr = new MediaController(Main6_sub2_2.this);
        mediaCtlr.setMediaPlayer(videoView);
        mediaCtlr.setVisibility(View.INVISIBLE); //设置隐藏
        videoView.setMediaController(mediaCtlr);
        videoView.start();

        //        自动播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

//        结束测试按钮
        main6_sub2_2_exist = (TextView)findViewById(R.id.main6_sub2_2_exist);
        main6_sub2_2_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_sub2_2.this,Main6_age7_18.class);
                startActivity(intent);
                finish();
            }
        });


        //        倒计时
        progressBar = findViewById(R.id.main6_sub2_progressBar);
        countdownText = findViewById(R.id.main6_sub2_countdownText);
        //        正计时
        elapsedTimeText = findViewById(R.id.main6_sub2_elapsedTimeText);

//        正计时
        elapsedHandler = new Handler();
        elapsedRunnable = new Runnable() {
            @Override
            public void run() {
                currentTime_2++;
                updateElapsedTimeText();
                if (currentTime_2 < totalTime_2) {
                    elapsedHandler.postDelayed(this, 1000); // 每秒更新一次
                }
            }
        };

        startCountdown();
        startElapsedTime();
        startProgressBar();

//
    }

    //倒计时设置
    private void startCountdown() {
        countDownTimer = new CountDownTimer(totalTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;

                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                countdownText.setText(timeLeftFormatted);

//                int progress = (int) (millisUntilFinished * 100 / totalTime);
////                progressBar.setProgress(progress);
            }
            @Override
            public void onFinish() {
                countdownText.setText("00:00");
                navigateToAnotherActivity();  //倒计时正常结束时跳转另一个界面
//                progressBar.setProgress(0);
            }
        };
        countDownTimer.start();
    }

    //进度条
    private void startProgressBar() {
        final long progressInterval = totalTime / progressMax;

        new CountDownTimer(totalTime, progressInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
//                int progress = (int) ((totalTime - timeRemaining) * progressMax / totalTime);
                int progress = (int) ((totalTime - millisUntilFinished) * progressMax / totalTime);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(progressMax);
            }
        }.start();
    }

    //倒计时结束时跳转页面
    private void navigateToAnotherActivity() {
        Intent intent = new Intent(Main6_sub2_2.this, Main6_sub2_3.class);
        startActivity(intent);
        finish(); // 结束当前页面
    }

    //计时结束
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        elapsedHandler.removeCallbacks(elapsedRunnable);
    }

    //    正计时
    private void startElapsedTime() {
        elapsedHandler.postDelayed(elapsedRunnable, 1000); // 启动正计时计时器，每秒更新一次
    }

    private void updateElapsedTimeText() {
        String elapsedTimeFormatted = String.format("%d", currentTime_2);
        elapsedTimeText.setText(elapsedTimeFormatted);
    }

//
}
