package com.book10.admin.book10.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.book10.admin.book10.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

/**
 * Created by admin on 12/15/14.
 */
@EFragment (R.layout.fragment_main)
public class MainFragment extends Fragment{

    @Click (R.id.main_favorites_button)
    protected void favoritesClicked() {
        FavoriteBooksFragment favoriteBooksFragment = FavoriteBooksFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, favoriteBooksFragment)
                .addToBackStack("main")
                .commit();
    }

    @Click (R.id.main_recommendations_button)
    protected void recommendationsClicked() {
        RecommendBooksFragment recommendBooksFragment = RecommendBooksFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.main_container, recommendBooksFragment)
                .addToBackStack("main")
                .commit();
    }
}
