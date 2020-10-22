package co.edu.uniquindio.util;

import java.io.File;
import java.util.ArrayList;

/**
 * Clase FileManager
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */
public class FileManager {
    private static final String rootPath = "files";

    /**
     * Método que lista los archivos en una dirección especificada
     * @param dirPath Dirección especificada
     * @return Lista de archivos a obtener
     */
    public static ArrayList<String> listFiles(String dirPath) {
        File directory = new File(rootPath + File.separator + dirPath);
        ArrayList<String> files = new ArrayList<>();

        if (!directory.exists()) {
            createUserDirectories(rootPath + File.separator + dirPath);
        }
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    files.add(file.getName());
                }
            }
        }
        return files;
    }

    /**
     * Método que realiza una verificaciónd de los archivos
     * @param filePath Dirección del archivo
     * @return Variable de control del método
     */
    public static boolean verifyFile(String filePath) {
        File file = new File(filePath);
        return file.isFile() && file.exists();
    }

    /**
     * Metodo que crea los directorios por defecto del usuario
     * @param uploadsDirPath ruta del directorio por defecto
     */
    public static void createUserDirectories(String uploadsDirPath) {
        System.out.println("Creando el directorio del usuario...");

        File uploadsDir = new File(uploadsDirPath);
        boolean upExists = uploadsDir.mkdirs();

        String downloadsDirPath = uploadsDirPath.substring(0, uploadsDirPath.lastIndexOf(File.separator) + 1);
        File downloadsDir = new File(downloadsDirPath + "Descargas");
        boolean downExits = downloadsDir.mkdirs();

        assert upExists && downExits;
        System.out.println("Directorio del usuario creado exitosamente");
        System.out.println("Considere agregar archivos y reiniciar");
    }
}
