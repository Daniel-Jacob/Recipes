day 1: 
- email login gemaakt. Facebook login werkt half en google login nog niet. Morgen mee verder. 

day 2: 
- Alle logins werken. Ik moet alleen nog logout buttons maken. 
Hier ga ik later mee verder. Ik wil eerst zorgen dat de calls naar de api, 
het opvragen van de json data en dit omzetten in objecten goed werkt. 
Hier ga ik morgen mee verder

day 3: 
- Calls naar API werken. Data is omgezet in json en wordt vervolgens succesvol
in een Recipes object gezet en doorgestuurd naar de gridview activity. 
Ik ga morgen proberen ervoor te zorgen dat de fotos van de gerechten
en de titel ook daadwerkelijk te zien zijn in deze gridview. 
Tevens eerste bijeenkomst met Renske. Design doorgenomen
samen met waar ik nu sta. dingen om over na te denken: 

-	Optie om later te registreren
- Registrationactivity: Als er 
sprake is van registratie dan 
twee passwordvelden en bij login
slechts 1
-	Wellicht een functie om notitie
bij recepten te maken

day 4:

Gewerkt aan gridview adapter, en data gebonden aan de adapter. Probleem waar ik vandaag voornamelijk tegenaan liep was een probleem met de url, die niet juist was geformat. Dit probleem opgelost en nu ziet het er netjes uit. Daarnaast morgen activity maken voor recipe details.

day 5: Recipe details werkt. Zaken waar ik voornamelijk tegenaan liep waren het netjes neerzetten van de details. Dit is nu acceptabel maar vereist finesse. Verder voornamelijk veel string formatting gedaan zodat text netjes in activity staat. Ik zal zondag of maandag verder werken aan de recipe by ingredient search. 

day 6: Bespreking met Renske. Laten zien wat ik tot nu toe heb. Zij adviseerde mij ook een optie te hebben dat de gebruiker een lijst van titels krijgt in plaats van een lijst van images. Daarnaast zal zij mij een stackoverflow post doorsturen waarmee ik een interface kan gebruiken voor mijn Asynctask, zodat ik niet elke keer een nieuwe asynctask hoef te maken (omdat ik de context meegeef). 

- listview geimplementeerd. Bij klik op het element wordt de gebruiker naar de receptendetails doorgestuurd. Morgen ga ik werken aan firebase en lijst met favorieten

day 7: App laat een lijst met favorieten zien die in firebase worden opgeslagen. Details nog uitwerken zoals een remove functie en bij een gewone klik doorsturen naar detailsactivity. Dit weekend of maandag werken aan recipe by ingredient search. 


day 8: Ik had titles in database gezet als string in plaats van objecten. Ik heb dit aangepast, omdat anders de onItemClickListener lastig s te implementeren. Daarnaast werkt nu zowel de onItemClickListener als de onItemLongClickListener. 

day 9: general recipe search en recipe by ingredient search nu werkend. In principe nergens tegenaan gelopen, behalve dat ik eerst enkel recipes op basis van titel wilde opzoeken. Renske gaf aan dat de general recipe search - die kijkt of de query een element in het object matcht - goed is. Verder is er naar voren gekomen dat de app nu qua scope in orde is. Ik ga morgen verder met het debuggen van de applicatie en werken aan de code kwaliteit. 

day 10: Gigantische oncreates gereduceerd. Aparte classes aangemaakt voor google, facebook en email sign in. Daarnaast begin gemaakt om onnodige code weg te halen. 

day 11: Navigationdrawer voor elke activity aangemaakt waarbij user kan uitloggen als hij of zij is ingelogd. Anders sign up optie weergeven. 

day 12: gewerkt aan het opschonen van mijn code. Dit gedaan aangezien ik donderdag gesprek heb met renske over hoe ik dit kan optimaliseren. Ik ga morgen beginnen aan het debuggen van de app.  

day 13: facebook sign out email sign out en sign up button gecreerd als het een lokale gebruiker is binnen de recipeactivity. Nog problemen met google sign out button. Callback wordt niet aangeroepen.

day 14: google sign out werkt nu. Ik had de callback in een class staan en dit wilde niet werken. Nadat ik de callback in mijn activity had gebracht werd hij aangeroepen. Verder heb ik facebook en googleauthenticatie geimplementeerd. Ik stopte de user eerst in firebase maar dit bleek niet de correcte methode te zijn. Verder heb ik gewerkt aan het debuggen van mainactivity. Controlflow loopt nog niet hellemaal lekker. Hier morgen mee verder. Ik heb een deel van de logica geimplementeerd die ervoor zorgt dat een user maar met een account tegelijk actief kan zijn.

day 15/16: Problemen gehad met de logica als de gebruiker de app wilt gebruiken met verschillende inlogmethoden. Eerst was de user steeds null en dat had met de asynctask te maken omdat firebase de informatie nog niet had opgehaald. Vervolgens was hetzelfde het geval voor de data. Ik moest daarom nog een conditie implementeren om de activity steeds te hercreeren totdat de data klaar was met laden. 

day 17: gewerkt aan de lokale gebruiker. Data wordt in sharedpreferences gezet. Verder empty textview geimplementeerd als favorites lijst leeg is. 

day 18: extra code toegevoegd zodat bij een restart van de app de gebruiker verdergaat waar hij of zij begonnen is. Daarnaast bugs weggewerkt: na een druk op de back button zag ik dat de progressbar nog verder draaide en ik geen nieuwe query kon doen. Dit opgelost door een recreate van de activity. 

