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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/* Populates listview with titles */
public class TitleActivity extends AppCompatActivity {
    // global variables
    ListView listView;
    Recipes recipes;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titleactivity);
        listView = (ListView) findViewById(R.id.titles);
        // grabs recipe data from different activity
        recipes = (Recipes) getIntent().getSerializableExtra("titles");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // tracks activity
        preferences.edit().putInt("Activity", 6).commit();
        addOrFetchRecipes();
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
                intent.putExtra("activity", 2);
                startActivity(intent);
            }
        });
        Utils utils = new Utils(this);
        utils.loginOrLogout(this);
    }
    /* adapter to add titles to listview */
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
        // add recipe titles to arraylist
        ListView listView = (ListView) findViewById(R.id.titles);
        RecipeAdapter adapter = new RecipeAdapter(this, R.layout.simple_list_itemmm, recipes);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void logout(View view) {
        Utils utils = new Utils(this);
        utils.signoutOrSignUp();
    }
    public void addOrFetchRecipes() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // add recipes to sharedpreferences
        if (recipes != null) {
            Gson gson = new Gson();
            String json = gson.toJson(recipes);
            preferences.edit().putString("json", json).commit();
        } else {
            // gets recipes from sharedpreferences
            preferences.getString("json", null);
            Gson gson = new Gson();
            String json = preferences.getString("json", "");
            Type type = new TypeToken<Recipes>() {
            }.getType();
            recipes = gson.fromJson(json, type);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DisplayRecipes.class);
        startActivity(intent);
    }
}