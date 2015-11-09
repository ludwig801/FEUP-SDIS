package protocol;

/**
 * Class to interpret and validate the message headers following the implemented protocol. 
 * @author Kevin Amorim & Luís Magalhães
 *
 */

public class BaseProtocol {
	
	private final String[] types = { "PUTCHUNK", "STORED", "GETCHUNK", "CHUNK", "DELETE", "REMOVED" };
	
	private String messageType;
	private String version;
	private String fileId;
	private int chunkNo;
	private int replicationDeg;
	
	private byte[] body;
	
	private boolean valid;
	
	public BaseProtocol(byte[] packet, int length) {
		messageType = null;
		version = null;
		fileId = null;
		chunkNo = -1;
		replicationDeg = -1;
		
		valid = validate(new String(getHeader(packet)));
		if(valid && length > 0) body = getBody(packet, length);
	}
	
	// Should follow protocol: 
	// <MessageType> <Version> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>
	public boolean validate(String pMsg) {
		
		String[] fields = pMsg.split(" ");
		if(fields.length == 0) return false;
				
		// Position in the msg
		int pos = 0;

		// MessageType
		if(fields.length > pos) {
			if(checkMessageType(fields[pos])) {
				messageType = fields[pos];
				pos++;
			} else {
				System.out.println("MessageType not found in message. Cannot continue.");
				return false;
			}
		}
		
		// Version
		if(fields.length > pos) {
			if(checkVersion(fields[pos])) {
				version = fields[pos];
				pos++;
			} else {
				System.out.println("Version not found in message. Using default 1.0");
				version = "1.0";
			}
		}
		
		// FileID (mandatory) 
		if(fields.length > pos) {
			if(checkFileId(fields[pos])) {
				fileId = fields[pos];
				pos++;
			} else {
				System.out.println("FileId not found in message. Cannot continue.");
				return false;
			}
		}
		
		// DELETE command does not have any other parameter
		if(messageType.toUpperCase().equals("DELETE")) 
			return fields.length > pos ? checkHeaderTerminator(fields[pos]) : false;
		
		// ChunkNo
		if(fields.length > pos) {
			if(checkChunkNo(fields[pos])) {
				chunkNo = Integer.parseInt(fields[pos]);
				pos++;
			} else {
				System.out.println("ChunkNo not found in message. Cannot continue.");
				return false;
			}
		}
		
		if(!messageType.toUpperCase().equals("PUTCHUNK")) return fields.length > pos ? checkHeaderTerminator(fields[pos]) : false;
		
		if(fields.length > pos) {
			if(checkReplicationDeg(fields[pos])) {
				replicationDeg = Integer.parseInt(fields[pos]);
				pos++;
			} else {
				System.out.println("ReplicationDeg not found in message. Cannot continue.");
				return false;
			}
		}
	
		return fields.length > pos ? checkHeaderTerminator(fields[pos]) : false;
	}
		
	// CHECKERS
	public boolean checkMessageType(String p) {	
		for(int i = 0; i < types.length; i++) 
			if(types[i].equals(p.toUpperCase())) return true;
		
		return false;
	}
	
	public boolean checkVersion(String p) {
		char[] tmpChar = p.toCharArray();
		
		if(tmpChar.length == 3) 
			if(Character.isDigit(tmpChar[0]) && Character.isDigit(tmpChar[2]) && tmpChar[1] == '.') 
				return true;
		
		return false;
	}
	
	public boolean checkFileId(String p) {		
		return p.toCharArray().length == 64;
	}
	
	public boolean checkChunkNo(String p) {
		char[] tmpChar = p.toCharArray();
		
		if(tmpChar.length <= 6 && tmpChar.length > 0) {
			int i = 0;
			for( ; i < tmpChar.length; i++) {
				if(!Character.isDigit(tmpChar[i])) return false;
			}
			return true;
		} 
		
		return false;
	}
	
	public boolean checkReplicationDeg(String p) {
		return !(p.toCharArray().length != 1 || !Character.isDigit(p.toCharArray()[0]));
	}
	
	public boolean checkHeaderTerminator(String p) {
		return p.equals("\r\n\r\n");
	}
	
	// GETTERS
	public boolean isValid() {
		return valid;
	}

	public String getMessageType() {
		return messageType;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getFileId() {
		return fileId;
	}
	
	public int getChunkNo() {
		return chunkNo;
	}
	
	public int getReplicationDeg() {
		return replicationDeg;
	}
	
	public byte[] getBody() {
		return body;
	}
	
	public String print() {
		String tmp = "";
		if(messageType != null) tmp += messageType;
		else return "INVALID";
		if(version != null) tmp += " " + version;
		if(fileId != null) tmp += " " + fileId;
		if(chunkNo != -1) tmp += " " + chunkNo;
		if(replicationDeg != -1) tmp += " " + replicationDeg;
		return tmp;
	}
	
	private byte[] getBody(byte[] data, int length) {
		int start = findBodyStartByte(data);
		byte[] res = new byte[length - start];
		
		System.arraycopy(data, start, res, 0, length - start);
		
		return res;
	}
	
	private byte[] getHeader(byte[] data) {
		int body_start = findBodyStartByte(data);
		int res_size = body_start != -1 ? body_start : data.length;
		byte[] res = new byte[res_size];
		System.arraycopy(data, 0, res, 0, res_size);
		return res;
	}
	
	private int findBodyStartByte(byte[] data) {
		byte[] tmp = data;
		for(int i = 0; i < tmp.length; i++) {
			if(tmp[i] == '\r') {
				if(((i+1) < tmp.length && tmp[i + 1] == '\n') && ((i+2) < tmp.length && tmp[i + 2] == '\r') && ((i+3) < tmp.length && tmp[i + 3] == '\n')) {
					return (i+4);
				}
			}
		}
		return -1;
	}

}
