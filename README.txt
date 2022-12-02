pour excecuter le client (SOCKET_SGBD\SGBD_client) : java affichage.Affiche
pour excecuter le server (SOCKET_SGBD\SGBD_socket): java affichage.Affiche

NB: dans clients , en clic tous de suite sur le boutton "connecter" car il est encore regle en IP localhost

REQUETE DISPONIBLE POUR LE MOMENT :

-connection a la database : 
	use nomDataBase
-creation database:  
	create database nomDatabase
-creation table[type valide: String,double,int,time,date] : 
	create table nomTable (colonne1 type1, colonne2 type2,...)
-insertion table:
	insert into nomTable (colonne1,colonne2...) values(valeur1,valeur2...)
-selection ["*-->tous les colonne" si non " colon1,colon2 ... ou colon2,colon1 ...  ]:
	select * from nomTable 
	select * from nomTable where condition1 and/or condition2 ...
 exemple condition : nom=mirado and/or numero=21 ... 


exemple de Database existant :
	db : mirado
	table : -eleve (nom String , numero int, nee Date)
		-etudiant (name String,numero int, naissance Date)
	
