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
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.LENGTH_SHORT;
/** registers and signs user in. */
public class EmailSignIn extends Activity {

    // global variables
    Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SharedPreferences preferences;
    FirebaseUser user;

    // constructor
    public EmailSignIn(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    // authentication listener
    FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("TAG", "onAuthStateChanged:signed_out");
            }
        }
    };
    /* creates user account with emailaddress and password */
    public void createAccount(final String email, final String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) this.context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            // display message if task failed
                            if (!task.isSuccessful()) {
                                Log.w("Tag", "Create account:failed", task.getException());
                                Toast.makeText(context, task.getException().getMessage(),
                                        LENGTH_SHORT).show();
                            } else {
                                // sign user in
                                signInWithEmailAndPassword(email, password);
                            }
                        }
                    });
        }
        else{
            Toast.makeText(context, "Please fill in your password and E-mail",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /* signs user in with email and password */
    public void signInWithEmailAndPassword(String email, final String password) {
        // get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
            // if user is authenticated sign in
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) this.context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                            // gets current user
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            // save sign in type
                            preferences.edit().putInt("signintype", 3).commit();
                            // authentication failed
                            if (!task.isSuccessful()) {
                                Log.w("Tag", "signInWithEmail:failed", task.getException());
                                Toast.makeText(context, "Sign in failed. You don't have an account " +
                                                "or the password is invalid",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // success: redirect to next activity
                                Intent intent = new Intent(context, RecipeActivity.class);
                                context.startActivity(intent);
                            }
                        }
                    });
    }
}