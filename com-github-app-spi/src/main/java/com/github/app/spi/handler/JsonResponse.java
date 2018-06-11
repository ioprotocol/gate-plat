package com.github.app.spi.handler;

import io.netty.util.Recycler;

public final class JsonResponse {
    /**
     * api operation success
     */
    public static final int CODE_SUCCESS = 0;
    /**
     * JWT token illegal
     */
    public static final int CODE_JWT_TOKEN_INVALIDATE = -1;
    /**
     * JWT token timeout
     */
    public static final int CODE_JWT_TOKEN_TIMEOUT = -2;
    /**
     * JWT token not filled into http headers with name {X-Token}
     */
    public static final int CODE_JWT_TOKEN_MISSING = -3;
    /**
     * This user have login
     */
    public static final int CODE_USER_HAVE_LOGINED = -4;
    /**
     * api operation failed
     */
    public static final int CODE_API_OPERATION_FAILED = -5;
    /**
     * api operation authentication failed
     */
    public static final int CODE_API_AUTHENTICATION_FAILED = -6;

    private int code;
    private String msg;
    private Object data;

    public static JsonResponse create(int code) {
        JsonResponse response = RECYCLER.get();
        response.setCode(code);
        return response;
    }

    public static JsonResponse create(int code, String msg) {
        JsonResponse response = RECYCLER.get();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static JsonResponse create(int code, String msg, Object data) {
        JsonResponse response = RECYCLER.get();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    private JsonResponse(Recycler.Handle<JsonResponse> recyclerHandle) {
        this.recyclerHandle = recyclerHandle;
    }

    private final Recycler.Handle<JsonResponse> recyclerHandle;
    private static final Recycler<JsonResponse> RECYCLER = new Recycler<JsonResponse>() {
        @Override
        protected JsonResponse newObject(Handle<JsonResponse> handle) {
            return new JsonResponse(handle);
        }
    };

    void recycle() {
        msg = null;
        code = 0;
        data = null;
        recyclerHandle.recycle(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
