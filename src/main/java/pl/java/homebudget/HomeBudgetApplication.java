package pl.java.homebudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:app.properties")
public class HomeBudgetApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBudgetApplication.class, args);
    }

}
