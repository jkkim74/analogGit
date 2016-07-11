function orgRoleCtrl($scope, $timeout, adminSvc, userSvc) {
    var selectedGroupTypeIndex = 0;
    var $orgRoleModal = null;

    $scope.groupTypes = [
        {name: '전체', value: 'all' },
        {name: '정조직', value: '1'},
        {name: '가상조직', value: '2'}
    ];

    $scope.init = function () {
        $scope.searchedGroupType = $scope.groupTypes[selectedGroupTypeIndex];
        $scope.orgCd = '';
        $scope.searchedOrgNm = '';
        callOrgResult();
        callRoleOrgResult();
    };

    $scope.changeGroupType = function () {
        selectedGroupTypeIndex = $scope.groupTypes.indexOf($scope.searchedGroupType);
    };

    $scope.searchOrgRole = function () {
        $scope.orgCd = '';
        callOrgResult();
        callRoleOrgResult();
    };


    /**
     * 사용자 권한 정보 조회
     *
     * @params
     */
    function callOrgResult() {
        var postData = {
            orgNm: $scope.searchedOrgNm,
            groupType: $scope.groupTypes[selectedGroupTypeIndex].value
        };
        $scope.orgForGridConfig = {
            id: 'orgForGrid',
            url: '/role/orgs',
            mtype: "POST",
            postData: postData,
            ajaxGridOptions: { contentType: "application/x-www-form-urlencoded; charset=utf-8" },
            datatype: "json",
            height: "200",
            shrinkToFit: true,
            loadui: "block",
            rowNum: 10000,
            colNames: ['조직유형', '조직코드', '조직명', '활성화여부', '상위조직명', '적용시작일', '적용종료일'],
            colModel: [
                {name: 'groupType', index: 'groupType', editable: false, width: 50, align: "center", sortable: true},
                {name: 'orgCd', index: 'orgCd', editable: true, width: 80, align: "left", sortable: false},
                {name: 'orgNm', index: 'orgNm', editable: true, align: "left", sortable: false},
                {name: 'actvnYn', index: 'actvnYn', editable: true, width: 30, align: "center", sortable: false},
                {name: 'supOrgNm', index: 'supOrgNm', editable: true, align: "left", sortable: false},
                {name: 'aplyStaDt', index: 'aplyStaDt', editable: false, width: 80, align: "center", sortable: false},
                {name: 'aplyEndDt', index: 'aplyEndDt', editable: false, width: 80, align: "center", sortable: false}
            ],
            gridview: true,
            multiselect: true,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            viewrecords: true,
            jsonReader: {
                root: "rows"
            },
            onSelectRow: function (id, state) {
                if (state) {
                    getRoleByOrgCd();
                }
            },
            editurl: "clientArray",
            gridComplete: function () {
                $scope.orgGrid = $(this);
            }
        };
    }

    function getRoleByOrgCd() {
        var orgGrid = $scope.orgGrid;
        var selOrgGrid = orgGrid.getGridParam("selarrrow");
        var selOrgGridLength = selOrgGrid.length;
        if (selOrgGridLength == 1) {	//선택된 row가 있을경우
            var ret = orgGrid.jqGrid("getRowData", selOrgGrid[0]);
            $scope.$safeApply(function() {
                $scope.orgCd = ret.orgCd;
                callRoleOrgResult();
            });
        } else {
            alert('하나의 조직만을 선택해 주세요.');
            return false;
        }
    }

    function callRoleOrgResult() {
        var url = '/role/orgRoles?orgCd=' + $scope.orgCd + '&rand=' + Math.floor(Math.random() * 10000);
        $scope.orgRoleForGridConfig = {
            url: url,
            type: "GET",
            datatype: "json",
            height: "200",
            shrinkToFit: true,
            loadui: "block",
            rowNum: 10000,
            id: 'orgRoleForGrid',
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
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            jsonReader: {
                root: "rows"
            }
        };
    }

    function orgRoleClear() {
        $scope.roles = [];
        if ($orgRoleModal) {
            $orgRoleModal.modal('hide');
        }
    }

    $scope.activeOrgRoleModal = function () {
        adminSvc.getAdminApi('/role/gets').then(function (result) {
            if (result.rows.length != 0) {
                $scope.roles = result.rows;
                $orgRoleModal = angular.element("#orgRoleModal");
                $orgRoleModal.modal('show');
            }
        });
    };

    $scope.addOrgRole = function () {
        var $checks = $("input[name='chk_role']:checked");
        var checkedSize = $checks.size();
        if (checkedSize < 1) {
            alert("등록할 권한을 선택해 주세요.");
            return;
        }
        var roleOrgDatas = _.map($checks, function (el) {
            return {roleId: $(el).val(), orgCd: $scope.orgCd};
        });
        if (roleOrgDatas.length) {
            var orgRole = {
                username: userSvc.getUser().username,
                comRoleOrgs: roleOrgDatas
            };
            adminSvc.postAdminApi('/role/addRoleOrg', orgRole).then(function (result) {
                if (result.code === 200) {
                    alert("권한 등록이 정상적으로 이루어졌습니다.");
                } else {
                    alert("권한 등록이 실패했습니다.");
                }
                callRoleOrgResult();
            });
            orgRoleClear();
        }
    };
}
