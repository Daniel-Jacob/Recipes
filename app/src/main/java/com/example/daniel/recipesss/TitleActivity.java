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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/* Populates listview with titles */
public class TitleActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener {

    // global variables
    ListView listView;
    Recipes recipes;
    SharedPreferences preferences;
    Utils utilities;
    GoogleSignIn googleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titleactivity);
        listView = (ListView) findViewById(R.id.titles);
        // grabs recipe data from display recipes activity
        recipes = (Recipes) getIntent().getSerializableExtra("titles");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // tracks activity
        preferences.edit().putInt("Activity", 6).commit();
        // if there are recipes save them, if not retrieve them
        utilities = new Utils(this);
        if(recipes == null){
            recipes = utilities.addOrFetchRecipes(recipes);
        }
        // sets titles to listview
        setAdapter(recipes);
        // make listview clickable
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get clicked recipe and pass it to Detailsactivity
                Recipe recipe = recipes.getRecipes().get(position);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("titleactivity", recipe);
                // define what activity the recipe needs to be fetched from
                intent.putExtra("activity", 2);
                startActivity(intent);
            }
        });
    }

    /* adapter to add titles to listview and remove duplicate titles */
    public void setAdapter(Recipes recipes) {
        RecipeCompare recipeCompare = new RecipeCompare();
        for(int i = 0; i < recipes.getRecipes().size(); i++){
            for(int j = 0; j < i -1; j++){
                int compare = recipeCompare.compare(recipes.getRecipes().get(j), recipes.getRecipes().get(i));
                if(compare == 1){
                    recipes.getRecipes().remove(j);
                }
            }
        }
        // sets adapter
        ListView listView = (ListView) findViewById(R.id.titles);
        RecipeAdapter adapter = new RecipeAdapter(this, R.layout.simple_list_itemmm, recipes);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /* if user is logged in, log out */
    public void logout(View view) {
        int signInType = utilities.getSignInType();
        // google user
        if (signInType == 1) {
            googleUser.signOut();
        } else {
            // other user
            utilities.signoutOrSignUp();
        }
    }

    /* goes to favorites */
    public void favorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
        preferences.edit().putBoolean("titleactivity", true).commit();
        preferences.edit().putBoolean("displayARecipe", false).commit();
        preferences.edit().putBoolean("display", false).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Connection failed", connectionResult.getErrorMessage());
        Toast.makeText(this, "connection with google server has failed",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleUser = new GoogleSignIn(this);
        // builds Google api client
        googleUser.buildApiClient();
        // connects Google api client
        googleUser.googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences.edit().putBoolean("titleactivity", true).commit();
        preferences.edit().putBoolean("displayARecipe", false).commit();
        preferences.edit().putBoolean("display", false).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils utilities = new Utils(this);
        // sets button text according to sign in type
        Button button = (Button)findViewById(R.id.Loginandlogout);
        int signInType = utilities.getSignInType();
        if(signInType == 4){
            button.setText("Sign up");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleUser.disconnectFromApi(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DisplayRecipes.class);
        startActivity(intent);
    }
}