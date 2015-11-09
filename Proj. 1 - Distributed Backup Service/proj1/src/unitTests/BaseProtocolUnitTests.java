package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import protocol.BaseProtocol;

public class BaseProtocolUnitTests {

	@Test
	public void testCheckMessageType() {
		BaseProtocol b = new BaseProtocol("".getBytes(), 0);
		
		String p = "putchunk";
		assertEquals(b.checkMessageType(p), true);
		
		p = "STORED";
		assertEquals(b.checkMessageType(p), true);
		
		p = "Random";
		assertEquals(b.checkMessageType(p), false);
		
		p = "";
		assertEquals(b.checkMessageType(p), false);
		
	}
	
	@Test
	public void testCheckVersion() {
		BaseProtocol b = new BaseProtocol("".getBytes(), 0);
		
		String p = "1.0";
		assertEquals(b.checkVersion(p), true);
		
		p = "1.a";
		assertEquals(b.checkVersion(p), false);
		
		p = "a.1";
		assertEquals(b.checkVersion(p), false);
		
		p = "1a";
		assertEquals(b.checkVersion(p), false);
		
		p = "";
		assertEquals(b.checkVersion(p), false);
		
	}

	@Test
	public void testCheckFileId() {
	
		BaseProtocol b = new BaseProtocol("".getBytes(), 0);
		
		String p = "de09f1bd7e5de396deada91088072f46fde0388ed3d0553722fdd4e862663622";
		assertEquals(b.checkFileId(p), true);
		
		p = "fileid";
		assertEquals(b.checkFileId(p), false);
		
		p = "";
		assertEquals(b.checkFileId(p), false);
		
	}
	
	@Test
	public void testCheckChunkNo() {
		
		BaseProtocol b = new BaseProtocol("".getBytes(), 0);
		
		String p = "1";
		assertEquals(b.checkChunkNo(p), true);
			
		p = "100000";
		assertEquals(b.checkChunkNo(p), true);
		
		p = "100a";
		assertEquals(b.checkChunkNo(p), false);
		
		p = "a";
		assertEquals(b.checkChunkNo(p), false);
		
		p = "1000000";
		assertEquals(b.checkChunkNo(p), false);
		
		p = "";
		assertEquals(b.checkChunkNo(p), false);
		
	}
	
	@Test
	public void testCheckReplicationDeg() {
		
		BaseProtocol b = new BaseProtocol("".getBytes(), 0);
		
		String p = "1";
		assertEquals(b.checkReplicationDeg(p), true);
		
		p = "10";
		assertEquals(b.checkReplicationDeg(p), false);
		
		p = "a";
		assertEquals(b.checkReplicationDeg(p), false);
		
		p = "";
		assertEquals(b.checkReplicationDeg(p), false);
	}
	
	@Test
	public void testCheckHeaderTerminator() {
		
		BaseProtocol b = new BaseProtocol("".getBytes(), 0);
		
		String p = "\r\n\r\n";
		assertEquals(b.checkHeaderTerminator(p), true);
		
		p = "\r\n";
		assertEquals(b.checkHeaderTerminator(p), false);
		
		p = "";
		assertEquals(b.checkHeaderTerminator(p), false);
	}
	
	@Test
	public void checkValidate() {
		
		// Valid values
		String version = "1.0";
		String fileId = "de09f1bd7e5de396deada91088072f46fde0388ed3d0553722fdd4e862663622";
		String chunkNo = "1";
		String repDeg = "2";
		String term = "\r\n\r\n";
		
		BaseProtocol b = new BaseProtocol(("PUTCHUNK " + version + " " + fileId + " " + chunkNo + " " + repDeg + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("STORED " + version + " " + fileId + " " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("GETCHUNK " + version + " " + fileId + " " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("DELETE " + version + " " + fileId + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("REMOVED " + version + " " + fileId + " " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol("".getBytes(), 0); 
		assertEquals(b.isValid(), false);
		
		// PUTCHUNK Exceptions
		b = new BaseProtocol(("PUTCHUNK " + fileId + " " + chunkNo + " " + repDeg + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("PUTCHUNK " + " " + chunkNo + " " + repDeg + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("PUTCHUNK " + fileId + " " + repDeg + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("PUTCHUNK " + fileId + " " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("PUTCHUNK " + fileId + " " + chunkNo + " " + repDeg).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		// STORED Exceptions
		b = new BaseProtocol(("STORED " + fileId + " " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("STORED " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("STORED " + fileId + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("STORED " + fileId + " " + chunkNo).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		// GETCHUNK Exceptions
		b = new BaseProtocol(("GETCHUNK " + fileId + " " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("GETCHUNK " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("GETCHUNK " + fileId + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("GETCHUNK " + fileId + " " + chunkNo).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		// DELETE Exceptions
		b = new BaseProtocol(("DELETE " + fileId + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("DELETE " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("DELETE " + fileId + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		// REMOVED Exceptions
		b = new BaseProtocol(("REMOVED " + fileId + " " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), true);
		
		b = new BaseProtocol(("REMOVED " + chunkNo + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("REMOVED " + fileId + " " + term).getBytes(), 0);
		assertEquals(b.isValid(), false);
		
		b = new BaseProtocol(("REMOVED " + fileId + " " + chunkNo).getBytes(), 0);
		assertEquals(b.isValid(), false);
	}
	
	
}
