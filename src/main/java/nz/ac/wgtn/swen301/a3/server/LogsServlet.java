package nz.ac.wgtn.swen301.a3.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        Persistency.clear();
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Persistency.clear();
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad or missing input parameter (for instance, level is not a valid level or limit is not a positive, non-zero integer)");
            out.close();
            return;
        }
        resp.setContentType("application/json");
        var level = LevelEnum.valueOf(reqLevelSting);
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
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        results.sort(Comparator.comparing(logEvent -> {
            try {
                return dateFormat.parse(logEvent.getTimestamp());
            } catch (ParseException ignored) {
            }
            return null;
        }));
        Collections.reverse(results);
        resp.setStatus(HttpServletResponse.SC_OK);
        out.println(objectMapper.writeValueAsString(results));
        out.close();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var reqBody = req.getReader().lines().collect(Collectors.joining());
        try {
            if (reqBody.isEmpty()) {
                throw new InvalidParameterException("req body is empty");
            }
            var newEvent = objectMapper.readValue(reqBody, LogEvent.class);
            if (Persistency.getDB().stream().map(LogEvent::getId).anyMatch(e -> e.equals(newEvent.getId()))) {
                resp.sendError(HttpServletResponse.SC_CONFLICT, "a log event with this id already exists");
            } else {
                Persistency.add(newEvent);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid input, object invalid");
        }
    }
}
