package edu.dase.prl.benchMark.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import edu.dase.prl.benchMark.records.SyntheticRecord;

public class CSVFileReader {

	private String[] header;
	private String csvFileName;
	private CSVParser csvFileParser;
	
	public CSVFileReader(String[] header, String csvFileName) throws CSVFileException {
		
		if (csvFileName == null || csvFileName.isEmpty()) {
			throw new CSVFileException(MessageFormat.format(CSVFileException.NULL_OR_EMPTY_ARG, "csvFileName"));
		}
		
		this.csvFileName = csvFileName;
		if (header != null) {
			this.header = new String[header.length];
			System.arraycopy(header, 0, this.header, 0, header.length);
		}
		
	}
	
	public List<String> loadListOfNamesForFrequencyAttack() throws IOException {
		
		File f = new File(csvFileName);
		URL fileURL = f.toURI().toURL();
		
		csvFileParser = CSVParser.parse(fileURL,  StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader(header).withSkipHeaderRecord());
	
		List<CSVRecord> records = csvFileParser.getRecords();
		List<String> namesList = new ArrayList<String>();
		
		for (CSVRecord record : records) {
			namesList.add(record.get(0));
		}
		
		return namesList;
	}
	
	public List<SyntheticRecord> loadEncryptedSyntheticRecords() throws IOException {
		
		File f = new File(csvFileName);
		URL fileURL = f.toURI().toURL();
		
		csvFileParser = CSVParser.parse(fileURL, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader(SyntheticRecord.RECORD_HEADER_ENCRYPTED).withAllowMissingColumnNames().withSkipHeaderRecord());
		
		List<CSVRecord> records = csvFileParser.getRecords();
		List<SyntheticRecord> synthetricRecords = new ArrayList<SyntheticRecord>();
		
		for (CSVRecord record : records) {
			String id = record.get(0);
			String firstName = record.get(1);
			String lastName = record.get(2);
			String ccNumber = record.get(3);
			String firstNameEncrypted = record.get(4);
			String lastNameEncrypted = record.get(5);
			String firstNameSoundex = record.get(6);
			String lastNameSoundex = record.get(7);
			String firstNameSoundexEncrypted = record.get(8);
			String lastNameSoundexEncrypted = record.get(9);
			String qNumber = record.get(10);
			if (!record.get(11).equals("QGramFN")) {
				System.err.println("Error while loading the synthetic record");
				System.exit(-31343);
			}
			int i = 12;
			List<String> fnGrams = new ArrayList<String>();
			while (!record.get(i).equals("QGramLN")) {
				fnGrams.add(record.get(i));
				i++;
			}
			i++;
			List<String> lnGrams = new ArrayList<String>();
			while(i < record.size()) {
				lnGrams.add(record.get(i));
				i++;
			}
			
			SyntheticRecord syntheticRecord = new SyntheticRecord(id, firstName, lastName, ccNumber, 
					firstNameEncrypted, lastNameEncrypted, firstNameSoundex, lastNameSoundex, 
					firstNameSoundexEncrypted, lastNameSoundexEncrypted, qNumber,fnGrams, lnGrams);
			synthetricRecords.add(syntheticRecord);
		}
		
		return synthetricRecords;
	}
	
	public List<SyntheticRecord> loadUnencryptedSyntheticRecords() throws IOException {
		
		File f = new File(csvFileName);
		URL fileURL = f.toURI().toURL();
		
		csvFileParser = CSVParser.parse(fileURL, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader(SyntheticRecord.RECORD_HEADER_ENCRYPTED).withAllowMissingColumnNames().withSkipHeaderRecord());
		
		List<CSVRecord> records = csvFileParser.getRecords();
		List<SyntheticRecord> synthetricRecords = new ArrayList<SyntheticRecord>();
		
		for (CSVRecord record : records) {
			String id = record.get(0);
			String firstName = record.get(1);
			String lastName = record.get(2);
			String ccNumber = record.get(3);
			
			SyntheticRecord syntheticRecord = new SyntheticRecord(id, firstName, lastName, ccNumber);
			synthetricRecords.add(syntheticRecord);
		}
		
		return synthetricRecords;
	}
	
}
