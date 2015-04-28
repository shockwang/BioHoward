package module.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

public class HelpUtil {
	public static String getHelp(String filename){
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(filename), "UTF-8");
			JSONArray helpMsgArray = (JSONArray) MapUtil.parser.parse(isr);
			StringBuffer buf = new StringBuffer();
			
			for (Object ooo : helpMsgArray)
				buf.append((String) ooo + "\n");
			return buf.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
