package com.example.aaapplication;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class Main4_sub1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4_sub1);
        final Handler handler = new Handler();

        LinearLayout layout = findViewById(R.id.whether_show);
        // 这里需要后端传入检测值(boolean类型)，没有人或距离不满足传入FALSE

        boolean isCameraBlocked = true;

        if(!isCameraBlocked){
            layout.setVisibility(View.GONE);
        }else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 在这里执行需要延迟的任务
                    // 例如，在10秒后执行某个操作
                    Intent intent = new Intent(Main4_sub1.this, Main4_sub2.class);
                    startActivity(intent);
                }
            }, 10000); // 10000 毫秒 = 10秒

        }

        // 用户点击后，返回上一层页面
        TextView btn_exist = findViewById(R.id.main4_sub1_exist);
        btn_exist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4_sub1.this,Main4_fit_0.class);
                startActivity(intent);
                handler.removeCallbacksAndMessages(null); // 取消延迟任务
                finish();
            }
        });

    }
}