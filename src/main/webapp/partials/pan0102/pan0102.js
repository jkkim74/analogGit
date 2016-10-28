'use strict';

angular.module('app').controller('PAN0102Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', '$uibModal',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, $uibModal) {

        // 조회 조건
        $scope.selectOptions = [
            { label: '회원ID', value: 'mbrId' },
            { label: '카드번호', value: 'cardNo' },
            { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' },
            { label: '메시지발송ID (검토중)', value: 'msgSndId' }
        ];

        // 기본 정보
        $scope.gridOptionsBasicInfo = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 1,
            columnDefs: [
                { field: 'mbrFgCd', displayName: '회원구분', cellTooltip: true, headerTooltip: true },
                { field: 'mbrStsCd', displayName: '회원상태', cellTooltip: true, headerTooltip: true },
                { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true },
                { field: 'ciNo', displayName: 'CI번호', width: 800, cellTooltip: true, headerTooltip: true },
                { field: 'pntExtnctYn', displayName: '포인트 소멸 여부', cellTooltip: true, headerTooltip: true }
            ]
        };

        // 회원 원장
        $scope.gridOptionsMemberInfo = {
            enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 1,
            columnDefs: [
                { field: 'mbrKorNm', displayName: '한글성명', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'leglBthdt', displayName: '법정생년월일', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'leglGndrFgCd', displayName: '성별', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'fstCardRregDt', displayName: 'OCB 최초 카드 본등록 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'clphnNo', displayName: '휴대전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'clphnNoDt', displayName: '휴대전화번호 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                {
                    field: 'clphnNoDupYn', displayName: '휴대전화번호 중복 여부', width: 100, cellTooltip: true, headerTooltip: true,
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'popup-cell\': row.entity.clphnNoDupYn === \'Y\'}" title="TOOLTIP" ng-click="row.entity.clphnNoDupYn === \'Y\' && grid.appScope.openDupModal(\'clphnNoDup\', row.entity.mbrId)">{{ COL_FIELD CUSTOM_FILTERS }}</div>'
                },
                { field: 'homeTelNo', displayName: '자택전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'homeTelNoDt', displayName: '자택전화번호 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNo', displayName: '직장전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNoDt', displayName: '직장전화번호 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'homeAddr', displayName: '자택주소', width: 500, cellTooltip: true, headerTooltip: true },
                { field: 'homeBasicAddrDt', displayName: '자택주소 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'jobpAddr', displayName: '직장주소', width: 500, cellTooltip: true, headerTooltip: true },
                { field: 'jobpBasicAddrDt', displayName: '직장주소 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddr', displayName: '이메일주소', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddrDt', displayName: '이메일주소 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                {
                    field: 'emailAddrDupYn', displayName: '이메일주소 중복 여부', width: 100, cellTooltip: true, headerTooltip: true,
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'popup-cell\': row.entity.emailAddrDupYn === \'Y\'}" title="TOOLTIP" ng-click="row.entity.emailAddrDupYn === \'Y\' && grid.appScope.openDupModal(\'emailAddrDupModal\', row.entity.mbrId)">{{ COL_FIELD CUSTOM_FILTERS }}</div>'
                },
                { field: 'destrExpctnDt', displayName: '개인정보 유효 기간 만료 예정일자', width: 100, cellTooltip: true, headerTooltip: true }
            ]
        };

        // 마케팅 동의 내역
        $scope.gridOptionsAgreementInfo = {
            enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 1,
            columnDefs: [
                { field: 'ocbMktngAgrmtYn', displayName: '마케팅 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'mktngFnlAgrmt', displayName: '마케팅 최종 동의 유입 출처/일자', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'entprMktngAgrmtYn', displayName: '교차 활용 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'tmRcvAgrmtYn', displayName: 'TM 수신 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'emailRcvAgrmtYn', displayName: '이메일 수신 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'advtSmsRcvAgrmtYn', displayName: '광고성 SMS 수신 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'ifrmtSmsRcvAgrmtYn', displayName: '정보성 SMS 수신 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'pushRcvAgrmtYn', displayName: '앱 푸시 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'pntUseRsvngPushAgrmtYn', displayName: '포인트 사용적립 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'bnftMlfPushAgrmtYn', displayName: '혜택/모바일전단 푸시 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'tusePushAgrmtYn', displayName: '친구와 함께쓰기 푸시 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'coinNotiPushAgrmtYn', displayName: '코인알림 푸시 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'locUtlzAgrmtYn', displayName: '위치활용 동의 여부', cellTooltip: true, headerTooltip: true },
                { field: 'blthAgrmtYn', displayName: 'BLE 동의 여부', cellTooltip: true, headerTooltip: true }
            ]
        };

        // 채널 가입 현황
        $scope.gridOptionsJoinInfoOcbapp = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 3,
            columnDefs: [
                { field: 'ocbAppJoinYn', displayName: 'OCB앱 가입 여부', cellTooltip: true, headerTooltip: true },
                { field: 'ocbappFnlEntrDt', displayName: 'OCB앱 최종 가입 일자', cellTooltip: true, headerTooltip: true },
                { field: 'ocbappFnlLgnDt', displayName: 'OCB앱 최종 로그인 일자', cellTooltip: true, headerTooltip: true },
            ]
        };
        $scope.gridOptionsJoinInfoOcbcom = {
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
            enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
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
            enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 1,
            columnDefs: [
                { field: 'mbrKorNm', displayName: '한글성명', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'clphnNo', displayName: '휴대전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'clphnNoFnlInfl', displayName: '휴대전화번호 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                {
                    field: 'clphnNoDupYn', displayName: '휴대전화번호 중복 여부', width: 100, cellTooltip: true, headerTooltip: true,
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'popup-cell\': row.entity.clphnNoDupYn === \'Y\'}" title="TOOLTIP" ng-click="row.entity.clphnNoDupYn === \'Y\' && grid.appScope.openDupModal(\'clphnNoDupModal\', row.entity.mbrId)">{{ COL_FIELD CUSTOM_FILTERS }}</div>'
                },
                { field: 'homeTelNo', displayName: '자택전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'homeTelNoFnlInfl', displayName: '자택전화번호 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNo', displayName: '직장전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNoFnlInfl', displayName: '직장전화번호 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'homeBasicAddr homeDtlAddr', displayName: '자택주소', width: 500, cellTooltip: true, headerTooltip: true },
                { field: 'homeDtlAddrFnlInfl', displayName: '자택주소 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'jobpBasicAddr jobpDtlAddr', displayName: '직장주소', width: 500, cellTooltip: true, headerTooltip: true },
                { field: 'jobpDtlAddrFnlInfl', displayName: '직장주소 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddr', displayName: '이메일주소', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddrFnlInfl', displayName: '이메일주소 최종 유입 출처/일자', width: 100, cellTooltip: true, headerTooltip: true },
                {
                    field: 'emailAddrDupYn', displayName: '이메일주소 중복 여부', width: 100, cellTooltip: true, headerTooltip: true,
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'popup-cell\': row.entity.emailAddrDupYn === \'Y\'}" title="TOOLTIP" ng-click="row.entity.emailAddrDupYn === \'Y\' && grid.appScope.openDupModal(\'emailAddrDupModal\', row.entity.mbrId)">{{ COL_FIELD CUSTOM_FILTERS }}</div>'
                }
            ]
        };

        // 마케팅 회원 원장 이력
        $scope.gridOptionsMarketingMemberInfoHistory = {
            flatEntityAccess: true,
            minRowsToShow: 5,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'dataSrcOrgCd', displayName: '유입기관코드', cellTooltip: true, headerTooltip: true },
                { field: 'agrmtVerCd', displayName: '동의버전', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'agrmtDt', displayName: '동의일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'agrmtYn', displayName: '동의여부', cellTooltip: true, headerTooltip: true },
                { field: 'mbrKorNm', displayName: '한글성명', cellTooltip: true, headerTooltip: true },
                { field: 'bthdt', displayName: '생년월일', cellTooltip: true, headerTooltip: true },
                { field: 'gndrFgCd', displayName: '성별', cellTooltip: true, headerTooltip: true },
                { field: 'emailAddr', displayName: '이메일주소', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'clphnNo', displayName: '휴대전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'homeTelNo', displayName: '자택전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNo', displayName: '직장전화번호', width: 150, cellTooltip: true, headerTooltip: true }
            ]
        };

        // 3자 제공 동의 이력
        $scope.gridOptionsThirdPartyProvideHistory = {
            flatEntityAccess: true,
            minRowsToShow: 5,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'dataDstOrgCd', displayName: '제공대상기관', cellTooltip: true, headerTooltip: true },
                { field: 'dataSrcOrgCd', displayName: '유입기관코드', cellTooltip: true, headerTooltip: true },
                { field: 'agrmtVerCd', displayName: '동의버전', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'agrmtDt', displayName: '동의일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'agrmtYn', displayName: '동의여부', cellTooltip: true, headerTooltip: true },
                { field: 'mbrKorNm', displayName: '한글성명', cellTooltip: true, headerTooltip: true },
                { field: 'bthdt', displayName: '생년월일', cellTooltip: true, headerTooltip: true },
                { field: 'gndrFgCd', displayName: '성별', cellTooltip: true, headerTooltip: true },
                { field: 'emailAddr', displayName: '이메일주소', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'clphnNo', displayName: '휴대전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'homeTelNo', displayName: '자택전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNo', displayName: '직장전화번호', width: 150, cellTooltip: true, headerTooltip: true }
            ]
        };

        // 보유 카드 목록
        $scope.gridOptionsCardList = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 5,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'cardDtlGrp', displayName: '카드코드 및 명칭', width: 300, cellTooltip: true, headerTooltip: true },
                { field: 'cardStsCd', displayName: '카드상태', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'cardNo', displayName: '카드번호', cellTooltip: true, headerTooltip: true },
                { field: 'issDt', displayName: '카드발급일자', cellTooltip: true, headerTooltip: true },
                { field: 'cardRregDt', displayName: '카드본등록일자', cellTooltip: true, headerTooltip: true }
            ]
        };

        // 거래 내역
        $scope.gridOptionsTransactionHistory = {
            flatEntityAccess: true,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'rcvDt', displayName: '접수일시', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'rcvSeq', displayName: '접수번호', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'apprDttm', displayName: '승인일시', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'repApprNo', displayName: '대표승인번호', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'apprNo', displayName: '승인번호', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'saleDttm', displayName: '매출일시', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'mbrId', displayName: '회원ID', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'cardDtlGrp', displayName: '카드그룹코드', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'cardNo', displayName: '카드번호', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'alcmpn', displayName: '발생제휴사', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'mcnt', displayName: '발생가맹점', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'stlmtMcnt', displayName: '정산가맹점', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'pntKnd', displayName: '포인트종류', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'slip', displayName: '전표', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'saleAmt', displayName: '매출금액', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'pnt', displayName: '포인트', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'csMbrCmmsn', displayName: '제휴사연회비', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'cmmsn', displayName: '수수료', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'pmntWay', displayName: '지불수단', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'org', displayName: '기관', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'oilPrdctSgrp', displayName: '유종', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'saleQty', displayName: '주유량', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                { field: 'cpnPrd', displayName: '쿠폰', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'trxType', displayName: '거래종류', width: 100, cellTooltip: true, headerTooltip: true }
            ],
            onRegisterApi: function (gridApi) {
                gridApi.grid.registerRowsProcessor($scope.singleFilter, 200);
                $scope.transactionHistoryGridApi = gridApi;
            }
        };

        $scope.singleFilter = function (renderableRows) {
            if ($scope.trxType !== '') {
                var matcher = new RegExp($scope.trxType);

                renderableRows.forEach(function (row) {
                    ['trxType'].forEach(function (field) {
                        if (!row.entity[field].match(matcher)) {
                            row.visible = false;
                        }
                    });
                });
            }
            return renderableRows;
        };

        $scope.changeTrxType = function () {
            $scope.transactionHistoryGridApi.grid.refresh();
        };

        // 이메일 발송 이력
        $scope.gridOptionsEmailSendHistory = {
            flatEntityAccess: true,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'sndDt', displayName: '발송일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'emailTitl', displayName: '이메일 제목', cellTooltip: true, headerTooltip: true },
                { field: 'lcptEmailSndRslt', displayName: '이메일 발송 결과', width: 100, cellTooltip: true, headerTooltip: true }
            ]
        };

        // 앱 푸시 이력
        $scope.gridOptionsAppPushHistory = {
            flatEntityAccess: true,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'sndDttm', displayName: '발송일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'msgTitl', displayName: '푸시 제목', cellTooltip: true, headerTooltip: true },
                { field: 'pushSndRslt', displayName: '푸시 결과', width: 100, cellTooltip: true, headerTooltip: true }
            ]
        };

        // 조회
        $scope.searchAll = function () {
            var params = {
                searchType: $scope.selectedOption.value,
                searchKeyword: $scope.searchKeyword
            };

            $scope.memberInfoPromise = apiSvc.getMemberInfo(params);
            $scope.memberInfoPromise.then(function (data) {
                $scope.gridOptionsBasicInfo.data = data;
                $scope.gridOptionsMemberInfo.data = data;
            });

            $scope.agreementInfoPromise = apiSvc.getAgreementInfo(params);
            $scope.agreementInfoPromise.then(function (data) {
                $scope.gridOptionsAgreementInfo.data = data;
            });

            $scope.joinInfoOcbappPromise = apiSvc.getJoinInfoOcbapp(params);
            $scope.joinInfoOcbappPromise.then(function (data) {
                $scope.gridOptionsJoinInfoOcbapp.data = data;
            });

            $scope.joinInfoOcbcomPromise = apiSvc.getJoinInfoOcbcom(params);
            $scope.joinInfoOcbcomPromise.then(function (data) {
                $scope.gridOptionsJoinInfoOcbcom.data = data;
            });

            $scope.latestUsageInfoPromise = apiSvc.getLastestUsageInfo(params);
            $scope.latestUsageInfoPromise.then(function (data) {
                $scope.gridOptionsLastestUsageInfo.data = data;
            });

            $scope.marketingMemberInfoPromise = apiSvc.getMarketingMemberInfo(params);
            $scope.marketingMemberInfoPromise.then(function (data) {
                $scope.gridOptionsMarketingMemberInfo.data = data;
            });

            $scope.memberInfoHistoryPromise = apiSvc.getMarketingMemberInfoHistory(params);
            $scope.memberInfoHistoryPromise.then(function (data) {
                $scope.gridOptionsMarketingMemberInfoHistory.data = data;
            });

            $scope.thirdPartyProvideHistoryPromise = apiSvc.getThirdPartyProvideHistory(params);
            $scope.thirdPartyProvideHistoryPromise.then(function (data) {
                $scope.gridOptionsThirdPartyProvideHistory.data = data;
            });

            $scope.cardListPromise = apiSvc.getCardList(params);
            $scope.cardListPromise.then(function (data) {
                $scope.gridOptionsCardList.data = data;
            });

            $scope.transactionHistoryPromise = apiSvc.getTransactionHistory(params);
            $scope.transactionHistoryPromise.then(function (data) {
                $scope.gridOptionsTransactionHistory.data = data;
            });

            $scope.emailSendHistoryPromise = apiSvc.getEmailSendHistory(params);
            $scope.emailSendHistoryPromise.then(function (data) {
                $scope.gridOptionsEmailSendHistory.data = data;
            });

            $scope.appPushHistoryPromise = apiSvc.getAppPushHistory(params);
            $scope.appPushHistoryPromise.then(function (data) {
                $scope.gridOptionsAppPushHistory.data = data;
            });
        };

        $scope.openDupModal = function (modalName, mbrId) {
            $uibModal.open({
                component: modalName,
                size: 'sm',
                resolve: {
                    mbrId: function () { return mbrId; }
                }
            });
        };

    }]);