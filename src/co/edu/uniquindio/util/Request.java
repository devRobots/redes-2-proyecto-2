package co.edu.uniquindio.util;

import java.util.ArrayList;

public class Request {
    private final ArrayList<UserInformation> candidates;
    private final String file;

    public Request(ArrayList<UserInformation> candidates, String file) {
        this.candidates = candidates;
        this.file = file;
    }

    public ArrayList<UserInformation> getCandidates() {
        return candidates;
    }

    public String getFile() {
        return file;
    }
}
