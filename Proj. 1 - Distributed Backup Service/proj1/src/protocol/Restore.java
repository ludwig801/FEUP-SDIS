package protocol;

import java.io.File;
import java.net.DatagramPacket;
import java.util.Vector;

import database.MyDatabase;
import proj1.Chunk;
import proj1.Message;
import proj1.MyChannel;
import proj1.MyFile;
import utils.Constants;
import utils.FileHelper;
import utils.Helper;

public class Restore implements SubProtocol {
	
	private static final String REPLY_TYPE = "CHUNK";
	private static final String VERSION = "1.0";
	
	public byte[] buildInvocation(String pFileId, int pChunkNo, int pReplicationDeg, Chunk pChunk) {
		Message msg = new Message(VERSION);
		msg.setMsgGetChunk(pFileId, pChunkNo);
		return msg.get();
	}
	
	public byte[] apply(DatagramPacket packet) {
		
		BaseProtocol base = new BaseProtocol(packet.getData(), packet.getLength());
		
		File f = new File(Constants.BACKUP_FOLDER_NAME + "\\" + base.getFileId());
		if(f.exists() && f.isDirectory()) {
			
			String path = Constants.BACKUP_FOLDER_NAME + "\\" + base.getFileId() + "\\" + base.getFileId() + "_" + base.getChunkNo();
			File tmp = new File(path);
			if(tmp.exists() && !tmp.isDirectory()) {
				MyFile chunk = new MyFile(path);
				return Helper.combine(buildReply(base), chunk.getData());
			}
			
		}
		
		return null;
	}
	
	public boolean restore(MyChannel mc, MyChannel mdr, MyDatabase db, String pFileId, int pNumberOfChunks) {
		
		// Init
		Helper.createDir("restored\\" + pFileId);
		
		// Receive chunks
		Vector<DatagramPacket> packets = new Vector<DatagramPacket>();
		
		Thread listener = new Thread() {
			public void run() {
				while(mdr.running) {
					DatagramPacket tmp = mdr.receive();
					packets.add(tmp);
				}
			}
		};
		listener.start();
		
		// Send GETCHUNK messages for all chunks
		for(int i = 0; i < pNumberOfChunks; i++) {
			
			byte[] msg = buildInvocation(pFileId, i, -1, null);
			mc.send(msg);
			
			while(true) {
				if(!packets.isEmpty()) {
					
					DatagramPacket tmpPacket = packets.remove(0);
					if(tmpPacket != null) {
						BaseProtocol base = new BaseProtocol(tmpPacket.getData(), tmpPacket.getLength());
						System.out.println("chunk: " + base.getChunkNo());
						if(base.getMessageType().equals("CHUNK") && base.getFileId().equals(pFileId) && base.getChunkNo() == i) {
							String path = "restored\\" + base.getFileId() + "\\" + base.getFileId() + "_" + base.getChunkNo();
							FileHelper.writeToFile(base.getBody(), base.getBody().length, path);
							break;
						}
					}
				}
			}
		}
		
		
		mdr.close();
		
		return true;
	}
	
	public byte[] buildReply(BaseProtocol base) {
		return (REPLY_TYPE + " " + VERSION + " " + base.getFileId() + " " + base.getChunkNo() + " \r\n\r\n").getBytes();
	}

}