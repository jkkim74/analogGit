function kidEmailCtrl($scope, adminSvc, userSvc, ngDialog, blockUI) {
    $scope.preViewFlag = false;
    $scope.mailReports = '';
    $scope.form = {};
    $scope.addable = true;
    $scope.form.ocbComment = '';

    $scope.init = function () {
        $scope.dateFlag = false;
        $scope.previousDate = DateHelper.getPreviousDate(1, 'YYYYMMDD');//"20140716";
        $scope.thisDate = $scope.previousDate.substring(4, 6) + "." + $scope.previousDate.substring(6);
        $scope.previousCommaDate = DateHelper.stringToYmdStr($scope.previousDate, '.');
        callBpmEmailResult();
    };

    //조직도, 메일 발송자 리스트, 경영실적 메일링 리포트 정도 로딩.
    function callBpmEmailResult() {
        var lastSel;
        $scope.bpmOrgTreeConfig = {
            id: 'bpmOrgTree',
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
                node.expand(true);
                $scope.orgTree = node;
            }
        };

        var emailUrl = '/admin/boss/emailUsers?sndObjId=3';
        $scope.emailUserConfig = {
            url: emailUrl,
            type: "GET",
            datatype: "json",
            height: "275",
            shrinkToFit: true, //width scroll
            loadui: "block",
            rowNum: 10000,
            id: 'emailUserForGrid',
            colNames: ['사번', '대상자명', '조직명', '이메일'],
            colModel: [
                {name: 'loginId', index: 'loginId', editable: false, width: 70, align: "center", sortable: false},
                {name: 'userNm', index: 'userNm', editable: false, width: 70, align: "center", sortable: false},
                {name: 'orgNm', index: 'orgNm', editable: false, width: 150, align: "center", sortable: false},
                {name: 'emailAddr', index: 'emailAddr', editable: false, width: 150, align: "center", sortable: false}
            ],
            onSelectRow: function (id, state) {
                if (state) {	// 선택되었을 때
                    lastSel = id;
                    afterEditRow(id);
                    $("#" + id).addClass("skp-grid-edit-mode");
                    $(this).jqGrid("editRow", id);
                } else {
                    $(this).jqGrid("saveRow", id, false, "clientArray");
                }
            },
            gridComplete: function () {
                $scope.emailUserGrid = $(this);
            },
            editurl: "clientArray",
            gridview: true,
            useColSpanStyle: true,
            sortname: 'userNm',
            sortorder: 'asc',
            multiselect: true,
            autowidth: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            }
        };
        getMailReportData();
    }

    // 메일 발송에 필요한 데이터 조회.
    function getMailReportData() {
        var url = '/admin/boss/businessDatas?basicDate=' + $scope.previousDate;
        adminSvc.getAdminApi(url).then(function(result) {
            if (result) {
                $scope.mailReports = result;
                $scope.thisPeriod = result.thisPeriod;
                $scope.form.ocbComment = (result.ocbComment) ? result.ocbComment : '' ;
                $scope.form.syrupComment = (result.syrupComment) ?  result.syrupComment : '';
                $scope.form.hoppinComment = (result.hoppinComment) ? result.hoppinComment : '';
                $scope.form.tstoreComment = (result.tstoreComment) ? result.tstoreComment : '';
                $scope.form.tmapComment = (result.tmapComment) ? result.tmapComment : '';
                $scope.form.sk11Comment = (result.sk11Comment) ? result.sk11Comment : '';
                $scope.form.tcloudComment = (result.tcloudComment) ? result.tcloudComment : '';
            }
        });
    }

    // 그리드 Row 선택시 적용 함수
    function afterEditRow(id) {
        var emailGrid = $scope.emailUserGrid;
        var ret = emailGrid.jqGrid("getRowData", id);
        if (ret.flag != "I") {
            emailGrid.jqGrid("setRowData", id, {flag: "U"}); // flag 변경
        }
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

    // 메일 수신자 대상에 추가
    $scope.addMailUser = function () {
        var node = $scope.orgTree;
        if (node) {
            if (node.data.loginId == "" || node.data.loginId == "USER_SET") {
                swal('사용자를 선택하세요');
                return false;
            } else {
                addRowInGrid(node.data.userNm, node.data.orgNm, node.data.loginId, node.data.emailAddr);
                // 변환한 데이터를 가지고 서버에 저장요청을 한다.
                if ($scope.addable) {
                    $scope.createMailGridData = {
                        auditId: userSvc.getUser().username,
                        sndObjId: 3,
                        loginId: node.data.loginId
                    };
                    //메일링 뉴스레터 수신자 등록 처리.
                    adminSvc.createMailUser($scope.createMailGridData).then(function (createResult) {
                        if (createResult.code === 200) {
                            swal("메일링 리스트 추가 처리가 정상적으로 이루어졌습니다.");
                        } else {
                            swal("메일링 리스트 추가 처리가 실패했습니다.");
                        }
                    });
                }
                $scope.preViewFlag = false;
            }
        } else {
            swal('사용자를 선택하세요');
            return false;
        }
    };

    // 메일 수신 대상자 제거
    $scope.deleteMailUser = function () {
        var delMailUserGrid = $scope.emailUserGrid;
        var mailGrid = delMailUserGrid.getGridParam("selarrrow");
        var mailGridLength = mailGrid.length;
        if (mailGridLength > 0) {	//선택된 row가 있을경우
            var mailGridData = [];
            //for (var i = 0; i < mailGridLength; i++) {
            mailGridLength.times(function (i) {
                var ret = delMailUserGrid.jqGrid("getRowData", mailGrid[i]);
                mailGridData.push(ret.loginId);
                delMailUserGrid.delRowData(mailGrid[i]);
            });

            // 변환한 데이터를 가지고 서버에 저장요청을 한다.
            $scope.deleteMailGridData = {
                username: userSvc.getUser().username,
                loginIds: mailGridData,
                itemCode : 3
            };
            adminSvc.deleteMailUser($scope.deleteMailGridData).then(function (delResult) {
                if (delResult.code === 200) {
                    swal("삭제 처리가 정상적으로 이루어졌습니다.");
                } else {
                    swal("삭제 처리가 실패했습니다.");
                }
            });
            $scope.preViewFlag = false;
        } else {
            swal("제외하실 사용자를 선택해 주세요.");
            return false;
        }
    };

    // 메일링 수신자 추가
    function addRowInGrid(userNm, orgNm, loginId, emailAddr) {
        var emailGrid = $scope.emailUserGrid;
        var gridTotalRecord = emailGrid.getGridParam("records");

        // 그리드에 존재유무 체크
        var recs = emailGrid.getDataIDs();
        var recsLength = recs.length;
        //for (var i = 0; i < recsLength; i++) {
        recsLength.times(function (i) {
            if (emailGrid.jqGrid('getCell', recs[i], "loginId") == loginId) {
                swal('이미 추가된 아이디입니다');
                $scope.addable = false;
                return;
            }
        });

        // 2. Grid 의 등록 함수를 호출한다.
        emailGrid.jqGrid("addRow", {
            rowID: "new_row_" + gridTotalRecord, // 새로 생성하는 Row 의 key
            position: "first", // 생성 위치(first/last)
            initdata: {userNm: userNm, orgNm: orgNm, loginId: loginId,
                emailAddr: emailAddr} || {flag: "I"},	// 초기 데이터
            addRowParams: {
                oneditfunc: function (id) {
                    lastsel = id;
                }
            }
        });
    }

    // 코멘트 저장
    $scope.commentSave = function () {
        $scope.preViewFlag = false;
        $scope.form.stcStrdDt = $scope.previousDate;
        $scope.form.auditId = userSvc.getUser().username;
        adminSvc.createMailComments($scope.form).then(function (result) {
            if (result.code !== 200) {
                swal('저장과정에 오류가 발생했습니다. 관리자에게 문의해 주세요.');
                return false;
            } else {
                swal('정상적으로 처리가 완료되었습니다.');
                return false;
            }
        });
    };

    // 이메일 발송전 미리보기
    $scope.preView = function () {
        $scope.preViewFlag = true;
        // 메일 발송일자를 가지고 미리 보기 요청을 보낸다.
        $scope.getMailGridData = {
            username: userSvc.getUser().username,
            basicDate: $scope.previousDate
        };
        var url = '/admin/boss/mailContentsForKid';
        adminSvc.postAdminApi(url, $scope.getMailGridData).then(function(result) {
            $scope.mailContents = result;
            ngDialog.open({
                template: $scope.mailContents.emailCtxtCtt,
                plain: true,
                scope: $scope,
                controller: 'kidEmailCtrl',
                className: 'ngdialog-theme-default'
            });
        });
    };

    // 이메일 발송 처리
    $scope.sendMail = function () {
        if (!$scope.preViewFlag) {
            swal("미리보기 화면을 먼저 확인하고 클릭하세요.");
            return;
        }
        blockUI.start();
        // 메일 발송일자를 파라미터로 보낸다.
        $scope.mailDatas = {
            basicDate: $scope.previousDate
        };
        var url = '/admin/boss/sendMailsForKid';
        adminSvc.postAdminApi(url, $scope.mailDatas).then(function(result) {
            blockUI.stop();
            if (result.code === 200) {
                swal("메일이 정상적으로 발송되었습니다.");
            } else {
                swal("일부 혹은 전체 메일 발송이 실패했습니다.");
            }
        });
    };
}
