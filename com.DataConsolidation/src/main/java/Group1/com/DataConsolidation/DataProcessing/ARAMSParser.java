package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class ARAMSParser extends Parser {
    public ARAMSParser(Sheet sheet, Progress progress) {
        super(sheet, progress, "ARAMS");
    }

    public ArrayList<MoveRecord> parse() throws WorkbookParseException {
        Iterator<Row> rowIter = this.sheet.rowIterator();

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

        parseHeadings(rowIter, headingNames);

        ArrayList<MoveRecord> out = new ArrayList();

        while (rowIter.hasNext()) {
            Row row = rowIter.next();
            progress.incrementRowsProcessed();

            MoveRecord move = new MoveRecord();
            move.id = getCellData(row, "Movement ID");
            move.locationFrom = getCellData(row, "From Premises");
            move.locationTo = getCellData(row, "From Activity");
            move.activityFrom = getCellData(row, "To Premises");
            move.activityTo = getCellData(row, "To Activity");
            move.departDate = getCellData(row, "Departure Date");
            move.arriveDate = getCellData(row, "Arrival Date");
            move.recordedDate = getCellData(row, "Recorded Date");
            move.status = getCellData(row, "Status");
            move.moveMethod = getCellData(row, "Move Method");
            move.moveDirection = getCellData(row, "Move Direction");
            move.species = getCellData(row, "Species");
            move.animalNumber = getCellData(row, "Animal No");
            move.herdMark = getCellData(row, "Herd Mark");
            move.animalDescription = getCellData(row, "Animal Description");
            move.departCountry = getCellData(row, "Dept Country");
            move.arriveCountry = getCellData(row, "Dest Country");

            out.add(move);
        }

        return out;
    }
}
