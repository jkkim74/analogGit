'use strict';

angular.module('app').controller('QCTESTCtrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', 'authSvc',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, authSvc) {

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
            $scope.maskingAuth = userInfo.maskingYn;
        });

        $scope.canSearch = true;
        $scope.qcObject = {
            memberId: null,
            extractTarget: null,
            extractCond: null,
            periodType: null,
            periodFrom: null,
            periodTo: null
        };

        $scope.qcObjectClear= function() {
            $scope.qcObject.memberId = null;
            $scope.qcObject.extractTarget = null;
            $scope.qcObject.extractCond = null;
            $scope.qcObject.periodType = null;
            $scope.qcObject.periodFrom = null;
            $scope.qcObject.periodTo= null;
        };

        $scope.qcObjectSample1 = function() {
            $scope.qcObject.memberId = 101700393;
            $scope.qcObject.extractTarget = 'tr_mbrKorNm';
            $scope.qcObject.extractCond = 't';
            $scope.qcObject.periodType = 'sale_dt';
            $scope.qcObject.periodFrom = '20170101';
            $scope.qcObject.periodTo= '20170303';
        };

        $scope.qcObjectSample2= function() {
            $scope.qcObject.memberId = 101700393;
            $scope.qcObject.extractTarget = 'tr';
            $scope.qcObject.extractCond = 't';
            $scope.qcObject.periodType = 'sale_dt';
            $scope.qcObject.periodFrom = '20170301';
            $scope.qcObject.periodTo= '20170306';
        };

        $scope.gridOptionsQueryCacheTest = {
            flatEntityAccess: true,
            columnDefs: [
                {field:'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                {field:'rcvDt', displayName:'접수일자', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'apprDttm', displayName:'승인일시', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'repApprNo', displayName:'대표승인번호', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'apprNo', displayName:'승인번호', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'saleDttm', displayName:'매출일시', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'mbrId', displayName:'회원ID', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'cardDtlGrpCd', displayName:'카드코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'dtlCdNm', displayName:'카드코드명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'cardNo', displayName:'카드번호', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'stlmtAlcmpnCd', displayName:'정산제휴사코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'alcmpnNm', displayName:'정산제휴사명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'stlmtMcnt_cd', displayName:'정산가맹점코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'mcntNm', displayName:'정산가맹점명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'alcmpnCd', displayName:'발생제휴사코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'alcmpnNm2', displayName:'발생제휴사명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'mcntCd', displayName:'발생가맹점코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'mcntNm2', displayName:'발생가맹점명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'pnt_knd_cd', displayName:'포인트종류코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'dtlCdNm2', displayName:'포인트종류명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'slipCd', displayName:'전표코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'dtlCdNm3', displayName:'전표명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'saleAmt', displayName:'매출금액', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'pnt', displayName:'포인트', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'csMbrCmmsn', displayName:'제휴사연회비', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'cmmsn', displayName:'수수료', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'pmntWayCd', displayName:'지불수단코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'dtlCdNm4', displayName:'지불수단명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'orgCd', displayName:'기관코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'dtlCdNm5', displayName:'기관명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'oilPrdctSgrpCd', displayName:'유종코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'dtlCdNm6', displayName:'유종명', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'cpnPrdCd', displayName:'쿠폰코드', width: 100, cellTooltip: true, headerTooltip: true},
                {field:'prdNm', displayName:'쿠폰명', width: 100, cellTooltip: true, headerTooltip: true}
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

        $scope.searchQcTest = function () {
            console.log('call::searchQcTest');
            console.log($scope.qcObject);

            var nStart = new Date().getTime();
            $scope.canSearch = false;
            $scope.gridOptionsQueryCacheTest.data = null;

            $scope.querycacheTestPromise = apiSvc.getQueryCacheTest($scope.qcObject);
            $scope.querycacheTestPromise.then(function (data) {
                var nEnd = new Date().getTime();
                var nDiff = nEnd - nStart;
                console.log(nDiff + "ms");
                //$timeout(function () {
                    $scope.gridOptionsQueryCacheTest.data = data;
                    $scope.canSearch = true;
                //}, 3000, true);
            });
        };

        $scope.sendPts = function (ptsMasking, ptsPrefix) {
            console.log('call::sendPts in qctest page');
            console.log($scope.qcObject);
            $scope.sendPtsPromise = apiSvc.sendPts({
                ptsMasking: ptsMasking,
                ptsPrefix: ptsPrefix,
                memberId: $scope.qcObject.memberId,
                extractTarget: $scope.qcObject.extractTarget,
                extractCond: $scope.qcObject.extractCond,
                periodType: $scope.qcObject.periodType,
                periodFrom: $scope.qcObject.periodFrom,
                periodTo: $scope.qcObject.periodTo
            });
        };

        $scope.isPtsDisabled = function () {
            return !($scope.qcObject.memberId
                        && $scope.qcObject.extractTarget
                        && $scope.qcObject.extractCond
                        && $scope.qcObject.periodType
                        && $scope.qcObject.periodFrom
                        && $scope.qcObject.periodTo);
        };

    }]);