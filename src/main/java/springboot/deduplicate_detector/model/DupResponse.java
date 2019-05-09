package springboot.deduplicate_detector.model;

import java.util.Set;

import org.apache.commons.csv.CSVRecord;

public class DupResponse {
	private Set<Set<CSVRecord>> dupSets;
	private Set<CSVRecord> nonDupRecords;	
	
	public DupResponse() {
		
	}
	
	public void setDupSets(Set<Set<CSVRecord>> dupSets) {
		this.dupSets = dupSets;
	}
	
	public Set<Set<CSVRecord>> getDupSets(){
		return dupSets;
	}
	
	public void setNonDupRecords(Set<CSVRecord> nonDupRecords) {
		this.nonDupRecords = nonDupRecords;
	}
	
	public Set<CSVRecord> getNonDupRecords(){
		return nonDupRecords;
	}
}
