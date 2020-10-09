package co.edu.uniquindio.udp.client;

import java.io.IOException;
import java.net.*;

public abstract class ClientProtocolUDP {
    public static final int PORT = 3500;
    public static final String SERVER = "localhost";

    private DatagramSocket clientSocket;
    private InetAddress serverIPAddress;

    public ClientProtocolUDP() {
        try {
            clientSocket = new DatagramSocket();
            serverIPAddress = InetAddress.getByName(SERVER);

            protocol();

            clientSocket.close();
        } catch (IOException ignored) {
        }
    }

    protected void sendString(String messageToSend) throws IOException {
        byte[] ba = messageToSend.getBytes();

        DatagramPacket packetToSend = new DatagramPacket(ba, ba.length, serverIPAddress, PORT);

        clientSocket.send(packetToSend);
    }

    protected String receiveString() throws IOException {
        byte[] bufferToReceive = new byte[1024];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);

        clientSocket.receive(packetToReceive);

        return new String(packetToReceive.getData(), 0, packetToReceive.getLength());
    }

    protected abstract void protocol();
}
