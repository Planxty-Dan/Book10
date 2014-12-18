package com.book10.admin.book10.APIcalls;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.book10.admin.book10.JsonParsing.ParseGoogleBooksJson;
import com.book10.admin.book10.Models.BooksModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by admin on 12/8/14.
 */
public class GoogleBooksAPI extends AsyncTask {

    public interface OnGoogleBooksDataLoadedListener {

        public void dataLoaded(List<BooksModel> books);

    }

    private final String SCHEME = "https";
    private final String AUTHORITY = "www.googleapis.com";
    private final String PATH_ONE = "books";
    private final String PATH_TWO = "v1";
    private final String PATH_THREE = "volumes";
    private final String QUERY = "q=";
    private final String QUERY_PARAMETER_TITLE = "intitle:";
    private final String QUERY_PARAMETER_AUTHOR = "inauthor:";
    private final String QUERY_PARAMTER_MAX_RESULTS = "maxResults";
    private final String MAX_RESULTS_3 = "3";
    private final String KEY = "key=";
    private final String API_KEY = "AIzaSyDb2gixf3_QzXw0-V5ZSxEBt8Midqxza30";
    private String bookTitle;
    private String bookAuthor;
    private OnGoogleBooksDataLoadedListener onDataLoadedListener;
    private ProgressDialog progressDialog;
    private Context context;
    private String searchResult;

    public GoogleBooksAPI(Context context, String bookTitle, String bookAuthor, OnGoogleBooksDataLoadedListener onDataLoadedListener) {
        this.context = context;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.onDataLoadedListener = onDataLoadedListener;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        InputStream inputStream;
        HttpsURLConnection urlConnection;
        String line;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            URL googleBooksURL = new URL(buildURI());
            urlConnection = (HttpsURLConnection) googleBooksURL.openConnection();
            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            searchResult = stringBuilder.toString();
            inputStream.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("IO exception", e.getMessage());
        }
        return searchResult;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressDialog.dismiss();
        List<BooksModel> googleBooksResults;
        if (searchResult == null) {
            googleBooksResults = new ArrayList<BooksModel>();
        }
        else {
            ParseGoogleBooksJson jsonData = new ParseGoogleBooksJson(searchResult);
            googleBooksResults = jsonData.parseJson();
        }
        onDataLoadedListener.dataLoaded(googleBooksResults);
    }

    private String buildURI() throws UnsupportedEncodingException {
        String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=";

        URLEncoder.encode(bookAuthor, "UTF-8");
        String titleQuery = QUERY_PARAMETER_TITLE + URLEncoder.encode(bookTitle, "UTF-8");
        String authorQuery = QUERY_PARAMETER_AUTHOR + URLEncoder.encode(bookAuthor, "UTF-8");
        String uriString = baseUrl + titleQuery + "+" + authorQuery + "&" + KEY + API_KEY;
        return uriString;
    }
}
