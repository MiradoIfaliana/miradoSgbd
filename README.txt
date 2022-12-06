pour excecuter le client (SGBD_client) : java affichage.Affiche
pour excecuter le server (SGBD_socket): java affichage.Affiche

NB: dans clients , on clic sur le boutton "connecter" sans saisir IP et Port car pour le moment il est regle en IP localhost

REQUETE DISPONIBLE POUR LE MOMENT :
-voir les databases :
	show databases
-voir les tables d'une database(bien sur apres connection a une base):
	show tables
-descrire une table(bien sur apres connection a une base) : 
	describe table nomTable
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
-difference:
	(requete1 de selection) -- (requete2 de selection) -- ...
	exemple : (select nom,numero from eleve where numero>9) -- (select name, from eleve where nom!=kevin) --...
	


exemple de Database existant :
	db : mirado
	table : -eleve (nom String , numero int, nee Date)
		-etudiant (name String,numero int, naissance Date)
	
