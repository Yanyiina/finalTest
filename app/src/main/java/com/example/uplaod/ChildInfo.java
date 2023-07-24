package com.example.uplaod;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @PROJECT_NAME: finalTest
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/7/24 9:06
 */
public class ChildInfo {
    @JsonProperty("child_id")
    private Integer childId;

    private Master master;

    private Result[] result;

    // 构造函数、getter和setter方法省略


    public long getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Result[] getResult() {
        return result;
    }

    public void setResult(Result[] result) {
        this.result = result;
    }
}
