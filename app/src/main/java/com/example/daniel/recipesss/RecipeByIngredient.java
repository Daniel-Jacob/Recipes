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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/* Activity that lets user search for recipes that match all the given ingredients */
public class RecipeByIngredient extends AppCompatActivity implements AsyncWithInterface.
        AsyncResponse, GoogleApiClient.OnConnectionFailedListener {
    // global variables
    SearchView searchView;
    SharedPreferences preferences;
    GoogleSignIn signIn;
    Utils utilities;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_by_ingredient);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        searchView = (SearchView) findViewById(R.id.searchview);
        String query = preferences.getString("query", "");
        searchView.setQuery(query, true);
        utilities = new Utils(this);
        // initializes listener
        OnQueryTextListener onQueryTextListener = new OnQueryTextListener(this);
        // sets listener
        searchView.setOnQueryTextListener(onQueryTextListener);
        // track activity
        preferences.edit().putInt("Activity", 4).commit();
    }
    @Override
    /* if there are recipes send them to next activity */
    public void processFinish(Recipes output) {
        // recipes found
        utilities.returnRecipesToGridview(output);
    }
    /* goes to favorites */
    public void favorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }
    @Override
    /* connection with google api client failed */
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Tag", connectionResult.getErrorMessage());
        Toast.makeText(getApplicationContext(), "Oops... something went wrong",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        signIn = new GoogleSignIn(this);
        signIn.buildApiClient();
        // connects Google api client
        signIn.googleApiClient.connect();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // sets logout or sign up button based on sign in type
        utilities.setLogoutOrSignOutButton((Button) findViewById(R.id.Loginandlogout));
        int activity = preferences.getInt("Activity", 0);
        // recreate activity if user comes from another activity
        if(activity != 4){
            String query = preferences.getString("query", "");
            searchView.setQuery(query, true);
            recreate();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        signIn.googleApiClient.stopAutoManage(this);
        // disconnects google api client
        signIn.googleApiClient.disconnect();
    }
    /* logs user out */
    public void loginOrLogout(View view) {
        int signInType = utilities.getSignInType();
        // google user
        if (signInType == 1) {
            signIn.signOut();
        }
        else{
            // other user
            utilities.signoutOrSignUp();
        }
    }
    /* goes to general recipe search */
    public void generalRecipes(View view) {
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        // make progressbar invisible
        progressBar.setVisibility(View.INVISIBLE);
        String query = searchView.getQuery().toString();
        preferences.edit().putString("query",query).commit();
    }
}