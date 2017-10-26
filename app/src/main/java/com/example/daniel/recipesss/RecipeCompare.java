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

import java.util.Comparator;

/* compares two recipes and returns 1 if the recipe is equal */
public class RecipeCompare implements Comparator<Recipe> {

    @Override
    /* compares two recipes */
    public int compare(Recipe o1,Recipe o2) {
        /* two recipes are equal if all the fields are the same */
        if(o1.getTitle().equals(o2.getTitle()) && o1.getImage().equals(o2.getImage())
                && o1.getAttributes().equals(o2.getAttributes()) &&
                o1.getIngredients().equals(o2.getIngredients())){
            return 1;
        }
        else{
            return 0;
        }
    }
}