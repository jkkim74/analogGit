/**
 * 선택한 공지사항 읽기
 */
var readSelectNoticeCtrl = function ($scope, $http, userSvc, $modalInstance, noticeList, categoryList, selectId, curIndex, checkAuthority) {
    $scope.checkAuth = checkAuthority;

    // 이전/이후 페이징 표시하기 위하 처리하는 함수.
    $scope.checkListSize = function(){
        var listLength = noticeList.length;
        if(listLength > 1){
            return true;
        }
        return false;
    };

    var returnValue = {
        work: "none",
        selectId: selectId,
        curIndex: curIndex
    };

    $scope.summernoteConfig = {
        height: "326",//"269px",
        welEditable: false,
        toolbar: [
        ]
    };

    // 공지사항 상세 조회 모달창 렌더링된 후 최초 실행되는 함수.
    $scope.modalInit = function () {
        $scope.selectNotice = noticeList[curIndex];
        $scope.popupDate = {
            start: DateHelper.stringToYmdStr($scope.selectNotice.popupStartDate, '.'),
            end: DateHelper.stringToYmdStr($scope.selectNotice.popupEndDate, '.')
        };

        setNavigator(curIndex);
        loadFileList($scope.selectNotice.id);
        increseHitCount($scope.selectNotice.id);
    };

    // 이전/이후 페이지 이동 처리.
    $scope.refreshSelectNotice = function (newIndex) {
        $scope.selectNotice = noticeList[newIndex];
        returnValue.selectId = $scope.selectNotice.id;
        returnValue.curIndex = newIndex;
        setNavigator(newIndex);
        loadFileList($scope.selectNotice.id);
    };

    // 공지사항 분류명 조회.
    $scope.getCodeName = function (code) {
        for (var i in categoryList) {
            if (code == categoryList[i].codeId) {
                return categoryList[i].codeName;
            }
        }
        return 'not matched category!';
    };

    // 모달창 닫기 이벤트 처리.
    $scope.cancel = function () {
        returnValue.work = 'cancel';
        $modalInstance.dismiss(returnValue);
    };

    // 공지사항 삭제 이벤트 처리.
    $scope.delete = function () {
        if (confirm('공지사항을 삭제하시겠습니까?')) {
            deleteNotice();
        }
    };

    // 공지사항 변경 화면으로 전환 처리.
    $scope.update = function () {
        returnValue.work = 'update';
        $modalInstance.close(returnValue);
    };

    // 페이지 네비게인션 인덱스 처리.
    function setNavigator(curIndex) {
        $scope.preIndex = curIndex - 1;
        if ($scope.preIndex < 0) {
            $scope.preIndex++;
        }

        $scope.nextIndex = curIndex + 1;
        if ($scope.nextIndex > noticeList.length - 1) {
            $scope.nextIndex--;
        }
    }

    /**
     * 첨부파일 목록
     */
    function loadFileList(selectId) {
        var noticeSelectFilesUrl = "/file/getMetaFileLists?containerName=helpdesk&contentId=" + selectId;
        $http.get(noticeSelectFilesUrl).success(function (data) {
            $scope.selectNoticeFiles = data;

            //CSS 이상 수정
            $('.note-editor .note-toolbar').remove();
            $('.note-editor .note-statusbar').remove();
            $('.note-editor .note-editable').attr('contenteditable', 'false');
            $('.note-editor').css('border', '0');
        });
    }

    /**
     * 조회수 증가
     */
    function increseHitCount(selectId) {
        var increseHitCountUrl = "/helpdesk/increseHitCount?id=" + selectId;
        $http.get(increseHitCountUrl).success(function () {
            //console.log(data);
        });
    }

    /**
     * 공지사항 삭제
     */
    function deleteNotice() {
        var deleteHelpDeskSelectionUrl = '/helpdesk/deleteSelection';
        $scope.form = {};
        $scope.form.updater = userSvc.getUser().fullname;
        $scope.form.updaterId = userSvc.getUser().username;
        $scope.form.updateIp = userSvc.getUser().ip;
        $scope.form.id = selectId;
        $http.post(deleteHelpDeskSelectionUrl, $scope.form).success(function () {
            returnValue.work = 'delete';
            $modalInstance.close(returnValue);

        });
    }
};
