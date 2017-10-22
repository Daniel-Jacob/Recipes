package com.example.daniel.recipesss;

import java.util.Comparator;

/**
 * Created by daniel on 22-10-2017.
 */

public class RecipeCompare implements Comparator<Recipe> {

    @Override
    public int compare(Recipe o1,Recipe o2) {
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
