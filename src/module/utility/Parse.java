package module.utility;

import module.command.api.IndexStringPair;

public class Parse {
	public static String mergeString(String[] input, int startFrom, int endWith, char token){
		String output = "";
		
		try {
			output += input[startFrom];
			startFrom++;
			
			while (startFrom <= endWith) {
				output = output + token + input[startFrom];
				startFrom++;
			}
			return output;
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
			return "";
		}
	}
	
	public static String mergeString(String[] input, int startFrom, char token){
		String output = "";
		
		try {
			output += input[startFrom];
			startFrom++;
			
			while (startFrom < input.length) {
				output = output + token + input[startFrom];
				startFrom++;
			}
			return output;
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
			return "";
		}
	}
	
	public static IndexStringPair parseName(String input){
		String[] temp = input.split("\\.");
		int index;
		String name;
		if (temp.length == 1) {
			index = 0;
			name = input;
		}
		else {
			try {
				index = Integer.parseInt(temp[0]) - 1;
				name = temp[1];
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return new IndexStringPair(name, index);
	}
}
