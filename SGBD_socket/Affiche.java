package affichage;

import java.io.*;
import java.sql.*;

import socket.SocketServer;
import thread.*;
import traitement.Traitement;
public class Affiche
{ 
   public static void main (String[] args)throws Exception
   {
      MyThread mythread=new MyThread(new SocketServer(), new Traitement());
      mythread.start();

      // Traitement traite=new Traitement();
      // traite.requeteTraitement("use mirado");
      // String[][] valiny=traite.requeteTraitement("select eleve.nom from eleve join etudiant on etudiant.name=eleve.nom");
      // traite.affiche(valiny);
      // String[] header=traite.getHeadersSelect("select * from eleve join etudiant on etudiant.name=eleve.nom");
      // for(int i=0;i<header.length;i++){
      //    System.out.println(header[i]);
      // }
   }  
}