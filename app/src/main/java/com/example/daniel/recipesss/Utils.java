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
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
/* Utilities class that includes searches onclickeventlisteners and signout options that occur
 more than once */
public class Utils extends Activity {

    // global variables
    Context context;
    View view;
    int activity;
    SharedPreferences preferences;
    int signInType;
    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseUser user;
    Recipe recipe;
    Recipes recipes;

    // constructor
    public Utils(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        signInType = getSignInType();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    /* gets sign in type */
    public int getSignInType() {
        int signInType = preferences.getInt("signintype", 0);
        return signInType;
    }

    /* signs user out of facebook, email or goes to sign up screen */
    public void signoutOrSignUp() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int signInType = preferences.getInt("signintype", 0);
        if (signInType == 2) {
            LoginManager.getInstance().logOut();
            FirebaseAuth.getInstance().signOut();
            preferences.edit().putInt("Activity", 1).commit();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        } else if (signInType == 3) {
            FirebaseAuth user = FirebaseAuth.getInstance();
            user.signOut();
            preferences.edit().putInt("Activity", 1).commit();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        } else if (signInType == 4) {
            preferences.edit().putInt("Activity", 1).commit();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    /* error handling before recipes are sent to gridview */
    public void returnRecipesToGridview(Recipes output) {
        ArrayList<String> values = new ArrayList<>();
        if (output.getRecipes().size() > 0) {
            // removes elements with duplicate titles
            for (int i = 0; i < output.getRecipes().size(); i++) {
                values.add(output.getRecipes().get(i).getTitle());
                for (int j = i + 1; j < output.getRecipes().size(); j++) {
                    if (output.getRecipes().get(i).getTitle().equals(output.getRecipes().get(j).getTitle()))
                        output.getRecipes().remove(output.getRecipes().get(j));
                }
            }
            // removes last row if elements of row are less than three
            if (output.getRecipes().size() % 3 != 0) {
                if (output.getRecipes().size() % 3 == 1) {
                    output.getRecipes().remove(output.getRecipes().get(output.getRecipes().size() - 1));
                } else if (output.getRecipes().size() % 3 == 2) {
                    output.getRecipes().remove(output.getRecipes().get(output.getRecipes().size() - 1));
                    output.getRecipes().remove(output.getRecipes().get(output.getRecipes().size() - 2));
                }
            }
        }
            if (output.getRecipes().size() > 0) {
                // send elements to gridview
                ToGridview(output);
            } else {
                // no recipes found so try again
                Toast.makeText(context, "No recipes found", Toast.LENGTH_LONG).show();
                ((Activity) context).recreate();
            }
        }

    /* sends recipes to gridview */
    public void ToGridview(Recipes recipes) {
        Intent intent = new Intent(context, DisplayRecipes.class);
        intent.putExtra("Data", recipes);
        context.startActivity(intent);
    }

    /* checks if user came from a previous activity or user shut down application
      and recipes need to be retrieved */
    public Recipes addOrFetchRecipes(Recipes recipes) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
        return recipes;
    }

    public void goToDisplayActivity(){
        Intent intent = new Intent(getApplicationContext(), DisplayRecipes.class);
        startActivity(intent);
    }

    /* user shut down app before so redirect user to where app was closed */
    public void redirectUserToCorrectActivity(int activity) {
        this.activity = activity;
        Intent intent;
        switch (activity) {
            case 2:
                intent = new Intent(context, RegistrationActivity.class);
                context.startActivity(intent);
                break;
            case 3:
                intent = new Intent(context, RecipeActivity.class);
                context.startActivity(intent);
                break;
            case 4:
                intent = new Intent(context, RecipeByIngredient.class);
                context.startActivity(intent);
                break;
            case 5:
                intent = new Intent(context, DisplayRecipes.class);
                context.startActivity(intent);
                break;
            case 6:
                intent = new Intent(context, TitleActivity.class);
                context.startActivity(intent);
                break;
            case 7:
                intent = new Intent(context, DetailsActivity.class);
                context.startActivity(intent);
                break;
            case 8:
                intent = new Intent(context, FavoritesActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    /* sets up logout or sign up button and progressbar */
    public void setupProgressBar(final Activity activity) {
        final Activity myActivity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myActivity.findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

}