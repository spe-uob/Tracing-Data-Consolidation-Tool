package Group1.com.DataConsolidation.DataProcessing;

// Represents a single CPH number
public class CPH {
    public String number;

    public CPH(String number) {
        // TODO: Convert FSA to CPH
        this.number = number;
    }

    public String getCountry() {
        int digits;
        try {
            // The country can be identified by looking at the first two digits
            digits = Integer.parseUnsignedInt(this.number, 0, 2, 10);
        } catch (Exception e) {
            return "UNKNOWN";
        }

        // 1  - 51: England
        // 52 - 66: Wales
        // 67+    : Scotland
        if (1 <= digits && digits <= 51) {
            return "England";
        } else if (52 <= digits && digits <= 66) {
            return "Wales";
        } else if (67 <= digits && digits <= 99) {
            return "Scotland";
        } else {
            return "UNKNOWN";
        }
    }
}