package module.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

public class HelpUtil {
	public static String getHelp(String filename){
		try {
			JSONArray helpMsgArray = (JSONArray) MapUtil.parser.parse(new FileReader(filename));
			StringBuffer buf= new StringBuffer();
			
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
