package com.book10.admin.book10.NewVersion;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.book10.admin.book10.Fragments.RecommendBooksFragment;
import com.book10.admin.book10.Fragments.UserSignInFragment;
import com.book10.admin.book10.MainActivity;
import com.book10.admin.book10.Models.BuildBooksFromParseObjects;
import com.book10.admin.book10.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/2/14.
 */
public class SplashActivity extends Activity {

    private ArrayList<ParseObject> recommendations;
    private ArrayList<ParseObject> favorites;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkLogin();
    }

    protected void checkLogin() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            userSignIn();
        } else {
            checkFavorites(currentUser);
        }
    }

    protected void userSignIn() {
        UserSignInFragment signInFragment = new UserSignInFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, signInFragment)
                .addToBackStack("main")
                .commit();
    }

    private void checkFavorites(ParseUser currentUser) {
        final ParseQuery<ParseObject> favoritesQuery = ParseQuery.getQuery("userFavorites");
        favoritesQuery.whereEqualTo("user", currentUser);
        favoritesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    if (parseObjects == null || parseObjects.size() == 0) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        favorites = (ArrayList<ParseObject>) parseObjects;
                        BuildBooksFromParseObjects buildBooks = new BuildBooksFromParseObjects();
                        buildBooks.favoriteBooks(favorites);
                        checkRecommendations(buildBooks);
                    }
                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });

    }

    private void checkRecommendations(final BuildBooksFromParseObjects buildBooks) {
        ParseQuery<ParseObject> recomendationsQuery = ParseQuery.getQuery("recommendations");
        recomendationsQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        recomendationsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    if (parseObjects == null || parseObjects.size() == 0) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        recommendations = (ArrayList<ParseObject>) parseObjects;
                        buildBooks.recommendedBooks(recommendations);
                    }
                } else {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }
}
