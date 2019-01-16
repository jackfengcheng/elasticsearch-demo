package com.xwtech.base;

/**
 * Created by admini on 2019/1/15.
 */
public class ApiResponse {

    private Integer code;
    private String message;
    private Object data;

    public ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public enum Status {
        SUCCESS(200, "OK"),
        BAD_REQUEST(400, "Bdd request"),
        INTERNAL_SERVER_ERROR(500, "Unkonwn Internal Error"),
        NOT_VALID_PARAM(40005, "not valid param"),
        NOT_SUPPORTED_OPERTION(40000, " opertion not  supported"),
        NOT_LOGIN(50000, "Not login");

        private int code;
        private String standarMesssage;

        Status(int code, String standarMesssage) {
            this.code = code;
            this.standarMesssage = standarMesssage;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStandarMesssage() {
            return standarMesssage;
        }

        public void setStandarMesssage(String standarMesssage) {
            this.standarMesssage = standarMesssage;
        }
    }
}
