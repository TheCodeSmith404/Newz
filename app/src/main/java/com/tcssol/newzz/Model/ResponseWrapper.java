package com.tcssol.newzz.Model;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseWrapper {
    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<News> news;

    public ResponseWrapper(String status, int totalResults, List<News> news) {
        this.status = status;
        this.totalResults = totalResults;
        this.news = news;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
