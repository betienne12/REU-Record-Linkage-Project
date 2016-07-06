package edu.dase.prl.stringComparsion;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class used to construct q-grams from words and construct words 
 * from q-grams. 
 * 
 * Note: All methods throw an exception and return null rather catching the
 * exception. This class is used in .ecl scripts (executed on HPCC platform),
 * and it is easier to debug the scripts if the methods throw the exceptions.
 */
public class QGram {
	
	public static String convertFromQGramArray(String[] qGrams) throws StringComparsionException {
		
		if (qGrams == null  || qGrams.length == 0) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : qGrams) {
			if (sb.length() == 0) {
				sb.append(s);
			} else {
				sb.delete(sb.length() - s.length() + 1, sb.length()).append(s);
			}
		}
		
		return sb.toString();
	}
	
	public static String[] convertToQgram(String plainText, int qNumber) throws StringComparsionException {
		
		if (plainText == null || plainText.isEmpty()) {
			return null;
		}
		if (qNumber <= 0) {
			throw new StringComparsionException(MessageFormat.format(
					StringComparsionException.Q_GRAM_LENGHT_ZERO_OR_LESS, qNumber));
		}
		
		// if qNumber is bigger then length of the string to convert, return the original string
		if(plainText.length() <= qNumber) {
			return new String[]{plainText};
		}
		
		List<String> qGrams = new LinkedList<String>();
		for (int i = qNumber; i < plainText.length() + 1 ;i++) {
			qGrams.add(plainText.substring(i - qNumber, i));
		}
		
		return qGrams.toArray(new String[qGrams.size()]);
	}
	
	public static boolean compareTwoQgrams(String[] q1, String[] q2) throws StringComparsionException {
		
		if (q1 == null || q1.length == 0) {
			throw new StringComparsionException(MessageFormat.format(
					StringComparsionException.NULL_OR_EMPTY_ARG, "q1"));
		}
		if (q2 == null || q2.length == 0) {
			throw new StringComparsionException(MessageFormat.format(
					StringComparsionException.NULL_OR_EMPTY_ARG, "q2"));
		}
		
		return Arrays.equals(q1, q2);
	}
	
	/*QGram() {
		
	}*/

}
