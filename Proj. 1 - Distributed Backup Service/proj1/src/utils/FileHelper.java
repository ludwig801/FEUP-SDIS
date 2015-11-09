package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;

public class FileHelper {
	
	public static boolean fileExists(String pPath) {
		File f = new File(pPath);
		return f != null ? f.exists() : false;
	}
	
	public static String getLastModified(String pPath, String dateFormat) {
		File file = new File(pPath);
		long time = file.lastModified();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(time);
	}
	
	public static String getOwnerName(String pPath) {
		try {
			File file = new File(pPath);
			UserPrincipal owner = null;
			owner = java.nio.file.Files.getOwner(file.toPath());
			return (owner.getName().split("\\\\")[1]);
		} catch (IOException e) {
			System.err.println("FileHelper: Invalid owner name.");
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static byte[] readFile(String path) {
		
		File file = new File(path);
		FileInputStream fis = null;
		
		if(file.exists()) {
			
			try {
				
				fis = new FileInputStream(file);
				
				byte[] buf = new byte[(int) file.length()];

				fis.read(buf);
				fis.close(); 
				
				return buf;
				
			} catch (FileNotFoundException e) {
				System.err.println("FileHelper: Error opening file.");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("FileHelper: Error reading file data.");
				e.printStackTrace();
			}
			
		} 	
		
		return null;
	}
	
	
	public static void writeToFile(byte[] buf, int length, String path) {
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			fos.write(buf, 0, length);
			fos.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could not create file to write.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not write to file.");
			e.printStackTrace();
		}
		
	}
	
	public static void deleteDirectory(File directory) {
		if(directory.exists()) {
			
			File[] files = directory.listFiles();
			
			if(files != null) {
				for(int i = 0; i < files.length; i++) {
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		directory.delete();
	}
	
	public static int countFiles(File directory) {
		if(directory.isDirectory()) {
			return 	directory.list().length;
		}
		
		System.err.println("FileHelper: Directory " + directory.getAbsolutePath() + " not found or is not a directory.");
		return -1;
	}

}
