package com.project.Seoul.service;

import com.google.gson.Gson;
import com.project.Seoul.domain.CulturalEventInfoWrapper;
import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.domain.FavoriteCultureInfo;
import com.project.Seoul.repository.EventsRepository;
import com.project.Seoul.repository.FavoriteEventsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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
    public List<CultureInfo> getAllCultureInfoApi() {

        //RestTemplate응 이용해 api 받아오는 방법
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://openapi.seoul.go.kr:8088/5a6f416d79776c6735304c6142424e/json/culturalEventInfo/1/110", String.class);

        String jsonInput = response.getBody();

        Gson gson = new Gson();

        CulturalEventInfoWrapper wrapper = gson.fromJson(jsonInput, CulturalEventInfoWrapper.class);

        //받아온 api데이터(json형식)의 row 부분을 CultureInfo 객체로 만들어서 리스트에 저장
        //curtureInfoList == 받아온 문화행사 정보들의 객체
        List<CultureInfo> curtureInfoList = wrapper.getCulturalEventInfo().getRow();

        return curtureInfoList;
    }


    public List<CultureInfo> getAllCultureInfoApiSortedByMonth() {
        List<CultureInfo> cultureInfoList = getAllCultureInfoApi(); // 이전에 API로부터 받아온 리스트를 가져옴
        return cultureInfoList.stream()
                .sorted(Comparator.comparing(CultureInfo::getDATE))
                .collect(Collectors.toList());
    }



    //keyword를 통해 events 찾기
    public List<CultureInfo> searchCulturalEvents(String keyword) {
        List<CultureInfo> allEvents = getAllCultureInfoApi();
        List<CultureInfo> filteredEvents = allEvents.stream()
                .filter(event -> event.getTITLE().contains(keyword))
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


    // 즐겨찾기 저장
    public void findAndSaveEventById(Long id) {
        Optional<CultureInfo> optionalCultureInfo = eventsRepository.findById(id);
        if (optionalCultureInfo.isPresent()) {
            CultureInfo cultureInfo = optionalCultureInfo.get();

            // ModelMapper의 인스턴스를 생성합니다.
            ModelMapper modelMapper = new ModelMapper();
            // CultureInfo 객체를 FavoriteCultureInfo 객체로 매핑합니다.
            FavoriteCultureInfo favoriteCultureInfo = modelMapper.map(cultureInfo, FavoriteCultureInfo.class);

            favoriteEventsRepository.save(favoriteCultureInfo);
        }
    }

    //즐겨찾기 디비에 있는 모든 데이터
    public List<FavoriteCultureInfo> getAllFavoriteEvents() {
        return favoriteEventsRepository.findAll();
    }

    //출력 타입을 FavoriteCultureInfo -> CultureInfo 변환
    private CultureInfo convertFavoriteToCulture(FavoriteCultureInfo favorite) {
        // ModelMapper의 인스턴스를 생성합니다.
        ModelMapper modelMapper = new ModelMapper();

        // CultureInfo 객체를 FavoriteCultureInfo 객체로 매핑합니다.
        CultureInfo cultureInfo = modelMapper.map(favorite, CultureInfo.class);


        return cultureInfo;
    }




    //선택된 드롭박스에 해당되는 데이터
    public List<CultureInfo> getDropBoxData(String mon) {


        if(mon.equals("전체")) {
            return getAllCultureInfoApiSortedByMonth();
        }
        /*

        else if(mon.equals("즐겨찾기")) {

            List<FavoriteCultureInfo> favoriteList = getAllFavoriteEvents();
            List<CultureInfo> cultureList = new ArrayList<>();

            for(FavoriteCultureInfo favorite : favoriteList) {
                //출력 타입을 FavoriteCultureInfo -> CultureInfo 변환
                CultureInfo cultureInfo = convertFavoriteToCulture(favorite);

                System.out.println("cultureInfo Title = " + cultureInfo.getTITLE());
                System.out.println("cultureInfo.getDATE() = " + cultureInfo.getDATE());

                cultureList.add(cultureInfo);
            }

            //즐겨찾기 목록 출력 시 날짜 순으로
            return cultureList.stream()
                    .sorted(Comparator.comparing(CultureInfo::getDATE))
                    .collect(Collectors.toList());

        }

         */

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
            LocalDate date = null;
            LocalDate date2 = null;


            // "2024-12-07~2024-12-07" 형태일 때
            if(dateString.length() > 10) {

                // '~'를 기준으로 문자열을 분할하고 첫 번째 날짜 부분만 사용
                String firstDatePart = dateString.split("~")[0];

                String secondDatePart = dateString.split("~")[1];

                // dateString을 LocalDate 객체로 파싱
                date = LocalDate.parse(firstDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                date2 = LocalDate.parse(secondDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                //4월~6월

                // LocalDate 객체에서 월을 추출 (1 ~ 12 범위의 값)
                monthValue = date.getMonthValue();
                monthValue2 = date2.getMonthValue();

                int m = Integer.parseInt(month);

                if (monthValue <= m && m <= monthValue2) {

                    filteredList.add(info);
                }

            }

            else {
                // '~'를 기준으로 문자열을 분할하고 첫 번째 날짜 부분만 사용
                String firstDatePart = dateString.split("~")[0];

                // dateString을 LocalDate 객체로 파싱
                date = LocalDate.parse(firstDatePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));


                // LocalDate 객체에서 월을 추출 (1 ~ 12 범위의 값)
                monthValue = date.getMonthValue();

                String str = String.valueOf(monthValue);

                if (str.equals(month)) {

                    filteredList.add(info);
                }

            }


        }

        for (CultureInfo info : filteredList) {
            System.out.println("info = " + info.getTITLE());
            System.out.println("info.getDATE() = " + info.getDATE());
            System.out.println();
        }

        return filteredList.stream()
                .sorted(Comparator.comparing(CultureInfo::getDATE))
                .collect(Collectors.toList());

    }



}

