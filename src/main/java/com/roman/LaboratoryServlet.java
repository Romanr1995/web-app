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
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        try {
            JSONParser parser = new JSONParser();

            JSONObject jsonObject;
            try (BufferedReader r = req.getReader()) {
                jsonObject = (JSONObject) parser.parse(r);
            } catch (ParseException e) {
                throw new ServletException(e);
            }
            resp.setContentType("text/html");
            try (PrintWriter wr = resp.getWriter()) {
                Request request = Request.makeFromJson(jsonObject);
                wr.println(correctExperiment(request));
            }

        } catch (Exception e) {
            throw new ServletException(e);
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
            int c = 0;
            int b = 0;
//            JSONObject o = (JSONObject) jsonObject.get("experiments");
            JSONArray ar = (JSONArray) jsonObject.get("targetAvgDaily");

            for (int i = 0; i < ar.toString().length(); i ++) {
                req.targetAvgDaily[i] = Double.parseDouble(ar.get(i).toString());
                c++;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("experiments");
            for (int j = 0; j < jsonArray.size(); j++) {
                String str = jsonArray.get(j).toString().substring(15);
                for (int p = 0; p < str.length(); p += 3) {
                    req.experiments[j][p] = Double.parseDouble(str.substring(p, p + 1));
                }
            }
//            Set<String> key = o.keySet();
//            for (String s : key) {
//                String a = o.get(s).toString();
//                for (int k = 0; k <= a.length(); k+=3) {
//                    req.experiments[b][k] = Double.parseDouble(a.substring(k, k + 1));
//                    b++;
//                }
//            }
            return req;
        }
    }
}
