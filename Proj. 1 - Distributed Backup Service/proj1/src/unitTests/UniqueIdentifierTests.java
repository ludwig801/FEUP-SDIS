package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import proj1.UniqueIdentifier;

// Hardcoded SHA-256 hash string generated with: http://www.xorbin.com/tools/sha256-hash-calculator

public class UniqueIdentifierTests {
	
	@Test
	public void createUniqueIdentifier() {
		UniqueIdentifier ident = new UniqueIdentifier("feupsdis");
		assertEquals(ident.getIdentifier(), "21a4f275965896ac2e0a01e429a63e75a770852dcfa18f51548ed7056c02692e");
	}
	
	@Test public void changeUniqueIdentifierText() {
		UniqueIdentifier ident = new UniqueIdentifier("feupsdis");
		ident.changeText("feupsdis2015");
		assertEquals(ident.getIdentifier(), "a4bde2bc80e4f553fa69fffcbf191063bffc2a40fd10a394cab8fb0f5b402d64");
	}

}
