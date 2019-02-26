package org.judovana.restapi;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import org.judovana.restapi.App;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static class TestServer implements HttpHandler {

        private final HttpServer hs;
        private final int port;

        public TestServer() throws IOException {
            ServerSocket s = new ServerSocket(0);
            port = s.getLocalPort();
            s.close();
            hs = HttpServer.create(new InetSocketAddress(port), 0);
            hs.createContext("/", this);
            hs.start();
        }

        private int counter = 0;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            counter++;
            String answer = "["
                    + "{\"id\":" + (counter / 2) + ",\"datePublished\":\"" + DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()) + "\"},"
                    + "{\"id\":" + ((counter * 100) / 5) + ",\"datePublished\":\"" + DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()) + "\"}"
                    + "]";
            byte[] result = answer.getBytes(Charset.forName("utf-8"));
            long size = answer.length(); //yahnot perfect, ets assuemno one will use this on chinese chars
            exchange.sendResponseHeaders(200, size);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(result);
            }
        }
    }

    @Test
    public void integration() throws IOException, InterruptedException {
        TestServer s = new TestServer();
        ByteArrayOutputStream answers = new ByteArrayOutputStream();
        App app = new App("http://localhost:" + s.port, 1, new PrintStream(answers));
        app.pool(10);
        System.out.println(new String(answers.toByteArray(), Charset.forName("utf-8")));

    }
}
