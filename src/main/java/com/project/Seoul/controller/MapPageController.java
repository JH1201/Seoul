package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.repository.EventsRepository;
import com.project.Seoul.service.HomeService;
import com.project.Seoul.service.MapPageService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MapPageController {

    public final HomeService homeService;
    public final MapPageService mapPageService;

    public final EventsRepository repository;

    public MapPageController(HomeService homeService, MapPageService mapPageService, EventsRepository repository) {
        this.homeService = homeService;
        this.mapPageService = mapPageService;
        this.repository = repository;
    }

    @GetMapping("/mapPage")
    public String eventListMapPage(Model model) {

        List<CultureInfo> cultureInfoList = homeService.getAllCultureInfoApiSortedByMonth();

        model.addAttribute("lists", cultureInfoList); // 행사 정보 추가

        /*
        //37.53681, 126.9268
        //37.59586, 127.0207
        Double swLat = 37.53681;
        Double swLng = 126.9268;
        Double neLat = 37.59586;
        Double neLng = 127.0207;

        List<CultureInfo> filteredEvents = mapPageService.filterEventsByBounds(swLat, swLng, neLat, neLng);

         */

        return "homepage/mapPage"; // 맵 페이지 템플릿 반환
    }


    // 지도에 있는 행사 리스트 출력
    @PostMapping("/filterEvents")
    @ResponseBody
    public List<CultureInfo> filterEvents(@RequestBody FilterRequest requestData) {
        List<CultureInfo> markersInfoList = requestData.getMarkersInfo();
        Map<String, Double> bounds = requestData.getBounds();
        return mapPageService.filterEventsByBounds(markersInfoList, bounds);
    }

    @Getter
    @Setter
    public static class FilterRequest {
        private List<CultureInfo> markersInfo;
        private Map<String, Double> bounds;

        // getters and setters
    }

    @GetMapping("/mapsearchEvents")
    public ResponseEntity<List<CultureInfo>> searchEvents(
            @RequestParam String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam List<String> eventTypes,
            @RequestParam List<String> locations) {

        List<CultureInfo> events = homeService.comprehensiveSearch(keyword, startDate, endDate, eventTypes, locations);
        return ResponseEntity.ok(events);
    }


}
