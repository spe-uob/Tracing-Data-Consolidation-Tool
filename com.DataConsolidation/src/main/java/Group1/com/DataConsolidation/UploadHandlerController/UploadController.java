package Group1.com.DataConsolidation.UploadHandlerController;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.logging.Logger;

@RestController
@CrossOrigin(value = "http://localhost:3000")
public class UploadController {
    private static final Logger logger = Logger.getLogger(UploadController.class.getName());
    private static String MessageToShow = "";
    @PostMapping("/upload")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) throws Exception {
        if (file == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }
        InputStream inputStream = file.getInputStream();
        String originalName = file.getOriginalFilename();
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
        return new ResponseEntity<String>(originalName, HttpStatus.OK);
    }
    @GetMapping("/upload")
    @ResponseBody
    public String showdata(){
        return MessageToShow;
    }


}
