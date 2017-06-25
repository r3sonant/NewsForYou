package com.weirdresonance.android.newsforyou;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

/**
 * Created by Steev on 21/06/2017.
 */

public class News {

    /**
     * Declare private variable for newsImage, newsTitle, newsSection and selected news item url (newsUrl).
     */
    private Bitmap newsImage;
    private String newsTitle;
    private String newsSection;
    private String newsPublishedDate;
    private String newsUrl;

    //public News(Bitmap newsImage, String newsTitle, String newsSection, String newsUrl) {
    public News(Bitmap newsImage, String newsTitle, String newsSection, String newsPublishedDate, String newsUrl) {

        this.newsImage = newsImage;
        this.newsTitle = newsTitle;
        this.newsSection = newsSection;
        this.newsPublishedDate = newsPublishedDate;
        this.newsUrl = newsUrl;
    }

    // Getter for NewsImage
    public Bitmap getNewsImage() {
        return newsImage;
    }

    // Getter for newsTitle
    public String getNewsTitle() {
        return newsTitle;
    }

    // Getter for newsSection
    public String getNewsSection() {
        return newsSection;
    }

    // Getter for newPublishedDate
    public String getNewsPublishedDate() {
        return newsPublishedDate;
    }

    // Getter for newsUrl
    public String getNewsUrl() {
        return newsUrl;
    }

}
