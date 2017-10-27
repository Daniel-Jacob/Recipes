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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        // retrieves previously entered email and password
        String emailAddress = preferences.getString("email", "");
        String password = preferences.getString("password", "");
        email.setText(emailAddress);
        passwrd.setText(password);
    }

    /* registers user */
    public void submit(View view) {
        // grabs text of email and password
        emailAddress = email.getText().toString();
        password = passwrd.getText().toString();
        // tries to register user
        emailSignIn.createAccount(emailAddress, password);
    }

    /* signs user in with email and password */
    public void logMeIn(View view) {
        emailAddress = email.getText().toString();
        password = passwrd.getText().toString();
        if(!emailAddress.isEmpty() && !password.isEmpty()) {
            emailSignIn.signInWithEmailAndPassword(emailAddress, password);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please fill in your " +
                    "emailaddress and password", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    /* adds authentication state listener */
    protected void onStart() {
        super.onStart();
        emailSignIn.mAuth.addAuthStateListener(emailSignIn.listener);
    }

    @Override
    /* saves email and password before the app is closed */
    protected void onPause() {
        super.onPause();
        String emailAddress = email.getText().toString();
        String password = passwrd.getText().toString();
        preferences.edit().putString("email",emailAddress).commit();
        preferences.edit().putString("password", password).commit();
    }

    @Override
    /* removes authentication state listener */
    protected void onStop() {
        super.onStop();
        if(emailSignIn.listener != null){
            emailSignIn.mAuth.removeAuthStateListener(emailSignIn.listener);
        }
    }

    @Override
    /* navigates user to starting screen */
    public void onBackPressed() {
        super.onBackPressed();
        // tracks where user is in app
        preferences.edit().putInt("Activity", 1).commit();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}