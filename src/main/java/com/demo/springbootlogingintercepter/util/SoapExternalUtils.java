package com.demo.springbootlogingintercepter.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Slf4j
@ResponseStatus
public class SoapExternalUtils implements ClientInterceptor {

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        StringBuilder responseBuilder = new StringBuilder();
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            messageContext.getResponse().writeTo(byteArrayOutputStream);
            String payload = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
            TransportContext transportContext = TransportContextHolder.getTransportContext();
            String urlLink = transportContext.getConnection().getUri().toString();
            URL url = new URL(urlLink);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            responseBuilder.append("\n============================================ Inbound Soap RESPONSE ==================================================================================")
                    .append("\nResponse Status: " + httpURLConnection.getResponseCode())
                    .append("\nResponse Body: " + payload)
                    .append("\n===============================================================================================================================================");

            log.info(responseBuilder.toString());

        } catch (Exception e) {
            log.error(e.getMessage());

        }

        return true;
    }


    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        StringBuilder requestBuilder = new StringBuilder();

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            messageContext.getRequest().writeTo(byteArrayOutputStream);
            String payload = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());

            TransportContext transportContext = TransportContextHolder.getTransportContext();
            String url = transportContext.getConnection().getUri().toString();
            requestBuilder.append("\n======================================== Outbound SOAP REQUEST =================================================================================")
                    .append("\nRequest Url:" + url)
                    .append("\nRequest Body: " + payload)
                    .append("\n===========================================================================================");
            log.info(requestBuilder.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        StringBuilder fault = new StringBuilder();
        fault.append("======================================== Inbound Soap Fault =================================================================================");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            messageContext.getResponse().writeTo(byteArrayOutputStream);
            String payload = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
            fault.append(payload);
            log.info(fault.toString());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return true;

    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception e) throws WebServiceClientException {

    }

    private SoapBody getSoapBody(MessageContext messageContext) {
        SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
        SoapEnvelope soapEnvelope = soapMessage.getEnvelope();
        return soapEnvelope.getBody();
    }


}
