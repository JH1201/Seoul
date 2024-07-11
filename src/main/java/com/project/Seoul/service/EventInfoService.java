package com.project.Seoul.service;

import com.google.gson.Gson;
import com.project.Seoul.domain.AttractionsInfo;
import com.project.Seoul.domain.AttractionsWrapper;
import com.project.Seoul.repository.AttractionsRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.deepl.api.*;

@Service
public class EventInfoService {

    Translator translator;
    private final RestTemplate restTemplate;
    public final AttractionsRepository attractionsRepository;
    public EventInfoService(RestTemplate restTemplate, AttractionsRepository attractionsRepository) {
        this.restTemplate = restTemplate;
        this.attractionsRepository = attractionsRepository;
    }

    public List<String> getAttractions() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://openapi.seoul.go.kr:8088/684f6b6357776c673732424679614f/json/TbVwAttractions/1/5/", String.class);

        String jsonInput = response.getBody();

        Gson gson = new Gson();

        AttractionsWrapper wrapper = gson.fromJson(jsonInput, AttractionsWrapper.class);
        List<AttractionsInfo> attractionsInfoList = wrapper.getTbVwAttractions().getRow();

        List<String> translatedList = new ArrayList<>();

        for (AttractionsInfo attractionsInfo : attractionsInfoList) {
            translatedList.add(translator(attractionsInfo.getPOST_SJ()));
        }

        Collections.shuffle(translatedList); // 번역된 리스트를 무작위로 섞기
        return translatedList.subList(0, Math.min(5, translatedList.size())); // 최대 5 반환
    }


    public void saveAttractions(List<AttractionsInfo> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        attractionsRepository.saveAll(list);
    }



    public String translator(String text) throws Exception{
        String authKey = "44d80e23-670e-48b6-a67c-3bd4d34e2003:fx";  // Replace with your key
        translator = new Translator(authKey);
        TextResult result =
                translator.translateText(text, null, "ko");

        return result.getText();
    }



    /*
    // 주소를 위도, 경도로 바꾸는 함수
    public String convertAddressToCoordinates(String address) {
        String url = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", address)
                .encode() // URL 인코딩 추가
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK 641a21f1a0b966ba38aa511de98f3755");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        System.out.println("URL being requested: " + url.toString());
        System.out.println("Headers: " + headers.toString());
        System.out.println("Response: " + response.getBody());

        return response.getBody();
    }
     */



}
