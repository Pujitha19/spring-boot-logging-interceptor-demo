package com.demo.springbootlogingintercepter.configurations;

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
public class SoapExternalConfig implements ClientInterceptor {


        @Override
        public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
            StringBuilder requestbuilder = new StringBuilder();
                       try {

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                messageContext.getResponse().writeTo(buffer);
                String payload = buffer.toString(StandardCharsets.UTF_8.name());
                TransportContext context = TransportContextHolder.getTransportContext();
                String url =context.getConnection().getUri().toString();
                URL obj=new URL(url);
                HttpURLConnection connection=(HttpURLConnection)obj.openConnection();

                requestbuilder.append("\n============================================ Inbound Soap RESPONSE ==================================================================================")
                        .append("\nResponse Status: "+ connection.getResponseCode())
                        .append("\nResponse Body: "+payload)
                        .append("\n===============================================================================================================================================");

                log.info(requestbuilder.toString());

            } catch (IOException |URISyntaxException e) {
                throw new WebServiceClientException("Can not write the SOAP response into the out stream", e) {

                    private static final long serialVersionUID = -7118480620416458069L;
                };


            }

            return true;
        }
        

        @Override
        public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
            StringBuilder requestbuild  = new StringBuilder();

            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                messageContext.getRequest().writeTo(buffer);
                String payload = buffer.toString(StandardCharsets.UTF_8.name());

                TransportContext context = TransportContextHolder.getTransportContext();
                String url =context.getConnection().getUri().toString();
                requestbuild.append("\n======================================== Outbound SOAP REQUEST =================================================================================")
                        .append("\nRequest Url:"+url)
                        .append("\nRequest Body: "+payload)
                        .append("\n===========================================================================================");
                log.info(requestbuild.toString());
            } catch (IOException |URISyntaxException e) {
                throw new WebServiceClientException("Can not write the SOAP request into the out stream", e) {
                    private static final long serialVersionUID = -7118480620416458069L;
                };
            }

            return true;
        }

        @Override
        public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
            log.info("======================================== Inbound Soap Fault =================================================================================");
            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                messageContext.getResponse().writeTo(buffer);
                String payload = buffer.toString(StandardCharsets.UTF_8.name());

            } catch (IOException e) {
                throw new WebServiceClientException("Can not write the SOAP fault into the out stream", e) {
                    private static final long serialVersionUID = 3538336091916808141L;
                };
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
