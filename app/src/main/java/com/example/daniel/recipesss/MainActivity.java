/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.daniel.recipesss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
Starting screen that provides google and facebook sign in options */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.
        OnConnectionFailedListener, View.OnClickListener {
    // global variables
    private static final int RESULT = 1;
    SharedPreferences preferences;
    GoogleSignIn initializeGoogleUser;
    FacebookSignIn initializeFacebookUser;
    GoogleApiClient googleApiClient;
    FirebaseAuth auth;
    FirebaseUser user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creates sharedpreferences instance
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        auth = FirebaseAuth.getInstance();
        // builds api client
        initializeGoogleUser = new GoogleSignIn(this);
        initializeGoogleUser.buildApiClient();
        googleApiClient = initializeGoogleUser.googleApiClient;
        // creates callback to facebook API
        initializeFacebookUser = new FacebookSignIn(this);
        initializeFacebookUser.createCallBack();
        // listeners for login buttons
        OnClickListener listener = new OnClickListener(this);
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.emailsignin).setOnClickListener(listener);
        findViewById(R.id.textView).setOnClickListener(listener);
        // update activity
        preferences.edit().putInt("Activity", 1).commit();
    }
    @Override
    /* signs user into google other sign in methods handled elsewhere in utils class  */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                signIn();
                break;
        }
    }
    @Override
    /* checks result of authentication */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // google user
            if (requestCode == RESULT) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                initializeGoogleUser.handleSignInResult(this, result);
                // facebook user
            } else if (requestCode == FacebookSdk.getCallbackRequestCodeOffset()) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                initializeFacebookUser.manager.onActivityResult(requestCode, resultCode, data);
                // adds sign in type to sharedpreferences
                preferences.edit().putInt("signintype", 2).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /* sign in method for google */
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        this.startActivityForResult(signInIntent, RESULT);
    }
    @Override
    /* google connection has failed */
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Connection", "The connection had" + connectionResult + "error");
        Toast.makeText(this, "An error occured... Try to sign in again or try later...",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    /* connects google api client */
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // gets current user
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               user = firebaseAuth.getCurrentUser();
            }
        });
    }
    @Override
    /* disconnects google api client */
    protected void onStop() {
        super.onStop();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}