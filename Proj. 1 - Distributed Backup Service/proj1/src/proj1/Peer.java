package proj1;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ui.ConfigurationUI;
import utils.Constants;
import configurations.ReadConfig;

public class Peer {
	
	private static String mc_addr;
	private static int mc_port;
	private static String mdb_addr;
	private static int mdb_port;
	private static String mdr_addr;
	private static int mdr_port;
	private static String db_name;
	private static Server server;
	private static String backup_folder_name;
	
	public static void main(String[] args) {
		
	
		if(!readConfigs()) {
			System.out.println("Configurations invalid or incomplete. Please check config.properties file.");
			return;
		}
		
		String localAddress = "NOT_FOUND";
		try {
			localAddress = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			System.err.println("Error getting local host IP. Continuing anyway...");
			e.printStackTrace();
		}
		
		System.out.println(localAddress + ": Peer launched");

		ConfigurationUI.printConfiguration(mc_addr, mc_port, mdb_addr, mdb_port, mdr_addr, mdr_port, db_name, backup_folder_name);
		
		server = new Server(mc_addr, mc_port, mdb_addr, mdb_port, mdr_addr, mdr_port, db_name);
		Constants.BACKUP_FOLDER_NAME = backup_folder_name;
		
		Thread serverThread = new Thread(server);
		serverThread.start();
		
	}
	
	public static void closePeer() {	
		server.close();
	}
	
	private static boolean readConfigs() {
		mc_addr = ReadConfig.getMCAddress();
		mc_port = ReadConfig.getMCPort();
		mdb_addr = ReadConfig.getMDBAddress();
		mdb_port = ReadConfig.getMDBPort();
		mdr_addr = ReadConfig.getMDRAddress();
		mdr_port = ReadConfig.getMDRPort();
		db_name = ReadConfig.getDbName();
		backup_folder_name = ReadConfig.getBackupFolderName();
		
		return (mc_addr != null && mc_port != -1 && mdb_addr != null 
				&& mdb_port != -1 && mdr_addr != null && mdr_port != -1 && db_name != null && backup_folder_name != null);
	}
	
}
