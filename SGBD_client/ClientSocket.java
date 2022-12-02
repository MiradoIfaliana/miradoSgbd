package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;

public class ClientSocket {
    String host ;  
    Socket socket;
    int portServer;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    public ClientSocket(String host,int port)throws  IOException{
        this.host = host;  //InetAddress.getLocalHost():satria samy ato @ ity ordi ty ihany izy ireo no mifandray
        portServer=port;
        System.out.println(host);
    }
    public void seConnecterToServer()throws  IOException{
        //socket = new Socket(host.getHostName(),this.portServer);
        socket = new Socket(host,this.portServer);
    }
    public void sendToServer(Object obj)throws  IOException{
        if(socket.isClosed()==true){    this.seConnecterToServer();     }
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(obj);
        oos.close();
    }
    public Object getObjectFromServer()throws  IOException, ClassNotFoundException{
        if(socket.isClosed()==true){    this.seConnecterToServer();     }
        ois = new ObjectInputStream(socket.getInputStream());
        Object obj=ois.readObject(); 
        ois.close();
        return obj;
    }

    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /*public static void main(String[] args) throws ClassNotFoundException, IOException{
        ClientSocket cs=new ClientSocket(InetAddress.getLocalHost().getHostAddress(),9876);
        cs.seConnecterToServer();
        cs.sendToServer("Iny ny anao Server a!");
        System.out.println("ato @ Client:"+cs.getObjectFromServer());
    }*/

}
