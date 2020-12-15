package com.leo.afbaselibrary.nets.entities;

import com.google.gson.JsonElement;

/**
 * created by czh on 16/12/5 10:15
 * email：1632365610@qq.com
 */
public class BBDResultEntity {
    private int error_code;
    private String reason;
    private JsonElement result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }
}
