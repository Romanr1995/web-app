package com.roman;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class Servlet2 implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("service");
        try (final PrintWriter wr = servletResponse.getWriter()) {
            wr.println("<b>Hello servet2");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet2";
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
