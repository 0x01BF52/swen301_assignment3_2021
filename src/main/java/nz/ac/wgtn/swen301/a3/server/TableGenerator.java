package nz.ac.wgtn.swen301.a3.server;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TableGenerator {
    static final String SHEET_NAME = "stats";

    public static List<List<String>> convert() {
        var listOfList = new ArrayList<List<String>>();
        ArrayList<String> headerList = new ArrayList<>(List.of("logger"));
        headerList.addAll(Arrays.stream(LevelEnum.values()).map(LevelEnum::toString).collect(Collectors.toUnmodifiableList()));
        listOfList.add(headerList);
        var groupByLogger = Persistency.getDB().stream().collect(Collectors.groupingBy(LogEvent::getLogger));
        groupByLogger.forEach((loggerName, logEvents) -> {
            var groupByLevel = logEvents.stream().collect(Collectors.groupingBy(LogEvent::getLevel));
            listOfList.add(generateList(loggerName, groupByLevel));
        });
        return listOfList;
    }

    private static List<String> generateList(String loggerName, Map<LevelEnum, List<LogEvent>> groupByLevel) {
        var list = new ArrayList<String>(List.of(loggerName));
        for (var i = 0; i < LevelEnum.values().length; i++) {
            LevelEnum currentLevel = LevelEnum.values()[i];
            if (groupByLevel.containsKey(currentLevel)) {
                list.add(String.valueOf(groupByLevel.get(currentLevel).size()));
            } else {
                list.add("0");
            }
        }
        return list;
    }

    public static Workbook generateWorkbook() {
        Workbook workbook = new HSSFWorkbook();
        var sheet = workbook.createSheet(SHEET_NAME);
        var table = TableGenerator.convert();
        IntStream.range(0, table.size()).forEach(rowIndex -> {
            var row = sheet.createRow(rowIndex);
            var colList = table.get(rowIndex);
            IntStream.range(0, colList.size()).forEach(colIndex -> {
                row.createCell(colIndex).setCellValue(colList.get(colIndex));
            });
        });
        return workbook;
    }

    public static String generateCSV() {
        var sheet = generateWorkbook().getSheetAt(0);
        var sb = new StringBuilder();
        Row row;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                sb.append(row.getCell(j)).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
