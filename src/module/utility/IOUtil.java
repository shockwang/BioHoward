package module.utility;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtil {
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
}
