package com.example.aaapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main_high extends AppCompatActivity {

    private Double maxHeight;
    private Double minHeight;
    private Double maxWeight;
    private Double minWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_high);


        Intent intent = getIntent();
        final List_stu student = (List_stu) intent.getSerializableExtra("student");

        final TextView btn_go = (TextView) findViewById(R.id.main_high_exist);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_high.this,Main3_function.class);
                intent.putExtra("student", student);
                startActivity(intent);
                finish();
            }
        });


        final Button btn_pose_go = (Button) findViewById(R.id.main_high_next);
        btn_pose_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText heightEditText = findViewById(R.id.main_high_height);
                final EditText weightEditText = findViewById(R.id.main_high_weight);
                String heightInput = heightEditText.getText().toString();
                String weightInput = weightEditText.getText().toString();

                if (!heightInput.isEmpty()) {
                    student.setHeight(Double.parseDouble(heightInput));
                    // 处理身高数据
                }

                if (!weightInput.isEmpty()) {
                    student.setWeight(Double.parseDouble(weightInput));
                    // 处理体重数据
                }

                if(heightInput.isEmpty() || weightInput.isEmpty()){
                    btn_pose_go.setEnabled(false);
                }

                if(student.getHeight() >= minHeight && student.getHeight() <= maxHeight && student.getWeight() >= minWeight && student.getWeight() <= maxWeight){
                    btn_pose_go.setBackgroundResource(R.drawable.color_main_bmi);
                    Intent intent = new Intent(Main_high.this,Main4_sub1.class);
                    intent.putExtra("student", student);
                    startActivity(intent);
                    finish();
                }

                // 设置输入框监听器，在输入完身高和体重后，启用或禁用按钮
                heightEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String heightInput = s.toString();
                        String weightInput = weightEditText.getText().toString();

                        if (!heightInput.isEmpty() && !weightInput.isEmpty()) {
                            btn_pose_go.setBackgroundResource(R.drawable.color_main_bmi);
                            btn_pose_go.setEnabled(true);
                        } else {
                            btn_pose_go.setEnabled(false);
                        }
                    }
                });

                weightEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String heightInput = heightEditText.getText().toString();
                        String weightInput = s.toString();

                        if (!heightInput.isEmpty() && !weightInput.isEmpty()) {
                            btn_pose_go.setBackgroundResource(R.drawable.color_main_bmi);
                            btn_pose_go.setEnabled(true);
                        } else {
                            btn_pose_go.setEnabled(false);
                        }
                    }
                });
            }
        });


        new Thread(new Runnable() {
            public void run() {
                try {
                    // 发送HTTP请求获取JSON数据
                    URL url = new URL("https://transferapi.imreliable.com/hardware/test/postureProjectListAndLastTest?child_id=" + student.getID());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // 读取返回的JSON数据
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println(response);
                    reader.close();
                    connection.disconnect();

                    // 解析JSON数据并提取 "list" 数据
                    JSONObject json = new JSONObject(response.toString());
                    // 获取身高体重的规范
                    JSONArray specification = json.getJSONObject("data").getJSONArray("projectList");
                    // 获取实际身高体重
                    JSONArray actual = json.getJSONObject("data").getJSONArray("lastTest");

                    // 从JSON数组中获取身高体重的规范
                    for(int i = 0; i < specification.length(); i++){
                        JSONObject item = specification.getJSONObject(i);

                        // 这里对输入进行限制
                        if(i == 0){
                            maxHeight = Double.parseDouble(item.getString("max_value"));
                            minHeight = Double.parseDouble(item.getString("min_value"));
                        }

                        if(i == 1){
                            maxWeight = Double.parseDouble(item.getString("max_value"));
                            minWeight = Double.parseDouble(item.getString("min_value"));
                        }

                    }

                    for (int i = 0; i < actual.length(); i++){
                        JSONObject jsonObject = actual.getJSONObject(i);

                        if(!jsonObject.getString("measured_result").equals("") && i == 0){
                            student.setHeight(Double.parseDouble(jsonObject.getString("measured_result")));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 渲染身高数据到UI
                                    TextView heightTextView = findViewById(R.id.main_high_height);
                                    heightTextView.setText(String.valueOf(student.getHeight()));
                                }
                            });
                        }

                        if(!jsonObject.getString("measured_result").equals("") && i == 1){
                            student.setWeight(Double.parseDouble(jsonObject.getString("measured_result")));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 渲染身高数据到UI
                                    TextView heightTextView = findViewById(R.id.main_high_weight);
                                    heightTextView.setText(String.valueOf(student.getWeight()));
                                }
                            });
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


//
    }
}
