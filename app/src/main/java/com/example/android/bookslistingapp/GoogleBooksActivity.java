package com.example.android.bookslistingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GoogleBooksActivity extends AppCompatActivity {

    private static final String LOG_TAG = GoogleBooks.class.getName();

    //Test URL for the Google Books API query (PLEASE MODIFY IT LATER)
    private static final String googleBooks_url = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    private GoogleBooksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_books_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView googleBooksListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new GoogleBooksAdapter(this, new ArrayList<GoogleBooks>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        googleBooksListView.setAdapter(mAdapter);

        // Start the AsyncTask to fetch the earthquake data
        GoogleBooksAsyncTask task = new GoogleBooksAsyncTask();
        task.execute(googleBooks_url);
    }

    /**
     * {@link AsyncTask} is used in the background thread (so it is then used in as an inner class)
     * , instead of the main thread, to perform network updates and then used to update the UI
     *
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type.
     *
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private class GoogleBooksAsyncTask extends AsyncTask<String, Void, List<GoogleBooks>> {

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link GoogleBooks} as the result.
         */
        @Override
        protected List<GoogleBooks> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<GoogleBooks> result = QueryUtils.extractFeaturefromJson(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<GoogleBooks> data) {
            mAdapter.clear();

            if ((data != null) && (!data.isEmpty())) {
                mAdapter.addAll(data);
            }
        }
    }
}
