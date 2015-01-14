package com.book10.admin.book10.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.book10.admin.book10.Fragments.FavoriteBooksFragment;
import com.book10.admin.book10.Fragments.MainFragment;
import com.book10.admin.book10.Fragments.UserSignInFragment;
import com.book10.admin.book10.R;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity (R.layout.activity_main)
@OptionsMenu (R.menu.menu_main)
public class MainActivity extends Activity {

    @AfterViews
    protected void toMainFragment() {
        MainFragment mainFragment = MainFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.main_container, mainFragment)
                .commit();
    }

    @OptionsItem (R.id.log_out)
    protected void logOut() {
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext(), UserSignInActivity.class);
        startActivity(intent);
    }
}
