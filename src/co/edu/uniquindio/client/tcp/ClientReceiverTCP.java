package co.edu.uniquindio.client.tcp;

import java.io.*;
import java.net.Socket;

public class ClientReceiverTCP {
    private Socket clientSideSocket;
    private ClientProtocolTCP clientProtocolTCP;

    public ClientReceiverTCP(String server, int port) {
        try {
            clientSideSocket = new Socket(server, port);

            clientProtocolTCP = new ClientProtocolTCP(clientSideSocket.getInputStream(), clientSideSocket.getOutputStream());
            clientProtocolTCP.initStringStreams();
            clientProtocolTCP.protocol();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            clientProtocolTCP.closeStringStreams();
            try {
                assert clientSideSocket != null;
                clientSideSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
