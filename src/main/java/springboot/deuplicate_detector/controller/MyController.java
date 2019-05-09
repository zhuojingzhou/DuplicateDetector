package springboot.deuplicate_detector.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import com.google.gson.Gson;

import springboot.deduplicate_detector.model.CSVProcessor;
import springboot.deduplicate_detector.model.DupResponse;
import springboot.deduplicate_detector.model.Processor;

@Controller
@EnableAutoConfiguration
public class MyController {
	
    
    @GetMapping("/dups")
    public String dups(Model model) {            		
           
        //TODO: pass parameters from the request columns from UI
        List<String> columns = Arrays.asList("first_name","last_name", "company",
        		"email","address1","address2","city","state_long","state","phone");
        
        Gson gson = new Gson();
            
            //initialize csv processor
            Processor<CSVRecord> cp = new CSVProcessor(columns);
            cp.setFile("normal.csv");
            cp.init();
            
            DupResponse results = cp.getResultSet();     
            
            List<List<String>> nestedSet = new ArrayList<>();  
            
            for(Set<CSVRecord> sets: results.getDupSets()) {
            	List<String> each = new ArrayList<>();  
            	for(CSVRecord output:sets) {       
            		each.add(gson.toJson(output.toMap()));
            	}    
            	nestedSet.add(each);
            }
            
            List<String> nonDup = new ArrayList<>();
            for(CSVRecord record:results.getNonDupRecords()) {            	
            	nonDup.add(gson.toJson(record.toMap()));
            }
            
            model.addAttribute("resultDups", nestedSet);
            model.addAttribute("resultNonDups", nonDup);
            return "dups";         
        
        }
}
