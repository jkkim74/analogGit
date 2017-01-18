'use strict';

angular.module('app').controller('PAN0107Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', 'authSvc',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, authSvc) {

        // 조회 조건
        $scope.selectOptions = [
            { label: '회원ID', value: 'mbrId' },
            { label: '카드번호', value: 'cardNo' },
            { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' }
        ];

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

        // 거래 내역
        // $scope.gridOptionsTransactionHistory = {
        //     flatEntityAccess: true,
        //     columnDefs: [
        //         { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
        //         { field: 'rcvDt', displayName: '접수일시', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'rcvSeq', displayName: '접수번호', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'apprDttm', displayName: '승인일시', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'repApprNo', displayName: '대표승인번호', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'apprNo', displayName: '승인번호', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'saleDttm', displayName: '매출일시', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'mbrId', displayName: '회원ID', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'cardDtlGrp', displayName: '카드그룹코드', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'cardNo', displayName: '카드번호', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'alcmpn', displayName: '발생제휴사', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'mcnt', displayName: '발생가맹점', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'stlmtMcnt', displayName: '정산가맹점', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'pntKnd', displayName: '포인트종류', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'slip', displayName: '전표', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'saleAmt', displayName: '매출금액', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'pnt', displayName: '포인트', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'csMbrCmmsn', displayName: '제휴사연회비', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'cmmsn', displayName: '수수료', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'pmntWay', displayName: '지불수단', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'org', displayName: '기관', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'oilPrdctSgrp', displayName: '유종', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'saleQty', displayName: '주유량', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
        //         { field: 'cpnPrd', displayName: '쿠폰', width: 100, cellTooltip: true, headerTooltip: true },
        //         { field: 'trxType', displayName: '거래종류', width: 100, cellTooltip: true, headerTooltip: true }
        //     ],
        //     onRegisterApi: function (gridApi) {
        //         gridApi.grid.registerRowsProcessor($scope.singleFilter, 200);
        //         $scope.transactionHistoryGridApi = gridApi;
        //     }
        // };

        // $scope.singleFilter = function (renderableRows) {
        //     if ($scope.trxType !== '') {
        //         var matcher = new RegExp($scope.trxType);

        //         renderableRows.forEach(function (row) {
        //             ['trxType'].forEach(function (field) {
        //                 if (!row.entity[field].match(matcher)) {
        //                     row.visible = false;
        //                 }
        //             });
        //         });
        //     }
        //     return renderableRows;
        // };

        // $scope.changeTrxType = function () {
        //     $scope.transactionHistoryGridApi.grid.refresh();
        // };

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

            $scope.emailSendHistoryPromise = apiSvc.getEmailSendHistory(params);
            $scope.emailSendHistoryPromise.then(function (data) {
                $scope.gridOptionsEmailSendHistory.data = data;
            });

            $scope.appPushHistoryPromise = apiSvc.getAppPushHistory(params);
            $scope.appPushHistoryPromise.then(function (data) {
                $scope.gridOptionsAppPushHistory.data = data;
            });
        };
        
        $scope.sendPts = function (ptsMasking, ptsPrefix) {
            $scope.sendPtsPromise = apiSvc.sendPts({ ptsMasking: ptsMasking, ptsPrefix: ptsPrefix, searchType: $scope.selectedOption.value, searchKeyword: $scope.searchKeyword });
        };

        $scope.isPtsDisabled = function () {
            return !$scope.ptsUsername 
            || (!$scope.gridOptionsAgreementInfo.data.length && !$scope.gridOptionsJoinInfoOcbapp.data.length && !$scope.gridOptionsJoinInfoOcbcom.data.length
            	&& !$scope.gridOptionsEmailSendHistory.data.length && !$scope.gridOptionsAppPushHistory.data.length);
        };

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
        });         

        // 거래내역 조회
        // $scope.searchTransactionHistory = function () {
        //     var params = {
        //         searchType: $scope.selectedOption2.value,
        //         searchKeyword: $scope.searchKeyword2
        //     };

        //     $scope.transactionHistoryPromise = apiSvc.getTransactionHistory(params);
        //     $scope.transactionHistoryPromise.then(function (data) {
        //         $scope.gridOptionsTransactionHistory.data = data;
        //     });
        // }

    }]);