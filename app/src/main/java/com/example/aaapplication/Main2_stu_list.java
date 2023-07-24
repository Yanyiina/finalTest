package com.example.aaapplication;



import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


public class Main2_stu_list extends AppCompatActivity {

    ListView listView;
    List<List_stu> list = new ArrayList<>();
    private List_stu_Adapter adapter;
    private List<List_stu> originalList = new ArrayList<>();
    private int currentPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private String childName;

//    加载列表
    private TextView showDialogButton;
    private List<String> pageList;
    private ArrayAdapter<String> adapter2;
    private ListView listViewSmall;
    private RelativeLayout mainLayout;
    private boolean isFirstSearchBoxClick = true;

    private ListView listLayout;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_stu_list);

//        加载列表
        showDialogButton = findViewById(R.id.main2_load_page);
        listViewSmall = findViewById(R.id.listView_small); // 获取ListView实例
        listLayout = findViewById(R.id.main2_stu_list);

        pageList = new ArrayList<>();
        // 从数据库中获取页数数据并添加到pageList中
        // 这里为示例直接添加数据
        for (int i = 1; i <= 20; i++) {
            pageList.add("第 " + i + " 页");
        }

        // 创建适配器并设置列表项数据
        adapter2 = new ArrayAdapter<>(this, R.layout.dialog_list, pageList);
        listViewSmall.setAdapter(adapter2);

        // 获取整个布局的实例
        mainLayout = findViewById(R.id.linear_list_page);


        // 设置 TextView 的点击事件监听器
        showDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示或隐藏列表
                if (listViewSmall.getVisibility() == View.VISIBLE) {
                    hideListView();
                } else {
                    showListView();
                }
            }
        });

        // 获取main2_stu_list列表视图
        ListView main2StuList = findViewById(R.id.main2_stu_list);
        // 设置 main2_stu_list 列表视图的触摸事件监听器
        main2StuList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideListView();
                // 如果listViewSmall显示，则不允许main2_stu_list响应触摸事件
                return listViewSmall.getVisibility() == View.VISIBLE;
            }
        });

        // 设置根布局的点击事件监听器
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断触摸点的位置是否在listViewSmall内
                int[] listViewPos = new int[2];
                listViewSmall.getLocationOnScreen(listViewPos);
                int listViewLeft = listViewPos[0];
                int listViewTop = listViewPos[1];
                int listViewRight = listViewLeft + listViewSmall.getWidth();
                int listViewBottom = listViewTop + listViewSmall.getHeight();

                float x = v.getX();
                float y = v.getY();

                if (x < listViewLeft || x > listViewRight || y < listViewTop || y > listViewBottom) {
                    // 点击的位置在listViewSmall以外，隐藏列表
                    hideListView();
                }
            }
        });



