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
	 * @throws IOException ex
	 */
	@Override
	protected void protocol() throws IOException {
		scanner = new Scanner(System.in);

		System.out.println("Conectando con el servidor...");
		if (login()) {
			while (true) {
				System.out.println();
				String message = scanner.nextLine();

				if (message.equals("LOGOUT")) { logout(); break; }
				String[] data = message.split(" ");

				switch (data[0]) {
					case "SETQUERIES": setQueries(); break;
					case "GET": sendString(message); getFile(data[1]); break;
					case "INFO": sendString(message); getInfo(); break;
					default: System.out.println(message + ": No es un comando valido");
				}
			}
		} else {
			System.out.println("INTENTOS MAXIMOS EXCEDIDOS");
		}

		System.out.println("Cerrando cliente...");
		scanner.close();
	}

	/**
	 * Método que obtiene la información del usuario
	 * @throws IOException ex
	 */
	private void getInfo() throws IOException {
		sendString(username);
		String answer = receiveString();
		System.out.println(answer);
	}

	/**
	 * Metodo que obtiene un archivo desde un peer
	 * @param fileName file
	 * @throws IOException ex
	 */
	private void getFile(String fileName) throws IOException {
		System.out.println("==[GET]========================");
		sendString(username);

		System.out.println("Actualizando indice de archivos...");
		ArrayList<UserInformation> candidates = (ArrayList<UserInformation>) receiveObject();
		System.out.println("Cantidatos encontrados: " + candidates.size());
		System.out.println("-------------------------------");

		for (UserInformation candidate : candidates) {
			System.out.print("Conectando con el peer: ");
			System.out.println(candidate.getIp().getHostAddress() + ":" + candidate.getPort());

			String s = File.separator;
			String filePath = username + s + "Descargas" + s + fileName;
			new ClientReceiverTCP(candidate.getIp().getHostAddress(), candidate.getPort(), filePath);
			if (FileManager.verifyFile(filePath)) {
				System.out.println("Archivo obtenido exitosamente");
				return;
			}
		}
		System.out.println("Archivo no encontrado");
	}

	/**
	 * Método que asigna las consultas
	 */
	private void setQueries() {
		System.out.println("==[SETQUERIES]=================");
		System.out.print("Ingrese el maximo de queries: ");
		String input = scanner.nextLine();
		try {
			int n = Integer.parseInt(input);
			if (n > 0) {
				sendString("SETQUERIES");
				sendString(username);
				sendString(n + "");
			} else {
				System.out.println("El valor " + input + " debe ser positivo");
			}
		} catch (IOException ignored) {
			System.out.println("No se reconocio el numero: " + input);
		}
	}

	/**
	 * Método Login del nodo cliente en el protocolo UDP
	 * @return Variable de control
	 * @throws IOException ex
	 */
	private boolean login() throws IOException {
		for (int i = 0; i < 3; i++) {
			System.out.print("Ingrese su nombre de usuario: ");
			username = scanner.nextLine().trim().toLowerCase();
			sendString("LOGIN");

			sendString(username);
			String response = receiveString();

			if (response.equals("OK")) {
				System.out.println();
				System.out.println("==[LOGIN]======================");

				int port = Integer.parseInt(receiveString());
				threadReceiver = new Thread(() -> new ServerSenderTCP(port, username));
				threadReceiver.start();

				System.out.println("Sincronizando archivos...");
				ArrayList<String> archivos = FileManager.listFiles(username.toLowerCase() + File.separator + "Compartida");

				sendObject(archivos);
				System.out.println("Archivos sincronizados");

				return true;
			} else {
				System.out.println("EL USUARIO YA ESTA REGISTRADO");
			}
		}
		return false;
	}

	/**
	 * Método Logout de un nodo cliente en el protocolo UDP
	 * @throws IOException ex
	 */
	private void logout() throws IOException {
		System.out.println("==[LOGOUT]=====================");
		sendString("LOGOUT " + username);
		System.out.println("Deteniendo servidor TCP de envios...");
		threadReceiver.interrupt();
		System.out.println("Adios " + username);
	}

}
