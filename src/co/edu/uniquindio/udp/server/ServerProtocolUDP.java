package co.edu.uniquindio.udp.server;

import co.edu.uniquindio.udp.Datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class ServerProtocolUDP {
    public static final int PORT = 3500;

    private DatagramSocket listener;

    public ServerProtocolUDP() {
        System.out.println("Run UDP Server...");

        try {
            listener = new DatagramSocket(PORT);

            while (true) {
                protocol();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Datagram<String> receiveString() throws IOException {
        byte[] bufferToReceive = new byte[1024];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);

        listener.receive(packetToReceive);

        String data = new String(packetToReceive.getData(), 0, packetToReceive.getLength());
        InetAddress ipAddress = packetToReceive.getAddress();
        int port = packetToReceive.getPort();

        return new Datagram<>(data, ipAddress, port);
    }

    protected void sendString(Datagram<String> datagram) throws IOException {
        byte[] ba = datagram.getData().getBytes();

        DatagramPacket packetToSend = new DatagramPacket(ba, ba.length, datagram.getIpAddress(), datagram.getPort());

        listener.send(packetToSend);
    }

    protected abstract void protocol();
}
