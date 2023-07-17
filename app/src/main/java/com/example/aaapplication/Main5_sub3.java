package com.example.aaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main5_sub3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_sub3);

        //        返回按钮
        Button main5_sub3_back = (Button) findViewById(R.id.main5_sub3_back);
        main5_sub3_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub3.this, Main5_age3_6.class);
                startActivity(intent);
            }
        });

//                开始评估
        Button main5_sub3_start = (Button) findViewById(R.id.main5_sub3_start);
        main5_sub3_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub3.this, Main5_sub3_1.class);
                startActivity(intent);
            }
        });


//
    }
}
