package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import com.project.Seoul.service.MapPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("lists", cultureInfoList);

        return "homepage/mapPage"; // 맵 페이지 템플릿 반환
    }


    // 지도에 있는 행사 리스트 출력
    @PostMapping("/filterEvents")
    @ResponseBody
    public List<CultureInfo> filterEvents(@RequestBody Map<String, Double> bounds) {
        // bounds에서 지도의 경계 값(남서쪽 위도, 남서쪽 경도, 북동쪽 위도, 북동쪽 경도)를 받아옵니다.
        // 여기서는 예시로 bounds.get("southWestLat") 등의 메서드를 호출한다고 가정합니다.
        // 실제로는 bounds 맵에서 해당 값들을 얻어와야 합니다.

        double southWestLat = bounds.get("southWestLng");
        double southWestLng = bounds.get("southWestLat");
        double northEastLat = bounds.get("northEastLng");
        double northEastLng = bounds.get("northEastLat");

        // 행사 리스트를 필터링하는 로직을 구현합니다.
        List<CultureInfo> filteredEvents = mapPageService.filterEventsByBounds(
                southWestLat, southWestLng, northEastLat, northEastLng);

        // 필터링된 행사 리스트를 반환합니다.
        return filteredEvents;
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
    public String searchEvent2(@RequestParam("keyword") String keyword, Model model) {
        if (keyword.trim().isEmpty()) {
            return "redirect:/mapPage"; // 비어있다면 mapPage으로 리디렉션
        }
        List<CultureInfo> searchResults = homeService.searchCulturalEvents(keyword);
        model.addAttribute("searchResults", searchResults);
        return "/homepage/mapPage";
    }
}
