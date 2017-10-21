package com.example.daniel.recipesss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by daniel on 19-10-2017.
 */

public class FavoritesHelper {
    Context context;
    Utils utils;
    RecipeAdapter recipeAdapter;
    View view;
    FirebaseDatabase database;
    DatabaseReference reference;
    Recipe recipe;
    FirebaseUser user;
    RecipeAdapter adapter;
    Recipes recipes;
    SharedPreferences preferences;
    int signInType;
    Recipes recipesLongClick;
    Activity myActivity;
    ListView listView;

    public FavoritesHelper(Context context) {
        this.context = context;
        utils = new Utils(context);
        view = LayoutInflater.from(context).inflate(R.layout.activity_favorites, null);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recipes = new Recipes();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        signInType = utils.getSignInType();
        this.myActivity = (FavoritesActivity)context;
        listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
    }

    public void fetchFavorites(final Recipes recipes) {

        if (user != null) {
            // fetches favorites from firebase
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                        recipe = snapshot.getValue(Recipe.class);
                        recipes.getRecipes().add(recipe);

                    }
                    setAdapter((FavoritesActivity) context, recipes);

                }

                @Override
                /** grabbing favorites got cancelled */
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "A database error occured... Please try " +
                            "again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public Recipes recipesUser(int signInType) {
        if (signInType != 4 && signInType != 0) {
            fetchFavorites(recipes);

        } else if (signInType == 4) {
            Gson gson = new Gson();
            String json = preferences.getString("recipeLocalUser", "");
            Type type = new TypeToken<Recipes>() {
            }.getType();
            recipes = gson.fromJson(json, type);
            setAdapter((FavoritesActivity) context, recipes);
        } else {
            Toast.makeText(context, "Oops... something went wrong", Toast.LENGTH_SHORT).show();
        }
        return recipes;
    }

    public void onItemClick(Recipes recipess) {
        this.recipes = recipess;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (signInType != 4) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        // fetches favorites
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot s : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                                Recipe recipe = s.getValue(Recipe.class);
                                // sends the recipe to detailsactivity
                                findRecipe(recipe, recipes, position);
                                toDetailsActivity(signInType, position);
                            }
                        }
                        @Override
                        /** an error occured while grabbing the recipe */
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context, "A database error of type" +
                                    databaseError + "occured", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    toDetailsActivity(signInType, position);
                }
            }

        });
    }

    public void listensForLongClickUIThread(Activity activity, final Recipes recipesLongClick) {
        this.recipesLongClick = recipes;Activity myActivity = activity;
        ListView listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (signInType != 4) {

                    removeRecipeFromDB(position);
                }
                // local user
                else {
                    removeRecipeFromSharedPreferences(recipesLongClick, position);
                }
                return false;
            }
        });
    }


    public void setAdapter(final FavoritesActivity activity, final Recipes recipes) {
        final Activity myActivity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListView listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
                        adapter = new RecipeAdapter(context, R.layout.simple_list_itemmm, recipes);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    public boolean findRecipe(Recipe recipe, Recipes recipes, int position) {
        if (recipe != null && recipe.getTitle().equals(recipes.getRecipes().get(position).getTitle())
                && recipe.getImage().equals(recipes.getRecipes().get(position).getImage())
                && recipe.getIngredients().equals(recipes.getRecipes().get(position).getIngredients())
                && recipe.getAttributes().equals(recipes.getRecipes().get(position).getAttributes())) {
            return true;
        }
        return false;
    }

    public void removeRecipeFromDB(final int position){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 for(DataSnapshot s : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                    Recipe recipe = s.getValue(Recipe.class);
                    // remove title from database
                    findRecipe(recipe, recipesLongClick, position);
                    s.getRef().removeValue();
                    recipesLongClick.getRecipes().remove(recipesLongClick.getRecipes().get(position));
                     break;
                }
                setAdapter((FavoritesActivity) context, recipesLongClick);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void toDetailsActivity(int signInType, int position) {
        if (signInType != 4) {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("Recipe", recipe);
            context.startActivity(intent);
        } else {
            FavoritesHelper helper = new FavoritesHelper(context);
            recipes = helper.recipesUser(signInType);
            recipe = recipes.getRecipes().get(position);
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("Recipe", recipe);
            context.startActivity(intent);
        }
    }

    public boolean removeRecipeFromSharedPreferences(Recipes recipesLongClick, int position) {
        for (int i = 0; i < recipesLongClick.getRecipes().size(); i++) {

            if (recipesLongClick.getRecipes().get(i).equals(recipesLongClick.getRecipes().get(position))) {
                recipesLongClick.getRecipes().remove(i);
            }
            Gson gson = new Gson();
            String json = gson.toJson(recipesLongClick);
            preferences.edit().putString("recipeLocalUser", json).commit();

        }
        setAdapter((FavoritesActivity) context, recipesLongClick);
        return false;
    }
}