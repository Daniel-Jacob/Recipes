/*
 * Copyright (C) 2015 Daniel Jacob
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

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

    /* class to get the context when in a static environment. Also helps to access googleapiclient
    globally Code used from https://stackoverflow.com/questions/2002288/static-way-to-get-
    context-on-android */
    public class MyApplication extends Application {
    // google api client
    GoogleApiClient googleApiClient;
        private static Context context;

        public void onCreate() {
            super.onCreate();
            // gets context in static environment
            MyApplication.context = getApplicationContext();
        }
        /* gets context in static environment */
        public static Context getAppContext() {
            return MyApplication.context;
        }
}