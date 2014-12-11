package com.book10.admin.book10.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.book10.admin.book10.MainActivity;
import com.book10.admin.book10.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by admin on 12/2/14.
 */
public class SplashActivity extends Activity {

    private List<ParseObject> recommendations;
    private List<ParseObject> backUpRecommendations;
    private List<ParseObject> favorites;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    private void checkRecommendations() {
        ParseQuery recomendationsQuery = ParseQuery.getQuery("reccomendations");
        recomendationsQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        recomendationsQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                
            }
        });
    }
}
