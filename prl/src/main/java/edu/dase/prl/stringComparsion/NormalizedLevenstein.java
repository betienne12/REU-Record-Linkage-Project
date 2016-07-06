package edu.dase.prl.stringComparsion;

import com.wcohen.ss.Levenstein;
import com.wcohen.ss.api.StringWrapper;

public class NormalizedLevenstein extends Levenstein {

	@Override
	public double score(StringWrapper s, StringWrapper t) {
		
		double score = super.score(s, t);
		double normalizedScore = 1 - (Math.abs(score) / Math.max(s.length(), t.length()));
		
		return normalizedScore;
	}

}
