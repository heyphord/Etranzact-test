/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmfs.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmfs.demo.dto.LoginRequestDTO;
import com.gmfs.demo.dto.DemoResponseDto;
import com.gmfs.demo.dto.UserDTO;
import com.gmfs.demo.service.DemoService;
import com.google.gson.Gson;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("api/v1")
public class DemoController {




    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(
            @RequestBody LoginRequestDTO loginRequestDTO

    ) {


        return ResponseEntity.ok( loginRequestDTO);


    }

    @RequestMapping(path = "/populate-db", method = RequestMethod.POST)
    public ResponseEntity<Object> populateDB() {



       String response = WebClient.builder().build()
                .get()
                .uri("https://jsonplaceholder.typicode.com/users")
                .retrieve()
                .bodyToMono(String.class)
                .block();


        Gson g = new Gson();
        UserDTO userDTO1 = g.fromJson(response, UserDTO.class);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "/get-gb", method = RequestMethod.GET)
    public ResponseEntity<Object> getRecords() {


        return new ResponseEntity<>("Get db api working", HttpStatus.OK);
    }

    /**
     * Demo controller with Request and Response DTO (Data transfer object)
     */
    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public ResponseEntity<Object> demo2(@RequestBody LoginRequestDTO request) {


        DemoResponseDto responseDto = new DemoResponseDto();
        responseDto.setValue("Response from DTO");

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * Demo controller with reusable service
     */
    @Autowired
    DemoService demoService;
    @RequestMapping(path = "/test-with-service", method = RequestMethod.GET)
    public ResponseEntity<Object> demo3() {


        String resultsFromService = demoService.reusableServiceMethod();


        return new ResponseEntity<>(resultsFromService, HttpStatus.OK);
    }


}
