package com.book10.admin.book10;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by admin on 12/7/14.
 */
public class Book10Application extends Application{

    private final String APPLICATION_ID = "VERZL9uf76OLTaNPLnkG11SaItHnWQSvjoaGKdlC";
    private final String CLIENT_ID = "4v9u32Z7A7cV2rIVetSqNgNciBnui2w1ykQBaRiL";

    @Override
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_ID);
    }

}
