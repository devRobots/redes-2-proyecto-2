package co.edu.uniquindio;

import java.net.InetAddress;
import java.util.ArrayList;

public class UserInformation {
    private String username;
    private ArrayList<String> files;
    private InetAddress ip;
    private int port;
    private boolean active;

    public UserInformation(String username, ArrayList<String> files, InetAddress ip, int port) {
        this.username = username;
        this.files = files;
        this.ip = ip;
        this.port = port;
        this.active = true;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "username=" + username +
                ", ip=" + ip +
                ", port=" + port +
                ", active=" + active +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }
}
