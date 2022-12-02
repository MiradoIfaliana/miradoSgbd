package fenetre;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.*;

import javax.swing.*;
import listeners.*;
import socket.*;

public class Fenetre extends JFrame
{ 
   JTextField champ ;
   JTextField champPort ;
   JTextField champIp;
   JPanel panel;
   JLabel labmsg;

   ClientSocket clientSocket;
   
   public JTextField getChamp() {
      return champ;
   }
   public void setChamp(JTextField champ){
      this.champ = champ;
   }
   public JPanel getPanel() {
      return panel;
   }
   public void setPanel(JPanel panel) {
      this.panel = panel;
   }
   public JTextField getChampPort() {
      return champPort;
   }
   public void setChampPort(JTextField champPort) {
      this.champPort = champPort;
   }
   public JTextField getChampIp() {
      return champIp;
   }
   public void setChampIp(JTextField champIp) {
      this.champIp = champIp;
   }
   public JLabel getLabmsg() {
      return labmsg;
   }
   public void setLabmsg(String text) {
      this.labmsg.setText(text);
      this.labmsg.setVisible(true);
   }
   public ClientSocket getClientSocket() {
      return clientSocket;
   }
   public void setClientSocket(ClientSocket clientSocket)throws IOException{
      this.clientSocket = clientSocket;
   }
   public Fenetre()throws Exception
   {  
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //this.setLayout(null);
   }
   public void create1eFenetre()throws Exception{
      this.setTitle("SGBD");
      this.setSize(800,400);//largeur et hauteur en pixel
      this.setLocationRelativeTo(null);//positionement au centre du objet fenetre
      //this.setLocation(10, 10);;
      this.setLayout(null);
      JLabel labelPort=new JLabel("Port Serveur :");
      champPort=new JTextField();
      JLabel labelIp=new JLabel("Host :");
      champIp=new JTextField();

      labelPort.setBounds( this.getWidth()/2-50, 20, 100, 20);
         champPort.setBounds( this.getWidth()/3, labelPort.getY()+labelPort.getHeight()+10, this.getWidth()/3,40 ); 
      labelIp.setBounds(this.getWidth()/2-30, champPort.getY()+champPort.getHeight()+10, 60, 20);
         champIp.setBounds(this.getWidth()/3, labelIp.getY()+labelIp.getHeight()+10,this.getWidth()/3, 40);
      JButton btIpPort=new JButton("se connecter");
      btIpPort.addMouseListener(new ListenMousConnectServ(this));
      btIpPort.setBounds(this.getWidth()/2-60, champIp.getY()+champIp.getHeight()+10, 120, 30);

      JPanel pan=new JPanel();
      pan.setLayout(null);
      pan.add(labelPort);
      pan.add(champPort);
      pan.add(labelIp);
      pan.add(champIp);
      pan.add(btIpPort);
      this.setContentPane(pan);
   }
   public void create2eFenetre()throws Exception{
      this.setTitle("SGBD");
      this.setSize(1000,700);//largeur et hauteur en pixel
      this.setLocationRelativeTo(null);//positionement au centre du objet fenetre
      this.setLocation(10, 10);;
      this.setLayout(null);
      champ=new JTextField();
      champ.setBounds(20, 20, this.getWidth()/2,100 );   
      JButton bOk=new JButton("OK");
      bOk.setBounds(20, champ.getHeight()+20+10, 100, 70);
      bOk.addMouseListener(new Listenermouse(this));
      labmsg=new JLabel("-");
      labmsg.setBounds( bOk.getX(), bOk.getY()+bOk.getHeight(), this.getWidth()-bOk.getX(),40 );
      panel=new JPanel();
      panel.setBackground(Color.gray);
      panel.setBounds(champ.getX()+champ.getWidth()+10, 20, Math.abs( this.getWidth()-champ.getX()-champ.getWidth()-30 ), this.getHeight()-40);
      JPanel pan=new JPanel();
      pan.setLayout(null);
      pan.add(champ);
      pan.add(bOk);
      pan.add(labmsg);
      pan.add(panel);
      this.setContentPane(pan);
   }
   public void createThepanJTable(String[][] rowData,String[] colName){
      JPanel jpan=panel;
      if(rowData!=null){
         if(rowData.length>=1){
         JTable tableau=new JTable(rowData,colName );
         jpan.setLayout(new BorderLayout());
         jpan.add(tableau.getTableHeader(),BorderLayout.NORTH);
         jpan.add(tableau,BorderLayout.CENTER);
         }
      }else {
         String[][] vide=new String[1][colName.length];
         for(int i=0;i<colName.length;i++){
            vide[0][i]="vide";
         }
         JTable tableau=new JTable(vide,colName );
         jpan.setLayout(new BorderLayout());
         jpan.add(tableau.getTableHeader(),BorderLayout.NORTH);
         jpan.add(tableau,BorderLayout.CENTER);
      }
   }

   public void ajour(){
   }
}