package ui;

public class ConfigurationUI {
	
	public static void printConfiguration(String mc_addr, int mc_port, String mdb_addr, int mdb_port, String mdr_addr, int mdr_port,
			String database, String backup_folder_name) {
		System.out.println("------------------------------");
		System.out.println("      MC: " + mc_addr + ":" + mc_port);
		System.out.println("     MDB: " + mdb_addr + ":" + mdb_port);
		System.out.println("     MDR: " + mdr_addr + ":" + mdr_port);
		if(database != null) System.out.println(" DB NAME: " + database);
		if(backup_folder_name != null) 	System.out.println("  BACKUP: " + backup_folder_name);
		System.out.println("------------------------------");
		System.out.println();
	}
	
	public static void printFileInfo(String header, String fileId) {
		System.out.println("---- FILE ----");
		System.out.println("Header: " + header);
		System.out.println("File id: " + fileId);
		System.out.println("--------------");
		System.out.println();
	}
	
	public static void printMessageInfo(int chunkNo, int headerSize, int bodySize, int msgSize) {
		System.out.println("---- MESSAGE ----");
		System.out.println("Chunk no: " + chunkNo);
		System.out.println("Header Size: " + headerSize + " bytes");
		System.out.println("Body Size: " + bodySize + " bytes");
		System.out.println("Message Size: " + msgSize + " bytes");
		System.out.println("-----------------");
		System.out.println();
	}

}
