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

/**
 * Created by Stephen.Pierce on 22/06/2017.
 */

public final class QueryUtils {

    static Bitmap thumbnailImage;
    static String title;
    static String author;
    static String newsUrl;
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
            httpUrlConnection.setReadTimeout(10000);

            // Set setConnectTimeout in milliseconds.
            httpUrlConnection.setConnectTimeout(15000);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            // If the response was successfully with a response code of 200
            // then read the input stream and parse the response.
            if (httpUrlConnection.getResponseCode() == 200) {
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
            JSONObject jsonObject = new JSONObject(newsJson);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items relating to the book.

            JSONObject response = jsonObject.getJSONObject("response");
            JSONArray newsItemArray = response.getJSONArray("results");

            // For each book in the bookArray create a Book object
            for (int i = 0; i < newsItemArray.length(); i++) {

                // Get the book at position i.
                JSONObject currentNewsItem = newsItemArray.getJSONObject(i);



                // Extract the JSONObject associated with the key volumeInfo
                //JSONObject volumeInfo = currentNewsItem.getJSONObject("results");

                // Check to see if volumeInfo contains the key "title".
                if(currentNewsItem.has("webTitle")) {
                    // Extract the value for the title key
                    title = currentNewsItem.getString("webTitle");
                }

/*                // Check to see if volumeInfo contains the key "author".
                if(volumeInfo.has("author")) {
                    // Extract the value for the author key
                    author = "Author: " + volumeInfo.getString("author");

                    // Trim the author string and remove [, ] and ".
                    author = author.replaceAll("[\\[\\]\"]", "");
                    author = author.replaceAll(",", ", ");
                }*/

/*                // Check to see if volumeInfo contains the key "infoLink".
                if(volumeInfo.has("infoLink")) {
                    // Extract the URL for the selected book key
                    newsUrl = volumeInfo.getString("infoLink");
                }*/

/*                // Check to see if volumeInfo contains the key "imageLinks"
                if(volumeInfo.has("imageLinks")) {
                    // Extract the JSONObject associate with the key imageLinks
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                    // Extract the JSONObject associated with the key thumbnail
                    String thumbnailURL = imageLinks.getString("smallThumbnail");

                    // Try and download the small thumbnail image
                    try {
                        InputStream in = new java.net.URL(thumbnailURL).openStream();
                        thumbnailImage = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                }*/

                // Create a new News object with the author and title of the book
                //News news = new News(thumbnailImage, title, author, newsUrl);
                News news = new News(title, "Animation, Kids & Family", "2015");

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
