package Group1.com.DataConsolidation;

import Group1.com.DataConsolidation.DataProcessing.Progress;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication(scanBasePackages = "Group1.com.DataConsolidation")
public class Application { //Start our backend Application

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Scope("singleton")
	public Progress progressSingleton() {
		return new Progress();
	}
}
