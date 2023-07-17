package com.example.aaapplication;

import android.os.AsyncTask;
import android.widget.ProgressBar;

public class UpdateProgressBarTask extends AsyncTask<Void, Integer, Void> {
    private ProgressBar progressBar;
    private int totalTime;

    public UpdateProgressBarTask(ProgressBar progressBar, int totalTime) {
        this.progressBar = progressBar;
        this.totalTime = totalTime;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 1; i <= totalTime; i++) {
            publishProgress(i);
            try {
                Thread.sleep(1000); // 1秒延迟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        progressBar.setProgress(progress);
    }

    @Override
    protected void onPostExecute(Void result) {
        // 更新进度条完成后的操作
    }
}
