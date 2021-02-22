package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class DataConsolidator {

    private Workbook wb;
    private Progress progress;

    public DataConsolidator(Workbook wb, Progress progress) {
        Assert.notNull(wb, "workbook was null");
        this.wb = wb;
        this.progress = progress;
    }

    public XSSFWorkbook parse() throws WorkbookParseException {
        if (wb.getNumberOfSheets() == 0) {
            throw new WorkbookParseException("empty workbook (no sheets)");
        }

        ArrayList<MoveRecord> moves = new ArrayList<>();
        progress.reset();

        Sheet arams = wb.getSheetAt(0); // Will this always correspond to ARAMS?
        ARAMSParser aramsParser = new ARAMSParser(arams, progress);
        moves.addAll(aramsParser.parse());

        Sheet scotlandFrom = wb.getSheetAt(1);
        SCOTEIDParser scoteidParserFrom = new SCOTEIDParser(scotlandFrom, progress);
        moves.addAll(scoteidParserFrom.parse());

        Sheet scotlandTo = wb.getSheetAt(2);
        SCOTEIDParser scoteidParserTo = new SCOTEIDParser(scotlandTo, progress);
        moves.addAll(scoteidParserTo.parse());

        Sheet wales = wb.getSheetAt(3);
        WalesParser walesParser = new WalesParser(wales, progress);
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

            int numEmpty = 0;
            for (int colIndex = 0; colIndex < fieldList.length; colIndex++) {
                String cellValue = (String)fieldList[colIndex].get(m);
                r.createCell(colIndex).setCellValue(cellValue);

                if (Objects.isNull(cellValue) || cellValue == "" || cellValue == " ") {
                    numEmpty += 1;
                }
            }

            // The whole row was empty, just ignore it (delete the row)
            if (numEmpty == fieldList.length) {
                sh.removeRow(r);
            } else {
                rowIndex += 1;
            }
        }

        return wb;
    }
}
