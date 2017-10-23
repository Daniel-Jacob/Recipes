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
import android.widget.ProgressBar;
import android.widget.TextView;
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
    // global variables
    Context context;
    Utils utils;
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
        this.myActivity = (FavoritesActivity) context;
        listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
    }

    /* fetches favorites from database */
    public void fetchFavorites(Recipes recipesFetch) {
        this.recipes = recipesFetch;

        if (user != null) {
            // fetches favorites from firebase
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                        // puts recipes in recipes object
                        recipe = snapshot.getValue(Recipe.class);
                        recipes.getRecipes().add(recipe);
                    }
                    // sets recipe adapter
                    setAdapter((FavoritesActivity) context, recipes);
                }

                @Override
                /* grabbing favorites got cancelled */
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "A database error occured... Please try " +
                            "again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /* gets recipes user based on sign in type */
    public Recipes recipesUser(int signInType) {
        // authenticated user
        if (signInType != 4 && signInType != 0) {
            if(user != null) {
                fetchFavorites(recipes);
            }
            // local user
        } else if (signInType == 4) {
            Gson gson = new Gson();
            // gets recipes
            String json = preferences.getString("recipeLocalUser", "");
            Type type = new TypeToken<Recipes>() {
            }.getType();
            recipes = gson.fromJson(json, type);
            // sets adapter on recipes
            setAdapter((FavoritesActivity) context, recipes);
            // error handling
        } else {
            Toast.makeText(context, "Oops... something went wrong", Toast.LENGTH_SHORT).show();
        }
        return recipes;
    }

    /* listens for click on favorites item */
    public void onItemClick(final Recipes recipess) {
        this.recipes = recipess;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // authenticated user
                if (signInType != 4) {
                    recipeDBbToDetailsActivity(position, recipes);
                } else {
                    // local user
                    toDetailsActivity(signInType, position);
                }
            }

        });
    }

    /* listens for long click and removes the clicked item from database and recipes object */
    public void listensForLongClickUIThread(Activity activity, final Recipes recipesLongClick) {
        this.recipesLongClick = recipes;
        Activity myActivity = activity;
        ListView listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // removes item from database
                if (signInType != 4) {
                    removeRecipeFromDB(position, recipesLongClick.getRecipes().get(position));
                }
                // local user, so remove from shared preferences
                else {
                    removeRecipeFromSharedPreferences(recipesLongClick, position);
                }
                return false;
            }
        });
    }

    /* sets recipe adapter on listview and sets up progressbar */
    public void setAdapter(FavoritesActivity activity, final Recipes recipesForAdapter) {
        recipes = new Recipes();
        this.recipes = recipesForAdapter;
        final Activity myActivity = activity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListView listView = (ListView) myActivity.findViewById(R.id.listviewwwww);
                        ProgressBar progressBar = (ProgressBar) myActivity.findViewById(R.id.indeterminateBar);
                        progressBar.setVisibility(View.VISIBLE);
                        adapter = new RecipeAdapter(context, R.layout.simple_list_itemmm, recipes);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        recipesIsEmpty(recipesForAdapter, progressBar);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }

    public void recipesIsEmpty(Recipes recipes, ProgressBar progressBar) {
        if (recipes.getRecipes() == null || recipes.getRecipes().isEmpty()) {
            listView.setEmptyView(myActivity.findViewById(R.id.empty_text_view));
            TextView textView = (TextView) myActivity.findViewById(R.id.Favorites);
            textView.setVisibility(View.INVISIBLE);

        }
    }

    public void recipeDBbToDetailsActivity(final int position, final Recipes recipes) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                    recipe = s.getValue(Recipe.class);
                }
                toDetailsActivity(signInType, position);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "A database error of type" +
                        databaseError + "occured", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void removeRecipeFromDB(final int position, final Recipe recipeRemoved) {
        final RecipeCompare compare = new RecipeCompare();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.child("Users").child(user.getUid()).child("Recipes").getChildren()) {
                    Recipe recipe = s.getValue(Recipe.class);
                    int compareRecipes = compare.compare(recipe, recipeRemoved);
                    if (compareRecipes == 1) {
                        Toast.makeText(context, recipesLongClick.getRecipes().get(position).
                                getTitle() + " " + "removed", Toast.LENGTH_SHORT).show();
                        recipesLongClick.getRecipes().remove(recipesLongClick.getRecipes().get(position));
                        s.getRef().removeValue();

                    }
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
            intent.putExtra("Recipe", recipes.getRecipes().get(position));
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
                Toast.makeText(context, recipesLongClick.getRecipes().
                        get(position) + " " + "removed", Toast.LENGTH_SHORT).show();
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
