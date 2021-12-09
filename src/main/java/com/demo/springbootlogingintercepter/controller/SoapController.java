package com.demo.springbootlogingintercepter.controller;

import com.demo.springbootlogingintercepter.model.soap.external.*;
import com.demo.springbootlogingintercepter.service.SoapCalculatorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SoapController {
    @Autowired
    private SoapCalculatorClient soapCalculatorClient;

    @PostMapping(value = "/add")
    public AddResponse addelements(@RequestBody Add add){
        return soapCalculatorClient.getaddition(add);
    }
    @PostMapping(value = "/subtract")
    public SubtractResponse addelements(@RequestBody Subtract subreq){
        return soapCalculatorClient.getSubtract(subreq);
    }
    @PostMapping(value = "/multiply")
    public MultiplyResponse addelements(@RequestBody Multiply multiply){
        return  soapCalculatorClient.getMultiply(multiply);
    }
    @PostMapping(value = "/divide")
    public DivideResponse addelements(@RequestBody Divide divide){
        return soapCalculatorClient.getDivide(divide);
    }

}
