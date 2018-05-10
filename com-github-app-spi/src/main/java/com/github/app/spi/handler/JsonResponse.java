package com.github.app.spi.handler;

public class JsonResponse {
    private Integer code;
    private String msg;
    private Object data;

    public JsonResponse() {
    }

    public JsonResponse(Integer code) {
        this.code = code;
    }

    public JsonResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public JsonResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JsonResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JsonResponse setData(Object data) {
        this.data = data;
        return this;
    }
}
