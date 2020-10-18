package co.edu.uniquindio.server.tcp;

import co.edu.uniquindio.util.TransferControlProtocol;

import java.io.*;

public class ServerProtocolTCP extends TransferControlProtocol {

    public ServerProtocolTCP(InputStream input, OutputStream output) {
        super(input, output);
    }

    @Override
    protected void protocol() {

    }
}
