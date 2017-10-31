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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* modelclass for recipes */
public class Recipes extends Recipe implements Serializable {
    // fields
    ArrayList<String> titles;
    List<Recipe> Recipes = new ArrayList<Recipe>();

    // getters and setters
    public void setRecipes(List<Recipe> recipes) {
        this.Recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return Recipes;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }
}