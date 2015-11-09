package proj1;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import database.MyDatabase;
import protocol.*;
import utils.Helper;

public class Server implements Runnable {	
	private String mc_addr;
	private String mdb_addr;
	private String mdr_addr;
	
	private int mc_port;
	private int mdb_port;
	private int mdr_port;
	
	private MyChannel mc_channel;
	private MyChannel mdb_channel;
	private MyChannel mdr_channel;
	
	private Vector<DatagramPacket> mc_packets;
	private Vector<DatagramPacket> mdb_packets;
	private Vector<DatagramPacket> mdr_packets;
	
	private MyDatabase db;
	
	private String backup_folder_name;
	
	public Server(String mc_addr, int mc_port, String mdb_addr, int mdb_port, String mdr_addr, int mdr_port, String db_name) {
		this.mc_addr = mc_addr;
		this.mc_port = mc_port;
		this.mdb_addr = mdb_addr;
		this.mdb_port = mdb_port;
		this.mdr_addr = mdr_addr;
		this.mdr_port = mdr_port;
		MyDatabase db = new MyDatabase(db_name);
		db.connect();
		
		mc_packets = new Vector<DatagramPacket>();
		mdb_packets = new Vector<DatagramPacket>();
		mdr_packets = new Vector<DatagramPacket>();
	}

	@Override
	public void run() {
		
		System.out.println("Server: Initialized Server thread. Peer is ready");
		System.out.println("Waiting for packets...");
		
		mc_channel = new MyChannel("MC", mc_addr, mc_port);
		mdb_channel = new MyChannel("MDB", mdb_addr, mdb_port);
		mdr_channel = new MyChannel("MDR", mdr_addr, mdr_port);
		
		// Listener thread
		Thread mcListenerThread = new Thread() {
			public void run() {
				while(mc_channel.running) {
					DatagramPacket tmp = mc_channel.receive();
					mc_packets.add(tmp);
				}
			}
		};
		mcListenerThread.start();
		
		// Processing thread
		Thread mcProcessThread = new Thread() {
			public void run() {
				while(true) {
					if(!mc_packets.isEmpty()) {
						processPacket(mc_packets.remove(0));
					}
				}
			}
		};
		mcProcessThread.start();
		
		// Listener thread
		Thread mdbListenerThread = new Thread() {
			public void run() {
				while(mdb_channel.running) {
					DatagramPacket tmp = mdb_channel.receive();
					mdb_packets.add(tmp);
				}
			}
		};
		mdbListenerThread.start();
		
		// Processing thread
		Thread mdbProcessThread = new Thread() {
			public void run() {
				while(true) {
					if(!mdb_packets.isEmpty()) {
						processPacket(mdb_packets.remove(0));
					}
				}
			}
		};
		mdbProcessThread.start();
		
		// Listener thread
		Thread mdrListenerThread = new Thread() {
			public void run() {
				while(mdr_channel.running) {
					DatagramPacket tmp = mdr_channel.receive();
					mdr_packets.add(tmp);
				}
			}
		};
		mdrListenerThread.start();
		
	}
	
	public void close() {
		System.out.println("Server: Closing server connection.");
		db.close();
	}
	
	private void processPacket(DatagramPacket packet) {
		Random r = new Random();
		BaseProtocol base = new BaseProtocol(packet.getData(), packet.getLength());
		System.out.println("Received: " + base.print());
		if(base.isValid()) {
			if(base.getMessageType().equals("PUTCHUNK")) {
				byte[] reply = applySubProtocol(new Backup(), packet);
				Helper.sleep(r.nextInt(401));
				mc_channel.send(reply);
			} else if(base.getMessageType().equals("GETCHUNK")) {

				getchunk(packet); 

			} else if(base.getMessageType().equals("DELETE")) {
				applySubProtocol(new Delete(), packet);
			} else if(base.getMessageType().equals("STORED")) {
				return;
			}
		}
	}
	
	private byte[] applySubProtocol(SubProtocol protocol, DatagramPacket packet) {
		return protocol.apply(packet);
	}
	
	private void getchunk(DatagramPacket packet) {
		
		BaseProtocol base = new BaseProtocol(packet.getData(), packet.getLength());
		Random r = new Random();
		mdr_packets.clear();
		Helper.sleep(r.nextInt(401));
		Vector<DatagramPacket> copy = mdr_packets;
		mdr_packets.clear();
		boolean found = false;
		while(!copy.isEmpty()) {
			DatagramPacket tmp = copy.remove(0);
			BaseProtocol tmpBase = new BaseProtocol(tmp.getData(), tmp.getLength());
			if(tmpBase.getMessageType().equals("CHUNK") 
					&& tmpBase.getFileId().equals(base.getFileId()) 
					&& tmpBase.getChunkNo() == base.getChunkNo()) {
				found = true;
			}
			
			if(found) break;
		}
		
		if(!found) {
			byte[] reply = applySubProtocol(new Restore(), packet);
			mdr_channel.send(reply);
		} else {
			System.out.println("Chunk already sent by another peer. Avoiding flood...");
		}
		
	}
}
