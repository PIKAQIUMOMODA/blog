package com.zcy.blog.enumerate;

public enum Result {
    SUCCESS("成功",1),
    FAILED("失败",2);
     private String message;
     private int state;

    Result(String message, int state) {
        this.message = message;
        this.state = state;
    }

    @Override
    public String toString() {
        return "{" +
                "\"message\":\"" + message + '\"' +
                ", \"state\":" + state +
                '}';
    }
}
