package utils;

import java.io.File;

public class Helper {
	
	public static boolean isInteger(String pValue) {
		return pValue.matches("\\d+");
	}
	
	public static byte[] combine(byte[] pOne, byte[] pTwo) {
			
		byte[] combined = new byte[pOne.length + pTwo.length];
		
		for(int i = 0; i < combined.length; i++) {	
			combined[i] = i < pOne.length ? pOne[i] : pTwo[i - pOne.length];
		}
		
		return combined;
		
	}
	
	public static boolean createDir(String dirname) {
		return (new File(dirname).mkdirs());
	}
	
	public static void sleep(long millis) {
		
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.err.println("Helper: Error while sleeping.");
			e.printStackTrace();
		}
		
	}
}
