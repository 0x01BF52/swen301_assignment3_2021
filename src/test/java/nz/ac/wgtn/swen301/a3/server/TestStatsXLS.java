package nz.ac.wgtn.swen301.a3.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestStatsXLS {
    private StatsXLSServlet statsXLSServlet;
    private LogsServlet logsServlet;

    @BeforeEach
    void setupServlet() {
        statsXLSServlet = new StatsXLSServlet();
        logsServlet = new LogsServlet();
    }

    @AfterEach
    void clearServlet() {
        statsXLSServlet = null;
        logsServlet = null;
    }

    @Test
    void contentTypeTest() throws ServletException, IOException {
        MockHttpServletRequest req1 = new MockHttpServletRequest();
        MockHttpServletResponse res1 = new MockHttpServletResponse();
        statsXLSServlet.doGet(req1, res1);
        assertEquals("application/vnd.ms-excel", res1.getContentType());
    }

    @Test
    void formatTest() throws ServletException, IOException {
        MockHttpServletRequest req1 = new MockHttpServletRequest();
        MockHttpServletResponse res1 = new MockHttpServletResponse();
        statsXLSServlet.doGet(req1, res1);
        Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(res1.getContentAsByteArray()));
        workbook.getSheetAt(0);
        var resHeader = new ArrayList<String>();
        workbook.getSheetAt(0).getRow(0).forEach(e -> resHeader.add(e.getStringCellValue()));
        ArrayList<String> headerList = new ArrayList<>(List.of("logger"));
        headerList.addAll(Arrays.stream(LevelEnum.values()).map(LevelEnum::toString).collect(Collectors.toUnmodifiableList()));
        assertEquals(headerList, resHeader);
    }

    @Test
    void dataCorrectnessTest() throws ServletException, IOException {
        var om = new ObjectMapper();
        for (int round = 0; round < 2; round++) {
            var list = IntStream.range(1, 6).mapToObj(i -> {
                return new LogEvent("msg" + String.valueOf(i), "formatTest", "logger" + String.valueOf(i), LevelEnum.values()[i], "log" + i);
            }).collect(Collectors.toUnmodifiableList());
            list.forEach(e -> {
                try {
                    var req = new MockHttpServletRequest();
                    var res = new MockHttpServletResponse();
                    req.setContent(om.writeValueAsBytes(e));
                    logsServlet.doPost(req, res);
                } catch (Exception ignored) {
                }
            });
        }
        /*
         * Example generated data for test
         * [logger4, 0, 0, 0, 0, 2, 0, 0, 0]
         * [logger5, 0, 0, 0, 0, 0, 2, 0, 0]
         * [logger2, 0, 0, 2, 0, 0, 0, 0, 0]
         * [logger3, 0, 0, 0, 2, 0, 0, 0, 0]
         * [logger1, 0, 2, 0, 0, 0, 0, 0, 0]
         * */
        MockHttpServletRequest req1 = new MockHttpServletRequest();
        MockHttpServletResponse res1 = new MockHttpServletResponse();
        statsXLSServlet.doGet(req1, res1);
        var workbook = new HSSFWorkbook(new ByteArrayInputStream(res1.getContentAsByteArray()));

        workbook.getSheetAt(0).forEach(r -> {
            var tempList = new ArrayList<String>();
            if (r.getRowNum() != 0) {
                r.forEach(c -> tempList.add(c.getStringCellValue()));
                assertEquals(1, tempList.stream().filter(c -> c.equals("2")).count());
            }
        });
    }
}
