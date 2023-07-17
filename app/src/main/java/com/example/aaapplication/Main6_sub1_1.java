package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class Main6_sub1_1 extends AppCompatActivity {

    private VideoView videoView;
    private Button btn_start;
    private RelativeLayout rel_front;
    private RelativeLayout rel_behind;
    private TextView timeText;
    private CountDownTimer countDownTimer;
    private TextView main6_sub1_1_exist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_sub1_1);

        //        按键播放视频
        videoView = (VideoView) findViewById(R.id.main6_sub1_1_videoView);
        btn_start = (Button) findViewById(R.id.main6_sub1_1_start);
        rel_front = (RelativeLayout)findViewById(R.id.main6_sub1_front);
        rel_behind = (RelativeLayout)findViewById(R.id.main6_sub1_behind);

        //读取放在 raw 目录下的文件
        videoView.setVideoURI(Uri.parse("android.resource://com.example.aaapplication/" + R.raw.main6_yangwo));
        videoView.setMediaController(new MediaController(this));

        //        设置 隐藏视频自带暂停开始健
        MediaController mediaCtlr = new MediaController(Main6_sub1_1.this);
        mediaCtlr.setMediaPlayer(videoView);
        mediaCtlr.setVisibility(View.INVISIBLE); //设置隐藏
        videoView.setMediaController(mediaCtlr);

        //        点击事件
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_front.setVisibility(View.GONE);
                rel_behind.setVisibility(View.VISIBLE);
                // 启动视频
                videoView.start();
                // 启动倒计时
                countDownTimer.start();
            }
        });
        //        自动播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });


        //        结束测试按钮
        main6_sub1_1_exist = (TextView)findViewById(R.id.main6_sub1_1_exist);
        main6_sub1_1_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                Intent intent = new Intent(Main6_sub1_1.this,Main6_age7_18.class);
                startActivity(intent);
                finish(); // 销毁当前活动
            }
        });


//        倒计时
        timeText = findViewById(R.id.main6_sub1_time);
        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 在每个计时间隔结束时触发
                long secondsRemaining = millisUntilFinished / 1000;
                timeText.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                // 倒计时完成时触发
                if (countDownTimer != null) {
                    Intent intent = new Intent(Main6_sub1_1.this, Main6_sub1_2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
//                timeText.setText("Done");
                }
            }
        };




//
    }
}
