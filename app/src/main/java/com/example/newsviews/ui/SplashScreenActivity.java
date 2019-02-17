package com.example.newsviews.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.newsviews.R;
import com.example.newsviews.databinding.ActivitySplashScreenBinding;
import com.example.newsviews.utilities.PrefManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private SplashScreenAdapter splashScreenAdapter;
    private TextView[] mDots;
    private PrefManager prefManager;
    private int currentItem;
    ActivitySplashScreenBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Setting the splash theme before onCreate of parent class
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstLaunch()) {
            launchMainScreen();// goto main activity and finish current
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        transparentStatusBar();
        setClickListener();// set on click listeners
        setupViewPager();// setup the viewpager, set adapter and page listener
        addDotsIndicator(0);// called for the first launch, after this handled in page listener


    }


    //Making Status bar transparent
    private void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().
                    getDecorView().
                    setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    //Main Screen Launcher
    private void launchMainScreen() {
        prefManager.setIsFirstLaunch(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //Setting onClickListener
    private void setClickListener() {
        mBinding.btnSkip.setOnClickListener(this);
        mBinding.btnNext.setOnClickListener(this);
    }

    //Setup the view Pager
    private void setupViewPager() {
        splashScreenAdapter = new SplashScreenAdapter(this);
        mBinding.viewPager.setAdapter(splashScreenAdapter);
        mBinding.viewPager.addOnPageChangeListener(pageChangeListener);
    }

    //Listener Reference of ViewPager
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            currentItem = position; // currentItem used to get current position and then increase position on click on next button
            addDotsIndicator(position);
            // change the next button text to "next" / "finish"
            if (position == splashScreenAdapter.getCount() - 1) {
                // last page, make it "finish" and make the skip button invisible
                mBinding.btnNext.setText(getString(R.string.finish));
                mBinding.btnSkip.setVisibility(View.INVISIBLE);
            } else {
                // not last page, set "next" text and make skip button visible
                mBinding.btnNext.setText(getString(R.string.next));
                mBinding.btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    public void addDotsIndicator(int position) {
        // Adding TetView dynamically
        mDots = new TextView[splashScreenAdapter.getCount()];
        /* Remove previous views when called next time
        if not called then views will keep on adding*/
        mBinding.layoutDots.removeAllViews();
        // Set bullets in each dot text view
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("•"));// Html code for bullet
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.dot_inactive_color));
            mBinding.layoutDots.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            // change color of the current selected dot
            mDots[position].setTextColor(getResources().getColor(R.color.dot_active_color));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (currentItem < splashScreenAdapter.getCount() - 1) {
                    ++currentItem; // increase the value by 1
                    mBinding.viewPager.setCurrentItem(currentItem); // set the layout at next position
                } else
                    launchMainScreen(); // launch main screen on last page
                break;
            case R.id.btn_skip:
                launchMainScreen();
                break;
        }
    }
}


