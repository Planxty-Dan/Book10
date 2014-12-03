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

/**
 * Created by admin on 12/3/14.
 */
public class UserNewAccountFragment extends Fragment implements View.OnClickListener{

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
        if (userEmail.equals(userConfirmEmail)) {
            if (true) { //check parse for email already existing
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                prefEditor.putString("username", userEmail);
                //add user id to Parse
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
            } else {
                Toast.makeText(getActivity(), R.string.new_user_email_already_registered, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.new_user_email_match_error, Toast.LENGTH_SHORT).show();
        }
    }
}
