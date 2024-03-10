package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    public final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public String homepage(Model model) {
        List<CultureInfo> list = homeService.getAllCultureInfoApi();

        model.addAttribute("list", list);
        return "/homepage/home";
    }

}
