package com.project.Seoul.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


//각각의 문화행사 정보 필드 값

@Getter
@Setter
@AllArgsConstructor
@Entity
public class CultureInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CODENAME; //분류
    private String GUNAME; //자치구
    private String TITLE; //공연,행사명
    private String DATE; //날짜,시간
    private String PLACE; //장소
    private String ORG_NAME; //기관명
    private String USE_TRGT; //이용대상

    @Column(length = 500)
    private String USE_FEE; //이용요금

    @Column(length = 500)
    private String PLAYER; //출연자정보

    @Column(length = 1500)
    private String PROGRAM; //프로그램소개

    @Column(length = 1000)
    private String ETC_DESC; //기타내용

    @Column(length = 500)
    private String ORG_LINK; //홈페이지 주소

    private String MAIN_IMG; //메인 이미지
    private String RGSTDATE; //신청일
    private String TICKET; //시민,기관
    private String STRTDATE; //시작일
    private String END_DATE; //종료일
    private String THEMECODE; //테마분류
    private String LOT; //위도
    private String LAT; //경도
    private String IS_FREE; //유무료
    private String HMPG_ADDR; //문화포털상세URL

    // 기본 생성자
    public CultureInfo() {
    }

}


