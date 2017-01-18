'use strict';

angular.module('app').controller('PAN0102Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', '$uibModal', 'authSvc',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, $uibModal, authSvc) {

        // 조회 조건
        $scope.selectOptions = [
            { label: '회원ID', value: 'mbrId' },
            { label: '카드번호', value: 'cardNo' },
            { label: 'OCB닷컴 로그인ID', value: 'ocbcomLgnId' }
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
                { field: 'clphnNoDt', displayName: '휴대전화번호 최종 유입 일자', width: 200, cellTooltip: true, headerTooltip: true },
                {
                    field: 'clphnNoDupYn', displayName: '휴대전화번호 중복 여부', width: 100, cellTooltip: true, headerTooltip: true,
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'popup-cell\': row.entity.clphnNoDupYn === \'Y\'}" role="button" title="TOOLTIP" ng-click="row.entity.clphnNoDupYn === \'Y\' && grid.appScope.openDupModal(\'clphnNoDupModal\', row.entity.mbrId)">{{ COL_FIELD CUSTOM_FILTERS }}</div>'
                },
                { field: 'homeTelNo', displayName: '자택전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'homeTelNoDt', displayName: '자택전화번호 최종 유입 일자', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNo', displayName: '직장전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNoDt', displayName: '직장전화번호 최종 유입 일자', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'homeAddr', displayName: '자택주소', width: 500, cellTooltip: true, headerTooltip: true },
                { field: 'homeBasicAddrDt', displayName: '자택주소 최종 유입 일자', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'jobpAddr', displayName: '직장주소', width: 500, cellTooltip: true, headerTooltip: true },
                { field: 'jobpBasicAddrDt', displayName: '직장주소 최종 유입 일자', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddr', displayName: '이메일주소', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddrDt', displayName: '이메일주소 최종 유입 일자', width: 200, cellTooltip: true, headerTooltip: true },
                {
                    field: 'emailAddrDupYn', displayName: '이메일주소 중복 여부', width: 100, cellTooltip: true, headerTooltip: true,
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'popup-cell\': row.entity.emailAddrDupYn === \'Y\'}" role="button" title="TOOLTIP" ng-click="row.entity.emailAddrDupYn === \'Y\' && grid.appScope.openDupModal(\'emailAddrDupModal\', row.entity.mbrId)">{{ COL_FIELD CUSTOM_FILTERS }}</div>'
                },
                { field: 'destrExpctnDt', displayName: '개인정보 유효 기간 만료 예정일자', width: 100, cellTooltip: true, headerTooltip: true }
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
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'popup-cell\': row.entity.clphnNoDupYn === \'Y\'}" role="button" title="TOOLTIP" ng-click="row.entity.clphnNoDupYn === \'Y\' && grid.appScope.openDupModal(\'clphnNoDupModal\', row.entity.mbrId)">{{ COL_FIELD CUSTOM_FILTERS }}</div>'
                },
                { field: 'homeTelNo', displayName: '자택전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'homeTelNoFnlInfl', displayName: '자택전화번호 최종 유입 출처/일자', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNo', displayName: '직장전화번호', width: 150, cellTooltip: true, headerTooltip: true },
                { field: 'jobpTelNoFnlInfl', displayName: '직장전화번호 최종 유입 출처/일자', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'homeAddr', displayName: '자택주소', width: 500, cellTooltip: true, headerTooltip: true },
                { field: 'homeAddrFnlInfl', displayName: '자택주소 최종 유입 출처/일자', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'jobpAddr', displayName: '직장주소', width: 500, cellTooltip: true, headerTooltip: true },
                { field: 'jobpAddrFnlInfl', displayName: '직장주소 최종 유입 출처/일자', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddr', displayName: '이메일주소', width: 200, cellTooltip: true, headerTooltip: true },
                { field: 'emailAddrFnlInfl', displayName: '이메일주소 최종 유입 출처/일자', width: 200, cellTooltip: true, headerTooltip: true },
                {
                    field: 'emailAddrDupYn', displayName: '이메일주소 중복 여부', width: 100, cellTooltip: true, headerTooltip: true,
                    cellTemplate: '<div class="ui-grid-cell-contents" ng-class="{\'popup-cell\': row.entity.emailAddrDupYn === \'Y\'}" role="button" title="TOOLTIP" ng-click="row.entity.emailAddrDupYn === \'Y\' && grid.appScope.openDupModal(\'emailAddrDupModal\', row.entity.mbrId)">{{ COL_FIELD CUSTOM_FILTERS }}</div>'
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

            $scope.latestUsageInfoPromise = apiSvc.getLastestUsageInfo(params);
            $scope.latestUsageInfoPromise.then(function (data) {
                $scope.gridOptionsLastestUsageInfo.data = data;
            });

            $scope.marketingMemberInfoPromise = apiSvc.getMarketingMemberInfo(params);
            $scope.marketingMemberInfoPromise.then(function (data) {
                $scope.gridOptionsMarketingMemberInfo.data = data;
            });

            $scope.marketingMemberInfoHistoryPromise = apiSvc.getMarketingMemberInfoHistory(params);
            $scope.marketingMemberInfoHistoryPromise.then(function (data) {
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
        
        $scope.sendPts = function (ptsMasking, ptsPrefix) {
            $scope.sendPtsPromise = apiSvc.sendPts({ ptsMasking: ptsMasking, ptsPrefix: ptsPrefix, searchType: $scope.selectedOption.value, searchKeyword: $scope.searchKeyword });
        };

        $scope.isPtsDisabled = function () {
            return !$scope.ptsUsername 
            || (!$scope.gridOptionsBasicInfo.data.length && !$scope.gridOptionsMemberInfo.data.length && !$scope.gridOptionsLastestUsageInfo.data.length
            	&& !$scope.gridOptionsMarketingMemberInfo.data.length && !$scope.gridOptionsMarketingMemberInfoHistory.data.length 
            	&& !$scope.gridOptionsThirdPartyProvideHistory.data.length && !$scope.gridOptionsCardList.data.length);
        };

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
        });       
        

    }
]).component('clphnNoDupModal', {
    templateUrl: 'partials/common/grid-modal-tpl.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function (apiSvc, uiGridConstants) {
        var self = this;

        self.title = '중복 휴대전화번호 보유자';

        self.gridOptions = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true }
            ]
        };

        self.$onInit = function () {
            self.gridPromise = apiSvc.getClphnNoDup({ mbrId: self.resolve.mbrId });
            self.gridPromise.then(function (data) {
                self.gridOptions.data = data;
            });
        };

    }
}).component('emailAddrDupModal', {
    templateUrl: 'partials/common/grid-modal-tpl.html',
    bindings: {
        resolve: '<',
        close: '&',
        dismiss: '&'
    },
    controller: function (apiSvc, uiGridConstants) {
        var self = this;

        self.title = '중복 이메일주소 보유자';

        self.gridOptions = {
            enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
            columnDefs: [
                { field: 'no', displayName: 'No.', width: 50, cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1}}</div>' },
                { field: 'mbrId', displayName: '회원ID', cellTooltip: true, headerTooltip: true }
            ]
        };

        self.$onInit = function () {
            self.gridPromise = apiSvc.getEmailAddrDup({ mbrId: self.resolve.mbrId });
            self.gridPromise.then(function (data) {
                self.gridOptions.data = data;
            });
        };

    }
});