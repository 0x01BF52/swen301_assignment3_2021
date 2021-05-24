package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Logs servlet.
 * Implement the logs/ services implemented using the standard servlet doGet, doPost
 * and doDelete methods in a class nz.ac.wgtn.swen301.a3.server.LogsServlet.
 * This class must have a default constructor (public, no parameters 1). [2 marks]
 */
public class LogsServlet extends HttpServlet {
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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost");
    }
}
