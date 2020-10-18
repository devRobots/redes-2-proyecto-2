package co.edu.uniquindio.client.tcp;

import co.edu.uniquindio.util.TransferControlProtocol;

import java.io.*;

public class ClientProtocolTCP extends TransferControlProtocol {

    private String userPath;

    public ClientProtocolTCP(InputStream input, OutputStream output, String userPath) {
        super(input, output);
        this.userPath = userPath;
    }

    @Override
    protected void protocol() {
        String[] splitPath = userPath.split(File.separator);
        String fileName = splitPath[splitPath.length-1];
        sendString(fileName);
        receiveFile("files" + File.separator + userPath);
    }
}
