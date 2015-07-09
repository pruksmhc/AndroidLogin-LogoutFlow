// Copyright 2004-present Facebook. All Rights Reserved.

package com.cloudsourcing.android.cloudsourcing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.parse.Parse;

/**
 * Created by yada on 7/6/15.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Here, the user can login using Facebook as well as normal login (username and password) *
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplication());
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        if (isLoggedIn()) { //person is logged in.
            Intent intent  = GameActivity.newIntent(this);
            startActivity(intent);
        } else {
            // Send user to LoginSignupActivity.class
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = new LoginFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        setContentView(R.layout.activity_game); //this inflates the activity main layout.
       //finding any fragments that exist by the fragmenta managaer.

        }


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null; //isloggedInw ill be true when the person is logged in.
    }
    }
