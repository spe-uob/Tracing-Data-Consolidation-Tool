package Group1.com.DataConsolidation.ProgressController;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
@RestController
public class ProgressController {
    @CrossOrigin(value = "http://localhost:3000")
    @GetMapping(value = "/progress", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getProgress(){
        return Flux.interval(Duration.ofSeconds(1)).map(it -> new Integer(5));
    }
}
