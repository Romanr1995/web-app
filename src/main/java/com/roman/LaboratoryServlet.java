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
 *
 * Массив targetAvgDaily - это целевой средний показатель по всем эксперементам.
 * Размеры всех массивов в рамках 1го запроса равны.
 *
 * Проверить что средние по всем экспериментам превышает или равен целевом.
 *
 * Если это так то вернуть ответ: OK!
 * иначе вернуть таблицу из 3 колонок: первая порядковый номер дня в котором есть несоответствие,
 * вторая актуальное сред значение, целевое среднее значение
 *
 * отправка POST через curl:
 * curl -d "@data.json" -X POST localhost:8080/blabla
 *
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

        JSONArray jsonArray;
        try (BufferedReader r = req.getReader()){
            jsonArray = (JSONArray) parser.parse(r);
        } catch (ParseException e) {
            throw new ServletException(e);
        }

        try (PrintWriter wr = resp.getWriter()){
            for (Object o: jsonArray) {
                Request request = Request.makeFromJson((JSONObject) o);
                correctExperiment(request);

            }
        }
    }

    public static void correctExperiment(Request r) {
        double summ = 0;

        for (int i = 0; i < r.targetAvgDaily.length; i++) {
            for (int k = 0; k < r.experiments.length; k++) {
                summ+=r.experiments[k][i];
            }
            if (summ/r.experiments.length <  r.targetAvgDaily[i]) {

                System.out.println("<table style=\"width:100%\">\n" +
                        "    <tr>\n" +
                        "      <th>День с отклонением</th>\n" +
                        "      <th>Среднее значение экспериментов</th>\n" +
                        "      <th>Целевое среднее значение</th>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td>" + (i + 1) + "</td>\n" +
                        "      <td>" + summ/r.experiments.length  + "</td>\n" +
                        "      <td>" + r.targetAvgDaily[i] + "</td>\n" +
                        "    </tr>\n" +
                        " </table>");
            }
        }
        System.out.println("OK");
    }

    static class Request {
        int experiments[][];
        int targetAvgDaily[];

        static Request makeFromJson(JSONObject jsonObject) {
            Request req = new Request();
            req.experiments = (int[][]) jsonObject.get("experiments");
            req.targetAvgDaily = (int[]) jsonObject.get("targetAvgDaily");
            return req;
        }
    }
}
