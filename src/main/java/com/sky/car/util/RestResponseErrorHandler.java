package com.sky.car.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().value() != HttpServletResponse.SC_OK;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody()));
        StringBuilder sb = new StringBuilder();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        try {
            throw new Exception(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
