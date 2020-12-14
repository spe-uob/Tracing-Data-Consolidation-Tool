package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class ARAMSParser {
    private Sheet sheet;

    public ARAMSParser(Sheet sheet) {
        this.sheet = sheet;
    }

    public ArrayList<MoveRecord> parse() throws WorkbookParseException {
        Iterator<Row> rowIter = this.sheet.rowIterator();
        Map<String, Integer> headings = parseHeadings(rowIter);

        DataFormatter formatter = new DataFormatter();
        ArrayList<MoveRecord> out = new ArrayList();

        while (rowIter.hasNext()) {
            Row row = rowIter.next();
            Cell cell = row.getCell(headings.get("Movement ID"));

            switch (cell.getCellType()) {
                case STRING:
                    String moveID = cell.getStringCellValue();
                    out.add(new MoveRecord(moveID));
                    break;
                case BLANK:
                    out.add(new MoveRecord(""));
                    break;
                default:
                    String msg = String.format(
                            "unrecognised cell type '%s' at (%s, %s): %s",
                            cell.getCellType().toString(),
                            cell.getRowIndex(),
                            cell.getColumnIndex(),
                            formatter.formatCellValue(cell));
                    throw new WorkbookParseException(msg);
            }
        }

        return out;
    }

    private Map<String, Integer> parseHeadings(Iterator<Row> rowIter) throws WorkbookParseException {
        if (!rowIter.hasNext())
            throw new WorkbookParseException("ARAMS: empty spreadsheet (no headings)");

        String[] headingNames = {
                "Movement ID",
                "From Premises",
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
            throw new WorkbookParseException("ARAMS: didn't find all headings");
        }

        return headings;
    }
}
