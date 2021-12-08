package com.demo.springbootlogingintercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;



@RestController
public class Controller
{
    @Autowired
    private RestTemplate restTemplate;

    Logger log = LoggerFactory.getLogger(Controller.class);

    private String url = "https://httpbin.org";

    @GetMapping(value="/get")
    public List<Object> getElements(){
        try {
            log.info("Getting the configuration");
            ResponseEntity<Fruits> elements = restTemplate.getForEntity(url + "/get", Fruits.class);
            return Arrays.asList(elements);
        }
        catch(ResourceAccessException e){
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping(value = "/post")
    public String postElements(Fruits fruits){
        ResponseEntity<String> frts=restTemplate.postForEntity(url+"/post",fruits,String.class);
        return frts.getBody();
    }
    @PutMapping(value = "/put")
    public List<Object> putElements(@RequestBody Fruits fruits){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Fruits> httpEntity=new HttpEntity<>(fruits,headers);
        log.info("Adding the data");
        ResponseEntity<Fruits> responseEntity=restTemplate.exchange(url+"/put", HttpMethod.PUT,httpEntity,Fruits.class);
        log.info("updating data");
        return Arrays.asList(responseEntity);
    }

    @DeleteMapping("/delete")
    public List<Object> deleteElements(Fruits fruits){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Fruits> entity = new HttpEntity<Fruits>(fruits,headers);
        ResponseEntity<Fruits> responseEntity = restTemplate.exchange(url+"/delete", HttpMethod.DELETE, entity, Fruits.class);
        System.out.println(responseEntity);
        log.info("Deleted data");
        return Arrays.asList(responseEntity);
    }


}


