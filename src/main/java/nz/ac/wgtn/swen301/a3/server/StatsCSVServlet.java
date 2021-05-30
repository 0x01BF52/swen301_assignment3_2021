package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatsCSVServlet extends HttpServlet {
    public StatsCSVServlet() {
        //All classes must have a default constructor (public, no parameters).
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/csv");
        resp.getOutputStream().print(TableGenerator.generateCSV());
    }

}
