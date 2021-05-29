package nz.ac.wgtn.swen301.a3.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGetLogs {
    private LogsServlet servlet;

    @BeforeEach
    void setupServlet() {
        servlet = new LogsServlet();
    }

    @AfterEach
    void clearServlet() {
        servlet = null;
    }

    @Test
    void test_noParameters() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    void test_withInvalidParameters() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameters(Map.of("limit", "5", "randomKey", "randomValue"));
        servlet.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    void test_withValidParameters1() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameters(Map.of("limit", "5", "level", "ALL"));
        servlet.doGet(request, response);
        assertEquals(MockHttpServletResponse.SC_OK, response.getStatus());
        assertEquals("[]\n", response.getContentAsString());
        //expect empty list
    }

    @Test
    void test_withValidParameters2() throws Exception {
        String body = "  {\n" +
            "    \"id\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\",\n" +
            "    \"message\": \"application started\",\n" +
            "    \"timestamp\": \"04-05-2021 10:12:00\",\n" +
            "    \"thread\": \"main\",\n" +
            "    \"logger\": \"com.example.Foo\",\n" +
            "    \"level\": \"DEBUG\",\n" +
            "    \"errorDetails\": \"string\"\n" +
            "  }";
        ObjectMapper om = new ObjectMapper();
        var list = IntStream.range(0, 3).mapToObj(i -> new LogEvent("message" + i, "thread", "test_withValidParameters2", LevelEnum.INFO, "detail")).collect(Collectors.toCollection(ArrayList::new));
        list.forEach(e -> {
            var req = new MockHttpServletRequest();
            var res = new MockHttpServletResponse();
            try {
                req.setContent(om.writeValueAsBytes(e));
                servlet.doPost(req, res);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        });
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        request.addParameters(Map.of("limit", "5", "level", "ALL"));
        servlet.doGet(request, response);
        assertEquals(200, response.getStatus());
        var newList = om.readValue(response.getContentAsString(), List.class);
        assertEquals(3, newList.size());
    }
}
