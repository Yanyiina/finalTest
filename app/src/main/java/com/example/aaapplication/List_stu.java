package com.example.aaapplication;


import java.io.Serializable;

//列表  构造类
public class List_stu  implements Serializable {
    private Integer ID;
    private String child_name;
    private Integer type;
    private Integer gender;
    private Integer age;
    // 身高 体重
    private Double height;
    private Double weight;
    // 保存正面体态识别结果
    private String positive_points;
    private String positive_height;
    private String positive_img;
    // 保存侧面体资体态相关结果
    private String side_img;
    private String side_height;
    private String side_points;

    public String getPositive_points() {
        return positive_points;
    }

    public void setPositive_points(String positive_points) {
        this.positive_points = positive_points;
    }

    public String getPositive_height() {
        return positive_height;
    }

    public void setPositive_height(String positive_height) {
        this.positive_height = positive_height;
    }

    public String getPositive_img() {
        return positive_img;
    }

    public void setPositive_img(String positive_img) {
        this.positive_img = positive_img;
    }

    public String getSide_img() {
        return side_img;
    }

    public void setSide_img(String side_img) {
        this.side_img = side_img;
    }

    public String getSide_height() {
        return side_height;
    }

    public void setSide_height(String side_height) {
        this.side_height = side_height;
    }

    public String getSide_points() {
        return side_points;
    }

    public void setSide_points(String side_points) {
        this.side_points = side_points;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

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

    public List_stu(Integer ID, String child_name, Integer type, Integer gender, Integer age, Double height, Double weight, String positive_points, String positive_height, String positive_img, String side_img, String side_height, String side_points, String parent_phone, String tag_id_list, String[] tag_list) {
        this.ID = ID;
        this.child_name = child_name;
        this.type = type;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.positive_points = positive_points;
        this.positive_height = positive_height;
        this.positive_img = positive_img;
        this.side_img = side_img;
        this.side_height = side_height;
        this.side_points = side_points;
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
