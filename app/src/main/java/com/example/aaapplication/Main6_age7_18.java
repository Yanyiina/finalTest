package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main6_age7_18 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_age7_18);

        //        返回按钮
        Button main6_btn_back = (Button) findViewById(R.id.main6_btn_back);
        main6_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main3_function.class);
                startActivity(intent);
            }
        });

//        点击 一分钟仰卧起坐 按钮
        Button main6_btn1 = (Button) findViewById(R.id.main6_btn1);
        main6_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub1.class);
                startActivity(intent);
            }
        });

        //        点击 单脚站立 按钮
        Button main6_btn2 = (Button) findViewById(R.id.main6_btn2);
        main6_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub2.class);
                startActivity(intent);
            }
        });

        //        点击 垂直纵跳 按钮
        Button main6_btn3 = (Button) findViewById(R.id.main6_btn3);
        main6_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub3.class);
                startActivity(intent);
            }
        });

        //        点击 30秒连续跳按钮
        Button main6_btn4 = (Button) findViewById(R.id.main6_btn4);
        main6_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub4.class);
                startActivity(intent);
            }
        });

        //        点击 俯卧撑 按钮
        Button main6_btn5 = (Button) findViewById(R.id.main6_btn5);
        main6_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub5.class);
                startActivity(intent);
            }
        });

        //        点击 俯卧撑 按钮
        Button main6_btn6 = (Button) findViewById(R.id.main6_btn6);
        main6_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub6.class);
                startActivity(intent);
            }
        });


//
    }
}
