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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class DetailsActivity extends AppCompatActivity {

    // global variables
    Recipe recipe;
    String title;
    SharedPreferences preferences;
    ArrayList<String> titles;
    DatabaseReference reference;
    Recipes recipes;
    ImageView im;
    TextView titleView;
    TextView attributeView;
    TextView ingredientView;
    FirebaseUser user;
    FirebaseDatabase database;
    int signInType;
    int comparison;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsactivity);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // tracks activity
        preferences.edit().putInt("Activity", 7).commit();
        // gets current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        titles = new ArrayList<>();
        // initialize views to hold recipe data
        im = (ImageView) findViewById(R.id.imageView1);
        im.setLayoutParams(new RelativeLayout.LayoutParams(600, 600));
        titleView = (TextView) findViewById(R.id.Title);
        ingredientView = (TextView) findViewById(R.id.ingredients);
        attributeView = (TextView) findViewById(R.id.attributes);
        // checks what activity user comes from
        final int requestCode = getIntent().getIntExtra("activity", 0);
        // returns recipe from previous activity
        recipe = recipeReturned(requestCode);
        if (recipe != null) {
            Gson gson = new Gson();
            String json = gson.toJson(recipe);
            preferences.edit().putString("recipe", json).commit();
        }
        // gets recipe that was viewed before application was closed
        if (recipe == null) {
            recipe = getRecipeFromPreviousUsage();
        }
        // sets up view with recipe
        setupDetails(recipe);
    }

    /* loads image into imageview */
    public void initializeImage(String url, ImageView imageView) {
        Picasso.with(getApplicationContext()).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("Tag", "Success");
            }

            @Override
            /* load failed */
            public void onError() {
                Log.d("Tag", "Error loading image");
                Toast.makeText(getApplicationContext(), "Error loading image...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* adds a given recipe to favorites list */
    public void goToFavorites(View view) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        int signInType = preferences.getInt("signintype", 0);
        // adds recipe to database
        if (signInType != 4 && signInType != 0) {
            addRecipesToDB();
            // adds recipe to shared preferences
        } else {
            addRecipesToSharedPreferences();
        }
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

    /* formats recipe data and puts title, ingredients and attributes in activity */
    public void setupDetails(Recipe recipe) {
        // grabs recipe data
        String url = recipe.getImage();
        title = recipe.getTitle();
        ArrayList<String> attributes = recipe.getAttributes();
        String ingredients = String.valueOf(recipe.getIngredients());
        // initialize image
        initializeImage(url, im);
        // set title
        titleView.setText(title);
        // formatting ingredient data
        ingredients = ingredients.replace('[', ' ');
        ingredients = ingredients.replace(']', ' ');
        String[] ingredientsArray = ingredients.split(",");
        for (int i = 0; i < ingredientsArray.length - 1; i++) {
            ingredientsArray[i] = ingredientsArray[i].replaceAll("\"", " ").replace("[", " ").
                    replaceAll("]", " ").replaceAll(",", " ");
        }
        // formatting attribute data
        attributeView.setText(attributes.toString().replaceAll("\"", " ").
                replace("[", " ").replace("]", " ").replace("{", " ").replace("}", " "));
        // further formatting and setting it to the textview
        ingredientView.setText("Ingredients: " + Arrays.toString(ingredientsArray).replace("[", "")
                .replace("]", "").replace("\"", ""));
    }

    /* if recipe exists add to shared preferences. if it doesn't exist then add it */
    public Recipe getRecipeFromPreviousUsage() {
        Gson gson = new Gson();
        if (recipe != null) {
            gson = new Gson();
            String json = gson.toJson(recipe);
            // put recipe in shared preferences
            preferences.edit().putString("recipe", json).commit();
            // puts recipe data in activity
            setupDetails(recipe);
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

    public void addRecipeToFavorites(){
        Gson gson = new Gson();
        RecipeCompare compare = new RecipeCompare();
        int recipeCompare = 0;
        for (Recipe r : recipes.getRecipes()) {
            recipeCompare = compare.compare(recipe, r);
        }
        if (recipeCompare == 0) {
            // adds recipe to recipes object
            recipes.getRecipes().add(recipe);
            // put recipes in shared preferences
            String jsonData = gson.toJson(recipes);
            preferences.edit().putString("recipeLocalUser", jsonData).commit();
            Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Item exists", Toast.LENGTH_SHORT).show();
        }
    }

    /* adds recipe to database */
    public void addRecipesToDB() {
        reference = database.getReference().child("Users").child(user.getUid()).child("Recipes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (user != null) {
                    RecipeCompare compare = new RecipeCompare();
                    Recipe recipeDatabase = new Recipe();
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        recipeDatabase = s.getValue(Recipe.class);
                        comparison = compare.compare(recipeDatabase, recipe);
                    }
                }
                    if(comparison == 0){

                            reference.push().setValue((recipe)
                                    , new DatabaseReference.CompletionListener() {

                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else{
                        Toast.makeText(getApplicationContext(), "Item exists", Toast.LENGTH_SHORT).show();
                    }
            }


                                    @Override
            /* adding to database failed */
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("Tag", "An error of type" + databaseError + "occured");
                                    }
                                });
                }
    /**
     * adds recipe for local user to shared preferences
     */
    public void addRecipesToSharedPreferences() {
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
    @Override
    protected void onResume(){
        super.onResume();
        // gets sign in type
        Utils utils = new Utils(this);
        signInType = utils.getSignInType();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}