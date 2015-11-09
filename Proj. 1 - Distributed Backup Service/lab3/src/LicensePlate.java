public class LicensePlate {
	
	public static boolean checkPlateNr(String p) {
		
		String[] trash = p.split("-");
		
		if(trash.length != 3) {
			return false;
		}
		
		for(String s : trash) {
			if(s.length() != 2) return false;
			for(char c : s.toCharArray()) {
				int code = (int) c;
				boolean valid = (code > 47 && code < 58) || (code > 64 && code < 91);
				if(!valid) return false;
			}
		}
		
		return true;
	}

}
