# PRojet2A

#Projet2A

Ce dossier contient le code que nous utilisons dans cette phase du projet 2A

Tout d'abord, deux fichiers V-REP : 

-bubbleRob 13-02.ttt --> Ce robot est capable d'esquiver des obstacles et de suivre une direction donnée. 
		     Son code est dans un child script, pas d'intéraction avec l'extérieur. 
-bubbleRob 20-02.ttt -> Ce robot est dirigé par un fichier java extérieur. 


Ensuite, 

-Le projet Java Cubot --> le fichier Cubot.java décrit le même comportement que le child script de bubbleRob 13-02. 
		      Mais, il ne marche pas parfaitement, il faut qu'on modifie un peu les " différences de temps".
		     Actuellement pas de conditions par le serveur REST Terminator, mais c'est notre prochaine étape. 
		     
		      


-le projet Java REST Terminator -> il créé un serveur localhost où le fichier Cubot.java va chercher les conditions.
			  
