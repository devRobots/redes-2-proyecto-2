package co.edu.uniquindio.server.udp;

import co.edu.uniquindio.util.Request;
import co.edu.uniquindio.util.UserInformation;
import co.edu.uniquindio.util.Datagram;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase ServerIndexUDP
 *
 * Representa el Index UDP del servidor
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */
public class ServerIndexUDP extends ServerProtocolUDP {

    private ArrayList<UserInformation> users;
    private int maxQueries;

    /**
     * Sobreescritura del método Init
     */
    @Override
    protected void init() {
        users = new ArrayList<>();
        maxQueries = 3;
    }

    /**
     * Sobreescritura del método protocol
     * @throws IOException
     */
    @Override
    protected void protocol() throws IOException{
        Datagram<String> datagram = receiveString();
        String[] params = datagram.getParams();

        switch (datagram.getType()) {
            case LOGIN: login(); break;
            case LOGOUT: logout(params[0]); break;
            case SETQUERIES: setQueries(); break;
            case GET: getFile(datagram); break;
            case INFO: getInfo(params); break;
            default: System.out.println(datagram.getData());
        }
    }

    /**
     * Método para obtener la información del usuario
     * @param params Lista de parámetros del cliente
     * @throws IOException
     */
    private void getInfo(String[] params) throws IOException {
        String username = receiveString().getData();
        UserInformation user = findUser(username);

        String message = "Result: ";
        switch (params[0]) {
            case "REQUESTS": message += user.getRequestsNumber() + ""; break;
            case "FILE": message += user.getRequestsNumber(params[1]) + "";break;
            case "REJECTEDS": message += user.getRejectedRequests() + ""; break;
            default: System.out.println("No se reconocio: " + Arrays.toString(params)); break;
        }

        Datagram<String> answer = new Datagram<>(message, user.getIp(), user.getPort());
        sendString(answer);
    }

    /**
     * Método que obtiene el datagrama equivalente a un archivo
     * @param datagram
     * @throws IOException
     */
    private void getFile(Datagram<String> datagram) throws IOException {
        String receiver = receiveString().getData();
        UserInformation userData = findUser(receiver);
        users.remove(userData);

        ArrayList<UserInformation> candidates = new ArrayList<>();
        for (int i = 0; i < users.size() && candidates.size() < maxQueries; i++) {
            UserInformation user = users.get(i);
            if (user.haveFile(datagram.getParams()[0])) {
                candidates.add(user);
            }
        }

        userData.addRequest(new Request(candidates, datagram.getData()));
        sendObject(new Datagram<>(candidates, datagram.getIpAddress(), datagram.getPort()));
        users.add(userData);
    }

    /**
     * Método que cambia la cantiad de los queries
     */
    private void setQueries() {
        try { maxQueries = Integer.parseInt(receiveString().getData()); }
        catch (Exception ignored) {}
    }

    /**
     * Método Login del servidor UDP
     * @throws IOException
     */
    private void login() throws IOException {
        Datagram<String> userDatagram = receiveString();
        InetAddress ip = userDatagram.getIpAddress();
        int port = userDatagram.getPort();

        UserInformation user = findUser(userDatagram.getData());
        boolean userExists = user != null;
        if (!userExists) {
            user = new UserInformation(userDatagram.getData());
        }

        if (!user.isActive()) {
            System.out.println("Registro del usuario: " + userDatagram.getData());
            sendString(new Datagram<>("OK", ip, port));

            sendString(new Datagram<>(
                    userDatagram.getPort() + "",
                    userDatagram.getIpAddress(),
                    userDatagram.getPort()));

            Datagram<Object> files = receiveObject();
            System.out.println("Archivos del usuario: " + files.getData().toString());

            user.setActive(true);
            user.setFiles((ArrayList<String>) files.getData());
            user.setIp(ip);
            user.setPort(port);

            if (!userExists) {
                users.add(user);
            }
        } else {
            sendString(new Datagram<>("ERROR", ip, port));
        }
    }

    /**
     * Método Logout del servidor UDP
     * @param username
     */
    private void logout(String username) {
        for (UserInformation user : users) {
            if (user.getUsername().equals(username)) {
                user.setActive(false);
                return;
            }
        }
    }

    /**
     * Método que obtiene la información del usuario
     * @param username
     * @return
     */
    private UserInformation findUser(String username) {
        for (UserInformation user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
