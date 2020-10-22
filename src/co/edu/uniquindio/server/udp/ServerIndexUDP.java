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
     * @throws IOException ex
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
            default: System.out.println("Comando no reconocido: " + datagram.getData());
        }
    }

    /**
     * Método para obtener la información del usuario
     * @param params Lista de parámetros del cliente
     * @throws IOException ex
     */
    private void getInfo(String[] params) throws IOException {
        System.out.println("==[INFO]=======================");

        String username = receiveString().getData();
        UserInformation user = findUser(username);
        assert user != null;

        System.out.println("Usuario solicitante: " + username);
        System.out.println("Tipo de Consulta: " + params[0]);

        String message = "Respuesta: ";
        switch (params[0]) {
            case "REQUESTS": message += user.getRequestsNumber() + ""; break;
            case "FILE": message += user.getRequestsNumber(params[1]) + "";break;
            case "REJECTEDS": message += user.getRejectedRequests() + ""; break;
            default: System.out.println("Parametros no reconocidos: " + Arrays.toString(params)); break;
        }

        System.out.println(message);
        Datagram<String> answer = new Datagram<>(message, user.getIp(), user.getPort());
        sendString(answer);
    }

    /**
     * Método que obtiene el datagrama equivalente a un archivo
     * @param datagram datagram
     * @throws IOException ex
     */
    private void getFile(Datagram<String> datagram) throws IOException {
        System.out.println("==[GET]========================");
        String receiver = receiveString().getData();
        UserInformation userData = findUser(receiver);
        assert userData != null;
        users.remove(userData);

        System.out.println("Usuario solicitante: " + receiver);
        System.out.println("Archivo  solicitado: " + datagram.getParams()[0]);

        ArrayList<UserInformation> candidates = new ArrayList<>();
        for (int i = 0; i < users.size() && candidates.size() < maxQueries; i++) {
            UserInformation user = users.get(i);
            if (user.haveFile(datagram.getParams()[0]) && !user.equals(userData) && user.isActive()) {
                System.out.print("Se encontro en el peer: " + user.getUsername());
                System.out.println(" (" + user.getIp().getHostAddress() + ":" + user.getPort() + ")");
                candidates.add(user);
            }
        }

        System.out.println("Peers encontrados: " + candidates.size());
        userData.addRequest(new Request(candidates, datagram.getData()));
        sendObject(new Datagram<>(candidates, datagram.getIpAddress(), datagram.getPort()));
        users.add(userData);
    }

    /**
     * Método que cambia la cantiad de los queries
     */
    private void setQueries() throws IOException{
        System.out.println("==[SETQUERIES]=================");
        String username = receiveString().getData();
        System.out.println("Usuario modificador: " + username);
        System.out.println("Valor anterior: " + maxQueries);
        try { maxQueries = Integer.parseInt(receiveString().getData()); }
        catch (Exception ignored) {}
        System.out.println("Valor nuevo: " + maxQueries);
    }

    /**
     * Método Login del servidor UDP
     * @throws IOException ex
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
            System.out.println("==[LOGIN]======================");
            System.out.println("Nombre del usuario: " + userDatagram.getData());
            sendString(new Datagram<>("OK", ip, port));

            sendString(new Datagram<>(userDatagram.getPort() + "", ip, port));

            Datagram<Object> files = receiveObject();
            System.out.println("Archivos del usuario: " + files.getData().toString());
            System.out.println("Direccion IP: " + ip.getHostAddress());
            System.out.println("Puerto: " + port);

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
     * @param username username
     */
    private void logout(String username) {
        for (UserInformation user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("==[LOGOUT]=====================");
                System.out.println("Se ha ido el usuario: " + username);
                user.setActive(false);
                return;
            }
        }
    }

    /**
     * Método que obtiene la información del usuario
     * @param username username
     * @return user
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
