package com.roman;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(name = "JsonServlet", urlPatterns = "/json")
public class JsonServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject;
        try (BufferedReader r = req.getReader()) {
            jsonObject = (JSONObject) parser.parse(r);
        } catch (ParseException ex) {
            throw new IOException(ex);
        }

        String name = (String) jsonObject.get("name");
        Long cnt = (Long) jsonObject.get("cnt");

        System.out.println("name = " + name);
        System.out.println("cnt = " + cnt);

        System.out.println("jsonServlet");
    }
}
