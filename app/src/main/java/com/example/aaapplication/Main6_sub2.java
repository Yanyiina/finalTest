package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main6_sub2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6_sub2);

        //        返回按钮
        Button main6_sub2_back = (Button) findViewById(R.id.main6_sub2_back);
        main6_sub2_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_sub2.this,Main6_age7_18.class);
                startActivity(intent);
            }
        });

//                开始评估
        Button main6_sub2_start = (Button) findViewById(R.id.main6_sub2_start);
        main6_sub2_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main6_sub2.this, Main6_sub2_1.class);
                startActivity(intent);
            }
        });

//
    }
}
