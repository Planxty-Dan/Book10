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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;

/**
 * Created by admin on 12/2/14.
 */
@EActivity (R.layout.activity_splash)
public class SplashActivity extends Activity {

    @AfterViews
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

    private void userSignIn() {
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
        Intent intent = new Intent(getApplicationContext(), MainActivity_.class);
        startActivity(intent);
    }
}
