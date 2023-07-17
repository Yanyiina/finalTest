package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main6_sub3_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_sub3_3);

        //      结束按钮
        TextView main6_sub3_3_exist = (TextView)findViewById(R.id.main6_sub3_3_exist);
        main6_sub3_3_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_sub3_3.this, Main6_age7_18.class);
                startActivity(intent);
                finish();
            }
        });


        //        返回按钮
        Button main6_sub3_3_back = (Button) findViewById(R.id.main6_sub3_3_back);
        main6_sub3_3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_sub3_3.this, Main6_age7_18.class);
                startActivity(intent);
            }
        });

        //        下一个项目   原地开合跳
        LinearLayout main6_sub3_3_start = (LinearLayout) findViewById(R.id.main6_sub3_3_start);
        main6_sub3_3_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_sub3_3.this, Main6_sub4.class);
                startActivity(intent);
            }
        });


//        查看报告   main5_sub1_3_check





//
    }
}
