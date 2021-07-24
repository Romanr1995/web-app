package com.roman;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//?a=12&b=10&op=*
//-> a * b = 120
public class CalculatorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double a = Double.parseDouble(req.getParameter("a"));
        double b = Double.parseDouble(req.getParameter("b"));
        Op op = Op.getByOpCode(req.getParameter("op"));

        double res;
        switch (op) {
            case PLUS:
                res = a + b; break;
            case MINUS:
                res = a - b; break;
            case DIV:
                res = a / b; break;
            case MULT:
                res = a * b; break;
            default:
                throw new RuntimeException();
        }

        try (final PrintWriter wr = resp.getWriter()) {
            wr.println("a " + op.opCode +  " b = " + res);
        }
    }


    private static enum Op {
        PLUS("+"), MINUS("-"), MULT("*"), DIV("/");

        public final String opCode;

        Op(String opCode) {
            this.opCode = opCode;
        }

        static Op getByOpCode(String code) {
            for (Op value : values()) {
                if (code.equals(value.opCode)) {
                    return value;
                }
            }

            throw new RuntimeException("wrong op code :" + code);
        }
    }
}
