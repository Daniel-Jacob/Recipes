day 1: 
- made email login. Facebook login works partially and google not yet, will continue tomorrow. 

day 2: 
- all login options work. I still need to make logout buttons. I will do this later. First I want the api calls and the conversion of the json data into objects to work. I will continue with this tomorrow.

day 3: 
- Calls to API work. Data is being converted to json, put into recipes object and sent to gridview activity. Tomrrow I will make sure try the images are displayed in the gridview. Also had first meeting with Renske. Talked about design document. She gave me a few pointers

-	provide option for user to register later on in the application
- Registrationactivity:Two password fields if the user is trying to register. Otherwise ust one field. 
-	Maybe a function to add a sticky note to a recipe

day 4:

- worked on gridview adapter, binded data to adapter. Main problem was formatting the url. This wasn't formatted correctly. This problem has been solved. Tomrrow I will create an activity for the recipe details.

day 5: Recipe details working. Main problem was making sure the data is populated in a decent way in the activity. Is acceptable now but requires a finishing touch. Mainly did string formatting, so no unneccesary characters are put in activity. Sunday or monday I will continue with recipes by ingredient search. 

day 6: meeting with renske. I have showed my current progress. She advised me to give the user the option to display their recipes as titles. She will send me a stackoverflow post with which i can use an interface for my async task so i dont need to make two asynctasks. 

- Implemented listview. When the user clicks on an element he or she is navigated to the recipe details. Toorrow I will continue with setting up the firebase database and the favorites list for authenticated users. 

day 7: App shows a favorites list which is stored in firebase. Need to work out the details, like a remove function and navigating the user to the details activity when an item is clicked. This weekend or monday I will continue with recipe by ingredient search.


day 8: First I put the recipe titles in firebase as an arraylist of strings instead of objects. I changed this, because otherwise the onItemClickListener is difficult to implement. Both onItemClickListener and onLongItemClickListener are working. 


day 9: general recipe search and recipe by ingredient search are working. No problems encountered. Renske told me that the general recipe search - looks if a field in the object matches the query - is fine. She also concluded that the scope of the application is fine now. I shall continue tomorrow with debugging of the application and increasing the quality of the code. 


day 10: Reduced oncreates. Made seperate classes for google, facebook and email sign in. Also removed unnecessary code. 


day 11: Made a navigation drawer for every activity , which provides a log out button if the user is logged in. Otherwise display sign up button. 

day 12: Tried to clean up code. Did this since I have a conversation on thursday with Renske on how to optimize this. I will continue tomorrow with debugging of my application.
 

day 13: created a facebook and email sign out button, and a sign up button if it is a local user. Encountering problems with sign out of google user. Callback is not being called. 


day 14: google sign out works. I had the callback in a class and this did not work. After putting the callback in an activity, it is being called. Implemented facebook and google authentication. First I put the user in firebase, but this did not seem like the correct method. Worked on debugging of mainActivity. Controlflow isn't quite solid yet. Will continue this tomorrow. Implemented logic, that the user can ony be active with one account at a time. 


day 15/16: Some problems with if a user wants to use different loginmethods. First the user is continously null. This was because of the asynchronous nature of firebase, which did not fetch the user and their data yet. Implemented a condition to recreate the activity if the user is null.

day 17: worked on logic of local user. Data is being put in sharedpreferences. Also an empty textview has been implemented for when the favorites list is empty. 

day 18: Added extra code for the continuation of the application where the user left off before the app closed. Reduced bugs: After back button press the progressbar was still running, and I could not do a new query. Solved this with a recreate of the activity. 


day 19/24:

worked on code quality. Put these days together because I lost my commits, because my repository went corrupt and needed to reinitialize. Problems I encountered with code quality:

1. Wat to put in which class: First I wanted to wrap all the code that happens more than once, in a utilities class. This gave me a bad grade on better code hub, because the class is not specific enough and is being called from every activity. Based on that I decided to create a favoritesHelper, onClickListener and onQueryTextListener classes. 

2. Tried to put progressbar in class. I knew I needed to call the run function, but did not know a seperate thread needed to be made. When I implemented an instance of thread it worked. 


3. Tried to refactor big functions into smaller functions. Instead of one huge onItemClick I devided it in functions for local and authenticated users. Did the same for a longClick and made removeFromDB and removeFromSharedPReferences functions. 


4. I realized that grabbing recipes object and grabbing the titles and putting them in an arraylist of strings is a useless endeavour. I therefore made the decision to implement a recipe adapter with which you can set the recipe objects on the adapter.

day 25: worked on code quality. Further divison of long functions in smaller functions. 

gewerkt aan code kwaliteit. Hierbij een aantal lange functies in kleinere functies opgedeeld. Bettercodehub grade is an eight now. Did some debugging. Made sure that an item can't get added for a second time within an activity. However because the data is not fetched yet from firebase, it still allows for duplicates. 


day 26: partially debugged the application. Made sure that items can't be put in firebse double. I had a bug where i put the intent in onDataChange method. Took this out of the loop. Also implemented better toast messages that tells which title is added to the favorites list. A current problem is dat I cant get the right data without recreating the activity, because of firebse asynchronous nature. It therefore takes a few seconds before the user is navigated to the next activity. 


day 27: Based on conversation with renske, the problem of yesterday wasn't a problem. Furthermore, I have put logout and sign up buttons in all activities. Made layout-land folders, and made sure that the user can login with multiple sign in methods again. Added persistence of query text and email and password fields of email user. This is only bij queries wordt dit alleen gedaan als async task nog niet codone if an async task isn't completed yet.main problem is that with very fast clicks duplicates are being put in listview. This causes a runtime error, because the element doesn't exist in firebase. 


day 28: removed duplicates bug. Reason for the bug was that I passed the position to a remove function inside a loop. the recipe is then removed the first time, but when done for a second time in that position there is no data, so when doing a long click this results in a runtime error. Solved this by not passing position to the function. Current problem I have is that when the back button is pressed when the app is restarted, the user goes back to mainactivity. Will solve this with implementation of some back button logic. 


day 29: Removed a duplicate functions out of favoritesHelper class. AddOrFetchRecipes put in class. Made utilities class smaller by putting a bunch of ui thread tasks in activity. Recreated layout land folders

day 30: Went through entire control flow to check if there are bugs. This was the case for 1 situation. When The back button is clicked fast, the asynctask did not complete yet. The asynctask called a toast message, but here the context could not be passed because it is called from a non activity class. Solved this by using Toaster library, that can display a toast without passing the context. 


day 31: double checked everything again. Got a 6 on better code hub after i did some back button implementation. Got this back to a 7. Negative part is seperation of concerns, because I call the recipes object in every class. 

