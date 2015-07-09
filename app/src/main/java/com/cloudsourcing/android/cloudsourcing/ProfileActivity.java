// Copyright 2004-present Facebook. All Rights Reserved.

package com.cloudsourcing.android.cloudsourcing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.profile_fragment_container); //finding any fragments that exist by the fragemnt mangaer.
        if (fragment == null) {
            fragment = new ProfileFragment(); //creating the sign up framgnet. if cannot find, then createa This is the most important one.
            fm.beginTransaction().add(R.id.profile_fragment_container, fragment).commit(); //creating a fragment.
        }


    }
}
