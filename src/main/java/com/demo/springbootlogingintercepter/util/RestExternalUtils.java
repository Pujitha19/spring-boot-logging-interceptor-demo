package com.demo.springbootlogingintercepter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;


@Component
public class RestExternalUtils implements ClientHttpRequestInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
    {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException
    {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder
                .append("\n===========================OUTBOUND REST REQUEST================================================")
                .append("\nURI         : "+ request.getURI())
                .append("\nMethod      : "+ request.getMethod())
                .append("\nHeaders     : "+ request.getHeaders())
                .append("\nRequest body: "+ new String(body, "UTF-8"))
                .append("\n===========================================================================================");
        log.info(requestBuilder.toString());

    }

    private void logResponse(ClientHttpResponse response) throws IOException
    {

        StringBuilder request = new StringBuilder();
        request
                .append("\n============================INBOUND REST RESPONSE==========================================")
                .append("\nStatus code  : "+ response.getStatusCode())
                .append("\nHeaders      : "+ response.getHeaders())
                .append("\nResponse body: "+ StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()))
                .append("\n===========================================================================================");
        log.info(request.toString());
    }
}