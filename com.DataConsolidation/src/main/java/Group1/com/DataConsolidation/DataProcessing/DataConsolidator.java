package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

public class DataConsolidator {

    private Progress progress;
    private final Workbook wb;

    public DataConsolidator(Workbook wb, Progress progress) {
        Assert.notNull(wb, "workbook was null");
        this.wb = wb;
        this.progress = progress;
    }

    public XSSFWorkbook parse(CPH outbreakSource) throws WorkbookParseException {
        if (wb.getNumberOfSheets() == 0) {
            throw new WorkbookParseException("empty workbook (no sheets)");
        }

        ArrayList<MoveRecord> movesFrom = new ArrayList<>();
        ArrayList<MoveRecord> movesTo = new ArrayList<>();
        progress.reset();

        Sheet arams = wb.getSheetAt(0); // TODO: Will this always correspond to ARAMS?
        ARAMSParser aramsParser = new ARAMSParser(arams, progress, outbreakSource);
        var aramsMoves = aramsParser.parse();
        movesFrom.addAll(aramsMoves.getFirst());
        movesTo.addAll(aramsMoves.getSecond());

        Sheet scotlandFrom = wb.getSheetAt(1);
        SCOTEIDParser scoteidParserFrom = new SCOTEIDParser(scotlandFrom, progress, outbreakSource);
        movesFrom.addAll(scoteidParserFrom.parse().getFirst());

        Sheet scotlandTo = wb.getSheetAt(2);
        SCOTEIDParser scoteidParserTo = new SCOTEIDParser(scotlandTo, progress, outbreakSource);
        movesTo.addAll(scoteidParserTo.parse().getFirst());

        Sheet wales = wb.getSheetAt(3);
        WalesParser walesParser = new WalesParser(wales, progress, outbreakSource);
        var walesMoves = walesParser.parse();
        movesFrom.addAll(walesMoves.getFirst());
        movesTo.addAll(walesMoves.getSecond());

        // First sort by location, then by date
        movesFrom.sort(
                Comparator.comparing((MoveRecord m) -> m.locationTo.number, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing((MoveRecord m) -> m.arriveDate, Comparator.nullsLast(Comparator.naturalOrder()))
        );
        movesTo.sort(
                Comparator.comparing((MoveRecord m) -> m.locationFrom.number, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing((MoveRecord m) -> m.departDate, Comparator.nullsLast(Comparator.naturalOrder()))
        );

        deduplicate(movesFrom);
        deduplicate(movesTo);

        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheetFrom = wb.createSheet("From Infected");
        Sheet sheetTo = wb.createSheet("To Infected");
        createResultSheet(sheetFrom, movesFrom);
        createResultSheet(sheetTo, movesTo);
        return wb;
    }

    private void createResultSheet(Sheet sh, ArrayList<MoveRecord> moves) throws WorkbookParseException {
        // Print the headings
        Field[] fieldList = MoveRecord.class.getDeclaredFields();
        Row headingsRow = sh.createRow(0);
        for (int i = 0; i < fieldList.length; i++) {
            // TODO: We should use more user-friendly field names in future
            headingsRow.createCell(i).setCellValue(fieldList[i].getName());
        }

        // Print the MoveRecords
        int rowIndex = 1;
        for (MoveRecord m : moves) {
            Row r = sh.createRow(rowIndex);

            for (int colIndex = 0; colIndex < fieldList.length; colIndex++) {
                String cellValue = m.fieldValue(fieldList[colIndex]);
                r.createCell(colIndex).setCellValue(cellValue);
            }

            rowIndex += 1;
        }
    }

    // We assume, following the sorting we perform, that duplicates will be listed consecutively
    private void deduplicate(ArrayList<MoveRecord> moves) {
        for (int i = 1; i < moves.size(); i++) {
            MoveComparison comparison = moves.get(i).compareTo(moves.get(i - 1));
            if (comparison == MoveComparison.Equal) {
                // We have a duplicate
                // TODO: Merge the records rather than just deleting whichever one appears second
                moves.remove(i);
                i--;
            } else if (comparison == MoveComparison.ApproxEqual) {
                // TODO: Do something like highlight the rows to indicate that there was an approximate match
            }
        }
    }
}
