package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;

public class ARAMSParser {
    private Sheet sheet;

    public ARAMSParser(Sheet sheet) {
        this.sheet = sheet;
    }

    public ArrayList<MoveRecord> parse() throws SpreadsheetParseException {
        // Skip headings row
        var rowIter = this.sheet.rowIterator();
        if (!rowIter.hasNext())
            throw new SpreadsheetParseException("empty spreadsheet");
        else
            rowIter.next();

        DataFormatter formatter = new DataFormatter();

        ArrayList<MoveRecord> out = new ArrayList();
        while (rowIter.hasNext()) {
            Row row = rowIter.next();
            Cell cell = row.getCell(1); // IDs start in column B

            switch (cell.getCellType()) {
                case STRING:
                    String moveID = cell.getStringCellValue();
                    out.add(new MoveRecord(moveID));
                    break;
                case BLANK:
                    out.add(new MoveRecord(""));
                    break;
                default:
                    String msg = String.format("unrecognised cell type '%s' at (%s, %s): %s",
                            cell.getCellType().toString(),
                            cell.getAddress().getRow(),
                            cell.getAddress().getColumn(),
                            formatter.formatCellValue(cell));
                    throw new SpreadsheetParseException(msg);
            }
        }

        return out;
    }
}
