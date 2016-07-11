function workRequestHelpDeskCtrl($scope, $http, helpDeskSvc, $filter, ngTableParams) {
    var PER_PAGE_LIST_COUNT = 10;
    var PAGINATION_RANGE = 10;

    $scope.hdWorkRequestTitle = '';
    $scope.hdWorkRequestRegistrator = '';
    $scope.hdWorkRequestManager = '';
    $scope.hdWorkRequestState = '';
    $scope.hdWorkRequestRegistrationDate = '';
    $scope.hdWorkRequestCompleteDate = '';

    $scope.hdWorkRequestTableInfo = {
        totalCount: 0,
        maxPage: 0,
        curPage: 0,
        startIndex: 0,
        endIndex: 0,
        range: []
    };

    $scope.init = function () {
        //console.log('workrequestHelpDeskCtrl');
        $scope.initData();
    };

    $scope.initData = function () {
        var workRequestCountUrl = "/helpdesk/workRequest/listCount";
        $http.get(workRequestCountUrl).success(function (data, status, headers, config) {
            var listCount = data[0].totalRecourdCount;
            $scope.hdWorkRequestTableInfo.totalCount = listCount;
            $scope.hdWorkRequestTableInfo.maxPage = Math.ceil(listCount / PER_PAGE_LIST_COUNT);
            $scope.hdWorkRequestTableInfo.curPage = 1;
            $scope.hdWorkRequestTableInfo.startIndex = listCount - PER_PAGE_LIST_COUNT + 1;
            $scope.hdWorkRequestTableInfo.endIndex = listCount;
            $scope.hdWorkRequestTableInfo.range = setRange();

            //console.log($scope.hdWorkRequestTableInfo);

            $scope.loadData();
        });
    };

    $scope.loadData = function () {

        var workRequestUrl = "/helpdesk/workRequest/list?hdWorkRequestTitle=" + $scope.hdWorkRequestTitle
                            + "&hdWorkRequestRegistrator=" + $scope.hdWorkRequestRegistrator
                            + "&hdWorkRequestManager=" + $scope.hdWorkRequestManager
                            + "&hdWorkRequestState=" + $scope.hdWorkRequestState
                            + "&hdWorkRequestRegistrationDate=" + $scope.hdWorkRequestRegistrationDate
                            + "&hdWorkRequestCompleteDate=" + $scope.hdWorkRequestCompleteDate
                            + "&startIndex=" + $scope.hdWorkRequestTableInfo.startIndex
                            + "&endIndex=" + $scope.hdWorkRequestTableInfo.endIndex;


        $http.get(workRequestUrl).success(function (data, status, headers, config) {
            $scope.workRequestList = data;
            //console.log("loadData workRequestList : ");
            //console.log($scope.workRequestList);

            $scope.workRequestTableParams = new ngTableParams({
                page: 1,            // show first page
                count: 10           // count per page
            }, {
                counts: [], // hide page counts control
                total: 1,  // value less than count hide pagination
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });

        });
    };

    $scope.setPage = function () {
        //console.log("arguments : ");
        //console.log(arguments);

        var selectPage = this.n;
        if (selectPage === undefined) {
            selectPage = arguments[0];
        }
        var variation = 0;

        //console.log("selectPage : " + selectPage);

        if (selectPage > $scope.hdWorkRequestTableInfo.curPage) {
            variation = (selectPage - $scope.hdWorkRequestTableInfo.curPage) * (-1) * PER_PAGE_LIST_COUNT;
        } else {
            variation = ($scope.hdWorkRequestTableInfo.curPage - selectPage) * PER_PAGE_LIST_COUNT;
        }
        $scope.hdWorkRequestTableInfo.startIndex += variation;
        $scope.hdWorkRequestTableInfo.endIndex += variation;
        $scope.hdWorkRequestTableInfo.curPage = selectPage;
        $scope.hdWorkRequestTableInfo.range = setRange();

        //console.log($scope.hdWorkRequestTableInfo);
        $scope.loadData();
    };

    $scope.prevPage = function () {
        if ($scope.hdWorkRequestTableInfo.curPage > 1) {
            $scope.setPage($scope.hdWorkRequestTableInfo.curPage - 1);
        }
    };

    $scope.nextPage = function () {
        if ($scope.hdWorkRequestTableInfo.curPage < $scope.hdWorkRequestTableInfo.maxPage) {
            $scope.setPage($scope.hdWorkRequestTableInfo.curPage + 1);
        }
    };

    function makeRange(start, end) {
        var ret = [];
        if (!end) {
            end = start;
            start = 1;
        }
        for (var i = start; i <= end; i++) {
            ret.push(i);
        }
        return ret;
    }

    function setRange() {
        var rangeScope = PAGINATION_RANGE;
        var rangeStd = Math.round((rangeScope + 1) / 2);
        var tmpRange = [];
        var curPage = $scope.hdWorkRequestTableInfo.curPage;
        var maxPage = $scope.hdWorkRequestTableInfo.maxPage;

        if (maxPage < rangeScope) {
            tmpRange = makeRange(maxPage);
        } else {
            if (curPage < rangeStd) {
                tmpRange = makeRange(rangeScope);
            } else {
                if (curPage + (rangeScope - rangeStd) < maxPage) {
                    if (rangeScope % 2 == 0) {
                        tmpRange = makeRange(curPage - (rangeScope - rangeStd + 1), curPage + (rangeScope - rangeStd));
                    } else {
                        tmpRange = makeRange(curPage - (rangeScope - rangeStd), curPage + (rangeScope - rangeStd));
                    }
                } else {
                    tmpRange = makeRange(maxPage - rangeScope + 1, maxPage);
                }
            }
        }
        return tmpRange;
    }


}