// Copyright 2004-present Facebook. All Rights Reserved.

package com.cloudsourcing.android.cloudsourcing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by yada on 7/5/15.
 */
public class LoginFragment extends Fragment{

    private CallbackManager mCallbackManager;
    private LoginButton mFacebookLoginButton;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mSubmitButton;
    private Button mSignUpButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCallbackManager = CallbackManager.Factory.create(); //initializing the callback function for the Facebook login.
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceWState) {
        View v = inflater.inflate(R.layout.log_in_fragment, container, false);
        mUsernameEditText = (EditText) v.findViewById(R.id.username_edit_text); //The user can put in their username.
        mPasswordEditText = (EditText) v.findViewById(R.id.password_edit_text);// the user puts in their password.
        //  passwordAgainEditText  = (EditText) findViewById(R.id.password_edit_text_again);
        mSignUpButton = (Button) v.findViewById(R.id.signup_button); //signup button.
        mSignUpButton.setText("Sign up");
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSignUp(); //create an intent to start the signup fragment.
            }
            //i need ot tell it to kill the intent k
        });
        mSubmitButton = (Button) v.findViewById(R.id.submit_button); //submitting the button.
        mSubmitButton.setText("Login");
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String username = mUsernameEditText.getText().toString().trim();
                //getting the username from the actual editText.
                final String password = mPasswordEditText.getText().toString().trim();
                //getting the password from the actual editText.
                login(username, password); //logs the user in.

            }
        });
        mFacebookLoginButton = (LoginButton) v.findViewById(R.id.login_button);
        //finding view by ID for the login button.
        mFacebookLoginButton.setReadPermissions("user_friends");
        mFacebookLoginButton.setFragment(this); //setting the login button to the fragment.
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {//this retreives the user ID and token.
//tell the person that the person is already logged in.
                logInUser(); //creates a new user in parse.
                redirectToHome(); //redirecting to the cloud fragments.
                //from here, get the currentaccesstoken.
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
        return v; //returning the view.
    }


    private void logInUser(){
        //Retrieves information on current user,
        //see https://developers.facebook.com/docs/graph-api/using-graph-api/v2.3
        //for more info.
        AccessToken accessToken = AccessToken.getCurrentAccessToken(); //getting teh current access token.
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                       // Log.e(TAG, object.toString());
                       // mSubmitButton.setText(object.toString()); //TODO may have error.)
                        //check if there is already
                        logInParseUser(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id"); //you only need ID and name.
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void logInParseUser(JSONObject object){
        //creates a new parse user table with the user ID as teh username.
        try {
            final String userId = object.getString("id");
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", userId);
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        // The query was successful.
                        login(userId, "A password");  //login the user.
                    } else {
                        // Create a new user.
                    }
                }
            });

            ParseUser user = ParseUser.getCurrentUser(); //getting the current user
            if (user == null) {
                //if there is no user, means that the user is new, sign the user up.
                ParseUser newUser = new ParseUser();
                newUser.setUsername(String.valueOf(userId)); //username is userId;
                newUser.setPassword("A password"); //this is simply necessary to create a parse account.

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        //handle the response.
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(getActivity(), e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            // redirect to the cloud page.
                            Intent intent = new Intent(getActivity(), GameActivity.class); //create intent.
                            startActivity(intent); //starting the intent.

                        }
                    }

                });
            }
        }catch(org.json.JSONException e){

        }
    }

    private void switchToSignUp() {
        //switching to th signup fragment.
        Fragment newFragment = new SignUpFragment();
        //replacing the login fragment with teh signup fragment.
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void login(String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (parseUser != null) {
                    // Hooray! The user is logged in
                    Toast.makeText(getActivity(), "You are logged in!", Toast.LENGTH_LONG).show();
                    redirectToHome(); //redirects to the cloud activity.

                } else {
                    // Login failed!
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //creating the context.
                    builder.setMessage(e.getMessage())
                            .setTitle("Oops!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    //error message.
                }
               ; //true becomes false, false becomes true
               // Log.i(TAG, "THE USER IS LOGGED IN?"+ Login.getoggedIn());
            }
        });
    }

    private void redirectToHome(){
        Intent intent = new Intent(getActivity(), GameActivity.class); //create intent.
        startActivity(intent); //starting the intent.
    }
    @Override
    public void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(getActivity());
    }


    @Override //when the intent is returned from the login fragment, the activity puts the callback
    //to handle the fb data.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

