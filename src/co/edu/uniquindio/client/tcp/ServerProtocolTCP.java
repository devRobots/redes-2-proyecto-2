package co.edu.uniquindio.client.tcp;

import co.edu.uniquindio.util.TransferControlProtocol;

import java.io.*;

public class ServerProtocolTCP extends TransferControlProtocol {

    private String userPath;

    public ServerProtocolTCP(String userPath, InputStream input, OutputStream output) {
        super(input, output);
        this.userPath = userPath;
    }

    @Override
    protected void protocol() {
        String fileName = receiveString();
        sendFile("files" + File.separator + userPath + File.separator + fileName);
    }
}
