package Group1.com.DataConsolidation.DataProcessing;

import org.apache.poi.ss.usermodel.*;

import java.util.*;

public abstract class Parser {
    protected Sheet sheet;
    protected DataFormatter formatter;
    protected Map<String, Integer> headings;
    protected String parserName;

    public Parser(Sheet sheet, String parserName) {
        this.sheet = sheet;
        this.formatter = new DataFormatter();
        this.parserName = parserName;
    }

    public abstract ArrayList<MoveRecord> parse() throws WorkbookParseException;

    protected WorkbookParseException makeError(String s) {
        return new WorkbookParseException(this.parserName + ": " + s);
    }

    protected void parseHeadings(Iterator<Row> rowIter, String[] headingNames)
            throws WorkbookParseException {
        if (!rowIter.hasNext())
            throw makeError("empty spreadsheet (no headings)");

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
                        String msg = String.format("duplicate heading '%s'", value);
                        throw makeError(msg);
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

            throw makeError("didn't find all headings - missing "
                            + String.join(", ", missingHeadings));
        }

        this.headings = headings;
    }

    // Gets the value of a cell in a given row corresponding to a specific heading
    protected String getCellData(Row row, String heading) throws WorkbookParseException {
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
                throw makeError(msg);*/
                return this.formatter.formatCellValue(cell);
        }
    }
}
