package database;

public class BackupDatabase extends MyDatabase {

	public BackupDatabase(String name) {
		super(name);
		connect();
		createDatabase();
	}
	
	public void addFile(String fileId) {
		String query = "INSERT INTO File (FileId) VALUES ('" + fileId + "');";
		executeUpdateStmt(query);
	}
	
	public void addChunk(String fileId, int chunkNo) {
		// TODO
	}
	
	// Creates all necessary table for the backup app database
	private void createDatabase() {
		createFileTable();
		createChunkTable();
	}
		
	private void createFileTable() {
		String query = "CREATE TABLE IF NOT EXISTS File (FileId TEXT PRIMARY KEY NOT NULL);";
		executeUpdateStmt(query);
	}
	
	private void createChunkTable() {
		String query = "CREATE TABLE IF NOT EXISTS Chunk (ID INT PRIMARY KEY NOT NULL, ChunkNo INT, FileId TEXT, FOREIGN KEY (FileId) REFERENCES File(FileID));";
		executeUpdateStmt(query);
	}

}
