'use strict';

angular.module('app').controller('QCTESTCtrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', 'authSvc',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, authSvc) {

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
            $scope.maskingAuth = userInfo.maskingYn;
        });

        $scope.isPtsDisabled = function () {
            return !$scope.ptsUsername
        };


        $scope.qcObject = {
            mbrId: null,
            extractTarget: null,
            extractCondition: null,
            standardDate: null,
            searchDateStart: null,
            searchDateEnd: null
        };

        $scope.gridOptionsQueryCacheTest = {
            flatEntityAccess: true,
            columnDefs: [
                {field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                {field: 'rcvDt', displayName: '접수일시', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'rcvSeq', displayName: '접수번호', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'apprDttm', displayName: '승인일시', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'repApprNo', displayName: '대표승인번호', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'apprNo', displayName: '승인번호', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'saleDttm', displayName: '매출일시', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'mbrId', displayName: '회원ID', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'cardDtlGrp', displayName: '카드그룹코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'cardNo', displayName: '카드번호', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'alcmpn', displayName: '발생제휴사', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'mcnt', displayName: '발생가맹점', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'stlmtMcnt', displayName: '정산가맹점', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'pntKnd', displayName: '포인트종류', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'slip', displayName: '전표', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'saleAmt', displayName: '매출금액', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                {field: 'pnt', displayName: '포인트', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                {field: 'csMbrCmmsn', displayName: '제휴사연회비', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                {field: 'cmmsn', displayName: '수수료', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                {field: 'pmntWay', displayName: '지불수단', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'org', displayName: '기관', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'oilPrdctSgrp', displayName: '유종', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'saleQty', displayName: '주유량', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' },
                {field: 'cpnPrd', displayName: '쿠폰', width: 100, cellTooltip: true, headerTooltip: true},
                {field: 'trxType', displayName: '거래종류', width: 100, cellTooltip: true, headerTooltip: true}
            ],
            onRegisterApi: function (gridApi) {
                //gridApi.grid.registerRowsProcessor($scope.singleFilter, 200);
                //$scope.transactionHistoryGridApi = gridApi;
            }
        };

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

        $scope.searchQueryCacheTest = function () {
            console.log('call::searchQueryCacheTest');
            console.log($scope.qcObject);
            //$scope.querycacheTestPromise = apiSvc.getQueryCacheTest($scope.qcObject);
            //$scope.querycacheTestPromise.then(function (data) {
            //    $scope.gridOptionsQueryCacheTest.data = data;
            //});
        };

        $scope.sendPts = function (ptsMasking, ptsPrefix) {
            $scope.sendPtsPromise = apiSvc.sendPts({
                ptsMasking: ptsMasking,
                ptsPrefix: ptsPrefix,
                //searchType: 'type',
                //searchKeyword: 'key...',
                options:$scope.qcObject
            });
        };


    }]);