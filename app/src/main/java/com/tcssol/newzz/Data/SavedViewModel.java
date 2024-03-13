package com.tcssol.newzz.Data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tcssol.newzz.Model.News;

import java.util.List;

public class SavedViewModel extends AndroidViewModel {
    public static Repository repository;
    public final LiveData<List<News>> allNews;


    public SavedViewModel(@NonNull Application application) {
        super(application);
        repository=new Repository(application);
        allNews=repository.getAllNews();
    }

    public LiveData<List<News>> getAllSavedNews(){return allNews;}
    public static void insert(News news){repository.insert(news);}
    public LiveData<News> get(long id){return repository.get(id);}
    public static void delete(News news){repository.delete(news);}
    public SavedDao getSavedNewsDao() {
        return repository.getSavedDao();
    }
}
