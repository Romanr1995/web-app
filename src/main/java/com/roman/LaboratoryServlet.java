package com.roman;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * приходит запрос с экспериментальными данными
 * see laboratoryExample.json
 * в experiments - данные по разным экспериментами в примере их коды - experiment1,experiment2 и т.д.
 * Массив чисел - это показатели в разные дни (1,2 и т.д)
 * <p>
 * Массив targetAvgDaily - это целевой средний показатель по всем эксперементам.
 * Размеры всех массивов в рамках 1го запроса равны.
 * <p>
 * Проверить что средние по всем экспериментам превышает или равен целевом.
 * <p>
 * Если это так то вернуть ответ: OK!
 * иначе вернуть таблицу из 3 колонок: первая порядковый номер дня в котором есть несоответствие,
 * вторая актуальное сред значение, целевое среднее значение
 * <p>
 * отправка POST через curl:
 * curl -d "@data.json" -X POST localhost:8080/blabla
 * <p>
 * ссылка на тренер HTML:
 * https://www.w3schools.com/html/tryit.asp?filename=tryhtml_table
 * пример таблицы:
 *
 * <table style="width:100%">
 *   <tr>
 *     <th>Товар</th>
 *     <th>Кол-во</th>
 *     <th>Цена</th>
 *   </tr>
 *   <tr>
 *     <td>Яблоки</td>
 *     <td>10кг</td>
 *     <td>100</td>
 *   </tr>
 *   <tr>
 *     <td>Бананы</td>
 *     <td>1кг</td>
 *     <td>30</td>
 *   </tr>
 * </table>
 */
@WebServlet(name = "Laboratory", urlPatterns = "/laboratory")
public class LaboratoryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject;
        try (BufferedReader r = req.getReader()) {
            jsonObject = (JSONObject) parser.parse(r);
        } catch (ParseException e) {
            throw new ServletException(e);
        }
        resp.setContentType("text/html");
        try (PrintWriter wr = resp.getWriter()) {
            Request request = Request.makeFromJson((JSONObject) jsonObject);
            wr.println(correctExperiment(request));

        }
    }

    public static String correctExperiment(Request r) {
        double sum = 0;

        for (int i = 0; i < r.targetAvgDaily.length; i++) {
            for (int k = 0; k < r.experiments.length; k++) {
                sum += r.experiments[k][i];
            }
            if (sum < r.targetAvgDaily[i]) {
                return "<table style=\"width:100%\">\n" +
                        "    <tr>\n" +
                        "      <th>День с отклонением</th>\n" +
                        "      <th>Среднее значение экспериментов</th>\n" +
                        "      <th>Целевое среднее значение</th>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td>" + (i + 1) + "</td>\n" +
                        "      <td>" + sum / r.experiments.length + "</td>\n" +
                        "      <td>" + r.targetAvgDaily[i] + "</td>\n" +
                        "    </tr>\n" +
                        " </table>";
            }
        }
        return "OK";
    }

    static class Request {
        Double[][] experiments;
        Double[] targetAvgDaily;

        static Request makeFromJson(JSONObject jsonObject) {
            Request req = new Request();
            req.experiments = (Double[][]) jsonObject.get("experiments");
            req.targetAvgDaily = (Double[]) jsonObject.get("targetAvgDaily");
            return req;
        }
    }
}
