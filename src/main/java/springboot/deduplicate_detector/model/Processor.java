package springboot.deduplicate_detector.model;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.similarity.LevenshteinDistance;

public abstract class Processor<T> {
	List<T> list;
	List<String> columnsToCheck;
	static LevenshteinDistance ld;
	final float RATE = 1f;
	private String fileName;

	
	abstract public void init();
	public void setFile(String file) {
		this.fileName = file;
	}
	
	abstract float computeSimilarityScore(T a, T b, List<String> fieldsToCompare);
	
	public List<T> getRecords(){
		return Collections.unmodifiableList(list);
	}	
	abstract public List<CSVRecord> getDupSet(T c);
	
	abstract public DupResponse getResultSet();
	
	abstract float recordCompleteRate(CSVRecord record, int totalColumns);
	
}
