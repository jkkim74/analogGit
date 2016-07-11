function qnaHelpDeskCtrl($scope, $http, helpDeskSvc, $filter, ngTableParams, $modal) {

    var PER_PAGE_LIST_COUNT = 10;
    var PAGINATION_RANGE = 10;

    $scope.hdQnaTableInfo = {
        totalCount: 0,
        maxPage: 0,
        curPage: 0,
        startIndex: 0,
        endIndex: 0,
        range: []
    };

    $scope.hdSearchCategoryAll = true;
    $scope.hdSearchCategory = [];
    $scope.hdSearchTypes = [
        {type: 'all', name: 'ALL(제목+내용)'},
        {type: 'title', name: '제목'},
        {type: 'context', name: '내용'}
    ];
    $scope.searchForm = {};

    $scope.init = function () {
        $scope.searchForm.hdSearchCategory = null;
        $scope.searchForm.hdSearchType = $scope.hdSearchTypes[0];
        $scope.searchForm.hdSearchString = '';

        loadSearchBox();
        loadData();
    };

    $scope.searchQna = function () {
        selectCategoryStr();
        loadData();
    };

    function selectCategoryStr() {
        $scope.searchForm.hdSearchCategory = [];

        for (var i in $scope.hdSearchCategory) {
            if ($scope.hdSearchCategory[i].selected) {
                $scope.searchForm.hdSearchCategory.push($scope.hdSearchCategory[i].codeId);
            }
        }
    }

    $scope.toggleSelect = function (index) {
        var condition = $scope.hdSearchCategory[index].selected;
        if (condition) {
            $scope.hdSearchCategory[index].selected = false;
            $scope.hdSearchCategoryAll = false;
        } else {
            $scope.hdSearchCategory[index].selected = true;
            checkAllSelect();
        }
    };

    function checkAllSelect() {
        for (var i in $scope.hdSearchCategory) {
            if (!$scope.hdSearchCategory[i].selected) {
                return;
            }
        }
        $scope.hdSearchCategoryAll = true;
    }

    $scope.toggleSelectAll = function () {
        if ($scope.hdSearchCategoryAll) {
            $scope.selectCategoryAll();
        } else {
            $scope.clearCategoryAll();
        }
    };
    $scope.selectCategoryAll = function () {
        for (var i in $scope.hdSearchCategory) {
            $scope.hdSearchCategory[i].selected = true;
        }
    };
    $scope.clearCategoryAll = function () {
        for (var i in $scope.hdSearchCategory) {
            $scope.hdSearchCategory[i].selected = false;
        }
    };

    function loadSearchBox() {
        var qnaSearchBoxUrl = "/commonGroupCode/getCodes?groupCodeId=HD_QNA_GROUP01";
        $http.get(qnaSearchBoxUrl).success(function (data, status, headers, config) {
            $scope.hdSearchCategory = data;
            for (var i in $scope.hdSearchCategory) {
                $scope.hdSearchCategory[i].selected = true;
            }
        });
    }

    function loadData() {
        var qnaCountUrl = "/helpdesk/qna/listCount";
        var queryWhere = {
            hdSearchCategory: $scope.searchForm.hdSearchCategory,
            hdSearchType: $scope.searchForm.hdSearchType.type,
            hdSearchString: $scope.searchForm.hdSearchString
        };

        if (queryWhere.hdSearchCategory != null) {
            if (queryWhere.hdSearchCategory.length < 1) {
                alert("하나 이상의 분류를 선택하세요.");
                return;
            }
        }

        $http.post(qnaCountUrl, queryWhere).success(function (data, status, headers, config) {
            var listCount = data[0].totalRecourdCount;
            $scope.hdQnaTableInfo.totalCount = listCount;
            $scope.hdQnaTableInfo.maxPage = Math.ceil(listCount / PER_PAGE_LIST_COUNT);
            $scope.hdQnaTableInfo.curPage = 1;
            $scope.hdQnaTableInfo.startIndex = listCount - PER_PAGE_LIST_COUNT + 1;
            $scope.hdQnaTableInfo.endIndex = listCount;
            $scope.hdQnaTableInfo.range = setRange();

            loadQnaList();
        });
    };

    function loadQnaList() {
        var qnaUrl = "/helpdesk/qna/list";
        var queryWhere = {
            hdSearchCategory: $scope.searchForm.hdSearchCategory,
            hdSearchType: $scope.searchForm.hdSearchType.type,
            hdSearchString: $scope.searchForm.hdSearchString,
            startIndex: $scope.hdQnaTableInfo.startIndex,
            endIndex: $scope.hdQnaTableInfo.endIndex
        };

        $http.post(qnaUrl, queryWhere).success(function (data, status, headers, config) {
            $scope.qnaList = data;
            $scope.qnaTableParams = new ngTableParams({
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
        var selectPage = this.n;
        if (selectPage === undefined) {
            selectPage = arguments[0];
        }
        var variation = 0;

        if (selectPage > $scope.hdQnaTableInfo.curPage) {
            variation = (selectPage - $scope.hdQnaTableInfo.curPage) * (-1) * PER_PAGE_LIST_COUNT;
        } else {
            variation = ($scope.hdQnaTableInfo.curPage - selectPage) * PER_PAGE_LIST_COUNT;
        }
        $scope.hdQnaTableInfo.startIndex += variation;
        $scope.hdQnaTableInfo.endIndex += variation;
        $scope.hdQnaTableInfo.curPage = selectPage;
        $scope.hdQnaTableInfo.range = setRange();

        loadQnaList();
    };

    $scope.prevPage = function () {
        if ($scope.hdQnaTableInfo.curPage > 1) {
            $scope.setPage($scope.hdQnaTableInfo.curPage - 1);
        }
    };

    $scope.nextPage = function () {
        if ($scope.hdQnaTableInfo.curPage < $scope.hdQnaTableInfo.maxPage) {
            $scope.setPage($scope.hdQnaTableInfo.curPage + 1);
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
        var curPage = $scope.hdQnaTableInfo.curPage;
        var maxPage = $scope.hdQnaTableInfo.maxPage;

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


    $scope.addNewQna = function () {
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.qna.write.tpl.html',
            controller: 'addNewQnaCtrl',
            resolve: {
                categoryList: function () {
                    return $scope.hdSearchCategory;
                }
            },
            keyboard: false
        });
        modalInstance.result.then(
            //close
            function (result) {
                //console.log(result);
                loadData();
            },
            //dismiss
            function (reason) {
                //console.log(reason);
                loadData();
            }
        );
    };

    $scope.readSelectQna = function (index, qnaId) {

        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.qna.read.tpl.html',
            controller: 'readSelectQnaCtrl',
            resolve: {
                qnaList: function () {
                    return $scope.qnaList;
                },
                categoryList: function () {
                    return $scope.hdSearchCategory;
                },
                selectId: function () {
                    return qnaId;
                },
                curIndex: function () {
                    return index;
                },
                condition: function () {
                    return {
                        hdSearchCategory: $scope.searchForm.hdSearchCategory,
                        hdSearchType: $scope.searchForm.hdSearchType.type,
                        hdSearchString: $scope.searchForm.hdSearchString
                    };
                }
            },
            keyboard: false
        });
        modalInstance.result.then(
            //close
            function (result) {
                if (result.work == 'update') {
                    updateSelectQna(result);
                    loadQnaList();
                } else if (result.work == 'delete') {
                    loadData();
                } else if (result.work == 'replyCompletion') {
                    loadQnaList();
                }
            },
            //dismiss
            function (reason) {
//                loadData();
                loadQnaList();
            }
        );
    };

    function updateSelectQna(result) {
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.qna.write.tpl.html',
            controller: 'updateSelectQnaCtrl',
            resolve: {
                selectItem: function () {
                    return result.selectItem;
                },
                categoryList: function () {
                    return $scope.hdSearchCategory;
                }
            },
            keyboard: false
        });
        modalInstance.result.then(
            //close
            function (result) {
                loadQnaList();
            },
            //dismiss
            function (reason) {
                loadQnaList();
            }
        );
    }
}