package co.edu.uniquindio.util;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Clase UserInformation
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */
public class UserInformation implements Serializable {
    private final String username;
    private ArrayList<String> files;
    private InetAddress ip;
    private int port;
    private boolean active;
    private final ArrayList<Request> requests;

    /**
     * Método Constructor de la clase UserInformation
     * @param username Nombre del usuario
     */
    public UserInformation(String username) {
        requests = new ArrayList<>();
        this.username = username;
        this.active = false;
    }

    /**
     * Sobreescritura del método toString
     * @return Formato de la información del usuario
     */
    @Override
    public String toString() {
        return "UserInformation{" +
                "username=" + username +
                ", ip=" + ip +
                ", port=" + port +
                ", active=" + active +
                '}';
    }

    /**
     * Método que obtiene el número de peticiones
     * @return Número de peticiones
     */
    public int getRequestsNumber() {
        return requests.size();
    }

    /**
     * Método que obtiene el número de peticiones según la extensión de archivo
     * @param fileExtension Extención de archivo
     * @return Número de peticiones
     */
    public int getRequestsNumber(String fileExtension) {
        int n = 0;
        for (Request request : requests) {
            String file = request.getFile();
            if (file != null) {
                if (file.endsWith(fileExtension)) {
                    n++;
                }
            }
        }
        return n;
    }

    /**
     * Método que obtiene el número de peticiones rechazadas
     * @return
     */
    public int getRejectedRequests() {
        int n = 0;
        for (Request request : requests) {
            if (request.getCandidates().isEmpty()) {
                n++;
            }
        }
        return n;
    }

    /**
     * Método que añade una petición
     * @param r Petición a añadir
     */
    public void addRequest(Request r) {
        requests.add(r);
    }

    /**
     * Método que verifica si un usuario tiene determinado archivo
     * @param fileName Archivo a verificar
     * @return Indicador de tenencia del archivo
     */
    public boolean haveFile(String fileName) {
        for (String file : files) {
            if (file.equalsIgnoreCase(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método que obtiene el Nombre del Usuario
     * @return Nombre del Usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Método que obtiene la dirección IP de un usuario
     * @return Dirección IP del usuario
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * Método que asigna una dirección IP al usuario
     * @param ip Dirección IP a asignar
     */
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    /**
     * Método que obtiene el puerto utilizado por el usuario
     * @return Puerto utilizado por el usuario
     */
    public int getPort() {
        return port;
    }

    /**
     * Método que le asigna un puerto al usuario
     * @param port Puerto a asignar
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Método que determina si el usuario está activo o no
     * @return Indicador de actividad
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Método que asigna el estado de actividad al usuario
     * @param active Variable que asigna al usuario como activo
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Métodoq que le asigna una lista de archivos al usuario
     * @param files Lista de archivos a asignar
     */
    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }
}
