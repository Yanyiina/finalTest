package com.example.aaapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aaapplication.R;

public class Main4_sub6 extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_sub6);

        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        String reportUrl = student.getReportUrl(); // 假设这是你获取到的报告URL

        // 在WebView中加载报告URL
        WebView webView = findViewById(R.id.webView); // 替换 R.id.webView 为你的 WebView ID
        webView.getSettings().setJavaScriptEnabled(true); // 允许加载JavaScript
        webView.loadUrl(reportUrl);
    }
}