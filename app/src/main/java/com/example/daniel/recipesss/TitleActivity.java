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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
/** Populates listview with titles */
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
        // sets titles to listview
        setAdapter(recipes);
        // make listview clickable
        listView.setClickable(true);
        // listen for click event
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
    }

    /**
     * adapter to add titles to listview
     */
    public void setAdapter(Recipes recipes) {
        // add recipe titles to arraylist
        ListView listView = (ListView) findViewById(R.id.titles);
        ArrayList<String> titles = new ArrayList<>();
        for (Recipe s : recipes.getRecipes()) {
            titles.add(s.getTitle());
        }
        // sets title adapter
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_itemmm, titles);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}