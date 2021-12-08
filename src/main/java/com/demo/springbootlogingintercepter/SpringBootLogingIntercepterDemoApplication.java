package com.demo.springbootlogingintercepter;

import com.demo.springbootlogingintercepter.Soap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
public class SpringBootLogingIntercepterDemoApplication {
        @Autowired
	private CalculatorClient calculatorClient;

	@PostMapping(value = "/add")
	public AddResponse addelements(@RequestBody Add add){
		return calculatorClient.getaddition(add);
	}
	@PostMapping(value = "/subtract")
	public SubtractResponse addelements(@RequestBody Subtract subreq){
		return calculatorClient.getSubtract(subreq);
	}
	@PostMapping(value = "/multiply")
	public MultiplyResponse addelements(@RequestBody Multiply multiply){
		return  calculatorClient.getMultiply(multiply);
	}
	@PostMapping(value = "/divide")
	public DivideResponse addelements(@RequestBody Divide divide){
		return calculatorClient.getDivide(divide);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootLogingIntercepterDemoApplication.class, args);
	}

}
