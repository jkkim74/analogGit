package com.skplanet.bisportal.model.bip;

import com.skplanet.bisportal.model.acl.BipUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by seoseungho on 2014. 9. 17..
 *
 * Model for DSS table
 */
@Data
public class Dss implements Serializable {
    private static final long serialVersionUID = -4341054684465578263L;

    // DSS ID
    private long id;
    // insert 에서 사용하는 변수 bm의 ID 를 갖는다.
    private List<Long> bmIdList;
    // DB에서 조회할 때 사용하는 변수, dss ID, bm ID, bm NAME을 갖는다.
    private transient List<DssBmList> bmList;
    // 주제
    private String subject;
    // 내용
    private String content;
    // 결과 및 시사점
    private String conclusion;
    // 한계 및 향후과제
    private String futureWork;
    // 데이터 source
    private String dataSource;
    // 샘플 크기
    private long sampleSize;
    // 사용변수
    private transient List<Map<String, String>> variables;
    // 사용변수 JSON
    private String variablesJSON;
    // 분석 시작 시간
    private String analysisStart;
    private String analysisStartYYYYMMDD;
    // 분석 종료 시간
    private String analysisEnd;
    private String analysisEndYYYYMMDD;
    // 데이터 수집 시작 시간
    private String dataStart;
    private String dataStartYYYYMMDD;
    // 데이터 수집 종료 시간
    private String dataEnd;
    private String dataEndYYYYMMDD;
    // 조회 수
    private int viewCount;
    // 추천 수
    private int likeCount;
    // 이전 DSS
    private Dss previousDss;
    // 다음 DSS
    private Dss nextDss;

    // 첨부 파일 리스트
    private transient List<FileMeta> fileMetaList;

    private BipUser createUser;
    private String createName;
    private String createId;    //사용자 로그인 아이디
    private String createDate;
    private String updateId;    //사용자 로그인 아이디
    private String updateDate;
}
