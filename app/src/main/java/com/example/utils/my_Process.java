package com.example.utils;

import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @PROJECT_NAME: new project
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/6/27 10:14
 */
public class my_Process extends AppCompatActivity {
    private static ProgressBar progressBar;
    private static TextView timeText;
    private static CountDownTimer countDownTimer;

    public static ProgressBar getProgressBar() {
        return progressBar;
    }

    public static void setProgressBar(ProgressBar progressBar) {
        my_Process.progressBar = progressBar;
    }

    public static TextView getTimeText() {
        return timeText;
    }

    public static void setTimeText(TextView timeText) {
        my_Process.timeText = timeText;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        my_Process.countDownTimer = countDownTimer;
    }

    public void start_process(ProgressBar process_id, TextView time_id){

        progressBar = process_id;
        timeText = time_id;

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
