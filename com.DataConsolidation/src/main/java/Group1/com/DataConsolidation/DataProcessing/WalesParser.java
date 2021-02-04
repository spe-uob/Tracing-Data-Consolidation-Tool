package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.*;
import java.util.*;

public class WalesParser extends Parser{

     public WalesParser(Sheet sheet) {
         super(sheet, "Wales");
     }

     public ArrayList<MoveRecord> parse() throws WorkbookParseException {
         Iterator<Row> rowIter = this.sheet.rowIterator();

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

         parseHeadings(rowIter, headingNames);

         ArrayList<MoveRecord> out = new ArrayList();

         while (rowIter.hasNext()) {
             Row row = rowIter.next();

             MoveRecord move = new MoveRecord();
             move.id = getCellData(row, "Ref");
             move.count = getCellData(row, "Count");
             move.species = getCellData(row, "Species");
             move.lotID = getCellData(row, "Lot");
             move.lotDate = getCellData(row, "Date");
             move.locationFrom = getCellData(row, "From CPH");
             move.locationTo = getCellData(row, "To CPH");
             move.createdBy = getCellData(row, "Created By");

             out.add(move);
         }
         return out;
     }
}
