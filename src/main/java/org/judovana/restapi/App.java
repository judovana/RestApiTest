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
        new App(args[0], 5 * 60, System.out).pool(-1);
    }

    /**
     * pools the service loops-times. You can pass negative number to pool for ever
     */
    public void pool(int loops) throws InterruptedException, IOException {
        Db db = new Db();
        //exercise - 5 minutes; but check last 6 minutes and check against db as there oculd be loan in the few milisicends the request took
        while (loops < 0 || loops > 0) {
            if (loops > 0) {
                loops--;
            }
            Provider provider = Provider.create(baseUrl, delay + 60);
            Loan[] loans = provider.readLoans();
            out.println(new Date() + ": " + loans.length + " (unfliltered)");
            out.println("New items: ");
            for (Loan loan : loans) {
                if (!db.contains(loan)) {
                    db.add(loan);
                    out.println(loan);
                }
            }
            Thread.sleep(delay * 1000);
        }
    }
    private final String baseUrl;
    private final int delay;
    private final PrintStream out;

    App(String arg, int delayInSeconds, PrintStream out) {
        this.baseUrl = arg;
        this.delay = delayInSeconds;
        this.out = out;
    }
}
