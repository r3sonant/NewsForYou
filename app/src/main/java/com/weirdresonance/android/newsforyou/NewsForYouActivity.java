package com.weirdresonance.android.newsforyou;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;

public class NewsForYouActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final String LOG_TAG = NewsForYouActivity.class.getName();

    /**
     * Test URL for Google Books API data
     */
    private static final String NEWS_REQUEST_URL =
            "https://content.guardianapis.com";
            //"https://content.guardianapis.com/search?&api-key=test&show-fields=thumbnail";
            //"https://content.guardianapis.com/search?q=politics&api-key=test&show-fields=thumbnail";
            //"http://content.guardianapis.com/search?q=politics&api-key=test";
            //"http://content.guardianapis.com/search?q=debates&api-key=test";
            //"https://www.googleapis.com/books/v1/volumes?q=";

    private static final String NEWS_REQUEST_URL_END = "api-key=test&show-fields=thumbnail";
    /**
     * TextView that is displayed when the list doesn't contain any books.
     */
    private TextView emptyNewsListView;

    /**
     * ListView that will contain the list of books.
     */
    private ListView newsListView;



    /**
     * Constant for the book loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    public static List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_for_you);

        callLoader(NEWS_REQUEST_URL);
    }


    /**
     * Calls the loader after a search term has been entered in the search bar.
     *
     * @param searchString
     */
    private void callLoader(String searchString) {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        newsAdapter = new NewsAdapter(newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(newsAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                News news = newsList.get(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(news.getNewsUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        // Restart the loader to clear it if a search has already been carried out
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);

        // Call FormatURL() to replace spaces with + and append the base URL to the search string
        //FormatURL(searchString);

        // Create a new adapter that takes an empty list of books as input
        //newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        //recyclerView.setAdapter(newsAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        /*newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentBook = newsAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getNewsUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });*/

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network.
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Get the loadingindicator view and assign it to loadingIndicator.
        View loadingIndicator = findViewById(R.id.loading_indicator);

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            // Make the loadingIndicator view visible.
            loadingIndicator.setVisibility(View.VISIBLE);


        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible

            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyNewsListView.setText("No internet connection");
        }
    }



/*
    private String FormatURL(String searchString) {
        searchString = searchString.replace(" ", "+");
        searchString = NEWS_REQUEST_URL + searchString + getString(R.string.maxResults);
        return searchString;
    }*/

    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Call FormatURL() and pass in the searchString entered in the searchbox.
        // This will add the base url and max results and pass it to the loader.
        //String searchUrl = FormatURL(searchString);



        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String newsSection = sharedPrefs.getString(
                getString(R.string.settings_news_section_key),
                getString(R.string.settings_news_section_default)
        );

        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

/*        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");*/

        //https://content.guardianapis.com/search?q=politics&api-key=test&show-fields=thumbnail
        uriBuilder.appendPath("search");
        uriBuilder.appendQueryParameter("q", newsSection);
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");

        return new NewsLoader(this, uriBuilder.toString());

    }



    @Override
    public void onLoadFinished(Loader<List<News>> NewsLoader, List<News> newsList) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No news found."
        //emptyNewsListView.setText(R.string.noNewsFound);

        // Clear the adapter of previous earthquake data
        //newsAdapter.clear();

        if (newsList != null && !newsList.isEmpty()) {

            this.newsList.addAll(newsList);
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        //newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

