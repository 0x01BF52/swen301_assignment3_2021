package nz.ac.wgtn.swen301.a3.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestStatsCSV {
    private StatsCSVServlet servlet;

    @BeforeEach
    void setupServlet() {
        servlet = new StatsCSVServlet();
    }

    @AfterEach
    void clearServlet() {
        servlet = null;
    }

    @Test
    void contentTypeTest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        assertEquals("text/csv", response.getContentType());
    }

    @Test
    void formatTest() throws Exception {
        var om = new ObjectMapper();
        var logsServlet = new LogsServlet();
        var list = IntStream.range(0, 100).mapToObj(i -> {
//            var r = new SecureRandom().nextInt(LevelEnum.values().length);
            return new LogEvent(String.valueOf(i), "formatTest", "formatTest", LevelEnum.INFO, "log" + i);
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
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        servlet.doGet(request2, response2);
        var resSplit = response2.getContentAsString().split("\n");
        assertEquals(TableGenerator.generateListOfList().size(), resSplit.length);
    }

    @Test
    void dataCorrectnessTest() throws Exception {
        var logsServlet = new LogsServlet();
        var om = new ObjectMapper();
        var list = IntStream.range(1, 6).mapToObj(i ->
            new LogEvent(String.valueOf(i), "formatTest", String.valueOf(i), LevelEnum.values()[i], "log" + i))
            .collect(Collectors.toUnmodifiableList());
        //except 5 log, each one is from different logger
        //which means 6 lines including header row
        list.forEach(e -> {
            try {
                var req = new MockHttpServletRequest();
                var res = new MockHttpServletResponse();
                req.setContent(om.writeValueAsBytes(e));
                logsServlet.doPost(req, res);
            } catch (Exception ignored) {
            }
        });
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        servlet.doGet(request2, response2);
        var resSplit = response2.getContentAsString().split("\n");
        assertEquals(6, resSplit.length);
        String expect = "[logger\tALL\tDEBUG\tINFO\tWARN\tERROR\tFATAL\tTRACE\tOFF\t, 1\t0\t1\t0\t0\t0\t0\t0\t0\t, 2\t0\t0\t1\t0\t0\t0\t0\t0\t, 3\t0\t0\t0\t1\t0\t0\t0\t0\t, 4\t0\t0\t0\t0\t1\t0\t0\t0\t, 5\t0\t0\t0\t0\t0\t1\t0\t0\t]";
        assertEquals(expect, Arrays.toString(resSplit));
    }

}
