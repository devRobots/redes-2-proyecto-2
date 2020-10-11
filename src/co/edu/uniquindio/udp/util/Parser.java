package co.edu.uniquindio.udp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Parser {
	public static byte[] objectToByteArray(Object o) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(o);
		out.close();

		byte[] buffer = bos.toByteArray();

		return buffer;
	}

	public static Object byteArrayToObject(byte[] byteArray) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
		Object o = in.readObject();
		in.close();

		return o;
	}
}
