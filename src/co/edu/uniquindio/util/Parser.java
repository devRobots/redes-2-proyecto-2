package co.edu.uniquindio.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Clase Parser
 *
 * @author Yesid Shair Rosas Toro
 * @author Juan David Usma Alzate
 * @author Samara Smith Rincon Montaña
 */
public class Parser {

	/**
	 * Método que hace una conversión de un objeto a un arreglo de bytes
	 * @param o Objeto a convertir
	 * @return Arreglo de Bytes convertidos
	 * @throws IOException
	 */
	public static byte[] objectToByteArray(Object o) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(o);
		out.close();

		byte[] buffer = bos.toByteArray();

		return buffer;
	}

	/**
	 * Método que hace una conversión de un arreglo de bytes a un objeto
	 * @param byteArray Arreglo de bytes a convertir
	 * @return Objeto después de convertir
	 * @throws IOException
	 */
	public static Object byteArrayToObject(byte[] byteArray) throws IOException {
		try {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
			Object o = in.readObject();
			in.close();

			return o;
		} catch (ClassNotFoundException ignored) {
			return null;
		}
	}
}
