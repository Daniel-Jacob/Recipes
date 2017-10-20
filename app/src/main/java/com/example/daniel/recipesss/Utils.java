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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/** Utilities class that includes searches
 *  onclickeventlisteners and signout
 *  options that occur more than once */
public class Utils extends Activity implements  View.OnClickListener {
    Context context;
    View view;
    int activity;
    SharedPreferences preferences;
    RecipeAdapter adapter;
    int signInType;
    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseUser user;
    Recipe recipe;
    Recipes recipes;
    Recipes recipesLongClick;

    // constructor
    public Utils(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        signInType = getSignInType();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public int getSignInType() {
        int signInType = preferences.getInt("signintype", 0);
        return signInType;
    }


    /**
     * sends recipes to gridview
     */
    public void ToGridview(Recipes recipes) {
        Intent intent = new Intent(context, DisplayRecipes.class);
        intent.putExtra("Data", recipes);
        context.startActivity(intent);
    }

    /**
     * signs user out of facebook, email or goes to sign up screen
     */
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

    public void returnRecipesToGridview(Recipes output) {
        if (output.getRecipes().size() > 0) {
            ToGridview(output);
        } else {
            // no recipes found so try again
            Toast.makeText(context, "No recipes found", Toast.LENGTH_LONG).show();
            ((Activity) context).recreate();
        }

    }

    /**
     * redirects user to the activity
     * the user was in before the app
     * was closed
     */
    public void redirectUserToCorrectActivity() {
        int activity = preferences.getInt("Activity", 0);
        switch (activity) {

            case 2:
                Intent registrationIntent = new Intent(context, RegistrationActivity.class);
                context.startActivity(registrationIntent);
                break;
            case 3:
                Intent recipeActivity = new Intent(context, RecipeActivity.class);
                context.startActivity(recipeActivity);
                break;
            case 4:
                Intent recipeByIngredient = new Intent(context, RecipeByIngredient.class);
                context.startActivity(recipeByIngredient);
                break;
            case 5:
                Intent displayRecipes = new Intent(context, DisplayRecipes.class);
                context.startActivity(displayRecipes);
                break;
            case 6:
                Intent titleActivity = new Intent(context, TitleActivity.class);
                context.startActivity(titleActivity);
                break;
            case 7:
                Intent detailsActivity = new Intent(context, DetailsActivity.class);
                context.startActivity(detailsActivity);
                break;
            case 8:
                Intent favorites = new Intent(context, FavoritesActivity.class);
                context.startActivity(favorites);
                break;
        }
    }


    @Override
    /** listens button clicks of sign in options */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInButton:
                GoogleSignIn signIn = new GoogleSignIn(context);
                signIn.signOut();
                // attempt facebook sign in
            case R.id.loginButton:
                Intent intent = new Intent();
                startActivityForResult(intent, FacebookSdk.getCallbackRequestCodeOffset());
                break;
            // goes to email registration and login screen
            case R.id.emailsignin:
                Intent intent1 = new Intent(context, RegistrationActivity.class);
                startActivity(intent1);
                break;
            // local user
            case R.id.textView:
                preferences.edit().putInt("signintype", 4).apply();
                Intent localUser = new Intent(context, RecipeActivity.class);
                startActivity(localUser);
        }
    }

    /* checks if user is logged in or signed out
    if the user is logged in login is displayed
    in the button and otherwise signup is displayed */
    public void setLogoutOrSignOutButton(Button button) {
        inflateLayout();
        int signInType = preferences.getInt("signintype", 0);
        // nobody is logged in
        if (signInType == 4) {
            button.setText("Sign up");
        } else {
            button.setText("Log out");
        }
    }

    public void inflateLayout() {
        int activity = preferences.getInt("Activity", 0);
        switch (activity) {
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.activity_recipeactivity, null);
                break;
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.activity_recipe_by_ingredient, null);
                break;
            case 5:
                view = LayoutInflater.from(context).inflate(R.layout.activity_gridview, null);
                break;
            case 6:
                view = LayoutInflater.from(context).inflate(R.layout.activity_titleactivity, null);
                break;
            case 7:
                view = LayoutInflater.from(context).inflate(R.layout.activity_detailsactivity, null);
                break;
            case 8:
                view = LayoutInflater.from(context).inflate(R.layout.activity_favorites, null);
                break;

        }

    }

    public void runOnUiThread(Activity activity) {
        final Activity myActivity = activity;

        new Thread(new Runnable() {
            @Override
            public void run() {
                final int activity = preferences.getInt("Activity", 0);
                myActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // inflates layout
                        inflateLayout();
                        myActivity.findViewById(R.id.indeterminateBar).setVisibility(View.VISIBLE);
                        Button button = (Button) myActivity.findViewById(R.id.Loginandlogout);
                        setLogoutOrSignOutButton(button);


                    }
                });

            }
        }).start();
    }
}