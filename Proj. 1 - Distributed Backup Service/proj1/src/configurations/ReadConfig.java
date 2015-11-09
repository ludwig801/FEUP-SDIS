package configurations;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfig {
	
	
	public static String getMCAddress() {
		Properties prop = openConfigFile();
		
		String addrStr = prop.getProperty("mc_addr");
		if(addrStr == null) System.err.println("Property 'mc_addr' not found.");
		
		return addrStr;
	}
	
	public static String getMDBAddress() {
		Properties prop = openConfigFile();
		
		String addrStr = prop.getProperty("mdb_addr");
		if(addrStr == null) System.err.println("Property 'mdb_addr' not found.");
		
		return addrStr;
	}
	
	public static String getMDRAddress() {
		Properties prop = openConfigFile();
		
		String addrStr = prop.getProperty("mdr_addr");
		if(addrStr == null) System.err.println("Property 'mdr_addr' not found.");
		
		return addrStr;
	}

	public static int getMCPort() {
		Properties prop = openConfigFile();
		
		String portStr = prop.getProperty("mc_port");

		if(portStr == null) {
			System.err.println("Property 'mc_port' not found.");
		} else if(!utils.Helper.isInteger(portStr)) {
			System.err.println("Property 'mc_port' invalid.");
		} else {
			return Integer.parseInt(prop.getProperty("mc_port"));
		}
		
		return -1;

	}
	
	public static int getMDBPort() {
		Properties prop = openConfigFile();
		
		String portStr = prop.getProperty("mdb_port");

		if(portStr == null) {
			System.err.println("Property 'mdb_port' not found.");
		} else if(!utils.Helper.isInteger(portStr)) {
			System.err.println("Property 'mdb_port' invalid.");
		} else {
			return Integer.parseInt(prop.getProperty("mdb_port"));
		}
		
		return -1;

	}
	
	public static int getMDRPort() {
		Properties prop = openConfigFile();
		
		String portStr = prop.getProperty("mdr_port");

		if(portStr == null) {
			System.err.println("Property 'mdr_port' not found.");
		} else if(!utils.Helper.isInteger(portStr)) {
			System.err.println("Property 'mdr_port' invalid.");
		} else {
			return Integer.parseInt(prop.getProperty("mdr_port"));
		}
		
		return -1;

	}

	public static int getPacketSize() {
		Properties prop = openConfigFile();
		
		String packetSizeStr = prop.getProperty("packet_size");
		
		if(packetSizeStr == null) {
			System.err.println("Property 'packet_size' not found.");
		} else if(!utils.Helper.isInteger(packetSizeStr)) {
			System.err.println("Property 'packet_size' invalid.");
		} else {
			return Integer.parseInt(prop.getProperty("packet_size"));
		}
		
		return -1;
	}
	
	public static String getDbName() {
		Properties prop = openConfigFile();
		
		String dbNameStr = prop.getProperty("db_name");
		if(dbNameStr == null) System.err.println("Property 'db_name' not found.");
		
		return dbNameStr;
	}
	
	public static String getBackupFolderName() {
		Properties prop = openConfigFile();
		
		String backupFolderName = prop.getProperty("backup_folder_name");
		if(backupFolderName == null) System.err.println("Property 'backup_folder_name' not found.");
		
		return backupFolderName;
	}
	
	public static String getRestoreFolderName() {
		Properties prop = openConfigFile();
		
		String restoreFolderName = prop.getProperty("restore_folder_name");
		if(restoreFolderName == null) System.err.println("Property 'restore_folder_name' not found.");
		
		return restoreFolderName;
	}
	
	private static Properties openConfigFile() {
		
		String configName = "config.properties";
		Properties prop = new Properties();
		
		InputStream inputStream = ReadConfig.class.getClassLoader().getResourceAsStream(configName);
		
		if(inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Property file: " + configName + " not found.");
		}
		
		return prop;
	}

}
