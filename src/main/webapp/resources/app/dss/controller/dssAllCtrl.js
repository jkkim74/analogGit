/**
 * Created by seoseungho on 2014. 10. 6..
 */
function dssAllCtrl($scope, $http, $filter, $log) {
    'use strict';

    $scope.init = function() {
        // 검색 기본 설정
        $scope.page = 1;
        $scope.pageSize = 12;

        // bm 유형 목록을 가져온다.
        $scope.bms = AjaxHelper.syncGetJSON('/dss/bmList', null);

        // 검색 폼 초기화
        $scope.initSearchForm();

        // 달력 컴포넌트 제어를 위한 객체 생성
        $scope.datePicker = {};
        $scope.datePicker.format = 'yyyy.MM.dd';
        $scope.datePicker.isOpen = {};

        // 정렬 데이터 변수
        $scope.orderBy = 'createDate';

        // 초기 보고서 조회
        $scope.search();

        // infiniteScroll 동작 여부
        $scope.infiniteScrolling = false;

        $scope.infiniteScrollContainerQuerySelector = '.container-wrap';

        $scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
            $('.data-tit').ellipsis({
                lines: 2,
                ellipClass: 'ellip'
            });
            $('.data-description').ellipsis({
                lines: 3,
                ellipClass: 'ellip'
            });
        });
    };

    // bm 조회에 사용할 id 목록을 수집한다.
    // bm0 을 prefix로 한 프로퍼티에서 id를 추출한다.
    $scope.collectBmIds = function(bmTypes) {
        var bmIds = [];
        angular.forEach(bmTypes, function(value, key) {
            if (key.indexOf('bm0') === 0 && value !== null) {
                bmIds.push(value.id);
            }
        });
        return bmIds;
    };

    // bmTypes로 조회할지 여부를 결정한다.
    $scope.useBmTypesForSearching = function (isAll) {
        return !!isAll;
    };


    // 보고서를 조회한다.
    $scope.search = function(callback) {
        var resultData,
            parts = [],
            requestURL;
        // 기본 검색 옵션을 설정한다.
        $scope.form = {};
        angular.extend($scope.form, {
            page: $scope.page,
            pageSize: $scope.pageSize
        });

        // bm 정보 처리 부분
        if (!$scope.useBmTypesForSearching($scope.selectBm.all)) {
            parts = $scope.collectBmIds($scope.selectBm);
        }

        $scope.form.bms = parts;

        // 등록일 검색 관련 처리 부분
        if (!$scope.selectDay.all) {
            $scope.form.searchStart = $scope.selectDay.searchStart.toISOString();
            $scope.form.searchEnd = $scope.selectDay.searchEnd.toISOString();
        }

        // 작성자 처리 부분
        if ($scope.createName) {
            $scope.form.createName = $scope.createName;
        }

        // 내용 검색 부분
        if ($scope.content) {
            $scope.form.contentType = $scope.contentType;
            $scope.form.content = $scope.content;
        }

        requestURL = UrlHelper.buildUrl('/dss', $scope.form);
        $http.get(requestURL).success(function(response) {
            $scope.dssList = $scope.dssList || [];
            // 조회 결과가 pageSize보다 크면 더 가져올 page가 있다고 판단한다.
            $scope.hasNext = response.length > $scope.pageSize;
            if ($scope.hasNext) {
                resultData = response.splice(0, $scope.pageSize);
            } else {
                resultData = response;
            }

            // 날짜 데이터를 포맷에 맞춰 변경한다.
            angular.forEach(resultData, function(dss, key) {
                dss.displayCreateDate = DateHelper.stringToYmdStr(dss.createDate, '.');
                $scope.dssList.push(dss);
            });

            // orderBy 기본이 createDate이기 때문에, createDate 정렬은 해줄 필요가 없다.
            if ($scope.orderBy === 'viewCount') {
                $scope.orderByViewCount('fromSearch');
            } else {
                $scope.orderByCreateDate('fromSearch');
            }

            // 더보기 처리를 위한 콜백 함수에 변수를 전달한다.
            var param = {
                hasNext : $scope.hasNext
            };

            // callback을 호출한다.
            if (angular.isFunction(callback)) {
                callback(param);
            }

            // searchButton을 클릭한 경우, 더보기 요청을 강제로 호출해준다.
            if ($scope.page === 1) {
                $scope.loadMore();
            }
        }).error(function(response) {
            $log.debug('error:', response);
        });
    };

    $scope.searchButton = function() {
        // 새로운 검색의 시작이기 떄문에 page를 1로 초기화 한다.
        $scope.page = 1;
        $scope.dssList = [];
        $scope.search();
    };

    // 달력 컴포넌트를 연다.
    $scope.openDatePicker = function($event, which) {
        $event.preventDefault();
        $event.stopPropagation();
        if ($scope.selectDay.all) {
            return false;
        }

        $scope.closeAllDatePickers();
        // 달력 컴포넌트를 연다.
        $scope.datePicker.isOpen[which]= true;
    };

    // 모든 달력 컴포넌트를 닫는다.
    $scope.closeAllDatePickers = function() {
        angular.forEach($scope.datePicker.isOpen, function(value, key) {
            $scope.datePicker.isOpen[key] = false;
        });
    };

    // 최신순 정렬
    $scope.orderByCreateDate = function(param) {
        if (!(param) && $scope.orderBy === 'createDate') {
            return false;
        }
        $scope.orderBy = 'createDate';
        $scope.dssList = $filter('orderBy')($scope.dssList, '-createDate', false);
    };

    // 인기순 정렬
    $scope.orderByViewCount = function(param) {
        if (!(param) && $scope.orderBy === 'viewCount') {
            return false;
        }
        $scope.orderBy = 'viewCount';
        $scope.dssList = $filter('orderBy')($scope.dssList, '-viewCount', false);
    };

    // 검색 폼 초기화
    $scope.initSearchForm = function () {
        // bm 유형 검색
        $scope.selectBm = {};
        $scope.selectBm.all = true;

        // 등록일자 검색
        $scope.selectDay = {};
        $scope.selectDay.all = true;
        $scope.selectDay.searchStart = new Date();
        $scope.selectDay.searchEnd = new Date();

        // 등록자명 검색
        $scope.createName = '';

        // 내용 검색
        $scope.contentType = 'all';
        $scope.content = '';
    };

    // 스크롤이 하단에 접근하면 호출되는 핸들러
    $scope.loadMore = function() {
        if (!$scope.hasNext) {
            return false;
        }
        $scope.infiniteScrolling = true;
        $scope.page = $scope.page + 1;

        $scope.search(function(param) {
            // 다음 page가 있으면 infiniteScroll을 다시 동작시킨다.
            if (param.hasNext) {
                $scope.infiniteScrolling = false;
            }
        });
    };
}
