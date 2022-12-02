package listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;

import fenetre.*;
import socket.ClientSocket;

public class ListenMousConnectServ implements MouseListener {
      Fenetre fenetre;
      public ListenMousConnectServ(Fenetre fen) {
            fenetre=fen;
      }

      public void mouseClicked(MouseEvent e){ 
            JTextField txtfPort=fenetre.getChampPort();
            System.out.println("Port : "+txtfPort.getText());    
            JTextField txtfIp=fenetre.getChampIp();
            System.out.println("Host : "+txtfIp.getText());   
            //creer l'objet ClientSoket par le Port et Host qui a ete saisi...
            //...et essayons de se connecter :
            try{
                  //fenetre.setClientSocket(new ClientSocket(txtfIp.getText(), Integer.valueOf(txtfPort.getText())));
                  fenetre.setClientSocket( new ClientSocket(InetAddress.getLocalHost().getHostAddress().toString(), 9876) );
                  ClientSocket cs=fenetre.getClientSocket();
                  //
                  //ClientSocket cs=new ClientSocket(InetAddress.getLocalHost().getHostAddress(),9876);
                  cs.seConnecterToServer();
                  cs.sendToServer("Iny ny anao Server a!");
                  System.out.println("ato @ Client:"+cs.getObjectFromServer());
                  fenetre.create2eFenetre();
                  
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
