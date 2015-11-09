package database;

import java.sql.*;

public class MyDatabase {
	
	private String name;
	private Connection c;
	private Statement stmt;
	
	public MyDatabase(String name) {
		this.name = name;		
		this.c = null;
		this.stmt = null;
	}
	
	public void connect() {
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not find class: libraries not being loaded, maybe?");
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e) {
			System.err.println("Could not open database.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		//System.out.println("Opened database successfully.");
		
	}
	
	public void init() {
		
		String createFileTableQuery = "CREATE TABLE IF NOT EXISTS File "
				+ "(Id INTEGER PRIMARY KEY, FileId TEXT, Filename TEXT, "
				+ "NumberOfChunks INTEGER);";
		
		String createChunkTableQuery = "CREATE TABLE IF NOT EXISTS Chunk "
				+ "(Id INTEGER PRIMARY KEY, FileId TEXT, "
				+ "ChunkNo INTEGER, ObjRepDegree INTEGER, ActualRepDegree INTEGER, "
				+ "UNIQUE (FileId, ChunkNo));";
		
		executeUpdateStmt(createFileTableQuery);
		executeUpdateStmt(createChunkTableQuery);
	}
	
	public void addFile(String fileId, String filename, int numberOfChunks) {
		String query = "INSERT INTO File (FileId, Filename, NumberOfChunks) "
				+ "VALUES ('" + fileId + "', '" + filename + "', " + numberOfChunks + ");";
		executeUpdateStmt(query);
	}
	
	public void deleteFile(String fileId) {
		String query = "DELETE FROM File WHERE FileId = '" + fileId + "';";
		executeUpdateStmt(query);	
	}
	
	public void addChunk(String fileId, int chunkNo, int replicationDeg) {
		String query = "INSERT OR IGNORE INTO Chunk (FileId, ChunkNo, ObjRepDegree, ActualRepDegree) "
				+ "VALUES ( '" + fileId + "', " + chunkNo + ", " + replicationDeg + ", 0);";
		executeUpdateStmt(query);
	}
	
	public void deleteChunks(String fileId) {
		String query = "DELETE FROM Chunk WHERE FileId = '" + fileId + "';";
		executeUpdateStmt(query);	
	}
	
	public void incrementReplicationDegree(String fileId, int chunkNo) {
		String query = "UPDATE Chunk SET ActualRepDegree = (ActualRepDegree + 1) WHERE FileId = '" + fileId + "' AND ChunkNo = " + chunkNo + ";";
		executeUpdateStmt(query);
	}
	
	public String getFileId(String filename) {
		String query = "SELECT FileId FROM File WHERE Filename = '" + filename + "';";
		ResultSet rs = executeQueryStmt(query);
		
		if(rs != null) {
			try {
				String fileId = rs.getString("FileId");
				rs.close();
				return fileId;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return "";

	}
	
	public int getNumberOfChunks(String fileId) {
		
		String query = "SELECT NumberOfChunks FROM File WHERE FileId = '" + fileId + "';";
		ResultSet rs = executeQueryStmt(query);
		
		if(rs != null) {
			try {
				int n = rs.getInt("NumberOfChunks");
				rs.close();
				return n;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return -1;
	}
	
	public int getReplicationDegreeOfChunk(String fileId, int chunkNo) {
		
		String query = "SELECT ActualRepDegree FROM Chunk WHERE FileId = '" + fileId + "' AND ChunkNo = " + chunkNo + ";";
		ResultSet rs = executeQueryStmt(query);
		
		if(rs != null) {
			try {
				int n = rs.getInt("ActualRepDegree");
				rs.close();
				return n;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return -1;
		
	}
	
	public void createTable(String tableName, String[] columns) {
		
		// Build query
		String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
		for(int i = 0; i < columns.length; i++) 
			query += columns[i] + " ";	
		query += ");";
		
		executeUpdateStmt(query);

	}
	
	public void insertInto(String tableName, String[] columns, String[] values) {
		
		// Build query
		String query = "INSERT INTO " + tableName + " (";
		for(int i = 0; i < columns.length; i++) {
			query += columns[i] + " ";
		}
		
		query += ") VALUES ( ";
		
		for(int i = 0; i < values.length; i++) {
			query += values[i] + " ";
		}
		
		query += ");";
		
		executeUpdateStmt(query);
		
	}
	
	public void select(String tableName, String[] columns) {
		
		// Build query
		String query = "SELECT ";
		for(int i = 0; i < columns.length; i++) {
			query += columns[i] + " ";
		}
		query += " FROM " + tableName;
		
		executeQueryStmt(query);
		
	}
	
	public void executeUpdateStmt(String query) {
		
		try {
			stmt = c.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			System.err.println("Could not create statement.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		//System.out.println("Update statement successfully executed.");
		
	}
	
	public ResultSet executeQueryStmt(String query) {
		
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		
		} catch (SQLException e) {
			System.err.println("Could not create statement.");
			e.printStackTrace();
		}

		return null;
		
	}
	
	public void close() {
		
		try {
			c.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println("Could not close database connection.");
			e.printStackTrace();
		}
		
		System.out.println("Closed database successfully.");
	}

}
