package proj1;

import java.net.DatagramPacket;

import configurations.ReadConfig;
import database.MyDatabase;
import protocol.*;
import utils.FileHelper;
import utils.Helper;

class Client  {

	public static void main(String[] args) {

		if(args.length < 2) {
			printUsage();
			return;
		}
		
		String db_name = ReadConfig.getDbName();
		MyDatabase db = new MyDatabase(db_name);
		db.connect();
		db.init();

		if(args[0].toLowerCase().equals("backup")) {
			backup(args, db);
		} else if(args[0].toLowerCase().equals("restore")) {
			restore(args, db);
		} else if(args[0].toLowerCase().equals("delete")) {
			delete(args, db);
		}
		
		System.out.println("Work finished.");

	}


	private static void printUsage() {
		System.out.println("usage: java Client backup <file_path> <replication_deg>");
		System.out.println("usage: java Client restore <filename>");
		System.out.println("usage: java Client delete <file_path>");
		System.out.println("usage: java Client space <amount_of_space>");
	}

	private static void backup(String[] args, MyDatabase db) {
		if(args.length < 3) {
			printUsage();
			return;
		} 
		
		String mc_address = ReadConfig.getMCAddress();
		int mc_port = ReadConfig.getMCPort();
		String mdb_address = ReadConfig.getMDBAddress();
		int mdb_port = ReadConfig.getMDBPort();
		int replicationDeg = Integer.parseInt(args[2]);
		MyChannel mc_channel = new MyChannel("MC", mc_address, mc_port);
		MyChannel mdb_channel = new MyChannel("MDB", mdb_address, mdb_port);
		MyFile file = new MyFile(args[1]);
		
		Backup b = new Backup();
		if(b.backupFile(mc_channel, mdb_channel, file, db, replicationDeg)) {
			System.out.println("Backup completed successfully.");
		} else {
			System.out.println("Backup not complete. Some data may be lost.");
		}
		
	}
	
	public static void restore(String[] args, MyDatabase db) {
		
		if(args.length < 2) {
			printUsage();
			return;
		}
		
		String mc_address = ReadConfig.getMCAddress();
		int mc_port = ReadConfig.getMCPort();
		MyChannel mc_channel = new MyChannel("MC", mc_address, mc_port);
		
		String mdr_address = ReadConfig.getMDRAddress();
		int mdr_port = ReadConfig.getMDRPort();
		MyChannel mdr_channel = new MyChannel("MDR", mdr_address, mdr_port);
				
		String fileId = db.getFileId(args[1]);
		System.out.println("FileID: " + fileId);
		int numberOfChunks = db.getNumberOfChunks(fileId);
		System.out.println("Number of Chunks: " + numberOfChunks);
		
		Restore restore = new Restore();
		if(restore.restore(mc_channel, mdr_channel, db, fileId, numberOfChunks)) {
			System.out.println("Restore completed successfully.");
		} else {
			System.out.println("Restore not completed. Some data was not recovered.");
		}	
	}

	public static void delete(String[] args, MyDatabase db) {
		
		if(args.length < 2) {
			printUsage();
			return;
		}
		
		String mc_address = ReadConfig.getMCAddress();
		int mc_port = ReadConfig.getMCPort();
		MyChannel mc_channel = new MyChannel("MC", mc_address, mc_port);
		
		String fileId = db.getFileId(args[1]);
		System.out.println("FileID: " + fileId);
		
		Delete delete = new Delete();
		if(delete.delete(mc_channel, db, fileId)) {
			System.out.println("File deleted successfully.");
		} else {
			System.out.println("Could not delete file.");
		}
		
		
		
	}
}
