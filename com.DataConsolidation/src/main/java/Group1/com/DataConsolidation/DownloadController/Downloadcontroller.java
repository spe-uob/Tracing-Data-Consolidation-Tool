package Group1.com.DataConsolidation.DownloadController;


import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;


//Taking shortcuts. Need more work an refactoring
@RestController
@CrossOrigin("http://localhost:3000")
public class Downloadcontroller {
    @RequestMapping(value = "/serve", method = RequestMethod.GET, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @ResponseBody
    public byte[] getFile () throws IOException {
        var ServeFile = new ClassPathResource("ProcessedFiles/processed.xlsx");
        InputStream inputStream = ServeFile.getInputStream();
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        return buffer;

    }

}