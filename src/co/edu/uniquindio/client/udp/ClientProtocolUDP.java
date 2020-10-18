package co.edu.uniquindio.client.udp;

import co.edu.uniquindio.util.Parser;

import java.io.IOException;
import java.net.*;

public abstract class ClientProtocolUDP {
    public static int PORT = 3500;
    public static String SERVER = "localhost";

    private DatagramSocket clientSocket;
    private InetAddress serverIPAddress;

    public ClientProtocolUDP() {
        try {
            clientSocket = new DatagramSocket();
            serverIPAddress = InetAddress.getByName(SERVER);

            protocol();

            clientSocket.close();
            System.exit(0);
        } catch (IOException ignored) {
        }
    }

    protected void sendString(String messageToSend) throws IOException {
        byte[] ba = messageToSend.getBytes();
        clientSocket.send(new DatagramPacket(ba, ba.length, serverIPAddress, PORT));
    }

    protected String receiveString() throws IOException {
        byte[] bufferToReceive = new byte[1024];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);
        clientSocket.receive(packetToReceive);
        return new String(packetToReceive.getData(), 0, packetToReceive.getLength());
    }

    protected void sendObject(Object o) throws IOException {
        byte[] ba = Parser.objectToByteArray(o);
        clientSocket.send(new DatagramPacket(ba, ba.length, serverIPAddress, PORT));
    }

    protected Object receiveObject() throws IOException {
        byte[] bufferToReceive = new byte[1024];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);
        clientSocket.receive(packetToReceive);
        return Parser.byteArrayToObject(bufferToReceive);
    }

    protected abstract void protocol() throws IOException;
}
