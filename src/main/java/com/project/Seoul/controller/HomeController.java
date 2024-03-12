package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    public final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public String homepage(Model model) {

        List<CultureInfo> list = homeService.getAllCultureInfoApiSortedByMonth();

        for (CultureInfo cultureInfo : list) {
            homeService.saveEvents(cultureInfo);

        }

        model.addAttribute("lists", list);
        return "/homepage/home";
    }


    @PostMapping("/home")
    public ResponseEntity<?> saveFavoriteEvent(@RequestBody Map<String, Long> payload) {
        Long cultureInfoId = payload.get("cultureInfoId");


        // ID를 사용하여 cultureInfo를 찾고, 이벤트를 저장하는 로직을 수행합니다.
        homeService.findAndSaveEventById(cultureInfoId);

        return ResponseEntity.ok().build();
    }

}
