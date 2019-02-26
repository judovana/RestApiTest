package org.judovana.restapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import org.judovana.restapi.beans.Loan;
import org.judovana.restapi.connection.Provider;

public class App {
    
    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        if (args.length != 1) {
            throw new RuntimeException("Expected exactly one argument - URL of api");
        }
        Db db = new Db();
        //exercise - 5 minutes; but check last 6 minutes and check against db as there oculd be loan in the few milisicends the request took
        while (true) {
            Provider provider = Provider.create(args[0], 6);
            Loan[] loans = provider.readLoans();
            System.out.println(new Date());
            for (Loan loan : loans) {
                if (!db.contains(loan)) {
                    db.add(loan);
                    System.out.println(loan);
                }
            }
            Thread.sleep(5 * 60 * 1000);
        }
    }
}
