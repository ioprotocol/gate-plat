package com.github.app.api;

import org.junit.Test;

import java.util.regex.Pattern;

public class RolePodomListTest {

    @Test
    public void test() throws Exception {
        String content = "/api/account/1345";
        String pattern = "/[a-zA-Z]+/account/[0-9]+";

        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println(isMatch);
    }
}
