package Group1.com.DataConsolidation.KnitController;

import org.springframework.context.ApplicationEvent;

public class KnitEvent extends ApplicationEvent {
    public KnitEvent(Object source){
        super(source);
    }
}
