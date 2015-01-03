package com.book10.admin.book10.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.book10.admin.book10.Activities.MainActivity;
import com.book10.admin.book10.R;
import com.book10.admin.book10.Utilities.CheckParseFavsAndRec;
import com.book10.admin.book10.Utilities.PullParseUserLists;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by admin on 12/2/14.
 */
public class UserSignInFragment extends Fragment implements View.OnClickListener{

    private final static String PASSWORD = "1111";
    private EditText enterEmail;

    public static UserSignInFragment newInstance() {
        UserSignInFragment userSignInFragment = new UserSignInFragment();
        return userSignInFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_user_sign_in, container, false);
        enterEmail = (EditText) rootview.findViewById(R.id.email_edittext);
        Button submitEmail = (Button) rootview.findViewById(R.id.submit_email_sign_in);
        Button newUser = (Button) rootview.findViewById(R.id.new_user_button);
        submitEmail.setOnClickListener(this);
        newUser.setOnClickListener(this);

        return rootview;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_email_sign_in:
                signIn();
                break;
            case R.id.new_user_button:
                newUserSelected();
                break;
        }
    }

    private void signIn() {
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

    private void newUserSelected() {
        UserNewAccountFragment newAccount = new UserNewAccountFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.signin_container, newAccount)
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
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
