package com.example.newsviews.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsviews.BuildConfig;
import com.example.newsviews.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {
    public AboutFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Element versionElement = new Element();
        versionElement.setTitle("Version: " + BuildConfig.VERSION_NAME);
        Element appTitle = new Element();
        appTitle.setTitle(getString(R.string.app_name));

        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .setImage(R.drawable.news_views_144dp)
                .addItem(appTitle)
                .addItem(versionElement)
                .setDescription(getString(R.string.app_description))
                .create();

        return aboutPage;
    }
}
