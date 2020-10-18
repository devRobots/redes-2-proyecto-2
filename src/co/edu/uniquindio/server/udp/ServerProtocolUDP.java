package co.edu.uniquindio.server.udp;

import co.edu.uniquindio.util.Datagram;
import co.edu.uniquindio.util.Parser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Clase ServerProtocolUDP
 *
 * Representa el protocolo UDP del servidor
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */
public abstract class ServerProtocolUDP {
    public static final int PORT = 3500;

    private DatagramSocket listener;

    /**
     * Método del protocolo del servidor UDP
     */
    public ServerProtocolUDP() {
        System.out.println("Run UDP Server...");
        init();
        try {
            listener = new DatagramSocket(PORT);
            while (true) {
                protocol();
            }
        }
        catch (IOException ignored) { }
    }

    /**
     * Método que recibe un arreglo de String que representa un datagrama
     * @return Arreglo de String que representa un datagrama
     * @throws IOException
     */
    protected Datagram<String> receiveString() throws IOException {
        byte[] bufferToReceive = new byte[1024];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);

        listener.receive(packetToReceive);

        String data = new String(packetToReceive.getData(), 0, packetToReceive.getLength());
        InetAddress ipAddress = packetToReceive.getAddress();
        int port = packetToReceive.getPort();

        return new Datagram<>(data, ipAddress, port);
    }

    /**
     * Método que envía un arreglo de String que representa un datagrama
     * @param datagram Arreglo de String que representa un datagrama
     * @throws IOException
     */
    protected void sendString(Datagram<String> datagram) throws IOException {
        byte[] ba = datagram.getData().getBytes();

        DatagramPacket packetToSend = new DatagramPacket(ba, ba.length, datagram.getIpAddress(), datagram.getPort());

        listener.send(packetToSend);
    }

    /**
     * Método que recibe un objeto
     * @return Datagrama recibido
     * @throws IOException
     */
    protected Datagram<Object> receiveObject() throws IOException {
        byte[] bufferToReceive = new byte[1024];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);
        listener.receive(packetToReceive);

        InetAddress clientIPAddress = packetToReceive.getAddress();
        int clientPort = packetToReceive.getPort();
        Object o = Parser.byteArrayToObject(bufferToReceive);

        return new Datagram<>(o, clientIPAddress, clientPort);
    }

    /**
     * Método que envía un objeto
     * @param datagram Datagrama a enviar
     * @throws IOException
     */
    protected void sendObject(Datagram<Object> datagram) throws IOException {
        byte[] ba = Parser.objectToByteArray(datagram.getData());
        DatagramPacket packetToSend = new DatagramPacket(ba, ba.length, datagram.getIpAddress(), datagram.getPort());
        listener.send(packetToSend);
    }

    protected abstract void protocol() throws IOException;
    protected abstract void init();
}
