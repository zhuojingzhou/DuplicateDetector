package springboot.deduplicate_detector.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class CSVProcessor extends Processor<CSVRecord>{
	
	static LevenshteinDistance ld;
	private List<CSVRecord> list;
	List<String> columnsToCheck;
	float threshold = 0.7f;
	private String fileName;
	
	public void setFile(String file) {
		this.fileName = file;
	}
	
	public CSVProcessor(List<String> columnsToCheck) {		
		list = new ArrayList<>();
		ld = new LevenshteinDistance();
		this.columnsToCheck = columnsToCheck;
	}
	
	//parse the file
	public void init() {
		Reader reader;
		try {
			Resource resource = new ClassPathResource(fileName);		
            File dbAsFile = resource.getFile();
                  
			reader = Files.newBufferedReader(Paths.get(dbAsFile.toURI()));
			CSVParser csvParser = new CSVParser(reader, CSVFormat.RFC4180.withHeader());
			
			for (CSVRecord csvRecord : csvParser) {
				list.add(csvRecord);
			}
			
			csvParser.close();
		} catch(FileNotFoundException e){
			  e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	/*
	 * Return a response contain each dup set and nondup set
	 */
	public DupResponse getResultSet(){
		DupResponse dp = new DupResponse();
		Set<Set<CSVRecord>> dupSets = new HashSet<>();
		Set<CSVRecord> nonDupRecords = new HashSet<>();
		
		nonDupRecords.addAll(list);
		
        for(CSVRecord currRecord:list) {
        	Set<CSVRecord> currSet = new HashSet<>(); 
        	List<CSVRecord> results = getDupSet(currRecord);
        	if(results.size() == 0) {
        		continue;
        	}else {
        		currSet.add(currRecord);
        	}
        	
        	for(CSVRecord dup: results) {
        		currSet.add(dup);
        	}
        	nonDupRecords.removeAll(currSet);
        	dupSets.add(currSet);              
        } 
        dp.setDupSets(dupSets);
        dp.setNonDupRecords(nonDupRecords);
        return dp;
	}
	
	@Override
	public List<CSVRecord> getDupSet(CSVRecord c){		
		List<CSVRecord> result = new ArrayList<>();
		for(CSVRecord r:list) {
			if(r.equals(c)) {
				continue;
			}			
			float currThreshold = threshold;
			//if completeness less than 1-threshold, currThreshold will be 1
			if(recordCompleteRate(c, columnsToCheck.size()) <= (RATE - threshold)) {
				currThreshold = RATE;
			}
			if(computeSimilarityScore(c, r, columnsToCheck) >= currThreshold ) {
				result.add(r);
			}					
		}		
		return result;
	}
	
	@Override
	/*
	 * compute the completeness of a record
	 */
	float recordCompleteRate(CSVRecord record, int totalColumns) {
		int count = 0;
		
		for(Map.Entry<String, String> pair:record.toMap().entrySet()) {
			if(columnsToCheck.contains(pair.getKey()) 
					&& pair.getValue() !=null 
					&& pair.getValue().length() > 0) {
				count++;
			}
		}
		//System.out.println((float)count / (float)totalColumns);
		return (float)count / (float)totalColumns;
	}
	
	
	/* Use LevenshteinDistance to compare distance for each column, 
	 * accumulate a final score for the whole row
	 * 
	 */
	@Override
	 float computeSimilarityScore(CSVRecord a, CSVRecord b, List<String> fields) {
		float score = 0l;
		for(String field:fields) {			
			String aFieldValue = a.get(field);
			String bFieldValue = b.get(field);
			if((aFieldValue == null || bFieldValue == null) ||
					(aFieldValue.length() == 0 || aFieldValue.length() == 0)) {
				score = score + (float)1 / (float)fields.size(); 
				continue;
			}
			//LevenshteinDistance
			int result = ld.apply(aFieldValue, bFieldValue);
			int base = Math.max(aFieldValue.length(), bFieldValue.length());
		
			float similarity = 1f - (float)result / (float)base;
			score = score + similarity / fields.size(); 			
			//System.out.println(field+" "+similarity+" values: "+a.get(field)+" "+b.get(field));
		}
		//System.out.println(score);
		return score;
	}
}
