package protocol;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import database.MyDatabase;
import proj1.Chunk;
import proj1.Message;
import proj1.MyChannel;
import proj1.MyFile;
import utils.Constants;
import utils.FileHelper;
import utils.Helper;

public class Backup implements SubProtocol {
	
	private static final String REPLY_TYPE = "STORED";
	private static final String VERSION = "1.0";
	
	public byte[] buildInvocation(String pFileId, int pChunkNo, int pReplicationDeg, Chunk pChunk) {
		Message msg = new Message(VERSION);
		msg.setMsgPutChunk(pFileId, pChunkNo, pReplicationDeg, pChunk);
		return msg.get();
	}
	
	public byte[] apply(DatagramPacket packet) {
		
		BaseProtocol base = new BaseProtocol(packet.getData(), packet.getLength());
		
		Helper.createDir(Constants.BACKUP_FOLDER_NAME + "\\" + base.getFileId());
		
		String path = Constants.BACKUP_FOLDER_NAME + "\\" + base.getFileId() + "\\" + generateFilename(base.getFileId(), base.getChunkNo());
		FileHelper.writeToFile(base.getBody(), base.getBody().length, path);
		
		//System.out.println("Chunk " + base.getChunkNo() + " successfully written to file.");
		
		return buildReply(base);
		
	}
	
	public boolean backupFile(MyChannel mc, MyChannel mdb, MyFile file, MyDatabase db, int repDeg) {
		
		int numberOfTries = 5;
		long waitTimeMs = 500;
		
		db.addFile(file.getFileId(), file.getPath(), file.getNumberOfChunks());
		
		Vector<Vector<String>> hosts = new Vector<Vector<String>>();
		
		// Init hosts vector
		for(int i = 0; i < file.getNumberOfChunks(); i++) {
			hosts.add(i, new Vector<String>());
		}
		
		// Listen to MC Channel
		List<DatagramPacket> packets = new ArrayList<DatagramPacket>();
		Thread mcThread = new Thread() {
			public void run() {
				while(mc.running) {
					DatagramPacket packet = mc.receive();
					packets.add(packet);
				}
			}
		};
		mcThread.start();
		
		for(int i = 0; i < numberOfTries; i++) {
			
			for(int j = 0; j < file.getNumberOfChunks(); j++) {
				
				db.addChunk(file.getFileId(), j, repDeg);
				if(db.getReplicationDegreeOfChunk(file.getFileId(), j) < repDeg) {
					byte[] msg = buildInvocation(file.getFileId(), j, repDeg, file.getChunkNo(j));
					mdb.send(msg);
				} 
			}
			
			Helper.sleep(waitTimeMs);
			
			// Check STORED messages
			while(!packets.isEmpty()) {
				DatagramPacket tmpPacket = packets.remove(0);
				if(tmpPacket == null) break;
				String host = tmpPacket.getAddress().getHostAddress();
				BaseProtocol base = new BaseProtocol(tmpPacket.getData(), tmpPacket.getLength());
				if(base.getMessageType().equals("STORED")) {
					if(base.getFileId().equals(file.getFileId()) && !hosts.get(base.getChunkNo()).contains(host)) {
						System.out.println("Host checked: " + host);
						hosts.get(base.getChunkNo()).add(host);
						db.incrementReplicationDegree(file.getFileId(), base.getChunkNo());
					}
				}
			} 
			
			// Check Replication Degrees
			int j = 0;
			for(; j < file.getNumberOfChunks(); j++) {
				if(db.getReplicationDegreeOfChunk(file.getFileId(), j) < repDeg)
					break;
				System.out.println("Chunk " + j + " backed up successfully.");
			}

			if(j == file.getNumberOfChunks()) {
				mc.close();
				mcThread.interrupt();
				return true;
			}
			
			waitTimeMs *= 2;
		}
		
		mc.close();
		return false;
	}
	
	public byte[] buildReply(BaseProtocol pBase) {
		return (REPLY_TYPE + " " + VERSION + " " + pBase.getFileId() + " " + pBase.getChunkNo() + " \r\n\r\n").getBytes();
	}

	private String generateFilename(String pFileId, int pChunkNo) {
		return pFileId + "_" + pChunkNo;
	}


}
