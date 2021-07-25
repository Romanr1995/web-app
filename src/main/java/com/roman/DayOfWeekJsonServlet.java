package com.roman;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(name = "DayOfWeekJson", urlPatterns = "/dayOfWeekJson")
public class DayOfWeekJsonServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONParser parser = new JSONParser();

        JSONArray jsonArray;//похож на ArrayList
        try (BufferedReader r = req.getReader()) {
            jsonArray = (JSONArray) parser.parse(r);
        } catch (ParseException e) {
            throw new ServletException(e);
        }

        resp.setContentType("text/html");
        try (PrintWriter wr = resp.getWriter()) {

            for (Object o : jsonArray) {
                Request r = Request.makeFromJson((JSONObject) o);
                DayOfWeek dayOfWeek = calcDayOfWeek(r);

                RussianDayOfWeek russianDayOfWeek = RussianDayOfWeek.getByIndex(dayOfWeek.ordinal());

                wr.println(r.date + "(" + r.format +") - <b>" + russianDayOfWeek.rusName);
            }
        }
    }


    DayOfWeek calcDayOfWeek(Request r) {
        LocalDate date = LocalDate.parse(r.date, DateTimeFormatter.ofPattern(r.format));

        return date.getDayOfWeek();
    }

    public static enum RussianDayOfWeek {
        MON("Понедельник"),
        TUE("Вторник"),
        WED("Среда"),
        THU("Четверг"),
        FRI("Пятница"),
        SAT("Суббота"),
        SUN("Воскресенье");

        public final String rusName;

        RussianDayOfWeek(String rusName) {
            this.rusName = rusName;
        }

        public static RussianDayOfWeek getByIndex(int idx) {
            return values()[idx];
        }
    }

    static class Request {
        String date;
        String format;

        static Request makeFromJson(JSONObject jsonObject) {
            Request req = new Request();
            req.date = (String) jsonObject.get("date");
            req.format = (String) jsonObject.get("format");

            return req;
        }
    }
}
