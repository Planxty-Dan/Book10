package com.book10.admin.book10.JsonParsing;

import com.book10.admin.book10.Models.BooksModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/9/14.
 */
public class ParseGoogleBooksJson {
    private final String ITEMS_ARRAY_KEY = "items";
    private final String GOOGLEID_KEY = "id";
    private final String VOLUME_INFO_KEY = "volumeInfo";
    private final String TITLE_KEY = "title";
    private final String AUTHORS_KEY = "authors";
    private final String DESCRIPTION_KEY = "description";
    private final String CATEGORIES_KEY = "categories";
    private final String IMAGES_KEY = "imageLinks";
    private final String IMAGE_URL_KEY = "thumbnail";

    private String jsonResults;
    private JSONObject mainJsonObject;

    public ParseGoogleBooksJson(String jsonResults) {
        this.jsonResults = jsonResults;
    }

    public List<BooksModel> parseJson() {
        List<BooksModel> books = new ArrayList<BooksModel>();
        try {
            mainJsonObject = new JSONObject(jsonResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray itemsArray = mainJsonObject.getJSONArray(ITEMS_ARRAY_KEY);
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject jsonBook = itemsArray.getJSONObject(i);
                String googleID = jsonBook.getString(GOOGLEID_KEY);
                JSONObject volumeInfoObject = jsonBook.getJSONObject(VOLUME_INFO_KEY);
                String title = volumeInfoObject.getString(TITLE_KEY);
                JSONArray authorsArray = volumeInfoObject.getJSONArray(AUTHORS_KEY);
                String author = getAuthors(authorsArray);
                String description = volumeInfoObject.getString(DESCRIPTION_KEY);
                JSONArray categoriesArray = volumeInfoObject.getJSONArray(CATEGORIES_KEY);
                String genre = getGenres(categoriesArray);
                JSONObject imageLink = volumeInfoObject.getJSONObject(IMAGES_KEY);
                String imageURL = imageLink.getString(IMAGE_URL_KEY);
                BooksModel currentBook = new BooksModel(googleID, title, author, genre, description, imageURL);
                books.add(currentBook);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    private String getAuthors(JSONArray authorsArray) {
        String author = "";
        try {
            if (authorsArray.length() > 1) {
                for (int j = 0; j < authorsArray.length(); j++) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(authorsArray.getString(j) + ", ");
                    author = builder.toString();
                }
            } else {
                author = authorsArray.getString(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return author;
    }

    private String getGenres(JSONArray categoriesArray) {
        String genre = "";
        try {
            if (categoriesArray.length() > 1) {
                for (int j = 0; j < categoriesArray.length(); j++) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(categoriesArray.getString(j) + ", ");
                    genre = builder.toString();
                }
            } else {
                genre = categoriesArray.getString(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return genre;
    }
}
