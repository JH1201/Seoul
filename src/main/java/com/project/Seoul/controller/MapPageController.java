package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MapPageController {

    public final HomeService homeService;

    public MapPageController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/mapPage")
    public String eventListMapPage(Model model) {
        List<CultureInfo> cultureInfoList = homeService.getAllCultureInfoApi();
        model.addAttribute("events", cultureInfoList); // 행사 정보 추가
        return "homepage/mapPage"; // 맵 페이지 템플릿 반환
    }
}
