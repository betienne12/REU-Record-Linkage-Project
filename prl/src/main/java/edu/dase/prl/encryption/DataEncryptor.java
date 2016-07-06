package edu.dase.prl.encryption;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Utility class that performs encryption/decryption operations using "AES"
 * algorithm.
 * 
 * Note: All methods throw an exception and return null rather catching the
 * exception. This class is used in .ecl scripts (executed on HPCC platform),
 * and it is easier to debug the scripts if the methods throw the exceptions.
 * 
 */
public class DataEncryptor {

	private static final String CIPHER_ALGORITHM = "AES";
	
	private Key key;
	private Cipher enCipher;
	private Cipher deCipher;
	
	private static Map<String, DataEncryptor> instance = new HashMap<String, DataEncryptor>();
	
	public DataEncryptor(String password) throws UnsupportedEncodingException, Exception {


		Field field = Class.forName("javax.crypto.JceSecurity").
				getDeclaredField("isRestricted");
		field.setAccessible(true);
		field.set(null, java.lang.Boolean.FALSE);
				
		byte[] keyBytes = new byte[32];
		byte[] passBytes = password.getBytes("UTF-8");
		int numBytes = Math.min(keyBytes.length, passBytes.length);
		System.arraycopy(passBytes, 0, keyBytes, 0, numBytes);
		key = new SecretKeySpec(keyBytes, CIPHER_ALGORITHM);
	}
	
	public static synchronized DataEncryptor getInstance(String password) throws Exception {
		
		if (instance.containsKey(password)) {
			return instance.get(password);
		}
		
		DataEncryptor newInstance = new DataEncryptor(password);
		instance.put(password, newInstance);
		
		return newInstance;
	}
	
	public static String encryptString(String textToEncrypt, String password)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException, DataEncryptorException, Exception {

		if (textToEncrypt == null || password == null) {
			return null;
		}
		
		DataEncryptor dataEncryptor = DataEncryptor.getInstance(password);
		
		return dataEncryptor.encrypt(textToEncrypt);
	}
	
	public static String[] encryptStringArray(String[] arrayToEncrypt, String password)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException, DataEncryptorException, Exception {

		if (arrayToEncrypt == null || arrayToEncrypt.length == 0 || password == null) {
			return null;
		}
		
		String[] encryptedArray = new String[arrayToEncrypt.length];
		
		for (int i = 0; i < arrayToEncrypt.length; i++) {
			encryptedArray[i] = encryptString(arrayToEncrypt[i], password);
		}
		
		return encryptedArray;
	}
	
	public static String decryptString(String textToDecrypt, String password)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException, DataEncryptorException, Exception {

		if (textToDecrypt == null || textToDecrypt.isEmpty() || password == null) {
			return null;
		}
		
		DataEncryptor dataEncryptor = DataEncryptor.getInstance(password);
		
		return dataEncryptor.decrypt(textToDecrypt);
	}
	
	public static String[] decryptedStringArray(String[] arrayToDecrypt, String password)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException, DataEncryptorException, Exception {

		if (arrayToDecrypt == null || arrayToDecrypt.length == 0 || password == null) {
			return null;
		}
		
		String[] decryptedArray = new String[arrayToDecrypt.length];
		
		for (int i = 0; i < arrayToDecrypt.length; i++) {
			decryptedArray[i] = decryptString(arrayToDecrypt[i], password);
		}
		
		return decryptedArray;
	}
//////
	protected String encrypt(String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, UnsupportedEncodingException, DataEncryptorException {

		plainText = new String(plainText.getBytes(), "UTF-8");
		
		if (enCipher == null) {
			enCipher = Cipher.getInstance(CIPHER_ALGORITHM);
			enCipher.init(Cipher.ENCRYPT_MODE, key);
		}
		
		byte[] encryptedBytes = null;
		try {
			encryptedBytes = enCipher.doFinal(plainText.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new DataEncryptorException("Couldn't encrypt the following string:\"" + plainText + "\"", e);
		}
		String encryptedValue = Base64.encodeBase64String(encryptedBytes);
		
		return encryptedValue;
	}
	
	protected String decrypt(String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, UnsupportedEncodingException, DataEncryptorException {

		if (deCipher == null) {
			deCipher = Cipher.getInstance(CIPHER_ALGORITHM);
			deCipher.init(Cipher.DECRYPT_MODE, key);
		}
		
		byte[] bytesToDecode = Base64.decodeBase64(cipherText);
		byte[] decryptedBytes = null;
		try {
			decryptedBytes = deCipher.doFinal(bytesToDecode);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new DataEncryptorException("Couldn't decrypt the following string:\"" + cipherText + "\"" + "\n" + e.toString(), e);
		}
		
		return new String(decryptedBytes, "UTF-8");
	}
	
}
