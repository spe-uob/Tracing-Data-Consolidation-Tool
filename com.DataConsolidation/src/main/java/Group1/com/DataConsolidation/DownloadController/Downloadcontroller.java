package Group1.com.DataConsolidation.DownloadController;


import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


//Taking shortcuts. Need more work an refactoring
@RestController
@CrossOrigin("http://localhost:3000")
public class Downloadcontroller {
    @RequestMapping(value = "/Processed.xlsx", method = RequestMethod.GET, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @ResponseBody
    public byte[] getFile () throws IOException {
        InputStream inputStream = new FileInputStream("src/main/resources/ProcessedFiles/processed.xlsx");
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        return buffer;

    }

}