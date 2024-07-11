package com.project.Seoul;

import com.project.Seoul.domain.CultureInfo;
import com.project.Seoul.service.EventInfoService;
import com.project.Seoul.service.HomeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SeoulApplication {


	public static HomeService homeService;

	public SeoulApplication(HomeService homeService) {
		this.homeService = homeService;
	}

	public static void main(String[] args) {

		SpringApplication.run(SeoulApplication.class, args);


		List<CultureInfo> list = homeService.getAllCultureInfoApiSortedByMonth();
		System.out.println("list.size = " + list.size());

		for (CultureInfo cultureInfo : list) {
			homeService.saveEvents(cultureInfo);
		}



	}



}
