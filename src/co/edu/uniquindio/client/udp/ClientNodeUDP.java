package co.edu.uniquindio.client.udp;

import co.edu.uniquindio.client.tcp.ClientReceiverTCP;
import co.edu.uniquindio.client.tcp.ServerSenderTCP;
import co.edu.uniquindio.util.UserInformation;
import co.edu.uniquindio.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase ClientNodeUDP
 *
 * Representa el cliente del modelo cliente-servidor
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */

public class ClientNodeUDP extends ClientProtocolUDP {

	private Scanner scanner;
	private String username;
	private Thread threadReceiver;

	/**
	 * Sobreescritura del método protocol
	 * @throws IOException
	 */
	@Override
	protected void protocol() throws IOException {
		scanner = new Scanner(System.in);

		System.out.println("Conectando con el servidor...");
		if (login()) {
			while (true) {
				String message = scanner.nextLine();
				if (message.equals("LOGOUT")) { logout(); break; }
				sendString(message);

				String[] data = message.split(" ");

				switch (data[0]) {
					case "SETQUERIES": setQueries(); break;
					case "GET": getFile(data[1]); break;
					case "INFO": getInfo(); break;
					default: System.out.println(message + ": No es un comando valido");
				}
			}
		} else {
			System.out.println("INTENTOS MAXIMOS EXCEDIDOS");
		}

		scanner.close();
	}

	/**
	 * Método que obtiene la información del usuario
	 * @throws IOException
	 */
	private void getInfo() throws IOException {
		sendString(username);
		String answer = receiveString();
		System.out.println(answer);
	}

	/**
	 *
	 * @param fileName
	 * @throws IOException
	 */
	private void getFile(String fileName) throws IOException {
		sendString(username);
		ArrayList<UserInformation> candidates = (ArrayList<UserInformation>) receiveObject();
		for (UserInformation candidate : candidates) {
			String s = File.separator;
			String filePath = username + s + "Descargas" + s + fileName;
			new ClientReceiverTCP(candidate.getIp().toString(), candidate.getPort(), filePath);
			if (FileManager.verifyFile(filePath)) {
				break;
			}
		}
	}

	/**
	 * Método que asigna las consultas
	 * @throws IOException
	 */
	private void setQueries() throws IOException {
		System.out.print("Ingrese el maximo de queries: ");
		int n = Integer.parseInt(scanner.nextLine());
		if (n > 0) {
			sendString("QUERIES " + n);
			System.out.println("OK");
		} else {
			System.out.println("ERROR");
		}
	}

	/**
	 * Método Login del nodo cliente en el protocolo UDP
	 * @return Variable de control
	 * @throws IOException
	 */
	private boolean login() throws IOException {
		for (int i = 0; i < 3; i++) {
			System.out.print("Ingrese su nombre de usuario: ");
			username = scanner.nextLine().trim().toLowerCase();
			sendString("LOGIN");

			sendString(username);
			String response = receiveString();

			if (response.equals("OK")) {
				System.out.println("Cliente conectado!");

				int port = Integer.parseInt(receiveString());
				threadReceiver = new Thread(() -> new ServerSenderTCP(port, username));
				threadReceiver.start();

				System.out.println("Sincronizando archivos...");
				ArrayList<String> archivos = FileManager.listFiles(username.toLowerCase() + File.separator + "Compartida");

				sendObject(archivos);
				System.out.println("Archivos sincronizados!");

				return true;
			} else {
				System.out.println("EL USUARIO YA ESTA REGISTRADO");
			}
		}
		return false;
	}

	/**
	 * Método Logout de un nodo cliente en el protocolo UDP
	 * @throws IOException
	 */
	private void logout() throws IOException {
		sendString("LOGOUT " + username);
		threadReceiver.interrupt();
		System.out.println("Adios");
	}

}
