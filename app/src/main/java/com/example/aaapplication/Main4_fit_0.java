package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main4_fit_0 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_fit_0);

        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        Button btn_go = (Button)findViewById(R.id.main4_btn_back);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4_fit_0.this,Main3_function.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        });


        Button btn_pose_go = (Button) findViewById(R.id.main4_start);
        btn_pose_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4_fit_0.this,Main4_sub1.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
