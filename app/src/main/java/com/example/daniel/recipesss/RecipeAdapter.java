package com.example.daniel.recipesss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



/* adapter that puts an arraylist of taskobjects on listview */
public class RecipeAdapter extends ArrayAdapter<Recipe> {
        private Recipes recipes = new Recipes();
        Recipe recipe;
        int comparator = 0;
    // constructor
    public RecipeAdapter(Context context, int textViewResourceId, Recipes recipes) {
        super(context, textViewResourceId, recipes.getRecipes());
        this.recipes = recipes;
        if(recipes.getRecipes().isEmpty() || recipes.getRecipes() == null){
           recipes = new Recipes();

        }

    }

    @Override
    /* sets titles of recipes on listview */
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        // inflate view
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.simple_list_itemmm, null);
        }
        // get recipe
        recipe = recipes.getRecipes().get(position);
        if (recipe != null) {

                    // set title of recipe on row of listview
                    TextView textView = (TextView) v.findViewById(R.id.text);
                    textView.setText(recipe.getTitle());
                }
        return v;
    }
}