package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Main5_sub5_3 extends AppCompatActivity {

    private Button btn_start;
    private RelativeLayout rel_front;
    private RelativeLayout rel_behind;
    private RelativeLayout rel_num;
    private RelativeLayout rel_cir;
    private TextView timeText;
    private CountDownTimer countDownTimer;
    private TextView main5_sub5_3_exist;

//    环形倒计时
    private ProgressBar progressBar;
    private TextView countdownText;
    private CountDownTimer countDown_Timer;

    private final long totalTime = 11000; // 总计时时间（毫秒）
    private final long interval = 1000; // 倒计时时间间隔（毫秒）
    private final int progressMax = 100; // 进度条最大值

    //    正向计时
    private TextView elapsedTimeText;

    private Handler elapsedHandler;
    private Runnable elapsedRunnable;
    private int totalTime_2 = 10; // 正计时总时间（秒）    与倒计时相差一
    private int currentTime_2 = 0; // 当前正计时时间（秒）


//    打地鼠

    private int mouseCount = 0;
    private int maxMouseCount = 10;
    private boolean gameStarted = false;
    private CountDownTimer mouseTimer;
    private TextView score;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_sub5_3);

        //点击切换页面
        btn_start = (Button) findViewById(R.id.main5_sub5_1_start);
        rel_front = (RelativeLayout)findViewById(R.id.main5_sub5_front);
        rel_behind = (RelativeLayout)findViewById(R.id.main5_sub5_behind);
        rel_num = (RelativeLayout)findViewById(R.id.main5_sub5_num);
        rel_cir = (RelativeLayout)findViewById(R.id.main5_sub5_cir);
        //地鼠
        score = (TextView)findViewById(R.id.score);


        //        点击  跳过 按钮， 切换页面
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_front.setVisibility(View.GONE);
                rel_behind.setVisibility(View.VISIBLE);
                // 启动倒计时
                countDownTimer.start();
            }
        });

        //      数字 倒计时
        timeText = findViewById(R.id.main5_sub5_time);
        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 在每个计时间隔结束时触发
                long secondsRemaining = millisUntilFinished / 1000;
                timeText.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                // 倒计时结束后的操作
                countDownTimer.cancel();
                countDownTimer = null;
                // 倒计时完成时触发
                rel_num.setVisibility(View.INVISIBLE);
                rel_cir.setVisibility(View.VISIBLE);

//                创建新的
                startCountdown();
                startElapsedTime();
                startProgressBar();

                // 启动打地鼠游戏
                startWhackAMoleGame();

            }
        };


//        结束测试按钮
        main5_sub5_3_exist = (TextView)findViewById(R.id.main5_sub5_3_exist);
        main5_sub5_3_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
//                地鼠
                if (mouseTimer != null) {
                    mouseTimer.cancel();
                    mouseTimer = null;
                }
                Intent intent = new Intent(Main5_sub5_3.this,Main5_age3_6.class);
                startActivity(intent);
                finish();
            }
        });


//        圆环倒计时
        //        倒计时
        progressBar = findViewById(R.id.main5_sub5_progressBar);
        countdownText = findViewById(R.id.main5_sub5_countdownText);
        //        正计时
        elapsedTimeText = findViewById(R.id.main5_sub5_elapsedTimeText);

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

//        startCountdown();
//        startElapsedTime();
//        startProgressBar();


//
    }

    //倒计时设置
    private void startCountdown() {
        countDown_Timer = new CountDownTimer(totalTime, interval) {
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
        countDown_Timer.start();
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
        Intent intent = new Intent(Main5_sub5_3.this, Main5_sub5_4.class);
        intent.putExtra("score", mouseCount);
        startActivity(intent);
        finish(); // 结束当前页面
    }

    //计时结束
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDown_Timer != null) {
            countDown_Timer.cancel();
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


    // 打地鼠游戏
    // 打地鼠游戏
    private void startWhackAMoleGame() {
        gameStarted = true;
        mouseCount = 0;
        mouseTimer = new CountDownTimer(totalTime, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (gameStarted && mouseCount < maxMouseCount) {
                    showMouseInRandomHole();
                } else {
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                cancel();
            }
        };
        mouseTimer.start();
    }

    private void stopWhackAMoleGame() {
        gameStarted = false;
        mouseCount = 0;
//        holeLayout.setOnClickListener(null);
        if (mouseTimer != null) {
            mouseTimer.cancel();
            mouseTimer = null;
        }
    }

    private void showMouseInRandomHole() {
        // 根据您提供的打地鼠布局文件中的 hole 和 mouse 控件的 ID 命名规则进行随机显示地鼠的逻辑
        int holeCount = 9; // 假设有 9 个洞
        int randomHoleIndex = (int) (Math.random() * holeCount) + 1; // 随机选择一个洞
        int mouseId = getResources().getIdentifier("mouse" + randomHoleIndex, "id", getPackageName());
        final ImageButton randomHoleLayout = findViewById(mouseId);
        randomHoleLayout.setVisibility(View.VISIBLE);

        randomHoleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameStarted && mouseCount < maxMouseCount) {
                    mouseCount++;
                    v.setVisibility(View.INVISIBLE);
                    score.setText("得分为" + mouseCount);
                    if (mouseCount == maxMouseCount) {
                        stopWhackAMoleGame();
                        navigateToAnotherActivity();
                    }
                }
            }
        });

        // 地鼠消失的延迟时间，可根据需要调整
        int mouseDisappearDelay = 500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                randomHoleLayout.setVisibility(View.INVISIBLE);
            }
        }, mouseDisappearDelay);
    }



//
}
