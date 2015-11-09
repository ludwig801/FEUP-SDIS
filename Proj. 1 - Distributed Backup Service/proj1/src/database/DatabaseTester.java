package database;

import proj1.MyFile;

/**
 * 
 * @author KevinAmorim
 *
 * Pure database tester. 
 * 
 * This is a runnable class which only purpose it's to create and handle a db, for testing.
 * 
 * 
 */

public class DatabaseTester {
	
	public static void main(String[] args) {
		
//		if(!(args.length == 1 || args.length >= 3)) {
//			System.out.println("Usage: java DatabaseTester <database_name> <query_type> <table_name> <values>*");
//			System.exit(0);
//		}
//		
//		String databaseName = args[0];
//		String queryType = args[1];
//		String tableName = args[2];
//		
//		MyDatabase db = new MyDatabase(databaseName);
//		db.connect();
//		
//		if(args.length >= 3) {
//			if(queryType.equals("select")) {
//				String[] values = new String[args.length - 3];
//				for(int i = 3; i < args.length; i++) values[i-3] = args[i];
//				
//				db.select(tableName, values);
//				
//			} else if(queryType.equals("create")) {
//				String[] values = new String[args.length - 3];
//				for(int i = 3; i < args.length; i++) values[i-3] = args[i];
//				
//				db.createTable(tableName, values);
//			} else if(queryType.equals("insert")) {
//				String[] columns = { args[3] };
//				String[] values = { args[4] };
//				
//				db.insertInto(tableName, columns, values);
//			}
//		}
//		
//		db.close();
		
		BackupDatabase db = new BackupDatabase("test");
		MyFile file = new MyFile("log.txt", 0);
		db.addFile(file.getFileId());
		db.close();
		
	}

}
