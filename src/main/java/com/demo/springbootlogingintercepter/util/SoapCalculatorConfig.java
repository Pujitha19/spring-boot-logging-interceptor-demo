package com.demo.springbootlogingintercepter.util;

import com.demo.springbootlogingintercepter.configurations.RestExternalConfig;
import com.demo.springbootlogingintercepter.configurations.SoapExternalConfig;
import com.demo.springbootlogingintercepter.service.SoapCalculatorClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class SoapCalculatorConfig {


    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
//        jaxb2Marshaller.setPackagesToScan("com.example.Soap.com.example.Soap");

        jaxb2Marshaller.setContextPath("com.demo.springbootlogingintercepter.model.Soap.External"); // this will serilaize and unserialize it
        return jaxb2Marshaller;
    }

    @Bean
    public SoapCalculatorClient calculatorClient(Jaxb2Marshaller jaxb2Marshaller){

        SoapCalculatorClient soapCalculatorClient = new SoapCalculatorClient();
        soapCalculatorClient.setDefaultUri("http://www.dneonline.com");
        soapCalculatorClient.setMarshaller(jaxb2Marshaller);
        soapCalculatorClient.setUnmarshaller(jaxb2Marshaller);
        soapCalculatorClient.setInterceptors(new ClientInterceptor[] {new SoapExternalConfig()});



        return soapCalculatorClient;
    }


}
