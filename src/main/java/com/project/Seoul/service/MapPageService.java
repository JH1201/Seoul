package com.project.Seoul.service;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.repository.EventsRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapPageService {

    public final EventsRepository eventsRepository;
    public final HomeService homeService;

    public MapPageService(EventsRepository eventsRepository, HomeService homeService) {
        this.eventsRepository = eventsRepository;
        this.homeService = homeService;
    }

    public List<CultureInfo> filterEventsByBounds(double swLat, double swLng, double neLat, double neLng) {
        // 전체 행사 목록을 가져옵니다. 이 부분은 실제로는 데이터베이스 쿼리가 될 것입니다.
        List<CultureInfo> allEvents = homeService.getAllCultureInfoApiSortedByMonth();

        // 경계 안에 있는 행사만 필터링합니다.
        return allEvents.stream()
                .filter(info -> {
                    //위도와 경도 위치 바꿔서 저장
                    double lng = Double.parseDouble(info.getLAT());
                    double lat = Double.parseDouble(info.getLOT());

                    System.out.println("MapPage");
                    System.out.println("lat = " + lat);
                    System.out.println("lng = " + lng);

                    System.out.println("swLat = " + swLat);
                    System.out.println("neLat = " + neLat);
                    System.out.println("swLng = " + swLng);
                    System.out.println("neLng = " + neLng);

                    // 위도와 경도가 지도의 경계 내에 있는지 확인
                    return lat >= swLat && lat <= neLat && lng >= swLng && lng <= neLng;
                })
                .collect(Collectors.toList());
    }


}
