package com.roman.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDemo {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        {
            Data data = new Data("Foo", 42, new BigInteger("8657548754575456"), "foo", "bar", "qwe");

            String jsonString = mapper.writeValueAsString(data);

            System.out.println(jsonString);
        }
        {
            String jsonString = "{\n" +
                                "  \"name\" : \"Ivan\",\n" +
                                "  \"number\": \"33\",\n" +
                                "  \"strings\": [\"a\", \"b\"]\n" +
                                "}\n";

            Data data = mapper.readValue(jsonString, Data.class);

            System.out.println();
        }
    }


    static class Data {
        String name;
        int number;
        BigInteger bigNumber;
        List<String> strings = new ArrayList<>();

        public Data() {

        }
        public Data(String name, int number, BigInteger bigNumber, String ... strings) {
            this.name = name;
            this.number = number;
            this.bigNumber = bigNumber;
            this.strings.addAll(Arrays.asList(strings));
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public BigInteger getBigNumber() {
            return bigNumber;
        }

        public List<String> getStrings() {
            return strings;
        }
    }
}
