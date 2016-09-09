'use strict';

App.controller('Pan0102Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiService', function ($scope, $q, $http, $timeout, uiGridConstants, apiService) {

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
      { field: 'mbrType', displayName: '회원구분', cellTooltip: true, headerTooltip: true },
      { field: 'mbrStsCd', displayName: '회원상태', cellTooltip: true, headerTooltip: true },
      { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
      { field: 'custId', displayName: 'CUST_ID', cellTooltip: true, headerTooltip: true },
      { field: 'ciNo', displayName: 'CI번호', cellTooltip: true, headerTooltip: true },
      { field: 'pntExtnctYn', displayName: '포인트 소멸 여부', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 회원 원장
  $scope.gridOptionsMemberInfo = {
    enableColumnMenus: false,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'mbrKorNm', displayName: '한글성명', cellTooltip: true, headerTooltip: true },
      { field: 'leglBthdt', displayName: '법정생년월일', cellTooltip: true, headerTooltip: true },
      { field: 'leglGndrFgCd', displayName: '성별', cellTooltip: true, headerTooltip: true },
      { field: 'fstCardRregDt', displayName: 'OCB 최초 카드 본등록 일자', cellTooltip: true, headerTooltip: true },
      { field: 'clphnNo', displayName: '휴대전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'clphnNoDt', displayName: '휴대전화번호 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'clphnNoDupYn', displayName: '휴대전화번호 중복 여부', cellTooltip: true, headerTooltip: true },
      { field: 'homeTelNo', displayName: '자택전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'homeTelNoDt', displayName: '자택전화번호 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'jobpTelNo', displayName: '직장전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'jobpTelNoDt', displayName: '직장전화번호 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'homeAddr', displayName: '자택주소', cellTooltip: true, headerTooltip: true },
      { field: 'homeBasicAddrDt', displayName: '자택주소 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'jobpAddr', displayName: '직장주소', cellTooltip: true, headerTooltip: true },
      { field: 'jobpBasicAddrDt', displayName: '직장주소 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'emailAddr', displayName: '이메일주소', cellTooltip: true, headerTooltip: true },
      { field: 'emailAddrDt', displayName: '이메일주소 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'emailAddrDupYn', displayName: '이메일주소 중복 여부', cellTooltip: true, headerTooltip: true },
      { field: 'destrExpctnDt', displayName: '개인정보 유효 기간 만료 예정일자', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 마케팅 동의 내역
  $scope.gridOptionsAgreementInfo = {
    enableColumnMenus: false,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'ocbMktngAgrmtYn', displayName: '마케팅 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'mktngFnlAgrmt', displayName: '마케팅 최종 동의 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'entprMktngAgrmtYn', displayName: '교차 활용 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'tmRcvAgrmtYn', displayName: 'TM 수신 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'emailRcvAgrmtYn', displayName: 'EM 수신 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'advtSmsRcvAgrmtYn', displayName: '광고성 SMS 수신 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'ifrmtSmsRcvAgrmtYn', displayName: '정보성 SMS 수신 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'pushRcvAgrmtYn', displayName: '앱 푸쉬 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'pntUseRsvngPushAgrmtYn', displayName: '포인트 사용적립 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'bnftMlfPushAgrmtYn', displayName: '혜택/모바일전단 푸쉬 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'tusePushAgrmtYn', displayName: '친구와 함께쓰기 푸쉬 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'coinNotiPushAgrmtYn', displayName: '동전 푸쉬 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'locUtlzAgrmtYn', displayName: '위치 정보 활용 동의 여부', cellTooltip: true, headerTooltip: true },
      { field: 'blthAgrmtYn', displayName: 'BLE 동의 여부', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 채널 가입 현황
  $scope.gridOptionsJoinInfoOcbapp = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 3,
    columnDefs: [
      { field: 'ocbappYn', displayName: 'OCB앱 이용 여부', cellTooltip: true, headerTooltip: true },
      { field: 'ocbappFnlEntrDt', displayName: 'OCB앱 최종 가입 일자', cellTooltip: true, headerTooltip: true },
      { field: 'ocbappFnlLgnDt', displayName: 'OCB앱 최종 로그인 일자', cellTooltip: true, headerTooltip: true },
    ]
  };
  $scope.gridOptionsJoinInfoOcbcom = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 3,
    columnDefs: [
      { field: 'unitedId', displayName: 'OCB닷컴 United ID', cellTooltip: true, headerTooltip: true },
      { field: 'lgnId', displayName: 'OCB닷컴 로그인ID', cellTooltip: true, headerTooltip: true },
      { field: 'entrDttm', displayName: 'OCB닷컴 가입 일자', cellTooltip: true, headerTooltip: true },
      { field: 'fnlLgnDttm', displayName: 'OCB닷컴 최종 로그인 일자', cellTooltip: true, headerTooltip: true }
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
      { field: 'fnlErngDt', displayName: 'OCB 적립/사용/할인/현금환급', cellTooltip: true, headerTooltip: true },
      { field: 'fnlCardRregDt', displayName: 'OCB 카드 본등록', cellTooltip: true, headerTooltip: true },
      { field: 'fnlCnsofEnqDt', displayName: '상담실 문의', cellTooltip: true, headerTooltip: true },
      { field: 'fnlPrdPchsDt', displayName: '상품 구매', cellTooltip: true, headerTooltip: true },
      { field: 'fnlPntInqDt', displayName: '포인트 조회', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 마케팅 회원 원장
  $scope.gridOptionsMarketingMemberInfo = {
    enableColumnMenus: false,
    enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 1,
    columnDefs: [
      { field: 'mbrKorNm', displayName: '한글성명', cellTooltip: true, headerTooltip: true },
      { field: 'clphnNo', displayName: '휴대전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'clphnNoDt', displayName: '휴대전화번호 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'homeTelNo', displayName: '자택전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'homeTelNoDt', displayName: '자택전화번호 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'jobpTelNo', displayName: '직장전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'jobpTelNoDt', displayName: '직장전화번호 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'homeBasicAddr homeDtlAddr', displayName: '자택주소', cellTooltip: true, headerTooltip: true },
      { field: 'homeDtlAddrDt', displayName: '자택주소 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'jobpBasicAddr jobpDtlAddr', displayName: '직장주소', cellTooltip: true, headerTooltip: true },
      { field: 'jobpDtlAddrDt', displayName: '직장주소 최종 유입 출처/일자', cellTooltip: true, headerTooltip: true },
      { field: 'emailAddr', displayName: '이메일주소', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 마케팅 회원 원장 이력
  $scope.gridOptionsMarketingMemberInfoHistory = {
    enableColumnMenus: false,
    flatEntityAccess: true,
    minRowsToShow: 5,
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'dataSrcOrgCd', displayName: '유입기관코드', cellTooltip: true, headerTooltip: true },
      { field: 'agrmtVerCd', displayName: '동의버전', cellTooltip: true, headerTooltip: true },
      { field: 'agrmtDt', displayName: '동의일자', cellTooltip: true, headerTooltip: true },
      { field: 'agrmtYn', displayName: '동의여부', cellTooltip: true, headerTooltip: true },
      { field: 'mbrKorNm', displayName: '한글성명', cellTooltip: true, headerTooltip: true },
      { field: 'bthdt', displayName: '생년월일', cellTooltip: true, headerTooltip: true },
      { field: 'gndrFgCd', displayName: '성별', cellTooltip: true, headerTooltip: true },
      { field: 'emailAddr', displayName: '이메일주소', cellTooltip: true, headerTooltip: true },
      { field: 'clphnNo', displayName: '휴대전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'homeTelNo', displayName: '자택전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'jobpTelNo', displayName: '직장전화번호', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 3자 제공 동의 이력
  $scope.gridOptionsThirdPartyProvideHistory = {
    enableColumnMenus: false,
    flatEntityAccess: true,
    minRowsToShow: 5,
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'dataDstOrgCd', displayName: '제공대상기관', cellTooltip: true, headerTooltip: true },
      { field: 'dataSrcOrgCd', displayName: '유입기관코드', cellTooltip: true, headerTooltip: true },
      { field: 'agrmtVerCd', displayName: '동의버전', cellTooltip: true, headerTooltip: true },
      { field: 'agrmtDt', displayName: '동의일자', cellTooltip: true, headerTooltip: true },
      { field: 'agrmtYn', displayName: '동의여부', cellTooltip: true, headerTooltip: true },
      { field: 'mbrKorNm', displayName: '한글성명', cellTooltip: true, headerTooltip: true },
      { field: 'bthdt', displayName: '생년월일', cellTooltip: true, headerTooltip: true },
      { field: 'gndrFgCd', displayName: '성별', cellTooltip: true, headerTooltip: true },
      { field: 'emailAddr', displayName: '이메일주소', cellTooltip: true, headerTooltip: true },
      { field: 'clphnNo', displayName: '휴대전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'homeTelNo', displayName: '자택전화번호', cellTooltip: true, headerTooltip: true },
      { field: 'jobpTelNo', displayName: '직장전화번호', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 보유 카드 목록
  $scope.gridOptionsCardList = {
    enableColumnMenus: false,
    enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
    flatEntityAccess: true,
    minRowsToShow: 5,
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'cardDtlGrp', displayName: '카드코드 및 명칭', cellTooltip: true, headerTooltip: true },
      { field: 'cardStsCd', displayName: '카드상태', cellTooltip: true, headerTooltip: true },
      { field: 'cardNo', displayName: '카드번호', cellTooltip: true, headerTooltip: true },
      { field: 'issDt', displayName: '카드발급일자', cellTooltip: true, headerTooltip: true },
      { field: 'cardRregDt', displayName: '카드본등록일자', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 거래 내역
  $scope.gridOptionsTransactionHistory = {
    enableColumnMenus: false,
    flatEntityAccess: true,
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'rcvDt', displayName: '접수일시', cellTooltip: true, headerTooltip: true },
      { field: 'rcvSeq', displayName: '접수번호', cellTooltip: true, headerTooltip: true },
      { field: 'apprDttm', displayName: '승인일시', cellTooltip: true, headerTooltip: true },
      { field: 'repApprNo', displayName: '대표승인번호', cellTooltip: true, headerTooltip: true },
      { field: 'apprNo', displayName: '승인번호', cellTooltip: true, headerTooltip: true },
      { field: 'saleDttm', displayName: '매출일시', cellTooltip: true, headerTooltip: true },
      { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
      { field: 'cardDtlGrp', displayName: '카드그룹코드', cellTooltip: true, headerTooltip: true },
      { field: 'cardNo', displayName: '카드번호', cellTooltip: true, headerTooltip: true },
      { field: 'alcmpn', displayName: '발생제휴사', cellTooltip: true, headerTooltip: true },
      { field: 'mcnt', displayName: '발생가맹점', cellTooltip: true, headerTooltip: true },
      { field: 'stlmtMcnt', displayName: '정산가맹점', cellTooltip: true, headerTooltip: true },
      { field: 'pntKnd', displayName: '포인트종류', cellTooltip: true, headerTooltip: true },
      { field: 'slip', displayName: '전표', cellTooltip: true, headerTooltip: true },
      { field: 'saleAmt', displayName: '매출금액', cellTooltip: true, headerTooltip: true },
      { field: 'pnt', displayName: '포인트', cellTooltip: true, headerTooltip: true },
      { field: 'csMbrCmmsn', displayName: '제휴사연회비', cellTooltip: true, headerTooltip: true },
      { field: 'cmmsn', displayName: '수수료', cellTooltip: true, headerTooltip: true },
      { field: 'pmntWay', displayName: '지불수단', cellTooltip: true, headerTooltip: true },
      { field: 'org', displayName: '기관', cellTooltip: true, headerTooltip: true },
      { field: 'oilPrdctSgrp', displayName: '유종', cellTooltip: true, headerTooltip: true },
      { field: 'saleQty', displayName: '주유량', cellTooltip: true, headerTooltip: true },
      { field: 'cpnPrd', displayName: '쿠폰', cellTooltip: true, headerTooltip: true },
      { field: 'trxType', displayName: '거래종류', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 이메일 발송 이력
  $scope.gridOptionsEmailSendHistory = {
    enableColumnMenus: false,
    flatEntityAccess: true,
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'sndDt', displayName: '발송일자', cellTooltip: true, headerTooltip: true },
      { field: 'emailTitl', displayName: '이메일 제목', cellTooltip: true, headerTooltip: true },
      { field: 'lcptEmailSndRsltCd', displayName: '이메일 발송 결과', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 앱 푸쉬 이력
  $scope.gridOptionsAppPushHistory = {
    enableColumnMenus: false,
    flatEntityAccess: true,
    columnDefs: [
      { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
      { field: 'sndDttm', displayName: '발송일자', cellTooltip: true, headerTooltip: true },
      { field: 'msgTitl', displayName: '푸쉬 제목', cellTooltip: true, headerTooltip: true },
      { field: 'pushSndRslt', displayName: '푸쉬 결과', cellTooltip: true, headerTooltip: true }
    ]
  };

  // 조회
  $scope.searchAll = function (searchType, searchKeyword) {
    var params = { searchType: searchType, searchKeyword: searchKeyword };

    $scope.memberInfoPromise = apiService.getMemberInfo(params);
    $scope.memberInfoPromise.then(function (data) {
      $scope.gridOptionsBasicInfo.data = data;
      $scope.gridOptionsMemberInfo.data = data;
    });

    $scope.agreementInfoPromise = apiService.getAgreementInfo(params);
    $scope.agreementInfoPromise.then(function (data) {
      $scope.gridOptionsAgreementInfo.data = data;
    });

    $scope.joinInfoOcbappPromise = apiService.getJoinInfoOcbapp(params);
    $scope.joinInfoOcbappPromise.then(function (data) {
      $scope.gridOptionsJoinInfoOcbapp.data = data;
    });

    $scope.joinInfoOcbcomPromise = apiService.getJoinInfoOcbcom(params);
    $scope.joinInfoOcbcomPromise.then(function (data) {
      $scope.gridOptionsJoinInfoOcbcom.data = data;
    });

    $scope.latestUsageInfoPromise = apiService.getLastestUsageInfo(params);
    $scope.latestUsageInfoPromise.then(function (data) {
      $scope.gridOptionsLastestUsageInfo.data = data;
    });

    // $scope.marketingMemberInfoPromise = apiService.getMarketingMemberInfo(params);
    // $scope.marketingMemberInfoPromise.then(function (data) {
    //   $scope.gridOptionsMarketingMemberInfo.data = data;
    // });

    $scope.memberInfoHistoryPromise = apiService.getMarketingMemberInfoHistory(params);
    $scope.memberInfoHistoryPromise.then(function (data) {
      $scope.gridOptionsMarketingMemberInfoHistory.data = data;
    });

    $scope.thirdPartyProvideHistoryPromise = apiService.getThirdPartyProvideHistory(params);
    $scope.thirdPartyProvideHistoryPromise.then(function (data) {
      $scope.gridOptionsThirdPartyProvideHistory.data = data;
    });

    $scope.cardListPromise = apiService.getCardList(params);
    $scope.cardListPromise.then(function (data) {
      $scope.gridOptionsCardList.data = data;
    });

    $scope.transactionHistoryPromise = apiService.getTransactionHistory(params);
    $scope.transactionHistoryPromise.then(function (data) {
      $scope.gridOptionsTransactionHistory.data = data;
    });

    $scope.emailSendHistoryPromise = apiService.getEmailSendHistory(params);
    $scope.emailSendHistoryPromise.then(function (data) {
      $scope.gridOptionsEmailSendHistory.data = data;
    });

    $scope.appPushHistoryPromise = apiService.getAppPushHistory(params);
    $scope.appPushHistoryPromise.then(function (data) {
      $scope.gridOptionsAppPushHistory.data = data;
    });
  };

}]);