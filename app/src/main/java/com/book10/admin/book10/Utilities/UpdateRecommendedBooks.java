package com.book10.admin.book10.Utilities;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/19/14.
 */
public class UpdateRecommendedBooks {

    private final static String USER_FAVORITES_KEY = "UserFavorites";
    private final static String BOOK_CATEGORY_IN_FAVORITES_KEY = "book";
    private final static String USER_CATEGORY_IN_FAVORITES_KEY = "user";

    public void getUserFavorites() {
        final ParseQuery<ParseObject> favoritesQuery = ParseQuery.getQuery(USER_FAVORITES_KEY);
        favoritesQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        favoritesQuery.include("book");
        favoritesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ArrayList<ParseObject> favorites = (ArrayList<ParseObject>) parseObjects;
                getBooksFromUserFavorites(favorites);
            }
        });
    }

    public void getBooksFromUserFavorites(ArrayList<ParseObject> favorites) {
        ArrayList<ParseObject> favoriteBooks = new ArrayList<ParseObject>();
        for (ParseObject userFavorite : favorites) {
            ParseObject thisBook = (ParseObject) userFavorite.get("book");
            favoriteBooks.add(thisBook);
        }
        buildListOfUsersWhoMatchFavorites(favoriteBooks);
    }

    private void buildListOfUsersWhoMatchFavorites(ArrayList<ParseObject> favorites) {
        ParseQuery<ParseObject> matchingUsers = ParseQuery.getQuery(USER_FAVORITES_KEY);
        matchingUsers.whereContainedIn(BOOK_CATEGORY_IN_FAVORITES_KEY, favorites);
        matchingUsers.whereNotEqualTo(USER_CATEGORY_IN_FAVORITES_KEY, ParseUser.getCurrentUser());
        matchingUsers.include("user");
        matchingUsers.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ArrayList<String> matchingUsers = new ArrayList<String>();
                for (ParseObject matchingUser : parseObjects) {
                    ParseObject user = matchingUser.getParseUser("user");
                    String userID = user.getObjectId().toString();
                    matchingUsers.add(userID);
                }
            }
        });
    }
}


