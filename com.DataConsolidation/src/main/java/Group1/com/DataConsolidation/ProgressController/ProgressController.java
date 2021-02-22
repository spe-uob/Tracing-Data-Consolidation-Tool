package Group1.com.DataConsolidation.ProgressController;

import Group1.com.DataConsolidation.DataProcessing.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
@RestController
public class ProgressController {
    final Progress parseProgress;

    @Autowired
    public ProgressController(Progress parseProgress) {
        this.parseProgress = parseProgress;
    }

    @CrossOrigin(value = "http://localhost:3000")
    @GetMapping(value = "/progress", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Float> getProgress(){

        return Flux.interval(Duration.ofSeconds(1)).map(it -> parseProgress.getProgress());
    }
}
