package com.book10.admin.book10.Models;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by admin on 12/11/14.
 */
public class BuildBooksFromParseObjects {

    private ArrayList<BooksModel> recommendedBooks;
    private ArrayList<BooksModel> favoriteBooks;
    private ArrayList<BooksModel> backUpRecommendedBooks;

    public interface OnBooksLoadedListener {

        public void recommendedBooksLoaded(ArrayList<BooksModel> recommendedBooks, ArrayList<BooksModel> backUpRecommendations);

        public void favoriteBooksLoaded(ArrayList<BooksModel> favoriteBooks);
    }

    public void recommendedBooks(ArrayList<ParseObject> recommendedParseObjects) {

    }

    public void favoriteBooks(ArrayList<ParseObject> favoriteParseObjects) {

    }


}
