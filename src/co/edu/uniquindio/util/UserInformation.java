package co.edu.uniquindio.util;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class UserInformation implements Serializable {
    private final String username;
    private ArrayList<String> files;
    private InetAddress ip;
    private int port;
    private boolean active;
    private final ArrayList<Request> requests;

    public UserInformation(String username) {
        requests = new ArrayList<>();
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

    public int getRequestsNumber() {
        return requests.size();
    }

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

    public int getRejectedRequests() {
        int n = 0;
        for (Request request : requests) {
            if (request.getCandidates().isEmpty()) {
                n++;
            }
        }
        return n;
    }

    public void addRequest(Request r) {
        requests.add(r);
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

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }
}
