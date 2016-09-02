'use strict';

App.controller('Pan0102Ctrl', ["$scope", "$q", "$http", "$timeout", "uiGridConstants", "apiService", function ($scope, $q, $http, $timeout, uiGridConstants, apiService) {

  $scope.title = '고객 정보 및 장기 거래 실적 조회';

  // 조회 조건
  $scope.selectOptions = [
    { label: '회원ID', value: 'mbrId' },
    { label: '카드번호', value: 'cardNo' },
    { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' },
    { label: '메시지발송ID (검토중)', value: 'msgSndId' }
  ];

  // 기본 정보
  $scope.gridOptionsBasicInfo = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'mbrType', displayName: '회원구분' },
      { field: 'mbrStsCd', displayName: '회원상태' },
      { field: 'mbrId', displayName: '회원ID' },
      { field: 'custId', displayName: 'CUST_ID' },
      { field: 'ciNo', displayName: 'CI번호' },
      { field: 'pntExtnctYn', displayName: '포인트 소멸 여부' }
    ]
  };

  // 회원 원장
  $scope.gridOptionsMemberInfo = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'mbrKorNm', displayName: '한글성명' },
      { field: 'leglBthdt', displayName: '법정생년월일' },
      { field: 'leglGndrFgCd', displayName: '성별' },
      { field: 'fstCardRregDt', displayName: 'OCB 최초 카드 본등록 일자' },
      { field: 'clphnNo', displayName: '휴대전화번호' },
      { field: 'clphnNoDt', displayName: '휴대전화번호 최종 유입 출처/일자' },
      { field: 'clphnNoDupYn', displayName: '휴대전화번호 중복 여부' },
      { field: 'homeTelNo', displayName: '자택전화번호' },
      { field: 'homeTelNoDt', displayName: '자택전화번호 최종 유입 출처/일자' },
      { field: 'jobpTelNo', displayName: '직장전화번호' },
      { field: 'jobpTelNoDt', displayName: '직장전화번호 최종 유입 출처/일자' },
      { field: 'homeBasicAddr homeDtlAddr', displayName: '자택주소' },
      { field: 'homeDtlAddrDt', displayName: '자택주소 최종 유입 출처/일자' },
      { field: 'jobpBasicAddr jobpDtlAddr', displayName: '직장주소' },
      { field: 'jobpDtlAddrDt', displayName: '직장주소 최종 유입 출처/일자' },
      { field: 'emailAddr', displayName: '이메일주소' },
      { field: 'emailAddrDt', displayName: '이메일주소 최종 유입 출처/일자' },
      { field: 'emailAddrDupYn', displayName: '이메일주소 중복 여부' },
      { field: 'destrExpctnDt', displayName: '개인정보 유효 기간 만료 예정일자' }
    ]
  };

  // 마케팅 동의 내역
  $scope.gridOptionsAgreementInfo = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'ocbMktngAgrmtYn', displayName: '마케팅 동의 여부' },
      { field: 'mktngFnlAgrmtSrcOrgCd / mktngFnlAgrmtDt', displayName: '마케팅 최종 동의 유입 출처/일자' },
      { field: 'entprMktngAgrmtYn', displayName: '교차 활용 동의 여부' },
      { field: 'tmRcvAgrmtYn', displayName: 'TM 수신 동의 여부' },
      { field: 'emailRcvAgrmtYn', displayName: 'EM 수신 동의 여부' },
      { field: 'advtSmsRcvAgrmtYn', displayName: '광고성 SMS 수신 동의 여부' },
      { field: 'ifrmtSmsRcvAgrmtYn', displayName: '정보성 SMS 수신 동의 여부' },
      { field: 'pushRcvAgrmtYn', displayName: '앱 푸쉬 동의 여부' },
      { field: 'pntUseRsvngPushAgrmtYn', displayName: '포인트 사용적립 동의 여부' },
      { field: 'bnftMlfPushAgrmtYn', displayName: '혜택/모바일전단 푸쉬 동의 여부' },
      { field: 'tusePushAgrmtYn', displayName: '친구와 함께쓰기 푸쉬 동의 여부' },
      { field: 'coinNotiPushAgrmtYn', displayName: '동전 푸쉬 동의 여부' },
      { field: 'locUtlzAgrmtYn', displayName: '위치 정보 활용 동의 여부' },
      { field: 'blthAgrmtYn', displayName: 'BLE 동의 여부' }
    ]
  };

  // 채널 가입 현황
  $scope.gridOptionsJoinInfo = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'ocbappYn', displayName: 'OCB앱 이용 여부' },
      { field: 'ocbappFnlEntrDttm', displayName: 'OCB앱 최종 가입 일자' },
      { field: 'ocbappFnlLgnDttm', displayName: 'OCB앱 최종 로그인 일자' },
      { field: 'ocbcomUnitedId', displayName: 'OCB닷컴 United ID' },
      { field: 'ocbcomLgnId', displayName: 'OCB닷컴 로그인ID' },
      { field: 'ocbcomEntrDttm', displayName: 'OCB닷컴 가입 일자' },
      { field: 'ocbcomFlnLgnDttm', displayName: 'OCB닷컴 최종 로그인 일자' }
    ]
  };

  // 거래 유형별 최종 서비스 이용 일자
  $scope.gridOptionsLastestUsageInfo = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'fnlErngDt', displayName: 'OCB 적립/사용/할인/현금환급' },
      { field: 'fnlCardRregDt', displayName: 'OCB 카드 본등록' },
      { field: 'fnlCnsofEnqDt', displayName: '상담실 문의' },
      { field: 'fnlPrdPchsDt', displayName: '상품 구매' },
      { field: 'fnlPntInqDt', displayName: '포인트 조회' }
    ]
  };

  // 마케팅 회원 원장
  $scope.gridOptionsMarketingMemberInfo = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'mbrKorNm', displayName: '한글성명' },
      { field: 'clphnNo', displayName: '휴대전화번호' },
      { field: 'clphnNoDt', displayName: '휴대전화번호 최종 유입 출처/일자' },
      { field: 'homeTelNo', displayName: '자택전화번호' },
      { field: 'homeTelNoDt', displayName: '자택전화번호 최종 유입 출처/일자' },
      { field: 'jobpTelNo', displayName: '직장전화번호' },
      { field: 'jobpTelNoDt', displayName: '직장전화번호 최종 유입 출처/일자' },
      { field: 'homeBasicAddr homeDtlAddr', displayName: '자택주소' },
      { field: 'homeDtlAddrDt', displayName: '자택주소 최종 유입 출처/일자' },
      { field: 'jobpBasicAddr jobpDtlAddr', displayName: '직장주소' },
      { field: 'jobpDtlAddrDt', displayName: '직장주소 최종 유입 출처/일자' },
      { field: 'emailAddr', displayName: '이메일주소' }
    ]
  };

  // 마케팅 회원 원장 이력
  $scope.gridOptionsMarketingMemberInfoHistory = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'dataSrcOrgCd', displayName: '유입기관코드' },
      { field: 'agrmtVerCd', displayName: '동의버전' },
      { field: 'agrmtDt', displayName: '동의일자' },
      { field: 'agrmtYn', displayName: '동의여부' },
      { field: 'mbrKorNm', displayName: '한글성명' },
      { field: 'bthdt', displayName: '생년월일' },
      { field: 'gndrFgCd', displayName: '성별' },
      { field: 'emailAddr', displayName: '이메일주소' },
      { field: 'clphnNo', displayName: '휴대전화번호' },
      { field: 'homeTelNo', displayName: '자택전화번호' },
      { field: 'jobpTelNo', displayName: '직장전화번호' }
    ]
  };

  // 3자 제공 동의 이력
  $scope.gridOptions3rdPartyProvideHistory = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'dataDstOrgCd', displayName: '제공대상기관' },
      { field: 'dataSrcOrgCd', displayName: '유입기관코드' },
      { field: 'agrmtVerCd', displayName: '동의버전' },
      { field: 'agrmtDt', displayName: '동의일자' },
      { field: 'agrmtYn', displayName: '동의여부' },
      { field: 'mbrKorNm', displayName: '한글성명' },
      { field: 'bthdt', displayName: '생년월일' },
      { field: 'gndrFgCd', displayName: '성별' },
      { field: 'emailAddr', displayName: '이메일주소' },
      { field: 'clphnNo', displayName: '휴대전화번호' },
      { field: 'homeTelNo', displayName: '자택전화번호' },
      { field: 'jobpTelNo', displayName: '직장전화번호' }
    ]
  };

  // 보유 카드 목록
  $scope.gridOptionsCardList = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'cardDtlGrpCd', displayName: '카드코드 및 명칭' },
      { field: 'cardStsCd', displayName: '카드상태' },
      { field: 'cardNo', displayName: '카드번호' },
      { field: 'issDt', displayName: '카드발급일자' },
      { field: 'cardRregDt', displayName: '카드본등록일자' }
    ]
  };

  // 거래 내역
  $scope.gridOptionsTransactionHistory = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'rcvDt', displayName: '접수일시', cellTooltip: true, headerTooltip: true },
      { field: 'rcvSeq', displayName: '접수번호', cellTooltip: true, headerTooltip: true },
      { field: 'apprDttm', displayName: '승인일시', cellTooltip: true, headerTooltip: true },
      { field: 'repApprNo', displayName: '대표승인번호', cellTooltip: true, headerTooltip: true },
      { field: 'apprNo', displayName: '승인번호', cellTooltip: true, headerTooltip: true },
      { field: 'saleDttm', displayName: '매출일시', cellTooltip: true, headerTooltip: true },
      { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
      { field: 'cardDtlGrpCd / cardDtlGrpNm', displayName: '카드그룹코드', cellTooltip: true, headerTooltip: true },
      { field: 'cardNo', displayName: '카드번호', cellTooltip: true, headerTooltip: true },
      { field: 'alcmpnCd / alcmpnNm', displayName: '발생제휴사', cellTooltip: true, headerTooltip: true },
      { field: 'mcntCd / mcntNm', displayName: '발생가맹점', cellTooltip: true, headerTooltip: true },
      { field: 'stlmtMcntCd / stlmtMcntNm', displayName: '정산가맹점', cellTooltip: true, headerTooltip: true },
      { field: 'pntKndCd / pntKndNm', displayName: '포인트종류', cellTooltip: true, headerTooltip: true },
      { field: 'slipCd / slipNm', displayName: '전표', cellTooltip: true, headerTooltip: true },
      { field: 'saleAmt', displayName: '매출금액', cellTooltip: true, headerTooltip: true },
      { field: 'pnt', displayName: '포인트', cellTooltip: true, headerTooltip: true },
      { field: 'csMbrCmmsn', displayName: '제휴사연회비', cellTooltip: true, headerTooltip: true },
      { field: 'cmmsn', displayName: '수수료', cellTooltip: true, headerTooltip: true },
      { field: 'pmntWayCd / pmntWayNm', displayName: '지불수단', cellTooltip: true, headerTooltip: true },
      { field: 'orgCd / orgNm', displayName: '기관', cellTooltip: true, headerTooltip: true },
      { field: 'oilPrdctSgrpCd / oilPrdctSgrpNm', displayName: '유종', cellTooltip: true, headerTooltip: true },
      { field: 'saleQty', displayName: '주유량', cellTooltip: true, headerTooltip: true },
      { field: 'cpnPrdCd / cpnPrdNm', displayName: '쿠폰', cellTooltip: true, headerTooltip: true },
      { field: 'transactionType', displayName: '거래종류', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 이메일 발송 이력
  $scope.gridOptionsEmailSendHistory = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'sndDt', displayName: '발송일자' },
      { field: 'emailTitl', displayName: '이메일 제목' },
      { field: 'lcptEmailSndRsltCd', displayName: '이메일 발송 결과' }
    ]
  };

  // 앱 푸쉬 이력
  $scope.gridOptionsAppPushHistory = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'sndDttm', displayName: '발송일자' },
      { field: 'msgTitl', displayName: '푸쉬 제목' },
      { field: 'pushSndRsltCd', displayName: '푸쉬 결과' }
    ]
  };

  // 조회
  $scope.searchAll = function (searchType, searchKeyword) {
    var params = {};
    params[searchType] = searchKeyword;

    apiService.getMemberInfo(params).then(function (data) {
      $scope.gridOptionsBasicInfo.data = data;
      $scope.gridOptionsMemberInfo.data = data;
    });

    apiService.getAgreementInfo(params).then(function (data) {
      $scope.gridOptionsAgreementInfo.data = data;
    });

    apiService.getJoinInfo(params).then(function (data) {
      $scope.gridOptionsJoinInfo.data = data;
    });

    apiService.getLastestUsageInfo(params).then(function (data) {
      $scope.gridOptionsLastestUsageInfo.data = data;
    });

    apiService.getMarketingMemberInfo(params).then(function (data) {
      $scope.gridOptionsMarketingMemberInfo.data = data;
    });

    apiService.getMarketingMemberInfoHistory(params).then(function (data) {
      $scope.gridOptionsMarketingMemberInfoHistory.data = data;
    });

    apiService.get3rdPartyProvideHistory(params).then(function (data) {
      $scope.gridOptions3rdPartyProvideHistory.data = data;
    });

    apiService.getCardList(params).then(function (data) {
      $scope.gridOptionsCardList.data = data;
    });

    apiService.getTransactionHistory(params).then(function (data) {
      $scope.gridOptionsTransactionHistory.data = data;
    });

    apiService.getEmailSendHistory(params).then(function (data) {
      $scope.gridOptionsEmailSendHistory.data = data;
    });

    apiService.getAppPushHistory(params).then(function (data) {
      $scope.gridOptionsAppPushHistory.data = data;
    });
  };

}]);