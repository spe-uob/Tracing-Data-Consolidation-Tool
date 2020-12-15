package Group1.com.DataConsolidation.ServeHandlerController;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@CrossOrigin("http://localhost:3000")
public class ServeController {
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
