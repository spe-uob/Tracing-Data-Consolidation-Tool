package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class SCOTEIDParser {
    private Sheet sheet;
    private Map<String, Integer> headings;
    private DataFormatter formatter;

    public SCOTEIDParser(Sheet sheet) {
        this.sheet = sheet;
        this.formatter = new DataFormatter();
    }

    public ArrayList<MoveRecord> parse() throws WorkbookParseException {
        Iterator<Row> rowIter = this.sheet.rowIterator();
        this.headings = parseHeadings(rowIter);

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

    // Gets the value of a cell in a given row corresponding to a specific heading
    private String getCellData(Row row, String heading) throws WorkbookParseException {
        Cell cell = row.getCell(this.headings.get(heading), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BLANK:
                return "";
            default:
                /*String msg = String.format(
                        "unrecognised cell type '%s' at (%s, %s), column '%s': %s",
                        cell.getCellType().toString(),
                        cell.getRowIndex(),
                        cell.getColumnIndex(),
                        heading,
                        formatter.formatCellValue(cell));
                throw new WorkbookParseException(msg);*/
                return this.formatter.formatCellValue(cell);
        }
    }

    private Map<String, Integer> parseHeadings(Iterator<Row> rowIter) throws WorkbookParseException {
        if (!rowIter.hasNext())
            throw new WorkbookParseException("SCOT EID: empty spreadsheet (no headings)");

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

        Map<String, Integer> headings = new HashMap<>();
        Row row = rowIter.next();

        for (Cell cell : row) {
            if (!cell.getCellType().equals(CellType.STRING)) {
                continue;
            }

            for (String name : headingNames) {
                String value = cell.getStringCellValue();
                if (value.compareToIgnoreCase(name) == 0) {
                    Integer oldIndex = headings.putIfAbsent(value, cell.getColumnIndex());
                    if (!Objects.isNull(oldIndex)) {
                        String msg = String.format("SCOT EID: duplicate heading '%s'", value);
                        throw new WorkbookParseException(msg);
                    }
                }
            }
        }

        if (headings.size() != headingNames.length) {
            // Find which headings were missing from the sheet
            ArrayList<String> missingHeadings = new ArrayList<>();
            for (String heading : headingNames) {
                if (!headings.containsKey(heading)) {
                    missingHeadings.add(heading);
                }
            }

            throw new WorkbookParseException(
                    "SCOT EID: didn't find all headings - missing "
                            + String.join(", ", missingHeadings));
        }

        return headings;
    }
}
