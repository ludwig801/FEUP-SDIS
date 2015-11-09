package protocol;

import java.io.File;
import java.net.DatagramPacket;

import database.MyDatabase;
import proj1.Chunk;
import proj1.Message;
import proj1.MyChannel;
import utils.Constants;
import utils.FileHelper;

public class Delete implements SubProtocol {
	
	private static final String REPLY_TYPE = "REMOVED";
	private static final String VERSION = "1.0";
	
	@Override
	public byte[] buildInvocation(String pFileId, int pChunkNo, int pReplicationDeg, Chunk pChunk) {
		Message msg = new Message(VERSION);
		msg.setMsgDelete(pFileId);
		return msg.get();
	}
	
	public byte[] apply(DatagramPacket packet) {
		
		BaseProtocol base = new BaseProtocol(packet.getData(), packet.getLength());
		
		String path = Constants.BACKUP_FOLDER_NAME + "\\" + base.getFileId();
		File directory = new File(path);
		
		FileHelper.deleteDirectory(directory);
		
		return null;
	}
	
	public boolean delete(MyChannel mc, MyDatabase db, String pFileId) {
		db.deleteChunks(pFileId);
		db.deleteFile(pFileId);
		byte[] msg = buildInvocation(pFileId, -1, -1, null);
		mc.send(msg);
		return true;
	}
	
	public byte[] buildReply(BaseProtocol base) {
		return (REPLY_TYPE + " " + VERSION + " " + base.getFileId() + " " + base.getChunkNo() + "\r\n\r\n").getBytes();
	}

}
