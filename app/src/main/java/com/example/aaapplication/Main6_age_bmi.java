package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Main6_age_bmi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_age_bmi);

        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        //      结束按钮
        TextView main5_sub6_3_exist = (TextView)findViewById(R.id.main6_sub7_3_exist);
        main5_sub6_3_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age_bmi.this, Main6_age7_18.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        });



        //        返回按钮
        Button main5_sub6_3_back = (Button) findViewById(R.id.main6_sub7_3_back);
        main5_sub6_3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age_bmi.this, Main6_age7_18.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        //        下一个项目    单腿站立
        LinearLayout main5_sub6_3_start = (LinearLayout) findViewById(R.id.main6_sub7_3_start);
        main5_sub6_3_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age_bmi.this, Main6_sub1.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });


//
    }
}
