package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestAnRegex {

    public static void main(String[] args) {
        try {
            Pattern pattern=Pattern.compile(".*,?+");
            String line1="wambo";
            String line2="wambo,";
            Matcher matcher;

            matcher=pattern.matcher(line1);
            System.out.println("1: "+matcher.matches());

            matcher=pattern.matcher(line2);
            System.out.println("2: "+matcher.matches());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
