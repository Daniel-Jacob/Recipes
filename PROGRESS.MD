This application is a recipe finder application. It helps users to find general recipes and recipes by ingredient. These recipes are requested from yummly api. Authenticated users can store their recipes in a firebase database, whereas a local user can save recipes locally. The starting screen of the application looks like this:
<div align="center">
        <img width="25%" height = "25%" align = "left" src="doc/download.png" alt="image" title="image"</img
</div>












The app starts off with the screen displayed above. As evident, the application supports Facebook, Google, and E-mail sign in. If a person does not want to sign in, he or she can continue as a local user. These options are provided so a user can save recipes in a favorites list. If a user is authenticated then recipes are saved in a firebase database. This has the advantage that the favorites list can also be accessed from a different device. A local user can save his or her recipes in a favorites list, but this is only accesible on the given device. 

After login the user is navigated to the activity where general recipes can be done. This means that a user can do a query and this query can match anything in the recipe, like title, ingredients, or attributes. If the user clicks on ingredient-based recipes then he or she is sent to a screen that looks similar, but when a query is done, this query needs to completely match the ingredients of a given recipe. In both cases (if there are any recipes) the user is sent to the next activity. Here the recipes are displayed as images in a gridview. If a user wishes to see the items as a list of titles then the user can click the 'list titles' button. 

In both activities it is the case when the user clicks a recipe then the user is navigated to the recipe itself, which displays the details of that recipe. The recipe can be added by clicking the plus button. This can only be done once, since the application does not allow duplicate entries. To view the favorites, the user can click the favorites button. These favorites are displayed in a listview. 

AsyncWithInterface:

This class implements the asynctask that is performed in both recipe activities. This is done by making use of an interface. This choice has been made, because with a regular asynctask a lot of code needs to be copied and put in a seperate file, because the context of a specific activity is passed. 

<b>Functions</b>

- onPreExecute: code that is run before the asynctask is performed. Not really used, because progressbar is set up in  onQueryTextListener class

- doInBackground: function to download recipes from yummly api endpoint
- onPostExecute:sends downloaded recipes to processFinish function (implemented in RecipeActivity and RecipeByIngredient activity . 
- recipesReturned
- initializeRecipes
- formatImageLink
