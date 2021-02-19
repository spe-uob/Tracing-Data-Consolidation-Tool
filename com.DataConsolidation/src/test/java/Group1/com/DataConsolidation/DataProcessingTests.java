package Group1.com.DataConsolidation;

import Group1.com.DataConsolidation.DataProcessing.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class DataProcessingTests {

    private CPH outbreakSource = new CPH("08/548/4000");

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
                () -> new DataConsolidator(new XSSFWorkbook()).parse(outbreakSource));
        assertEquals("empty workbook (no sheets)", e.getMessage());

        // TODO: Test workbook with no data
        // TODO: Test workbook with some sheets missing

    }

    @Test
    void walesParsing() {
        // Should reject workbook with no rows
        XSSFWorkbook wb1 = new XSSFWorkbook();
        Sheet sheet = wb1.createSheet("test");
        Exception e = assertThrows(WorkbookParseException.class,
                () -> new WalesParser(sheet, outbreakSource).parse());
        assertEquals("Wales: empty spreadsheet (no headings)", e.getMessage());

        // Should reject every sheet in this file
        XSSFWorkbook wb2 = assertDoesNotThrow(() -> loadExcelFile("wales_invalid.xlsx"));
        for (Sheet sh : wb2) {
            String testName = sh.getSheetName();
            WalesParser p = new WalesParser(sh, outbreakSource);
            e = assertThrows(WorkbookParseException.class,
                    () -> p.parse(),
                    "didn't reject invalid wales sheet: " + testName);
            System.out.println(e.getMessage());
        }

        // Should accept every sheet in this file
        XSSFWorkbook wb3 = assertDoesNotThrow(() -> loadExcelFile("wales_valid.xlsx"));
        for (Sheet sh : wb3) {
            String testName = sh.getSheetName();
            WalesParser p = new WalesParser(sh, outbreakSource);
            assertDoesNotThrow(
                    () -> p.parse(),
                    "didn't accept valid wales sheet: " + testName);
        }
    }

    @Test
    void aramsParsing() {
        // Should reject workbook with no rows
        XSSFWorkbook wb1 = new XSSFWorkbook();
        Sheet sheet = wb1.createSheet("test");
        Exception e = assertThrows(WorkbookParseException.class,
                () -> new ARAMSParser(sheet, outbreakSource).parse());
        assertEquals("ARAMS: empty spreadsheet (no headings)", e.getMessage());

        // Should reject every sheet in this file
        XSSFWorkbook wb2 = assertDoesNotThrow(() -> loadExcelFile("arams_invalid.xlsx"));
        for (Sheet sh : wb2) {
            String testName = sh.getSheetName();
            ARAMSParser p = new ARAMSParser(sh, outbreakSource);
            e = assertThrows(WorkbookParseException.class,
                    () -> p.parse(),
                    "didn't reject invalid arams sheet: " + testName);
            System.out.println(e.getMessage());
        }

        // Should accept every sheet in this file
        XSSFWorkbook wb3 = assertDoesNotThrow(() -> loadExcelFile("arams_valid.xlsx"));
        for (Sheet sh : wb3) {
            String testName = sh.getSheetName();
            ARAMSParser p = new ARAMSParser(sh, outbreakSource);
            assertDoesNotThrow(
                    () -> p.parse(),
                    "didn't accept valid arams sheet: " + testName);
        }
    }

    @Test
    void scoteidParsing() {
        // Should reject workbook with no rows
        XSSFWorkbook wb1 = new XSSFWorkbook();
        Sheet sheet = wb1.createSheet("test");
        Exception e = assertThrows(WorkbookParseException.class,
                () -> new SCOTEIDParser(sheet, outbreakSource).parse());
        assertEquals("SCOT EID: empty spreadsheet (no headings)", e.getMessage());

        // Should reject every sheet in this file
        XSSFWorkbook wb2 = assertDoesNotThrow(() -> loadExcelFile("scoteid_invalid.xlsx"));
        for (Sheet sh : wb2) {
            String testName = sh.getSheetName();
            SCOTEIDParser p = new SCOTEIDParser(sh, outbreakSource);
            e = assertThrows(WorkbookParseException.class,
                    () -> p.parse(),
                    "didn't reject invalid scot eid sheet: " + testName);
            System.out.println(e.getMessage());
        }

        // Should accept every sheet in this file
        XSSFWorkbook wb3 = assertDoesNotThrow(() -> loadExcelFile("scoteid_valid.xlsx"));
        for (Sheet sh : wb3) {
            String testName = sh.getSheetName();
            SCOTEIDParser p = new SCOTEIDParser(sh, outbreakSource);
            assertDoesNotThrow(
                    () -> p.parse(),
                    "didn't accept valid scot eid sheet: " + testName);
        }
    }

    @Test
    @Disabled
    void parsesTestData() {
        XSSFWorkbook wb = assertDoesNotThrow(() -> loadExcelFile("test_data_cleaned.xlsx"));
        for (Sheet sh : wb) {
            System.out.println(sh.getSheetName());
        }
        DataConsolidator cs = assertDoesNotThrow(() -> new DataConsolidator(wb));
        assertDoesNotThrow(() -> cs.parse(outbreakSource).write(new FileOutputStream("/tmp/parsed_data.xlsx")));
        // TODO: Check reasonable output
    }
}
