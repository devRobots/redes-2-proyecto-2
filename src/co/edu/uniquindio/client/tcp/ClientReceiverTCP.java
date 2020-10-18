package co.edu.uniquindio.client.tcp;

import java.io.*;
import java.net.Socket;

public class ClientReceiverTCP {
    private Socket clientSideSocket;
    private ClientProtocolTCP clientProtocolTCP;

    public ClientReceiverTCP(String server, int port, String filePath) {
        try {
            clientSideSocket = new Socket(server.substring(1), port);
            clientProtocolTCP = new ClientProtocolTCP(
                    clientSideSocket.getInputStream(),
                    clientSideSocket.getOutputStream(),
                    filePath);
            clientProtocolTCP.initStringStreams();
            clientProtocolTCP.protocol();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            assert clientProtocolTCP != null;
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
