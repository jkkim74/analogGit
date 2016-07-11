function bpmMenuManagementCtrl($scope, $log, adminSvc, menuSvc, menuManagementSvc, MENU_SEARCH_OPTION_CALENDAR_TYPE, MENU_SEARCH_OPTION_ADD_TYPE) {
    var selectedServiceIndex = 0, selectedCategoryIndex = 0;
    var dashboardMenuModal = null;

    $scope.reportTabs = [{
        title: 'dashboardCategory',
        name: '카테고리 등록',
        url: 'dashboardCategory.tpl.html'
    }, {
        title: 'dashboardMenu',
        name: '메뉴 등록',
        url: 'dashboardMenu.tpl.html'
    }];

    $scope.init = function () {
        $scope.dashboardService = [menuSvc.getDashboardService()];
        $scope.selectedService = $scope.dashboardService[selectedServiceIndex];
        $scope.selectedMenu = {};
        $scope.editableMenu = {};
        $scope.calendarType = MENU_SEARCH_OPTION_CALENDAR_TYPE;
        $scope.addType = MENU_SEARCH_OPTION_ADD_TYPE;
        $scope.saveLoading = false;
        $scope.currentTab = 'dashboardCategory.tpl.html';
    };

    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab.url;
    };

    $scope.isActiveTab = function(tabUrl) {
        return tabUrl == $scope.currentTab;
    };

    $scope.activeDashboardMenuModal = function() {
        loadDashboard();
        $scope.dashboardCategory = {};
        $scope.dashboardMenu = {};
        $scope.dashboardMenu.selectedService = $scope.dashboardService[0];
        $scope.dashboardMenu.selectedCategory = $scope.dashboardMenu.selectedService.categories[selectedCategoryIndex];
        dashboardMenuModal = angular.element('#dashboardMenuModal');
        dashboardMenuModal.modal('show');
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
                    menuSvc.loadDashboardService();
                });
            }
        }
    };

    $scope.changeService = function() {
        selectedServiceIndex = $scope.dashboardService.indexOf($scope.selectedService);
    };

    $scope.changeMenuCategory = function() {
        selectedCategoryIndex = $scope.dashboardMenu.selectedService.categories.indexOf($scope.dashboardMenu.selectedCategory);
    };

    $scope.edit = function(item) {
        $scope.selectedMenu = item;
        $.extend($scope.editableMenu, $scope.selectedMenu);
        $scope.selectedMenuJson = angular.toJson($scope.selectedMenu, true);
    };

    $scope.save = function () {
        if (confirm("저장하시겠습니까?")) {
            $scope.saveLoading = true;
            menuManagementSvc.updateMenu($scope.editableMenu, function () {
                loadDashboard();
                $scope.saveLoading = false;
            }, function(){
                $log.error('failed update menu');
                $scope.saveLoading = false;
            });
        }
    };

    function loadDashboard() {
        $scope.dashboardService = [menuSvc.loadDashboardService()];
        $scope.selectedService = $scope.dashboardService[selectedServiceIndex];
    }

    function dashboardMenuClear() {
        if(dashboardMenuModal) {
            dashboardMenuModal.modal('hide');
        }
    }

    // Dashboard 카테고리, 메뉴 등록 함수
    $scope.addDashboard = function () {
        if ($scope.currentTab === 'dashboardCategory.tpl.html')
            addDashboardCategory();
        else
            addDashboardMenu();
        dashboardMenuClear();
    };

    function addDashboardCategory() {
        adminSvc.postAdminApi('/menu/addDashboardCategory', $scope.dashboardCategory).then(function(result) {
            if (result.code === 200) {
                loadDashboard();
                alert("Dashboard 카테고리 등록이 정상적으로 이루어졌습니다.");
            } else {
                alert("Dashboard 카테고리 등록이 실패했습니다.");
            }
        });
    }

    function addDashboardMenu() {
        var category = $scope.dashboardMenu.selectedService.categories[selectedCategoryIndex];
        $scope.dashboardMenu.parentId = category.id;
        adminSvc.postAdminApi('/menu/addDashboardMenu', $scope.dashboardMenu).then(function(result) {
            if (result.code === 200) {
                loadDashboard();
                alert("Dashboard 메뉴 등록이 정상적으로 이루어졌습니다.");
            } else {
                alert("Dashboard 메뉴 등록이 실패했습니다.");
            }
        });
    }
}
