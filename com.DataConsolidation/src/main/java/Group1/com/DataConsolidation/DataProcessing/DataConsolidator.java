package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;

import java.util.ArrayList;

public class DataConsolidator {

    private Workbook wb;

    public DataConsolidator(XSSFWorkbook wb) {
        Assert.notNull(wb, "workbook was null");
        this.wb = wb;
    }

    public String parse() throws WorkbookParseException {
        if (wb.getNumberOfSheets() == 0) {
            throw new WorkbookParseException("empty workbook (no sheets)");
        }

        // For now, only ARAMS parsing is implemented
        Sheet arams = wb.getSheetAt(0); // Will this always correspond to ARAMS?
        ARAMSParser aramsParser = new ARAMSParser(arams);
        ArrayList<MoveRecord> aramsMoves = aramsParser.parse();

        long numMoves = aramsMoves.size();
        return String.format("Parsed %d movements", numMoves);
    }
}
