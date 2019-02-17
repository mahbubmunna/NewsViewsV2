package com.example.newsviews.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsviews.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SplashScreenAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SplashScreenAdapter(Context context) {
        this.context = context;
    }

    //Array of screen layouts
    public int[] layouts={
            R.layout.splash_screen_one,
            R.layout.splash_screen_two,
            R.layout.splash_screen_three,
    };


    // Returns total number of screens
    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==  object;
    }

    // Returns the layout at the given position of the view pager

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view= layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);

        return view;
    }

    // stops slide on last screen
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
