package Group1.com.DataConsolidation;

import Group1.com.DataConsolidation.DataProcessing.ARAMSParser;
import Group1.com.DataConsolidation.DataProcessing.DataConsolidator;
import Group1.com.DataConsolidation.DataProcessing.WorkbookParseException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Disabled;
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
    void rejectsInvalidWorkbooks() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataConsolidator(null));

        Exception e = assertThrows(WorkbookParseException.class,
                () -> new DataConsolidator(new XSSFWorkbook()).parse());
        assertEquals("empty workbook (no sheets)", e.getMessage());

        // TODO: Test workbook with no data
        // TODO: Test workbook with some sheets missing
    }

    @Test
    void aramsParsing() {
        // Should reject workbook with no rows
        XSSFWorkbook wb1 = new XSSFWorkbook();
        Sheet sheet = wb1.createSheet("test");
        Exception e = assertThrows(WorkbookParseException.class,
                () -> new ARAMSParser(sheet).parse());
        assertEquals("ARAMS: empty spreadsheet (no headings)", e.getMessage());

        // Should reject every sheet in this file
        XSSFWorkbook wb2 = assertDoesNotThrow(() -> loadExcelFile("arams_invalid.xlsx"));
        for (Sheet sh : wb2) {
            String testName = sh.getSheetName();
            ARAMSParser p = new ARAMSParser(sh);
            assertThrows(WorkbookParseException.class,
                    () -> p.parse(),
                    "didn't reject invalid arams sheet: " + testName);
        }

        // Should accept every sheet in this file
        XSSFWorkbook wb3 = assertDoesNotThrow(() -> loadExcelFile("arams_valid.xlsx"));
        for (Sheet sh : wb3) {
            String testName = sh.getSheetName();
            ARAMSParser p = new ARAMSParser(sh);
            assertDoesNotThrow(
                    () -> p.parse(),
                    "didn't accept valid arams sheet: " + testName);
        }
    }

    @Test
    @Disabled
    void parsesTestData() {
        XSSFWorkbook wb = assertDoesNotThrow(() -> loadExcelFile("test_data.xlsx"));
        for (Sheet sh : wb) {
            System.out.println(sh.getSheetName());
        }
        DataConsolidator cs = assertDoesNotThrow(() -> new DataConsolidator(wb));
        String result = assertDoesNotThrow(() -> cs.parse());
        assertEquals("Parsed 12301 movements", result);
    }
}
