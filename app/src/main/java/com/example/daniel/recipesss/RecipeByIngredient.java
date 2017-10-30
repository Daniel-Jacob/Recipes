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
    GoogleSignIn googleUser;
    Utils utilities;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_by_ingredient);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        searchView = (SearchView) findViewById(R.id.searchview);
        // gets not yet completed query
        String query = preferences.getString("query", "");
        // sets query to searchview
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

    /* logs user out */
    public void loginOrLogout(View view) {
        int signInType = utilities.getSignInType();
        // google user
        if (signInType == 1) {
            googleUser.signOut();
        } else {
            // other user
            utilities.signoutOrSignUp();
        }
    }

    /* goes to general recipe search */
    public void generalRecipes(View view) {
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        startActivity(intent);
    }

    /* goes to favorites */
    public void favorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
        preferences.edit().putBoolean("titleactivity", false).commit();
        preferences.edit().putBoolean("displayARecipe", false).commit();
        preferences.edit().putBoolean("display", false).commit();
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
        // builds google api client
        googleUser = new GoogleSignIn(this);
       // googleUser.buildApiClient();
        // connects Google api client
      //  googleUser.googleApiClient.connect();
        googleUser.connectToApi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        utilities.setupProgressBar(this);
        preferences.edit().putBoolean("recipeByIngredient", true).commit();
        preferences.edit().putBoolean("recipeActivity", false).commit();
        String query = searchView.getQuery().toString();
        preferences.edit().putString("query", query).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // sets logout or sign up button based on sign in type
        int signInType = utilities.getSignInType();
        Button button = (Button) findViewById(R.id.Loginandlogout);
        if (signInType == 4) {
            button.setText("Sign up");
        }
        int activity = preferences.getInt("Activity", 0);
        // user comes from different activity so recreate so query can be submitted
        if (activity != 4) {
            String query = preferences.getString("query", "");
            searchView.setQuery(query, true);
            recreate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleUser.googleApiClient.stopAutoManage(this);
        // disconnects google api client
        googleUser.googleApiClient.disconnect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // send user back to general recipe search activity
        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
        startActivity(intent);
    }
}