package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        System.out.println(oneCultureInfoApi.getLAT());
        System.out.println(oneCultureInfoApi.getLOT());

        // 뷰(웹 페이지) 템플릿으로 넘길 때 사용
        model.addAttribute("title", oneCultureInfoApi.getTITLE());
        model.addAttribute("homePage", oneCultureInfoApi.getORG_LINK());
        model.addAttribute("detail", oneCultureInfoApi.getHMPG_ADDR());
        model.addAttribute("lat", oneCultureInfoApi.getLAT());
        model.addAttribute("lot", oneCultureInfoApi.getLOT());



        return "/homepage/eventInfo";
    }

    @GetMapping("/search")
    public String searchEvent(@RequestParam("keyword") String keyword, Model model) {
        if (keyword.trim().isEmpty()) {
            return "redirect:/home"; // 비어있다면 홈으로 리디렉션
        }
        List<CultureInfo> searchResults = homeService.searchCulturalEvents(keyword);
        model.addAttribute("searchResults", searchResults);
        return "/homepage/eventListPage";
    }

}
