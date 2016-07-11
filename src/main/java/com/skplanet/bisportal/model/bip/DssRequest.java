package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.util.List;

/**
 * Created by seoseungho on 2014. 9. 24..
 */
@Data
public class DssRequest {
    int page;
    int pageSize;

    // request에서 받아온 bm ID 리스트 예( 235,236,237 )
    String bms;

    // bms의 값을 List<String> 으로 치환해서 담는 변수
    List<String> bmList;

    // 등록일 검색 시작 일
    String searchStart;
    // 등록일 검색 끝 일
    String searchEnd;
    // 등록자 명
    String createName;

    // 내용 검색 필드 (all, subject, content 중 1개)
    String contentType;
    // 내용
    String content;
    // order by
    String orderBy;

}
