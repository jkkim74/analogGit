function faqHelpDeskCtrl($scope, $http, $modal, menuSvc) {
    $scope.checkAdmin = (menuSvc.getNavigationMenu().helpDeskAdmin == 'Y');

    $scope.checkAuthority = function () {
        return $scope.checkAdmin;
    };

    $scope.init = function () {
        //infinite scroll default setting
        $scope.page = 1;
        $scope.pageSize = 10;

        loadSearchBox();

        $scope.searchForm = {};
        $scope.searchForm.hdSearchType = 'all';
        $scope.searchForm.hdSearchString = '';

        loadData();

        // infiniteScroll 동작 여부
        $scope.infiniteScrolling = false;
        $scope.infiniteScrollContainerQuerySelector = '.container-wrap';

    };

    /**
     * load default data
     */
    function loadSearchBox() {
        var url = "/commonGroupCode/getCodes?groupCodeId=HD_SEARCH_TYPE";
        $http.get(url).success(function (data, status, headers, config) {
            $scope.hdSearchTypes = data;
        });
    }

    function loadData(callback) {
        var faqUrl = '/helpdesk/faq/list';

        var queryWhere = {
            hdSearchType: $scope.searchForm.hdSearchType,
            hdSearchString: $scope.searchForm.hdSearchString,
            page: $scope.page,
            pageSize: $scope.pageSize
        };
        $http.post(faqUrl, queryWhere).success(function (data, status, headers, config) {
            $scope.faqList = $scope.faqList || [];

            $scope.hasNext = data.length > $scope.pageSize;
            //console.log('hasNext : ' + $scope.hasNext);
            if($scope.hasNext){
                resultData = data.splice(0,$scope.pageSize);
            }else{
                resultData = data;
            }

            angular.forEach(resultData, function(notice, key) {
                //console.log(notice);
                $scope.faqList.push(notice);
            });

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

        });
    }

    // infinite scroll handler function
    $scope.loadMore = function() {
        if (!$scope.hasNext) {
            return false;
        }
        $scope.infiniteScrolling = true;
        $scope.page = $scope.page + 1;

        loadData(function(param) {
            // 다음 page가 있으면 infiniteScroll을 다시 동작시킨다.
            if (param.hasNext) {
                $scope.infiniteScrolling = false;
            }
        });
    };


    $scope.newLineRemove = function (str) {
        return str.replace(/<.*?>/g, '').replace(/&nbsp;/g, ' ');
    };

    $scope.searchFaq = function () {
        $scope.page = 1;
        $scope.faqList = [];
        loadData();
    };

    $scope.addNewFaq = function () {
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.faq.write.tpl.html',
            controller: 'addNewFaqCtrl',
            resolve: {
            },
            keyboard: false
        });
        modalInstance.result.then(
            //close
            function (result) {
                $scope.searchFaq();
            },
            //dismiss
            function (reason) {
                $scope.searchFaq();
            }
        );

    };

    $scope.readSelectFaq = function (index, faqId) {

        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.faq.read.tpl.html',
            controller: 'readSelectFaqCtrl',
            resolve: {
                faqList: function () {
                    return $scope.faqList;
                },
                selectId: function () {
                    return faqId;
                },
                curIndex: function () {
                    return index;
                }
            },
            keyboard: false
        });
        modalInstance.result.then(
            //close
            function (result) {
                if (result.work == 'update') {
                    updateSelectFaq(result);
                }
                if(result.work == 'delete'){
                    //none
                }
                $scope.searchFaq();
            },
            //dismiss
            function (reason) {
                $scope.searchFaq();
            }
        );
    };


    function updateSelectFaq(result) {
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.faq.write.tpl.html',
            controller: 'updateSelectFaqCtrl',
            resolve: {
                faqList: function () {
                    return $scope.faqList;
                },
                selectId: function () {
                    return result.selectId;
                },
                curIndex: function () {
                    return result.curIndex;
                }
            },
            keyboard: false
        });
        modalInstance.result.then(
            //close
            function (result) {
                $scope.searchFaq();
            },
            //dismiss
            function (reason) {
                $scope.searchFaq();
            }
        );

    }
}
