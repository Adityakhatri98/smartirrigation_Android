package com.example.smartcrop;

class ApiError {
    String code;
    String message;

    public ApiError(String conflict, String message) {
        super();
        this.code = conflict;
        this.message = message;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMes() {
        return message;
    }
    public void setMes(String mes) {
        this.message = mes;
    }
}
