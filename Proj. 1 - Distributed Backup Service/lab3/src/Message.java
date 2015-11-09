public class Message {
	
	private String operation;
	private String license;
	private String name;
	private String response;
	
	public Message() {
		operation = "";
		license = "";
		name = "";
		response = "";
	}
	
	public String get() {
		String res = "";
		if(!getOperation().equals("")) res += getOperation();
		if(!getLicense().equals("")) res += (" " + getLicense());
		if(!getName().equals("")) res += (" " + getName());
		return res;
	}
	
	public String getDebug() {
		return "<" + getOperation() + "><" + getOperands() + ">" + "::<" + getResponse() + ">";
	}
	
	public String getOperation() {
		return operation;
	}
	
	public String getOperands() {
		
		String res = "";
		
		if(license.length() > 0) {
			res += license;
			if(name.length() > 0) res += " " + name;
		}

		return res;
	}
	
	
	public String getResponse() {
		return response;
	}
	
	public String getLicense() {
		return license;
	}
	
	public String getName() {
		return name;
	}
	
	public void setOperation(String p) {
		operation = p;
	}
	
	public void setLicense(String p) {
		license = p;
	}
	
	public void setName(String p) {
		name = p;
	}
	
	public void setResponse(String p) {
		response = p;
	}

}
