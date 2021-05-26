package nz.ac.wgtn.swen301.a3.server;

import java.util.List;

/**
 * Uses:
 * HTMLTableBuilder htmlBuilder = new HTMLTableBuilder(null, true, 2, 3);
 * htmlBuilder.addTableHeader("1H", "2H", "3H");
 * htmlBuilder.addRowValues("1", "2", "3");
 * htmlBuilder.addRowValues("4", "5", "6");
 * htmlBuilder.addRowValues("9", "8", "7");
 * String table = htmlBuilder.build();
 * System.out.println(table.toString());
 */

public class StatsTableHTMLBuilder {
    private int columns;
    private StringBuilder table = new StringBuilder();
    private static final String DOCTYPE = "<!DOCTYPE html>";
    private static final String HEAD = "<head><title>Log Statistics</title><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head>";
    private static final String TITLE = "<h1>Log Statistics</h1>";
    private static final String BODY_START = "<body>";
    private static final String BODY_END = "</body>";
    private static final String HTML_START = "<html>";
    private static final String HTML_END = "</html>";
    private static final String TABLE_START_BORDER = "<table border=\"1\">";
    private static final String TABLE_START = "<table>";
    private static final String TABLE_END = "</table>";
    private static final String HEADER_START = "<th>";
    private static final String HEADER_END = "</th>";
    private static final String ROW_START = "<tr>";
    private static final String ROW_END = "</tr>";
    private static final String COLUMN_START = "<td>";
    private static final String COLUMN_END = "</td>";


    /**
     * Instantiates a new Html table builder.
     *
     * @param headerList the headerList
     * @param border     the border
     * @param rows       the rows
     * @param columns    the columns
     */
    public StatsTableHTMLBuilder(List<String> headerList, boolean border) {
        this.columns = headerList.size();
        table.append(DOCTYPE);
        table.append(HTML_START);
        table.append(HEAD);
        table.append(TITLE);
        table.append(BODY_START);
        if (border) { table.append(TABLE_START_BORDER);} else { table.append(TABLE_START);}
        table.append(TABLE_END);
        this.addTableHeader(headerList);
        table.append(BODY_END);
        table.append(HTML_END);
    }


    /**
     * Add table header.
     *
     * @param values the values
     */
    public void addTableHeader(List<String> values) {
        int lastIndex = table.lastIndexOf(TABLE_END);
        if (lastIndex > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(ROW_START);
            for (String value : values) {
                sb.append(HEADER_START);
                sb.append(value);
                sb.append(HEADER_END);
            }
            sb.append(ROW_END);
            table.insert(lastIndex, sb.toString());
        }
    }


    /**
     * Add row values.
     *
     * @param values the values
     */
    public void addRowValues(List<String> values) {
        if (values.size() != columns) {
            System.out.println("Error column lenth");
        } else {
            int lastIndex = table.lastIndexOf(ROW_END);
            if (lastIndex > 0) {
                int index = lastIndex + ROW_END.length();
                StringBuilder sb = new StringBuilder();
                sb.append(ROW_START);
                for (String value : values) {
                    sb.append(COLUMN_START);
                    sb.append(value);
                    sb.append(COLUMN_END);
                }
                sb.append(ROW_END);
                table.insert(index, sb.toString());
            }
        }
    }


    /**
     * Build table html string.
     *
     * @return string
     */
    public String build() {
        return table.toString();
    }


}