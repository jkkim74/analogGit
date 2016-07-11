function adminMenuManagementCtrl($scope, $log, adminSvc, menuSvc, menuManagementSvc, MENU_SEARCH_OPTION_CALENDAR_TYPE, MENU_SEARCH_OPTION_ADD_TYPE) {
    var selectedServiceIndex = 0, selectedCategoryIndex = 0;
    var adminMenuModal = null;

    $scope.reportTabs = [{
        title: 'adminCategory',
        name: '카테고리 등록',
        url: 'adminCategory.tpl.html'
    }, {
        title: 'adminMenu',
        name: '메뉴 등록',
        url: 'adminMenu.tpl.html'
    }];

    $scope.init = function () {
        $scope.adminService = [menuSvc.getAdminService()];
        $scope.selectedService = $scope.adminService[selectedServiceIndex];
        $scope.selectedMenu = {};
        $scope.editableMenu = {};
        $scope.calendarType = MENU_SEARCH_OPTION_CALENDAR_TYPE;
        $scope.addType = MENU_SEARCH_OPTION_ADD_TYPE;
        $scope.saveLoading = false;
        $scope.currentTab = 'adminCategory.tpl.html';
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
                    menuSvc.loadAdminService();
                });
            }
        }
    };

    $scope.changeService = function() {
        selectedServiceIndex = $scope.adminService.indexOf($scope.selectedService);
    };

    $scope.changeMenuCategory = function() {
        selectedCategoryIndex = $scope.adminMenu.selectedService.categories.indexOf($scope.adminMenu.selectedCategory);
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
                loadAdmin();
                $scope.saveLoading = false;
            }, function(){
                $log.error('failed update menu');
                $scope.saveLoading = false;
            });
        }
    };


    function loadAdmin() {
        $scope.adminService = [menuSvc.loadAdminService()];
        $scope.selectedService = $scope.adminService[selectedServiceIndex];
    }

    function adminMenuClear() {
        if(adminMenuModal) {
            adminMenuModal.modal('hide');
        }
    }

    $scope.activeAdminMenuModal = function() {
        loadAdmin();
        $scope.adminCategory = {};
        $scope.adminMenu = {};
        $scope.adminMenu.selectedService = $scope.adminService[0];
        $scope.adminMenu.selectedCategory = $scope.adminMenu.selectedService.categories[selectedCategoryIndex];
        adminMenuModal = angular.element('#adminMenuModal');
        adminMenuModal.modal('show');
    };

    // Admin 카테고리, 메뉴 등록 함수
    $scope.addAdmin = function () {
        if ($scope.currentTab === 'adminCategory.tpl.html')
            addAdminCategory();
        else
            addAdminMenu();
        adminMenuClear();
    };

    function addAdminCategory() {
        adminSvc.postAdminApi('/menu/addAdminCategory', $scope.adminCategory).then(function(result) {
            if (result.code === 200) {
                loadAdmin();
                alert("Admin 카테고리 등록이 정상적으로 이루어졌습니다.");
            } else {
                alert("Admin 카테고리 등록이 실패했습니다.");
            }
        });
    }

    function addAdminMenu() {
        var category = $scope.adminMenu.selectedService.categories[selectedCategoryIndex];
        $scope.adminMenu.parentId = category.id;
        adminSvc.postAdminApi('/menu/addAdminMenu', $scope.adminMenu).then(function(result) {
            if (result.code === 200) {
                loadAdmin();
                alert("Admin 메뉴 등록이 정상적으로 이루어졌습니다.");
            } else {
                alert("Admin 메뉴 등록이 실패했습니다.");
            }
        });
    }
}
