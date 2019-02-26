package org.judovana.restapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import org.judovana.restapi.beans.Loan;
import org.judovana.restapi.connection.Provider;

public class App {

    public static void main(String[] args) throws MalformedURLException, IOException {
        if (args.length != 1) {
            throw new RuntimeException("Expected exactly one argument - URL of api");
        }
        Provider provider = Provider.create(args[0]);
        Loan[] loans = provider.readLoans();
        System.out.println(Arrays.toString(loans));
    }
}
