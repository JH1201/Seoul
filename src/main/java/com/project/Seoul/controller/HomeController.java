package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    public final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping({"/home"})
    public String homepage(Model model) {
        List<CultureInfo> list = homeService.getAllCultureInfoApi();

        model.addAttribute("lists", list);
        return "/homepage/home";
    }

    // HomeController.java 또는 적절한 컨트롤러 파일 내에 추가
    @GetMapping("/mapPage")
    public String allEvents(Model model) {
        List<CultureInfo> allEvents = homeService.getAllCultureInfoApi(); // 모든 문화행사를 가져오는 서비스 메서드
        model.addAttribute("searchResults", allEvents);
        return "homepage/mapPage"; // 모든 문화행사를 포함한 mapPage를 반환
    }
}
