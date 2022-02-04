package com.tms.serviceImpl;

import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tms.configuration.JwtUtils;
import com.tms.util.RestTemplateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QBToken {


    HttpHeaders headers;
    HttpEntity entity;
    private String accessToken = "";
    @Autowired
    RestTemplateUtil restTemplateUtil;

    @Value("${qb.service.baseUrl}")
    public String qbBaseUrl;

    private RestTemplate restTemplate;

    public QBToken(RestTemplateBuilder restTemplateBuilder) {

        this.restTemplate = restTemplateBuilder.build();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    String username = "admin@content.com";
    String password = "Le@rn1ng11nf@NCC";
    String grant_type = "password";

    public String getUAMToken() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("grant_type", grant_type);
        entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(qbBaseUrl, entity, String.class);

        JsonObject jsonObject = new JsonParser().parse(response.getBody().toString()).getAsJsonObject();
        accessToken = jsonObject.get("accessToken").toString().replaceAll("\"", "");
        accessToken = "Bearer " + accessToken;

        return accessToken;

    }

}