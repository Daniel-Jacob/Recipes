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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/* activity to register or login user through email */
public class RegistrationActivity extends AppCompatActivity {
    // global variables
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences preferences;
    EditText email;
    EditText passwrd;
    EmailSignIn emailSignIn;
    FirebaseUser user;
    String emailAddress;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationactivity);
        // gets instance of firebase database
        database = FirebaseDatabase.getInstance();
        // gets pointer to database
        reference = database.getReference();
        // instance of email sign in/registration class
        emailSignIn = new EmailSignIn(this);
        // creates sharedpreferences instance
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // tracks activity
        preferences.edit().putInt("Activity", 2).commit();
        // email and password fields
        email = (EditText) findViewById(R.id.email);
        passwrd = (EditText) findViewById(R.id.password);
    }
    /* registers user */
    public void submit(View view) {
        // grabs text of email and password
        emailAddress = email.getText().toString();
        password = passwrd.getText().toString();
        // tries to register user
        emailSignIn.createAccount(emailAddress, password);
        // user exists to add to database
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String userId = user.getUid();
            // put user data in database
            reference.child("Users").child(userId).setValue(emailSignIn.getUserData());
        }
    }
    /* signs user in with email and password */
    public void logMeIn(View view) {emailSignIn.signInWithEmailAndPassword(emailAddress, password);}

    @Override
    protected void onStart() {
        super.onStart();
        // add authentication listener
        emailSignIn.mAuth.addAuthStateListener(emailSignIn.listener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // track last activity
        preferences.edit().putInt("Activity", 2).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // stops authentication listener
        if(emailSignIn.listener != null){
            emailSignIn.mAuth.removeAuthStateListener(emailSignIn.listener);
        }
    }
}