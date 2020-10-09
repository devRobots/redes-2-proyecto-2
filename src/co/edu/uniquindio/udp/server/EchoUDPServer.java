package co.edu.uniquindio.udp.server;

import co.edu.uniquindio.udp.Datagram;

import java.io.IOException;

public class EchoUDPServer extends ServerProtocolUDP {

	public static void main(String[] args) {
		new EchoUDPServer();
	}

	@Override
	protected void protocol() {
		try {
			Datagram<String> datagram = receiveString();
			System.out.println("Recibido: " + datagram.getData());
			sendString(datagram);
			System.out.println("Enviado: " + datagram.getData());
		} catch (IOException ignored) {
		}
	}
}
