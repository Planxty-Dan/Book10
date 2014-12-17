package com.book10.admin.book10.Fragments;

import com.book10.admin.book10.Models.BooksModel;

import java.util.ArrayList;

/**
 * Created by admin on 12/16/14.
 */
public class FavoritesSingleton {

    private static FavoritesSingleton instance;
    private static ArrayList<BooksModel> favoritesList;

    private FavoritesSingleton() {}

    public void setFavoritesList(ArrayList<BooksModel> favoritesList) {
        this.favoritesList = favoritesList;
    }

    public ArrayList<BooksModel> getFavoritesList() {
        return favoritesList;
    }

    public void addToFavoritesList(BooksModel book) {
        favoritesList.add(book);
    }

    public void removeFromFavoritesList(int index) {
        favoritesList.remove(index);
    }

    public static FavoritesSingleton getInstance() {
        if (instance == null) {
            instance = new FavoritesSingleton();
        }
        return instance;
    }
}
