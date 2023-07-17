//列表适配器
package com.example.aaapplication;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class List_stu_Adapter extends ArrayAdapter<List_stu> {

    public interface OnButtonClickListener {
        void onButtonClicked(int position, List_stu list);
    }

    private OnButtonClickListener buttonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public void updateData(List<List_stu> newData) {
        clear();
        addAll(newData);
        notifyDataSetChanged();
    }

    public List_stu_Adapter(Context context, int resource, List<List_stu> objects) {
        super(context, resource, objects);
    }
    //每个子项被滚动到屏幕内的时候会被调用
    // 将position final掉
    public View getView(final int position, View convertView, ViewGroup parent) {

        final List_stu list = getItem(position); // 得到当前项的 List_stu 实例
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItemView = inflater.inflate(R.layout.list_stu_item, parent, false);
        }

        // 设置列表项之间的间距
//        int spacing = 16; // 设置你想要的间距大小，单位为像素
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) listItemView.getLayoutParams();
////        listItemView.setPadding(spacing, spacing, spacing, spacing);
//        params.setMargins(spacing, spacing, spacing, spacing);

        TextView stu_name = listItemView.findViewById(R.id.stu_name);
        TextView stu_course = listItemView.findViewById(R.id.stu_course);
        TextView stu_sex = listItemView.findViewById(R.id.stu_sex);
        TextView stu_age = listItemView.findViewById(R.id.stu_age);
        TextView stu_num = listItemView.findViewById(R.id.stu_num);

        stu_name.setText(list.getChild_name());
        stu_course.setText(list.getTag_list().length != 0 ? list.getTag_list()[0] : "空");
        stu_sex.setText((list.getGender() == 1) ? "男" : "女");
        stu_age.setText(list.getAge() + "岁");
        stu_num.setText(list.getParent_phone() != "null" ? list.getParent_phone() : "空");

        Button stu_go = listItemView.findViewById(R.id.stu_go);
        stu_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClicked(position, list);
                }
            }
        });
        return listItemView;
    }


}
