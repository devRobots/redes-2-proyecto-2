package co.edu.uniquindio.client.tcp;

import co.edu.uniquindio.util.TransferControlProtocol;

import java.io.*;

public class ClientProtocolTCP extends TransferControlProtocol {

    public ClientProtocolTCP(InputStream input, OutputStream output) {
        super(input, output);
    }

    @Override
    protected void protocol() {

    }
}
