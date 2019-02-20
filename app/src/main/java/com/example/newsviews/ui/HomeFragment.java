package com.example.newsviews.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsviews.R;
import com.example.newsviews.databinding.FragmentHomeBinding;
import com.example.newsviews.service.model.News;
import com.example.newsviews.service.repository.NewsRepository;
import com.example.newsviews.ui.adapter.NewsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding fragmentHomeBinding;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private NewsAdapter mNewsAdapter;
    private NewsRepository newsRepository;
    private static final String API_SOURCE = "espn-cric-info";
    private static final String API_KEY = "1d99e842d6ca40c0b249d256d07e463d";

    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home,
                container,
                false);
        View rootView = fragmentHomeBinding.getRoot();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentHomeBinding.rvNews.setLayoutManager(layoutManager);
        fragmentHomeBinding.rvNews.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter();
        fragmentHomeBinding.rvNews.setAdapter(mNewsAdapter);

        newsRepository = NewsRepository.getInstance();

        retrieveNews();

        return rootView;
    }

    private void retrieveNews() {
        LiveData<News> newsLiveData = newsRepository.getNewsList(API_SOURCE, API_KEY);
        newsLiveData.observe(this, new Observer<News>() {
            @Override
            public void onChanged(News news) {
                Log.d(TAG, "Receiving database update from LiveData");
                mNewsAdapter.setArticlesList(news.getArticles());
            }
        });
    }

}
