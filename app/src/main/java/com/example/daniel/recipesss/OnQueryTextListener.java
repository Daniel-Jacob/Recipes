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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;



/**
 * listener for queries done in searchview
 */
public class OnQueryTextListener implements SearchView.OnQueryTextListener {
    SharedPreferences preferences;
    int counter;
    Context context;
    View view;
    // constructor
    public OnQueryTextListener(Context c) {
        context = c;
    }
    @Override
    /* searches query when it is submitted */
    public boolean onQueryTextSubmit(String query) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int activity = preferences.getInt("Activity", 0);
        // inflates layout
        if (activity == 3) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_recipeactivity, null);
        } else if (activity == 4) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_recipe_by_ingredient, null);
        }
        // executes async task
        search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /** method to do error checking
     * on query and execute asynctask */
    public boolean search(String query){
        // convert query to array of characters
        char[] characters = query.toCharArray();
        // loop over characters
        for (char c : characters) {
            // if character isnt a letter or a space
            if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                counter += 1;
                Toast.makeText(context,
                        "Search contains a non-alphabetical character", Toast.LENGTH_LONG).show();
                break;
            }
        }
        /** make sure asynctask
         is only executed once */
        if (counter == 0) {
            counter = counter + 1;
            // creates an asyntask
            new AsyncWithInterface((AsyncWithInterface.AsyncResponse) context).execute(query);
            Utils utils = new Utils(context);
            utils.runOnUiThread((Activity) context);

        }
        return false;
    }

}