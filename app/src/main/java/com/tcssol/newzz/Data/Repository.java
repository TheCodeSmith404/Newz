package com.tcssol.newzz.Data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.tcssol.newzz.Model.News;
import com.tcssol.newzz.Model.ResponseWrapper;
import com.tcssol.newzz.Network.ApiService;
import com.tcssol.newzz.Network.ClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private ApiService apiService;
    private  LiveData<List<News>> allNews;
    private SavedDao savedDao;
    public Repository(Application application){
        SavedRoomDatabase savedRoomDatabase=SavedRoomDatabase.getDatabase(application);
        this.savedDao=savedRoomDatabase.savedDao();
        this.allNews= savedDao.getAllNews();
    }

    public Repository() {
        apiService = ClientInstance.getRetrofitInstance().create(ApiService.class);
    }

    public void getTopHeadlines(String countryCode, final OnArticlesFetchedListener listener) {
        Call<ResponseWrapper> call = apiService.getTopHeadlines(countryCode,"060d499abb00497ba27e0e487c2a98a4");
        executeCall(call, listener);
    }

    public void getEverything(String query,String fromDate,String sortBy, final OnArticlesFetchedListener listener) {
        Call<ResponseWrapper> call = apiService.getEverything(query,fromDate,sortBy,"060d499abb00497ba27e0e487c2a98a4");
        Log.d("Load Data","Getting Everything");
        executeCall(call, listener);
    }

    private void executeCall(Call<ResponseWrapper> call, final OnArticlesFetchedListener listener) {
        Log.d("Load Data","Executing Call");
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.isSuccessful()) {
                    Log.d("Load Data","Success");
                    ResponseWrapper responseWrapper = response.body();
                    listener.onArticlesFetched(responseWrapper.getNews());
                } else {
                    Log.d("Load Data","Failed "+response.code()+response.errorBody().toString());
                    listener.onError("Failed to fetch articles");
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Log.d("Load Data","Network Error");
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }
    public LiveData<List<News>> getAllNews(){
        return allNews;
    }

    public void insert(News news){
        SavedRoomDatabase.databaseWriterExecutor.execute(()-> savedDao.insertNews(news));
    }
    public LiveData<News> get(long id){return savedDao.get(id);}
    public void delete(News news){
        SavedRoomDatabase.databaseWriterExecutor.execute(()->savedDao.delete(news));
    }
    public SavedDao getSavedDao() {
        return savedDao;
    }

    public interface OnArticlesFetchedListener {
        void onArticlesFetched(List<News> articles);
        void onError(String errorMessage);
    }
}
