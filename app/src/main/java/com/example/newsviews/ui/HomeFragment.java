package com.example.newsviews.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeFragment extends Fragment implements NewsAdapter.ItemClickListener{
    private FragmentHomeBinding fragmentHomeBinding;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private NewsAdapter mNewsAdapter;
    private NewsRepository newsRepository;
    private static final String API_SOURCE = "espn-cric-info";
    private static final String API_KEY = "1d99e842d6ca40c0b249d256d07e463d";
    private Toast mToast;

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

        mNewsAdapter = new NewsAdapter(this);
        fragmentHomeBinding.rvNews.setAdapter(mNewsAdapter);
        fragmentHomeBinding.progressBarHome.setVisibility(View.VISIBLE);

        newsRepository = NewsRepository.getInstance();

        if (((MainActivity)getActivity()).isNetworkConnected()) retrieveNews();
        else showDialog();

        fragmentHomeBinding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNewsAdapter.setArticlesList(null);
                fragmentHomeBinding.progressBarHome.setVisibility(View.VISIBLE);
                retrieveNews();
            }
        });

        return rootView;
    }

    private void showDialog() {
        //fragmentHomeBinding.progressBarHome.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_iternet)
                .setTitle(R.string.warning_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (((MainActivity)getActivity()).isNetworkConnected()) retrieveNews();
                        else showDialog();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void retrieveNews() {
        LiveData<News> newsLiveData = newsRepository.getNewsList(API_SOURCE, API_KEY);
        newsLiveData.observe(this, new Observer<News>() {
            @Override
            public void onChanged(News news) {
                Log.d(TAG, "Receiving database update from LiveData");
                mNewsAdapter.setArticlesList(news.getArticles());
                fragmentHomeBinding.progressBarHome.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId, String url) {
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item " + itemId + " Clicked";
        mToast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT);
        mToast.show();

        fireWebShowDialog(url, itemId);

    }

    private void fireWebShowDialog(final String url, int  itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.web_title))
                .setPositiveButton("Browser", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browseIntent = new Intent(Intent.ACTION_VIEW);
                        browseIntent.setData(Uri.parse(url));
                        startActivity(browseIntent);


                    }
                })
                .setNegativeButton("Application", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("URL_LINK", url);
                        Log.d(TAG, url);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
