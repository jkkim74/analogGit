var readSelectFaqCtrl = function ($scope, $http, userSvc, $modalInstance, faqList, selectId, curIndex, menuSvc) {
    var returnValue = {
        work: "none",
        selectId: selectId,
        curIndex: curIndex
    };

    $scope.summernoteConfig = {
        height: "326",//"269px",
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
        //console.log("readSelectFaqCtrl");
        $scope.selectFaq = faqList[curIndex];

        setNavigator(curIndex);
        loadFileList($scope.selectFaq.id);
        increseHitCount($scope.selectFaq.id);
    };

    $scope.refreshSelectFaq = function (newIndex) {

        $scope.selectFaq = faqList[newIndex];

        returnValue.selectId = $scope.selectFaq.id;
        returnValue.curIndex = newIndex;

        setNavigator(newIndex);
        loadFileList($scope.selectFaq.id)
    };

    $scope.cancel = function () {
        returnValue.work = 'cancel';
        $modalInstance.dismiss(returnValue);
    };
    $scope.delete = function () {
        if (confirm('FAQ를 삭제하시겠습니까?')) {
            deleteFaq();
        }
    };
    $scope.update = function () {
        returnValue.work = 'update';
        $modalInstance.close(returnValue);
    };

    $scope.downloadFile = function (path) {
        alert("다운로드 준비중입니다...");
        //console.log("downloadFile....path : " + path);
    };

    $scope.checkAuthority = function () {
        return (menuSvc.getNavigationMenu().helpDeskAdmin == 'Y');
    };


    function setNavigator(curIndex) {
        $scope.preIndex = curIndex - 1;
        if ($scope.preIndex < 0) {
            $scope.preIndex++;
        }

        $scope.nextIndex = curIndex + 1;
        if ($scope.nextIndex > faqList.length - 1) {
            $scope.nextIndex--;
        }
    };


    function loadFileList(selectId) {
        var faqSelectFilesUrl = "/file/getMetaFileLists?containerName=helpdesk&contentId=" + selectId;
        $http.get(faqSelectFilesUrl).success(function (data, status, headers, config) {
            $scope.selectFaqFiles = data;
            //console.log($scope.selectFaqFiles);

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

    function deleteFaq() {
        //console.log("deleteFaq()");
        var deleteHelpDeskFaqUrl = '/helpdesk/deleteSelection';

        $scope.form = {};
        $scope.form.updater = userSvc.getUser().fullname;
        $scope.form.updaterId = userSvc.getUser().username;
        $scope.form.updateIp = userSvc.getUser().ip;
        $scope.form.id = selectId;

        //console.log($scope.form);

        $http.post(deleteHelpDeskFaqUrl, $scope.form).success(function (data, status, headers, config) {
            returnValue.work = 'delete';
            $modalInstance.close(returnValue);
        });
    }
};
