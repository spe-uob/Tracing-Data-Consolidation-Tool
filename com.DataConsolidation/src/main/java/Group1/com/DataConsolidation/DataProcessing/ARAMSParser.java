package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class ARAMSParser {
    private Sheet sheet;
    private Map<String, Integer> headings;
    private DataFormatter formatter;

    public ARAMSParser(Sheet sheet) {
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
            move.departDate = getCellData(row, "Dest Country");

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
            throw new WorkbookParseException("ARAMS: empty spreadsheet (no headings)");

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
                        String msg = String.format("ARAMS: duplicate heading '%s'", value);
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
                    "ARAMS: didn't find all headings - missing "
                    + String.join(", ", missingHeadings));
        }

        return headings;
    }
}
