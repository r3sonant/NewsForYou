package com.weirdresonance.android.newsforyou;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Stephen.Pierce on 22/06/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Log messages tag
     */
    private static final String LOG_TAG = NewsLoader.class.getName();
    private final Context context;
    private final String url;


    public NewsLoader(Context context, String url) {
        super(context);
        this.context = context;
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }

        // Run the network request, parse the response, and extract a list of books.
        List<News> news = QueryUtils.fetchNewsData(url);
        return news;
    }
}




