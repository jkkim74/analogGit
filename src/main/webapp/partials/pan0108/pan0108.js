/**
 * Created by 1003899 on 2017. 4. 3..
 */

'use strict';

angular.module('app').controller('PAN0108Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', 'authSvc','$filter','toastr',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, authSvc,$filter,toastr) {

        //이메일 제목 연계 검색
        $scope.searchCond = {
            onHive:false,   //연계검색
            emailAddr:null, //이메일주소
            sameType:null,   //이메일주소 일치여부
            startDate:null, //조회기간 시작
            endDate:null,   //조회기간 종료
            emailTitle:null//이메일 제목
        };

        // 조회 조건
        $scope.selectEqualOptions = [
            {label: '전체일치', value: 'all'},
            {label: 'ID일치', value: 'id'}
        ];

        $scope.gridOptionsTable1 = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 3,
            columnDefs: [
                {field: 'mbrId', displayName: 'MBR_ID', cellTooltip: true, headerTooltip: true},
                {field: 'unitedId', displayName: 'OCBCOM 로그인 ID', cellTooltip: true, headerTooltip: true},
                {field: 'emailAddr', displayName: '이메일 주소', cellTooltip: true, headerTooltip: true},
                {field: 'emailAddrDupYn', displayName: '중복여부', cellTooltip: true, headerTooltip: true},
                {field: 'martUpdDttm', displayName: '최종원장 변경일자', cellTooltip: true, headerTooltip: true}
            ]
        };
        $scope.gridOptionsTable2 = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 3,
            columnDefs: [
                {field: 'mbrId', displayName: 'MBR_ID', cellTooltip: true, headerTooltip: true},
                {field: 'unitedId', displayName: 'OCBCOM 로그인 ID', cellTooltip: true, headerTooltip: true},
                {field: 'emailAddr', displayName: '이메일 주소', cellTooltip: true, headerTooltip: true},
                {field: 'emailAddrDupYn', displayName: '중복여부', cellTooltip: true, headerTooltip: true},
                {field: 'martUpdDttm', displayName: '최종원장 변경일자', cellTooltip: true, headerTooltip: true}
            ]
        };
        $scope.gridOptionsTable3 = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            flatEntityAccess: true,
            minRowsToShow: 3,
            columnDefs: [
                {field: 'mbrId', displayName: 'MBR_ID', cellTooltip: true, headerTooltip: true},
                {field: 'unitedId', displayName: 'OCBCOM 로그인 ID', cellTooltip: true, headerTooltip: true},
                {field: 'emailAddr', displayName: '이메일 주소', cellTooltip: true, headerTooltip: true},
                {field: 'minCustRegDt', displayName: '최소일자', cellTooltip: true, headerTooltip: true},
                {field: 'maxCustRegDt', displayName: '최종일자', cellTooltip: true, headerTooltip: true}
            ]
        };

        function makeSearchEmailParams() {
            return {
                onHive: $scope.searchCond.onHive,
                emailAddr: ($scope.searchCond.sameType != 'id') ? $scope.searchCond.emailAddr : ($scope.searchCond.emailAddr).split('@')[0],
                sameType: $scope.searchCond.sameType,
                startDate: $filter('date')($scope.searchCond.startDate, 'yyyyMMdd'),
                endDate: $filter('date')($scope.searchCond.endDate, 'yyyyMMdd'),
                emailTitle: $scope.searchCond.emailTitle
            };
        }

        // 조회
        $scope.searchEmail = function () {

            if ($scope.emailSearchForm.$invalid) {
                toastr.warning('이메일주소는 필수입력항목입니다.');
                return;
            }

            var params = makeSearchEmailParams();

            console.log('Search email params:',params);

            $scope.gridOptionsTable1.data = null;
            $scope.gridOptionsTable2.data = null;
            $scope.gridOptionsTable3.data = null;

            $scope.table1Promise = apiSvc.getMemberLedger(params);
            $scope.table2Promise = apiSvc.getMarketingLedger(params);
            $scope.table3Promise = apiSvc.getMarketingHistory(params);

            $scope.table1Promise.then(function (data) {
                $scope.gridOptionsTable1.data = data;
                // console.log(data);
            });
            $scope.table2Promise.then(function (data) {
                $scope.gridOptionsTable2.data = data;
                // console.log(data);
            });
            $scope.table3Promise.then(function (data) {
                $scope.gridOptionsTable3.data = data;
                // console.log(data);
            });
        };

        $scope.sendPts = function (ptsMasking, ptsPrefix) {

            var params = makeSearchEmailParams();
            console.log(params);

            if ($scope.emailSearchForm.$invalid) {
                toastr.warning('조회 조건을 확인하세요!!');
                return;
            }

            $scope.sendPtsPromise = apiSvc.sendPts({
                ptsMasking: ptsMasking,
                ptsPrefix: ptsPrefix,
                onHive: params.onHive,
                emailAddr: params.emailAddr,
                sameType: params.sameType,
                startDate: params.startDate,
                endDate: params.endDate,
                emailTitle: params.emailTitle
            });
        };

        $scope.isPtsDisabled = function () {
            // return $scope.emailSearchForm.$invalid;
            return false;
        };

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
            $scope.maskingAuth = userInfo.maskingYn;
        });

    }]);
