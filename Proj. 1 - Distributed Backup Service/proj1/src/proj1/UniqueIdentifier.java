package proj1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UniqueIdentifier {
	
	public String text;
	public String identifier;
	
	private final String function = "SHA-256";
	
	public UniqueIdentifier() {
		text = "";
		identifier = "";
	}
	
	public UniqueIdentifier(String text) {
		this.text = text;
		generate();
	}
	
	public void setIdentifier(String pIdentifier) {
		identifier = pIdentifier;
	}
	
	public void changeText(String p) {
		this.text = p;
		generate();
	}
	
	public String getText() {
		return text;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void generate() {
		try {
			MessageDigest mDigest = MessageDigest.getInstance(function);
			byte[] result = mDigest.digest(text.getBytes());
			
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			identifier = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("SHA256: Unknown error.");
			System.err.println("Tried to convert: [" + text + "] with " + function + " but failed.");
			e.printStackTrace();
		}
	}

}
