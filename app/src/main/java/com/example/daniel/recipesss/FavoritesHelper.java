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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.lang.reflect.Type;

public class FavoritesHelper {

    // global variables
    Context context;
    Utils utils;
    View view;
    FirebaseDatabase database;
    DatabaseReference reference;
    Recipe recipe;
    FirebaseUser user;
    RecipeAdapter adapter;
    Recipes recipes;
    SharedPreferences preferences;
    int signInType;
    Recipes recipesLongClick;
    Activity myActivity;
    ListView listView;

    // constructor
    public FavoritesHelper(Context context) {
        this.context = context;
        utils = new Utils(context);
        view = LayoutInflater.from(context).inflate(R.layout.activity_favorites, null);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recipes = new Recipes();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        signInType = utils.getSignInType();
        this.myActivity = (FavoritesActivity) context;
        listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
    }
    /* fetches favorites from database */
    public void fetchFavorites(Recipes recipesFetch) {
        this.recipes = recipesFetch;
        setProgressBar((Activity) context);
        if (user != null) {
            // fetches favorites from firebase
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                        // puts recipes in recipes object
                        recipe = snapshot.getValue(Recipe.class);
                        recipes.getRecipes().add(recipe);
                    }
                    // sets recipe adapter
                    setAdapter((FavoritesActivity) context, recipes);
                }

                @Override
                /* grabbing favorites got cancelled */
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "A database error occured... Please try " +
                            "again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /* gets recipes user based on sign in type */
    public Recipes recipesUser(int signInType) {
        // authenticated user
        if (signInType != 4 && signInType != 0) {
            // so fetch favorites from database
            fetchFavorites(recipes);
            // local user
        } else if (signInType == 4) {
            Gson gson = new Gson();
            // gets recipes
            String json = preferences.getString("recipeLocalUser", "");
            Type type = new TypeToken<Recipes>() {
            }.getType();
            recipes = gson.fromJson(json, type);
            if(recipes == null){
                recipes = new Recipes();
            }
            // sets adapter on recipes
            setAdapter((FavoritesActivity) context, recipes);
            // error handling
        } else {
            Toast.makeText(context, "Oops... something went wrong", Toast.LENGTH_SHORT).show();
        }
        return recipes;
    }
    /* listens for click on favorites item */
    public void onItemClick(final Recipes recipess) {
        this.recipes = recipess;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // authenticated user
                if (signInType != 4) {
                    recipeDBbToDetailsActivity(position);
                } else {
                    // local user
                    toDetailsActivity(signInType, position);
                }
            }

        });
    }
    /* listens for long click and removes the clicked item from database and recipes object */
    public void onLongClick(Activity activity, final Recipes recipesLongClick) {
        this.recipesLongClick = recipes;
        Activity myActivity = activity;
        ListView listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // removes item from database and from recipes object
                if (signInType != 4) {
                    removeRecipeFromDB(recipesLongClick.getRecipes().get(position));
                    recipesLongClick.getRecipes().remove(position);
                }
                else {
                    // local user, so remove from shared preferences and recipes object
                    removeRecipeFromSharedPreferences(recipesLongClick, position);
                }
                return false;
            }
        });
    }
    /* removes recipe from database */
    public void removeRecipeFromDB(final Recipe recipeRemoved) {
        final RecipeCompare compare = new RecipeCompare();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                    Recipe recipe = s.getValue(Recipe.class);
                    // double check if two recipes are equal
                    int compareRecipes = compare.compare(recipe, recipeRemoved);
                    if (compareRecipes == 1) {
                        Toast.makeText(context, recipe.getTitle() + " " + "removed", Toast.LENGTH_SHORT).show();
                        // remove value
                        s.getRef().removeValue();
                    }
                }
                // set adapter
                setAdapter((FavoritesActivity) context, recipesLongClick);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database error: ", databaseError.getDetails());
                Toast.makeText(context, "oops... something went wrong", Toast.LENGTH_SHORT).
                        show();
            }
        });
    }
    /* removes a given recipe from shared preferences */
    public boolean removeRecipeFromSharedPreferences(Recipes recipesLongClick, int position) {
        for (int i = 0; i < recipesLongClick.getRecipes().size(); i++) {
            // element found
            if (recipesLongClick.getRecipes().get(i).equals(recipesLongClick.getRecipes().get(position))) {
                Toast.makeText(context, recipesLongClick.getRecipes().get(i).getTitle() + " " + "removed", Toast.LENGTH_SHORT).show();
                // element removed
                recipesLongClick.getRecipes().remove(i);
            }
            Gson gson = new Gson();
            String json = gson.toJson(recipesLongClick);
            // update json string
            preferences.edit().putString("recipeLocalUser", json).commit();
        }
        // set adapter
        setAdapter((FavoritesActivity) context, recipesLongClick);
        return false;
    }
    /* grabs a recipe from the database and sends it to detailsActivity */
    public void recipeDBbToDetailsActivity(final int position) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                    recipe = s.getValue(Recipe.class);
                }
                toDetailsActivity(signInType, position);
            }
            @Override
            /* database retriaval cancelled */
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database error:", databaseError.getDetails());
                Toast.makeText(context, "A database error of type" +
                        databaseError + "occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /* sends recipe data to detailsactivity */
    public void toDetailsActivity(int signInType, int position) {
        // authenticated user
        if (signInType != 4) {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("Recipe", recipes.getRecipes().get(position));
            context.startActivity(intent);
        } else {
            // local user
            FavoritesHelper helper = new FavoritesHelper(context);
            recipes = helper.recipesUser(signInType);
            recipe = recipes.getRecipes().get(position);
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("Recipe", recipe);
            context.startActivity(intent);
        }
    }
    /* sets recipe adapter on listview and sets up progressbar */
    public void setAdapter(FavoritesActivity activity, final Recipes recipesForAdapter) {
        this.recipes = recipesForAdapter;
        final Activity myActivity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListView listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
                        ProgressBar progressBar = (ProgressBar) myActivity.findViewById(R.id.indeterminateBar);
                        progressBar.setVisibility(View.VISIBLE);
                        adapter = new RecipeAdapter(context, R.layout.simple_list_itemmm, recipes);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        recipesIsEmpty(recipesForAdapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        loginOrLogout((Activity) context);

                    }
                });
            }
        }).start();
    }
    /* displays textview when recipes list is empty */
    public void recipesIsEmpty(Recipes recipes){
        if(recipes.getRecipes().isEmpty()){
            listView.setEmptyView(myActivity.findViewById(R.id.empty_text_view));
            TextView textView = (TextView)myActivity.findViewById(R.id.Favorites);
            textView.setVisibility(View.INVISIBLE);
        }
    }
    /* displays log out or sign up button based on login status */
    public void loginOrLogout(Activity activity) {
        final Activity myActivity = activity;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final int signInType = preferences.getInt("signintype", 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int activity = preferences.getInt("Activity", 0);
                myActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // inflates layout
                        Button button = (Button) myActivity.findViewById(R.id.Loginandlogout);
                        // authenticated user
                        if(signInType != 4) {
                            button.setText("Log out");
                        }
                        // local user
                        else {
                            button.setText("Sign up");
                        }
                    }
                });
            }
        }).start();
    }
    /* sets progressbar */
    public void setProgressBar(Activity activity) {
        final Activity myActivity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                myActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // inflates layout
                        myActivity.findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }
}