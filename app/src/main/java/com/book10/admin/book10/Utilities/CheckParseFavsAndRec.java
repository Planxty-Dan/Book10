package com.book10.admin.book10.Utilities;
import android.content.Context;
import android.util.Log;
import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.Models.FavoritesSingleton;
import com.book10.admin.book10.Models.RecommendedSingleton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/29/14.
 */
public class CheckParseFavsAndRec {

    public interface ParseDataLoadedListener {
        public void dataLoaded();
    }

    private ArrayList<ParseObject> recommendationsParseObjects;
    private ArrayList<ParseObject> favoritesParseObjects;
    private ArrayList<BooksModel> favoritesBookObjects;
    private ArrayList<BooksModel> recommendationsBookObjects;
    private FavoritesSingleton favoritesSingleton = FavoritesSingleton.getInstance();
    private RecommendedSingleton recommendedSingleton = RecommendedSingleton.getInstance();
    private BuildBooksFromParseObjects buildBooks = new BuildBooksFromParseObjects();
    private ParseDataLoadedListener parseDataLoadedListener;
    private boolean favoritesLoaded = false;
    private boolean recommendationsLoaded = false;

    public CheckParseFavsAndRec(ParseDataLoadedListener listener) {
        parseDataLoadedListener = listener;
    }

    public void checkFavorites() {
        final ParseQuery<ParseObject> favoritesQuery = ParseQuery.getQuery("UserFavorites");
        favoritesQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        favoritesQuery.include("book");
        favoritesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    if (parseObjects == null || parseObjects.size() == 0) {
                        favoritesBookObjects = new ArrayList<BooksModel>();
                        favoritesSingleton.setFavoritesList(favoritesBookObjects);
                    } else {
                        favoritesParseObjects = (ArrayList<ParseObject>) parseObjects;
                        favoritesBookObjects = buildBooks.build(favoritesParseObjects);
                        favoritesSingleton.setFavoritesList(favoritesBookObjects);
                    }
                } else {
                    Log.d("Error", e.getMessage());
                }
                favoritesLoaded = true;
                checkAllDataLoaded();
            }
        });
    }

    public void checkRecommendations() {
        ParseQuery<ParseObject> recomendationsQuery = ParseQuery.getQuery("recommendationsParseObjects");
        recomendationsQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        recomendationsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    if (parseObjects == null || parseObjects.size() == 0) {
                        recommendationsBookObjects = new ArrayList<BooksModel>();
                        recommendedSingleton.setRecommendedList(recommendationsBookObjects);
                    } else {
                        recommendationsParseObjects = (ArrayList<ParseObject>) parseObjects;
                        recommendationsBookObjects = buildBooks.build(recommendationsParseObjects);
                        recommendedSingleton.setRecommendedList(recommendationsBookObjects);
                    }
                } else {
                    Log.d("Error", e.getMessage());
                }
                recommendationsLoaded = true;
                checkAllDataLoaded();
            }
        });
    }

    private void checkAllDataLoaded() {
        if (favoritesLoaded && recommendationsLoaded) {
            parseDataLoadedListener.dataLoaded();
        }
    }
}
