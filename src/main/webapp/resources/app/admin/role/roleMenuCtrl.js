function roleMenuCtrl($scope, adminSvc, userSvc) {
    var $roleSearchModal = null, $roleMenuModal = null;

    $scope.init = function () {
        $scope.searchString = null;
        $scope.roleId = null;
        $scope.menus = [];
        $scope.roles = [];
        $scope.pageState = {
            'loading': false,
            'emptyData': true
        };
    };

    function roleSearchClear() {
        $scope.roles = [];
        if ($roleSearchModal) {
            $roleSearchModal.modal('hide');
        }
    }

    function roleMenuClear() {
        $scope.menus = [];
        if ($roleMenuModal) {
            $roleMenuModal.modal('hide');
        }
    }

    $scope.activeRoleModal = function () {
        adminSvc.getAdminApi('/role/gets').then(function (result) {
            if (result.rows.length != 0) {
                $scope.roles = result.rows;
                $roleSearchModal = angular.element("#roleSearchModal");
                $roleSearchModal.modal('show');
            }
        });
    };

    $scope.addSearchRole = function () {
        var $checks = $("input[name=chk_role]:checked");
        var checkedSize = $checks.size();
        if (checkedSize < 1 || checkedSize > 1) {
            swal("하나의 권한을 선택해 주세요.");
            return;
        }
        $scope.searchString = $checks.val();
        $scope.roleId = $checks.attr('id').substring(9);
        roleSearchClear();
    };

    $scope.activeRoleMenuModal = function () {
        if (!$scope.searchString) {
            swal("권한의 메뉴를 등록 등록시 권한을 먼저 선택해 주세요.");
            return;
        }
        adminSvc.getAdminApi('/menu/gets').then(function (result) {
            if (result.length != 0) {
                $scope.menus = result;
                $roleMenuModal = angular.element("#roleMenuModal");
                $roleMenuModal.modal('show');
            }
        });
    };

    $scope.addRoleMenu = function () {
        var menuData = [], tmp;
        var $checkMenus = $("input[name=menuId]:checked");
        $.each($checkMenus, function(i, obj) {
            tmp = {
                'id': $(obj).val()
            };
            menuData.push(tmp);
        });
        $scope.roleMenus = {
            username : userSvc.getUser().username,
            roleId: $scope.roleId,
            menus: menuData
        };
        adminSvc.postAdminApi('/role/addRoleMenu', $scope.roleMenus).then(function (result) {
            if (result.code === 200) {
                callRoleMenuResult();
                swal("권한 메뉴 등록이 정상적으로 이루어졌습니다.");
            } else {
                swal("권한 메뉴 등록이 실패했습니다.");
            }
        });
        roleMenuClear();
    };

    function callRoleMenuResult() {
        var url = '/role/roleMenus';
        $scope.requestData = {
            searchString: ''
        };
        if ($scope.searchString != null) {
            $scope.requestData.searchString = $scope.searchString;
            $scope.pageState = {
                'loading': true,
                'emptyData': true
            };
        } else {
            swal("권한명 필수 입력 사항입니다. 권한을 선택해 주세요.");
            return;
        }
        $scope.tableId = 'roleMenuTableForGrid';
        adminSvc.postAdminApi(url, $scope.requestData).then(function (result) {
            var dataLength = result.rows.length;
            if (dataLength != 0) {
                gridRoleMenu(result.rows);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    function gridRoleMenu(datas) {
        $scope.roleMenuForGridConfig = {
            data: datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 10000,
            colNames: ['ID', '서비스명', '레벨', '경로명(서비스>카테고리>메뉴)', '메뉴명', '노출여부', '사용여부'],
            colModel: [
                {name: 'id', index: 'id', editable: false, width: 30, align: "center", sortable: true},
                {name: 'rootName', index: 'rootName', editable: false, align: "left", sortable: false},
                {name: 'menuLevel', index: 'menuLevel', editable: false, width: 70, align: "left", sortable: false},
                {name: 'pathName', index: 'pathName', editable: false, width: 200, align: "left", sortable: false},
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
            onSelectRow: function(id){
                $(this).jqGrid("getRowData", id);
            },
            editurl: "clientArray",
            gridComplete: function () {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            }
        };
        var table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#roleMenuForGrid").html(table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.roleMenuForGridConfig);
    }

    /**
     * 검색
     * @param result
     */
    $scope.searchRole = function () {
        if ($scope.searchString == null) {
            swal('권한을 선택해 주세요.');
            return;
        }
        callRoleMenuResult();
    };

    $scope.removeRoleMenu = function() {
        var gridData = [];
        var $table = $('#' + $scope.tableId);
        var gr = $table.jqGrid("getGridParam","selarrrow");	// 선택된 Rows
        if (gr.length > 0) {
            //for (var i = 0; i < gr.length; i++) {
            var grLength = gr.length;
            grLength.times(function (i) {
                //선택된 값들을 Local 에 저장한다.
                //$table.jqGrid("saveRow", gr[i], false, "clientArray");
                var ret = $table.jqGrid("getRowData", gr[i]);
                gridData.push(ret);
            });
            // 변환한 데이터를 가지고 서버에 저장요청을 한다.
            $scope.setComRoleMenuData = {
                username : userSvc.getUser().username,
                roleId: $scope.roleId,
                menus : gridData
            };
            adminSvc.postAdminApi('/role/removeComRoleMenus', $scope.setComRoleMenuData).then(function(result) {
                if (result.code === 200) {
                    callRoleMenuResult();
                    swal("권한 메뉴 삭제가 정상적으로 이루어졌습니다.");
                } else {
                    swal("권한 메뉴 삭제에 실패했습니다.");
                }
            });
        } else {
            swal("선택된 데이터가 없습니다.");
        }
    };
}
