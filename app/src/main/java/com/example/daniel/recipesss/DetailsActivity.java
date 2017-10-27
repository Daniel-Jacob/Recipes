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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
/* grabs title, ingredients, attributes and image of a given recipe and adds these to the activity */
public class DetailsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // global variables
    Recipe recipe;
    SharedPreferences preferences;
    DatabaseReference reference;
    Recipes recipes;
    ImageView imageView;
    TextView titleView;
    TextView attributeView;
    TextView ingredientView;
    FirebaseUser user;
    FirebaseDatabase database;
    int signInType;
    int comparison;
    GoogleSignIn googleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsactivity);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // tracks activity
        preferences.edit().putInt("Activity", 7).commit();
        // gets current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        // initialize views to hold recipe data
        imageView = (ImageView) findViewById(R.id.imageView1);
        // sets layout parameters
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(600, 600));
        // initializes textviews
        titleView = (TextView) findViewById(R.id.Title);
        ingredientView = (TextView) findViewById(R.id.ingredients);
        attributeView = (TextView) findViewById(R.id.attributes);
        // checks what activity user comes from
        final int requestCode = getIntent().getIntExtra("activity", 0);
        // returns recipe from that activity
        recipe = recipeReturned(requestCode);
        // if there is a recipe then save it
        if (recipe != null) {
            Gson gson = new Gson();
            String json = gson.toJson(recipe);
            preferences.edit().putString("recipe", json).commit();
        } else {
            // application has been shut down so retrieve saved recipe
            recipe = getRecipeFromPreviousUsage();
        }
        // sets up view with the recipe
        setupDetails(recipe);
    }

    /* returns the recipe that has been clicked in the previous activity */
    public Recipe recipeReturned(int requestCode) {
        if (requestCode == 0) {
            recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
        } else if (requestCode == 1) {
            recipe = (Recipe) getIntent().getSerializableExtra("Element");
        } else if (requestCode == 2) {
            recipe = (Recipe) getIntent().getSerializableExtra("titleactivity");
        } else {
            Toast.makeText(this, "unknown activity", Toast.LENGTH_LONG).show();
        }
        return recipe;
    }

    /* if recipe exists add to shared preferences. if it doesn't exist then add it */
    public Recipe getRecipeFromPreviousUsage() {
        Gson gson = new Gson();
        if (recipe != null) {
            gson = new Gson();
            String json = gson.toJson(recipe);
            // put recipe in shared preferences
            preferences.edit().putString("recipe", json).commit();
        } else {
            // gets type
            Type type = new TypeToken<Recipe>() {
            }.getType();
            // gets recipe
            String json = preferences.getString("recipe", "");
            recipe = gson.fromJson(json, type);
        }
        return recipe;
    }

    /* formats recipe data and puts title, ingredients and attributes in activity */
    public void setupDetails(Recipe recipe) {
        // grabs recipe data
        String url = recipe.getImage();
        String title = recipe.getTitle();
        ArrayList<String> attributes = recipe.getAttributes();
        String ingredients = String.valueOf(recipe.getIngredients());
        // binds image to imageview
        initializeImage(url, imageView);
        // sets title
        titleView.setText(title);
        // formatting ingredient data
        ingredients = ingredients.replace('[', ' ');
        ingredients = ingredients.replace(']', ' ');
        String[] ingredientsArray = ingredients.split(",");
        for (int i = 0; i < ingredientsArray.length - 1; i++) {
            ingredientsArray[i] = ingredientsArray[i].replaceAll("\"", " ").
                    replace("[", " ").replaceAll("]", " ").
                    replaceAll(",", " ");
        }
        // formatting attribute data
        attributeView.setText(attributes.toString().replaceAll("\"", " ").
                replace("[", " ").replace("]", " ").replace("{", " ").replace("}", " "));
        // further formatting of ingredient data and setting it to the textview
        ingredientView.setText("Ingredients: " + Arrays.toString(ingredientsArray).replace("[", "")
                .replace("]", "").replace("\"", ""));
    }

    /* loads image into imageview */
    public void initializeImage(String url, ImageView imageView) {
        Picasso.with(getApplicationContext()).load(url).into(imageView, new Callback() {
            @Override
            /* image loaded */
            public void onSuccess() {
                Log.d("Success:", "Image succesfully loaded");
            }

            @Override
            /* load failed */
            public void onError() {
                Log.d("Error", "Error loading image");
                Toast.makeText(getApplicationContext(), "Error loading image...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* adds a given recipe to favorites list */
    public void goToFavorites(View view) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        int signInType = preferences.getInt("signintype", 0);
        // authenticated user
        if (signInType != 4 && signInType != 0) {
            if (user != null) {
                // adds recipe to database
                addRecipeToDB();
            } else {
                // authenticated user has not been loaded yet
                recreate();
            }
            // local user, so add recipe to shared preferences
        } else {
            fetchRecipesForLocalUser();
        }
    }

    /* adds recipe to database */
    public void addRecipeToDB() {
        // initialize action button
        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.favorites);
        // make it unclickable until recipe is added to database
        button.setClickable(false);
        reference = database.getReference().child("Users").child(user.getUid()).child("Recipes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // recipes already exist in database
                if (dataSnapshot.getValue() != null) {
                    // user exists
                    if (user != null) {
                        RecipeCompare compare = new RecipeCompare();
                        Recipe recipeDatabase;
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            // get recipes from database
                            recipeDatabase = s.getValue(Recipe.class);
                            // compare recipes from database with current clicked recipe
                            comparison = compare.compare(recipeDatabase, recipe);
                            // recipe exists so don't add
                            if (comparison == 1) {
                                Toast.makeText(getApplicationContext(), recipeDatabase.getTitle() + " " + "exists", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        // recipe doesn't exist, so add
                        if (comparison == 0) {
                            reference.push().setValue(recipe);
                            Toast.makeText(getApplicationContext(), recipe.getTitle() + " " + "added", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // add recipe to database
                    reference.push().setValue(recipe);
                    Toast.makeText(getApplicationContext(), recipe.getTitle() + " " + "added", Toast.LENGTH_SHORT).show();
                }
                // recipe has been added so make button clickable again
                button.setClickable(true);
            }

            @Override
            /* adding to database failed */
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Tag", "An error of type" + databaseError + "occured");
            }
        });
    }

    /*  adds recipe for local user to shared preferences */
    public void fetchRecipesForLocalUser() {
        String json = preferences.getString("recipeLocalUser", "");
        // if recipes exist get them first
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<Recipes>() {
            }.getType();
            recipes = gson.fromJson(json, type);
        }
        // otherwise make net instance of recipes
        else {
            recipes = new Recipes();
        }
        addRecipeToFavorites();
    }

    /* adds recipe to shared preferences */
    public void addRecipeToFavorites() {
        Gson gson = new Gson();
        RecipeCompare compare = new RecipeCompare();
        int recipeCompare = 0;
        for (Recipe r : recipes.getRecipes()) {
            recipeCompare = compare.compare(recipe, r);
        }
        // add to shared preferences if it doesn't exist yet
        if (recipeCompare == 0) {
            recipes.getRecipes().add(recipe);
            String jsonData = gson.toJson(recipes);
            preferences.edit().putString("recipeLocalUser", jsonData).commit();
            Toast.makeText(getApplicationContext(), recipe.getTitle() + " " + "added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), recipe.getTitle() + " " + "exists", Toast.LENGTH_SHORT).show();
        }
    }

    /* signs user out if authenticated */
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

    /* goes to favorites */
    public void favorites(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
        preferences.edit().putBoolean("details", true).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Connection failed", connectionResult.getErrorMessage());
        Toast.makeText(this, "Connection to google server has failed",
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
    /* sets up button according to sign in type */
    protected void onResume() {
        super.onResume();
        // gets sign in type
        Utils utils = new Utils(this);
        signInType = utils.getSignInType();
        // sets button text according to sign in type
        Button button = (Button) findViewById(R.id.Loginandlogout);
        if (signInType == 4) {
            button.setText("Sign up");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleUser.googleApiClient.stopAutoManage(this);
        // disconnects Google api client
        googleUser.googleApiClient.disconnect();
    }

    @Override
    /* goes to DisplayRecipes */
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DisplayRecipes.class);
        startActivity(intent);
    }
}