package com.project.Seoul.service;

import com.google.gson.Gson;
import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.domain.SubwayInfo;
import com.project.Seoul.domain.SubwayWrapper;
import com.project.Seoul.repository.EventsRepository;
import com.project.Seoul.repository.SubwayRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapPageService {

    public final EventsRepository eventsRepository;
    public final SubwayRepository subwayRepository;
    public final HomeService homeService;

    public MapPageService(EventsRepository eventsRepository, SubwayRepository subwayRepository, HomeService homeService) {
        this.eventsRepository = eventsRepository;
        this.subwayRepository = subwayRepository;
        this.homeService = homeService;
    }

    public List<SubwayInfo> getSubway() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://openapi.seoul.go.kr:8088/5441476e45776c673630537351474b/json/tbTraficElvtr/1/5/", String.class);

        String jsonInput = response.getBody();
        System.out.println("jsonInput = " + jsonInput);
        
        Gson gson = new Gson();

        SubwayWrapper wrapper = gson.fromJson(jsonInput, SubwayWrapper.class);
        List<SubwayInfo> subwayInfoList = wrapper.getTbTraficElvtr().getRow();

        return subwayInfoList;
    }

    public void saveParkingArea(SubwayInfo subwayInfo) {
        subwayRepository.save(subwayInfo);
    }

    public List<CultureInfo> filterEventsByBounds(List<CultureInfo> markersInfo, Map<String, Double> bounds) {
        if (bounds.get("southWestLat") == null || bounds.get("southWestLng") == null ||
                bounds.get("northEastLat") == null || bounds.get("northEastLng") == null) {
            return Collections.emptyList();
        }
        double southWestLat = bounds.get("southWestLat");
        double southWestLng = bounds.get("southWestLng");
        double northEastLat = bounds.get("northEastLat");
        double northEastLng = bounds.get("northEastLng");

        return markersInfo.stream()
                .filter(cultureInfo -> {
                    try {
                        double lng = Double.parseDouble(cultureInfo.getLAT().trim());
                        double lat = Double.parseDouble(cultureInfo.getLOT().trim());
                        return lat >= southWestLat && lat <= northEastLat && lng >= southWestLng && lng <= northEastLng;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }



}