day 19/24:
 
gewerkt aan de codekwaliteit. Deze dagen bijeen gezet, omdat ik mijn commits kwijt ben geraakt omdat mijn repository corrupt werd en dit moest herinitializeren. Problemen waar ik tegenaan liep bij het verbeteren van de codekwaliteit:

1. Wat stop je in welke class: Ik wilde eerst alle code die ik meer dan één keer gebruikte in een utilities class zetten. Dit leidde echter tot een slechter cijfer op better code hub, omdat de class niet specifiek genoeg was waardor hij bijna in elke activity werd aangeroepen. Op basis daarvan een favoriteshelper en onClickListener en onQueryClickListener classes gemaakt

2. Vervolgens geprobeerd progressbar in class te zetten. Ik wist dat dit moest gebeuren door de run functie te overriden maar wist niet dat er een aparte thread aangemaakt moest worden. Toen ik dit codeerde werkte het. 

3. Grote functies geprobeerd in te korten door er kleinere functies van te maken. In plaats van een grote onitemclick functie heb ik de functie opgedeeld in een deel dat het recept vindt in sharedpreferences of de database, en een functie om dat recept naar de volgende activity te brengen. Hetzelfde gedaan voor de longclick om removeFromDB en removeFromSharedPreferences functies aan te maken. 

4. Voor het setten van de adapter realiseerde ik mij dat het onnodig was om eerst de recepten objecten te krijgen, dan deze naar een arraylist van strings om te zetten om vervolgens de arrayAdapter te initializeren. Daarom heb ik besloten om alsnog een recipe adapter te implementeren, waardoor de objecten gewoon in de adapter kunnen worden gezet.

day 25: gewerkt aan code kwaliteit. Hierbij een aantal lange functies in kleinere functies opgedeeld. Voorbeelden hiervan zijn de onItemClick, de onItemLongClick en ook de downloadFromServer functie. 
Better code hub cijfer is nu een 8. Verder wat debugging gedaan. Gezorgd dat in iedergeval een item niet voor een 2e keer binnen een activity opgeslagen kan worden. Dit werkt op dit moment alleen binnen de activity omdat de data nog niet is opgehaald wanneer ik check of het recept al bestaat. Hier morgen mee verder. 

day 26: deel van de app gedebugd. Gezorgd dat items nu niet duplicaat in database gezet worden. Verder bug weggewerkt waar intent in de loop van ondatachange stond. Deze eruit gehaald. Daarnaast betere toast messages geimplementeerd, waardor verteld wordt welke titel toegevoegd wordt aan favorieten. Waar ik nu tegenaan loop is dat ik niet zonder het hercreeren van de activity de juiste data kan ophalen, omdat firebase dit asynchroon doet. Hierdoor duurt het een aantal seconden voordat er gereageerd wordt op de favorites knop. Hier morgen mee verder en verder debuggen van runtime errors

day 27: probleem van gisteren was geen probleem bleek later. Verder heb ik logout/ sign up buttons in alle activities gezet,layout-land folders aangemaakt, en gezorgd dat gebruiker nu met meerdere loginmethoden weer kan inloggen en zijn data kan ophalen. Verder persistence van email and password en queries toegevoegd. bij queries wordt dit alleen gedaan als async task nog niet completed is. Belangrijk punt waar ik op dit moment tegenaan loop is of het erg is dat bij hele snelle kliks items in listview worden gezet voordat de data is gecheckt door firebase.Dit veroorzaakt met een klik op het item een runtime error omdat de data niet in de database bestaat maar wel in de listview staat. 

day 28: vandaag duplicates bug van gisteren weggewerkt. Reden voor de bug was omdat firebase de data niet op tijd ophaalt waardoor duplictes in firebase komen. Dit weggewerkt door een long click mogelijk te maken als de data is opgehaald. Verder heb ik ervoor gezorgd dat bij het afsluiten en opnieuw opstarten van de app, de app begint waar hij werd afgesloten. Probleem is nu alleen dat als de backbutton wordt gedrukt er nu genavigeerd wordt naar de main activity. Morgen zal ik de logica implementeren om een nintuitieve control flow te hebben bij een restart. Verder werken aan code kwaliteit. Meeste bugs zijn nu wel weggewerkt. 

day 29: 

duplicate functie uit FavoritesHelper class gehaald. AddOrFetchRecipes in class gezet, utilities class kleiner gemaakt door aantal ui thread zaken in activity te zetten. Verder layout land folders gemaakt en deze nagelopen. 

day 30: voornamelijk elke manier van naar een activity gaan langsgelopen om te kijken of er geen bugs in zitten. Dit was voor 1 situatie het geval, namelijk snel backbutton klikken als asynctask niet klaar was. Hierdoor kwam ik de app in een toast terecht, die ik aanriep vanuit een class, waar ik de context meegaf. Dit zorgde voor problemen omdat ik hem aanriep van een non activity class. Ik heb dit opgelost door gebruik te maken van de toaster library, die een toast kan weergeven zonder de context mee te geven. 

day 31: nog eens alles nagelopen. Nadat ik aantal zaken had toegevoegd zoals bijvoorbeeld backbutton implementaties kreeg ik weer een 6 op better code hub. Dit heb ik terug weten te schroeven naar een 7. Ik krijg echter nog steeds een minpunt vanwege seperation of concerns. Dit komt omdat ik mijn utils class in  meerdere activities gebruik. wellicht functies opsplitsen in aparte class, zodat dit weggewerkt kan worden
