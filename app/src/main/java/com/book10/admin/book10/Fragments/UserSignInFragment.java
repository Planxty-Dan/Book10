package com.book10.admin.book10.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import com.book10.admin.book10.Activities.MainActivity_;
import com.book10.admin.book10.R;
import com.book10.admin.book10.Utilities.CheckParseFavsAndRec;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by admin on 12/2/14.
 */
@EFragment (R.layout.fragment_user_sign_in)
public class UserSignInFragment extends Fragment{

    private final static String PASSWORD = "1111";

    @ViewById(R.id.email_edittext)
    protected EditText enterEmail;

    @Click(R.id.submit_email_sign_in)
    protected void signIn() {
        String usersSignIn = enterEmail.getText().toString();
        ParseUser.logInInBackground(usersSignIn, PASSWORD, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    getUserLists();
                    Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getActivity(), R.string.failed_login, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Click(R.id.new_user_button)
    protected void newUserSelected() {
        UserNewAccountFragment newAccount = UserNewAccountFragment_.builder().build();
        getFragmentManager().beginTransaction()
                .replace(R.id.signin_container, newAccount)
                .commit();
    }

    private void getUserLists() {
        final ProgressDialog progressDialog =new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        CheckParseFavsAndRec checkParseFavsAndRec = new CheckParseFavsAndRec(new CheckParseFavsAndRec.ParseDataLoadedListener() {
            @Override
            public void dataLoaded() {
                progressDialog.dismiss();
                startMainActivity();
            }
        });
        checkParseFavsAndRec.checkFavorites();
        checkParseFavsAndRec.checkRecommendations();
    }

    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
