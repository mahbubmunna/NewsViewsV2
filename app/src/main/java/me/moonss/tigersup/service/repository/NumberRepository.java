package me.moonss.tigersup.service.repository;

import android.content.Context;
import android.util.Log;

import me.moonss.tigersup.service.model.Number;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NumberRepository {
    private NumberApiService numberApiService;
    private static NumberRepository sInstance;
    private static final Object LOCK = new Object();
    private static final String LOG_TAG = NumberApiService.class.getSimpleName();

    public static NumberRepository getInstance(Context context)
    {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating Number Repo");
                sInstance = new NumberRepository();
            }
        }
        return sInstance;
    }

    private NumberRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NumberApiService.HTTPS_API_NUMBER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        numberApiService = retrofit.create(NumberApiService.class);
    }

    public LiveData<Number> getNumberTrivia(final String number) {
        final MutableLiveData<Number> numberData = new MutableLiveData<>();
        numberApiService.getNumberTrivia(number).enqueue(new Callback<Number>() {
            @Override
            public void onResponse(Call<Number> call, Response<Number> response) {
                numberData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Number> call, Throwable t) {
                numberData.setValue(null);
            }
        });
        return numberData;
    }
}
