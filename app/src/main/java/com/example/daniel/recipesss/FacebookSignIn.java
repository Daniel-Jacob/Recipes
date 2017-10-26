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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Arrays;
/* Signs in facebook user */
public class FacebookSignIn extends AppCompatActivity {

    // global variables
    CallbackManager manager;
    LoginButton facebookLogin;
    Context context;
    SharedPreferences preferences;
    View view;
    int activity;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;

    // constructor
    public FacebookSignIn(Context c) {
        this.context = c;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // inflates view and initializes facebook login button
        view = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        facebookLogin = (LoginButton) view.findViewById(R.id.loginButton);
        // gets authentication instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }
    /* logs user in */
    public void createCallBack() {
        // creates callback manager
        manager = CallbackManager.Factory.create();
        // logs access token
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        // executes facebook async task
        registerCallBack();
    }
    /* executes asynctask to facebook api */
    public void registerCallBack() {
        facebookLogin.registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            // grabs user data
            public void onSuccess(final LoginResult loginResult) {
                // sets permissions
                facebookLogin.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "about_me"));
                // performs a graph request to get user data
                GraphRequest request = submitGraphRequest(loginResult);
                // async task to facebook api
                executeFacebookAsyncTask(request);
            }

            @Override
            /* request was cancelled */
            public void onCancel() {
                Log.d("Tag", "Authentication cancelled");
                Toast.makeText(context, "Authentication was cancelled...", Toast.LENGTH_SHORT).show();
            }

            @Override
            /* an error occured */
            public void onError(FacebookException error) {
                Log.d("Tag", "An error occured" + error);
                Toast.makeText(context, "An error occured.. Try again and if " +
                        "the problem persists try later", Toast.LENGTH_LONG).show();
            }
        });
    }
    /* submits a graph request for facebook user */
    public GraphRequest submitGraphRequest(final LoginResult result){
        GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    // autenticates user with firebase
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        handleFacebookAccessToken(result.getAccessToken());
                    }
                });
        return request;
    }
    /* executes asynctask */
    public void executeFacebookAsyncTask(GraphRequest request){
        Bundle parameters = new Bundle();
        // put user info in bundle
        parameters.putString("fields", "id,name,link");
        // use user info to execute async task
        request.setParameters(parameters);
        handleFacebookAccessToken(request.getAccessToken());
        request.executeAsync();
        Intent intent = new Intent(context, RecipeActivity.class);
        context.startActivity(intent);
    }
    /* authenticates user with firebase */
    public void handleFacebookAccessToken(AccessToken token) {
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        // signs facebook user in
        auth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, get firebase user
                            user = auth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}