package com.project.Seoul.controller;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.HomeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
                           @RequestParam(defaultValue = "20", name = "size") int size) {

        // 한 페이지에 나타나는 행사의 갯수는 20개이다.

        // 페이징을 처리하기 전에 총 페이지 수를 미리 계산해야 합니다.
        List<CultureInfo> allCultureInfoApi = homeService.getAllCultureInfoApiSortedByMonth();


        int eventSize = allCultureInfoApi.size();
        int totalPageCount = homeService.getTotalPages(size, eventSize);

        // 페이지 번호 조정 로직을 여기로 이동
        int blockLimit = 10; // 페이징 했을 때 보여지는 번호의 개수
        int startPage = Math.max(1, (((int) Math.ceil(((double) page / blockLimit))) - 1) * blockLimit + 1);
        int endPage = Math.min(totalPageCount, startPage + blockLimit - 1);

        if (page > totalPageCount) {
            page = totalPageCount; // 페이지 번호를 마지막 페이지로 설정
        }


        // 페이지 번호 조정 후에 한 번만 호출
        Page<CultureInfo> eventPaging = homeService.paging(page, size);

        System.out.println("eventSize = " + eventSize);
        System.out.println("totalPageCount = " + totalPageCount);
        System.out.println("startPage = " + startPage);
        System.out.println("endPage = " + endPage);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPageCount);

        List<CultureInfo> items = homeService.getAllCultureInfoApiSortedByMonth(); // API로부터 아이템들을 가져오는 메서드
        Collections.shuffle(items); // 아이템들을 랜덤하게 섞는다
        List<CultureInfo> selectedItems = items.subList(0, 10); // 상위 10개 아이템을 선택

        model.addAttribute("items", selectedItems); // 모델에 아이템들을 추가
        model.addAttribute("lists", eventPaging);

        return "homepage/home";
    }


    //검색 기능
    @GetMapping("/searchEvents")
    public ResponseEntity<Map<String, Object>> searchEvents(
            @RequestParam String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam List<String> eventTypes,
            @RequestParam List<String> locations,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        List<CultureInfo> events = homeService.comprehensiveSearch(keyword, startDate, endDate, eventTypes, locations);

        int totalItems = events.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // 페이지 시작과 끝 계산
        int start = (page - 1) * size;
        int end = Math.min(start + size, totalItems);

        // 인덱스가 경계를 넘어갔을 때 응답
        if (start > end || start < 0 || end > totalItems) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<CultureInfo> paginatedList = events.subList(start, end);

        Map<String, Object> response = new HashMap<>();
        response.put("data", paginatedList);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    /*
    @GetMapping("/monthlySort")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sort(
            @RequestParam("month") String month,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        List<CultureInfo> list = homeService.getDropBoxData(month);

        int totalItems = list.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // 페이지 시작과 끝 계산
        int start = (page - 1) * size;
        int end = Math.min(start + size, totalItems);

        // Check for index out of bounds
        if (start > end || start < 0 || end > totalItems) {
            // Respond with a BAD_REQUEST or any other appropriate status
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<CultureInfo> paginatedList = list.subList(start, end);

        Map<String, Object> response = new HashMap<>();
        response.put("data", paginatedList);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }
    */

    @GetMapping("/detailfilterEvents")
    @ResponseBody
    public ResponseEntity<List<CultureInfo>> filterEvents(@RequestParam("eventType") List<String> eventTypes,
                                                          @RequestParam("location") List<String> locations) {
        List<CultureInfo> allEvents = homeService.getAllCultureInfoApiSortedByMonth();

        // Use copies of the original lists that can be modified without affecting lambda expressions.
        List<String> effectiveEventTypes = eventTypes.contains("전체") ? new ArrayList<>() : new ArrayList<>(eventTypes);
        List<String> effectiveLocations = locations.contains("전체") ? new ArrayList<>() : new ArrayList<>(locations);

        LocalDate today = LocalDate.now(); // 오늘 날짜를 가져옵니다.

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");


        // "축제"가 eventType에 포함되어 있는지 확인합니다.
        boolean includeFestivals = effectiveEventTypes.stream().anyMatch(type -> type.contains("축제"));

        List<CultureInfo> filteredEvents = allEvents.stream()
                .filter(event -> (effectiveEventTypes.isEmpty() || effectiveEventTypes.contains(event.getCODENAME()) || (includeFestivals && event.getCODENAME().contains("축제"))))
                .filter(event -> effectiveLocations.isEmpty() || effectiveLocations.contains(event.getGUNAME()))
                .filter(event -> {
                    if (event.getEND_DATE() == null) return true; // 종료 날짜가 없는 경우 필터링하지 않음
                    LocalDate endDate;
                    try {
                        // 종료 날짜 문자열을 LocalDateTime 객체로 파싱 후 LocalDate로 변환
                        endDate = LocalDateTime.parse(event.getEND_DATE(), formatter).toLocalDate();
                    } catch (Exception e) {
                        e.printStackTrace(); // 파싱 실패시 에러 출력
                        return false; // 잘못된 데이터는 필터링에서 제외
                    }
                    return !endDate.isBefore(today); // 오늘 날짜 이전인 이벤트는 제외
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredEvents);
    }



}
