package unitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import utils.FileHelper;

public class FileHelperUnitTests {

	@Test
	public void testCountFiles() {
		new File("test").mkdir();
		try {
			FileOutputStream fos = new FileOutputStream("test/test_file.txt");
			fos.close();
			assertEquals(FileHelper.countFiles(new File("test")), 1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
