package com.weirdresonance.android.newsforyou;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.thumbnail;

/**
 * Created by Stephen.Pierce on 22/06/2017.
 */

public final class QueryUtils {

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int RESPONSE_CODE_200 = 200;
    static Bitmap newsImage;
    static String newsTitle;
    static String newsSection;
    static String newsPublishedDate;
    static String newsUrl;

    static JSONObject jsonObject;
    static JSONObject response;
    static JSONArray newsItemArray;
    static JSONObject fields;
    /**
     * Log messages tag
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor as an object instance of this class should never be created.
     */
    private QueryUtils() {
        throw new AssertionError("You cannot create instances of QueryUtils!");
    }

    /**
     * Query the Google Books API and return a list of books based on the search criteria entered
     * by the user.
     *
     * @param requestURL
     * @return
     */
    public static List<News> fetchNewsData(String requestURL) {

        // Create a URL object
        URL url = createUrl(requestURL);

        // Perform an HTTP request to the URL provided in url and receive a JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "There was a problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of books
        return news;

    }

    /**
     * Form the URL and return it to the fetchBookData method
     *
     * @param requestURL
     * @return
     */
    private static URL createUrl(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "There was a problem building the URL.", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

        // Check to see if the URL is null and if it is return straight away
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;

        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();

            // Set setReadTimeout in milliseconds.
            httpUrlConnection.setReadTimeout(READ_TIMEOUT);

            // Set setConnectTimeout in milliseconds.
            httpUrlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            // If the response was successfully with a response code of 200
            // then read the input stream and parse the response.
            if (httpUrlConnection.getResponseCode() == RESPONSE_CODE_200) {
                inputStream = httpUrlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpUrlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON result", e);
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }


    private static List<News> extractFeatureFromJson(String newsJson) {

        // If the JSON String is empty then return early
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        // Create an ArrayList that we can add the books to
        List<News> newsList = new ArrayList<>();

        // Try and parse the JSON response string and catch and handle if a JSONException exception is thrown
        try {
            // Create an JSONObject from the bookJson response string
            jsonObject = new JSONObject(newsJson);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items relating to the book.

            if (jsonObject.has("response")) {
                response = jsonObject.getJSONObject("response");
            }

            if (response.has("results")) {
                newsItemArray = response.getJSONArray("results");
            }




            // For each book in the bookArray create a Book object
            for (int i = 0; i < newsItemArray.length(); i++) {

                // Get the book at position i.
                JSONObject currentNewsItem = newsItemArray.getJSONObject(i);

                if (currentNewsItem.has("fields")) {
                    fields = currentNewsItem.getJSONObject("fields");
                }

                // Extract the JSONObject associated with the key volumeInfo
                //JSONObject volumeInfo = currentNewsItem.getJSONObject("results");

                // Check to see if the response contains the key "newsTitle".
                if(currentNewsItem.has("webTitle")) {
                    // Extract the value for the newsTitle key
                    newsTitle = currentNewsItem.getString("webTitle");
                }

                // Check to see if response contains the key "sectionName".
                if(currentNewsItem.has("sectionName")) {
                    // Extract the value for the author key
                    newsSection = currentNewsItem.getString("sectionName");

                    // Trim the author string and remove [, ] and ".
/*                    author = author.replaceAll("[\\[\\]\"]", "");
                    author = author.replaceAll(",", ", ");*/
                }

                // Check to see if the response contains the key "webPublicationDate".
                if(currentNewsItem.has("webPublicationDate")) {
                    // Extract the URL for the selected book key
                    newsPublishedDate = currentNewsItem.getString("webPublicationDate");
                }

                // Check to see if the response contains the key "webUrl".
                if(currentNewsItem.has("webUrl")) {
                    // Extract the URL for the selected book key
                    newsUrl = currentNewsItem.getString("webUrl");
                }


                // Check to see if volumeInfo contains the key "thumbnail"
                if(fields.has("thumbnail")) {

                    // Extract the JSONObject associate with the key imageLinks
                    String thumbnail = fields.getString("thumbnail");

                    // Extract the JSONObject associated with the key thumbnail
                    //String thumbnailURL = thumbnail.getString("thumbnail");

                    // Try and download the small thumbnail image
                    try {
                        InputStream in = new java.net.URL(thumbnail).openStream();
                        newsImage = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                }

                // Create a new News object with the author and newsTitle of the book
                //News news = new News(newsImage, newsTitle, author, newsUrl);
                News news = new News(newsImage, newsTitle, newsSection, newsPublishedDate, newsUrl);

                // Add the book to the list of books
                newsList.add(news);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "There was a problem parsing the result of the book JSON results", e);
        }

        // Return the list of news
        return newsList;
    }

}
