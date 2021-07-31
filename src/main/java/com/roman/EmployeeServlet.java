package com.roman;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "EmployeeServlet", urlPatterns = "/emp/add")
public class EmployeeServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();
    private Set<Employee> db = new LinkedHashSet<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Employee employee = mapper.readValue(req.getReader(), Employee.class);
            boolean res = db.add(employee);

            if (res) {

                resp.setContentType("text/html");
                try (PrintWriter wr = resp.getWriter()) {
                    printTable(wr);
                }
            } else {
                resp.sendError(450, "Попытка добавить уже существующего пользователя!");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    void printTable(Writer wr) throws IOException {
        wr.append("<table style=\"width: auto\">")
          .append("<tr>")
          .append("<th>Имя</th>")
          .append("<th>День Рождения</th>")
          .append("<th>Должность</th>")
          .append("</tr>");

        for (Employee employee : db) {
            wr.append("<tr>")
              .append("<td>").append(employee.name).append("</td>")
              .append("<td>").append(employee.birthday.toString()).append("</td>")
              .append("<td>").append(employee.position).append("</td>")
              .append("</tr>");
        }

        wr.append("</table>");
    }

    static class Employee {
        @JsonProperty
        String name;

        @JsonProperty
        LocalDate birthday;

        @JsonProperty
        String position;

        @ConstructorProperties({"name", "birthday", "position"})
        public Employee(String name, String birthday, String position) {
            this.name = name;
            this.birthday = LocalDate.parse(birthday);
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return Objects.equals(name, employee.name) && Objects.equals(birthday, employee.birthday) && Objects.equals(position, employee.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, birthday, position);
        }
    }
}
