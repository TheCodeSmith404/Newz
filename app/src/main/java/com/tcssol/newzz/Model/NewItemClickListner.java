package com.tcssol.newzz.Model;

public interface NewItemClickListner {
    public void OpenArticle(String link);
    public void shareArticle(String link);
    public void saveArticle(News news);
    public void deleteArticle(News news);
}
