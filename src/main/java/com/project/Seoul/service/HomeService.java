package com.project.Seoul.service;

import com.google.gson.Gson;
import com.project.Seoul.domain.CulturalEventInfoWrapper;
import com.project.Seoul.domain.CultureInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HomeService {
    public List<CultureInfo> getAllCultureInfoApi() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://openapi.seoul.go.kr:8088/5a6f416d79776c6735304c6142424e/json/culturalEventInfo/1/20", String.class);

        String jsonInput = response.getBody();

        Gson gson = new Gson();

        CulturalEventInfoWrapper wrapper = gson.fromJson(jsonInput, CulturalEventInfoWrapper.class);
        List<CultureInfo> curtureInfoList = wrapper.getCulturalEventInfo().getRow();


        return curtureInfoList;
    }
}

