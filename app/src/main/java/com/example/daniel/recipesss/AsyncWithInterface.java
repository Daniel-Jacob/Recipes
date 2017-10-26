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

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/* creates an asynctask from yummly api and returns recipes to processfinish function */
public class AsyncWithInterface extends AsyncTask<String, Integer, String > {

    // global variables
    Context context;
    Recipes recipes;
    String imageLink;
    String[] imageLinkTrimmed;
    String cleanImageLink;
    JSONObject recipe = null;

    /* interface for asynctask */
    public interface AsyncResponse {
        void processFinish(Recipes output);
    }

    /* after asynctask is complete point to processFinish function */
    public AsyncResponse delegate = null;

    public AsyncWithInterface(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    /* code before download */
    @Override
    protected void onPreExecute() {
    }

    @Override
    /* recipes are being downloaded */
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    @Override
    /* converts jsondata into recipes object and goes to processFinish */
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // returns recipe object from json data
        recipes = recipesReturned(result);
        // send object to process finish
        delegate.processFinish(recipes);
    }

    /* returns recipes from json data */
    public Recipes recipesReturned(String result) {
        JSONObject myJSON;
        recipes = new Recipes();
        try {
            // creates jsonobject
            myJSON = new JSONObject(result);
            // gets json array
            JSONArray object;
            object = myJSON.getJSONArray("matches");
            // creates recipes object
            recipes = initializeRecipes(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    /* returns recipes object from json array */
    public Recipes initializeRecipes(JSONArray array) {
        Recipes recipes = new Recipes();
        for (int i = 0; i < array.length(); i++) {
            try {
                // gets a recipe
                recipe = (JSONObject) array.get(i);
                // get title of recipe
                String recipeName = String.valueOf(recipe.get("recipeName"));
                // gets image link
                imageLink = String.valueOf(recipe.get("imageUrlsBySize"));
                // formats image link
                imageLink = formatImageLink(imageLink);
                // variables to hold recipe data
                ArrayList<String> ingredients = new ArrayList<>();
                ArrayList<String> attributes = new ArrayList<>();
                ingredients.add(String.valueOf(recipe.get("ingredients")));
                attributes.add(String.valueOf(recipe.get("attributes")));
                // recipe object
                Recipe recipeObject = new Recipe();
                // sets recipe data of a given recipe
                recipeObject.setImage(cleanImageLink);
                recipeObject.setTitle(recipeName);
                recipeObject.setIngredients(ingredients);
                recipeObject.setAttributes(attributes);
                // adds recipe to recipes object
                recipes.Recipes.add(recipeObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }
    /* formats imagelink */
    public String formatImageLink(String imageLink) {
        // splits imagelink
        imageLinkTrimmed = imageLink.split(":");
        // removes characters that make link invalid
        cleanImageLink = imageLinkTrimmed[1] + ":" + imageLinkTrimmed[2];
        cleanImageLink = cleanImageLink.substring(1, cleanImageLink.length() - 2);
        return cleanImageLink;
    }
}