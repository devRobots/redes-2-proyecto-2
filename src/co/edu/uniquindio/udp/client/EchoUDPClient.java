package co.edu.uniquindio.udp.client;

import java.io.IOException;
import java.util.Scanner;

public class EchoUDPClient extends ClientProtocolUDP {

	@Override
	protected void protocol() {
		try {
			System.out.print("Ingrese un mensaje: ");
			String message = new Scanner(System.in).nextLine();

			System.out.println("Enviado: " + message);

			sendString(message);

			String response = receiveString();

			System.out.println("Recibido: " + response);
		} catch (IOException ignored) {
		}
	}

	public static void main(String[] args) {
		new EchoUDPClient();
	}
}
