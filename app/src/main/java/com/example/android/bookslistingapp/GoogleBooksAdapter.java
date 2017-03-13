package com.example.android.bookslistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ultrajustin22 on 12/3/2017.
 * For each Google book that is displayed on the screen, it will create a list item layout for that particular book.
 *
 * This includes recycling the view that isn't wanted on the phone/tablet screen and shows the view the user wants to see
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */

public class GoogleBooksAdapter extends ArrayAdapter<GoogleBooks> {

    /**
     * Constructs a new {@link GoogleBooksAdapter}.
     *
     * @param context     of the app
     * @param googleBooks is the list of Google books, which is the data source of the adapter
     */
    public GoogleBooksAdapter(Context context, List<GoogleBooks> googleBooks) {
        super(context, 0, googleBooks);
    }

    /**
     * Returns a list item view that displays information about the Google Book at the given position
     * in the list of googleBooks.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.google_books_list_item, parent, false);
        }
        //Find the current GoogleBooks object the given position of the googleBooks list
        GoogleBooks currentGoogleBooks = getItem(position);

        TextView googleBooks_title_textView = (TextView) listItemView.findViewById(R.id.google_books_title);
        googleBooks_title_textView.setText(currentGoogleBooks.getTitle());

        TextView googleBooks_author_textView = (TextView) listItemView.findViewById(R.id.google_books_author);
        googleBooks_author_textView.setText(currentGoogleBooks.getAuthor());

        return listItemView;
    }
}
