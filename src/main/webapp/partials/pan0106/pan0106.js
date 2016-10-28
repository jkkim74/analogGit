'use strict';

angular.module('app').controller('PAN0106Ctrl', ['$scope', '$q', '$http', '$timeout', 'uiGridConstants', 'apiSvc', 'authSvc',
    function ($scope, $q, $http, $timeout, uiGridConstants, apiSvc, authSvc) {

        $scope.selectOptions = [
            { label: '회원ID', value: 'mbrId' }
        ];

        $scope.gridOptionsMembers = {
            enableRowSelection: true,
            enableRowHeaderSelection: false,
            multiSelect: false,
            useExternalPagination: true,
            columnDefs: [
                {
                    field: 'no', displayName: 'No.', width: 100,
                    cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row) + 1 + (grid.api.pagination.getPage() - 1) * grid.options.paginationPageSize}}</div>'
                },
                { field: 'mbrId', displayName: '회원ID', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'unitedId', displayName: 'OCB닷컴 United ID', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'gndr', displayName: '성별', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'age', displayName: '연령', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'homeHjdLgrp', displayName: '자택 행정동 대그룹명', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'homeHjdMgrp', displayName: '자택 행정동 중그룹명', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'jobpHjdLgrp', displayName: '직장 행정동 대그룹명', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'jobpHjdMgrp', displayName: '직장 행정동 중그룹명', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'mrrgYn', displayName: '결혼 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'ocbMktngAgrmtYn', displayName: '마케팅 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'emailRcvAgrmtYn', displayName: '이메일 수신 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'advtSmsRcvAgrmtYn', displayName: '광고성 SMS 수신 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'ifrmtSmsRcvAgrmtYn', displayName: '정보성 SMS 수신 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'pushRcvAgrmtYn', displayName: '앱 푸시 수신 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'bnftMlfPushAgrmtYn', displayName: '혜택/모바일전단 푸시 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'pntUseRsvngPushAgrmtYn', displayName: '포인트사용적립 푸시 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'tusePushAgrmtYn', displayName: '친구와 함께쓰기 푸시 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'coinNotiPushAgrmtYn', displayName: '코인알림 푸시 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'locUtlzAgrmtYn', displayName: '위치활용 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'mlfShpAgrmtYn', displayName: '모바일전단매장 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'mlfTrdareaAgrmtYn', displayName: '모바일전단상권 동의 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'ocbcomJoinYn', displayName: 'OCB닷컴 가입 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'ocbAppJoinYn', displayName: 'OCB앱 가입 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'ocbPlusJoinYn', displayName: 'OCB플러스 가입 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'seg629Cd', displayName: '629세그먼트코드', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'trfcSegCd', displayName: '트래픽세그먼트코드', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'ocbcomMth07FnlLgnDt', displayName: 'OCB닷컴 최종 로그인 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'ocbAppMth07FnlUseDt', displayName: 'OCB앱 최종 사용 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'appAtdcMth07FnlUseDt', displayName: '앱출석 최종 사용 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'rletMth07FnlUseDt', displayName: '룰렛 최종 사용 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'gameMth07FnlUseDt', displayName: '게임 최종 사용 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'ocbPlusMth07FnlYachvDt', displayName: 'OCB플러스 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'lckrMth07FnlYachvDt', displayName: '라커0 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'mzmMth07FnlYachvDt', displayName: '미리줌 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'azmMth07FnlYachvDt', displayName: '더줌 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'prdLflMth07FnlYachvDt', displayName: '상품전단 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'mblCardPossYn', displayName: '모바일카드 보유 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'skpCardPossYn', displayName: 'SKP카드 보유 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'crdtCardPossYn', displayName: '신용카드 보유 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'chkcrdPossYn', displayName: '체크카드 보유 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'cmncnCardPossYn', displayName: '통신카드 보유 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'encleanBnsCardPossYn', displayName: '엔크린보너스카드 보유 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'freiWlfrCardPossYn', displayName: '화물복지카드 보유 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'rflExcsCardPossYn', displayName: '주유전용카드 보유 여부', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'rflMth07FnlYachvDt', displayName: '주유 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'cmncnMth07FnlYachvDt', displayName: '통신 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'fncMth07FnlYachvDt', displayName: '금융 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'etcMth07FnlYachvDt', displayName: '기타 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'onlnMth07FnlYachvDt', displayName: '온라인 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'onlnMth07CpnFnlYachvDt', displayName: '온라인쿠폰 최종 유실적 일자', width: 100, cellTooltip: true, headerTooltip: true },
                { field: 'avlPnt', displayName: '가용포인트', width: 100, cellTooltip: true, headerTooltip: true, cellFilter: 'number' }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    var offset = (newPage - 1) * pageSize;
                    $scope.loadMembers(offset, pageSize);
                });
            }
        };

        $scope.upload = function (file) {
            $scope.uploadPromise = apiSvc.upload({ file: file, columnName: $scope.selectedOption.value });
            $scope.uploadPromise.then(function () {
                $scope.checkUploadProgress();
            });
        };

        $scope.checkUploadProgress = function () {
            // 업로드 끝 flag 까지 재귀반복
            $timeout(function () {
                apiSvc.getUploaded({ countOnly: true }).then(function (data) {
                    $scope.uploadedItems = data.totalItems;

                    if (data.message === 'FINISH') {
                        $scope.uploadStatusIsRunning = false;
                    } else {
                        $scope.uploadStatusIsRunning = true;
                        $scope.checkUploadProgress();
                    }
                });
            }, 1000);
        };

        $scope.loadMembers = function (offset, limit) {
            var params = {
                offset: offset || 0,
                limit: limit || $scope.gridOptionsMembers.paginationPageSize
            }

            $scope.membersPromise = apiSvc.getMembers(params);
            $scope.membersPromise.then(function (data) {
                $scope.gridOptionsMembers.data = data.value;
                $scope.gridOptionsMembers.totalItems = data.totalItems;
            });
        };

        $scope.sendPts = function () {
            $scope.sendPtsPromise = apiSvc.sendPts({ ptsMasking: !!$scope.ptsMasking });
            $scope.sendPtsPromise.finally(function () {

            });
        };

        authSvc.getUserInfo().then(function (userInfo) {
            $scope.ptsUsername = userInfo.ptsUsername;
        });

    }]);