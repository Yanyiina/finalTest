package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main5_sub5_4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_sub5_4);

//        得分
        // 获取上一个页面传递的Intent
        Intent intent = getIntent();
        // 从Intent中提取得分数据
        int score = intent.getIntExtra("score", 0);
        // 找到TextView
        TextView scoreTextView = findViewById(R.id.main5_sub5_4_score);
        // 将得分设置为TextView的文本
        scoreTextView.setText(String.valueOf(score));


        //      结束按钮
        TextView main5_sub5_4_exist = (TextView)findViewById(R.id.main5_sub5_4_exist);
        main5_sub5_4_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub5_4.this, Main5_age3_6.class);
                startActivity(intent);
                finish();
            }
        });


        //        返回按钮
        Button main5_sub5_4_back = (Button) findViewById(R.id.main5_sub5_4_back);
        main5_sub5_4_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub5_4.this, Main5_age3_6.class);
                startActivity(intent);
                finish();
            }
        });

        //        下一个项目
        LinearLayout main5_sub5_4_start = (LinearLayout) findViewById(R.id.main5_sub5_4_start);
        main5_sub5_4_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub5_4.this, Main5_age3_6.class);
                startActivity(intent);
            }
        });


//        查看报告   main5_sub1_3_check


    }
}
