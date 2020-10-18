package co.edu.uniquindio.client.tcp;

import co.edu.uniquindio.util.TransferControlProtocol;

import java.io.*;

/**
 * Clase ServerProtocolTCP
 *
 * Representa el protocolo TCP para el servidor
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */

public class ServerProtocolTCP extends TransferControlProtocol {

    private String userPath;

    /**
     * Constructor del ServerProtocolTCP
     *
     * @param userPath           Ruta del archivo del usuario
     * @param input              Flujo de entrada de los datos
     * @param output             Flujo de salida de los datos
     */
    public ServerProtocolTCP(String userPath, InputStream input, OutputStream output) {
        super(input, output);
        this.userPath = userPath;
    }

    /**
     * Sobreescritura del método protocol
     */
    @Override
    protected void protocol() {
        String fileName = receiveString();
        sendFile("files" + File.separator + userPath + File.separator + fileName);
    }
}
