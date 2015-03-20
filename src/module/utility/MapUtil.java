package module.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MapUtil {
	public static void parseMapFromJSON(String filename){
		JSONParser parser = new JSONParser();
		
		try {
			JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(filename));
			
			String name = (String) jsonObj.get("name");
			String description = (String) jsonObj.get("description");
			
			System.out.println("name: " + name + ", description: " + description);
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
		
	}
}
