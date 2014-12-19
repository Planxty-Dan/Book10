package com.book10.admin.book10.Models;

import java.util.ArrayList;

/**
 * Created by admin on 12/18/14.
 */
public class RecommendedSingleton {

    private static RecommendedSingleton instance;
    private static ArrayList<BooksModel> recommendedList;

    public static RecommendedSingleton getInstance() {
        if (instance == null) {
            instance = new RecommendedSingleton();
        }
        return instance;
    }

    private RecommendedSingleton() {};

    public void setRecommendedList(ArrayList<BooksModel> recommendedList) {
        this.recommendedList = recommendedList;
    }

    public ArrayList<BooksModel> getRecommendedList() {
        return recommendedList;
    }

    public void addToRecommendedList(BooksModel book) {
        recommendedList.add(book);
    }

    public void removeFromRecommendedList(int index) {
        recommendedList.remove(index);
    }
}
