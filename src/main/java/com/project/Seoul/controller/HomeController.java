package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.repository.EventsRepository;
import com.project.Seoul.service.HomeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    public final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }



    @GetMapping("/home")
    public String homepage(Model model,
                           @RequestParam(defaultValue = "1", name = "page") int page,
                           @RequestParam(defaultValue = "24", name = "size") int size) {
        
        List<CultureInfo> list = homeService.getAllCultureInfoApiSortedByMonth();

        for (CultureInfo cultureInfo : list) {
            homeService.saveEvents(cultureInfo);
        }

        // 페이징을 처리하기 전에 총 페이지 수를 미리 계산해야 합니다.
        // 이 예제에서는 homeService.getTotalPages(size) 메서드가 총 페이지 수를 반환한다고 가정합니다.
        int totalPageCount = homeService.getTotalPages(size);

        // 페이지 번호 조정 로직을 여기로 이동
        int blockLimit = 10;
        int startPage = Math.max(1, (((int) Math.ceil(((double) page / blockLimit))) - 1) * blockLimit + 1);
        int endPage = Math.min(totalPageCount, startPage + blockLimit - 1);

        if (page > totalPageCount) {
            page = totalPageCount; // 페이지 번호를 마지막 페이지로 설정
        }


        // 페이지 번호 조정 후에 한 번만 호출
        Page<CultureInfo> eventPaging = homeService.paging(page, size);

        System.out.println("totalPageCount = " + totalPageCount);
        System.out.println("startPage = " + startPage);
        System.out.println("endPage = " + endPage);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPageCount);

        List<CultureInfo> items = homeService.getAllCultureInfoApi(); // API로부터 아이템들을 가져오는 메서드
        Collections.shuffle(items); // 아이템들을 랜덤하게 섞는다
        List<CultureInfo> selectedItems = items.subList(0, 10); // 상위 10개 아이템을 선택

        model.addAttribute("items", selectedItems); // 모델에 아이템들을 추가
        model.addAttribute("lists", eventPaging);

        return "/homepage/home";
    }


    /*
    //즐겨찾기 기능
    @PostMapping("/home")
    public ResponseEntity<?> saveFavoriteEvent(@RequestBody Map<String, Long> payload) {
        Long cultureInfoId = payload.get("cultureInfoId");


        // ID를 사용하여 cultureInfo를 찾고, 이벤트를 저장하는 로직을 수행합니다.
        homeService.findAndSaveEventById(cultureInfoId);

        return ResponseEntity.ok().build();
    }

     */

    @GetMapping("/monthlySort")
    @ResponseBody
    public ResponseEntity<List<CultureInfo>> sort(@RequestParam("month") String month) {
        List<CultureInfo> list = homeService.getDropBoxData(month);
        return ResponseEntity.ok(list); // JSON 형식으로 데이터 반환
    }

}
