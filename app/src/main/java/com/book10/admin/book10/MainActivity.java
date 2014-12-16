package com.book10.admin.book10;

import android.app.Activity;
import android.os.Bundle;
import com.book10.admin.book10.Fragments.MainFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toRecommendedBooksFragment();
    }

    protected void toRecommendedBooksFragment() {
        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.container, mainFragment)
                .commit();
    }


}
