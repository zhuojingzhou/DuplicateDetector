Duplicate Detector

1. Project overview
	This is a simple duplicate detector that will find set of duplicate records based on the following columns.  
	"first_name",  
	"last_name",   
	"company",  
	"email",  
	"address1",  
	"address2",  
	"city",  
	"state_long",  
	"state",  
	"phone"   

	Please note id and zip are excluded because it doesn't make sense to be used in dup check.  
	Default file is normal.csv. To test the advanced.csv file, change the filename in MyController.java at line 47 (cp.setFile("adbanced.csv");)  
	Default threshold = 0.7f. However, if a record completeness is under 0.3, threshold will be 1.  

2. Steps to run the application:  
	1. cd ${project_directory}  
	2. mvn install  
	3. mvn spring-boot:run  
	3. curl http://localhost:8080/dups or open the url in a browser  

Things to improve:  
Pass threshold, columns to check and file from the HTTP Request. Or from a property file  
Complete the unit test  
Load data into a database if the file is large   
Make it a multithead program, concurrently computing the dups after the read action is done.   
