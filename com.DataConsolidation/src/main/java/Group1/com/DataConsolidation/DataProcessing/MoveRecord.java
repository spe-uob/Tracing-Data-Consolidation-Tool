package Group1.com.DataConsolidation.DataProcessing;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MoveRecord {
    public String id;
    public String activityFrom;
    public String activityTo;
    public String recordedDate;
    public String status;
    public String moveMethod;
    public String moveDirection;
    public String species;
    public String animalCount;
    public String animalDescription;
    public String reads;
    public String percentage;
    public String moveMove;
    public String lotDate;
    public String lotID;
    public String readLocation;
    public String createdBy;
    public String departCountry;
    public String arriveCountry;
    public String originatingSheet;
    public CPH locationFrom;
    public CPH locationTo;
    public Date departDate;
    public Date arriveDate;

    public MoveRecord() {}

    public boolean isEmpty() throws WorkbookParseException {
        Field[] fieldList = MoveRecord.class.getDeclaredFields();
        int numFieldsEmpty = 0;

        for (Field field : fieldList) {
            String cellValue = this.fieldValue(field);
            if (Objects.isNull(cellValue) || cellValue.equals("") || cellValue.equals(" ")) {
                numFieldsEmpty += 1;
            }
        }

        return numFieldsEmpty == fieldList.length;
    }

    public boolean isFromInfected(CPH outbreakSource) {
        if (locationFrom.number.equals(outbreakSource.number)) {
            return true;
        } else if (locationTo.number.equals(outbreakSource.number)) {
            return false;
        } else {
            // This typically shouldn't happen (only if CPH number parsing failed)
            return true;
        }
    }

    public String fieldValue(Field field) throws WorkbookParseException {
        try {
            if (field.getType() == CPH.class) {
                return ((CPH)field.get(this)).number;
            } else if (field.getType() == Date.class) {
                Date d = (Date)field.get(this);
                if (Objects.isNull(d)) {
                    return "";
                } else {
                    return new SimpleDateFormat("dd/MM/yyyy").format(d);
                }
            } else {
                return (String)field.get(this);
            }
        } catch(IllegalAccessException e) {
            throw new WorkbookParseException("Failed to reflect on MoveRecord", e);
        }
    }

    // Checks if two moves are a duplicate of each other
    public MoveComparison compareTo(MoveRecord other) {
        if (this.originatingSheet.equals(other.originatingSheet)) {
            // We assume no moves are duplicated within the same input sheet
            return MoveComparison.Unequal;
        } else if (!datesOverlap(other)) {
            return MoveComparison.Unequal;
        } else if (!this.locationFrom.equals(other.locationFrom) || !this.locationTo.equals(other.locationTo)) {
            return MoveComparison.Unequal;
        } else if (this.animalCount.equals(other.animalCount)) {
            return MoveComparison.Equal;
        } else if (countsApproximatelyEqual(this.animalCount, other.animalCount)) {
            return MoveComparison.ApproxEqual;
        } else {
            return MoveComparison.Unequal;
        }
    }

    private boolean datesOverlap(MoveRecord other) {
        boolean arriveDatesEqual = Objects.nonNull(this.arriveDate)
                && Objects.nonNull(other.arriveDate)
                && this.arriveDate.equals(other.arriveDate);
        boolean departDatesEqual = Objects.nonNull(this.departDate)
                && Objects.nonNull(other.departDate)
                && this.departDate.equals(other.departDate);
        return arriveDatesEqual || departDatesEqual;
    }

    // Check if the counts are equal to within ~10%
    private boolean countsApproximatelyEqual(String countA, String countB) {
        try {
            int a = Integer.parseUnsignedInt(countA);
            int b = Integer.parseUnsignedInt(countB);
            float absDiff = (float)Math.abs(a - b);
            float maxRelativeError = 0.1f * ((float)a + (float)b) / 2.0f;
            return absDiff <= maxRelativeError;
        } catch (Exception e) {
            return false;
        }
    }
}
