package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main_high extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_high);


        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        TextView btn_go = (TextView) findViewById(R.id.main_high_exist);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_high.this,Main3_function.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        });


        Button btn_pose_go = (Button) findViewById(R.id.main_high_next);
        btn_pose_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_high.this,Main4_sub1.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        });


//
    }
}
