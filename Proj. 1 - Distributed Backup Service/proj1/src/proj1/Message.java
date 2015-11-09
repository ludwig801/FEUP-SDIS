package proj1;

import utils.Helper;

public class Message {
	
	private final String CRLF = "\r\n";
	
	private String type;
	private String version;
	private String fileId;
	private int chunkNo;
	private int replicationDeg;
	private Chunk chunk;
		
	public Message(String pVersion) {
		type = null;
		version = pVersion;
		fileId = null;
		chunkNo = -1;
		replicationDeg = -1;
		chunk = null;
	}
	
	public void setMsgPutChunk(String pFileId, int pChunkNo, int pReplicationDeg, Chunk pChunk) {
		type = "PUTCHUNK";
		fileId = pFileId;
		chunkNo = pChunkNo;
		chunk = pChunk;
		replicationDeg = pReplicationDeg;
	}
	
	public void setMsgStored(String pFileId, int pChunkNo) {
		type = "STORED";
		fileId = pFileId;
		chunkNo = pChunkNo;
	}
	
	public void setMsgGetChunk(String pFileId, int pChunkNo) {
		type = "GETCHUNK";
		fileId = pFileId;
		chunkNo = pChunkNo;
	}
	
	public void setMsgChunk(String pFileId, int pChunkNo) {
		type = "CHUNK";
		fileId = pFileId;
		chunkNo = pChunkNo;
	}
	
	public void setMsgDelete(String pFileId) {
		type = "DELETE";
		fileId = pFileId;
	}
	
	public void setMsgRemoved(String pFileId, int pChunkNo) {
		type = "REMOVED";
		fileId = pFileId;
		chunkNo = pChunkNo;
	}
	
	public byte[] getHeader() {
		String msg = type + " " + version;
		
		if(fileId != null) {
			msg += " " + fileId;
		}
		
		if(chunkNo != -1) {
			msg += " " + chunkNo;
		}
		
		if(replicationDeg != -1) {
			msg += " " + replicationDeg;
		}
		
		msg += " " + (CRLF + CRLF);
		
		return msg.getBytes();
	}
	
	public byte[] getBody() {
		return chunk != null ? chunk.getData() : null;
	}
	
	public int getMsgSize() {
		return get().length;
	}
	
	public int getBodySize() {
		return getBody().length;
	}
	
	public int getHeaderSize() {
		return getHeader().length;
	}
	
	public byte[] get() {	
		return getBody() != null ? Helper.combine(getHeader(), getBody()) : getHeader();
	}

}
