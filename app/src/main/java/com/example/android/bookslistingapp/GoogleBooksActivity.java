package com.example.android.bookslistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GoogleBooksActivity extends AppCompatActivity {

    private static final String LOG_TAG = GoogleBooks.class.getName();

    //Test URL for the Google Books API query (PLEASE MODIFY IT LATER)
    private String googleBooksAPI = "https://www.googleapis.com/books/v1/volumes?q=";

    private GoogleBooksAdapter mAdapter;

    //String value of the text as a
    private String googleBooksgetText;

    //Empty TextView
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_books_activity);

        final TextView emptyTextView = (TextView) findViewById(R.id.empty_list);
        emptyTextView.setVisibility(View.INVISIBLE);

        // Find a reference to the {@link ListView} in the layout
        ListView googleBooksListView = (ListView) findViewById(R.id.list);

        googleBooksListView.setEmptyView(emptyTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new GoogleBooksAdapter(this, new ArrayList<GoogleBooks>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        googleBooksListView.setAdapter(mAdapter);

        //Find the button so the user will be able to click on the button to find the search results
        Button findButton = (Button) findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.search_progress_bar);
                progressBar.setVisibility(View.VISIBLE);

                /*
                ** Check Internet connection before making the search
                 */
                // Check state of network connectivity by referencing the ConnectivityManager
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    EditText editText = (EditText) findViewById(R.id.google_books_search);
                    googleBooksgetText = editText.getText().toString();

                    String toSearch = "";
                    if (googleBooksgetText.length() > 0) {
                        googleBooksgetText = googleBooksgetText.replace(" ", "+");
                        toSearch = googleBooksAPI + googleBooksgetText;
                    }
                    // Start the AsyncTask to fetch the earthquake data
                    GoogleBooksAsyncTask task = new GoogleBooksAsyncTask();
                    task.execute(toSearch);
                }
                if ((networkInfo == null) || !((networkInfo.isConnected()))) {
                    // If cannot connect to Internet
                    // First, hide loading indicator so error message will be visible
                    View loadingIndicator = findViewById(R.id.search_progress_bar);
                    loadingIndicator.setVisibility(View.GONE);

                    // Start the AsyncTask to fetch the earthquake data
                    GoogleBooksAsyncTask task = new GoogleBooksAsyncTask();
                    task.execute("Problem connection");

                    // Update empty state with no connection error message
                    emptyTextView.setText("No Internet Connection");
                }
                else {
                    emptyTextView.setText("No books match your search query");
                }
            }
        });
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
            List<GoogleBooks> result = QueryUtils.fetchGoogleBooksData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<GoogleBooks> data) {
            mAdapter.clear();

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.search_progress_bar);
            progressBar.setVisibility(View.GONE);

            if ((data != null) && (!data.isEmpty())) {
                mAdapter.addAll(data);
            }
        }
    }
}
