package com.example.daniel.recipesss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.facebook.FacebookSdk;

/*listener that checks if one of the loginbuttons is clicked */
public class OnClickListener implements View.OnClickListener {
    Context context;
    SharedPreferences preferences;
    // constuctor
    public OnClickListener(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // attempt facebook sign in
            case R.id.loginButton:
                Intent intent = new Intent();
                ((Activity) context).startActivityForResult(intent, FacebookSdk.getCallbackRequestCodeOffset());
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