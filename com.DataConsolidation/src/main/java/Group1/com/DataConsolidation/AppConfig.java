package Group1.com.DataConsolidation;

import Group1.com.DataConsolidation.DataProcessing.Progress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    @Bean(name = "ProgressBean")
    @Scope("singleton")
    public Progress progressSingleton() {
        return new Progress();
    }

}
