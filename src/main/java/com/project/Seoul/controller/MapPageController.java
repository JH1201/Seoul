package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import com.project.Seoul.service.MapPageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class MapPageController {

    public final HomeService homeService;
    public final MapPageService mapPageService;

    public MapPageController(HomeService homeService, MapPageService mapPageService) {
        this.homeService = homeService;
        this.mapPageService = mapPageService;
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


    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<CultureInfo>> searchEvent(@RequestParam("keyword") String keyword) {
        if (keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList()); // 비어 있는 경우 빈 목록 반환
        }
        List<CultureInfo> searchResults = homeService.searchCulturalEvents(keyword);
        return ResponseEntity.ok(searchResults); // JSON 형식으로 검색 결과 반환
    }

    @GetMapping("/search2")
    @ResponseBody
    public ResponseEntity<List<CultureInfo>> searchEvent2(@RequestParam("keyword") String keyword) {
        if (keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList()); // 비어 있는 경우 빈 목록 반환
        }
        List<CultureInfo> searchResults = homeService.searchCulturalEvents(keyword);
        return ResponseEntity.ok(searchResults); // JSON 형식으로 검색 결과 반환
    }
}
