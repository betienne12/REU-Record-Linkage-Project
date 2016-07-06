package edu.dase.prl.benchMark.utils;

public class CSVFileException extends Exception {

	private static final long serialVersionUID = -3941618984438893753L;
	
	protected static final String NULL_OR_EMPTY_ARG = "Arg: {0} was null or empty";

	public CSVFileException() {
		
	}

	public CSVFileException(String message) {
		
		super(message);
	}

	public CSVFileException(Throwable cause) {
		
		super(cause);
	}

	public CSVFileException(String message, Throwable cause) {
		
		super(message, cause);
	}

	public CSVFileException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
