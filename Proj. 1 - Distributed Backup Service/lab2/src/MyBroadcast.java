import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MyBroadcast implements Runnable {

	final static int SIZE = 1024;

	InetAddress group;
	int port;
	
	MulticastSocket socket;
	
	int srvPort;
	

	public MyBroadcast(String mcastAddr, int mcastPort) {

		try {
			group = InetAddress.getByName(mcastAddr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		port = mcastPort;
		socket = createSocket();

	}
	
	public MyBroadcast(String mcastAddr, int mcastPort, int srvPort) {
		
		this(mcastAddr, mcastPort);
		this.srvPort = srvPort;
			
	}

	@Override
	public void run() {
		
		String msg = "" + srvPort;

		while (true) {
			send(msg);
			sleep(1000);
		}

	}
	
	public DatagramPacket receive() {
		
		byte[] buf = new byte[SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return packet;	
	}
	
	public void send(String msg) {
		
		DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), group, port);
		
		try {
			
			socket.send(packet);
			System.out.println("multicast: " + group.getHostAddress() + " " + port + " : " + InetAddress.getLocalHost() + " " + srvPort);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}

	private MulticastSocket createSocket() {

		MulticastSocket socket;
		try {
			socket = new MulticastSocket(port);
			socket.joinGroup(group);
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}
		
	private void sleep(long milli) {
		
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