//        添加   main2_btn_add
        Button main2_btn_add = findViewById(R.id.main2_btn_add);
        main2_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 如果listView_small显示，则按钮不可操作
                hideListView();

                // 3. 在监听器中显示弹窗
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Main2_stu_list.this);
                LayoutInflater inflater = Main2_stu_list.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_layout, null);
                dialogBuilder.setView(dialogView);

                // 4. 获取输入数据
                final EditText editTextName = dialogView.findViewById(R.id.etName);
                final EditText editTextCourse = dialogView.findViewById(R.id.etCourse);
                final EditText editTextGender = dialogView.findViewById(R.id.etSex);
                final EditText editTextAge = dialogView.findViewById(R.id.etAge);
                final EditText editTextPhoneNumber = dialogView.findViewById(R.id.etNum);

                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editTextName.getText().toString();
                        String course = editTextCourse.getText().toString();
                        String gender = editTextGender.getText().toString();
                        int age = Integer.parseInt(editTextAge.getText().toString());
                        String phoneNumber = editTextPhoneNumber.getText().toString();

                        // 5. 执行保存操作
                        // 在这里执行将数据保存到数据库或进行其他处理逻辑的操作
                    }
                });

                dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击取消按钮，可以在这里执行取消操作，如果有的话
                    }
                });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }
        });




        // 加载字体文件
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "main2_stu_list.ttf");
        // 获取 textview 实例
        Button btn1 = findViewById(R.id.main2_btn_add);
        // 设置 typeface, 就是字体文件
        btn1.setTypeface(iconfont);

        TextView lastPage = findViewById(R.id.main2_btn_go_lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    new LoadPageTask().execute();
                }
            }
        });

        TextView nextPage = findViewById(R.id.main2_btn_go_nextPage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < totalPage) {
                    currentPage++;
                    new LoadPageTask().execute();
                }
            }
        });


        // 退出按钮
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        Button btn_back = findViewById(R.id.main2_btn_go_out);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewSmall.getVisibility() == View.VISIBLE) {
                    hideListView();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Main2_stu_list.this);
                    dialog.setMessage("退出将不能进行体测，确定退出吗？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                            System.exit(0);
                            exitApplication();
                        }
                    });

                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
            }
        });


        // 列表项
        // 第二步：绑定控件
        listView = findViewById(R.id.main2_stu_list);

        // 第三步：准备数据
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 发送HTTP请求获取JSON数据
                    URL url = new URL("https://transferapi.imreliable.com/hardware/child/list?page=1&limit=" + pageSize + "&type=1");
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
                    JSONArray listArray = json.getJSONObject("data").getJSONArray("list");
                    int anInt = json.getJSONObject("data").getInt("count");
                    if(anInt % 10 == 0) totalPage = anInt / pageSize;
                    else totalPage = anInt / pageSize + 1;
                    TextView viewById = findViewById(R.id.main2_load_page);
                    viewById.setText(currentPage + "/" + totalPage);


                    // 存储 "list" 数据的列表
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject item = listArray.getJSONObject(i);

                        // 将数据转换为 list_stu 对象
                        List_stu student = new List_stu();
                        student.setID(item.getInt("id"));
                        student.setChild_name(item.getString("child_name"));
                        student.setType(item.getInt("type"));
                        student.setGender(item.getInt("gender"));
                        student.setParent_phone(item.getString("parent_phone"));
                        student.setTag_id_list(item.getString("tag_id_list"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            student.setAge(Period.between(LocalDate.parse(item.getString("birthday")), LocalDate.now()).getYears());
                        }
                        JSONArray tag_listArray = item.getJSONArray("tag_list");
                        // 设置其他字段
                        String[] tag_listList = new String[tag_listArray.length()];
                        for (int j = 0; j < tag_listArray.length(); j++) {
                            tag_listList[j] = tag_listArray.getString(j);
                        }
                        student.setTag_list(tag_listList);
                        // 将对象添加到列表中
                        list.add(student);
                    }
                    originalList.addAll(list);

                    // 第四步：设计每一个列表项的子布局
                    // 第五步：定义适配器 控件 -桥梁-数据
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new List_stu_Adapter(Main2_stu_list.this, R.layout.list_stu_item, list);
                            adapter.setOnButtonClickListener(new List_stu_Adapter.OnButtonClickListener() {
                                @Override
                                public void onButtonClicked(int position, List_stu student) {
                                    // 创建Intent对象
                                    Intent intent = new Intent(Main2_stu_list.this, Main3_function.class);
                                    // 将孩子信息通过Intent传递到下一个页面
                                    intent.putExtra("student", student);
                                    // 启动下一个页面
                                    startActivity(intent);
                                }
                            });

                            listView.setAdapter(adapter);
                            listView = findViewById(R.id.main2_stu_list);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        // 列表中  去测试按钮
        Button btn_go = findViewById(R.id.main2_btn_not_stu);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideListView();
                Intent intent = new Intent(Main2_stu_list.this, Main3_function.class);
                startActivity(intent);
            }
        });


        // 列表中  设置  按钮
        Button btn_setup = findViewById(R.id.main2_btn_setup);
        btn_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewSmall.getVisibility() == View.VISIBLE) {
                    hideListView();
                } else {
                    Intent intent = new Intent(Main2_stu_list.this, Main_set.class);
                    startActivity(intent);
                }
            }
        });




        // 搜索框
        final EditText searchEditText = findViewById(R.id.main2_search_edit);
        // 设置搜索框的点击事件监听器
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listViewSmall.getVisibility() == View.VISIBLE) {
                    hideListView(); // 点击搜索框时隐藏listView_small
                }
                isFirstSearchBoxClick = false;
            }
        });

        // 设置搜索框的触摸事件监听器


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 在文本改变之前执行的逻辑，此处不需要操作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当文本改变时执行的逻辑
                String searchText = s.toString().trim();
                System.out.println(searchText);
//                if (searchText.length() >= 1) {
                filterList(searchText);
