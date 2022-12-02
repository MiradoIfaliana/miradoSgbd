package listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

import fenetre.*;
import socket.*;

public class Listenermouse implements MouseListener {
      Fenetre fenetre;
      //------------------------------------------------------------------------pour le premier affichage
      //------------------------------------------------------------------------pour le premier affichage
      public Listenermouse(Fenetre fen) {
            fenetre=fen;
      }

      public void mouseClicked(MouseEvent e){  
            JTextField txtf=fenetre.getChamp();
            String rqt=txtf.getText();
            System.out.println(rqt);
            try{
                  ClientSocket cs=fenetre.getClientSocket();
                  cs.seConnecterToServer();
                  cs.sendToServer(rqt);

                  Vector fromServer=(Vector)cs.getObjectFromServer();
                  //-------------------------------------------------------------
                  fenetre.setLabmsg((String)fromServer.elementAt(0));
                  if(fromServer.size()>=3){
                        fenetre.createThepanJTable((String [][])fromServer.elementAt(2), (String[])fromServer.elementAt(1));
                        fenetre.setVisible(true);
                  }
                  //-------------------------------------------------------------
                  System.out.println("ato @ Client:"+fromServer);
            }catch(ClassNotFoundException ce){
                  ce.printStackTrace();
            }catch(IOException io){
                  io.printStackTrace();
            }catch(Exception ex){
                  ex.printStackTrace();
            }

      }
      public void mouseReleased(MouseEvent e){}
      public void mouseEntered(MouseEvent e){}
      public void mousePressed(MouseEvent e){}
      public void mouseExited(MouseEvent e){}
}
