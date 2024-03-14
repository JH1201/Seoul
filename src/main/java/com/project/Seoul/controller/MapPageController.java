package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
public class MapPageController {

    public final HomeService homeService;

    public MapPageController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/mapPage")
    public String eventListMapPage(Model model) {
        List<CultureInfo> cultureInfoList = homeService.getAllCultureInfoApiSortedByMonth();

        model.addAttribute("lists", cultureInfoList); // 행사 정보 추가

        return "homepage/mapPage"; // 맵 페이지 템플릿 반환
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
