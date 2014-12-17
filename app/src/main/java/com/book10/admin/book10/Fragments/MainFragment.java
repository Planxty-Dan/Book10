package com.book10.admin.book10.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.book10.admin.book10.R;

/**
 * Created by admin on 12/15/14.
 */
public class MainFragment extends Fragment{

    private Button favoritesButton;
    private Button recommendationsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        favoritesButton = (Button) rootView.findViewById(R.id.main_favorites_button);
        recommendationsButton = (Button) rootView.findViewById(R.id.main_favorites_button);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteBooksFragment favoriteBooksFragment = FavoriteBooksFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, favoriteBooksFragment)
                        .addToBackStack("main")
                        .commit();
            }
        });
        recommendationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendBooksFragment recommendBooksFragment = RecommendBooksFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, recommendBooksFragment)
                        .addToBackStack("main")
                        .commit();
            }
        });
    }
}
