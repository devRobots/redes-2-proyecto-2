package co.edu.uniquindio.udp.client;

import co.edu.uniquindio.udp.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EchoUDPClient extends ClientProtocolUDP {

	private Scanner scanner;
	private String username;

	@Override
	protected void protocol() throws IOException{
		scanner = new Scanner(System.in);

		System.out.println("Conectando con el servidor...");
		if (login()) {
			String message = "";
			while (!message.contains("LOGOUT")) {
				message = scanner.nextLine();
				sendString(message);
			}
		} else {
			System.out.println("INTENTOS MAXIMOS EXCEDIDOS");
		}
		scanner.close();
	}

	private boolean login() throws IOException {
		for (int i = 0; i < 3; i++) {
			System.out.print("Ingrese su nombre de usuario: ");
			String user = scanner.nextLine().trim().toUpperCase();
			sendString("LOGIN");

			sendString(user);
			String response = receiveString();

			if (response.equals("OK")) {
				System.out.println("Cliente conectado!");
				System.out.println("Sincronizando archivos...");

				ArrayList<String> archivos = FileManager.listFiles(user + File.separator + "Compartida");
				sendObject(archivos);

				System.out.println("Archivos sincronizados!");

				username = user;

				return true;
			} else {
				System.out.println("EL USUARIO YA ESTA REGISTRADO");
			}
		}
		return false;
	}

	public static void main(String[] args) {
		new EchoUDPClient();
	}
}
