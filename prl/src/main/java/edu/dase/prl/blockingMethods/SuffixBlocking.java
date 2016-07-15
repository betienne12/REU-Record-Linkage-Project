package edu.dase.prl.blockingMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import edu.dase.prl.benchMark.records.BetterRecord;
import edu.dase.prl.encryption.DataEncryptor;

public class SuffixBlocking {
public static HashMap<String, ArrayList<BetterRecord>> suffixBlocking(String file, int suffixLength) throws Exception{
		
		
		ArrayList<BetterRecord> records = new ArrayList<>();
		Scanner in = new Scanner(new File(file));
		while (in.hasNext()) {
			records.add(new BetterRecord(in.nextLine()));
		}
		in.close();

				HashMap<String, ArrayList<BetterRecord>> blockingTable = new HashMap<>();
				for (BetterRecord r: records) {
					String query = DataEncryptor.encryptString(r.lastName,"password");
					String suffix=query.substring(query.length() -suffixLength);
					if (blockingTable.containsKey(suffix)) {
						blockingTable.get(suffix).add(r);
					} else {
						ArrayList<BetterRecord> temp = new ArrayList<>();
						temp.add(r);
						blockingTable.put(suffix, temp);
					}
				}
			

				return blockingTable;
		}

}