package module.utility;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class EnDecoder {
	public static String encodeChangeLine(String input) {
		String[] temp = input.split("\n");
		String output = Parse.mergeString(temp, 0, '\u24A0');
		output += '\n';
		return output;
	}

	public static String decodeChangeLine(String input) {
		try {
			String[] temp = input.split("\u24A0");
			String output = Parse.mergeString(temp, 0, '\n');
			return output;
		} catch (IndexOutOfBoundsException e) {
			return input;
		}
	}
	
	public static void sendUTF8Packet(DataOutputStream out, String message){
		try {
			byte[] b = message.getBytes("UTF-8");
			out.write(b);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
