package edu.dase.prl.benchMark.records;

public abstract class Record {

	protected String id;
	protected boolean isEncrypted;
	
	public Record(String id, boolean isEncrypted) {
		
		this.id = id;
		this.isEncrypted = isEncrypted;
	}
	
	public Record() {
		
	}
	
	
	@Override
	public String toString() {
		
		return "Record details: " + 
				"id: '" + id;
	}

	public String getId() {
		
		return id;
	}

	public void setId(String id) {
		
		this.id = id;
	}

	public boolean isEncrypted() {
		
		return isEncrypted;
	}

	public void setEncrypted(boolean isEncrypted) {
		
		this.isEncrypted = isEncrypted;
	}

}
