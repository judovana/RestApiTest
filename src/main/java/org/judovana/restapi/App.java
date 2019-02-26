package org.judovana.restapi;

import java.io.IOException;
import java.net.MalformedURLException;
import org.judovana.restapi.connection.Provider;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws MalformedURLException, IOException {
        if (args.length != 1) {
            throw new RuntimeException("Expected exactly one argument - URL of api");
        }
        Provider provider = Provider.create(args[0]);
        provider.dummyAttempt();
    }
}
