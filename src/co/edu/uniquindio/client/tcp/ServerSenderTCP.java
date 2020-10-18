package co.edu.uniquindio.client.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase ServerSenderTCP
 *
 * Representa el remitente para el servidor destino
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */

public class ServerSenderTCP {
    private ServerSocket listener;
    private Socket serverSideSocket;
    private ServerProtocolTCP serverProtocolTCP;

    /**
     * Constructor del ServerSenderTCP
     *
     * @param port          Puerto de la red
     * @param username      nombre del usuario
     */

     public ServerSenderTCP(int port, String username) {
         try {
             listener = new ServerSocket(port);
             System.out.println("SERVIDOR TCP INICIADO");

             while (true) {
                 serverSideSocket = listener.accept();
                 System.out.println("Conectando con cliente...");

                 try {
                     serverProtocolTCP = new ServerProtocolTCP(
                             username + File.separator + "Compartida",
                             serverSideSocket.getInputStream(),
                             serverSideSocket.getOutputStream());
                     serverProtocolTCP.initStringStreams();
                     serverProtocolTCP.protocol();
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
