package com.project.Seoul.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class AttractionsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String POST_SN;//	고유번호
    private String LANG_CODE_ID; //	언어
    private String POST_SJ;	// 상호명
    private String POST_URL;	//콘텐츠URL
    private String ADDRESS;	//주소
    private String NEW_ADDRESS;	//신주소
    private String CMMN_TELNO;	//전화번호
    private String CMMN_FAX;	//팩스번호
    private String CMMN_HMPG_URL;	//웹사이트

    @Column(length = 500)
    private String CMMN_USE_TIME;	//운영시간

    private String CMMN_BSNDE;	//운영요일
    private String CMMN_RSTDE;	//휴무일

    @Column(length = 500)
    private String SUBWAY_INFO;	//교통정보
    private String TAG;	//태그
    private String BF_DESC;	//장애인편의시설


    public AttractionsInfo() {

    }
}
