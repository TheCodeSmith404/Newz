package com.tcssol.newzz.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tcssol.newzz.Model.News;

import java.util.List;

@Dao
public interface SavedDao {
    @Insert
    void insertNews(News news);

    @Query("DELETE FROM saved_articles")
    void deleteAll();
    @Query("SELECT * FROM saved_articles ORDER BY id ASC")
    LiveData<List<News>> getAllNews();

    @Query("SELECT * FROM saved_articles WHERE saved_articles.id=:id")
    LiveData<News> get(long id);
    @Delete
    void delete(News news);
}
