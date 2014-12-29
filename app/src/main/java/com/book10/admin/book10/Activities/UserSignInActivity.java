package com.book10.admin.book10.Activities;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.book10.admin.book10.Fragments.UserSignInFragment;
import com.book10.admin.book10.R;

/**
 * Created by admin on 12/29/14.
 */
public class UserSignInActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        toSignInFragment();
    }

    private void toSignInFragment() {
        UserSignInFragment signInFragment = UserSignInFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.signin_container, signInFragment)
                .commit();
    }
}
