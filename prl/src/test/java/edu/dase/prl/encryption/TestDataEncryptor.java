package edu.dase.prl.encryption;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class TestDataEncryptor {

	@Test
	public void testEncryptionAndDecryption() throws Exception {
		
		DataEncryptor dataEncryptor = DataEncryptor.getInstance("password");
		
		String original = new String("Linda");
		String encrypted = dataEncryptor.encrypt(original);
		String decrypted = dataEncryptor.decrypt(encrypted);
		
		AssertJUnit.assertEquals(original, decrypted);
	}
	
	@Test
	public void testEncryptionAndDecryptionArrays() throws Exception {
		
		String password = "34ndla!JNEk%@";
		
		String[] plainTextArray = {"text1", "text2", "text3"};
		
		String[] encryptedArray = DataEncryptor.encryptStringArray(plainTextArray, password);
		String[] decryptedArray = DataEncryptor.decryptedStringArray(encryptedArray, password);
		
		Assert.assertEquals(plainTextArray, decryptedArray);
	}
}
