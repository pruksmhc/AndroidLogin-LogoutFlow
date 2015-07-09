// Copyright 2004-present Facebook. All Rights Reserved.

package com.cloudsourcing.android.cloudsourcing;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Button mLogOutButton;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //the profile is the launcher activity here, and it will redirect to login if the user is not logged in.
        if (!isLoggedIn()) { //redirecting to login page.
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mLogOutButton = (Button) v.findViewById(R.id.log_out_button); //creating the logout button.
        mLogOutButton.setText("Log out");
        mLogOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut(); //logging out of login manager.
               /** Boolean b = isLoggedIn();
                String str = String.valueOf(b);
                mLogOutButton.setText(str); //show state of being logged out or in
                This is for testing purposes in case the logout button mulfunctions in the future.**/
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });

        return v;

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken(); //is this person logged in.
        return accessToken != null;
    }

    }
