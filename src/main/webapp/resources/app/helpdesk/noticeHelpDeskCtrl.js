function noticeHelpDeskCtrl($rootScope, $scope, $http, $modal, menuSvc) {
    // 수정/삭제 버튼 활성화 위해 권한 체크.
    $scope.checkAuthority = function () {
        return (menuSvc.getNavigationMenu().helpDeskAdmin == 'Y');
    };

    // 페이지 렌더링된후 최초에 실행되는 함수. 공지팝업이 필요한부분 처리, 공지 목록 조회 처리.
    $scope.init = function () {
        if($rootScope.showMainPageNotice){
            setMainPageNotice();
            $rootScope.showMainPageNotice = false;
        }

        //infinite scroll default setting
        $scope.page = 1;
        $scope.pageSize = 10;
        loadSearchBox();
        // search default setting
        $scope.searchForm = {};
        $scope.searchForm.hdSearchCategory = null;
        $scope.searchForm.hdSearchType = 'all';
        $scope.searchForm.hdSearchString = '';
        loadData();
        // infiniteScroll 동작 여부
        $scope.infiniteScrolling = false;
        $scope.infiniteScrollContainerQuerySelector = '.container-wrap';
    };

    // 공지사항 상세 모달창 처리.
    function setMainPageNotice() {
        var url = '/helpdesk/mainPage/notice';
        $http.get(url).success(function (data) {
            if (data.length > 0) {
                var mainPageNotice = [];
                mainPageNotice.push(data[0]);
                readMainPageNotice(mainPageNotice);
            }
        });
    }

    // 공지 상세 모달창 셋팅.
    function readMainPageNotice(mainPageNotice) {
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.notice.read.tpl.html',
            controller: 'readSelectNoticeCtrl',
            resolve: {
                noticeList: function () {
                    return mainPageNotice;
                },
                categoryList: function () {
                    return $scope.hdSearchCategory;
                },
                selectId: function () {
                    return mainPageNotice[0].id;
                },
                curIndex: function () {
                    return 0;
                },
                checkAuthority: function () {
                    return false;
                }
            },
            keyboard: false
        });
        modalInstance.result.then(
            //close
            function () {
            },
            //dismiss
            function () {
            }
        );
    }

    /**
     * 검색 조건 정보 로드.
     */
    function loadSearchBox() {
        var url = "/commonGroupCode/getCodes?groupCodeId=HD_NOTICE_TYPE";
        $http.get(url).success(function (data) {
            $scope.hdSearchCategory = data;
        });

        url = "/commonGroupCode/getCodes?groupCodeId=HD_SEARCH_TYPE";
        $http.get(url).success(function (data) {
            $scope.hdSearchTypes = data;
        });
    }


    /**
     * 공지 데이터 로드
     */
    function loadData(callback) {
        var resultData;
        var noticeUrl = '/helpdesk/notice/list';
        var queryWhere = {
            hdSearchCategorySingle: $scope.searchForm.hdSearchCategory,
            hdSearchType: $scope.searchForm.hdSearchType,
            hdSearchString: $scope.searchForm.hdSearchString,
            page: $scope.page,
            pageSize: $scope.pageSize
        };

        $http.post(noticeUrl, queryWhere).success(function (data) {
            $scope.noticeList = $scope.noticeList || [];
            $scope.hasNext = data.length > $scope.pageSize;
            if($scope.hasNext) {
                resultData = data.splice(0, $scope.pageSize);
            } else {
                resultData = data;
            }
            angular.forEach(resultData, function(notice) {
                $scope.noticeList.push(notice);
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

    // 다음 페이지 데이터 로드.
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

    /**
     * html 태그제거
     */
    $scope.newLineRemove = function (str) {
        return str.replace(/<.*?>/g, '').replace(/&nbsp;/g, ' ');
    };

    /**
     * 검색
     */
    $scope.searchNotice = function () {
        $scope.page = 1;
        $scope.noticeList = [];
        loadData();
    };

    /**
     * 공지사항등록
     */
    $scope.addNewNotice = function () {
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.notice.write.tpl.html',
            controller: 'addNewNoticeCtrl',
            resolve: {
                categoryList: function () {
                    return $scope.hdSearchCategory;
                }
            },
            keyboard: false
        });
        modalInstance.result.then(
            function () {
                $scope.searchNotice();
            },
            function () {
                $scope.searchNotice();
            }
        );
    };

    /**
     * 선택한 공지사항 modal
     */
    $scope.readSelectNotice = function (index, noticeId) {
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.notice.read.tpl.html',
            controller: 'readSelectNoticeCtrl',
            resolve: {
                noticeList: function () {
                    return $scope.noticeList;
                },
                categoryList: function () {
                    return $scope.hdSearchCategory;
                },
                selectId: function () {
                    return noticeId;
                },
                curIndex: function () {
                    return index;
                },
                checkAuthority:function(){
                    return $scope.checkAuthority();
                }

            },
            keyboard: false
        });
        modalInstance.result.then(
            //close
            function (result) {
                if (result.work == 'update') {
                    updateSelectNotice(result);
                }
                if(result.work == 'delete'){
                    //none
                }
                $scope.searchNotice();
            },
            //dismiss
            function () {
                $scope.searchNotice();
            }
        );
    };

    /**
     * 공지사항 수정
     */
    function updateSelectNotice(result) {
        var modalInstance = $modal.open({
            templateUrl: 'page/templates/helpdesk.notice.write.tpl.html',
            controller: 'updateSelectNoticeCtrl',
            resolve: {
                noticeList: function () {
                    return $scope.noticeList;
                },
                categoryList: function () {
                    return $scope.hdSearchCategory;
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
            function () {
                $scope.searchNotice();
            },
            //dismiss
            function () {
                $scope.searchNotice();
            }
        );

    }

    $scope.getCategoryName = function(categoryCode) {
        var categoryName = '공지';
        switch(categoryCode) {
            case 'emergency':
                categoryName='긴급';
                break;
            case 'important':
                categoryName='주요';
                break;
            case 'notify':
                categoryName='알림';
                break;
        }
        return categoryName;

    };
}
