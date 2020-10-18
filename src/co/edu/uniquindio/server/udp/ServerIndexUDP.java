package co.edu.uniquindio.server.udp;

import co.edu.uniquindio.util.UserInformation;
import co.edu.uniquindio.util.Datagram;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class ServerIndexUDP extends ServerProtocolUDP {

    private ArrayList<UserInformation> users;
    private int maxQueries;

    @Override
    protected void init() {
        users = new ArrayList<>();
        maxQueries = 3;
    }

    @Override
    protected void protocol() throws IOException{
        Datagram<String> datagram = receiveString();
        String[] params = datagram.getParams();

        switch (datagram.getType()) {
            case LOGIN: login(); break;
            case LOGOUT: logout(params[0]); break;
            case SETQUERIES: setQueries(params[0]); break;
            case GET: getFile(datagram);
            default: System.out.println(datagram.getData());
        }
    }

    private void getFile(Datagram<String> datagram) throws IOException {
        ArrayList<UserInformation> candidates = new ArrayList<>();
        for (int i = 0; i < users.size() && candidates.size() < maxQueries; i++) {
            UserInformation user = users.get(i);
            if (user.haveFile(datagram.getParams()[0])) {
                candidates.add(user);
            }
        }
        sendObject(new Datagram<>(candidates, datagram.getIpAddress(), datagram.getPort()));
    }

    private void setQueries(String value) {
        try { maxQueries = Integer.parseInt(value); }
        catch (Exception ignored) {}
    }

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

    private void logout(String username) {
        for (UserInformation user : users) {
            if (user.getUsername().equals(username)) {
                user.setActive(false);
                return;
            }
        }
    }

    private UserInformation findUser(String username) {
        for (UserInformation user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
