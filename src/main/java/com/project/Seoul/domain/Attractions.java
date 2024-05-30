package com.project.Seoul.domain;

import lombok.Getter;
import lombok.Setter;

import javax.xml.transform.Result;
import java.util.List;

//json형식 데이터 파싱할 때 사용
@Getter
@Setter
public class Attractions {
    private int list_total_count;
    private List<AttractionsInfo> row;
}
