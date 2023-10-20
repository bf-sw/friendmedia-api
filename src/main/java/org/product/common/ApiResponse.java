package org.product.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse<T> {
    @JsonProperty("status")
    private long status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("redirect")
    private String redirect = "";

    @JsonProperty("data")
    private T t;

    public ApiResponse(long status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(ResponseStatus response) {
        this.status = response.getStatus();
        this.message = response.getMessage();
    }

    public ApiResponse(ResponseStatus response, String message) {
        this.status = response.getStatus();
        this.message = message;
    }

    public ApiResponse(ResponseStatus response, String message, T t) {
        this.status = response.getStatus();
        this.message = message;
        this.t = t;
    }

    public static ApiResponse ok() {
        return new ApiResponse(ResponseStatus.SUCCESS, "ok");
    }

    public static ApiResponse ok(Object object) {
        return new ApiResponse(ResponseStatus.SUCCESS, "ok", object);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(ResponseStatus.ERROR, message);
    }

    public static ApiResponse error(long status, String message) {
        return new ApiResponse(status, message);
    }

    public static ApiResponse error(ResponseStatus response) {
        return new ApiResponse(response);
    }

    public static ApiResponse none_auth() {
        return new ApiResponse(ResponseStatus.NONE_AUTH);
    }
}
