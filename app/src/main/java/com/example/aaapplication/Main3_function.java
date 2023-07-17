package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main3_function extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_function);

//        返回按钮
        Button btn_go = (Button)findViewById(R.id.main3_btn_back);
        btn_go.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3_function.this,Main2_stu_list.class);
                startActivity(intent);
            }
        });

//        点击  体态评估按钮  跳转页面4


//        体适能 选择  获取年龄

        // 获取传递的整数类型年龄数据
        int age = getIntent().getIntExtra("age", 0);


        LinearLayout btn_fit = (LinearLayout) findViewById(R.id.btn_fit);
//        btn_fit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Main3_function.this,Main6_age7_18.class);
//                startActivity(intent);
//            }
//            });

//        跳转不同页面
        if(age <= 6 && age >=3){
            //        点击  体适能按钮  跳转页面5
            btn_fit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Main3_function.this,Main5_age3_6.class);
                    startActivity(intent);
                }
            });
        }else if(age>6 && age <= 18){
            btn_fit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Main3_function.this,Main6_age7_18.class);
                    startActivity(intent);
                }
            });
        }

        // 获取点击时传过来的student，用于渲染页面
        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        //性别改头像
        ImageView imageView = findViewById(R.id.main3_picture);
        TextView textView = findViewById(R.id.main3_name);


        int sex = student.getGender();

        if (sex == 2) {
            imageView.setImageResource(R.drawable.main3_girl_wid);
        } else {
            imageView.setImageResource(R.drawable.main3_boy_wid);
        }
        textView.setText(student.getChild_name());

        LinearLayout btn_pose = (LinearLayout) findViewById(R.id.btn_pose);
        btn_pose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main3_function.this,Main4_fit_0.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

    }
}
