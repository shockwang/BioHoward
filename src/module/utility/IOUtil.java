package module.utility;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtil {
	private static char[] array = new char[1024];
	
	public static String readLineFromClientSocket(BufferedReader sock){
		try {
			return sock.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("socket unexpected closed.");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void clearBufferedReader(BufferedReader sock){
		try {
			sock.read(array);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
