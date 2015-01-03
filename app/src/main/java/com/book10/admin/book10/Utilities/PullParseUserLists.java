package com.book10.admin.book10.Utilities;

import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.Models.FavoritesSingleton;
import com.book10.admin.book10.Models.RecommendedSingleton;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.ArrayList;

/**
 * Created by admin on 12/19/14.
 */
public class PullParseUserLists {

    private final static String FAVORITES_KEY = "favoritesList";
    private final static String RECOMMENDED_KEY = "recommendedList";
    private BuildBooksFromParseObjects buildBooks = new BuildBooksFromParseObjects();

    public void pullFavorites() {
        ArrayList<ParseObject> favoritesParse = (ArrayList<ParseObject>) ParseUser.getCurrentUser().get(FAVORITES_KEY);
        ArrayList<BooksModel> favoritesBooks = buildBooks.build(favoritesParse);
        FavoritesSingleton favoritesSingleton = FavoritesSingleton.getInstance();
        favoritesSingleton.setFavoritesList(favoritesBooks);
    }

    public void pullRecommendations(){
        ArrayList<ParseObject> recommendedParse = (ArrayList<ParseObject>) ParseUser.getCurrentUser().get(RECOMMENDED_KEY);
        ArrayList<BooksModel> recommendedBooks = buildBooks.build(recommendedParse);
        RecommendedSingleton recommendedSingleton = RecommendedSingleton.getInstance();
        recommendedSingleton.setRecommendedList(recommendedBooks);
    }
}
