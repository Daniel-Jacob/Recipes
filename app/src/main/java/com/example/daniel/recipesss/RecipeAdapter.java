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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/* adapter that puts an arraylist of taskobjects on listview */
public class RecipeAdapter extends ArrayAdapter<Recipe> {

    // objects
    private Recipes recipes = new Recipes();
    Recipe recipe;

    // constructor
    public RecipeAdapter(Context context, int textViewResourceId, Recipes recipes) {
        super(context, textViewResourceId, recipes.getRecipes());
        this.recipes = recipes;
        if(recipes.getRecipes().isEmpty() || recipes.getRecipes() == null){
            recipes = new Recipes();
        }
    }

    @Override
    /* sets titles of recipes on listview */
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        // inflate view
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.simple_list_itemmm, null);
        }
        // get recipe
        recipe = recipes.getRecipes().get(position);
        // set recipe title
        if (recipe != null) {
            TextView textView = (TextView) v.findViewById(R.id.text);
            textView.setText(recipe.getTitle());
        }
        return v;
    }
}