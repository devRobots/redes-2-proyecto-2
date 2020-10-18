package co.edu.uniquindio.util;

import java.net.InetAddress;

public class Datagram<T> {
    private final T data;
    private final InetAddress ipAddress;
    private final int port;
    private final Type type;
    private String[] params;

    public Datagram(T data, InetAddress ipAddress, int port) {
        this.data = data;
        this.ipAddress = ipAddress;
        this.port = port;

        if (data instanceof String) {
            String[] dataArray = ((String) data).split(" ");
            switch (dataArray[0]) {
                case "LOGIN": type = Type.LOGIN; break;
                case "LOGOUT": type = Type.LOGOUT; break;
                case "GET": type = Type.GET; break;
                case "SETQUERIES": type = Type.SETQUERIES; break;
                case "INFO": type = Type.INFO; break;
                default: type = Type.OTHER; break;
            }
            if (dataArray.length > 1) {
                params = new String[dataArray.length-1];
                System.arraycopy(dataArray, 1, params, 0, dataArray.length - 1);
            }
        } else {
            type = Type.OTHER;
            params = null;
        }
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

    public Type getType() {
        return type;
    }

    public String[] getParams() {
        return params;
    }

    public enum Type {
        LOGIN, LOGOUT, GET, SETQUERIES, INFO, OTHER
    }
}
