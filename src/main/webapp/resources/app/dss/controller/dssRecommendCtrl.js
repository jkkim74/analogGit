/**
 * Created by seoseungho on 2014. 9. 24..
 */
function dssRecommendCtrl($scope, $http, $log) {
    var SWIPE_REPORT_SIZE = 3;
    $scope.init = function() {
        // 검색 기본 설정
        $scope.defaultSearchOption = {
            page: 1,
            pageSize: 15,
            orderBy: 'viewCount'
        };

        $scope.search();

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

    // 보고서를 조회한다.
    $scope.search = function() {
        var requestURL = UrlHelper.buildUrl('/dss', $scope.defaultSearchOption);
        $http.get(requestURL).success(function(response) {
            var dssLength;

            if (response.length > $scope.defaultSearchOption.pageSize) {
                $scope.dssList = response.splice(0, $scope.defaultSearchOption.pageSize);
            } else {
                $scope.dssList = response;
            }

            dssLength = $scope.dssList.length;
            // 날짜 데이터를 포맷에 맞춰 변경한다.
            angular.forEach($scope.dssList, function(value, key) {
                value.createDate = DateHelper.stringToYmdStr(value.createDate, '.');
            });

            // 스와이핑 되는 영역에 리포트는 3개. 4번째 리포트부터는 사각형 메뉴 형태로 노출한다.
            if ($scope.dssList.length) {
                $scope.swipeReports = $scope.dssList.splice(0, (dssLength < SWIPE_REPORT_SIZE ? dssLength : SWIPE_REPORT_SIZE));
                $scope.normalReports = $scope.dssList;
            }

            //console.log($scope.swipeReports, $scope.normalReports);
        }).error(function(response) {
            $log.debug('error:', response);
        });
    };
}
