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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Sets up the favorites of a given user */
public class FavoritesActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    // global variables
    SharedPreferences preferences;
    FirebaseDatabase database;
    DatabaseReference reference;
    Recipe recipe;
    ListView listView;
    FirebaseUser user;
    FirebaseAuth auth;
    int signInType;
    ArrayAdapter arrayAdapter;
    ProgressBar progressBar;
    Recipes recipes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        listView = (ListView) findViewById(R.id.listviewwwww);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        // shared preferences instance
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int signInType = preferences.getInt("signintype", 0);
        // tracks activity
        preferences.edit().putInt("Activity", 8);
        recipes = new Recipes();
        FavoritesHelper helper = new FavoritesHelper(this);
        recipes = helper.recipesUser(signInType);
        helper.onItemClick(this, recipes);
        helper.listensForLongClickUIThread(this, recipes);
        // gets firebase user
        arrayAdapter = new RecipeAdapter(this, R.layout.simple_list_itemmm, recipes);
        listView.setAdapter(arrayAdapter);
    }

    /**
     * removes recipe from
     * database and listview
     */

    @Override
    // connection has failed
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Connection failed...", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils utils = new Utils(this);
        signInType = utils.getSignInType();

    }
}
