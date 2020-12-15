package Group1.com.DataConsolidation.KnitController;


import Group1.com.DataConsolidation.DataProcessing.DataConsolidator;
import Group1.com.DataConsolidation.DataProcessing.WorkbookParseException;
import Group1.com.DataConsolidation.UploadHandlerController.UploadController;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.logging.Logger;

@RestController
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
public class Knitcontroller {
    private static final Logger logger = Logger.getLogger(Knitcontroller.class.getName());
    private final ApplicationEventPublisher applicationEventPublisher;

    private static String UPLOADED_FOLDER = "src/main/resources/UploadedFiles/";

    @Autowired
    public Knitcontroller(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }
    @PostMapping("/knit")
    public String knit(@RequestParam("action") String action){
        if(action == null){
            throw new RuntimeException("You must select the a file for knitting");
        }


        logger.info("Knit received here");
        KnitEvent knitevent = new KnitEvent(this);
        applicationEventPublisher.publishEvent(knitevent);

        // Read excel file from X, parse, write excel file to Y
        try (InputStream inStream = new FileInputStream(UPLOADED_FOLDER + "targetFile.xlsx")) {
            var outFile = new File("src/main/resources/ProcessedFiles/processed.xlsx");
            outFile.createNewFile();
            OutputStream outStream = new FileOutputStream(outFile);
            Workbook wbIn = WorkbookFactory.create(inStream);
            XSSFWorkbook wbOut = new DataConsolidator(wbIn).parse();
            wbOut.write(outStream);
            log.info("Processing done");
        } catch (IOException | WorkbookParseException e) {
            e.printStackTrace();
        }

        return "knit received";
    }

}
