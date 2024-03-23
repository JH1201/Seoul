package com.project.Seoul.service;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.repository.EventsRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapPageService {

    public final EventsRepository eventsRepository;
    public final HomeService homeService;

    public MapPageService(EventsRepository eventsRepository, HomeService homeService) {
        this.eventsRepository = eventsRepository;
        this.homeService = homeService;
    }

    public List<CultureInfo> filterEventsByBounds(List<CultureInfo> markersInfo, Map<String, Double> bounds) {
        if (bounds.get("southWestLat") == null || bounds.get("southWestLng") == null ||
                bounds.get("northEastLat") == null || bounds.get("northEastLng") == null) {
            return Collections.emptyList();
        }
        double southWestLat = bounds.get("southWestLat");
        double southWestLng = bounds.get("southWestLng");
        double northEastLat = bounds.get("northEastLat");
        double northEastLng = bounds.get("northEastLng");

        return markersInfo.stream()
                .filter(cultureInfo -> {
                    try {
                        double lng = Double.parseDouble(cultureInfo.getLAT().trim());
                        double lat = Double.parseDouble(cultureInfo.getLOT().trim());
                        return lat >= southWestLat && lat <= northEastLat && lng >= southWestLng && lng <= northEastLng;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

}
