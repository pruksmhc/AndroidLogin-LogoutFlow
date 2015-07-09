package com.cloudsourcing.android.cloudsourcing;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by yada on 7/3/15.
 */
public class SignUpFragment extends Fragment {
    //This is the registration activity one. It's not too challenging.
    //THis is the login page.
    //when someone logs in, it must retrieve their actual information from Cloudcode and then store it in local storage.
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private Button submitButton;
    private String TAG = "com.cloudsourcing.android.cloudsourcing.signupfragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceWState) {
        View v = inflater.inflate(R.layout.sign_up_fragment, container, false);
        usernameEditText = (EditText) v.findViewById(R.id.sign_up_username_edit_text);
        passwordEditText = (EditText) v.findViewById(R.id.sign_up_password_edit_text);
        passwordAgainEditText  = (EditText) v.findViewById(R.id.sign_up_password_edit_text_again);
        submitButton =(Button) v.findViewById(R.id.sign_up_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                signup();
            }
        });
        return v;
    }

    private void signup() {
        final String username = usernameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        String passwordAgain = passwordAgainEditText.getText().toString().trim();

        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }
        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }
        if (!password.equals(passwordAgain)) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        if (validationError) {
         // Toast.makeText(getActivity(), validationErrorMessage.toString(), Toast.LENGTH_LONG)
               //   .show();
            return;
        }
//after all of this, it is finally ready to log in the user into the crowd.
        ParseUser user = new ParseUser(); //creating a parse user.
        user.setUsername(username); //settingthe parseuser with setUsername
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
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
                    Log.e(TAG, "WE DID DIT");

                }
            }

        });
    }
}
