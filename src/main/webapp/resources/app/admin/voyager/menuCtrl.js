function menuCtrl($scope, $log, adminSvc, userSvc, menuSvc, menuManagementSvc, apiSvc) {
    var selectedVisibleYnIndex = 0, selectedServiceByCategoryIndex = 0, selectedServiceByMenuIndex = 0,
        selectedCategoryIndex = 0, selectedProjectIndex = 0, selectedAddTypeIndex = 0,
        selectedMenuVisibleYnIndex = 0,selectedServiceVisibleYnIndex = 0,
        selectedCategoryVisibleYnIndex = 0, selectedCalendarTypeIndex = 0;
    var createMenuModal = null, saveMenuSearchModal = null, saveServiceCategoryDataModal = null, orgEditableMenuCode = null;
    $scope.dynaTreeTable = "menuCategoryTree";
    $scope.roleClick = [];
    $scope.menuRoleInfo = {};// 권한 및 사용자 권한 매핑 정보
    $scope.selectedGridData = []; //선택된 사용자 수
    $scope.roleInfo = []; // 권한 및 사용자 매핑 카운트 정보
    $scope.searchLoginId = '';

    $scope.visibleYns = [
        {name: '활성', value: 'Y' },
        {name: '비활성', value: 'N'},
        {name: '개발', value: 'P'},
        {name: '삭제', value: 'D'}
    ];
    $scope.menuTabs = [{
        title: 'service',
        name: '서비스 등록',
        url: 'service.tpl.html'
    }, {
        title: 'category',
        name: '카테고리 등록',
        url: 'category.tpl.html'
    }, {
        title: 'menu',
        name: '메뉴 등록',
        url: 'menu.tpl.html'
    }];
    $scope.searchMenuNm = '';
    $scope.menuIds = [];
    $scope.parentId = '';

    // 페이지 렌더링된 후 최초로 실행되는 함수. 변수 초기화 및 메뉴 데이터로드 및 메뉴 트리 그리기 함수 호출.
    $scope.init = function () {
        $scope.menu = {};
        $scope.editableMenu = {};
        $scope.menuTree = {};
        $scope.currentTab = 'menu.tpl.html';
        $scope.currentTemplate = 'editableCategory.tpl.html';
        $scope.calendarType = ["", "period", "single"];
        $scope.addType = ["", "select", "text", "mstr"];
        $scope.searchedVisibleYn = $scope.visibleYns[selectedVisibleYnIndex];
        getMenuTree();
        // 널 검색도 허용하도록 하기 위해.
        $('#' + $scope.dynaTreeTable).dynatree('disable');
        $('#' + $scope.dynaTreeTable).dynatree('enable');
        getMenuGrid();
    };

    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab.url;
    };

    $scope.isActiveTab = function(tabUrl) {
        return tabUrl == $scope.currentTab;
    };

    $scope.changeVisibleYn = function () {
        selectedVisibleYnIndex = $scope.visibleYns.indexOf($scope.searchedVisibleYn);
    };

    // 조회 조건 변경해서 검색했을 경우 처리.
    $scope.searchMenu = function () {
        $scope.menuId = '';
        $scope.parentId = '';
        if (!$scope.searchMenuNm) {
            getMenuGrid();
        } else {
            $scope.init();
        }
    };

    // dynaTree 오픈소스를 활용해 메뉴 트리 구성함
    function getMenuTree() {
        $scope.menuCategoryTreeConfig = {
            id: $scope.dynaTreeTable,
            clickFolderMode: 1,
            debugLevel: 0,
            initAjax: {
                async: false,
                url: "/menu/menuTrees",
                type: "POST",
                postProcess: function (data) {
                    return data;
                }
            },
            onPostInit: function () {
                this.reactivate();
            },
            onActivate: function (node) {
                var key = node.data.key;
                if (node.data.menuId == "") {
                    node.data.menuId = "MENU_SET";
                    createMenuTree(node, key); //서비스/카테고리 선택시에만 사용자 가져오기
                }
                //$scope.parentId = key;
                $scope.menuTree = node.data;
                getMenuGrid(key);
                node.expand(true);
            },
            onRender: function() {
                $('#menuCategoryTree').css("height", "500px");
                $('#menuCategoryTree .dynatree-container').css("height", "500px");
            }
        };
    }

    /**
     * 목적 : 메뉴별 하위 메뉴 트리생성
     */
    function createMenuTree(node, menuId) {
        var searchCondition = 'parentId=' + menuId;
        menuSvc.getTreeMenus(searchCondition).then(function (result) {
            angular.forEach(result, function (obj) {
                node.addChild({
                    title: obj.name + "(" + obj.code + ")",
                    key: obj.id,
                    code: obj.code,
                    name: obj.name,
                    menuId: obj.menuId,
                    menuNm: obj.menuNm,
                    lvl: obj.lvl
                });
            });
        });
    }

    /**
     * 목적 : MSTR 메뉴별 하위 리포트 트리생성
     */
    function createMstrMenuTree(node, objectId, projectId) {
        var searchCondition = 'objectId=' + objectId + "&projectId=" + projectId;
        menuSvc.getMstrSubMenuTrees(searchCondition).then(function (result) {
            angular.forEach(result, function (obj) {
                node.addChild({
                    title: obj.objectName,
                    key: obj.objectId,
                    parentObjectId: obj.parentObjectId,
                    objectType: obj.objectType,
                    subType: obj.subType,
                    menuNm: obj.menuNm,
                    unitType: obj.unitType,
                    isFolder: obj.isFolder
                });
            });
        });
    }

    /**
     * 그리드에 표시할 메뉴 정보 조회
     *
     * @params
     */
    function getMenuGrid(menuId) {
        roleMenuClear();
        var searchCondition;
        $scope.selectedGridData = []; //선택된 메뉴 수
        getRoles(true);
        if (menuId) {
            searchCondition = 'parentId=' + menuId;
        } else {
            if ($scope.searchMenuNm) {
                searchCondition = 'name=' + $scope.searchMenuNm
                    + '&visibleYn=' + $scope.visibleYns[selectedVisibleYnIndex].value;
            }
        }
        if (angular.isUndefined(searchCondition)) {
            // 디폴트 그리드 생성.
            gridMenuInfo({page:0,total:0,records:0,rows:null});
        } else {
            menuSvc.getTreeMenus(searchCondition).then(function (result) {
                gridMenuInfo(result);
            });
        }
    }

    // 메뉴 정보 표시
    function gridMenuInfo(datas) {
        $scope.menuTableId = "menuForJqgrid";
        $scope.userConfig = {
            data : datas,
            datatype: "local",
            height: "476",
            shrinkToFit: true,
            rowNum: 10000,
            colNames: ['ID', '서비스명', '커테고리명', '메뉴명', '상태', '삭세여부', '옵션여부'],
            colModel: [
                {name: 'id', index: 'id', editable: false, hidden: true, align: "center", sortable: true},
                {name: 'serviceName', index: 'serviceName', editable: false, width: 50, align: "left", sortable: false},
                {name: 'categoryName', index: 'categoryName', editable: false, width: 50, align: "left", sortable: false},
                {name: 'name', index: 'name', editable: true, align: "center", sortable: false},
                {name: 'state', index: 'state', editable: true, edittype: 'select', editoptions: {value:{Y:'활성', N:'비활성', P:'개발', D:'삭제'}}, width: 50, align: "center", sortable: false},
                {name: 'deleteYn', index: 'deleteYn', editable: true, width: 40, align: "center", sortable: false},
                {name: 'menuSearchOptionYn', index: 'menuSearchOptionYn', editable: true, width: 40, align: "center", sortable: false}
            ],
            gridview: true,
            multiselect: true,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            jsonReader: {
                root: "rows"
            },
            onSelectRow: function () {
                getRoleMenuCount();
            },
            editurl: "clientArray"
        };
        var table = angular.element('<table id="' + $scope.menuTableId + '" />');
        angular.element("#menuContainer").html(table);
        angular.element("#" + $scope.menuTableId).jqGrid("GridUnload");
        angular.element("#" + $scope.menuTableId).jqGrid($scope.userConfig);
    }

    $scope.$on('$destroy', function() {
        $('#menuContainer').empty();
    });

    // 권한 목록을 조회
    function getRoles(init) {
        if (init) {
            getRoleGrid(null);
        } else {
            adminSvc.getAdminApi('/role/gets').then(function(result){
                $scope.roles = result.rows;
                getRoleGrid($scope.roles);
            });
        }
    }

    // 권한 추가 및 해제 설정 처리
    function setRoleGrid(id, selectedRow) {
        $scope.roleClick.push({roleId : selectedRow.id, name : selectedRow.name, click : 1});
        $scope.roleInfo[selectedRow.name].click = 1;
        if ($scope.roleInfo[selectedRow.name].background == '#fcefa1') {
            $scope.roleInfo[selectedRow.name].background = '#ffffff';
            $('#roleForJqgrid #' + id).css('background-color', '#ffffff');
        } else if ($scope.roleInfo[selectedRow.name].background == '#dfdfdf') {
            $scope.roleInfo[selectedRow.name].background = '#fcefa1';
            $('#roleForJqgrid #' + id).css('background-color', '#fcefa1');
        } else {
            $scope.roleInfo[selectedRow.name].background = '#fcefa1';
            $('#roleForJqgrid #' + id).css('background-color', '#fcefa1');
        }
    }

    // 메뉴별 권한 정보 초기화.
    function initRole() {
        if ($scope.menuRoleInfo.menuCounts) {
            if ($scope.menuRoleInfo.menuCounts.length > 0) {
                _.map($scope.menuRoleInfo.menuCounts, function (doc) {
                    $scope.roleInfo[doc.name] = {id : doc.roleId, count: doc.count};
                });
            }
        }
        if ($scope.menuRoleInfo.menuRoles) {
            if ($scope.menuRoleInfo.menuRoles.length > 0) {
                _.map(_.groupBy($scope.menuRoleInfo.menuRoles, function(doc){
                    return doc.name;
                }),function(grouped){
                    var ids = _.pluck(grouped, 'menuIdMenuName').join(',');
                    $scope.roleInfo[grouped[0].name] =
                        _.extend($scope.roleInfo[grouped[0].name], {ids:  ids});
                });
            }
        }
    }

    // 권한 정보 화면 디스플레이
    function getRoleGrid(rows) {
        $scope.tableId = "roleForJqgrid";
        var table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#roleContainer").html(table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid({
            datatype: "local",
            height: "476",
            shrinkToFit: true,
            loadui: "block",
            rowNum: 10000,
            colNames: ['ID', '권한명'],
            colModel: [
                {name: 'id', index: 'id', hidden: true, editable: false, align: "center", sortable: true},
                {name: 'name', index: 'name', editable: false, width: 100, align: "left", sortable: false}
            ],
            sortname: "id",
            sortorder: 'asc',
            multiselect: false,
            sortable: false,
            loadonce: true,
            autowidth: true,
            jsonReader: {
                root: "rows"
            },
            beforeSelectRow: function() {
                return $scope.selectedGridData.length > 0 ? true : false;
            },
            onSelectRow: function (id) {
                if ($scope.selectedGridData.length < 1) {
                    swal('메뉴를 선택해 주세요.');
                    return;
                }
                var ret = $(this).jqGrid("getRowData", id);
                setRoleGrid(id, ret);
            }
        });
        if (!rows) {
            // 디폴트 그리드 생성.
            angular.element('#' + $scope.tableId).jqGrid();
        } else {
            var rowSize = rows.length;
            initRole();
            for (var i = 0; i < rowSize; i++) {
                angular.element('#' + $scope.tableId).jqGrid('addRowData', i + 1, rows[i]);
                // 선택된 사용자 권한 있는 항목 표시.
                setBackground(i, rows[i]);
            }
        }
    }

    // 권한 허용 메뉴 백그라운드 색으로 표시.
    function setBackground(i, rows) {
        var selectedMenuCount = $scope.selectedGridData.length;
        if (!angular.isUndefined($scope.roleInfo[rows.name])) {
            if ($scope.roleInfo[rows.name].count == selectedMenuCount) {
                $scope.roleInfo[rows.name] = _.extend($scope.roleInfo[rows.name], {background:  '#fcefa1', click: 0});
                $('#roleForJqgrid #' + (i + 1)).css('background', '#fcefa1');//#fcefa1
            } else if ($scope.roleInfo[rows.name].count > 0 && $scope.roleInfo[rows.name].count < selectedMenuCount) {
                $('#roleForJqgrid #' + (i + 1)).css('background', '#dfdfdf');
                $scope.roleInfo[rows.name] = _.extend($scope.roleInfo[rows.name], {background:  '#dfdfdf', click: 0});
            } else {
                $('#roleForJqgrid #' + (i + 1)).css('background', '#ffffff');
                $scope.roleInfo[rows.name] = _.extend($scope.roleInfo[rows.name], {background:  '#ffffff', click: 0});
            }
        } else {
            $scope.roleInfo[rows.name] =  {id: rows.id, count: 0, background:  '#ffffff', click: 0};
            $('#roleForJqgrid #' + (i + 1)).css('background', '#ffffff');
        }
    }

    // 메뉴 정보에서 하나의 메뉴를 선택한 정보 조회.
    function getSelectMenuData(isEditable) {
        var $table = $('#' + $scope.menuTableId);
        var gr = $table.jqGrid("getGridParam","selarrrow"); // 선택된 Rows
        if (gr.length > 0) {
            var grLength = gr.length;
            $scope.selectedGridData = [];
            _(grLength).times(function (i) {
                //선택된 값들을 Local 에 저장한다.
                if (isEditable)
                    $table.jqGrid("saveRow", gr[i], false, "clientArray");
                var ret = $table.jqGrid("getRowData", gr[i]);
                $scope.selectedGridData.push(ret);
                //$scope.menuIds.push(ret.id);
            });
            $scope.selectedGridData = _.uniq($scope.selectedGridData, function (item) {
                return item.id;
            });
            $scope.menuIds = [];
            var selectedGridDataLength = $scope.selectedGridData.length;
            _(selectedGridDataLength).times(function (i) {
                $scope.menuIds.push($scope.selectedGridData[i].id);
            });
        } else {
            $scope.selectedGridData = [];
            $scope.menuIds = [];
        }
    }

    // 선택한 메뉴별 권한 허용 개수 조회.
    function getRoleMenuCount() {
        roleMenuClear();
        getSelectMenuData(false);
        var selectedGridLength = $scope.selectedGridData.length;
        if (selectedGridLength > 0) { //선택된 row가 있을경우
            $scope.requestGridData = {
                username: userSvc.getUser().username,
                menuIds: $scope.menuIds
            };
            adminSvc.postAdminApi('/role/menuRoleCount', $scope.requestGridData).then(function (result) {
                $scope.menuRoleInfo = result;
                getRoles(false);
            });
        } else {
            roleMenuClear();
            getRoles(true);
        }
    }

    // 메뉴 수정 Modal 화면 표시
    $scope.activeSaveMenuSearchModal = function() {
        getSelectMenuData(false);
        var menuIdsLength = $scope.menuIds.length;
        if (menuIdsLength < 1) {
            swal('메뉴를 선택해 주세요.');
            return;
        }
        if (menuIdsLength > 1) {
            swal('하나의 메뉴만을 선택해 주세요.');
            return;
        }
        // 초기화(신규 등록 메뉴와 수정 메뉴 변수 초기화.
        $scope.$safeApply(function () {
            $scope.menu = {};
            $scope.editableMenu = {};
            selectedProjectIndex = 0;
            $scope.selectedNodes = [];
        });
        $scope.editableMenu = menuSvc.getMenu($scope.selectedGridData[0].serviceName,
            $scope.selectedGridData[0].categoryName, $scope.menuIds[0]);
        orgEditableMenuCode = $scope.editableMenu.code;
        if ($scope.editableMenu.menuSearchOptionYn === 'N') {
            $scope.editableMenu.menuSearchOption = {
                addType: '',
                calendarType: ''
            };
        }
        $scope.editableMenu.visibleYnName =  _.find($scope.visibleYns, function(obj) {
            return ($scope.editableMenu.visibleYn == obj.value);
        });
        selectedMenuVisibleYnIndex = $scope.visibleYns.indexOf($scope.editableMenu.visibleYnName);
        $scope.selectedPeriod = {
            codes: {"day": false, "week": false, "month": false}
        };
        if ($scope.editableMenu.menuSearchOptionYn === 'Y' && $scope.editableMenu.menuSearchOption.addType === 'mstr') {
            var projectId = $scope.editableMenu.menuSearchOption.label.split(',');
            $scope.editableMenu.menuSearchOption.selectedProject =  _.find($scope.mstrProjects, function(obj) {
                return (projectId[0] == obj.id);
            });
            selectedProjectIndex = $scope.mstrProjects.indexOf($scope.editableMenu.menuSearchOption.selectedProject);
            $scope.editableMenu.menuSearchOption.projectId = projectId[0];
            $scope.selectedMstrProject = $scope.mstrProjects[selectedProjectIndex];
            getMstrMenuTree('mstrEditMenuContainer', 'mstrEditMenuTree', projectId[0]);
        } else if ($scope.editableMenu.menuSearchOptionYn === 'Y' && $scope.editableMenu.menuSearchOption.dateTypes) {
            if ($scope.editableMenu.menuSearchOption.dateTypes.indexOf("day") > -1)
                $scope.selectedPeriod.codes["day"] = true;
            if ($scope.editableMenu.menuSearchOption.dateTypes.indexOf("week") > -1)
                $scope.selectedPeriod.codes["week"] = true;
            if ($scope.editableMenu.menuSearchOption.dateTypes.indexOf("month") > -1)
                $scope.selectedPeriod.codes["month"] = true;
        }
        saveMenuSearchModal = angular.element('#saveMenuSearchModal');
        saveMenuSearchModal.modal('show');
    };

    // 메뉴 수정 처리.
    $scope.saveMenuSearchData = function () {
        $scope.editableMenu.visibleYn = $scope.visibleYns[selectedMenuVisibleYnIndex].value;
        if ($scope.editableMenu.visibleYn === 'D') {
            if (!confirm("해당 메뉴를 삭제하시겠습니까?")) {
                return;
            }
            $scope.editableMenu.deleteYn = 'Y';
        }
        if ($scope.editableMenu.menuSearchOptionYn === 'Y' && $scope.editableMenu.menuSearchOption.addType
            && $scope.editableMenu.menuSearchOption.addType == 'mstr') {
            if ($scope.selectedNodes.length > 0) {
                var message = checkMstrMenu('editableMenu');
                if (message) {
                    alert(message);
                    return;
                }
            }
            // 동일 MSTR 메뉴는 등록 안되게.
            if ($scope.selectedNodes.length > 0 && $scope.editableMenu.code != orgEditableMenuCode) {
                var mstrMenus = apiSvc.getMstrMenus($scope.editableMenu.code);
                if (mstrMenus && mstrMenus.length) {
                    alert("동일한 메뉴가 이미 등록되어 있습니다.");
                    return;
                }
            }
        }
        if ($scope.editableMenu.menuSearchOptionYn === 'Y') {
            var dateTypes = [];
            _.map($scope.selectedPeriod.codes, function (value, key) {
                if (value)
                    dateTypes.push(key);
            });
            if (dateTypes.length) {
                $scope.editableMenu.menuSearchOption.dateTypes = dateTypes.join(',');
            } else {
                $scope.editableMenu.menuSearchOption.dateTypes = '';
            }
        }
        $scope.editableMenu.auditId = userSvc.getUser().username;
        menuManagementSvc.updateMenu($scope.editableMenu, function () {
            swal("메뉴 수정이 정상적으로 이루어졌습니다.");
            loadService();
            $scope.init();
            roleMenuClear();
        }, function(){
            $log.error('failed update menu');
        });
    };

    // 신규 및 메뉴 수정 화면에서 메뉴 탭의 서비스와 카테고리 정보 조회.
    function loadServiceCategoryData() {
        $scope.editableService = {};
        $scope.editableCategory= {};
        if ($scope.menuTree.commonCodeId === 2 || $scope.menuTree.commonCodeId === 5 ||
            $scope.menuTree.commonCodeId === 8) {
            $scope.currentTemplate = 'editableService.tpl.html';
            /** @namespace $scope.menuTree.commonCodeId */
            if ($scope.menuTree.commonCodeId === 2) {
                $scope.editableService = menuSvc.getDashboardService();
            } else if ($scope.menuTree.commonCodeId === 5) {
                $scope.editableService = menuSvc.getService($scope.menuTree.id);
            } else {
                $scope.editableService = menuSvc.getAdminService();
            }
            $scope.editableService.visibleYnName = _.find($scope.visibleYns, function(obj) {
                return ($scope.editableService.visibleYn == obj.value);
            });
            selectedMenuVisibleYnIndex = $scope.visibleYns.indexOf($scope.editableService.visibleYnName);
        } else if ($scope.menuTree.commonCodeId === 3 || $scope.menuTree.commonCodeId === 6 ||
            $scope.menuTree.commonCodeId === 9) {//카테고리
            $scope.currentTemplate = 'editableCategory.tpl.html';
            $scope.editableCategory = menuSvc.getCategory($scope.menuTree.parentId, $scope.menuTree.id,
                $scope.menuTree.commonCodeId);
            $scope.editableCategory.visibleYnName = _.find($scope.visibleYns, function(obj) {
                return ($scope.editableCategory.visibleYn == obj.value);
            });
            selectedMenuVisibleYnIndex = $scope.visibleYns.indexOf($scope.editableCategory.visibleYnName);
        } else {
            return false;
        }
        return true;
    }

    // 서비스/카테고리 수정 메뉴 활성화
    $scope.activeSaveServiceCategoryData = function() {
        if (_.size($scope.menuTree) < 1) {
            swal('서비스나 카테고리를 선택해 주세요.');
            return;
        }
        if (!loadServiceCategoryData()) {
            swal('수정할수 없는 서비스나 카테고리입니다.');
            return;
        }
        saveServiceCategoryDataModal = angular.element('#saveServiceCategoryDataModal');
        saveServiceCategoryDataModal.modal('show');
    };

    // 서비스 및 카테고리 내용 수정
    $scope.saveServiceCategoryData = function () {
        var editableServiceCategory;
        if (_.size($scope.editableService) > 0) {
            $scope.editableService.visibleYn = $scope.visibleYns[selectedMenuVisibleYnIndex].value;
            if ($scope.editableService.visibleYn === 'D') {
                // 메뉴나 카테고리가 있으면 삭제 불가.
                if ($scope.editableService.categories && $scope.editableService.categories.length) {
                    alert("하위 카테고리가 있어 해당 서비스를 삭제할 수 없습니다.");
                    return;
                }
                if (!confirm("해당 서비스를 삭제하시겠습니까?")) {
                    return;
                }
                $scope.editableService.deleteYn = 'Y';
            }
            $scope.editableService.auditId = userSvc.getUser().username;
            editableServiceCategory = $scope.editableService;
        } else {
            $scope.editableCategory.visibleYn = $scope.visibleYns[selectedMenuVisibleYnIndex].value;
            if ($scope.editableCategory.visibleYn === 'D') {
                if ($scope.editableCategory.menus && $scope.editableCategory.menus.length) {
                    alert("하위 메뉴가 있어 해당 카테고리를 삭제할 수 없습니다.");
                    return;
                }
                if (!confirm("해당 카테고리를 삭제하시겠습니까?")) {
                    return;
                }
                $scope.editableCategory.deleteYn = 'Y';
            }
            $scope.editableCategory.auditId = userSvc.getUser().username;
            editableServiceCategory = $scope.editableCategory;
        }
        menuManagementSvc.updateMenu(editableServiceCategory, function () {
            loadService();
            $scope.init();
            swal("서비스나 카테고리 수정이 정상적으로 이루어졌습니다.");
        }, function(){
            $log.error('failed update menu');
        });
    };

    // 콘트롤러에서 사용되는 변수 초기화.
    function roleMenuClear() {
        $scope.selectedGridData = [];
        $scope.roleClick = [];
        $scope.menuRoleInfo = {};// 권한 및 사용자 권한 매핑 정보
        $scope.roleInfo = []; // 권한 및 사용자 매핑 카운트 정보
        $scope.menu = {};
        $scope.editableMenu = {};
        if (createMenuModal) {
            createMenuModal.modal('hide');
        }
        if (saveMenuSearchModal) {
            saveMenuSearchModal.modal('hide');
        }
        if (saveServiceCategoryDataModal) {
            saveServiceCategoryDataModal.modal('hide');
        }
    }

    // 메뉴별 권한 등록/해제 처리.
    $scope.saveRoleData = function() {
        if ($scope.selectedGridData.length < 1) {
            swal('선택된 메뉴가 없습니다.');
            return;
        }
        var uniqueClicks = _.uniq($scope.roleClick, function (item, key, index) {
            return item.roleId;
        });

        var addRole = [];
        var removeRole = [];
        var removeComment = [];
        _.each(uniqueClicks, function (data, index) {
            if ($scope.roleInfo[data.name].background == '#fcefa1') { // 권한 추가
                addRole.push(data.roleId);
            } else if ($scope.roleInfo[data.name].background == '#ffffff') {
                removeRole.push(data.roleId);
                if (!angular.isUndefined($scope.roleInfo[data.name].ids)) {
                    removeComment.push($scope.roleInfo[data.name].ids + '메뉴의 ' + data.name);
                }
            }
        });

        if (addRole.length < 1 && removeRole.length < 1) {
            swal('선택된 권한이 없습니다.');
            return;
        }
        var setMenuRole = {
            username: userSvc.getUser().username,
            menuIds : $scope.menuIds
        };
        if (addRole.length > 0) {
            setMenuRole.addRoles = addRole;
        }
        if (removeRole.length > 0) {
            setMenuRole.removeRoles = removeRole;
        }
        if (removeComment.length > 0) {
            var removeConfirm = confirm(removeComment.join(',') + '권한이 삭제됩니다.');
            if (removeConfirm) {
                handleComRoleMenu(setMenuRole);
            } else {
                getRoleMenuCount();
            }
        } else {
            handleComRoleMenu(setMenuRole);
        }
    };

    // 권한별 메뉴 등록/해제 처리.
    function handleComRoleMenu(setMenuRole) {
        adminSvc.postAdminApi('/role/handleComMenuRole', setMenuRole).then(function (result) {
            getRoleMenuCount();
            if (result.code === 200) {
                swal("권한 처리가 정상적으로 이루어졌습니다.");
            } else {
                swal("권한 처리가 실패했습니다.");
            }
        });
    }

    // 현재 Voyager에서 사용되는 서비스 전체 목록 조회.
    function loadService() {
        $scope.services = menuSvc.loadReportService().concat([menuSvc.loadDashboardService()])
            .concat([menuSvc.loadAdminService()]);
    }

    // 신규 메뉴 등록 화면 활성화.
    $scope.activeCreateMenuModal = function() {
        $scope.services = menuSvc.getReportService().concat([menuSvc.getDashboardService()])
            .concat([menuSvc.getAdminService()]);
        $scope.service = {};
        $scope.category = {};
        $scope.menu = {};
        $scope.editableMenu = {};
        $scope.currentTab = 'menu.tpl.html';
        $scope.selectedNodes = [];
        selectedServiceByCategoryIndex = 0, selectedServiceByMenuIndex = 0,
            selectedCategoryIndex = 0, selectedProjectIndex = 0, selectedMenuVisibleYnIndex = 0;
        $scope.category.selectedService = $scope.services[selectedServiceByCategoryIndex];
        $scope.menu.selectedService = $scope.services[selectedServiceByMenuIndex];
        $scope.menu.selectedCategory = $scope.menu.selectedService.categories[selectedCategoryIndex];
        $scope.menu.visibleYn = 'Y';// 디폴트 셋팅.
        $scope.menu.visibleYnName =  _.find($scope.visibleYns, function(obj) {
            return ($scope.menu.visibleYn == obj.value);
        });
        $scope.category.visibleYn = 'Y';// 디폴트 셋팅.
        $scope.category.visibleYnName =  _.find($scope.visibleYns, function(obj) {
            return ($scope.category.visibleYn == obj.value);
        });
        $scope.service.visibleYn = 'Y';// 디폴트 셋팅.
        $scope.service.visibleYnName =  _.find($scope.visibleYns, function(obj) {
            return ($scope.service.visibleYn == obj.value);
        });
        $('#mstrMenuContainer').empty();
        //selectedMenuVisibleYnIndex = $scope.visibleYns.indexOf($scope.menu.visibleYnName);
        createMenuModal = angular.element('#createMenuModal');
        createMenuModal.modal('show');
    };

    $scope.changeMenuSearchOptionYn = function() {
        if ($scope.menu.menuSearchOptionYn === 'Y') {
            $scope.menu.menuSearchOption = {
                addType: '',
                calendarType: ''
            };
        }
    };

    // 카테고리 탭의 서비스 선택 인덱스 정보 처리.
    $scope.changeCategoryService = function() {
        selectedServiceByCategoryIndex = $scope.services.indexOf($scope.category.selectedService);
    };

    // 메뉴 탭의 선택된 서비스 선택 인덱스 정보 처리.
    $scope.changeMenuService = function() {
        selectedServiceByMenuIndex = $scope.services.indexOf($scope.menu.selectedService);
        $scope.menu.selectedService = $scope.services[selectedServiceByMenuIndex];
        if (!$scope.menu.selectedService.categories.length) {
            alert('선택할 카테고리 정보가 없어서 카테고리 등록 메뉴로 이동합니다.');
            $scope.currentTab = 'category.tpl.html';
            return;
        }
        $scope.menu.selectedCategory = $scope.menu.selectedService.categories[selectedCategoryIndex];
    };

    // 메뉴 탭의 선택된 카테고리 인덱스 정보 처리.
    $scope.changeMenuCategory = function() {
        selectedCategoryIndex = $scope.menu.selectedService.categories.indexOf($scope.menu.selectedCategory);
        $scope.menu.selectedCategory = $scope.menu.selectedService.categories[selectedCategoryIndex];
    };

    // 메뉴 신규 화면의 메뉴탭의 추가옵션 선택 인덱스 정보 처리.
    $scope.changeMenuAddType = function() {
        selectedAddTypeIndex = $scope.addType.indexOf($scope.menu.menuSearchOption.addType);
        if (selectedAddTypeIndex === 3 && $scope.menu.selectedService.commonCodeId != 5) {
            selectedAddTypeIndex = 0;
            $scope.menu.menuSearchOption.addType = '';
            alert('Dashboard나 관리자 페이지에서는 MSTR 메뉴등록을 할 수 없습니다.');
            return false;
        }
        if (selectedAddTypeIndex === 2) {
            $('#mstrMenuContainer').empty();
        }
    };

    // 메뉴 수정 화면의 메뉴탭의 추가옵션 선택 인덱스 정보 처리.
    $scope.changeEditMenuAddType = function() {
        selectedAddTypeIndex = $scope.addType.indexOf($scope.editableMenu.menuSearchOption.addType);
        if (selectedAddTypeIndex === 3 && ($scope.selectedGridData[0].serviceName == 'Dashboard' ||
            $scope.selectedGridData[0].serviceName == '관리자 페이지')) {
            selectedAddTypeIndex = 0;
            $scope.editableMenu.menuSearchOption.addType = '';
            alert('Dashboard나 관리자 페이지에서는 MSTR 메뉴등록을 할 수 없습니다.');
            return false;
        }
        if (selectedAddTypeIndex === 3) {
            $('#mstrEditMenuContainer').empty();
        } else if (selectedAddTypeIndex == 0) {
            $scope.editableMenu.menuSearchOption.label = '';
        }
    };

    // 메뉴 수정 화면의 날짜주기 타입.
    $scope.changeEditMenuCalendarType = function() {
        selectedCalendarTypeIndex = $scope.calendarType.indexOf($scope.editableMenu.menuSearchOption.calendarType);
        if (selectedCalendarTypeIndex === 0) {
            $scope.selectedPeriod = {
                codes: {"day": false, "week": false, "month": false}
            };
        }
    };

    // 메뉴 신규 화면 MSTR 프로젝트 변경 인덱스 정보 처리.
    $scope.changeMenuProject = function() {
        selectedProjectIndex = $scope.mstrProjects.indexOf($scope.menu.menuSearchOption.selectedProject);
        $scope.selectedMstrProject = $scope.mstrProjects[selectedProjectIndex];
        //var projectId = $scope.mstrProjects[selectedProjectIndex].id;
        $scope.menu.menuSearchOption.projectId = $scope.selectedMstrProject.id;
        getMstrMenuTree('mstrMenuContainer', 'mstrMenuTree', $scope.selectedMstrProject.id);
    };

    // 메뉴 수정 화면 MSTR 프로젝트 변경 인덱스 정보 처리.
    $scope.changeEditMenuProject = function() {
        selectedProjectIndex = $scope.mstrProjects.indexOf($scope.editableMenu.menuSearchOption.selectedProject);
        $scope.selectedMstrProject = $scope.mstrProjects[selectedProjectIndex];
        //var projectId = $scope.mstrProjects[selectedProjectIndex].id;
        $scope.editableMenu.menuSearchOption.projectId = $scope.selectedMstrProject.id;
        getMstrMenuTree('mstrEditMenuContainer', 'mstrEditMenuTree', $scope.selectedMstrProject.id);
    };

    // 메뉴 수정탭의 상태 선택 인덱스 정보 처리.
    $scope.changeEditMenuVisibleYn = function() {
        selectedMenuVisibleYnIndex = $scope.visibleYns.indexOf($scope.editableMenu.visibleYnName);
    };

    // 서비스 수정화면에서 상태 인덱스 정보 처리.
    $scope.changeEditServiceVisibleYn = function() {
        selectedMenuVisibleYnIndex = $scope.visibleYns.indexOf($scope.editableService.visibleYnName);
    };

    // 카테고리 수정화면에서 상태 인덱스 정보 처리.
    $scope.changeEditCategoryVisibleYn = function() {
        selectedMenuVisibleYnIndex = $scope.visibleYns.indexOf($scope.editableCategory.visibleYnName);
    };

    // 메뉴 신규 화면에서 메뉴 상태 인덱스 정보 처리.
    $scope.changeMenuVisibleYn = function() {
        selectedMenuVisibleYnIndex = $scope.visibleYns.indexOf($scope.menu.visibleYnName);
    };

    // 메뉴 신규 화면에서 서비스 상태 인덱스 정보 처리.
    $scope.changeServiceVisibleYn = function() {
        selectedServiceVisibleYnIndex = $scope.visibleYns.indexOf($scope.service.visibleYnName);
    };

    // 메뉴 신규화면에서 카테고리의 상태 인덱스 정보 처리.
    $scope.changeCategoryVisibleYn = function() {
        selectedCategoryVisibleYnIndex = $scope.visibleYns.indexOf($scope.category.visibleYnName);
    };

    // 서바스, 카테고리, 메뉴 등록 함수
    $scope.add = function () {
        if ($scope.currentTab === 'service.tpl.html')
            addService();
        else if ($scope.currentTab === 'category.tpl.html')
            addCategory();
        else
            addMenu();

    };

    // 서비스 신규 등곡 처리.
    function addService() {
        $scope.service.visibleYn = $scope.visibleYns[selectedServiceVisibleYnIndex].value;
        $scope.service.auditId = userSvc.getUser().username;
        if ($scope.service.visibleYn === 'D') {
            $scope.service.deleteYn = 'Y';
        }
        adminSvc.postAdminApi('/menu/addReportService', $scope.service).then(function(result) {
            if (result.code === 200) {
                loadService();
                swal("서비스 등록이 정상적으로 이루어졌습니다.");
                $scope.init();
                roleMenuClear();
            } else {
                swal("서비스 등록이 실패했습니다.");
            }
        });

    }

    // 카테고리 등록 처리.
    function addCategory() {
        var services = $scope.services[selectedServiceByCategoryIndex];
        $scope.category.parentId = services.id;
        $scope.category.auditId = userSvc.getUser().username;
        var commonCodeId = $scope.category.selectedService.commonCodeId;
        $scope.category.visibleYn = $scope.visibleYns[selectedCategoryVisibleYnIndex].value;
        if ($scope.category.visibleYn === 'D') {
            $scope.category.deleteYn = 'Y';
        }
        var addCategoryUrl;
        if (commonCodeId === 5) {
            addCategoryUrl = '/menu/addReportCategory';
        } else if (commonCodeId === 8) {
            addCategoryUrl = '/menu/addAdminCategory';
        } else if (commonCodeId === 2) {
            addCategoryUrl = '/menu/addDashboardCategory';
        } else {
            alert("정보가 부정확합니다. 다시 입력해 주세요.");
            return;
        }
        adminSvc.postAdminApi(addCategoryUrl, $scope.category).then(function(result) {
            if (result.code === 200) {
                loadService();
                swal("카테고리 등록이 정상적으로 이루어졌습니다.");
                $scope.init();
                roleMenuClear();
            } else {
                swal("카테고리 등록이 실패했습니다.");
            }
        });
    }

    // 일/주/월 중복 체크.
    function unique(arr) {
        var uniqDateTypes = _.uniq(arr.join(",").split(","));
        return uniqDateTypes.join(",").length === arr.join(",").length;
    }

    // MSTR 메뉴의 등록이나 변경처리시 사전에 유효성 검증.
    function checkMstrMenu(mode) {
        var message = '', menuCode = [], mstrMenu = [], labelCode = [],
            dateType, projectId, mstrDateTypes = [];
        if (!$scope.selectedNodes.length) {
            return message = 'MSTR 리포트 메뉴를 선택해 주세요.';
        }
        if ($scope.selectedNodes.length > 3) {
            return message = 'MSTR 리포트 메뉴는 최대 3개까지만 선택할 수 있습니다.';
        }
        (mode == 'menu') ? projectId = $scope.menu.menuSearchOption.projectId :
            projectId = $scope.editableMenu.menuSearchOption.projectId;
        angular.forEach($scope.selectedNodes, function (obj) {
            if (obj.data.isFolder) {
                message = '폴더는 MSTR 메뉴로 등록할 수 없습니다.';
                return true;
            }
            dateType = apiSvc.getMstrDateTypes(projectId, obj.data.key);
            if (!dateType || !dateType.dateTypes) {
                mstrMenu.push({objectId: obj.data.key,
                    title: obj.data.title, objectType: obj.data.objectType});
            } else {
                mstrMenu.push({dateType: dateType.dateTypes, objectId: obj.data.key,
                    title: obj.data.title, objectType: obj.data.objectType});
                mstrDateTypes.push(dateType.dateTypes);
            }
            menuCode.push(obj.data.key);
            labelCode.push(obj.data.key + '_' + obj.data.objectType);
        });
        if (message) {
            return message;
        }
        if ($scope.selectedNodes.length > 1 && ((mode == 'menu' && !$scope.menu.name) ||
            (mode == 'editableMenu' && !$scope.editableMenu.name))) {
            return message = '2개 이상의 메뉴를 등록할 경우 메뉴명을 입력해야합니다.';
        }
        if (!unique(mstrDateTypes)) {
            return message = '일/주/월 중복으로 들어간 리포트가 존재합니다.';
        }
        if (mode == 'menu') {
            if ($scope.selectedNodes.length == 1) {
                $scope.menu.code = menuCode[0];
                $scope.menu.name = mstrMenu[0].title;
                $scope.menu.menuSearchOption.label = projectId + ','
                    + $scope.menu.code + '_' + mstrMenu[0].objectType;
            } else {
                $scope.menu.code = menuCode.join("_");
                $scope.menu.menuSearchOption.label = projectId + ','
                    + labelCode.join(",");
            }
            $scope.menu.menuSearchOption.dateTypes = 'day,week,month';
            $scope.menu.menuSearchOption.calendarType = 'period';
        } else {
            if ($scope.selectedNodes.length == 1) {
                $scope.editableMenu.code = menuCode[0];
                $scope.editableMenu.name = mstrMenu[0].title;
                $scope.editableMenu.menuSearchOption.label = projectId + ','
                    + $scope.editableMenu.code + '_' + mstrMenu[0].objectType;
            } else {
                $scope.editableMenu.code = menuCode.join("_");
                $scope.editableMenu.menuSearchOption.label = projectId + ','
                    + labelCode.join(",");
            }
        }
        return message;
    }

    // 메뉴 신규 등록 처리.
    function addMenu() {
        var addMenuUrl;
        var category = $scope.menu.selectedService.categories[selectedCategoryIndex];
        $scope.menu.parentId = category.id;
        $scope.menu.auditId = userSvc.getUser().username;
        var commonCodeId = $scope.menu.selectedService.commonCodeId;
        $scope.menu.visibleYn = $scope.visibleYns[selectedMenuVisibleYnIndex].value;
        if (commonCodeId === 5) {
            if ($scope.menu.menuSearchOptionYn === 'Y'
                && $scope.menu.menuSearchOption.addType
                && $scope.menu.menuSearchOption.addType == 'mstr') {
                var message = checkMstrMenu('menu');
                if (message) {
                    alert(message);
                    return;
                }
                // 동일 MSTR 메뉴는 등록 안되게.
                var mstrMenus = apiSvc.getMstrMenus($scope.menu.code);
                if (mstrMenus && mstrMenus.length > 0) {
                    alert("동일한 메뉴가 이미 등록되어 있습니다.");
                    return;
                }
            }
            addMenuUrl = '/menu/addReportMenu';
        } else if (commonCodeId === 8) {
            addMenuUrl = '/menu/addAdminMenu';
        } else if (commonCodeId === 2) {
            addMenuUrl = '/menu/addDashboardMenu';
        } else {
            swal("정보가 부정확합니다. 다시 입력해 주세요.");
            return;
        }
        adminSvc.postAdminApi(addMenuUrl, $scope.menu).then(function(result) {
            if (result.code === 200) {
                loadService();
                swal("메뉴 등록이 정상적으로 이루어졌습니다.");
                $scope.init();
                roleMenuClear();
            } else {
                swal("메뉴 등록이 실패했습니다.");
            }
        });
    }

    // 메뉴 신규 및 수정에서 MSTR 메뉴 트리 구성
    function getMstrMenuTree(mstrContainer, dynaTreeTable, projectId) {
        var mstrMenuTreeConfig = {
            id: dynaTreeTable,
            checkbox: true,
            selectMode: 2,
            isFolder: true,
            clickFolderMode: 1,
            debugLevel: 0,
            initAjax: {
                async: false,
                url: "/menu/mstrMenuTrees",
                type: "POST",
                data: {
                    projectId: projectId
                },
                postProcess: function (data) {
                    return data;
                }
            },
            onPostInit: function () {
                this.reactivate();
            },
            onSelect: function(select, node) {
                $scope.$safeApply(function () {
                    $scope.selectedNodes = node.tree.getSelectedNodes();
                });
            },
            onActivate: function (node) {
                var key = node.data.key;
                if (!node.data.menuId && node.data.isFolder && node.data.lvl != '1') {
                    node.data.menuId = "MENU_SET";
                    createMstrMenuTree(node, key, projectId);
                } /*else {
                    setFormMenu(node)
                }*/
                node.expand(true);
            }
        };
        var div = angular.element('<div id="' + dynaTreeTable + '" />');
        angular.element("#" + mstrContainer).html(div);
        angular.element("#" + dynaTreeTable).dynatree(mstrMenuTreeConfig);
    }
}
