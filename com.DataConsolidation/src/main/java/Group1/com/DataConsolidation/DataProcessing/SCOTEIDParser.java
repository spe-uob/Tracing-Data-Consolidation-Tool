package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class SCOTEIDParser extends Parser {
    public SCOTEIDParser(Sheet sheet) {
        super(sheet, "SCOT EID");
    }

    public ArrayList<MoveRecord> parse() throws WorkbookParseException {
        Iterator<Row> rowIter = this.sheet.rowIterator();

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

        parseHeadings(rowIter, headingNames);

        ArrayList<MoveRecord> out = new ArrayList();

        while (rowIter.hasNext()) {
            Row row = rowIter.next();

            MoveRecord move = new MoveRecord();
            move.id = getCellData(row, "Unique_Ref");
            move.animalNumber = getCellData(row, "Sheep");
            move.reads = getCellData(row, "Reads");
            move.percentage = getCellData(row, "%");
            move.moveMove = getCellData(row, "Move");
            move.lotDate = getCellData(row, "Lot Date");
            move.lotID = getCellData(row, "Lot");
            move.locationFrom = getCellData(row, "Depart. CPH");
            move.readLocation = getCellData(row, "Read Location");
            move.activityFrom = getCellData(row, "Dest. CPH");

            out.add(move);
        }

        return out;
    }
}