//                } else {
//                    adapter.updateData(originalList);
//                }
            }

            public void afterTextChanged(Editable s) {
            }
        });







//
    }

    private void filterList(String searchText) {

        // 重置 isFirstSearchBoxClick 为 true，允许再次隐藏 listView_small
        isFirstSearchBoxClick = true;

        // 如果listView_small显示，则不允许搜索框响应点击事件
        if (listViewSmall.getVisibility() == View.VISIBLE) {
            return;
        }

        List<List_stu> filteredList = new ArrayList<>();
        childName = searchText;

//        if (searchText.isEmpty()) {
//            // 输入为空时显示所有人,重新
//            new LoadPageTask().execute();
//        } else {
//            // 检索
//
//        }
        if (searchText.isEmpty()) {
            // 输入为空时重新加载数据
            currentPage = 1;
            System.out.println("标记");
            new LoadPageTask().execute();
        } else {
            // 进行搜索
            new LoadPageTask().execute();
        }
//        adapter.updateData(filteredList);
    }

    private class LoadPageTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            list.clear();
            // 在后台执行网络请求的逻辑
            try {
                // 发送HTTP请求获取JSON数据
                URL url;
                if(childName == null || childName.isEmpty()) {
                    System.out.println("111111");
                    url = new URL("https://transferapi.imreliable.com/hardware/child/list?page=" + currentPage + "&limit=" + pageSize + "&type=1");
                }else {
                    System.out.println("222222");
                    url = new URL("https://transferapi.imreliable.com/hardware/child/list?page=" + currentPage + "&limit=" + pageSize + "&type=1" + "&child_name=" + childName);
                }

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
                JSONArray listArray = json.getJSONObject("data").getJSONArray("list");
                int anInt = json.getJSONObject("data").getInt("count");
                if(anInt % 10 == 0) totalPage = anInt / pageSize;
                else totalPage = anInt / pageSize + 1;
                TextView viewById = findViewById(R.id.main2_load_page);
                viewById.setText(currentPage + "/" + totalPage);

                // 存储 "list" 数据的列表
                for (int i = 0; i < listArray.length(); i++) {
                    JSONObject item = listArray.getJSONObject(i);

                    // 将数据转换为 list_stu 对象
                    List_stu student = new List_stu();
                    student.setID(item.getInt("id"));
                    student.setChild_name(item.getString("child_name"));
                    student.setType(item.getInt("type"));
                    student.setGender(item.getInt("gender"));
                    student.setParent_phone(item.getString("parent_phone"));
                    student.setTag_id_list(item.getString("tag_id_list"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        student.setAge(Period.between(LocalDate.parse(item.getString("birthday")), LocalDate.now()).getYears());
                    }
                    JSONArray tag_listArray = item.getJSONArray("tag_list");
                    // 设置其他字段
                    String[] tag_listList = new String[tag_listArray.length()];
                    for (int j = 0; j < tag_listArray.length(); j++) {
                        tag_listList[j] = tag_listArray.getString(j);
                    }
                    student.setTag_list(tag_listList);
                    // 将对象添加到列表中
                    list.add(student);
                }
                originalList.addAll(list);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            // 更新UI的逻辑
            adapter = new List_stu_Adapter(Main2_stu_list.this, R.layout.list_stu_item, list);
            adapter.setOnButtonClickListener(new List_stu_Adapter.OnButtonClickListener() {
                @Override
                public void onButtonClicked(int position, List_stu student) {
                    // 创建Intent对象
                    Intent intent = new Intent(Main2_stu_list.this, Main3_function.class);
                    // 将孩子信息通过Intent传递到下一个页面
                    intent.putExtra("student", student);
                    // 启动下一个页面
                    startActivity(intent);
                }
            });

            listView.setAdapter(adapter);
        }
    }




//    列表加载
// 处理显示列表点击事件
//    public void onShowListClick(View view) {
//        if (listViewSmall.getVisibility() == View.VISIBLE) {
//            // 如果列表已经显示，则隐藏它
//            hideListView();
//        } else {
//            // 如果列表尚未显示，则显示它
//            showListView();
//        }
//    }

    // 隐藏列表
    private void hideListView() {
        listViewSmall.setVisibility(View.GONE);
    }

    // 显示列表
    private void showListView() {
        listViewSmall.setVisibility(View.VISIBLE);
    }

//    退出应用
    private void exitApplication() {
        // 清除所有Activity栈
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }





//
}
