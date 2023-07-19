package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main5_age3_6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_age3_6);

        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

//        返回按钮
        Button main5_btn_back = (Button) findViewById(R.id.main5_btn_back);
        main5_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_age3_6.this,Main3_function.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

//        点击 单腿站立 按钮
        TextView main5_btn2 = (TextView) findViewById(R.id.main5_btn2);
        main5_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_age3_6.this,Main5_sub1.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });


//        点击 身体支撑 按钮
        TextView main5_btn1 = (TextView) findViewById(R.id.main5_btn1);
        main5_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_age3_6.this,Main5_sub2.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });


//        点击 垂直纵跳 按钮
        TextView main5_btn3 = (TextView) findViewById(R.id.main5_btn3);
        main5_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_age3_6.this,Main5_sub3.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

//         点击 原地开合跳 按钮
        TextView main5_btn4 = (TextView) findViewById(R.id.main5_btn4);
        main5_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_age3_6.this,Main5_sub4.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });


//        点击 反应时评估 按钮
        TextView main5_btn5 = (TextView) findViewById(R.id.main5_btn5);
        main5_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_age3_6.this,Main5_sub5.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });


        //        点击 身体形态 按钮
        TextView main5_btn6 = (TextView) findViewById(R.id.main5_btn6);
        main5_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_age3_6.this,Main5_age3_6_bmi.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });



//
    }
}
