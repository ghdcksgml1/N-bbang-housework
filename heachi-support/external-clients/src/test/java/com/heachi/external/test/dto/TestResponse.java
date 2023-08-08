package com.heachi.external.test.dto;

import java.util.Map;

public class TestResponse {
    private String result;
    private Map<String, Double> rates;

    public TestResponse() {
    }

    public TestResponse(String result, Map<String, Double> rates) {
        this.result = result;
        this.rates = rates;
    }

    public String getResult() {
        return result;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return "TestResponse{" +
                "result='" + result + '\'' +
                ", rates=" + rates +
                '}';
    }
}
