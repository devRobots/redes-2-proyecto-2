package co.edu.uniquindio.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public abstract class TransferControlProtocol {
    protected PrintWriter writer;
    protected BufferedReader reader;

    protected BufferedInputStream inputFile;
    protected BufferedOutputStream outputFile;

    private final InputStream input;
    private final OutputStream output;

    public TransferControlProtocol(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void initStringStreams() {
        writer = new PrintWriter(output, true);
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public void closeStringStreams() {
        try {
            writer.close();
            reader.close();
        } catch (Exception ignored) {
        }
    }

    public void initFileStreams(File file, boolean sendMode) {
        try {
            if (sendMode) {
                inputFile = new BufferedInputStream(new FileInputStream(file.getPath()));
                outputFile = new BufferedOutputStream(output);
            } else {
                inputFile = new BufferedInputStream(input);
                outputFile = new BufferedOutputStream(new FileOutputStream(file.getPath()));
            }
        } catch (Exception ignored) {
        }
    }

    public void closeFileStreams() {
        try {
            inputFile.close();
            outputFile.close();
        } catch (Exception ignored) {
        }
    }

    protected void sendString(String message) {
        writer.println(message);
        System.out.println("Mensaje enviado: " + message);
    }

    protected String receiveString() {
        String message = null;
        try {
            message = reader.readLine();
            System.out.println("Mensaje recibido: " + message);
        } catch (Exception ignored) {
        }
        return message;
    }

    protected void sendFile(String filePath) {
        File file = new File(filePath);
        initFileStreams(file, true);
        System.out.println("Enviando el archivo: " + file.getPath());

        long size = file.length();
        sendString("SIZE " + size);

        try {
            byte[] byteArray = new byte[512];
            int in;
            while ((in = inputFile.read(byteArray)) != -1) {
                outputFile.write(byteArray, 0, in);
            }
            outputFile.flush();
            closeFileStreams();
        } catch (Exception ignored) {
        }

        System.out.println("Archivo enviado exitosamente");
    }

    protected void receiveFile(String filePath) {
        File file = new File(filePath);
        initFileStreams(file, false);
        System.out.println("Esperando el archivo: " + file.getPath());

        String sizeString = receiveString();
        long size = Long.parseLong(sizeString.split(" ")[1]);

        try {
            byte[] receivedData = new byte[512];
            int in;
            long remainder = size;

            while ((in = inputFile.read(receivedData)) != -1) {
                outputFile.write(receivedData, 0, in);
                remainder -= in;
                if (remainder == 0) break;
            }
            outputFile.flush();
            closeFileStreams();
        } catch (Exception ignored) {
        }

        System.out.println("Archivo recibido exitosamente");
    }

    public void sendFiles(String filePath) {
        try{
            File dir = new File(filePath);
            File[] files = dir.listFiles();
            assert files != null;

            System.out.println("Creando archivo temporal comprimido...");
            File zip = new File(filePath + File.separator + "temp.zip");
            System.out.println("Archivo temporal comprimido '" + zip.getName() + "' creado exitosamente");

            byte[] buffer = new byte[1024];

            FileOutputStream fos = new FileOutputStream(zip);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("Comprimiendo el archivo: "+ file.getName());
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    FileInputStream in = new FileInputStream(file.getPath());
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    in.close();
                }
            }

            zos.closeEntry();
            zos.close();

            sendFile(zip.getPath());
            zip.deleteOnExit();
            System.out.println("Archivo temporal comprimido eliminado");
        }catch(Exception ignored){
        }
    }

    public void receiveFiles(String filePath) {
        try {
            File zip = new File(filePath + File.separator + "temp.zip");
            receiveFile(zip.getPath());

            ZipInputStream zis = new ZipInputStream(new FileInputStream(zip.getPath()));
            ZipEntry file;
            while (null != (file = zis.getNextEntry())) {
                if (!file.isDirectory()) {
                    System.out.println("Extrayendo el archivo: "+ file.getName());
                    FileOutputStream fos = new FileOutputStream(filePath + File.separator + file.getName());
                    int in;
                    byte[] buffer = new byte[1024];
                    while (0 < (in = zis.read(buffer))) {
                        fos.write(buffer, 0, in);
                    }
                    fos.close();
                    zis.closeEntry();
                }
            }

            boolean finish = zip.delete();
            assert finish;
            System.out.println("Archivo temporal comprimido eliminado");
        } catch(Exception ignored){
        }
    }

    protected abstract void protocol();
}
