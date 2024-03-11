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

    //문화행사 정보 api
    public List<CultureInfo> getAllCultureInfoApi() {

        //RestTemplate응 이용해 api 받아오는 방법
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://openapi.seoul.go.kr:8088/5a6f416d79776c6735304c6142424e/json/culturalEventInfo/1/50", String.class);

        String jsonInput = response.getBody();

        Gson gson = new Gson();

        CulturalEventInfoWrapper wrapper = gson.fromJson(jsonInput, CulturalEventInfoWrapper.class);

        //받아온 api데이터(json형식)의 row 부분을 CultureInfo 객체로 만들어서 리스트에 저장
        //curtureInfoList == 받아온 문화행사 정보들의 객체
        List<CultureInfo> curtureInfoList = wrapper.getCulturalEventInfo().getRow();

        return curtureInfoList;
    }


    //모든 문화행사 정보 중에 title이 같은 행사를 찾는 함수
    public CultureInfo getOneCultureInfoApi(String title) {
        // 모든 문화 정보를 가져온다.
        List<CultureInfo> cultureInfoList = getAllCultureInfoApi();

        // 리스트를 순회하면서 homePage와 일치하는 객체를 찾는다.
        for (CultureInfo cultureInfo : cultureInfoList) {
            if (cultureInfo.getTITLE().equals(title)) {

                // 일치하는 객체를 찾았으므로 반환한다.
                return cultureInfo;
            }
        }

        // 일치하는 객체를 찾지 못했으므로 null 반환 또는 예외 처리
        return null; // 혹은 적절한 예외를 발생시키기
    }


}

