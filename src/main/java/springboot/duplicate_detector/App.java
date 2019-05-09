package springboot.duplicate_detector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * deuplicate detector
 *
 */
@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = {"springboot.deuplicate_detector.controller"})
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
