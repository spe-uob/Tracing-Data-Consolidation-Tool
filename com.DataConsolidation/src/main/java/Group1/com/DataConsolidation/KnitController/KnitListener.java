package Group1.com.DataConsolidation.KnitController;

import Group1.com.DataConsolidation.DataProcessing.DataConsolidator;
import Group1.com.DataConsolidation.DataProcessing.WorkbookParseException;
import Group1.com.DataConsolidation.UploadHandlerController.UploadController;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.logging.Logger;

@Slf4j
@Service
public class KnitListener implements ApplicationListener<KnitEvent> {

    private static final Logger logger = Logger.getLogger(KnitListener.class.getName());
    private static String UPLOADED_FOLDER = "src/main/resources/UploadedFiles/";
    @Override
    public void onApplicationEvent(KnitEvent event) {
        logger.info("event listened");
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
        TestTestThread parsethread = new TestTestThread("parsethread");
        TestTestThread SSEthread = new TestTestThread("SSEthread");
        parsethread.start();
        SSEthread.start();

    }
}
