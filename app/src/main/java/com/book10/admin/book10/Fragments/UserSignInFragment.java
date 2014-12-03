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
 * Created by admin on 12/2/14.
 */
public class UserSignInFragment extends Fragment implements View.OnClickListener{

    private EditText enterEmail;
    private Button submitEmail;
    private Button newUser;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_user_sign_in, container, false);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        enterEmail = (EditText) rootview.findViewById(R.id.email_edittext);
        submitEmail = (Button) rootview.findViewById(R.id.submit_email_sign_in);
        newUser = (Button) rootview.findViewById(R.id.new_user_button);
        populateEditText();
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

    @Override
    public void onResume() {
        super.onResume();
        populateEditText();
    }

    private void populateEditText() {
        if (sharedPreferences.getString("username", null) != null) {
            enterEmail.setText(sharedPreferences.getString("username", null));
        }
    }

    private void signIn() {
        String usersEntry = enterEmail.getText().toString();
        if (true) {  //check parse for login
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            prefEditor.putString("username", usersEntry);
            //set up Parse login here
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
        } else {
            Toast.makeText(getActivity(), R.string.failed_login, Toast.LENGTH_SHORT).show();
        }
    }

    private void newUserSelected() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        UserNewAccountFragment newAccount = new UserNewAccountFragment();
        fragmentTransaction.replace(R.id.container, newAccount);
        fragmentTransaction.addToBackStack("sign in");
        fragmentTransaction.commit();

    }
}
