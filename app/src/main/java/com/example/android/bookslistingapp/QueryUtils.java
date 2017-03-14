package com.example.android.bookslistingapp;

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
 * Helper methods used to retrieving the data from the Google Books API
 */

public final class QueryUtils {

    //Used for log messages, especially for error messages
    private static final String LOG_TAG = QueryUtils.class.getName();

    //Head's Up: This is a private constructor, because we are not creating a QueryUtils object at all.
    //We are only using the methods and variables which can be used/accessed by this class
    private QueryUtils(){

    }

    /**
     * Query the GoogleBooks API dataset and return a list of {@link GoogleBooks} objects.
     */
    public static List<GoogleBooks> fetchGoogleBooksData(String requestUrl) {
        // Create URL object
        URL url = createURL(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<GoogleBooks> earthquakes = extractFeaturefromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }

    //Creating a new URL object from the given URL
    private static URL createURL (String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        }
        //If exception occurs, we are able to catch the exception
        catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Error creating the URL" + e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Takes byte code from the InputStream and makes the code more readable through the readFromStream helper method
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link GoogleBooks} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<GoogleBooks> extractFeaturefromJson (String googleBooksJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(googleBooksJson)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding Google Books to
        List<GoogleBooks> googleBooksArrayList = new ArrayList<>();

        //Parsing the JSON string
        try{
            JSONObject baseJSONResponse = new JSONObject(googleBooksJson);
            //Extract the object googleBooksArray
            JSONArray googleBooksItemsArray = baseJSONResponse.getJSONArray("items");

            //For each googleBooks object within the array, create a new googleBooks object
            for (int i = 0; i < googleBooksItemsArray.length(); i++) {
                //Get the current Google Book
                JSONObject currentGoogleBook = googleBooksItemsArray.getJSONObject(i);
                JSONObject volumeInfo = currentGoogleBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");
                JSONArray authors = volumeInfo.getJSONArray("authors");
                String author = authors.getString(0);

                GoogleBooks googleBooks = new GoogleBooks(title,author);
                googleBooksArrayList.add(googleBooks);
            }
        }
        catch (JSONException e){
            Log.e(LOG_TAG,"A JSONException occurred" + e);
        }
        //Returns the ArrayList of earthquakes
        return googleBooksArrayList;
    }
}
