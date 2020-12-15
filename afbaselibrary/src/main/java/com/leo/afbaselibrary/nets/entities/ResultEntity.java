package com.leo.afbaselibrary.nets.entities;

import com.google.gson.JsonElement;

/**
 * created by arvin on 16/10/24 17:25
 * email：1035407623@qq.com
 */
public class ResultEntity {

    /**
     * 状态码
     */
    private int error_code;
    /**
     * 处理消息
     */
    private String reason;
    /**
     * 内容
     */
    private JsonElement result;

    @Override
    public String toString() {
        return "ResultEntity{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }

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
