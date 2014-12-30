package com.book10.admin.book10.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.book10.admin.book10.Fragments.MainFragment;
import com.book10.admin.book10.Fragments.UserSignInFragment;
import com.book10.admin.book10.R;
import com.parse.ParseUser;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toMainFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void toMainFragment() {
        MainFragment mainFragment = MainFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.main_container, mainFragment)
                .commit();
    }

    private void logOut() {
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext(), UserSignInActivity.class);
        startActivity(intent);
    }
}
