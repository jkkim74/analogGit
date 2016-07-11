var readSelectQnaCtrl = function ($scope, $http, userSvc, $modalInstance, qnaList,
                                  categoryList, selectId, curIndex, condition, menuSvc, apiSvc) {

    $scope.replyForm = {};

    var returnValue = {
        work: "none",
        selectItem: qnaList[curIndex]
    };

    $scope.summernoteConfig = {
//        height: "326",//"269px",
//        focus: false,
        welEditable: false,
        toolbar: [
//            ['style', ['bold', 'italic', 'underline', 'clear']],
//            ['fontsize', ['fontsize']],
//            ['color', ['color']],
//            ['para', ['ul', 'ol', 'paragraph']],
//            ['height', ['height']]
        ]
    };

    $scope.modalInit = function () {
        $scope.selectQna = qnaList[curIndex];
        $scope.replyForm.context = '';
        $scope.replyList = {};

        loadFileList($scope.selectQna.id);
        increseHitCount($scope.selectQna.id);
        refreshReplyList($scope.selectQna.id);
    };

    $scope.refreshSelectQna = function (id, boardType, navigator) {
        var tmpUrl = "/helpdesk/prevNextItem";
        var queryWhere = {
            id: id,
            boardType: boardType,
            navigator: navigator,
            hdSearchCategory: condition.hdSearchCategory,
            hdSearchType: condition.hdSearchType,
            hdSearchString: condition.hdSearchString
        };
        $http.post(tmpUrl, queryWhere).success(function (data) {
            if (data.length > 0) {
                $scope.selectQna = data[0];

                returnValue.selectItem = $scope.selectQna;
                loadFileList($scope.selectQna.id);
                refreshReplyList($scope.selectQna.id);
            } else {
                alert("마지막 글 입니다...");
            }
        });
    };

    $scope.getCodeName = function (code) {
        for (var i in categoryList) {
            if (code == categoryList[i].codeId) {
                return categoryList[i].codeName;
            }
        }
        return 'not matched category!';
    };

    $scope.cancel = function () {
        returnValue.work = 'cancel';
        $modalInstance.dismiss(returnValue);
    };
    $scope.delete = function () {

//        var cancelConfirm = ;
        if (confirm('해당 글을 삭제하시겠습니까?')) {
            deleteQna($scope.selectQna.id);
            returnValue.work = 'delete';
        }
        $modalInstance.close(returnValue);
    };
    $scope.update = function () {
        returnValue.work = 'update';
        $modalInstance.close(returnValue);
    };

    $scope.replyCompletion = function () {
        replyCompletionYes($scope.selectQna.id);
        returnValue.work = 'replyCompletion';
        $modalInstance.close(returnValue);
    };

    $scope.downloadFile = function (path) {
        alert("다운로드 준비중입니다...");
        //console.log("downloadFile....path : " + path);
    };

    $scope.replySave = function () {
        if ($scope.replyForm.context == null || $scope.replyForm.context == "") {
            alert("답글을 확인하세요.");
            return;
        }

        var tmpUrl = "/helpdesk/reply/qna/insert";
        var queryWhere = {
            id: $scope.selectQna.id,
            context: $scope.replyForm.context,
            writer: userSvc.getUser().fullname,
            writerId: userSvc.getUser().username,
            writeIp: userSvc.getUser().ip
        };

        //console.log(queryWhere);
        $http.post(tmpUrl, queryWhere).success(function () {
            clearReplyContext();
            refreshReplyList($scope.selectQna.id)
        });
    };

    $scope.replyDelete = function (selectSn, selectId) {
        var tmpUrl = "/helpdesk/reply/qna/delete";
        var queryWhere = {
            sn: selectSn,
            updateIp: userSvc.getUser().ip
        };

        //console.log(queryWhere);
        $http.post(tmpUrl, queryWhere).success(function () {
            refreshReplyList(selectId)
        });
    };

    $scope.replyUpdateToggle = function (index) {
        //console.log("replyUpdateToggle()");
        //replyState - true:수정, false:수정취소
        $scope.replyList[index].replyState = !$scope.replyList[index].replyState;

        if (!$scope.replyList[index].replyState) {
            $scope.replyList[index].tmpContext = $scope.replyList[index].context;
        }
        //console.log($scope.replyList[index].replyState);
    };

    $scope.replyUpdate = function (selectSn, selectId, index) {
        var tmpUrl = "/helpdesk/reply/qna/update";
        var queryWhere = {
            sn: selectSn,
            id: selectId,
            context: $scope.replyList[index].tmpContext,
            updateIp: userSvc.getUser().ip
        };

        $http.post(tmpUrl, queryWhere).success(function () {
            refreshReplyList(selectId)
        });
    };

    $scope.checkUser = function (accessId) {
        //Todo - if need to seeting admin view?
        if (userSvc.getUser().username == accessId) {
            return true;
        }
        return false;
    };

    $scope.checkAuthority = function () {
        return (menuSvc.getNavigationMenu().helpDeskAdmin == 'Y');
    };

    $scope.checkReplyer = function (replyerId) {
        return apiSvc.getComeRoleUser(replyerId, 5) ? true : false;
    };

    function clearReplyContext() {
        $scope.replyForm.context = '';
    }

    function refreshReplyList(selectId) {
        var tmpUrl = "/helpdesk/reply/qna/select";
        var queryWhere = {
            id: selectId
        };

        $http.post(tmpUrl, queryWhere).success(function (data) {
            $scope.replyList = data;

            for (var i in $scope.replyList) {
                $scope.replyList[i].replyState = true;  //ture:수정, false:수정취소 (답글 수정가능여부)
            }
        });

    }

    function loadFileList(selectId) {
        var qnaSelectFilesUrl = "/file/getMetaFileLists?containerName=helpdesk&contentId=" + selectId;
        $http.get(qnaSelectFilesUrl).success(function (data) {
            $scope.selectQnaFiles = data;
            //console.log($scope.selectQnaFiles);

            $('.note-editor .note-toolbar').remove();
            $('.note-editor .note-statusbar').remove();
            $('.note-editor .note-editable').attr('contenteditable', 'false');
            $('.note-editor').css('border', '0');
        });
    }

    function increseHitCount(selectId) {
        var increseHitCountUrl = "/helpdesk/increseHitCount?id=" + selectId;
        $http.get(increseHitCountUrl).success(function (data, status, headers, config) {
            //console.log(data);
        });
    }

    function deleteQna(selectId) {
        var deleteHelpDeskSelectionUrl = '/helpdesk/deleteSelection';

        $scope.form = {};
        $scope.form.updater = userSvc.getUser().fullname;
        $scope.form.updaterId = userSvc.getUser().username;
        $scope.form.updateIp = userSvc.getUser().ip;
        $scope.form.id = selectId;

        $http.post(deleteHelpDeskSelectionUrl, $scope.form).success(function (data, status, headers, config) {
            //console.log(data);
        });
    }

    function replyCompletionYes(selectId) {
        var tmpUrl = '/helpdesk/qna/updateReplyCompletion';

        $scope.form = {};
        $scope.form.id = selectId;
        $http.post(tmpUrl, $scope.form).success(function (data, status, headers, config) {
            //console.log(data);
        });
    }
};
