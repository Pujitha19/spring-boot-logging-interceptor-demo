package com.demo.springbootlogingintercepter.service;


import com.demo.springbootlogingintercepter.model.soap.external.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;


public class SoapCalculatorClient extends WebServiceGatewaySupport {

    private static Logger log = LoggerFactory.getLogger(SoapCalculatorClient.class);

    public AddResponse getaddition(Add addrequest){
        Add add = new Add();
        add.setIntB(addrequest.getIntB());
        add.setIntA(addrequest.getIntA());
        AddResponse addResponse = (AddResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.dneonline.com/calculator.asmx?wsdl",add,new SoapActionCallback("http://tempuri.org/Add"));

        return addResponse;
    }
    public SubtractResponse getSubtract(Subtract subreq){
        Subtract subtract=new Subtract();
        subtract.setIntA(subreq.getIntA());
        subtract.setIntB(subreq.getIntB());
        SubtractResponse subtractResponse=(SubtractResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.dneonline.com/calculator.asmx?wsdl",subtract,new SoapActionCallback("http://tempuri.org/Subtract"));
        return  subtractResponse;
    }
    public MultiplyResponse getMultiply(Multiply multiply)
    {
        Multiply multiply1=new Multiply();
        multiply1.setIntA(multiply.getIntA());
        multiply1.setIntB(multiply.getIntB());
        MultiplyResponse multiplyResponse=(MultiplyResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.dneonline.com/calculator.asmx?wsdl",multiply1,new SoapActionCallback("http://tempuri.org/Multiply"));
        return  multiplyResponse;
    }
    public DivideResponse getDivide(Divide divide){
        Divide divide1=new Divide();
        divide1.setIntA(divide.getIntA());
        divide1.setIntB(divide.getIntB());
        DivideResponse divideResponse=(DivideResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.dneonline.com/calculator.asmx?wsdl",divide1,new SoapActionCallback("http://tempuri.org/Divide"));
        return divideResponse;
    }

//    public void MySoapClient() {
//        this.setInterceptors(new ClientInterceptor[] { new LoggingVonfig() });
//
//    }

}
