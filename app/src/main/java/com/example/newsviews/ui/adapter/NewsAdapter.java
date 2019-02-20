package com.example.newsviews.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.newsviews.databinding.NewsItemViewBinding;
import com.example.newsviews.service.model.Articles;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    // Member variable to handle item clicks
    private List<Articles> mArticlesList;
    public NewsAdapter() {

    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        NewsItemViewBinding newsItemViewBinding =
                NewsItemViewBinding.inflate(inflater, parent, false);

        return new NewsViewHolder(newsItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mArticlesList == null) {
            return 0;
        }
        return mArticlesList.size();
    }

    public void setArticlesList(List<Articles> mArticlesList) {
        if (mArticlesList != null) {
            this.mArticlesList = mArticlesList;
            notifyDataSetChanged();
        }

    }

    public List<Articles> getArticlesList() {
        return mArticlesList;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        private final NewsItemViewBinding mNewsBiding;
        public NewsViewHolder(NewsItemViewBinding mNewsBiding) {
            super(mNewsBiding.getRoot());
            this.mNewsBiding = mNewsBiding;
        }

        void bind(int position) {
            mNewsBiding.titleText.setText(mArticlesList.get(position).getTitle());
            mNewsBiding.authorText.setText(mArticlesList.get(position).getAuthor());
            mNewsBiding.descriptionText.setText(mArticlesList.get(position).getDescription());
            mNewsBiding.publisTimeText.setText(mArticlesList.get(position).getPublishedAt());
            Picasso
                    .get()
                    .load(mArticlesList.get(position).getUrlToImage())
                    .into(mNewsBiding.contentImage);
        }

    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }
}
