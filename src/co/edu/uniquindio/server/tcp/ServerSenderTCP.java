package co.edu.uniquindio.server.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSenderTCP {
    public static final int PORT = 3400;

    private ServerSocket listener;
    private Socket serverSideSocket;
    private ServerProtocolTCP serverProtocolTCP;

     public ServerSenderTCP() {
         try {
             listener = new ServerSocket(PORT);

             while (true) {
                 System.out.println("The ECHO TCP SERVER is waiting for a client...");
                 serverSideSocket = listener.accept();

                 try {
                     serverProtocolTCP = new ServerProtocolTCP(serverSideSocket.getInputStream(), serverSideSocket.getOutputStream());
                     serverProtocolTCP.initStringStreams();
                     serverProtocolTCP.protocol();
                     System.out.println();
                     throw new Exception();
                 } catch (Exception ex) {
                     System.out.println(ex.getMessage());
                 } finally {
                     serverProtocolTCP.closeStringStreams();
                 }
             }
         } catch (IOException ex) {
            ex.printStackTrace();
         } finally {
             try {
                 assert serverSideSocket != null;
                 serverSideSocket.close();
                 listener.close();
             } catch (IOException ex) {
                 ex.printStackTrace();
             }
         }
     }
}
