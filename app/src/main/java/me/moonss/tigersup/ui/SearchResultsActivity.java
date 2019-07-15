package me.moonss.tigersup.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.newsviews.R;
import com.example.newsviews.databinding.ActivitySearchResultBinding;
import me.moonss.tigersup.service.model.Number;
import me.moonss.tigersup.service.repository.NumberRepository;

public class SearchResultsActivity extends AppCompatActivity {
    private NumberRepository mNumRepo;
    private ActivitySearchResultBinding mBinding;
    private final static String TAG = SearchResultsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_result);
        mNumRepo = NumberRepository.getInstance(getApplicationContext());
        mBinding.progressBarSearchView.setVisibility(View.VISIBLE);
        mBinding.resultText.setVisibility(View.GONE);


        handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            final LiveData<Number> number = mNumRepo.getNumberTrivia(query);
            number.observe(this, new Observer<Number>() {
                @Override
                public void onChanged(Number number) {
                    Log.d(TAG, "Getting Live data through observer");
                    mBinding.progressBarSearchView.setVisibility(View.GONE);
                    if (number != null) {
                        mBinding.resultText.setVisibility(View.VISIBLE);
                        mBinding.resultText.setText(number.text);
                    } else {
                        mBinding.resultText.setVisibility(View.VISIBLE);
                        mBinding.resultText.setText("Please try again in right way");
                    }
                }
            });


        }
    }

}
