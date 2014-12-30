package com.book10.admin.book10.Models;

import java.util.ArrayList;

/**
 * Created by admin on 12/16/14.
 */
public class FavoritesSingleton {

    private static FavoritesSingleton instance;
    private static ArrayList<BooksModel> favoritesList;

    public static FavoritesSingleton getInstance() {
        if (instance == null) {
            instance = new FavoritesSingleton();
        }
        return instance;
    }

    private FavoritesSingleton() {}

    public void setFavoritesList(ArrayList<BooksModel> favoritesList) {
        this.favoritesList = favoritesList;
    }

    public ArrayList<BooksModel> getFavoritesList() {
        if (favoritesList == null) {
            favoritesList = new ArrayList<BooksModel>();
        }
        return favoritesList;
    }

    public void addToFavoritesList(BooksModel book) {
        favoritesList.add(book);
    }

    public void removeFromFavoritesList(int index) {
        favoritesList.remove(index);
    }
}
