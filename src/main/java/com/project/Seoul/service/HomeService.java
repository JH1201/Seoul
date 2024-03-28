package com.project.Seoul.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.Seoul.domain.*;
import com.project.Seoul.repository.EventsRepository;
import com.project.Seoul.repository.FavoriteEventsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeService {

    public final EventsRepository eventsRepository;
    public final FavoriteEventsRepository favoriteEventsRepository;

    public HomeService(EventsRepository eventsRepository, FavoriteEventsRepository favoriteEventsRepository) {
        this.eventsRepository = eventsRepository;
        this.favoriteEventsRepository = favoriteEventsRepository;
    }


    //문화행사 정보 api
    /*
    public List<CultureInfo> getAllCultureInfoApi() {

        //RestTemplate응 이용해 api 받아오는 방법
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://openapi.seoul.go.kr:8088/5a6f416d79776c6735304c6142424e/json/culturalEventInfo/1/1000", String.class);

        String jsonInput = response.getBody();

        Gson gson = new Gson();

        CulturalEventInfoWrapper wrapper = gson.fromJson(jsonInput, CulturalEventInfoWrapper.class);

        //받아온 api데이터(json형식)의 row 부분을 CultureInfo 객체로 만들어서 리스트에 저장
        //curtureInfoList == 받아온 문화행사 정보들의 객체
        List<CultureInfo> curtureInfoList = wrapper.getCulturalEventInfo().getRow();



        return curtureInfoList;
    }

     */

    public List<CultureInfo> getAllCultureInfoApi() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://openapi.seoul.go.kr:8088/5a6f416d79776c6735304c6142424e/json/culturalEventInfo/1/1000", String.class);

        String jsonInput = response.getBody();

        Gson gson = new Gson();

        CulturalEventInfoWrapper wrapper = gson.fromJson(jsonInput, CulturalEventInfoWrapper.class);

        List<CultureInfo> cultureInfoList = wrapper.getCulturalEventInfo().getRow();


        // Remove items where the start date is in 2023 or the end date is not in 2024
        cultureInfoList.removeIf(info -> {
            String date = info.getDATE();
            if (date.contains(" ~ ")) {
                String[] dates = date.split(" ~ ");
                String startDate = dates[0];
                String endDate = dates[1];
                // 2023년에 끝나는 행사를 지움. 2023년에 시작해서 2024년에 끝나는 행사는 유지됨
                return endDate.startsWith("2023-");
            } else {
                // 날짜가 단일 값이고, 그 값이 2023년에 해당하는 경우 해당 항목을 지움
                return date.startsWith("2023-");
            }
        });

        return cultureInfoList;
    }

    public List<CultureInfo> getAllCultureInfoApiSortedByMonth() {
        List<CultureInfo> cultureInfoList = getAllCultureInfoApi(); // 이전에 API로부터 받아온 리스트를 가져옴

        //오늘 날짜 변수
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        return cultureInfoList.stream()
                .filter(cultureInfo -> {
                    LocalDateTime eventDateTime = LocalDateTime.parse(cultureInfo.getEND_DATE(), formatter); // 이벤트의 종료 날짜를 파싱
                    LocalDate eventDate = eventDateTime.toLocalDate();
                    return !eventDate.isBefore(today); // 오늘 날짜가 종료일인 행사 포함
                })
                .sorted(Comparator.comparing(CultureInfo::getDATE))
                .collect(Collectors.toList());
    }

    /*public List<CultureInfo> findByDate(String Str_D, String End_D) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(Str_D, formatter);
        LocalDate endDate = LocalDate.parse(End_D, formatter);

        List<CultureInfo> cultureInfoList = getAllCultureInfoApi(); // 이전에 API로부터 받아온 리스트를 가져옴

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        return cultureInfoList.stream()
                .filter(cultureInfo -> {
                    // CultureInfo 객체의 날짜를 LocalDate 객체로 변환
                    LocalDate cultureStartDate = LocalDate.parse(cultureInfo.getSTRTDATE(), formatter1);


                    LocalDate cultureEndDate = LocalDate.parse(cultureInfo.getEND_DATE(), formatter1);

                    // startDate 이후 및 endDate 이전이거나 같은 날짜인지 확인
                    return (cultureEndDate.isAfter(startDate) || cultureEndDate.isEqual(startDate)) &&
                            (cultureStartDate.isBefore(endDate) || cultureStartDate.isEqual(endDate));
                })
                .sorted(Comparator.comparing(cultureInfo -> LocalDate.parse(cultureInfo.getSTRTDATE(), formatter1)))
                .collect(Collectors.toList());
    }*/





    //keyword를 통해 events 찾기
    public List<CultureInfo> searchCulturalEvents(String keyword) {
        List<CultureInfo> allEvents = getAllCultureInfoApiSortedByMonth();

        if (keyword == null || keyword.trim().isEmpty()) {
            return allEvents;
        }

        List<CultureInfo> filteredEvents = allEvents.stream()
                .filter(event -> event.getTITLE().contains(keyword) || event.getPLACE().contains(keyword))
                .collect(Collectors.toList());
        return filteredEvents.isEmpty() ? new ArrayList<>() : filteredEvents;
    }


    //모든 문화행사 정보 중에 title이 같은 행사를 찾는 함수
    public CultureInfo getOneCultureInfoApi(String title) {
        // 모든 문화 정보를 가져온다.
        List<CultureInfo> cultureInfoList = getAllCultureInfoApi();

        // 리스트를 순회하면서 homePage와 일치하는 객체를 찾는다.
        for (CultureInfo cultureInfo : cultureInfoList) {
            if (cultureInfo.getTITLE().equals(title)) {

                // 일치하는 객체를 찾았으므로 반환한다.
                return cultureInfo;
            }
        }

        // 일치하는 객체를 찾지 못했으므로 null 반환 또는 예외 처리
        return null; // 혹은 적절한 예외를 발생시키기
    }

    // 모든 이벤트를 디비에 저장
    public void saveEvents(CultureInfo cultureInfo) {
        eventsRepository.save(cultureInfo);
    }




    //출력 타입을 FavoriteCultureInfo -> CultureInfo 변환
    private CultureInfo convertFavoriteToCulture(FavoriteCultureInfo favorite) {
        // ModelMapper의 인스턴스를 생성합니다.
        ModelMapper modelMapper = new ModelMapper();

        // CultureInfo 객체를 FavoriteCultureInfo 객체로 매핑합니다.
        CultureInfo cultureInfo = modelMapper.map(favorite, CultureInfo.class);


        return cultureInfo;
    }

    public List<CultureInfo> comprehensiveSearch(String keyword, LocalDate startDate, LocalDate endDate, List<String> eventTypes, List<String> locations) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDate today = LocalDate.now();

        boolean includeAllEventTypes = eventTypes.contains("전체");
        boolean includeAllLocations = locations.contains("전체");
        boolean includeFestivals = eventTypes.stream().anyMatch(type -> type.equalsIgnoreCase("축제"));

        return getAllCultureInfoApiSortedByMonth().stream()
                .filter(event -> (keyword == null || keyword.trim().isEmpty() || event.getTITLE().contains(keyword) || event.getPLACE().contains(keyword)))
                .filter(event -> includeAllEventTypes || eventTypes.isEmpty() || eventTypes.contains(event.getCODENAME()) || (includeFestivals && event.getCODENAME().contains("축제")))
                .filter(event -> includeAllLocations || locations.isEmpty() || locations.contains(event.getGUNAME()))
                .filter(event -> {
                    if (startDate == null && endDate == null && event.getEND_DATE() == null) {
                        return true; // 시작 및 종료 날짜 필터링이 없고, 이벤트의 종료 날짜 정보가 없는 경우
                    }
                    if (event.getEND_DATE() == null) {
                        return true; // 종료 날짜가 없는 경우 필터링하지 않음
                    }
                    // 이벤트 종료 날짜 처리
                    LocalDate eventEndDate = LocalDate.parse(event.getEND_DATE().substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
                    return (startDate == null || !eventEndDate.isBefore(startDate)) &&
                            (endDate == null || !eventEndDate.isAfter(endDate)) &&
                            !eventEndDate.isBefore(today); // 오늘 날짜 이전인 이벤트는 제외
                })
                .collect(Collectors.toList());
    }

    //선택된 드롭박스에 해당되는 데이터
    public List<CultureInfo> getDropBoxData(String mon) {


        if(mon.equals("전체")) {
            return getAllCultureInfoApiSortedByMonth();
        }

        //데이터베이스에서 month에 해당하는 데이터 조회
        List<CultureInfo> filteredList = new ArrayList<>();

        // 비숫자 문자를 모두 제거하여 숫자만 남깁니다.
        String month = mon.replaceAll("\\D+", "");


        // 예시 로직: 모든 CultureInfo 객체를 순회하면서, 주어진 month와 일치하는 객체를 찾아 filteredList에 추가
        for (CultureInfo info : getAllCultureInfoApi()) { // getAllCultureInfo()는 모든 CultureInfo 객체를 반환하는 메서드

            // info.getDATE() 문자열 예시: "2023-01-15"
            String dateString = info.getDATE();

            int monthValue;
            int monthValue2;
            int yearValue;
            int yearValue2;

            LocalDate date = null;
            LocalDate date2 = null;
            LocalDate year = null;
            LocalDate year2 = null;


            // "2024-12-07~2024-12-07" 형태일 때
            if(dateString.length() > 10) {

                // '~'를 기준으로 문자열을 분할하고 첫 번째 날짜 부분만 사용
                String firstDatePart = dateString.split("~")[0];

                String secondDatePart = dateString.split("~")[1];

                // dateString을 LocalDate 객체로 파싱

                year = LocalDate.parse(secondDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                year2 = LocalDate.parse(secondDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                date = LocalDate.parse(firstDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                date2 = LocalDate.parse(secondDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                //

                yearValue = year.getYear();
                yearValue2 = year2.getYear();

                // LocalDate 객체에서 월을 추출 (1 ~ 12 범위의 값)
                monthValue = date.getMonthValue();
                monthValue2 = date2.getMonthValue();

                int m = Integer.parseInt(month);

                if (monthValue <= m && m <= monthValue2) {

                    if(yearValue2 != 2024) continue;

                    filteredList.add(info);
                }

            }

            else {
                // '~'를 기준으로 문자열을 분할하고 첫 번째 날짜 부분만 사용
                String firstDatePart = dateString.split("~")[0];

                year = LocalDate.parse(firstDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // dateString을 LocalDate 객체로 파싱
                date = LocalDate.parse(firstDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                // LocalDate 객체에서 월을 추출 (1 ~ 12 범위의 값)
                monthValue = date.getMonthValue();
                yearValue = year.getYear();

                String str = String.valueOf(monthValue);

                if (str.equals(month)) {
                    if(yearValue != 2024) continue;

                    filteredList.add(info);
                }

            }


        }


        return filteredList.stream()
                .sorted(Comparator.comparing(CultureInfo::getDATE))
                .collect(Collectors.toList());

    }

    public Page<CultureInfo> paging (int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        return eventsRepository.findAll(pageable);

    }

    public int getTotalPages(int size, int eventSize) {

        return (int) Math.ceil((double) eventSize /size);
    }



}

