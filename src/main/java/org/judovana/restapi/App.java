package org.judovana.restapi;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.Date;
import org.judovana.restapi.beans.Loan;
import org.judovana.restapi.connection.Provider;

public class App {

    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        if (args.length != 1) {
            throw new RuntimeException("Expected exactly one argument - URL of api");
        }
        new App(args[0], 5, System.out).pool();
    }

    public void pool() throws InterruptedException, IOException {
        Db db = new Db();
        //exercise - 5 minutes; but check last 6 minutes and check against db as there oculd be loan in the few milisicends the request took
        while (true) {
            Provider provider = Provider.create(baseUrl, delay + 1);
            Loan[] loans = provider.readLoans();
            System.out.println(new Date()+": "+loans.length+" (unfliltered)");
            for (Loan loan : loans) {
                if (!db.contains(loan)) {
                    db.add(loan);
                    out.println(loan);
                }
            }
            Thread.sleep(delay * 60 * 1000);
        }
    }
    private final String baseUrl;
    private final int delay;
    private final PrintStream out;

    private App(String arg, int delayIniMinutes, PrintStream out) {
        this.baseUrl = arg;
        this.delay = delayIniMinutes;
        this.out = out;
    }
}
