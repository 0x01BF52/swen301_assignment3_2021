package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StatsServlet extends HttpServlet {
    private int generateRows() {
        return 0;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("StatsServlet doGet Invoke");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
//        IntStream.range(0, 10).mapToObj(i -> new LogEvent(UUID.randomUUID(), "random message", new Date(), "thread", "logger1", LevelEnum.FATAL, "string")).forEach(persistency::add);
        ArrayList<String> headerList = new ArrayList<>(List.of("logger"));
        headerList.addAll(Arrays.stream(LevelEnum.values()).map(LevelEnum::toString).collect(Collectors.toUnmodifiableList()));
        var htmlBuilder = new StatsTableHTMLBuilder(headerList, true);
        var groupByLogger = Persistency.getDB().stream().collect(Collectors.groupingBy(LogEvent::getLogger));
        groupByLogger.forEach((loggerName, logEvents) -> {
            var groupByLevel = logEvents.stream().collect(Collectors.groupingBy(LogEvent::getLevel));
            htmlBuilder.addLogStats(loggerName, groupByLevel);
        });
        out.println(htmlBuilder.build());
        out.close();
    }
}
