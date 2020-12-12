package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;

public class DataConsolidator {

    private Workbook wb;

    public DataConsolidator(XSSFWorkbook wb) throws IOException {
        Assert.notNull(wb, "workbook was null");
        this.wb = wb;
    }

    public String parse() throws SpreadsheetParseException {
        try {
            Sheet arams = wb.getSheetAt(0); // Will this always correspond to ARAMS?
            ARAMSParser aramsParser = new ARAMSParser(arams);
            ArrayList<MoveRecord> aramsMoves = aramsParser.parse();

            long numMoves = aramsMoves.size();
            if (numMoves == 0)
                throw new SpreadsheetParseException("empty spreadsheet");

            return String.format("Parsed %d movements", numMoves);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SpreadsheetParseException("empty spreadsheet", e);
        }
    }
}
