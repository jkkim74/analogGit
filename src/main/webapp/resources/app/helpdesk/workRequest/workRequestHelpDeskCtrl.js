function workRequestHelpDeskCtrl($scope, $http, strSvc, helpDeskSvc, ngTableParams) {
    function resetForm() {
        $scope.datePickerMaxDate = new Date();
        $scope.searchText = {
            summary: null,
            applicant: null,
            approval: null
        };
        $scope.searchForm = {
            summary: null,
            //description: null,
            applicant: null,
            approval: null,
            status: 0,
            component: 0,
            createDateTotal: true,
            createStartDate: null,
            createEndDate: null,
            dueDateTotal: true,
            dueStartDate: null,
            dueEndDate: null
        };
    }

    function datePickerBinding() {
        $('.input-append.date').datepicker({
            format: 'yyyy.mm.dd',
            autoclose: true,
            weekStart: 1
        });
    }

    $scope.goToJiraArticle = function(key){
        var BASE_URL = 'http://jira.skplanet.com/browse/';
        //console.log(BASE_URL+key);
        window.open(BASE_URL+key);
    };

    $scope.checkBoxState = function () {
        if ($scope.searchForm.createDateTotal) {
            $scope.searchForm.createStartDate = null;
            $scope.searchForm.createEndDate = null;
        }

        if ($scope.searchForm.dueDateTotal) {
            $scope.searchForm.dueStartDate = null;
            $scope.searchForm.dueEndDate = null;
        }
    };

    $scope.doSearch = function () {
        //common string --> unicode string
        $scope.searchForm.summary = strSvc.getStringToUnicode($scope.searchText.summary);
        $scope.searchForm.applicant = strSvc.getStringToUnicode($scope.searchText.applicant);
        $scope.searchForm.approval = strSvc.getStringToUnicode($scope.searchText.approval);
        initJiraData();
    };

    $scope.doSearchInit = function () {
        resetForm();
    };

    $scope.init = function () {
        helpDeskSvc.getComponentTypes().then(function (data) {
            $scope.componentTypeList = data;
        });
        $scope.statusTypeList = helpDeskSvc.getStatusTypes();

        $scope.searchMessage = '검색 결과가 없습니다.';

        resetForm();
        datePickerBinding();

        initJiraData();

    };

    function initJiraData() {
        var workRequestCountUrl = "/jiraApi/getJiraListFromApi";

        $scope.searchMessage = '검색 중입니다...';
        $scope.workRequestList = [];

        $http.get(workRequestCountUrl, {params: $scope.searchForm}).success(function (data, status, headers, config) {
            var listCount = data.length;
            $scope.workRequestList = data;

            $scope.workRequestTableParams = new ngTableParams({
                page: 1,            // show first page
                count: 10           // count per page
            }, {
                counts: [], // hide page counts control
                total: 0,  // value less than count hide pagination
                getData: function ($defer, params) {
                    $defer.resolve(data.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });

            $scope.searchMessage = '검색 결과가 없습니다.';
        });
    }
}
