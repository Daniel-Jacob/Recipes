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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.firebase.auth.FirebaseUser;

/** activity that lets user search for general recipes */
public class RecipeActivity extends AppCompatActivity
        implements AsyncWithInterface.AsyncResponse, OnConnectionFailedListener {
    // global variables
    SearchView searchView;
    SharedPreferences preferences;
    FirebaseUser user;
    ProgressBar progressBar;
    Utils utilities;
    GoogleSignIn signIn;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeactivity);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // tracks activity
        preferences.edit().putInt("Activity", 3).commit();
        // initialize searchview
        searchView = (SearchView) findViewById(R.id.searchview);
        utilities = new Utils(this);
        // initializes litener
        OnQueryTextListener onQueryTextListener = new OnQueryTextListener(this);
        // listens for an entered query
        searchView.setOnQueryTextListener(onQueryTextListener);
    }
    /* signs user out */
    public void loginOrLogout(View view) {
        int signInType = utilities.getSignInType();
        // google user
        if (signInType == 1) {
            // signs google user out
            signIn.signOut();
        }
        else{
            // signs in other user
            utilities.signoutOrSignUp();
        }
    }

    /* goes to recipe by ingredient activity */
    public void recipeByIngredient(View view) {
        Intent intentByIngredient = new Intent(this, RecipeByIngredient.class);
        startActivity(intentByIngredient);
    }
    /* goes to favorites */
    public void favorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        signIn = new GoogleSignIn(this);
        // builds Google api client
        signIn.buildApiClient();
        // connects Google api client
        signIn.googleApiClient.connect();
        // adds authentication listener for email
        EmailSignIn emailSignIn = new EmailSignIn(getApplicationContext());
        emailSignIn.mAuth.addAuthStateListener(emailSignIn.listener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        // make progressbar invisible
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        signIn.googleApiClient.stopAutoManage(this);
        // disconnects Google api client
        signIn.googleApiClient.disconnect();
    }
    @Override
    /* displays message according to signintype */
    public void onBackPressed() {
       // gets sign in type
        int signInType = utilities.getSignInType();
        // user is signed in
        if (user != null) {
            Toast.makeText(getApplicationContext(),
                    "You are already signed in. Logout to go to the previous screen",
                    Toast.LENGTH_LONG).show();
        }
        // user is not signed in
        else if(signInType == 4){
            Toast.makeText(this, "Click signup to create an account", Toast.LENGTH_SHORT).show();
        }
        else {
            // user doesnt belong in activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    /* goes to next activity if there are recipes */
    public void processFinish(Recipes output) {
        utilities.returnRecipesToGridview(output);
    }

    @Override
    // connection failed
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {Toast.makeText
            (getApplicationContext(), "Oops.. something went wrong", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // sets logout or sign up button based on sign in type
        utilities.setLogoutOrSignOutButton((Button) findViewById(R.id.Loginandlogout));
        int activity = preferences.getInt("Activity", 0);
        // user comes from different activity so recreate so query can be submitted
        if(activity != 3){
            recreate();
        }
    }
}