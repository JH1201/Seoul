package com.project.Seoul.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//json형식 데이터 파싱할 때 사용
@Getter
@Setter
public class Subway {
    private int list_total_count;
    private List<SubwayInfo> row;
}
