package com.roman;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.DayOfWeek;

public class DayWeek extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        String dayEnglish = date.getDayOfWeek().toString();

        String dayRussian;

        switch (dayEnglish) {
            case "MONDAY":
                dayRussian = "Понедельник";break;
            case "TUESDAY":
                dayRussian = "Вторник";break;
            case "WEDNESDAY":
                dayRussian = "Среда";break;
            case "THURSDAY":
                dayRussian = "Четверг";break;
            case "FRIDAY":
                dayRussian = "Пятница";break;
            case "SATURDAY":
                dayRussian = "Суббота";break;
            case "SUNDAY":
                dayRussian = "Воскресенье";break;
            default:
                throw new RuntimeException();
        }
        resp.setContentType("text/html");
        try (final PrintWriter wr = resp.getWriter()) {
            wr.println("День недели " + date + " - " + dayRussian);
        }
    }

}
