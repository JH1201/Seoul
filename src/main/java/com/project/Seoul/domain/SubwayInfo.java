package com.project.Seoul.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class SubwayInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String TYPE;//	노드링크 유형
    private String NODE_WKT; //	노드 WKT
    private String NODE_ID;	//노드 ID
    private String NODE_CODE;	//노드 유형 코드
    private String SGG_CD;	//시군구코드
    private String SGG_NM;	//시군구명
    private String EMD_CD;	//읍면동코드
    private String EMD_NM;	//읍면동명
    private String SW_CD;	//지하철역코드
    private String SW_NM;	//지하철역명



    public SubwayInfo() {

    }
}
