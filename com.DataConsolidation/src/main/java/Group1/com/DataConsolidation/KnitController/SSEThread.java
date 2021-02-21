package Group1.com.DataConsolidation.KnitController;

import java.util.logging.Logger;

class SSEThread implements Runnable{
    private static final Logger logger = Logger.getLogger(Group1.com.DataConsolidation.KnitController.SSEThread.class.getName());
    Thread thread;
    private String threadname;
    public SSEThread(String threadname) {
        this.threadname = threadname;
    }
    @Override
    public void run() {
        for(int i = 1; i <= 4; i++){
            logger.info(threadname + i);
        }
    }
    public void start() {
        if (thread  == null) {
            thread = new Thread(this, threadname);
            thread.start();
        }
        logger.info("Thread started");
    }
}
