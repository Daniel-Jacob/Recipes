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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/* Sets up the favorites of a given user */
public class FavoritesActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // global variables
    SharedPreferences preferences;
    FirebaseDatabase database;
    DatabaseReference reference;
    Recipe recipe;
    ListView listView;
    FirebaseUser user;
    FirebaseAuth auth;
    int signInType;
    ProgressBar progressBar;
    Recipes recipes;
    Utils utilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        listView = (ListView) findViewById(R.id.listviewwwww);
        auth = FirebaseAuth.getInstance();
        // get current user
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        // initialize progressbar
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // gets sign in type
        signInType = preferences.getInt("signintype", 0);
        // tracks activity
        preferences.edit().putInt("Activity", 8).commit();
        recipes = new Recipes();
        // authenticated user, but has not been loaded yet
        if(user == null && signInType != 4){
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            recreate();
            progressBar =(ProgressBar)findViewById(R.id.indeterminateBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        // creates favoriteshelper
        FavoritesHelper helper = new FavoritesHelper(this);
        // gets recipes of user
        recipes = helper.recipesUser(signInType);
        // listens for click on favorites item
        helper.onItemClick(recipes);
        // listens for a long click on favorites item
        helper.onLongClick(this, recipes);
    }

    @Override
    /* connection with google api client has failed */
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Connection failed...", Toast.LENGTH_SHORT).show();
    }
    /* logs user out if authenticated */
    public void logout(View view) {
        Utils utils = new Utils(this);
        utils.signoutOrSignUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // sets button text according to sign in type
        Button button = (Button)findViewById(R.id.Loginandlogout);
        utilities = new Utils(this);
        int signInType = utilities.getSignInType();
        if(signInType == 4){
            button.setText("Sign up");
        }
    }

    @Override
    /* navigate user back to recipeActivity */
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DisplayRecipes.class);
        startActivity(intent);
    }
}