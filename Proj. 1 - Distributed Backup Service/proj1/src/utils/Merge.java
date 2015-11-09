package utils;

import java.io.File;

public class Merge {
	
	public static void main(String[] args) {
		
		String path = args[0];
		System.out.println("Merging files in: " + path + "...");
		
		File folder = new File(path);
		File[] files = folder.listFiles();
		
		byte[] buf = new byte[0];
		
		for(File file : files) {
			if(file.isFile()) {
				byte[] tmp = FileHelper.readFile(file.getPath());
				buf = Helper.combine(buf, tmp);
			}
		}
		
		FileHelper.writeToFile(buf, buf.length, path + "\\merged");
		System.out.println("Length: " + buf.length);
	}

}
