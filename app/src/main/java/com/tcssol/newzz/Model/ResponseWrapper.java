package com.tcssol.newzz.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseWrapper {
    @SerializedName("status")
    private String status;

    @SerializedName("articles")
    private List<News> articles;

    public ResponseWrapper(String status, List<News> articles) {
        this.status = status;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }
}
