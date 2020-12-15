package Group1.com.DataConsolidation.KnitController;

import Group1.com.DataConsolidation.UploadHandlerController.UploadController;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KnitListener implements ApplicationListener<KnitEvent> {

    private static final Logger logger = Logger.getLogger(KnitListener.class.getName());
    @Override
    public void onApplicationEvent(KnitEvent event) {
        logger.info("event listened");
        //Calling duplication functions here. These functions will run when the knit button on the frontend is clicked (for now)

    }
}
