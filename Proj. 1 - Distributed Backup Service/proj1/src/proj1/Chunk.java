package proj1;

import java.io.Serializable;

public class Chunk implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -25902542160122730L;
	private String fileId; 
	private int chunkNo;
	private byte[] data;
	
	public Chunk(String fileId, int chunkNo) {
		this.fileId = fileId;
		this.chunkNo = chunkNo;
	}
	
	public void setData(byte[] pData) {
		data = pData;
	}
	
	public String getFileId() {
		return this.fileId;
	}
	
	public int chunkNo() {
		return this.chunkNo;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public int getSize() {
		return getData() != null ? getData().length : 0;
	}

}
