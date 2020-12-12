package Group1.com.DataConsolidation;

import Group1.com.DataConsolidation.DataProcessing.ARAMSParser;
import Group1.com.DataConsolidation.DataProcessing.DataConsolidator;
import Group1.com.DataConsolidation.DataProcessing.SpreadsheetParseException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class DataProcessingTests {

    XSSFWorkbook loadExcelFile(String path) {
        InputStream data = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
        return assertDoesNotThrow(() -> new XSSFWorkbook(data), "could not open " + path);
    }

    @Test
    void rejectsEmptyWorkbooks() {
        // Null workbook
        assertThrows(IllegalArgumentException.class,
                () -> new DataConsolidator(null));

        // Empty workbook
        Exception e = assertThrows(SpreadsheetParseException.class,
                () -> new DataConsolidator(new XSSFWorkbook()).parse());
        assertEquals("empty spreadsheet", e.getMessage());

        // Workbook with no rows
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("test sheet A");
        e = assertThrows(SpreadsheetParseException.class,
                () -> new DataConsolidator(wb).parse());
        assertEquals("empty spreadsheet", e.getMessage());

        // Workbook with no cells in the row
        Row row = sheet.createRow(0);
        e = assertThrows(SpreadsheetParseException.class,
                () -> new DataConsolidator(wb).parse());
        assertEquals("empty spreadsheet", e.getMessage());
    }

    @Test
    void rejectsAramsInvalidFormat() {
        XSSFWorkbook wb = assertDoesNotThrow(() -> loadExcelFile("arams_invalid.xlsx"));
        for (Sheet sh : wb) {
            ARAMSParser p = new ARAMSParser(sh);
            assertThrows(SpreadsheetParseException.class, () -> p.parse());
        }
    }

    @Test
    void parsesTestData() {
        XSSFWorkbook wb = assertDoesNotThrow(() -> loadExcelFile("test_data.xlsx"));
        DataConsolidator cs = assertDoesNotThrow(() -> new DataConsolidator(wb));
        String result = assertDoesNotThrow(() -> cs.parse());
        assertEquals("Parsed 12301 movements", result);
    }
}
