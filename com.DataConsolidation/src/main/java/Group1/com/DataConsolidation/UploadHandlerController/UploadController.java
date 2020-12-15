package Group1.com.DataConsolidation.UploadHandlerController;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

@RestController
@CrossOrigin(value = "http://localhost:3000")
public class UploadController {
    private static String UPLOADED_FOLDER = "src/main/resources/UploadedFiles/";
    private static final Logger logger = Logger.getLogger(UploadController.class.getName());
    private static String MessageToShow = "";
    @PostMapping("/upload")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) throws Exception {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        byte[] buffer = new byte[inputStream.available()];
        //Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        inputStream.read(buffer);
        File targetFile = new File(UPLOADED_FOLDER + "targetFile.xlsx"); // All Files will be stored as xlsx ?
        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        }


        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String name = file.getName();
        String contentType = file.getContentType();
        long size = file.getSize();
        logger.info("inputStream: " + inputStream);
        logger.info("originalName: " + originalName);
        logger.info("name: " + name);
        logger.info("contentType: " + contentType);
        logger.info("size: " + size);
        MessageToShow = originalName + " size:" + size + " Content Type: "+ contentType;
        // Do processing with uploaded file data in Service layer


        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/resources/")
                .path(originalName)
                .toUriString();
        return new ResponseEntity<String>(fileDownloadUri, HttpStatus.OK);
    }
    @GetMapping("/upload")
    @ResponseBody
    public String showdata(){
        return MessageToShow;
    }


}
