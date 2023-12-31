package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

//2  身体支撑

public class Main5_sub2_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_sub2_3);

        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        //      结束按钮
        TextView main5_sub2_3_exist = (TextView)findViewById(R.id.main5_sub2_3_exist);
        main5_sub2_3_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub2_3.this, Main5_age3_6.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        });



        //        返回按钮
        Button main5_sub2_3_back = (Button) findViewById(R.id.main5_sub2_3_back);
        main5_sub2_3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub2_3.this, Main5_age3_6.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        //        下一个项目    单腿站立
        LinearLayout main5_sub2_3_start = (LinearLayout) findViewById(R.id.main5_sub2_3_start);
        main5_sub2_3_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub2_3.this, Main5_sub1.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });


//        查看报告   main5_sub1_3_check
//
    }
}
