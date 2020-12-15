package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DataConsolidator {

    private Workbook wb;

    public DataConsolidator(Workbook wb) {
        Assert.notNull(wb, "workbook was null");
        this.wb = wb;
    }

    public XSSFWorkbook parse() throws WorkbookParseException {
        if (wb.getNumberOfSheets() == 0) {
            throw new WorkbookParseException("empty workbook (no sheets)");
        }

        ArrayList<MoveRecord> moves = new ArrayList<>();

        Sheet arams = wb.getSheetAt(0); // Will this always correspond to ARAMS?
        ARAMSParser aramsParser = new ARAMSParser(arams);
        moves.addAll(aramsParser.parse());

        Sheet wales = wb.getSheetAt(3);
        WalesParser walesParser = new WalesParser(wales);
        moves.addAll(walesParser.parse());

        // TODO: Deduplicate the MoveRecords

        // Create output sheet
        try {
            return createResultWorkbook(moves);
        } catch(ClassNotFoundException | IllegalAccessException e) {
            throw new WorkbookParseException("could not reflect on MoveRecord", e);
        }
    }

    private XSSFWorkbook createResultWorkbook(ArrayList<MoveRecord> moves) throws ClassNotFoundException, IllegalAccessException {
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sh = wb.createSheet("Results");
        Class cls = Class.forName("Group1.com.DataConsolidation.DataProcessing.MoveRecord");

        // Print the headings
        Field[] fieldList = cls.getDeclaredFields();
        Row headingsRow = sh.createRow(0);
        for (int i = 0; i < fieldList.length; i++) {
            // We should use more user-friendly field names in future
            headingsRow.createCell(i).setCellValue(fieldList[i].getName());
        }

        // For each entry, output its data
        int rowIndex = 1;
        for (MoveRecord m : moves) {
            Row r = sh.createRow(rowIndex);

            for (int colIndex = 0; colIndex < fieldList.length; colIndex++) {
                Object cellValue = fieldList[colIndex].get(m);
                r.createCell(colIndex).setCellValue((String)cellValue);
            }

            rowIndex += 1;
        }

        return wb;
    }
}
