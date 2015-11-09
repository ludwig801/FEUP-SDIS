import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class Client {
	
	public static void main(String args[]) throws IOException {
		
		if(args.length < 3 || args.length > 5) {
			System.out.println("Usage: java Client <host_name> <port_number> <oper> <opnd>*");
			return;
		}
		
		InetAddress address = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);
		DatagramSocket socket = new DatagramSocket();
			
		String msg = "";
		
		// If a license plate is passed as arg check if is valid
		if(args.length > 3) {
			
			String plateNr = args[3];
			
			if(!LicensePlate.checkPlateNr(plateNr)) {
				
				System.out.println("Register: Invalid plate format.");
				
			} else {
				
				if(args[2].equals("register")) {
					
					if(args.length == 5) {	
						msg = register(args);			
					} else {		
						System.out.println("Register: Invalid number of arguments.");
					}
					
				} else if(args[2].equals("lookup")) {	
					
					if(args.length == 4) {
						msg = lookup(args);	
					} else {		
						System.out.println("Lookup: Invalid number of arguments.");		
					}
					
				} 
				
			}
			
		} else  if(args[2].equals("close")) {
			
			msg = close();
			
		} else {
			
			System.out.println("Error: Invalid operand.");
			
		}

		if(msg != "") {
			send(socket, msg, address, port);
			String received = receive(socket, address, port);
			System.out.println(received);
		}
		
		socket.close();
		
	}
	
	private static String receive(DatagramSocket socket, InetAddress address, int port) throws IOException {
		byte[] rbuf = new byte[2048];
		DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length);
		socket.receive(packet);
		String received = new String(packet.getData());
		return received.trim();
	}
	
	private static void send(DatagramSocket socket, String msg, InetAddress address, int port) throws IOException {
		byte[] buf = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
	}
	
	private static String register(String[] args) throws IOException {
		String plateNr = args[3];
		String ownerName = args[4];
		return "REGISTER " + plateNr + " " + ownerName;	
	}

	private static String lookup(String[] args) throws IOException {	
		String plateNr = args[3];
		return "LOOKUP " + plateNr;
	}

	private static String close() {
		return "CLOSE";
	}
}
