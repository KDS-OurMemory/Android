package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("result") private int result;
    @SerializedName("time") private String time;
    //@SerializedName("body") private String body;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /*public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }*/
}
