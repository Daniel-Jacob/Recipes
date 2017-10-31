/*
 * Copyright (C) 2015 Daniel Jacob
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
import android.widget.GridView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
/* Grabs recipes from asynctask and populates them in a gridview */
public class DisplayRecipesActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{

    // global variables
    ArrayList<Recipe> elements;
    Recipes recipes;
    GridView gv;
    SharedPreferences preferences;
    Utils utilities;
    GoogleSignIn googleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        // grabs recipe data from previous activity
        recipes = (Recipes) getIntent().getSerializableExtra("Data");
        // if there are recipes save them, otherwise fetch them from previous save
        utilities = new Utils(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // tracks activity
        preferences.edit().putInt("Activity", 5).commit();
        // query has been completed so make query variable empty
        preferences.edit().putString("query", "").commit();
        elements = new ArrayList<>();
        // initializes gridview
        gv = (GridView) findViewById(R.id.gridview);
        // sets adapter on image data
        setImageAdapter(recipes);
        // makes gridview clickable
        gv.setClickable(true);
        // listens for clicks on images
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // brings clicked recipe data to next activity
                Recipe recipe = elements.get(position);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                preferences.edit().putBoolean("displayARecipe", true).commit();
                preferences.edit().putBoolean("titleactivity", false).commit();
                intent.putExtra("Element", recipe);
                // track from what activity recipe is passed
                intent.putExtra("activity", 1);
                startActivity(intent);
            }
        });
    }

    /* sets image adapter */
    public void setImageAdapter(Recipes recipess){
        // recipes have been saved on previous application use
        recipes = utilities.addOrFetchRecipes(recipess);
        for (int i = 0; i < recipes.getRecipes().size(); i++) {
            // saves recipe to elements array
            Recipe recipe = recipes.getRecipes().get(i);
            elements.add(recipe);
        }
        ImageAdapter adapter = new ImageAdapter(this, R.layout.grid_item_layout, elements);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /* if user is logged in log user out */
    public void logout(View view) {
        Utils utils = new Utils(this);
        int signInType = utils.getSignInType();
        // google user
        if(signInType == 1){
            googleUser.signOut();
        }
        // other user
        utils.signoutOrSignUp();
    }

    /* send recipes to titleActivity */
    public void listTitles(View view) {
        Intent intent = new Intent(this, TitleActivity.class);
        intent.putExtra("titles", recipes);
        startActivity(intent);
    }

    /* send user to favoritesActivity */
    public void goToFavoritesActivity(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
        preferences.edit().putBoolean("display", true).commit();
        preferences.edit().putBoolean("displayARecipe", true).commit();
    }

    @Override
    /* google connection failed */
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Connection failed: ", connectionResult.getErrorMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleUser = new GoogleSignIn(this);
        googleUser.connectToApi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // update booleans for back button logic
        preferences.edit().putBoolean("titleactivity", false).commit();
        preferences.edit().putBoolean("displayARecipe", true).commit();
        preferences.edit().putBoolean("display", false).commit();
        preferences.edit().putBoolean("displayARecipeFromFavorites", false).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        utilities = new Utils(this);
        Button button = (Button)findViewById(R.id.Loginandlogout);
        // sets button text according to sign in type
        int signInType = utilities.getSignInType();
        if(signInType == 4){
            button.setText("Sign up");
        }
        // query has been submitted so empty query
        preferences.edit().putString("query", "").commit();
        // track activity
        preferences.edit().putInt("Activity", 5).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleUser.disconnectFromApi(this);
    }

    @Override
    /* send user back to recipeActivity or recipeByIngredientActivity */
    public void onBackPressed() {
        super.onBackPressed();
        boolean recipe = preferences.getBoolean("recipeActivity", false);
        boolean recipeByIngredient = preferences.getBoolean("recipeByIngredient", false);
        if(recipe) {
            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
            startActivity(intent);
        }
        else if(recipeByIngredient){
            Intent intent = new Intent(getApplicationContext(), RecipeByIngredientActivity.class);
            startActivity(intent);
        }
    }
}