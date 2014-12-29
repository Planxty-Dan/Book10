package com.book10.admin.book10.Utilities;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 12/19/14.
 */
public class UpdateRecommendedBooks {

    private final String USER_FAVORITES_KEY = "UserFavorites";
    private final String BOOK_CATEGORY_IN_FAVORITES_KEY = "book";
    private final String USER_CATEGORY_IN_FAVORITES_KEY = "user";

    public void getRecommendations() {
        final ParseQuery<ParseObject> favoritesQuery = ParseQuery.getQuery(USER_FAVORITES_KEY);
        favoritesQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        favoritesQuery.include("book");
        favoritesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ArrayList<ParseObject> favorites = (ArrayList<ParseObject>) parseObjects;
                buildListOfFavoriteMatches(favorites);
            }
        });
    }

    private void buildListOfFavoriteMatches(ArrayList<ParseObject> favorites) {
        ParseQuery<ParseObject> matchingUsers = ParseQuery.getQuery(USER_FAVORITES_KEY);
        matchingUsers.whereContainedIn(BOOK_CATEGORY_IN_FAVORITES_KEY, Arrays.asList(favorites));
        matchingUsers.whereNotEqualTo(USER_CATEGORY_IN_FAVORITES_KEY, ParseUser.getCurrentUser());
        matchingUsers.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

            }
        });
    }
}
