package edu.dase.prl.stringComparsion;

public class StringComparsionException extends Exception {

	private static final long serialVersionUID = 9090954080476627522L;
	
	public static final String NULL_OR_EMPTY_ARG = "Arg: {0} was null or empty";
	public static final String Q_GRAM_LENGHT_ZERO_OR_LESS = "Q-gram cannot have length of {0}";

	public StringComparsionException() {
	}

	public StringComparsionException(String message) {
		
		super(message);
	}

	public StringComparsionException(Throwable cause) {
		
		super(cause);
	}

	public StringComparsionException(String message, Throwable cause) {
		
		super(message, cause);
	}

	public StringComparsionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
