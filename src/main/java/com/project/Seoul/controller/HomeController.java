package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.domain.FavoriteCultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.HashMap;
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

        List<CultureInfo> items = homeService.getAllCultureInfoApi(); // API로부터 아이템들을 가져오는 메서드
        Collections.shuffle(items); // 아이템들을 랜덤하게 섞는다
        List<CultureInfo> selectedItems = items.subList(0, 5); // 상위 5개 아이템을 선택
        model.addAttribute("items", selectedItems); // 모델에 아이템들을 추가
        model.addAttribute("lists", list);

        /*
        List<FavoriteCultureInfo> allFavoriteEvents = homeService.getAllFavoriteEvents();

        // list 내의 각 `CultureInfo` 객체에 대해 `allFavoriteEvents`와 비교하여 isFavorite 설정
        for (CultureInfo cultureInfo : list) {
            for (FavoriteCultureInfo favEvent : allFavoriteEvents) {
                if (cultureInfo.getTITLE().equals(favEvent.getTITLE())) { // 또는 적절한 비교 로직
                    cultureInfo.setFavorite(true);
                    break;
                }
            }
        }

         */


        return "/homepage/home";
    }

    /*
    @PostMapping("/home")
    public ResponseEntity<?> saveFavoriteEvent(@RequestBody Map<String, Long> payload) {
        Long cultureInfoId = payload.get("cultureInfoId");


        // ID를 사용하여 cultureInfo를 찾고, 이벤트를 저장하는 로직을 수행합니다.
        homeService.findAndSaveEventById(cultureInfoId);


        // 반환할 데이터를 포함하는 맵 생성
        Map<String, Long> response = new HashMap<>();
        response.put("cultureInfoId", cultureInfoId);
        System.out.println("cultureInfoId = " + cultureInfoId);

        // ResponseEntity에 담아 JSON 형태로 반환
        return ResponseEntity.ok(response);
    }

     */

    @GetMapping("/monthlySort")
    public String sort(@RequestParam("month") String month, Model model) {

        List<CultureInfo> list = homeService.getDropBoxData(month);

        model.addAttribute("lists", list);
        model.addAttribute("selectedMonth", month); // 선택된 월을 모델에 추가

        return "/homepage/home";
    }

}
