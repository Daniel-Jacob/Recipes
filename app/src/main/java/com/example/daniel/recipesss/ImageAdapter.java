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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * sets images that are grabbed from yummly API into a gridview
 */
public class ImageAdapter extends BaseAdapter {
    // global variables
    private Context context;
    ArrayList<Recipe> data;
    String url;
    int layout;

    // constructor
    public ImageAdapter(Context c, int gridItemLayout, ArrayList<Recipe> data) {
        context = c;
        this.data = data;
        this.layout = gridItemLayout;
        ImageView imageView;
    }
    /** gets number of items */
    public int getCount() {
        return data.size();
    }
    /** gets item an given position */
    public Object getItem(int position) {
        return data.get(position);
    }
    /** gets item id */
    public long getItemId(int position) {
        return position;
    }
    /** create a new ImageView for each item referenced by the Adapter */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        // gets image and loads it into imageview
        url = data.get(position).getImage();
        loadImageData(url, imageView);
        return imageView;
    }
    public void loadImageData(String url, ImageView imageView){
        if (url != null) {
            Picasso.with(context)
                    .load(url)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            System.out.print("image downloaded successfully");
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }


    }
}