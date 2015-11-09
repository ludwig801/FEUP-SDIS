package proj1;

import java.util.ArrayList;
import java.util.List;

import utils.Constants;
import utils.FileHelper;

public class MyFile {
	
	private final String dateFormat = "yyyMMddHHmmss";	
	
	private String path;
	private String lastModified;
	private String ownerName;
	
	private String header;
	
	private UniqueIdentifier fileId;
	
	private List<Chunk> chunks;
	
	private int chunkMaxSize;
		
	public MyFile(String pPath, int chunkMaxSize) {
				
		if(FileHelper.fileExists(pPath)) {
			
			path = pPath;
			//filename = pPath.split("\\.")[0];
			lastModified = FileHelper.getLastModified(pPath, dateFormat);
			ownerName = FileHelper.getOwnerName(pPath);
			setHeader();
			setFileId();
			
			this.chunks = new ArrayList<Chunk>();
			this.chunkMaxSize = chunkMaxSize;
			
			split();
			
		} else {
			System.err.println("MyFile Error: File not found.");
		}
		
	}
	
	public MyFile(String path) {
		this(path, Constants.PACKET_SIZE - Constants.AVERAGE_HEADER_SIZE);
	}
	
	public MyFile(String pFileId, String pFilename) {
		fileId = new UniqueIdentifier();
		fileId.setIdentifier(pFileId);
		path = pFilename;
		this.chunks = new ArrayList<Chunk>();
	}
	
	public String toString() {
		return "Name: " + path + '\n' + "Date Modified: " + lastModified + '\n' + "Owner Name: " + ownerName;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getFileId() {
		return fileId.getIdentifier();
	}
	
	public byte[] getData() {
		return FileHelper.readFile(path);
	}
	
	public int getNumberOfChunks() {
		return this.chunks != null ? this.chunks.size() : 0;
	}
	
	public Chunk getChunkNo(int n) {
		return this.chunks != null ? this.chunks.get(n) : null;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void addChunk(Chunk c) {
		this.chunks.add(c);
	}
	
	public void merge() {
		
		if(chunks == null || chunks.size() == 0) {
			System.err.println("MyFile: No chunks to merge.");
		} else {
			int size = 0;
			for(int i = 0; i < chunks.size(); i++) {
				size += chunks.get(i).getSize();
			}
			
			byte[] buf = new byte[size];
			int k = 0;
			for(int i = 0; i < chunks.size(); i++) {
				System.arraycopy(chunks.get(i).getData(), 0, buf, k, chunks.get(i).getSize());
				k += chunks.get(i).getSize();
			}
			
			FileHelper.writeToFile(buf, buf.length, path);
			System.out.println("MyFile: Chunks merged.");
		}
		
	}
	
	private void setHeader() {
		header = path + "_" + lastModified + "_" + ownerName;
	}
	
	private void setFileId() {
		fileId = new UniqueIdentifier(getHeader());
	}
	
	private void split() {
		
		byte[] buf = getData();
		double tmp = (buf.length / (double)chunkMaxSize);
		int n = (int) Math.ceil(tmp);
		
		int k = 0;
		int chunkNo = 0;
		for(int i = 0; i < n; i++) {
			int size = (i == (n-1)) ? (buf.length - (i * chunkMaxSize)) : chunkMaxSize;
			byte[] chunkBuf = new byte[size];
			for(int j = 0; j < size && k < buf.length; j++) {
				chunkBuf[j] = buf[k];
				k++;
			}
			
			Chunk tmpChunk = new Chunk(this.fileId.getIdentifier(), chunkNo);
			tmpChunk.setData(chunkBuf);
			this.chunks.add(tmpChunk);
			chunkNo++;
			
		}
	}
}
