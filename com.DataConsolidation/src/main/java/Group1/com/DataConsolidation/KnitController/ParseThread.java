package Group1.com.DataConsolidation.KnitController;

import Group1.com.DataConsolidation.DataProcessing.DataConsolidator;
import Group1.com.DataConsolidation.DataProcessing.WorkbookParseException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.logging.Logger;

class ParseThread implements Runnable{
        private static String UPLOADED_FOLDER = "src/main/resources/UploadedFiles/";
        private static final Logger logger = Logger.getLogger(Group1.com.DataConsolidation.KnitController.ParseThread.class.getName());
        Thread thread;
        private String threadname;
        public ParseThread(String threadname) {
            this.threadname = threadname;
        }
        @Override
        public void run() {
            try (InputStream inStream = new FileInputStream(UPLOADED_FOLDER + "targetFile.xlsx")) {
                var outFile = new File("src/main/resources/ProcessedFiles/processed.xlsx");
                outFile.createNewFile();
                OutputStream outStream = new FileOutputStream(outFile);
                Workbook wbIn = WorkbookFactory.create(inStream);
                XSSFWorkbook wbOut = new DataConsolidator(wbIn).parse();
                wbOut.write(outStream);
                logger.info("Processing done");
            } catch (IOException | WorkbookParseException e) {
                e.printStackTrace();
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
