package com.demo.springbootlogingintercepter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import java.util.Collections;
import java.util.List;

@Configuration
public class CalculatorConfig {


    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
//        jaxb2Marshaller.setPackagesToScan("com.example.Soap.com.example.Soap");

        jaxb2Marshaller.setContextPath("com.example.Soap.com.example.Soap"); // this will serilaize and unserialize it
        return jaxb2Marshaller;
    }

    @Bean
    public CalculatorClient calculatorClient(Jaxb2Marshaller jaxb2Marshaller){

        CalculatorClient calculatorClient = new CalculatorClient();
        calculatorClient.setDefaultUri("http://www.dneonline.com");
        calculatorClient.setMarshaller(jaxb2Marshaller);
        calculatorClient.setUnmarshaller(jaxb2Marshaller);



        return calculatorClient;
    }


}
