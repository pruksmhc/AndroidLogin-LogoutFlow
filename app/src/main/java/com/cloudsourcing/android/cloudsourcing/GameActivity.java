// Copyright 2004-present Facebook. All Rights Reserved.

package com.cloudsourcing.android.cloudsourcing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;

/**
 * Created by angelmaredia on 7/4/15.
 */

public class GameActivity extends AppCompatActivity {


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        FacebookSdk.sdkInitialize(getApplicationContext());

        addFrag();

    }

     public void addFrag() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment mainFrag = fm.findFragmentById(R.id.fragment_container);
        if (mainFrag == null) {
            mainFrag = CloudFrag.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, mainFrag)
                    .commit();
        }
    }



    public void replaceFrag(Fragment frag) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, new CloudFrag())
                .commit();

    }
}

