function reportMenuManagementCtrl($scope, $log, adminSvc, menuSvc, menuManagementSvc, MENU_SEARCH_OPTION_CALENDAR_TYPE, MENU_SEARCH_OPTION_ADD_TYPE) {
    var selectedServiceIndex = 0, selectedServiceByCategoryIndex = 0, selectedServiceByMenuIndex = 0, selectedCategoryIndex = 0;
    var reportMenuModal = null;

    $scope.reportTabs = [{
        title: 'reportService',
        name: '서비스 등록',
        url: 'reportService.tpl.html'
    }, {
        title: 'reportCategory',
        name: '카테고리 등록',
        url: 'reportCategory.tpl.html'
    }, {
        title: 'reportMenu',
        name: '메뉴 등록',
        url: 'reportMenu.tpl.html'
    }];

    $scope.init = function () {
        $scope.reportServices = menuSvc.getReportService();
        $scope.selectedService = $scope.reportServices[selectedServiceIndex];
        $scope.selectedMenu = {};
        $scope.editableMenu = {};
        $scope.calendarType = MENU_SEARCH_OPTION_CALENDAR_TYPE;
        $scope.addType = MENU_SEARCH_OPTION_ADD_TYPE;
        $scope.saveLoading = false;
        $scope.currentTab = 'reportService.tpl.html';
    };

    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab.url;
    };

    $scope.isActiveTab = function(tabUrl) {
        return tabUrl == $scope.currentTab;
    };

    $scope.treeOptions = {
        accept: function (sourceNode, destNodes) {
            if(destNodes.$modelValue[0]) {
                return (sourceNode.$modelValue.parentId == destNodes.$modelValue[0].parentId);
            }
            return false;
        },
        dragStart: function() {
            $scope.selectedMenu = {};
            $scope.selectedMenuJson = {};
            $scope.editableMenu = {};
        },
        dropped: function (event) {
            $scope.selectedMenu = event.source.nodeScope.$modelValue;
            if(event.source.index != event.dest.index) {
                // 선택된 아이템과 같은 레벨의 아이템의 orderIdx를 일괄로 업데이트한다.
                menuManagementSvc.updateOrderIdx(event.dest.nodesScope.$modelValue, function () {
                    menuSvc.loadReportService();
                });
            }
        }
    };

    $scope.changeService = function() {
        selectedServiceIndex = $scope.reportServices.indexOf($scope.selectedService);
    };

    $scope.changeCategoryService = function() {
        selectedServiceByCategoryIndex = $scope.reportServices.indexOf($scope.reportCategory.selectedService);
    };

    $scope.changeMenuService = function() {
        selectedServiceByMenuIndex = $scope.reportServices.indexOf($scope.reportMenu.selectedService);
        $scope.reportMenu.selectedCategory = $scope.reportMenu.selectedService.categories[selectedCategoryIndex];
    };

    $scope.changeMenuCategory = function() {
        selectedCategoryIndex = $scope.reportMenu.selectedService.categories.indexOf($scope.reportMenu.selectedCategory);
    };

    $scope.edit = function(item) {
        $scope.selectedMenu = item;
        $.extend($scope.editableMenu, $scope.selectedMenu); // copy object
        $scope.selectedMenuJson = angular.toJson($scope.selectedMenu, true);
    };

    $scope.save = function () {
        if (confirm("저장하시겠습니까?")) {
            $scope.saveLoading = true;
            menuManagementSvc.updateMenu($scope.editableMenu, function () {
                loadReport();
                $scope.saveLoading = false;
            }, function(){
                $log.error('failed update menu');
                $scope.saveLoading = false;
            });
        }
    };

    function loadReport() {
        $scope.reportServices = menuSvc.loadReportService();
        $scope.selectedService = $scope.reportServices[selectedServiceIndex];
    }

    function reportMenuClear() {
        if(reportMenuModal) {
            reportMenuModal.modal('hide');
        }
    }

    $scope.activeReportMenuModal = function() {
        loadReport();
        $scope.reportService = {};
        $scope.reportCategory = {};
        $scope.reportMenu = {};
        $scope.reportCategory.selectedService = $scope.reportServices[selectedServiceByCategoryIndex];
        $scope.reportMenu.selectedService = $scope.reportServices[selectedServiceByMenuIndex];
        $scope.reportMenu.selectedCategory = $scope.reportMenu.selectedService.categories[selectedCategoryIndex];
        reportMenuModal = angular.element('#reportMenuModal');
        reportMenuModal.modal('show');
    };

    // 리포트 서바스, 카테고리, 메뉴 등록 함수
    $scope.addReport = function () {
        if ($scope.currentTab === 'reportService.tpl.html')
            addService();
        else if ($scope.currentTab === 'reportCategory.tpl.html')
            addCategory();
        else
            addMenu();
        reportMenuClear();
    };

    function addService() {
        adminSvc.postAdminApi('/menu/addReportService', $scope.reportService).then(function(result) {
            if (result.code === 200) {
                loadReport();
                alert("리포트 서비스 등록이 정상적으로 이루어졌습니다.");
            } else {
                alert("리포트 서비스 등록이 실패했습니다.");
            }
        });
    }

    function addCategory() {
        var services = $scope.reportServices[selectedServiceByCategoryIndex];
        $scope.reportCategory.parentId = services.id;
        adminSvc.postAdminApi('/menu/addReportCategory', $scope.reportCategory).then(function(result) {
            if (result.code === 200) {
                loadReport();
                alert("리포트 카테고리 등록이 정상적으로 이루어졌습니다.");
            } else {
                alert("리포트 카테고리 등록이 실패했습니다.");
            }
        });
    }

    function addMenu () {
        var category = $scope.reportMenu.selectedService.categories[selectedCategoryIndex];
        $scope.reportMenu.parentId = category.id;
        adminSvc.postAdminApi('/menu/addReportMenu', $scope.reportMenu).then(function(result) {
            if (result.code === 200) {
                loadReport();
                alert("리포트 메뉴 등록이 정상적으로 이루어졌습니다.");
            } else {
                alert("리포트 메뉴 등록이 실패했습니다.");
            }
        });
    }
}
