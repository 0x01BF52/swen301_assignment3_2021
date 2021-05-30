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

    public StatsServlet() {
        //All classes must have a default constructor (public, no parameters).
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("StatsServlet doGet Invoke");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        ArrayList<String> headerList = new ArrayList<>(List.of("logger"));
        headerList.addAll(Arrays.stream(LevelEnum.values()).map(LevelEnum::toString).collect(Collectors.toUnmodifiableList()));
        var htmlBuilder = new StatsTableHTMLBuilder(headerList, false);
        var groupByLogger = Persistency.getDB().stream().collect(Collectors.groupingBy(LogEvent::getLogger));
        groupByLogger.forEach((loggerName, logEvents) -> {
            var groupByLevel = logEvents.stream().collect(Collectors.groupingBy(LogEvent::getLevel));
            htmlBuilder.addLogStats(loggerName, groupByLevel);
        });
        out.println(htmlBuilder.build());
        out.close();
    }
}
