import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

class Client {
	
	public static void main(String args[]) throws IOException {
		
		if(args.length < 3 || args.length > 5) {
			System.out.println("Usage: java Client <host_name> <port_number> <oper> <opnd>*");
			return;
		}
		
		InetAddress address = InetAddress.getByName(args[0]);
		int port = Integer.parseInt(args[1]);
		
		// Client socket
		Socket socket = new Socket(address, port);
		BufferedReader in = null;
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		Message msg = new Message();
		
		// If a license plate is passed as arg check if is valid
		if(args.length > 3) {
			
			String plateNr = args[3];
			
			if(!LicensePlate.checkPlateNr(plateNr)) {
				
				System.out.println("Register: Invalid plate format.");
				
			} else {
				
				if(args[2].equals("register")) {
					
					if(args.length == 5) {	
						register(args, msg);			
					} else {		
						System.out.println("Register: Invalid number of arguments.");
					}
					
				} else if(args[2].equals("lookup")) {	
					
					if(args.length == 4) {
						lookup(args, msg);	
					} else {		
						System.out.println("Lookup: Invalid number of arguments.");		
					}
					
				} else {
					System.out.println("Error: Invalid operation.");
				}
				
			}
			
		} else  if(args[2].equals("close")) {
			
			close(msg);
			
		} else {
			
			System.out.println("Error: Invalid operand.");
			
		}
		
		if(msg.getOperation() != "") {
			send(out, msg);
		}
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String response = in.readLine();
		msg.setResponse(response);
		System.out.println(msg.getDebug());
		
		in.close();
		out.close();
		socket.close();
		
	}

	
	private static void send(PrintWriter out, Message msg) {
		out.println(msg.get());
	}
	
	private static void register(String[] args, Message msg) throws IOException {
		String plateNr = args[3];
		String ownerName = args[4];
		msg.setOperation("REGISTER");
		msg.setLicense(plateNr);
		msg.setName(ownerName);
	}

	private static void lookup(String[] args, Message msg) throws IOException {	
		String plateNr = args[3];
		msg.setOperation("LOOKUP");
		msg.setLicense(plateNr);
	}

	private static void close(Message msg) {
		msg.setOperation("CLOSE");
	}

}
