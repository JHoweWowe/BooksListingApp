package com.example.android.bookslistingapp;

/**
 * Created by ultrajustin22 on 12/3/2017.
 * This class displays the basic information of what a GoogleBooks object should show
 */

public class GoogleBooks {
    private String mTitle;
    private String mAuthor;

    //Creating a new GoogleBooks object
    public GoogleBooks(String title, String author){
        mTitle = title;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
