package com.example.daniel.recipesss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by daniel on 20-10-2017.
 */


// adapter that puts an arraylist of taskobjects on listview
public class RecipeAdapter extends ArrayAdapter<Recipe> {
        private Recipes recipes = new Recipes();
        Recipe recipe;


    public RecipeAdapter(Context context, int textViewResourceId, Recipes recipes) {
        super(context, textViewResourceId, recipes.getRecipes());
        this.recipes = recipes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = vi.inflate(R.layout.simple_list_itemmm, null);
        }

        recipe = recipes.getRecipes().get(position);
        if (recipe != null) {
            TextView textView = (TextView) v.findViewById(R.id.text);

            textView.setText(recipe.getTitle());

        }
        return v;
    }
}
