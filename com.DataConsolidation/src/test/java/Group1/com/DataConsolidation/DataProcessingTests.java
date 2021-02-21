package Group1.com.DataConsolidation;

import Group1.com.DataConsolidation.DataProcessing.ARAMSParser;
import Group1.com.DataConsolidation.DataProcessing.SCOTEIDParser;
import Group1.com.DataConsolidation.DataProcessing.DataConsolidator;
import Group1.com.DataConsolidation.DataProcessing.WorkbookParseException;
import Group1.com.DataConsolidation.DataProcessing.WalesParser;
import Group1.com.DataConsolidation.DataProcessing.Parser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataProcessingTests{

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
    void walesParsing() {
        // Should reject workbook with no rows
        XSSFWorkbook wb1 = new XSSFWorkbook();
        Sheet sheet = wb1.createSheet("test");
        Exception e = assertThrows(WorkbookParseException.class,
                () -> new WalesParser(sheet).parse());
        assertEquals("Wales: empty spreadsheet (no headings)", e.getMessage());

        // Should reject every sheet in this file
        XSSFWorkbook wb2 = assertDoesNotThrow(() -> loadExcelFile("wales_invalid.xlsx"));
        for (Sheet sh : wb2) {
            String testName = sh.getSheetName();
            WalesParser p = new WalesParser(sh);

            Iterator<Row> rowIter = sh.rowIterator();
            String[] headingNames = {
                    "Ref",
                    "Count",
                    "Species",
                    "Lot",
                    "Date",
                    "From CPH",
                    "To CPH",
                    "Created By",
            };

            Map<String, Integer> headings = new HashMap<>();
            Row row = rowIter.next();

            //excptValue == 0: no exception thrown
            //excptValue == 1: duplicate heading
            //excptValue == 2: missing column
            var excptValue = 0;

            //check duplicate heading
            String valueName = "";
            for (Cell cell : row) {
                if (!cell.getCellType().equals(CellType.STRING)) {
                    continue;
                }
                for (String name : headingNames) {
                    String value = cell.getStringCellValue();
                    if (value.compareToIgnoreCase(name) == 0) {
                        Integer oldIndex = headings.putIfAbsent(value, cell.getColumnIndex());
                        if (!Objects.isNull(oldIndex)) {
                            valueName = value;
                            excptValue = 1;
                        }
                    }

                }
            }

            ArrayList<String> missing = new ArrayList<>();
            if(excptValue == 0){
                if (headings.size() != headingNames.length) {
                    // Find which headings were missing from the sheet
                    ArrayList<String> missingHeadings = new ArrayList<>();
                    for (String heading : headingNames) {
                        if (!headings.containsKey(heading)) {
                            missingHeadings.add(heading);
                            excptValue = 2;
                        }
                    }
                    missing = missingHeadings;
                }
            }

            String expect1 = String.format("Wales: duplicate heading '%s'",valueName);
            String expect2 = String.format("Wales: didn't find all headings - missing "+ String.join(", ", missing));

            e = assertThrows(WorkbookParseException.class,
                    () -> p.parse(),
                    "didn't reject invalid wales sheet: " + testName);
            System.out.println(e.getMessage());
            if(excptValue == 1)
                assertEquals(expect1, e.getMessage());
            else if(excptValue == 2)
                assertEquals(expect2, e.getMessage());
        }

        // Should accept every sheet in this file
        XSSFWorkbook wb3 = assertDoesNotThrow(() -> loadExcelFile("wales_valid.xlsx"));
        for (Sheet sh : wb3) {
            String testName = sh.getSheetName();
            WalesParser p = new WalesParser(sh);
            assertDoesNotThrow(
                    () -> p.parse(),
                    "didn't accept valid wales sheet: " + testName);
        }
    }

    @Test
    void aramsParsing(){
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

            Iterator<Row> rowIter = sh.rowIterator();
            String[] headingNames = {
                    "Movement ID",
                    "From Premises",
                    "From Activity",
                    "To Premises",
                    "To Activity",
                    "Departure Date",
                    "Arrival Date",
                    "Recorded Date",
                    "Status",
                    "Move Method",
                    "Move Direction",
                    "Species",
                    "Animal No",
                    "Herd Mark",
                    "Animal Count",
                    "Animal Description",
                    "Dept Country",
                    "Dest Country"
            };

            Map<String, Integer> headings = new HashMap<>();
            Row row = rowIter.next();

            //excptValue == 0: no exception thrown
            //excptValue == 1: duplicate heading
            //excptValue == 2: missing column
            var excptValue = 0;

            //check duplicate heading
            String valueName = "";
            for (Cell cell : row) {
                if (!cell.getCellType().equals(CellType.STRING)) {
                    continue;
                }
                for (String name : headingNames) {
                    String value = cell.getStringCellValue();
                    if (value.compareToIgnoreCase(name) == 0) {
                        Integer oldIndex = headings.putIfAbsent(value, cell.getColumnIndex());
                        if (!Objects.isNull(oldIndex)) {
                            valueName = value;
                            excptValue = 1;
                        }
                    }

                }
            }

            ArrayList<String> missing = new ArrayList<>();
            if(excptValue == 0){
                if (headings.size() != headingNames.length) {
                    // Find which headings were missing from the sheet
                    ArrayList<String> missingHeadings = new ArrayList<>();
                    for (String heading : headingNames) {
                        if (!headings.containsKey(heading)) {
                            missingHeadings.add(heading);
                            excptValue = 2;
                        }
                    }
                    missing = missingHeadings;
                }
            }

            String expect1 = String.format("ARAMS: duplicate heading '%s'",valueName);
            String expect2 = String.format("ARAMS: didn't find all headings - missing "+ String.join(", ", missing));
            e = assertThrows(WorkbookParseException.class,
                    () -> p.parse(),
                    "didn't reject invalid arams sheet: " + testName);
            System.out.println(e.getMessage());
            if(excptValue == 1)
                assertEquals(expect1, e.getMessage());
            else if(excptValue == 2)
                assertEquals(expect2, e.getMessage());
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
    void scoteidParsing() {
        // Should reject workbook with no rows
        XSSFWorkbook wb1 = new XSSFWorkbook();
        Sheet sheet = wb1.createSheet("test");
        Exception e = assertThrows(WorkbookParseException.class,
                () -> new SCOTEIDParser(sheet).parse());
        assertEquals("SCOT EID: empty spreadsheet (no headings)", e.getMessage());

        // Should reject every sheet in this file
        XSSFWorkbook wb2 = assertDoesNotThrow(() -> loadExcelFile("scoteid_invalid.xlsx"));
        for (Sheet sh : wb2) {
            String testName = sh.getSheetName();
            SCOTEIDParser p = new SCOTEIDParser(sh);

            Iterator<Row> rowIter = sh.rowIterator();
            String[] headingNames = {
                    "Unique_Ref",
                    "Sheep",
                    "Reads",
                    "%",
                    "Move",
                    "Lot Date",
                    "Lot",
                    "Depart. CPH",
                    "Read Location",
                    "Dest. CPH"
            };

            Map<String, Integer> headings = new HashMap<>();
            Row row = rowIter.next();

            //excptValue == 0: no exception thrown
            //excptValue == 1: duplicate heading
            //excptValue == 2: missing column
            var excptValue = 0;

            //check duplicate heading
            String valueName = "";
            for (Cell cell : row) {
                if (!cell.getCellType().equals(CellType.STRING)) {
                    continue;
                }
                for (String name : headingNames) {
                    String value = cell.getStringCellValue();
                    if (value.compareToIgnoreCase(name) == 0) {
                        Integer oldIndex = headings.putIfAbsent(value, cell.getColumnIndex());
                        if (!Objects.isNull(oldIndex)) {
                            valueName = value;
                            excptValue = 1;
                        }
                    }

                }
            }

            ArrayList<String> missing = new ArrayList<>();
            if(excptValue == 0){
                if (headings.size() != headingNames.length) {
                    // Find which headings were missing from the sheet
                    ArrayList<String> missingHeadings = new ArrayList<>();
                    for (String heading : headingNames) {
                        if (!headings.containsKey(heading)) {
                            missingHeadings.add(heading);
                            excptValue = 2;
                        }
                    }
                    missing = missingHeadings;
                }
            }

            String expect1 = String.format("SCOT EID: duplicate heading '%s'",valueName);
            String expect2 = String.format("SCOT EID: didn't find all headings - missing "+ String.join(", ", missing));

            e = assertThrows(WorkbookParseException.class,
                    () -> p.parse(),
                    "didn't reject invalid scot eid sheet: " + testName);
            System.out.println(e.getMessage());
            if(excptValue == 1)
                assertEquals(expect1, e.getMessage());
            else if(excptValue == 2)
                assertEquals(expect2, e.getMessage());
        }

        // Should accept every sheet in this file
        XSSFWorkbook wb3 = assertDoesNotThrow(() -> loadExcelFile("scoteid_valid.xlsx"));
        for (Sheet sh : wb3) {
            String testName = sh.getSheetName();
            SCOTEIDParser p = new SCOTEIDParser(sh);
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
        assertDoesNotThrow(() -> cs.parse());
        // TODO: Check reasonable output
    }
}