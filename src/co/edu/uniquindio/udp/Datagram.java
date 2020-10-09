package co.edu.uniquindio.udp;

import java.net.InetAddress;

public class Datagram<T> {
    private final T data;
    private final InetAddress ipAddress;
    private final int port;

    public Datagram(T data, InetAddress ipAddress, int port) {
        this.data = data;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public T getData() {
        return data;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
