package com.book10.admin.book10.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.book10.admin.book10.Activities.MainActivity;
import com.book10.admin.book10.Fragments.UserSignInFragment;
import com.book10.admin.book10.Models.BooksModel;
import com.book10.admin.book10.Models.RecommendedSingleton;
import com.book10.admin.book10.R;
import com.book10.admin.book10.Utilities.BuildBooksFromParseObjects;
import com.book10.admin.book10.Models.FavoritesSingleton;
import com.book10.admin.book10.Utilities.CheckParseFavsAndRec;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.ArrayList;

/**
 * Created by admin on 12/2/14.
 */
public class SplashActivity extends Activity {

    private ArrayList<ParseObject> recommendationsParseObjects;
    private ArrayList<ParseObject> favoritesParseObjects;
    private ArrayList<BooksModel> favoritesBookObjects;
    private ArrayList<BooksModel> recommendationsBookObjects;
    private FavoritesSingleton favoritesSingleton;
    private RecommendedSingleton recommendedSingleton = RecommendedSingleton.getInstance();
    private BuildBooksFromParseObjects buildBooks = new BuildBooksFromParseObjects();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        favoritesSingleton = FavoritesSingleton.getInstance();
        checkLogin();
    }

    protected void checkLogin() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            userSignIn();
        } else {
            CheckParseFavsAndRec checkParseFavsAndRec = new CheckParseFavsAndRec(new CheckParseFavsAndRec.ParseDataLoadedListener() {
                @Override
                public void dataLoaded() {
                    startMainActivity();
                }
            });
            checkParseFavsAndRec.checkFavorites();
            checkParseFavsAndRec.checkRecommendations();
        }
    }

    protected void userSignIn() {
        Intent intent = new Intent(getApplicationContext(), UserSignInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
