package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("title", oneCultureInfoApi.getTITLE());
        model.addAttribute("lat", oneCultureInfoApi.getLAT());
        model.addAttribute("lot", oneCultureInfoApi.getLOT());


        return "/homepage/eventInfo";
    }

}
