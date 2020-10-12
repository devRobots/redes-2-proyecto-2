package co.edu.uniquindio.udp.server;

import co.edu.uniquindio.UserInformation;
import co.edu.uniquindio.udp.Datagram;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class EchoUDPServer extends ServerProtocolUDP {

    public static void main(String[] args) {
        new EchoUDPServer();
    }

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
        String message = datagram.getData();

        String[] params = message.split(" ");
        String type = params[0];

        switch (type) {
            case "LOGIN": login(); break;
            case "LOGOUT": logout(params[1]); break;
            default: System.out.println(message);
        }
    }

    private void login() throws IOException {
        Datagram<String> username = receiveString();
        InetAddress ip = username.getIpAddress();
        int port = username.getPort();

        UserInformation user = findUser(username.getData());
        boolean userExists = user != null;
        if (!userExists) {
            user = new UserInformation(username.getData());
        }

        if (!user.isActive()) {
            System.out.println("Registro del usuario: " + username.getData());
            sendString(new Datagram<>("OK", ip, port));

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
