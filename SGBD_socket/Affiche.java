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
   }  
}