package com.example.aaapplication;


import java.io.Serializable;

//列表  构造类
public class List_stu  implements Serializable {
    private Integer ID;
    private String child_name;
    private Integer type;
    private Integer gender;
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    private String parent_phone;
    private String tag_id_list;
    private String[] tag_list;

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public List_stu(Integer ID, String child_name, Integer type, Integer gender, Integer age, String parent_phone, String tag_id_list, String[] tag_list) {
        this.ID = ID;
        this.child_name = child_name;
        this.type = type;
        this.gender = gender;
        this.age = age;
        this.parent_phone = parent_phone;
        this.tag_id_list = tag_id_list;
        this.tag_list = tag_list;
    }

    public List_stu() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParent_phone() {
        return parent_phone;
    }

    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }

    public String getTag_id_list() {
        return tag_id_list;
    }

    public void setTag_id_list(String tag_id_list) {
        this.tag_id_list = tag_id_list;
    }

    public String[] getTag_list() {
        return tag_list;
    }

    public void setTag_list(String[] tag_list) {
        this.tag_list = tag_list;
    }

    @Override
    public String toString() {
        return "List_stu{" +
                "ID=" + ID +
                ", child_name='" + child_name + '\'' +
                ", type=" + type +
                ", parent_phone='" + parent_phone + '\'' +
                ", tag_id_list='" + tag_id_list + '\'' +
                ", tag_list=" + tag_list +
                '}';
    }
}
