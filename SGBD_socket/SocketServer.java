package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class SocketServer {
    
    //static ServerSocket variable
    //variable ServerSocket statique
    private static ServerSocket server;
    //socket server port on which it will listen
    //port du serveur socket sur lequel il ecoutera
    private static int port = 9876;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public SocketServer()throws  IOException {
        server = new ServerSocket(port);
    }
    public void waitAndAcceptAndTakeSocketClient()throws  IOException{
        socket = server.accept();
    }
    public Object getObjectFromClient()throws  IOException, ClassNotFoundException{
        ois = new ObjectInputStream(socket.getInputStream());
        Object obj=ois.readObject();
        ois.close();
        return obj;
    }
    public void sendToClient(Object obj)throws  IOException{
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(obj);
        oos.close();
    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        SocketServer ss=new SocketServer();
        while(true){
        ss.waitAndAcceptAndTakeSocketClient();
        System.out.println("ato @Server:"+ss.getObjectFromClient());    //recevoir l'object du client
        System.out.println("connection socket1:"+ss.getSocket().isConnected());
        ss.waitAndAcceptAndTakeSocketClient();     
        ss.sendToClient("ok voaray client!"); //envoye a client
        System.out.println("connection socket2:"+ss.getSocket().isConnected());
        }
    }
}
