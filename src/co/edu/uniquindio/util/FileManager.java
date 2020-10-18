package co.edu.uniquindio.util;

import java.io.File;
import java.util.ArrayList;

public class FileManager {
    private static final String rootPath = "files";

    public static ArrayList<String> listFiles(String dirPath) {
        File directory = new File(rootPath + File.separator + dirPath);
        ArrayList<String> files = new ArrayList<>();
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    files.add(file.getName());
                }
            }
        }
        return files;
    }

    public static boolean verifyFile(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }
}
