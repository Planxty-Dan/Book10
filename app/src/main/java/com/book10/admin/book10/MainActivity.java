package com.book10.admin.book10;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.book10.admin.book10.Fragments.MainFragment;
import com.book10.admin.book10.Fragments.UserSignInFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainFragment mainFragment = new MainFragment();
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPrefrences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPrefrences.getString("username", null) == null) {
            userSignIn();
        } else {
            getFragmentManager().beginTransaction()
                .add(R.id.container, mainFragment)
                .commit();
        }
    }

    protected void userSignIn() {
        UserSignInFragment signInFragment = new UserSignInFragment();
        getFragmentManager().beginTransaction()
            .add(R.id.container, signInFragment)
            .addToBackStack("main")
            .commit();
    }
}
