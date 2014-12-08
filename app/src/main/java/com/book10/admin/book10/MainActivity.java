package com.book10.admin.book10;

import android.app.Activity;
import android.os.Bundle;
import com.book10.admin.book10.Fragments.RecommendBooksFragment;
import com.book10.admin.book10.Fragments.UserSignInFragment;
import com.parse.ParseUser;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    protected void checkLogin() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            userSignIn();
        } else {
            toRecommendedBooksFragment();
        }
    }

    protected void toRecommendedBooksFragment() {
        RecommendBooksFragment recommendBooksFragment = new RecommendBooksFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.container, recommendBooksFragment)
                .commit();
    }

    protected void userSignIn() {
        UserSignInFragment signInFragment = new UserSignInFragment();
        getFragmentManager().beginTransaction()
            .add(R.id.container, signInFragment)
            .addToBackStack("main")
            .commit();
    }
}
