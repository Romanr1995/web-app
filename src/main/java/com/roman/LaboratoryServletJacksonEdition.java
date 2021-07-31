package com.roman;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LaboratoryServletJacksonEdition", urlPatterns = "/laboratory_jackson")
public class LaboratoryServletJacksonEdition extends HttpServlet {
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        System.out.println("LaboratoryServletJacksonEdition init");
        mapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            LaboratoryRequest labReq = mapper.readValue(req.getReader(), LaboratoryRequest.class);

            System.out.println();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }


    static class LaboratoryRequest {
        private List<Double> targetAvgDaily;
        private List<Map<String, List<Double>>> experiments;

//        @JsonCreator
//        public LaboratoryRequest(@JsonProperty("targetAvgDaily") List<Double> targetAvgDaily,
//                                 @JsonProperty("experiments") List<Map<String, List<Double>>> experiments) {
//            this.targetAvgDaily = targetAvgDaily;
//            this.experiments = experiments;
//        }

        @ConstructorProperties({"targetAvgDaily", "experiments"})
        public LaboratoryRequest(List<Double> targetAvgDaily, List<Map<String, List<Double>>> experiments) {
            this.targetAvgDaily = targetAvgDaily;
            this.experiments = experiments;
        }

        public List<Double> getTargetAvgDaily() {
            return targetAvgDaily;
        }

        public List<Map<String, List<Double>>> getExperiments() {
            return experiments;
        }
    }


}
