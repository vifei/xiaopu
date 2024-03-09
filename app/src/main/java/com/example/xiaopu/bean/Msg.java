package com.example.xiaopu.bean;

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "content='" + content + '\'' +
                ", type=" + type +
                '}';
    }

    private String content;
    private final int type;

    public Msg(String content,int type){
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}