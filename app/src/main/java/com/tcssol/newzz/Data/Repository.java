package com.tcssol.newzz.Data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.tcssol.newzz.Model.News;
import com.tcssol.newzz.Model.ResponseWrapper;
import com.tcssol.newzz.Network.ApiService;
import com.tcssol.newzz.Network.ClientInstance;

import java.io.IOException;
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

    public void getTopHeadlines(String[] lang,String[] countryCode, final OnArticlesFetchedListener listener) {
        Call<ResponseWrapper> call = apiService.getTopHeadlines("24h",lang,countryCode);
        executeCall(call, listener);
    }

    public void getEverything(String query,String[] language,String[] country,String topic, final OnArticlesFetchedListener listener) {
        Call<ResponseWrapper> call = apiService.getEverything(query,language,country,topic);
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
                    listener.onArticlesFetched(responseWrapper.getArticles());
                } else {
                    Log.d("Load Data","Failed "+response.code()+response.message());
                    Log.d("Load Data","Failed "+response.code()+response.errorBody().toString());
                    String errorText;
                    try {
                        errorText=response.errorBody().string();
                    } catch (IOException e) {
                        errorText="UnKnown Error";
                    }
                    listener.onError(errorText);
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Log.d("Load Data","Network Error"+t.getMessage());
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
