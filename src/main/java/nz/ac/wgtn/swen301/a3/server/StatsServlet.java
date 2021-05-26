package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StatsServlet extends HttpServlet {
    private int generateRows() {
        return 0;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("StatsServlet doGet Invoke");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        var persistency = LogsServlet.getPersistency();
        IntStream.range(0, 10).mapToObj(i -> new LogEvent(UUID.randomUUID(), "random message", new Date(), "thread", "logger", LevelEnum.FATAL, "string")).forEach(persistency::add);
        ArrayList<String> headerList = new ArrayList<>(List.of("logger"));
        headerList.addAll(Arrays.stream(LevelEnum.values()).map(LevelEnum::toString).collect(Collectors.toUnmodifiableList()));
        System.out.println(headerList);
        var htmlBuilder = new StatsTableHTMLBuilder(headerList, true);
//        htmlBuilder.addTableHeader(headerList);
        htmlBuilder.addRowValues(List.of("0", "0", "0", "4", "5", "6", "9", "8", "7"));

        Map<String, List<LogEvent>> groupByLogger = persistency.getList().stream().collect(Collectors.groupingBy(LogEvent::getLogger));
        groupByLogger.forEach((loggerName, logEvents) -> {
            Map<LevelEnum, List<LogEvent>> groupByLevel = logEvents.stream().collect(Collectors.groupingBy(LogEvent::getLevel));
            List<String> row = new ArrayList<>(List.of(loggerName));
            row.addAll(groupByLevel.values().stream().map(logEventList -> String.valueOf(logEventList.size())).collect(Collectors.toUnmodifiableList()));
            htmlBuilder.addRowValues(row);
        });
        out.println(htmlBuilder.build());
        out.close();
    }
}
