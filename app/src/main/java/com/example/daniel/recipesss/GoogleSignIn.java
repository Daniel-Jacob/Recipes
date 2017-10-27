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
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
/* helper class to sign user into google */
public class GoogleSignIn extends MainActivity implements GoogleApiClient.OnConnectionFailedListener, Serializable {

    // global variables
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    GoogleApiClient googleApiClient;
    SharedPreferences preferences;
    GoogleSignInOptions gso;
    Context context;
    FirebaseAuth auth;
    FirebaseUser user;
    String googleClientId = "818367032142-kjv7mqeb242bdvq35jg3v5cnblelke7r.apps.googleusercontent.com";

    // constructor
    public GoogleSignIn(Context c) {
        this.context = c;
        auth = FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    /* builds googleapiclient */
    public GoogleApiClient buildApiClient() {
        if (googleApiClient == null) {
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail().requestIdToken(googleClientId)
                    .build();
            googleApiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage((FragmentActivity) context, (GoogleApiClient.OnConnectionFailedListener) context)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        return googleApiClient;
    }
    /* sign user in and redirects to next activity */
    public void handleSignInResult(Context context, GoogleSignInResult result) {
        this.context = (MainActivity) context;
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            // here the user data is put in firebase database
            String user = acct.getId();
            reference.child("Users").child(user).push().setValue(user);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            // tracks sign in type
            preferences.edit().putInt("signintype", 1).commit();
            // redirect user to next activity
            Intent intent = new Intent(this.context, RecipeActivity.class);
            context.startActivity(intent);
        }
    }
    /* authenticates google user with firebase */
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // sign in succesfull
                        if (task.isSuccessful()) {
                            // Sign in success, gets firebase user
                            Log.d("Tag", "signInWithCredential:success");
                            // get user
                            user = auth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            preferences.edit().putInt("Activity", 1).commit();
                        }
                    }
                });
    }
    /* signs user out */
    public void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // signs user out of firebase
                        FirebaseAuth.getInstance().signOut();
                        // go back to sign in activity
                        Intent intent = new Intent(context, MainActivity.class);
                        preferences.edit().putInt("Activity", 1).commit();
                        context.startActivity(intent);
                    }
                });
    }

    @Override
    // connection failed so display message
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),
                "A connection error occured...", Toast.LENGTH_SHORT).show();
    }
}