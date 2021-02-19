package Group1.com.DataConsolidation.DataProcessing;

import javassist.Loader;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class MoveRecord {
    public String id;
    public String activityFrom;
    public String activityTo;
    public String recordedDate;
    public String status;
    public String moveMethod;
    public String moveDirection;
    public String species;
    public String animalNumber;
    public String animalDescription;
    public String reads;
    public String percentage;
    public String moveMove;
    public String lotDate;
    public String lotID;
    public String readLocation;
    public CPH locationFrom;
    public CPH locationTo;
    public Date departDate;
    public Date arriveDate;

    // TODO: Can we merge either of these with the above?
    public String count;
    public String createdBy;

    // We don't expect any of these to be empty
    public String departCountry;
    public String arriveCountry;

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

        // We don't expect the departCountry and arriveCountry fields to be empty,
        // as we fill them from the CPH numbers.
        return numFieldsEmpty == fieldList.length - 2;
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
                    return null;
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

}
