package protocol;

import java.net.DatagramPacket;
import java.rmi.Remote;
import java.rmi.RemoteException;

import proj1.Chunk;

public interface SubProtocol {
	
	byte[] buildInvocation(String pFileId, int pChunkNo, int pReplicationDeg, Chunk pChunk);
	byte[] apply(DatagramPacket packet);
	byte[] buildReply(BaseProtocol protocol);
	
}
