package com.example.uplaod;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @PROJECT_NAME: finalTest
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/7/24 9:07
 */
public class Result {

    @JsonProperty("project_id")
    private int projectId;

    @JsonProperty("measured_result")
    private int measuredResult;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getMeasuredResult() {
        return measuredResult;
    }

    public void setMeasuredResult(int measuredResult) {
        this.measuredResult = measuredResult;
    }
}
