package nz.ac.wgtn.swen301.a3.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
    private final Persistency persistency = new Persistency();
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
        System.out.println("doGet");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Echoing Request Headers</h1>");
        out.println("<table border><th>Index</th><th>Value</th>");
        // list headers send by the client
        var tempList = persistency.getList();
        persistency.add(new LogEvent(UUID.randomUUID(),"random message",new Date(),"thread","logger", LevelEnum.ERROR, "string"));
        for (var i = 0; i < tempList.size(); i++) {
            var index = String.valueOf(i);
            var value = tempList.get(i);

            out.print("<tr><td>");
            out.print(index);
            out.print("</td><td>");
            out.print(objectMapper.writeValueAsString(value));
            out.println("</td></tr>");
        }
//        for (Enumeration headers = req.getHeaderNames(); headers.hasMoreElements(); ) {
//            String header = headers.nextElement().toString();
//            String value = req.getHeader(header);
//            out.print("<tr><td>");
//            out.print(header);
//            out.print("</td><td>");
//            out.print(value);
//            out.println("</td></tr>");
//        }
//        req.getHeaderNames().asIterator().forEachRemaining(System.out::println);
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPostInvoke");
        var reqBody = req.getReader().lines().collect(Collectors.joining());
        persistency.add(objectMapper.readValue(reqBody, LogEvent.class));
    }
}
