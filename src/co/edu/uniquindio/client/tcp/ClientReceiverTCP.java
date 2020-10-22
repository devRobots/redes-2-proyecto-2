package co.edu.uniquindio.client.tcp;

import java.io.*;
import java.net.Socket;

/**
 * Clase ClientReceiverTCP
 *
 * Recibe los datos del usuario desde un socket vinculado
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */

public class ClientReceiverTCP {
    private Socket clientSideSocket;
    private ClientProtocolTCP clientProtocolTCP;

    /**
     * Constructor del ClienteReceiverTCP
     *
     * @param server             Servidor
     * @param port               Puerto de una red
     * @param filePath           Ruta del archivo
     */

    public ClientReceiverTCP(String server, int port, String filePath) {
        try {
            clientSideSocket = new Socket(server, port);
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
