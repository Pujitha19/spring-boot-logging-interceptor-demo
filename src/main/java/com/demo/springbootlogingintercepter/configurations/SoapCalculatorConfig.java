package com.demo.springbootlogingintercepter.configurations;

import com.demo.springbootlogingintercepter.service.SoapCalculatorClient;
import com.demo.springbootlogingintercepter.util.SoapExternalUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class SoapCalculatorConfig {


    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.demo.springbootlogingintercepter.model.soap.external"); // this will serilaize and unserialize it
        return jaxb2Marshaller;
    }

    @Bean
    public SoapCalculatorClient calculatorClient(Jaxb2Marshaller jaxb2Marshaller){

        SoapCalculatorClient soapCalculatorClient = new SoapCalculatorClient();
        soapCalculatorClient.setDefaultUri("http://www.dneonline.com");
        soapCalculatorClient.setMarshaller(jaxb2Marshaller);
        soapCalculatorClient.setUnmarshaller(jaxb2Marshaller);
        soapCalculatorClient.setInterceptors(new ClientInterceptor[] {new SoapExternalUtils()});



        return soapCalculatorClient;
    }


}
