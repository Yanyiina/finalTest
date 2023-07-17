package com.example.aaapplication;
//选择反应式评估
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main5_sub5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_sub5);

        //        返回按钮
        Button main5_sub5_back = (Button) findViewById(R.id.main5_sub5_back);
        main5_sub5_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub5.this,Main5_age3_6.class);
                startActivity(intent);
            }
        });

//                开始评估
        Button main5_sub5_start = (Button) findViewById(R.id.main5_sub5_start);
        main5_sub5_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main5_sub5.this, Main5_sub5_3.class);
                startActivity(intent);
            }
        });


//
    }
}
