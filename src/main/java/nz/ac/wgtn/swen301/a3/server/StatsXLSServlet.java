package nz.ac.wgtn.swen301.a3.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.IntStream;

public class StatsXLSServlet extends HttpServlet {
    static final String SHEET_NAME = "stats";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/vnd.ms-excel");
        TableGenerator.generateWorkbook().write(resp.getOutputStream());
    }
}
