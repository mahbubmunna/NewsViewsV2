package me.moonss.tigersup.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newsviews.R;
import com.example.newsviews.databinding.FragmentHomeBinding;
import me.moonss.tigersup.service.model.News;
import me.moonss.tigersup.service.repository.NewsRepository;
import me.moonss.tigersup.ui.adapter.NewsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeFragment extends Fragment implements NewsAdapter.ItemClickListener {
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

        mNewsAdapter = new NewsAdapter(this);
        fragmentHomeBinding.rvNews.setAdapter(mNewsAdapter);
        fragmentHomeBinding.progressBarHome.setVisibility(View.VISIBLE);

        newsRepository = NewsRepository.getInstance();

        if (((NewsDisplayActivity)getActivity()).isNetworkConnected()) retrieveNews();
        else showNetworkError();

        fragmentHomeBinding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragmentHomeBinding.refresher.setRefreshing(false);
                fragmentHomeBinding.rvNews.setVisibility(View.INVISIBLE);
                fragmentHomeBinding.progressBarHome.setVisibility(View.VISIBLE);
                mNewsAdapter.setArticlesList(null);
                retrieveNews();
            }
        });

        return rootView;
    }

    private void showNetworkError() {
        //fragmentHomeBinding.progressBarHome.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_iternet)
                .setTitle(R.string.warning_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (((NewsDisplayActivity)getActivity()).isNetworkConnected()) retrieveNews();
                        else showNetworkError();
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
                if (news != null) {
                    mNewsAdapter.setArticlesList(news.getArticles());
                    fragmentHomeBinding.rvNews.setVisibility(View.VISIBLE);
                    fragmentHomeBinding.progressBarHome.setVisibility(View.GONE);
                } else {
                    showNetworkError();
                }
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId, String url) {


        fireWebShowDialog(url, itemId);

    }

    private void fireWebShowDialog(final String url, int  itemId) {


        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.setShowTitle(true);
        builder.setStartAnimations(getActivity(), R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
        builder.setExitAnimations(getActivity(), R.anim.fui_slide_out_left, R.anim.fui_slide_in_right);

        CustomTabsIntent customTabsIntent = builder.build();


        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));

    }

}
