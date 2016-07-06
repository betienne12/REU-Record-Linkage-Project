package edu.dase.prl.benchMark.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.dase.prl.encryption.DataEncryptor;
import edu.dase.prl.encryption.DataEncryptorException;

public class CSVFileWriter {

	private String[] header;
	private String csvFileName;
	
	private CSVPrinter csvPrinter;
	
	public CSVFileWriter(String[] header, String csvFileName) throws CSVFileException {
		
		if (csvFileName == null || csvFileName.isEmpty()) {
			throw new CSVFileException(MessageFormat.format(CSVFileException.NULL_OR_EMPTY_ARG, "csvFileName"));
		}
		
		this.csvFileName = csvFileName;
		if (header != null) {
			this.header = new String[header.length];
			System.arraycopy(header, 0, this.header, 0, header.length);
		}
	}
	
	public <K, V extends Number> void saveQGramFrequencies(Collection<Map.Entry<K, V>> qGramFrequencies, boolean includeTranslations) throws Exception {
		
		csvPrinter = new CSVPrinter(new FileWriter(csvFileName), CSVFormat.DEFAULT.withHeader(header).withAllowMissingColumnNames());
		
		for (Map.Entry<K, V> mapEntry : qGramFrequencies) {
			List<String> recordContent = new ArrayList<String>();
			recordContent.add(mapEntry.getKey().toString());
			if (includeTranslations) {
				recordContent.add(DataEncryptor.decryptString(mapEntry.getKey().toString(), "password"));
			}
			recordContent.add(mapEntry.getValue().toString());
			
			csvPrinter.printRecord(recordContent);
		}
		
		csvPrinter.flush();
		csvPrinter.close();
	}
		
}
