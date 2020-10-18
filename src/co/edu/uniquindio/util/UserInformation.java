package co.edu.uniquindio.util;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class UserInformation implements Serializable {
    private String username;
    private ArrayList<String> files;
    private InetAddress ip;
    private int port;
    private boolean active;

    public UserInformation(String username) {
        this.username = username;
        this.active = false;
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

    public boolean haveFile(String fileName) {
        for (String file : files) {
            if (file.equalsIgnoreCase(fileName)) {
                return true;
            }
        }
        return false;
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
