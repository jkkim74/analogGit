function userCtrl($scope, adminSvc, userSvc, menuSvc) {
    var selectedUserTypeIndex = 0;
    var roleMenuModal = null;
    $scope.dynaTreeTable = "userOrgTree";
    $scope.roleClick = [];
    $scope.userRoleInfo = {};// 권한 및 사용자 권한 매핑 정보
    $scope.selectedGridData = []; //선택된 사용자 수
    $scope.roleInfo = []; // 권한 및 사용자 매핑 카운트 정보
    $scope.searchUserNm = '';
    $scope.searchLoginId = '';

     $scope.userTypes = [
        {name: '전체', value: '0' },
        {name: '임직원', value: '1'},
        {name: '재직', value: '2'}
    ];

    $scope.init = function () {
        $scope.searchedUserType = $scope.userTypes[selectedUserTypeIndex];
        getOrgTree();
        // 널 검색도 허용하도록 하기 위해.
        $('#' + $scope.dynaTreeTable).dynatree('disable');
        $('#' + $scope.dynaTreeTable).dynatree('enable');
        getUserGrid();
    };

    $scope.changeUserType = function () {
        selectedUserTypeIndex = $scope.userTypes.indexOf($scope.searchedUserType);
    };

    $scope.searchUser = function () {
        $scope.orgCd = '';
        if (!$scope.searchUserNm && !$scope.searchLoginId) {
            getUserGrid();
        } else {
            $scope.init();
        }

    };

    function getOrgTree() {
        $scope.userOrgTreeConfig = {
            id: $scope.dynaTreeTable,
            clickFolderMode: 1,
            debugLevel: 0,
            initAjax: {
                async: false,
                url: "/admin/boss/orgTrees",
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
                if (node.data.loginId == "") {
                    node.data.loginId = "USER_SET";
                    createUserTree(node, key); //조직선택시에만 사용자 가져오기
                }
                getUserGrid(key);
                node.expand(true);
                //$scope.orgTree = node;
            },
            onRender: function() {
                $('#userOrgTree .dynatree-container').css("height", "500px");
            }
        };
    }

    /**
     * 목적 : 조직별 사용자 트리생성
     */
    function createUserTree(node, orgCd) {
        adminSvc.getOrgUserTrees(orgCd).then(function (result) {
            angular.forEach(result, function (obj) {
                node.addChild({
                    title: obj.userNm + "(" + obj.loginId + ")",
                    key: obj.loginId,
                    userNm: obj.userNm,
                    loginId: obj.loginId,
                    emailAddr: obj.emailAddr,
                    orgNm: obj.orgNm
                });
            });
        });
    }

    /**
     * 사용자 정보 조회
     *
     * @params
     */
    function getUserGrid(orgCd) {
        roleUserClear();
        $scope.selectedGridData = []; //선택된 사용자 수
        getRoles(true);
        var postData = {
            userType: $scope.userTypes[selectedUserTypeIndex].value,
            timeStamp : DateHelper.timestamp()
        };

        if (orgCd) {
            postData.searchCondition = "orgCd";
            postData.searchKeyword = orgCd;
        } else {
            if ($scope.searchUserNm) {
                postData.searchCondition = "userNm";
                postData.searchKeyword = $scope.searchUserNm;
            }
            if ($scope.searchLoginId) {
                postData.searchCondition = "loginId";
                postData.searchKeyword = $scope.searchLoginId;
            }
        }

        if (angular.isUndefined(postData.searchCondition)) {
            // 디폴트 그리드 생성.
            gridUserInfo({page:0,total:0,records:0,rows:null});
        } else {
            adminSvc.postAdminApi('/admin/boss/orgUsers', postData).then(function(result){
                gridUserInfo(result);
            });
        }
    }

    // 사용자 정보 조회
    function gridUserInfo(datas) {
        $scope.userTableId = "userForJqgrid";
        $scope.userConfig = {
            data : datas,
            datatype: "local",
            height: "476",
            shrinkToFit: true,
            rowNum: 10000,
            colNames: ['사용자ID', '사용자명', '부서', '직책', '재직상태', '사용자구분'],
            colModel: [
                {name: 'loginId', index: 'loginId', editable: false, width: 70, align: "center", sortable: true},
                {name: 'userNm', index: 'userNm', editable: false, width: 60, align: "left", sortable: false},
                {name: 'orgNm', index: 'orgNm', editable: false, width: 100, align: "left", sortable: false},
                {name: 'jbchargenm', index: 'jbchargenm', editable: false, width: 40, align: "center", sortable: false},
                {name: 'actvnYn', index: 'actvnYn', editable: false, width: 40, align: "left", sortable: false},
                {name: 'userType', index: 'userType', editable: false, width: 40, align: "center", sortable: false}
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
                getRoleUserCount();
            },
            editurl: "clientArray"
        };
        var table = angular.element('<table id="' + $scope.userTableId + '" />');
        angular.element("#userContainer").html(table);
        angular.element("#" + $scope.userTableId).jqGrid("GridUnload");
        angular.element("#" + $scope.userTableId).jqGrid($scope.userConfig);
    }

    $scope.$on('$destroy', function() {
        //$('#userContainer').children().unbind();
        //$('#userContainer').children().off();
        $('#userContainer').empty();
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

    function initRole() {
        if (!angular.isUndefined($scope.userRoleInfo.roleCounts)) {
            if ($scope.userRoleInfo.roleCounts.length > 0) {
                _.map($scope.userRoleInfo.roleCounts, function (doc) {
                    $scope.roleInfo[doc.name] = {id : doc.roleId, count: doc.count};
                });
            }
        }
        if (!angular.isUndefined($scope.userRoleInfo.roleUsers)) {
            if ($scope.userRoleInfo.roleUsers.length > 0) {
                _.map(_.groupBy($scope.userRoleInfo.roleUsers, function(doc){
                    return doc.name;
                }),function(grouped){
                    var ids = _.pluck(grouped, 'loginidUserNm').join(',');
                    $scope.roleInfo[grouped[0].name] = _.extend($scope.roleInfo[grouped[0].name], {ids:  ids});
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
                    swal('사용자를 선택해 주세요.');
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

    function setBackground(i, rows) {
        var selectedUserCount = $scope.selectedGridData.length;
        if (!angular.isUndefined($scope.roleInfo[rows.name])) {
            if ($scope.roleInfo[rows.name].count == selectedUserCount) {
                $scope.roleInfo[rows.name] = _.extend($scope.roleInfo[rows.name], {background:  '#fcefa1', click: 0});
                $('#roleForJqgrid #' + (i + 1)).css('background', '#fcefa1');//#fcefa1
            } else if ($scope.roleInfo[rows.name].count > 0 && $scope.roleInfo[rows.name].count < selectedUserCount) {
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

    function getRoleUserCount() {
        roleUserClear();
        var roleUserGrid = angular.element("#userForJqgrid");
        var selectedGrid = roleUserGrid.getGridParam("selarrrow");
        var selectedGridLength = selectedGrid.length;
        if (selectedGridLength > 0) {	//선택된 row가 있을경우
            $scope.selectedGridData = [];
            _(selectedGridLength).times(function (i) {
                var ret = roleUserGrid.jqGrid("getRowData", selectedGrid[i]);
                $scope.selectedGridData.push(ret.loginId);
            });

            $scope.requestGridData = {
                username: userSvc.getUser().username,
                loginIds: $scope.selectedGridData
            };
            adminSvc.postAdminApi('/role/roleUserCount', $scope.requestGridData).then(function (result) {
                $scope.userRoleInfo = result;
                getRoles(false);
            });
        } else {
            roleUserClear();
            getRoles(true);
        }
    }

    function roleUserClear() {
        $scope.selectedGridData = [];
        $scope.roleClick = [];
        $scope.userRoleInfo = {};// 권한 및 사용자 권한 매핑 정보
        $scope.roleInfo = []; // 권한 및 사용자 매핑 카운트 정보
        if (roleMenuModal) {
            roleMenuModal.modal('hide');
        }
    }

    $scope.activeRoleMenuModal = function () {
        var roleIds = [];
        _.each($scope.roles, function (data) {
            if ($scope.roleInfo[data.name].background == '#fcefa1' ||
                $scope.roleInfo[data.name].background == '#dfdfdf') {
                roleIds.push({id: parseInt(data.id), name: data.name});
            }
        });
        if (roleIds.length < 1) {
            swal("선택된 사용자의 허가된 그룹 권한이 없습니다.");
            return;
        }
        var roleDatas = {
            comRoles : roleIds
        };
        adminSvc.postAdminApi('/menu/getMenusByRole', roleDatas).then(function (result) {
            if (result.length != 0) {
                $scope.menus = result;
                roleMenuModal = angular.element("#roleMenuModal");
                roleMenuModal.modal('show');
            }
        });
    };

    $scope.saveRoleData = function() {
        if ($scope.selectedGridData.length < 1) {
            swal('선택된 사용자가 없습니다.');
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
                    removeComment.push($scope.roleInfo[data.name].ids + '님의 ' + data.name);
                }
            }
        });

        if (addRole.length < 1 && removeRole.length < 1) {
            swal('선택된 권한이 없습니다.');
            return;
        }
        var setUserRole = {
            username: userSvc.getUser().username,
            loginIds : $scope.selectedGridData
        }
        if (addRole.length > 0) {
            setUserRole.addRoles = addRole;
        }
        if (removeRole.length > 0) {
            setUserRole.removeRoles = removeRole;
        }
        if (removeComment.length > 0) {
            var removeConfirm = confirm(removeComment.join(',') + '권한이 삭제됩니다.');
            if (removeConfirm) {
                handleComRoleUser(setUserRole);
            }
        } else {
            handleComRoleUser(setUserRole);
        }
    };

    // 현재 Voyager에서 사용되는 서비스 전체 목록 조회.
    function loadMenuData() {
        menuSvc.loadReportService();
        menuSvc.loadDashboardService();
        menuSvc.loadAdminService();
        menuSvc.loadNavigationMenu();
    }

    function handleComRoleUser(setUserRole) {
        adminSvc.postAdminApi('/role/handleComRoleUser', setUserRole).then(function (result) {
            roleUserClear();
            getRoleUserCount();
            if (result.code === 200) {
                swal("권한 처리가 정상적으로 이루어졌습니다.");
                loadMenuData();
            } else {
                swal("권한 처리가 실패했습니다.");
            }
        });
    }
}
