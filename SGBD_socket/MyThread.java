package thread;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

import socket.SocketServer;
import traitement.Traitement;
import exception.*;

public class MyThread extends Thread
{ 
  private SocketServer ss;
  private Traitement traite;
  public MyThread(SocketServer ss,Traitement trt){
   this.ss=ss;
   this.traite=trt;
  }
  public void run(){
    //1s--->1000millis
    while(true){
      try{
        //String[][] res=traite.requeteTraitement(rqt);
        //ss.sendToClient(res); //envoye a client
        ss.waitAndAcceptAndTakeSocketClient();
        String rqt=(String)ss.getObjectFromClient();
        if(rqt.compareToIgnoreCase("exit")==0){   break;  }

        //----
        Vector v=new Vector();

        //-------------------------------------------------------------------------------------------------------
        try{
            String[][] resq=traite.requeteTraitement(rqt);//resq null si c'est pas de type select
            String[] decomp=traite.decompositionSimple(rqt);
            if( decomp[0].compareToIgnoreCase("select")==0 || decomp[0].compareToIgnoreCase("(select")==0 || decomp[0].compareToIgnoreCase("show")==0 || decomp[0].compareToIgnoreCase("describe")==0 ){
                String [] headers=traite.getHeadersSelect(rqt);
                v.add(" ");
                v.add(headers);
                v.add(resq);
            }else if(decomp[0].compareToIgnoreCase("use")==0){
                v.add("Connecte a "+decomp[1]);
            }else if(decomp[0].compareToIgnoreCase("create")==0 && decomp[1].compareToIgnoreCase("database")==0){
                v.add("Database "+decomp[2]+" cree");
            }else if(decomp[0].compareToIgnoreCase("create")==0 && decomp[1].compareToIgnoreCase("table")==0){
                v.add("Table "+decomp[2]+" cree");
            }else if(decomp[0].compareToIgnoreCase("delete")==0 && decomp[1].compareToIgnoreCase("table")==0){
                v.add("delete ok!");
            }else if(decomp[0].compareToIgnoreCase("insert")==0){
                v.add("Insertion ok! ");
            }
          
        }catch(RQTException re){
              v.add(re.getMessage());
              re.printStackTrace();
        }catch(Exception ex){
              ex.printStackTrace();
              v.add("syntaxe non valide");
        }finally{
        }
        //-----------------------------------------------------------------------------------------------
        System.out.println("ato @Server:"+rqt);
        ss.waitAndAcceptAndTakeSocketClient();     
        //ss.sendToClient("ok voaray client!");
        ss.sendToClient(v);  //envoye la reponce de la requete
      }catch(ClassNotFoundException ce){
        ce.printStackTrace();
      }catch(IOException io){
        io.printStackTrace();
      }catch(Exception ex){
        ex.printStackTrace();
      }
      try{
        //this.sleep(1000);
      }catch(Exception e){
        e.printStackTrace();
      }
    }
  }
   
}