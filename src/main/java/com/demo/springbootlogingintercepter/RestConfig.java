package com.demo.springbootlogingintercepter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Configuration
public class RestConfig {



@Bean
 public RestTemplate restTemplate(){
 RestTemplate restTemplate;
 return new RestTemplate();
 }

}
