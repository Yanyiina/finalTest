package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main6_age7_18 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_age7_18);

        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        //        返回按钮
        Button main6_btn_back = (Button) findViewById(R.id.main6_btn_back);
        main6_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main3_function.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

//        点击 一分钟仰卧起坐 按钮
        TextView main6_btn1 = (TextView) findViewById(R.id.main6_btn1);
        main6_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub1.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        //        点击 单脚站立 按钮
        TextView main6_btn2 = (TextView) findViewById(R.id.main6_btn2);
        main6_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub2.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        //        点击 垂直纵跳 按钮
        TextView main6_btn3 = (TextView) findViewById(R.id.main6_btn3);
        main6_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub3.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        //        点击 30秒连续跳按钮
        TextView main6_btn4 = (TextView) findViewById(R.id.main6_btn4);
        main6_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub4.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        //        点击 俯卧撑 按钮
        TextView main6_btn5 = (TextView) findViewById(R.id.main6_btn5);
        main6_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub5.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        //        点击 俯卧撑 按钮
        TextView main6_btn6 = (TextView) findViewById(R.id.main6_btn6);
        main6_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_sub6.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        //        点击 身体形态 按钮
        TextView main6_btn7 = (TextView) findViewById(R.id.main6_btn7);
        main6_btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18.this,Main6_age7_18_bmi.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });



//
    }
}
