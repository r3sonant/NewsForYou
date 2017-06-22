package com.weirdresonance.android.newsforyou;

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
    private List<News> newsList;

    // Declare ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView newsImage;
        public TextView newsTitle, newsAuthor, newsUrl;


        public MyViewHolder(View view) {
            super(view);
            newsImage = (ImageView) view.findViewById(R.id.newsImageView);
            newsTitle = (TextView) view.findViewById(R.id.newsTitleView);
            newsAuthor = (TextView) view.findViewById(R.id.newsAuthorView);
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
        holder.newsTitle.setText(news.getNewsTitle());
        holder.newsAuthor.setText(news.getNewsAuthor());
        //holder.newsUrl.setText(news.getNewsUrl());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


}
