package module.utility;

public class Search {
	// see if part of target string equals to src
	public static boolean searchName(String target, String src){
		String[] temp = target.split(" ");
		for (String part : temp) {
			if (part.equals(src)) return true;
		}
		return false;
	}
}
