package proj1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MyChannel implements Runnable {
	
	private String name;
	private InetAddress address;
	private int port;
	
	private MulticastSocket socket;
	
	private List<DatagramPacket> packets_received;
	
	public boolean running;
	
	public MyChannel(String name, String address, int port) {
		
		this.name = name;
		this.address = null;
		try {
			this.address = InetAddress.getByName(address);
			this.port = port;
			this.socket = new MulticastSocket(this.port);
			socket.joinGroup(this.address);
		} catch (UnknownHostException e) {
			System.err.println(name + ": Error getting InetAddress.");
		} catch (IOException e) {
			System.err.println(name + ": Error creating socket or joining group.");
		}
		
		this.packets_received = new ArrayList<DatagramPacket>();
		
		running = true;
		
	}
	
	public void send(byte[] msg) {
		DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);
		
		try {	
			socket.send(packet);
			//System.out.println(address.getHostAddress() + ":" + port + " : " + msg);
			
		} catch (IOException e) {
			System.err.println(name + ": Failed to send message to channel.");
			e.printStackTrace();
		}
	}
	
	public void send(String msg) {
		DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
		
		try {
			
			socket.send(packet);
			//System.out.println(address.getHostAddress() + ":" + port + " : " + msg);
			
		} catch (IOException e) {
			System.err.println(name + ": Failed to send message to channel.");	
		}
	}

	
	public DatagramPacket receive() {
		int packet_size = 64000;
		byte[] buf = new byte[packet_size];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
			//System.out.println("Received: " + new String(packet.getData()).split("\r\n\r\n")[0]);
			return packet;
		} catch (IOException e) {
			System.err.println(name + ": Error receiving packet.");
		}
		return null;
	}
	
	public DatagramPacket getNextMsg() {
		return packets_received.isEmpty() ? null : packets_received.remove(0);
	}

	@Override
	public void run() {
		System.out.println(name + ": Thread initialized.");
		while(running) {
			DatagramPacket packet = receive();
			packets_received.add(packet);
		}
	}
	
	public void close() {
		this.running = false;
		this.socket.close();
	}

}
