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

        // 뷰(웹 페이지) 템플릿으로 넘길 때 사용
        model.addAttribute("title", oneCultureInfoApi.getTITLE());
        model.addAttribute("homePage", oneCultureInfoApi.getORG_LINK());
        model.addAttribute("detail", oneCultureInfoApi.getHMPG_ADDR());
        model.addAttribute("lat", oneCultureInfoApi.getLAT());
        model.addAttribute("lot", oneCultureInfoApi.getLOT());



        return "/homepage/eventInfo";
    }

}
