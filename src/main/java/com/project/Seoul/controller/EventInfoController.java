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
public class EventInfoController {

    public final HomeService homeService;

    public EventInfoController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/eventInfo")
    public String eventInfoPage(@RequestParam("title") String title,
                                Model model) {

        CultureInfo oneCultureInfoApi = homeService.getOneCultureInfoApi(title);

        // 뷰(웹 페이지) 템플릿으로 넘길 때 사용
        model.addAttribute("title", oneCultureInfoApi.getTITLE());
        model.addAttribute("homePage", oneCultureInfoApi.getORG_LINK());
        model.addAttribute("detail", oneCultureInfoApi.getHMPG_ADDR());
        model.addAttribute("lat", oneCultureInfoApi.getLAT());
        model.addAttribute("lot", oneCultureInfoApi.getLOT());
        model.addAttribute("date", oneCultureInfoApi.getDATE());
        model.addAttribute("main_img", oneCultureInfoApi.getMAIN_IMG());


        return "/homepage/eventInfo";
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
