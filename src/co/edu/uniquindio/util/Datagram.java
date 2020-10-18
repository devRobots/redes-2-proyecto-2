package co.edu.uniquindio.util;

import java.net.InetAddress;

/**
 * Clase Datagram
 *
 * Clase que representa un datagrama
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */
public class Datagram<T> {
    private final T data;
    private final InetAddress ipAddress;
    private final int port;
    private final Type type;
    private String[] params;

    /**
     * Método constructor del datagrama
     * @param data          Datos del datagrama
     * @param ipAddress     Dirección IP del datagrama
     * @param port          Puerto
     */
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

    /**
     * Método que obtiene los datos del datagrama
     * @return Datos del datagrama
     */
    public T getData() {
        return data;
    }

    /**
     * Método que obtiene la dirección IP del datagrama
     * @return Dirección IP del datagrama
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * Método que obtiene el puerto del datagrama
     * @return Puerto del datagrama
     */
    public int getPort() {
        return port;
    }

    /**
     * Método que obtiene el tipo de datagrama
     * @return Tipo de datagrama
     */
    public Type getType() {
        return type;
    }

    /**
     * Método que obtiene los parametros del datagrama
     * @return Parametros del datagrama
     */
    public String[] getParams() {
        return params;
    }

    public enum Type {
        LOGIN, LOGOUT, GET, SETQUERIES, INFO, OTHER
    }
}
