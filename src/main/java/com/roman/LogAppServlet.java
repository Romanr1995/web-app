package com.roman;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LogAppServlet", urlPatterns = "/logapp")
public class LogAppServlet extends HttpServlet {
    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, String> logAndPsw = new HashMap<>();


    @Override
    public void init() throws ServletException {
        logAndPsw.put("Ivan122", "Ivan1995");
        logAndPsw.put("Roman1995", "Romanr111");
        logAndPsw.put("Sergei@89", "Petrov1989");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            LoginAndPswRequest loginAndPswRequest = mapper.readValue(req.getReader(), LoginAndPswRequest.class);

            String savedPsw = logAndPsw.get(loginAndPswRequest.login);
            if (loginAndPswRequest.password != null && loginAndPswRequest.password.equals(savedPsw)) {

                resp.setContentType("text/html");
                try (PrintWriter wr = resp.getWriter()) {
                    wr.append("Пароль и логин введены верно.Вход в систему выполнен");
                }
            } else {
                resp.sendError(450, "Неверный логин или пароль");
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    static class LoginAndPswRequest {
        private String login;
        private String password;

        @ConstructorProperties({"login", "password"})
        public LoginAndPswRequest(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }
}
