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

        Sheet arams = wb.getSheetAt(0); // Will this always correspond to ARAMS?
        ARAMSParser aramsParser = new ARAMSParser(arams);
        ArrayList<MoveRecord> aramsMoves = aramsParser.parse();

        // TODO: Parse scotland / wales sheets
        // TODO: Merge all MoveRecords into one big array and deduplicate

        Sheet wales = wb.getSheetAt(1);
        WalesParser walesParser = new WalesParser(wales);
        ArrayList<MoveWales> walesMoves = walesParser.parse();


        long numMoves = aramsMoves.size();
        return String.format("Parsed %d movements", numMoves);
    }
}
