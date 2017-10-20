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
