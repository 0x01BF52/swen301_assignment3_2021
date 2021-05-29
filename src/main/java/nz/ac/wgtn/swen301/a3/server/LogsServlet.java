package nz.ac.wgtn.swen301.a3.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Logs servlet.
 * Implement the logs/ services implemented using the standard servlet doGet, doPost
 * and doDelete methods in a class nz.ac.wgtn.swen301.a3.server.LogsServlet.
 * This class must have a default constructor (public, no parameters 1). [2 marks]
 */
public class LogsServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Instantiates a new Logs servlet.
     */
    public LogsServlet() {
        //This class must have a default constructor (public, no parameters 1). [2 marks]
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doDelete");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        var reqLimit = req.getParameter("limit");
        var reqLevelSting = req.getParameter("level");
        try {
            var limit = Integer.parseInt(reqLimit);
            LevelEnum.valueOf(reqLevelSting);
            if (limit <= 0) {
                throw new InvalidParameterException();
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.close();
            return;
        }
        System.out.println("LogsServlet doGet Invoke");
        resp.setContentType("application/json");
        var level = LevelEnum.valueOf(reqLevelSting);
        Persistency.add(new LogEvent(UUID.randomUUID(), "random message", new Date(), "thread", "logger", LevelEnum.FATAL, "string"));
        var db = Persistency.getDB();
        ArrayList<LogEvent> results = new ArrayList<>();
        if (db.isEmpty()) {
            out.println(results);
            out.close();
            return;
        }

        for (int i = db.size() - 1; (i >= db.size() - 1 - Integer.parseInt(reqLimit)) && (i >= 0); i--) {
            if (db.get(i).getLevel().isGreaterOrEqual(level)) {
                results.add(db.get(i));
            }
        }
        results.sort(Comparator.comparing(LogEvent::getTimestamp));
        out.println(objectMapper.writeValueAsString(results));

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPostInvoke");
        var reqBody = req.getReader().lines().collect(Collectors.joining());
        Persistency.add(objectMapper.readValue(reqBody, LogEvent.class));
    }
}
