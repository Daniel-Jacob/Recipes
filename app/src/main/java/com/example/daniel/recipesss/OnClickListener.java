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
import android.preference.PreferenceManager;
import android.view.View;

import com.facebook.FacebookSdk;
/* listener that checks if one of the loginbuttons is clicked */
public class OnClickListener implements View.OnClickListener {

    // global variables
    MainActivity context;
    SharedPreferences preferences;
    int signInType;

    // constuctor
    public OnClickListener(MainActivity context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        signInType = preferences.getInt("signintype", 0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // attempt facebook sign in
            case R.id.loginButton:
                Intent intent = new Intent();
                context.startActivityForResult(intent, FacebookSdk.getCallbackRequestCodeOffset());
                break;
            // goes to email registration and login screen
            case R.id.emailsignin:
                Intent intent1 = new Intent(context, RegistrationActivity.class);
                context.startActivity(intent1);
                break;
            // local user
            case R.id.textView:
                preferences.edit().putInt("signintype", 4).apply();
                Intent localUser = new Intent(context, RecipeActivity.class);
                context.startActivity(localUser);
        }
    }
}