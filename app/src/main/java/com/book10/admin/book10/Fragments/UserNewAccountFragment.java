package com.book10.admin.book10.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.book10.admin.book10.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

/**
 * Created by admin on 12/3/14.
 */
public class UserNewAccountFragment extends Fragment implements View.OnClickListener{

    private final String PASSWORD = "1111";
    private final String FAVORITES_KEY = "favoritesList";
    private final String RECOMMENDED_KEY = "recommendedList";
    private final String UNWANTED_RECOMMENDATIONS_KEY = "unwantedRecommendations";
    private EditText enterEmail;
    private EditText confirmEmail;
    private Button submitNewUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_new_account, container, false);
        enterEmail = (EditText) rootView.findViewById(R.id.new_user_email_edittext);
        confirmEmail = (EditText) rootView.findViewById(R.id.new_user_confirm_email_edittext);
        submitNewUser = (Button) rootView.findViewById(R.id.submit_new_user);
        submitNewUser.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_new_user:
                newUserSubmitted();
                break;
        }
    }

    private void newUserSubmitted() {
        String userEmail = enterEmail.getText().toString();
        String userConfirmEmail = confirmEmail.getText().toString();
        if (checkEmail(userEmail, userConfirmEmail)) {
            ParseUser parseUser = new ParseUser();
            parseUser.setUsername(userEmail);
            parseUser.setPassword(PASSWORD);
            parseUser.setEmail(userEmail);
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_SHORT).show();
                        ArrayList<ParseObject> recommended = new ArrayList<ParseObject>();
                        ArrayList<ParseObject> favorites = new ArrayList<ParseObject>();
                        ArrayList<ParseObject> unwantedRecommendations = new ArrayList<ParseObject>();
                        ParseUser.getCurrentUser().put(RECOMMENDED_KEY, recommended);
                        ParseUser.getCurrentUser().put(FAVORITES_KEY, favorites);
                        ParseUser.getCurrentUser().put(UNWANTED_RECOMMENDATIONS_KEY, unwantedRecommendations);
                        MainFragment mainFragment = MainFragment.newInstance();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, mainFragment);
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getActivity(), R.string.parse_exception_create_login + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.new_user_email_match_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkEmail(String userEmail, String confirmationEmail) {
        if (userEmail.equals(confirmationEmail)) {
            return true;
        } else {
            return false;
        }
    }
}
