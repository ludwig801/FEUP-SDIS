import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

class Server {
	
	public static void main(String args[]) throws IOException {
		
		if(args.length != 3) {
			System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port>");
			return;
		}
		
		int port = Integer.parseInt(args[0]);
		String mcastAddr = args[1];
		int mcastPort = Integer.parseInt(args[2]);

		DatagramSocket socket = new DatagramSocket(port);
		
		int size = 2048;
		byte[] buf = new byte[size];
		
		DatagramPacket packet;
		
		// Database
		HashMap<String, String> licensePlates = new HashMap<String, String>();
		
		MyBroadcast broadcast = new MyBroadcast(mcastAddr, mcastPort, port);
		
		Thread thread = new Thread(broadcast);
		thread.start();
		
		while(true) {
			buf = new byte[size];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			String received = new String(packet.getData()).trim();
			
			String response = processRequest(licensePlates, received);
			
			// Creates response
			byte[] rbuf = response.getBytes();
			packet = new DatagramPacket(rbuf, rbuf.length, packet.getAddress(), packet.getPort());
			socket.send(packet);
			
			// Close connection
			if(response.equals("close")) {
				break;
			}
			
		}
		
		socket.close();
		
	}

	private static String processRequest(HashMap<String, String> licensePlates, String received) {
				
		String[] data = received.split(" ");
		
		printLog(data);
		
		String response;
		if(data[0].equals("REGISTER")) {
			response = "" + register(licensePlates, data[1], data[2]);	
		} else if(data[0].equals("LOOKUP")) {		
			response = "" + lookup(licensePlates, data[1]);		
		} else if(data[0].equals("CLOSE")) {	
			response = "close";		
		} else {	
			response = "what?";	
		}
		
		return response;
		
	}
	
	private static void printLog(String[] data) {
		
		if(data.length > 0) {
			System.out.println(data[0]);
		} 
		
		if(data.length > 1) {
			System.out.println(" " + data[1]);
		}
		
		if(data.length > 2) {
			System.out.println(" " + data[2]);
		}

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
