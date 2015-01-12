package com.book10.admin.book10.Utilities;

import android.util.Log;
import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.Models.RecommendedSingleton;
import com.book10.admin.book10.Models.UserMatchesObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by admin on 12/19/14.
 */
public class UpdateRecommendedBooks {

    private final static String USER_FAVORITES_KEY = "UserFavorites";
    private final static String BOOK_CATEGORY_IN_FAVORITES_KEY = "book";
    private final static String USER_CATEGORY_IN_FAVORITES_KEY = "user";
    private RecommendedSingleton recommendedSingleton = RecommendedSingleton.getInstance();
    private ArrayList<BooksModel> recommendedBooks = recommendedSingleton.getRecommendedList();
    private ArrayList<ParseObject> recommendedParseObjectBooks = new ArrayList<ParseObject>();
    private ArrayList<ParseObject> userFavorites = new ArrayList<ParseObject>();

    public void getUserFavorites() {
        final ParseQuery<ParseObject> favoritesQuery = ParseQuery.getQuery(USER_FAVORITES_KEY);
        favoritesQuery.whereEqualTo(USER_CATEGORY_IN_FAVORITES_KEY, ParseUser.getCurrentUser());
        favoritesQuery.include(BOOK_CATEGORY_IN_FAVORITES_KEY);
        favoritesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ArrayList<ParseObject> favorites = (ArrayList<ParseObject>) parseObjects;
                userFavorites = getBooksFromUserFavorites(favorites);
                buildListOfUsersWhoMatchFavorites(userFavorites);
            }
        });
    }

    private ArrayList<ParseObject> getBooksFromUserFavorites(ArrayList<ParseObject> favorites) {
        ArrayList<ParseObject> books = new ArrayList<ParseObject>();
        for (ParseObject userFavorite : favorites) {
            ParseObject thisBook = (ParseObject) userFavorite.get("book");
            books.add(thisBook);
        }
        return books;
    }

    private void buildListOfUsersWhoMatchFavorites(ArrayList<ParseObject> favorites) {
        ParseQuery<ParseObject> matchingUsers = ParseQuery.getQuery(USER_FAVORITES_KEY);
        matchingUsers.whereContainedIn(BOOK_CATEGORY_IN_FAVORITES_KEY, favorites);
        matchingUsers.whereNotEqualTo(USER_CATEGORY_IN_FAVORITES_KEY, ParseUser.getCurrentUser());
        matchingUsers.include(USER_CATEGORY_IN_FAVORITES_KEY);
        matchingUsers.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ArrayList<String> matchingUsers = new ArrayList<String>();
                for (ParseObject matchingUser : parseObjects) {
                    ParseObject user = matchingUser.getParseUser("user");
                    String userID = user.getObjectId().toString();
                    matchingUsers.add(userID);
                }
                countMatches(matchingUsers);
            }
        });
    }

    private void countMatches(ArrayList<String> matchingUsers) {
        HashMap<String, Integer> numberOfUserMatches = new HashMap();
        for (String userID : matchingUsers) {
            if (numberOfUserMatches.containsKey(userID)) {
                numberOfUserMatches.put(userID, numberOfUserMatches.get(userID) + 1);
            } else {
                numberOfUserMatches.put(userID, 1);
            }
        }
        sortCountedMatches(numberOfUserMatches);
    }

    private void sortCountedMatches(HashMap<String, Integer> countedMatches) {
        ArrayList<UserMatchesObject> sortedUserMatchesList = new ArrayList<UserMatchesObject>();
        for (int i = 10; i > 0; i--) {
            for (HashMap.Entry<String, Integer> entry : countedMatches.entrySet()) {
                if (entry.getValue() == i) {
                    UserMatchesObject userMatches = new UserMatchesObject(entry.getKey(), entry.getValue());
                    sortedUserMatchesList.add(userMatches);
                }
            }
        }
        findMatchingUserWithSameNumberOfMatches(sortedUserMatchesList);
    }

    private void findMatchingUserWithSameNumberOfMatches(ArrayList<UserMatchesObject> sortedUserMatchesList) {
        int ArrayListStartIndex = 0;
        int numberOfMatcheUsers = 0;
        do {
            boolean countTiedUserMatches = true;
            while (countTiedUserMatches) {
                if (sortedUserMatchesList.size() > (ArrayListStartIndex + numberOfMatcheUsers + 1) &&
                        sortedUserMatchesList.get(ArrayListStartIndex).getNumberOfMatches() ==
                                sortedUserMatchesList.get(ArrayListStartIndex + numberOfMatcheUsers + 1).getNumberOfMatches()) {
                    numberOfMatcheUsers++;
                } else {
                    countTiedUserMatches = false;
                }
            }
            buildListOfUserIDsForTiedNumberOfMatches(sortedUserMatchesList, ArrayListStartIndex, numberOfMatcheUsers);
            ArrayListStartIndex = numberOfMatcheUsers + 1;
        } while (recommendedParseObjectBooks.size() < 21);
        saveBooks();
    }

    private void buildListOfUserIDsForTiedNumberOfMatches(ArrayList<UserMatchesObject> sortedUserMatchesList, int startIndex, int stopIndex) {
        ArrayList<String> userIDsWithTiedNumberOfMatches = new ArrayList<String>();
        stopIndex = (startIndex + stopIndex);
        for (int i = startIndex; i <= stopIndex; i++) {
            String tempID = sortedUserMatchesList.get(i).getUserID();
            userIDsWithTiedNumberOfMatches.add(tempID);
        }
        getUsersFromMatchedList(userIDsWithTiedNumberOfMatches);
    }

    private void getUsersFromMatchedList(ArrayList<String> matchedUserIDs) {
        try {
            ParseQuery<ParseUser> matchedUsers = ParseUser.getQuery();
            matchedUsers.whereContainedIn("objectId", matchedUserIDs);
            matchedUsers.include("User");
            ArrayList<ParseUser> userObjects = (ArrayList<ParseUser>) matchedUsers.find();
            getFavoriteBooksOfMatchedUsers(userObjects);
        } catch (ParseException e) {
            Log.e("matchedUserFind", e.getLocalizedMessage());
        }
    }

    private void getFavoriteBooksOfMatchedUsers(ArrayList<ParseUser> matchedUsers) {
        try {
            ParseQuery<ParseObject> matchedUserFavorites = ParseQuery.getQuery(USER_FAVORITES_KEY);
            matchedUserFavorites.whereContainedIn(USER_CATEGORY_IN_FAVORITES_KEY, matchedUsers);
            matchedUserFavorites.include(BOOK_CATEGORY_IN_FAVORITES_KEY);
            matchedUserFavorites.include(USER_CATEGORY_IN_FAVORITES_KEY);
            ArrayList<ParseObject> favoritesOfMatchedUsers = (ArrayList<ParseObject>) matchedUserFavorites.find();
            getBooksFromParseFavoritesObjects(favoritesOfMatchedUsers);
        } catch (ParseException e) {
            Log.e("exception in parse call", e.getLocalizedMessage());
        }
    }

    private void getBooksFromParseFavoritesObjects(ArrayList<ParseObject> favoritesOfMatchedUsers) {
        ArrayList<ParseObject> recommendedParseObjects = new ArrayList<ParseObject>();
        for (ParseObject book: favoritesOfMatchedUsers) {
            recommendedParseObjects.add((book.getParseObject("book")));
        }
        addBooksToRecommendedList(recommendedParseObjects);
    }

    private void addBooksToRecommendedList(ArrayList<ParseObject> recommendedBooks) {
        ArrayList<String> IDsOfUserFavorites = new ArrayList<String>();
        ArrayList<String> IDsOfBooksAddedToRecommendations = new ArrayList<String>();
        for (ParseObject favoriteBook: userFavorites) {
            IDsOfUserFavorites.add(favoriteBook.getObjectId());
        }
        for (ParseObject recommendedBook: recommendedBooks) {
            String bookID = recommendedBook.getObjectId();
            if (IDsOfUserFavorites.contains(bookID) || IDsOfBooksAddedToRecommendations.contains(bookID)) {
                recommendedBooks.remove(this);
            } else {
                IDsOfBooksAddedToRecommendations.add(bookID);
                recommendedParseObjectBooks.add(recommendedBook);
            }
        }
    }

    private void saveBooks() {
        BuildBooksFromParseObjects buildBooks = new BuildBooksFromParseObjects();
        if (recommendedParseObjectBooks.size() > 20) {
            ArrayList<ParseObject> parseBooksSubList = new ArrayList<ParseObject>(recommendedParseObjectBooks.subList(0, 20));
            recommendedParseObjectBooks = parseBooksSubList;
        }
        recommendedBooks = buildBooks.build(recommendedParseObjectBooks);
        recommendedSingleton.setRecommendedList(recommendedBooks);
    }
}


