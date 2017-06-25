package com.weirdresonance.android.newsforyou;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * NewsAdapter class
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    public static List<News> newsList;

    // Declare ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView newsImage;
        public TextView newsTitle, newsSection, newsPublishedDate;
        public Uri newsUrl;


        public MyViewHolder(View view) {
            super(view);
            newsImage = (ImageView) view.findViewById(R.id.newsImageView);
            newsTitle = (TextView) view.findViewById(R.id.newsTitleView);
            newsSection = (TextView) view.findViewById(R.id.newsSectionView);
            newsPublishedDate = (TextView) view.findViewById(R.id.newsPublishedDate);
        }
    }


    // News Adapter
    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    // Bind
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsImage.setImageBitmap(news.getNewsImage());
        holder.newsTitle.setText(news.getNewsTitle());
        holder.newsSection.setText(news.getNewsSection());
        holder.newsPublishedDate.setText(news.getNewsPublishedDate());
        holder.newsUrl.parse(news.getNewsUrl());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


}
