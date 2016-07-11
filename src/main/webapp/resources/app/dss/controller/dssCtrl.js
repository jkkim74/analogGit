/**
 * Created by seoseungho on 2014. 9. 12..
 */
function dssCtrl($scope, $state) {
    $scope.$state = $state;

    $scope.init = function() {
        EventBindingHelper.initReportSidebar();
    };

    // 리포트 조회
    $scope.getDssList = function (params) {
        return AjaxHelper.syncGetJSON('/dss', params);
    };
}
