# Design Recipes4you

The Recipes4you application has 3 methods of authentication: E-mail, Facebook, and google. However authentication is not mandatory,
therefore a user can choose not to authenticate. If the user authenticates, then favorites will be stored in Firebase. 
This means the user can access his or her favorites from another device. If the user doesn't choose to authenticate then the favorites
will only be available locally (i.e. just on the given device).

The chosen API is Spoonacular. This API was chosen because of the support for ingredient-based search and general search. 
Furthermore it supports autocomplete, and has detailed information about recipes that can be used to provide the user with
this information when they click the given recipe. 

<b>Problem</b>

spoonacular api documentation is hosted on mashape which is currently experiencing some technical difficulties (i.e. sometimes it works and sometimes it does not. I will try to get the documentation, but if not possible as soon as possible I need to consider a different API ).

-- update --
Chosen API for now seems like yummly, since the documentation still does not load

Still problems with spoonacular

<b>Classes and methods:</b>

1. Facebook login class: This class proceeds with login using facebook authentication
2. Google login class: This class proceeds with login using google authentication 
3. E-mail registration/login class: This class proceeds with login or registration using E-mail 
and stores the  registrered user in firebase
- createAccount function to create user
- signInWithEmailAndPassword function to sign the user in. 
4. Recipeclass: Here the user can search for general recipes
- getGeneralRecipes function to get a list of recipes from api
- list from getGeneralRecipes() will be stored in an instance of RecipeList modelclass
- populateGridview: uses image and title getters and setters of modelclass to add these to the gridview
- getters and setters for Recipename, images, preparationtime, type of food, calories etc. 
(used to put fields into the activity that provides recipe info)
5. RecipeByIngredient class: The user can search for recipes by a list of products that he or she puts in
- RecipeByIngredient modelclass: stores the list of recipes by ingredient
- populateGridview: same as the recipeSearch class, but returned recipes are based on ingredients. 
- getters and setters for Recipename, images, preparationtime, type of food, calories etc. 
(used to put fields into the activity that provides recipe info)
6. Favorites class: stores, retrieves, deletes and marks certain recipes as eaten and not eaten
- method to add favorites to database if there is an authentication method
- if not then add recipe to sharedpreferences
- method to split the list in recipes that have been eaten and recipes that have not been eaten 

<b>Sketches</b>

<p align="center">
  <img src="loginandregistrationscreen.jpg"/>
  <img src="recipescreens.jpg"/>
  <img src="gridviewanddetails.jpg" width="350"/>
  <img src="favoritesdesign.jpg" width="350"/>
    

</p>

<b>API and data sources</b>

Spoonacular food api. Thiss also the main data source. It supports recipe by ingredient as well as regular recipe search. 

<b>Database tables and fields:</b>

At this point the reason for the database is to save favorites so they can be shared on multiple devices. The plan is to create two categories of favorites: 1. favorite recipes that have not been tried yet, and 2. Recipes that have been tried. Therefore 1 table will be created with the following fields:

1. Recipename | String
2. Date created | Datetime object
3. Type food | String
4. eaten/not eaten | Boolean

---------------------
This database list of under development

If there is more time left then I would like to add a shoppinglist. If this is possible I would need to create two tables and join them together. 
