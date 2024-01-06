package com.example.xiaopu.bean;

import java.io.Serializable;

public class Talks implements Serializable {

    private String id;
    private String type;
    private String content;
    private String uid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Talks{" +
                "id=" + id +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", uid=" + uid +
                '}';
    }
}
