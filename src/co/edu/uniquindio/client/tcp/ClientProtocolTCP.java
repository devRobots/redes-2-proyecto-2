package co.edu.uniquindio.client.tcp;

import co.edu.uniquindio.util.TransferControlProtocol;

import java.io.*;

/**
 * Clase ClientProtocolTCP
 *
 * Representa el protocolo TCP para el cliente
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */

public class ClientProtocolTCP extends TransferControlProtocol {

    private String userPath;

    /**
     * Constructor del ClienteProtocolTCP
     *
     * @param input       Flujo de entrada de los datos
     * @param output      Flujo de salida de los datos
     * @param userPath    ruta del archivo del usuario
     */

    public ClientProtocolTCP(InputStream input, OutputStream output, String userPath) {
        super(input, output);
        this.userPath = userPath;
    }

    /**
     * Sobreescritura del método protocol
     */
    @Override
    protected void protocol() {
        String aux = File.separator;
        String fileName = userPath.substring(userPath.lastIndexOf(aux)+1);
        sendString(fileName);
        receiveFile("files" + File.separator + userPath);
    }
}
