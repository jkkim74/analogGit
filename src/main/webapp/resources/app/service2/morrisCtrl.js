/**
 * Created by cookatrice on 2014. 6. 10..
 */

function morrisCtrl($scope, $http, API_BASE_URL) {
    $scope.xkey = 'range';
    $scope.ykeys = ['total_A', 'total_B'];
    $scope.labels = ['total tasks', 'out of budget tasks'];
    $scope.myData = [
        { range: '2006', total_A: 34, total_B: 5},
        { range: '2007', total_A: 20, total_B: 5 },
        { range: '2008', total_A: 35, total_B: 8 },
        { range: '2009', total_A: 20, total_B: 1 },
        { range: '2010', total_A: 20, total_B: 6 }
    ];
    $scope.myData2 = [
        {label: "Banana", value: 22},
        {label: "Orange", value: 40},
        {label: "Apple", value: 30}
    ];
}
