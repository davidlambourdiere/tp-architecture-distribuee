# TP rabbitmq
3ème année
### Contenu du Travail Pratique
À rendre en binôme (ou en monôme pour ceux qui le souhaitent évidemment, le 11 décembre.

* Implémenter un front-end en JAVA. Ce dernier expose une API REST que vous pouvez appeler depuis CURL ou POSTMAN.
Inutile de faire une UI, ce n'est pas le but recherché. 
Ce composant expose une service sur lequel des clients peuvent faire des POST de job requests. Le métier m’importe peu. Il s'agit d'un exercice (conversion d'image, traduction, jouer un son, mettre un texte en sens inverse, etc.). Le format de la Job Request est JSON. Elle doit forcément correspondre à un fichier stocké sur un filesystem commun au front-end et au back-end (worker). (Pas de solution de cloud pour le moment).
* Implémenter un back-end (un workker) dont la responsabilité est la fonction métier. Dans mon exemple, le front-end véhicule un ID qui sert de référence dans la job request. Le worker peut retrouver le fichier car il le recherche dans la base MONGODB  grâce à l'ID.
* La collection dans MONGODB contient des caractéristiques (attributs) qui ne sont pas transmises via RABBITMQ (le MOM), dans le JSON.
* La Job Request en JSON peut ne contient qu'un ID. C'est un ordre d'exécution poussé par le front-end vers le worker.
* Le front est en aval (downstream), le worker est en amont (upstream)
* Ecrire les caractéristiques dans MONGODB avant d'écrire le message dans RABBITMQ. (Ce dernier est très rapide). Vous risqueriez de ne pas trouver les valeur dans MONGO à la lecture du message JSON consommé depuis RABBITMQ.
* Vous n'implémentez pas la Websocket.
* Tous les composants sont en local (on-premise).
* Livrez le code sous GIT (en ligne, pas sur l'infra)

Cerises sur le gâteau dans cet ordre (bonus évidemment) 


	
1. Vous pouvez si vous y arrivez et si vous avez du temps remplacer le filesystem soit par GCP Cloud Storage ou soit par un bucket AWS S3.
2. Vous conteneurisez RABBITMQ, MONGODB, et vos composants en dernier.
3. Vous implémentez la Websocket.

Posez toutes les questions nécessaires.
