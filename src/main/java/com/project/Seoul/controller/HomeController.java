package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
                           @RequestParam(defaultValue = "30", name = "size") int size) {
        


        // 페이징을 처리하기 전에 총 페이지 수를 미리 계산해야 합니다.
        // 이 예제에서는 homeService.getTotalPages(size) 메서드가 총 페이지 수를 반환한다고 가정합니다.
        List<CultureInfo> allCultureInfoApi = homeService.getAllCultureInfoApi();
        int eventSize = allCultureInfoApi.size();
        int totalPageCount = homeService.getTotalPages(size, eventSize);

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


    @GetMapping("/monthlySort")
    @ResponseBody
    public ResponseEntity<List<CultureInfo>> sort(@RequestParam("month") String month) {

        List<CultureInfo> list = homeService.getDropBoxData(month);


        return ResponseEntity.ok(list); // JSON 형식으로 데이터 반환
    }

    @GetMapping("/filterEvents")
    @ResponseBody
    public ResponseEntity<List<CultureInfo>> filterEvents(@RequestParam("eventType") List<String> eventTypes,
                                                          @RequestParam("location") List<String> locations) {
        List<CultureInfo> allEvents = homeService.getAllCultureInfoApiSortedByMonth();

        // Use copies of the original lists that can be modified without affecting lambda expressions.
        List<String> effectiveEventTypes = eventTypes.contains("전체") ? new ArrayList<>() : new ArrayList<>(eventTypes);
        List<String> effectiveLocations = locations.contains("전체") ? new ArrayList<>() : new ArrayList<>(locations);

        // "축제"가 eventType에 포함되어 있는지 확인합니다.
        boolean includeFestivals = effectiveEventTypes.stream().anyMatch(type -> type.contains("축제"));

        List<CultureInfo> filteredEvents = allEvents.stream()
                .filter(event -> effectiveEventTypes.isEmpty() || effectiveEventTypes.contains(event.getCODENAME()) || (includeFestivals && event.getCODENAME().contains("축제")))
                .filter(event -> effectiveLocations.isEmpty() || effectiveLocations.contains(event.getGUNAME()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredEvents);
    }



}
