package edu.dase.prl.encryption;

public class DataEncryptorException extends Exception {

	private static final long serialVersionUID = 3794273118332985631L;

	public DataEncryptorException() {
	}

	public DataEncryptorException(String message) {
		
		super(message);
	}

	public DataEncryptorException(Throwable cause) {
		
		super(cause);
	}

	public DataEncryptorException(String message, Throwable cause) {
		
		super(message, cause);
	}

	public DataEncryptorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
