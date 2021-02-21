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
        ParseThread parsethread = new ParseThread("parsethread");
        SSEThread SSEthread = new SSEThread("SSEthread");
        parsethread.start();
        SSEthread.start();

    }
}
