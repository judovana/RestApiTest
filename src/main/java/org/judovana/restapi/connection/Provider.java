/*
 * The MIT License
 *
 * Copyright 2019 jvanek.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.judovana.restapi.connection;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.judovana.restapi.beans.Loan;

/**
 *
 * @author jvanek
 */
public class Provider {

    private final URL api;

    private Provider(URL api) {
        this.api = api;
    }

    public static Provider create(String from, int ageInMinutes) throws MalformedURLException {
        //provider is only for interesting fields
        return new Provider(new URL(from + "?datePublished__gt=" + getAge(ageInMinutes) + "&fields=id,datePublished"));
    }

    public Loan[] readLoans() throws IOException {
        //no paging via cool
        //X-Page: N and X-Size: KL 
        //by cool answer of X-Total: XY
        //as I have never seen more then three loans appearing in last five minutes
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(api.openStream(), Charset.forName("utf-8")))) {
            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                sb.append(s);
            }
        }
        Gson gson = new Gson();
        Loan[] loans = gson.fromJson(sb.toString(), Loan[].class);
        return loans;
    }

    static String getAge(int ageInMinutes) {
        return getAge(LocalDateTime.now(), ageInMinutes);
    }

    static String getAge(LocalDateTime base, int ageInMinutes) {
        //lets expect our api provider is in our timezone for simplicity
        LocalDateTime oldTime = base.minusMinutes(ageInMinutes);
        return DateTimeFormatter.ISO_DATE_TIME.format(oldTime);
    }

}
