package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main6_age7_18_bmi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_age7_18_bmi);



        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        //        返回按钮
        Button main5_sub2_back = (Button) findViewById(R.id.main6_sub7_back);
        main5_sub2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18_bmi.this,Main6_age7_18.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

//                开始评估
        Button main6_sub2_start = (Button) findViewById(R.id.main6_sub7_start);
        main6_sub2_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_age7_18_bmi.this, Main6_age_bmi.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });



//
    }
}
