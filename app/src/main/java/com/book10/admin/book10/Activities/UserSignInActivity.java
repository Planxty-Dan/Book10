package com.book10.admin.book10.Activities;

import android.app.Activity;
import com.book10.admin.book10.Fragments.UserSignInFragment;
import com.book10.admin.book10.Fragments.UserSignInFragment_;
import com.book10.admin.book10.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by admin on 12/29/14.
 */
@EActivity (R.layout.activity_sign_in)
public class UserSignInActivity extends Activity{

    @AfterViews
    protected void toSignInFrag() {
        UserSignInFragment signInFragment = UserSignInFragment_.builder().build();
        getFragmentManager().beginTransaction()
                .add(R.id.signin_container, signInFragment)
                .commit();
    }
}
