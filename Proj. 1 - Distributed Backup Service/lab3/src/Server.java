import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

class Server {
	
	public static void main(String args[]) throws IOException {
		
		if(args.length != 1) {
			System.out.println("Usage: java Server <srvc_port>");
			return;
		}
		
		int port = Integer.parseInt(args[0]);
		
		// Server
		ServerSocket srvSocket = new ServerSocket(port);
		Socket echoSocket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		
		// Database
		HashMap<String, String> licensePlates = new HashMap<String, String>();
		
		Message msg = new Message();
		
		while(true) {
			
			echoSocket = srvSocket.accept();
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			
			String str = in.readLine();
			String[] s = str.split(" ");
			if(s.length > 0) msg.setOperation(s[0]);
			if(s.length > 1) msg.setLicense(s[1]);
			if(s.length > 2) msg.setName(s[2]);

			if(msg.getOperation().equals("CLOSE")) {
				out.println("Ok :(");
				break;
			}
			
			
			String response = processRequest(licensePlates, msg);
			msg.setResponse(response);
			
			System.out.println(msg.getDebug());
			out.println(msg.getResponse());

		}
		
		System.out.println("Closing connection...");
		
		srvSocket.close();
		echoSocket.close();
		
	}

	private static String processRequest(HashMap<String, String> licensePlates, Message received) {
					
		String response;
		if(received.getOperation().equals("REGISTER")) {
			response = "" + register(licensePlates, received.getLicense(), received.getName());	
		} else if(received.getOperation().equals("LOOKUP")) {		
			System.out.println("look");
			response = "" + lookup(licensePlates, received.getLicense());		
		} else if(received.getOperation().equals("CLOSE")) {	
			response = "close";		
		} else {	
			response = "what?";	
		}
		
		return response;
		
	} 
	
	private static int register(HashMap<String, String> licensePlates, String license, String name) {
		
		// If the license plate already exists
		if(lookup(licensePlates, license) != "NOT_FOUND") return -1;
		
		licensePlates.put(license, name);
		return licensePlates.size();
	}
	
	private static String lookup(HashMap<String, String> licensePlates, String license) {
		
		String name = licensePlates.get(license);
		return (name != null) ? name : "NOT_FOUND";
			
	}
	
}
