package com.github.app.api;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.github.cage.YCage;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

public class CageTest {

    @Test
    public void test() throws IOException {
        generate(new GCage(), 10, "F:\\cg1", ".jpg", "co28");
        generate(new YCage(), 10, "F:\\cy1", ".jpg", "eT6wLAH");
        generate(new GCage(), 100, "F:\\cg2", ".jpg", null);
        generate(new YCage(), 100, "F:\\cy2", ".jpg", null);
    }

    protected static void generate(Cage cage, int num, String namePrefix,
                                   String namePostfix, String text) throws IOException {
        for (int fi = 0; fi < num; fi++) {
            OutputStream os = new FileOutputStream(namePrefix + fi
                    + namePostfix, false);
            try {
                cage.draw(
                        text != null ? text : cage.getTokenGenerator().next(),
                        os);
            } finally {
                os.close();
            }
        }
    }

    @Test
    public void test1() {
        String content1 = "/api/123";
        String content2 = "/open/123";
        String content3 = "/static/123";
        String content4 = "https://localhost:9258";

        String pattern = "^(?!/static).*";
        String pattern1 = "http.*";

        System.out.println(Pattern.matches(pattern, content1));
        System.out.println(Pattern.matches(pattern, content2));
        System.out.println(Pattern.matches(pattern, content3));
        System.out.println(Pattern.matches(pattern1, content4));
    }
}
