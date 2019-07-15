package me.moonss.tigersup.service.repository;

import android.util.Log;

import me.moonss.tigersup.service.model.News;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRepository {
    private NewsApiService newsApiService;
    private static NewsRepository sInstance;
    private static final Object LOCK = new Object();
    private static final String LOG_TAG = NumberApiService.class.getSimpleName();

    public static NewsRepository getInstance() {

        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating Number Repo");
                sInstance = new NewsRepository();
            }
        }
        return sInstance;
    }

    private NewsRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsApiService.HTTPS_API_NEWS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsApiService = retrofit.create(NewsApiService.class);
    }

    public LiveData<News> getNewsList(final String source, final String apiKey) {
         final MutableLiveData<News> news = new MutableLiveData<>();

            newsApiService.getLatestNews(source, apiKey).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    news.setValue(response.body());
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    news.setValue(null);
                }
            });

        return news;
    }
}
