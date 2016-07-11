function userRoleMenuCtrl($scope, adminSvc, userSvc) {
    var $userSearchModal = null;
    var $userRoleModal = null;
    var $userMenuModal = null;

    $scope.init = function () {
        $scope.searchedText = null;
        $scope.searchedUser = null;
        $scope.OrgUsers = [];

        callUserRoleResult();
        callUserMenuResult();
    };

    $scope.activeUserModal = function () {
        $userSearchModal = angular.element('#userSearchModal');
        $userSearchModal.modal('show');
    };

    function userSearchClear() {
        $scope.searchedText = null;
        $scope.OrgUsers = [];
        if($userSearchModal) {
            $userSearchModal.modal('hide');
        }
    }

    function userRoleClear() {
        $scope.roles = [];
        if($userRoleModal) {
            $userRoleModal.modal('hide');
        }
    }

    function userMenuClear() {
        $scope.menus = [];
        if($userMenuModal) {
            $userMenuModal.modal('hide');
        }
    }

    $scope.addSearchUser = function () {
        var $checks = $("input[name=chk_user]:checked");
        var checkedSize = $checks.size();
        if (checkedSize < 1 || checkedSize > 1) {
            alert("한명의 사용자를 선택해 주세요.");
            return;
        }
        $scope.searchedUser = $checks.val();
        userSearchClear();
    };

    $scope.searchUser = function () {
        // 검색할 사용자 정보 셋팅.
        $scope.searchParam = {
            searchCondition: 'userNm',
            searchKeyword: $scope.searchedText
        };
        adminSvc.getOrgUsers($scope.searchParam).then(function (result) {
            $scope.OrgUsers = result;
        });
    };

    $scope.searchUserRoleMenu = function () {
        callUserRoleResult();
        callUserMenuResult();
    };

    $scope.activeUserRoleModal = function () {
         if (!$scope.searchedUser) {
            swal("권한을 등록시 사용자를 선택해 권한 조회를 먼저해 주세요.");
            return;
         }
        adminSvc.getAdminApi('/role/gets').then(function(result) {
            if (result.rows.length != 0) {
                $scope.roles = result.rows;
                $userRoleModal = angular.element("#userRoleModal");
                $userRoleModal.modal('show');
            }
        });
    };

    $scope.activeUserMenuModal = function () {
        if (!$scope.searchedUser) {
            swal("메뉴 권한을 등록시 사용자를 선택해 권한 조회를 먼저해 주세요.");
            return;
        }
        adminSvc.getAdminApi('/menu/gets').then(function(result) {
            if (result.length != 0) {
                $scope.menus = result;
                $userMenuModal = angular.element("#userMenuModal");
                $userMenuModal.modal('show');
            }
        });
    };

    $scope.addUserRole = function() {
        var $checks = $("input[name='chk_role']:checked");
        var checkedSize = $checks.size();
        if (checkedSize < 1) {
            swal("등록할 권한을 선택해 주세요.");
            return;
        }
        var roleUserDatas = _.map($checks, function(el){return {roleId : $(el).val(), loginId : $scope.searchedUser};});
        if (roleUserDatas.length) {
            var userRole = {
                username : userSvc.getUser().username,
                comRoleUsers : roleUserDatas
            };
            adminSvc.postAdminApi('/role/addUserRole', userRole).then(function(result) {
                if (result.code === 200) {
                    swal("권한 등록이 정상적으로 이루어졌습니다.");
                } else {
                    swal("권한 등록이 실패했습니다.");
                }
                $scope.searchUserRoleMenu();
            });
            userRoleClear();
        }

    };

    $scope.addUserMenu = function() {
        var $checks = $("input[name='chk_menu']:checked");
        var checkedSize = $checks.size();
        if (checkedSize < 1) {
            swal("등록할 권한을 선택해 주세요.");
            return;
        }
        var userMenuDatas = _.map($checks, function(el) {
            return {
                menuId : $(el).val(),
                loginId : $scope.searchedUser,
                createYn: 'Y',
                readYn: 'Y',
                updateYn: 'Y',
                deleteYn: 'Y'
            };
        });
        if (userMenuDatas.length) {
            var userMenu = {
                username : userSvc.getUser().username,
                comUserMenus : userMenuDatas
            };
            adminSvc.postAdminApi('/role/addUserMenu', userMenu).then(function(result) {
                if (result.code === 200) {
                    swal("메뉴 권한 등록이 정상적으로 이루어졌습니다.");
                } else {
                    swal("메뉴 권한 등록이 실패했습니다.");
                }
                $scope.searchUserRoleMenu();
            });
            userMenuClear();
        }
    };

    /**
     * 사용자 권한 정보 조회
     *
     * @params
     */
    function callUserRoleResult() {
        var url = '/role/userRoles?loginId=' + $scope.searchedUser;

        $scope.userRoleForGridConfig = {
            url: url,
            type: "GET",
            datatype: "json",
            height: "150",
            shrinkToFit: true,
            loadui: "block",
            rowNum: 10000,
            id: 'userRoleForGrid',
            colNames: ['ID', '권한명', '권한 설명', '사용여부', '등록자', '변경일자'],
            colModel: [
                {name: 'id', index: 'id', editable: false, width: 30, align: "center", sortable: true},
                {name: 'name', index: 'name', editable: true, align: "left", sortable: false},
                {name: 'description', index: 'description', editable: true, align: "left", sortable: false},
                {name: 'deleteYn', index: 'deleteYn', editable: true, width: 30, align: "center", sortable: false},
                {name: 'auditId', index: 'auditId', editable: false, width: 60, align: "center", sortable: false},
                {name: 'auditDtm', index: 'auditDtm', editable: false, align: "center", sortable: false}
            ],
            gridview: true,
            multiselect: true,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            jsonReader: {
                root: "rows"
            },
            onSelectRow: function(id){
                $(this).jqGrid("getRowData", id);
            },
            editurl: "clientArray",
            gridComplete: function () {
                $scope.userRoleGrid = $(this);
            }
        };
    }

    // 사용자별 권한 정보 삭제
    $scope.removeUserRole = function() {
        var gridData = [];
        var gr = $scope.userRoleGrid.jqGrid("getGridParam","selarrrow");	// 선택된 Rows
        if (gr.length > 0) {
            //for (var i = 0; i < gr.length; i++) {
            var grLength = gr.length;
            grLength.times(function (i) {
                //선택된 값들을 Local 에 저장한다.
                var ret = $scope.userRoleGrid.jqGrid("getRowData", gr[i]);
                gridData.push(ret);
            });
            // 변환한 데이터를 가지고 서버에 저장요청을 한다.
            $scope.setComRoleUserData = {
                username : userSvc.getUser().username,
                loginId : $scope.searchedUser,
                comRoles : gridData
            };
            adminSvc.postAdminApi('/role/removeComRoleUsers', $scope.setComRoleUserData).then(function(result) {
                if (result.code === 200) {
                    callUserRoleResult();
                    swal("사용자 권한 정보 삭제가 정상적으로 이루어졌습니다.");
                } else {
                    swal("사용자 권한 정보 삭제에 실패했습니다.");
                }
            });
        } else {
            swal("선택된 권한이 없습니다.");
        }
    };

    function callUserMenuResult() {
        var url = '/role/userMenus?loginId=' + $scope.searchedUser;

        $scope.userMenuForGridConfig = {
            url: url,
            type: "GET",
            datatype: "json",
            height: "200",
            shrinkToFit: true,
            loadui: "block",
            rowNum: 10000,
            id: 'userMenuForGrid',
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
            jsonReader: {
                root: "rows"
            },
            onSelectRow: function(id){
                $(this).jqGrid("getRowData", id);
            },
            editurl: "clientArray",
            gridComplete: function () {
                $scope.userMenuGrid = $(this);
            }
        };
    }

    // 사용자별 메뉴 정보 삭제
    $scope.removeUserMenu = function() {
        var gridData = [];
        var gr = $scope.userMenuGrid.jqGrid("getGridParam","selarrrow");	// 선택된 Rows
        if (gr.length > 0) {
            //for (var i = 0; i < gr.length; i++) {
            var grLength = gr.length;
            grLength.times(function (i) {
                //선택된 값들을 Local 에 저장한다.
                var ret = $scope.userMenuGrid.jqGrid("getRowData", gr[i]);
                gridData.push(ret);
            });
            // 변환한 데이터를 가지고 서버에 저장요청을 한다.
            $scope.setComUserMenuData = {
                username : userSvc.getUser().username,
                loginId : $scope.searchedUser,
                menus : gridData
            };
            adminSvc.postAdminApi('/role/removeComUserMenus', $scope.setComUserMenuData).then(function(result) {
                if (result.code === 200) {
                    callUserMenuResult();
                    alert("사용자 메뉴 정보 삭제가 정상적으로 이루어졌습니다.");
                } else {
                    alert("사용자 메뉴 정보 삭제에 실패했습니다.");
                }
            });
        } else {
            alert("선택된 메뉴가 없습니다.");
        }
    };
}
