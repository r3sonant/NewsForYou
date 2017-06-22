package com.weirdresonance.android.newsforyou;

import android.graphics.Bitmap;

/**
 * Created by Steev on 21/06/2017.
 */

public class News {

    /**
     * Declare private variable for newsImage, newsTitle, newsAuthor and selected news item url (newsUrl).
     */
    //private Bitmap newsImage;
    private String newsTitle;
    private String newsAuthor;
    private String newsUrl;

    //public News(Bitmap newsImage, String newsTitle, String newsAuthor, String newsUrl) {
    public News(String newsTitle, String newsAuthor, String newsUrl) {

        //this.newsImage = newsImage;
        this.newsTitle = newsTitle;
        this.newsAuthor = newsAuthor;
        this.newsUrl = newsUrl;
    }

    // Getter for newsTitle
    public String getNewsTitle() {
        return newsTitle;
    }

    // Getter for newsAuthor
    public String getNewsAuthor() {
        return newsAuthor;
    }

    // Getter for newUrl
    public String getNewsUrl() {
        return newsUrl;
    }


/*    public Bitmap getNewsImage() {
        return newsImage;
    }*/


}
