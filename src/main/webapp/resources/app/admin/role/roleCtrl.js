function roleCtrl($scope, adminSvc, userSvc) {
    var selectedRoleTypeIndex = 0;
    var roleUserModal = null;
    var addRoleModal = null;
    $scope.addRole = {};
    $scope.addRoleForm = {};
    $scope.roleTableId = 'roleForGrid';
    $scope.roleObject = null;
    $scope.menuClick = [];
    $scope.roleMenuInfo = {};// 권한 및 메뉴 매핑 정보
    $scope.menuInfo = []; // 권한 및 메뉴 매핑 카운트 정보
    $scope.searchRoleNm = '';
    $scope.selectedRoleData = []; //선택된 권한 수
    $scope.roleTypes = [
        {name: '전체', value: 'all'},
        {name: '활성', value: 'N'},
        {name: '비활성', value: 'P'},
        {name: '삭제', value: 'Y'}
    ];
    $scope.roleIds = [];

    $scope.init = function () {
        $scope.searchRoleType = $scope.roleTypes[selectedRoleTypeIndex];
        callRoleInfoResult();
    };

    $scope.changeRoleType = function () {
        selectedRoleTypeIndex = $scope.roleTypes.indexOf($scope.searchRoleType);
    };

    function callRoleInfoResult() {
        roleMenuClear();
        getMenus(true);
        var postData = {
            roleType: $scope.roleTypes[selectedRoleTypeIndex].value,
            name : $scope.searchRoleNm,
            timeStamp : DateHelper.timestamp()
        };
        adminSvc.postAdminApi('/role/gets', postData).then(function(result){
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridRoleInfo(result.rows);
            } else {
                gridRoleInfo({page:0,total:0,records:0,rows:null});
            }
        });
    }

    function gridRoleInfo(datas) {
        $scope.roleInfoForGridConfig = {
            data : datas,
            datatype: "local",
            height: "476",
            shrinkToFit: true,
            rowNum: 10000,
            colNames: ['ID', '권한명', '권한 설명', '유효시작일', '유효종료일', '권한상태2', '권한상태'],
            colModel: [
                {name: 'id', index: 'id', editable: false, width: 30, align: "center", sortable: true},
                {name: 'name', index: 'name', editable: true, width: 80, align: "left", sortable: false},
                {name: 'description', index: 'description', editable: true, align: "left", sortable: false},
                {name: 'aplyStaDt', index: 'aplyStaDt', editable: false, width: 60, align: "center", sortable: false},
                {name: 'aplyEndDt', index: 'aplyEndDt', editable: false, width: 60, align: "center", sortable: false},
                {name: 'deleteYn', index: 'deleteYn', hidden: true, editable: false, align: "center", sortable: false},
                {name: 'state', index:'state', editable: true, edittype: 'select', editoptions: {value:{N:'활성', P:'비활성', Y:'삭제'}}, width: 40, align: "center", sortable: false}
            ],
            gridview: true,
            loadui: "block",
            multiselect: true,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            },
            onSelectRow: function(id, state){
                if(state) {	// 선택되었을 때
                    $("#" + id).addClass("skp-grid-edit-mode");
                    $(this).jqGrid("editRow", id);
                } else {
                    $(this).jqGrid("saveRow",id, false, "clientArray");
                }
                getRoleMenuCount();
            },
            editurl: "clientArray"
        };
        var table = angular.element('<table id="' + $scope.roleTableId + '" />');
        angular.element("#roleContainer").html(table);
        angular.element("#" + $scope.roleTableId).jqGrid("GridUnload");
        angular.element("#" + $scope.roleTableId).jqGrid($scope.roleInfoForGridConfig);
    }

    $scope.$on('$destroy', function() {
        $('#roleContainer').empty();
    });

    function initMenu() {
        if (!angular.isUndefined($scope.roleMenuInfo.roleCounts)) {
            if ($scope.roleMenuInfo.roleCounts.length > 0) {
                _.map($scope.roleMenuInfo.roleCounts, function (doc) {
                    $scope.menuInfo[doc.menuId] = {id : doc.menuId, count: doc.count};
                });
            }
        }
        if (!angular.isUndefined($scope.roleMenuInfo.roleMenus)) {
            if ($scope.roleMenuInfo.roleMenus.length > 0) {
                _.map(_.groupBy($scope.roleMenuInfo.roleMenus, function(doc){
                    return doc.menuId;
                }),function(grouped){
                    var ids = _.pluck(grouped, 'roleIdroleName').join(',');
                    $scope.menuInfo[grouped[0].menuId] =
                        _.extend($scope.menuInfo[grouped[0].menuId], {ids:  ids});
                });
            }
        }
    }

    // 권한 목록을 조회
    function getMenus(init) {
        if (init) {
            gridMenuInfo(null);
        } else {
            adminSvc.getAdminApi('/menu/gets').then(function(result){
                gridMenuInfo(result);
            });
        }
    }

    function getRoleMenuCount() {
        roleMenuClear();
        getSelectRoleData(false);
        var selectedGridLength = $scope.selectedRoleData.length;
        if (selectedGridLength > 0) {	//선택된 row가 있을경우
            $scope.requestGridData = {
                username: userSvc.getUser().username,
                roleIds: $scope.roleIds
            };
            adminSvc.postAdminApi('/role/roleMenuCount', $scope.requestGridData).then(function (result) {
                $scope.roleMenuInfo = result;
                getMenus(false);
            });
        } else {
            roleMenuClear();
            getMenus(true);
        }
    }

    function gridMenuInfo(datas) {
        $scope.tableId = 'menuForGrid';
        var table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#menuContainer").html(table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid({
            datatype: "local",
            height: "476",
            shrinkToFit: true,
            rowNum: 10000,
            colNames: ['ID', '서비스명', '카테고리명', '메뉴명', '노출여부', '미사용여부'],
            colModel: [
                {name: 'id', index: 'id', editable: false, width: 30, align: "center", sortable: true},
                {name: 'serviceName', index: 'serviceName', editable: false, width: 120, align: "left", sortable: false},
                {name: 'categoryName', index: 'categoryName', editable: false, width: 120, align: "left", sortable: false},
                {name: 'name', index: 'name', editable: false, align: "left", sortable: false},
                {name: 'visibleYn', index: 'visibleYn', editable: false, width: 30, align: "center", sortable: false},
                {name: 'deleteYn', index: 'deleteYn', editable: false, width: 30, align: "center", sortable: false}
            ],
            gridview: true,
            multiselect: true,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            },
            beforeSelectRow: function() {
                return $scope.selectedRoleData.length > 0 ? true : false;
            },
            onSelectRow: function (id) {
                if ($scope.selectedRoleData.length < 1) {
                    swal('권한을 선택해 주세요.');
                    return;
                }
                var ret = $(this).jqGrid("getRowData", id);
                setMenuGrid(id, ret);
            }
        });
        if (!datas) {
            // 디폴트 그리드 생성.
            angular.element('#' + $scope.tableId).jqGrid();
        } else {
            initMenu();
            for (var i = 0, dataSize = datas.length; i < dataSize; i++) {
                angular.element('#' + $scope.tableId).jqGrid('addRowData', i + 1, datas[i]);
                // 선택된 사용자 권한 있는 항목 표시.
                setBackground(i, datas[i]);
            }
        }
    }

    // 권한 추가 및 해제 설정 처리
    function setMenuGrid(id, selectedRow) {
        $scope.menuClick.push({menuId : selectedRow.id, name : selectedRow.name, click : 1});
        $scope.menuInfo[selectedRow.id].click = 1;
        if ($scope.menuInfo[selectedRow.id].background == '#fcefa1') {
            $scope.menuInfo[selectedRow.id].background = '#ffffff';
            $('#menuForGrid #' + id).css('background-color', '#ffffff');
        } else if ($scope.menuInfo[selectedRow.id].background == '#dfdfdf') {
            $scope.menuInfo[selectedRow.id].background = '#fcefa1';
            $('#menuForGrid #' + id).css('background-color', '#fcefa1');
        } else {
            $scope.menuInfo[selectedRow.id].background = '#fcefa1';
            $('#menuForGrid #' + id).css('background-color', '#fcefa1');
        }
    }

    function setBackground(i, rows) {
        var selectedRoleCount = $scope.selectedRoleData.length;
        if (!angular.isUndefined($scope.menuInfo[rows.id])) {
            if ($scope.menuInfo[rows.id].count == selectedRoleCount) {
                $scope.menuInfo[rows.id] = _.extend($scope.menuInfo[rows.id], {background:  '#fcefa1', click: 0});
                $('#menuForGrid #' + (i + 1)).css('background', '#fcefa1');//#fcefa1
            } else if ($scope.menuInfo[rows.id].count > 0 && $scope.menuInfo[rows.id].count < selectedRoleCount) {
                $('#menuForGrid #' + (i + 1)).css('background', '#dfdfdf');
                $scope.menuInfo[rows.id] = _.extend($scope.menuInfo[rows.id], {background:  '#dfdfdf', click: 0});
            } else {
                $('#menuForGrid #' + (i + 1)).css('background', '#ffffff');
                $scope.menuInfo[rows.id] = _.extend($scope.menuInfo[rows.id], {background:  '#ffffff', click: 0});
            }
        } else {
            $scope.menuInfo[rows.id] =  {id: rows.id, count: 0, background:  '#ffffff', click: 0};
            $('#menuForGrid #' + (i + 1)).css('background', '#ffffff');
        }
    }

    function getSelectRoleData(isEditable) {
        var $table = $('#' + $scope.roleTableId);
        var gr = $table.jqGrid("getGridParam","selarrrow");	// 선택된 Rows
        if (gr.length > 0) {
            var grLength = gr.length;
            $scope.selectedRoleData = [];
            _(grLength).times(function (i) {
                //선택된 값들을 Local 에 저장한다.
                if (isEditable)
                    $table.jqGrid("saveRow", gr[i], false, "clientArray");
                var ret = $table.jqGrid("getRowData", gr[i]);
                $scope.selectedRoleData.push(ret);
                $scope.roleIds.push(ret.id);
            });
            $scope.selectedRoleData = _.uniq($scope.selectedRoleData, function (item) {
                return item.id;
            });
            $scope.roleIds = [];
            var selectedRoleDataLength = $scope.selectedRoleData.length;
            _(selectedRoleDataLength).times(function (i) {
                $scope.roleIds.push($scope.selectedRoleData[i].id);
            });
        } else {
            $scope.selectedRoleData = [];
            $scope.roleIds = [];
        }
    }

    $scope.saveRoleData = function () {
        getSelectRoleData(true);
        if ($scope.selectedRoleData.length > 0) {
            // 변환한 데이터를 가지고 서버에 저장요청을 한다.
            var comRoleData = {
                username : userSvc.getUser().username,
                comRoles : $scope.selectedRoleData
            };
            adminSvc.postAdminApi('/role/updateComRoles', comRoleData).then(function(result) {
                callRoleInfoResult();
                if (result.code === 200) {
                    swal("권한 수정 정상적으로 이루어졌습니다.");
                } else {
                    swal("권한 수정에 실패했습니다.");
                }
            });

        } else {
            swal("선택된 데이터가 없습니다.");
        }
    };

    // 권한에 메뉴 추가 및 삭제하기
    $scope.saveMenuData = function() {
        if ($scope.selectedRoleData.length < 1) {
            swal('선택된 권한이 없습니다.');
            return;
        }
        var uniqueClicks = _.uniq($scope.menuClick, function (item) {
            return item.menuId;
        });
        var addMenu = [];
        var removeMenu = [];
        var removeComment = [];
        _.each(uniqueClicks, function (data, index) {
            if ($scope.menuInfo[data.menuId].background == '#fcefa1') { // 권한 추가
                addMenu.push(data.menuId);
            } else if ($scope.menuInfo[data.menuId].background == '#ffffff') {
                removeMenu.push(data.menuId);
                if (!angular.isUndefined($scope.menuInfo[data.menuId].ids)) {
                    removeComment.push($scope.menuInfo[data.menuId].ids + '권한의 ' + data.name);
                }
            }
        });

        if (addMenu.length < 1 && removeMenu.length < 1) {
            swal('선택된 메뉴가 없습니다.');
            return;
        }
        var setRoleMenu = {
            username: userSvc.getUser().username,
            roleIds : $scope.roleIds
        }
        if (addMenu.length > 0) {
            setRoleMenu.addMenus = addMenu;
        }
        if (removeMenu.length > 0) {
            setRoleMenu.removeMenus = removeMenu;
        }
        if (removeComment.length > 0) {
            var removeConfirm = confirm(removeComment.join(',') + '메뉴가 삭제됩니다.');
            if (removeConfirm) {
                handleComRoleMenu(setRoleMenu);
            }
        } else {
            handleComRoleMenu(setRoleMenu);
        }
    };

    function handleComRoleMenu(setRoleMenu) {
        adminSvc.postAdminApi('/role/handleComRoleMenu', setRoleMenu).then(function (result) {
            roleMenuClear();
            getRoleMenuCount();
            if (result.code === 200) {
                swal("권한의 메뉴 처리가 정상적으로 이루어졌습니다.");
            } else {
                swal("권한의 메뉴 처리가 실패했습니다.");
            }
        });
    }

    $scope.activeRoleUserModal = function () {
        if ($scope.roleIds.length < 1) {
            swal("권한을 먼저 선택해 주세요.");
            return;
        }
        var roleDatas = {
            roleIds: $scope.roleIds
        };
        adminSvc.postAdminApi('/role/getUserByRole', roleDatas).then(function (result) {
            if (result && result.length != 0) {
                $scope.comRoleUsers = result;
                roleUserModal = angular.element("#roleUserModal");
                roleUserModal.modal('show');
            } else {
                swal("권한에 등록된 사용자가 없습니다.");
            }
        });
    };

    $scope.activeAddRoleModal = function () {
        addRoleModal = angular.element("#addRoleModal");
        addRoleModal.modal('show');
    };

    $scope.addRole = function() {
        if (confirm("등록하시겠습니까?")) {
            $scope.addRole = $scope.addRoleForm;
            $scope.addRole.deleteYn = 'N';
            $scope.addRole.aplyStaDt = DateHelper.getCurrentDateByPattern('YYYYMMDD');
            $scope.addRole.aplyEndDt = '99991231';
            $scope.addRole.auditId = userSvc.getUser().username;
            adminSvc.postAdminApi('/role/add', $scope.addRole).then(function (result) {
                callRoleInfoResult();
                if (result.code === 200) {
                    swal("권한 등록이 정상적으로 이루어졌습니다.");
                } else {
                    swal("권한 등록에 실패했습니다.");
                }
            });
            addRoleModalClear();
        }
    };

    /**
     * 검색
     * @param result
     */
    $scope.searchRole = function() {
        callRoleInfoResult();
    };

    function roleMenuClear() {
        $scope.roleInfoForGridConfig = {};
        $scope.selectedRoleData = [];
        $scope.menuClick = [];
        $scope.roleMenuInfo = {};// 권한 및 메뉴 매핑 정보
        $scope.menuInfo = []; // 권한 및 사용자 매핑 카운트 정보
        if (roleUserModal) {
            roleUserModal.modal('hide');
        }

        if (addRoleModal) {
            addRoleModal.modal('hide');
        }
    }

    function addRoleModalClear() {
        if (addRoleModal) {
            addRoleModal.modal('hide');
        }
    }
}
