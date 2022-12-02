package affichage;

import java.io.*;
import java.sql.*;
import fenetre.Fenetre;
public class Affiche
{ 
    public static void main (String[] args)throws Exception
     {
      Fenetre fenetre=new Fenetre();
      fenetre.create1eFenetre();
      //fenetre.create2eFenetre();
      fenetre.setVisible(true);
   }
     
      
}