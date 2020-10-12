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

    @Override
    protected void init() {
        users = new ArrayList<>();
    }

    @Override
    protected void protocol() {
        try {
            Datagram<String> datagram = receiveString();
            String message = datagram.getData();

            String[] params = message.split(" ");
            String type = params[0];

            switch (type) {
                case "LOGIN": login(); break;
                case "LOGOUT": logout(params[1]); break;
                default: System.out.println(message);
            }
        } catch (IOException ignored) {
        }
    }

    private void login() throws IOException {
        Datagram<String> username = receiveString();
        InetAddress ip = username.getIpAddress();
        int port = username.getPort();

        if (verifyUser(username.getData())) {
            System.out.println("Registro del usuario: " + username.getData());
            sendString(new Datagram<>("OK", ip, port));

            Datagram<Object> files = receiveObject();
            users.add(new UserInformation(username.getData(),
                    (ArrayList<String>) files.getData(), ip, port));
            System.out.println("Archivos del usuario: " + files.getData().toString());
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

    private boolean verifyUser(String username) {
        for (UserInformation user : users) {
            if (user.getUsername().equals(username)) {
                return !user.isActive();
            }
        }
        return true;
    }
}
