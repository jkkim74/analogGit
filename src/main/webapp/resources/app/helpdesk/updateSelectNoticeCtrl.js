/**
 * 공지사항 수정 Ctrl
 */
var updateSelectNoticeCtrl = function ($scope, $http, userSvc, $modalInstance, noticeList, selectId, curIndex, categoryList, FileUploader, helpDeskSvc) {
    $scope.categoryList = categoryList;

    $scope.modalInit = function () {
        helpDeskSvc.bindingDatePicker();
        $scope.selectNotice = noticeList[curIndex];
        $scope.curFileCount = 5;

        $scope.temp = {
            popupStartDate : DateHelper.stringToYmdStr($scope.selectNotice.popupStartDate,'.'),
            popupEndDate : DateHelper.stringToYmdStr($scope.selectNotice.popupEndDate,'.')
        };
        $scope.form = {
            category : $scope.selectNotice.category,
            title : $scope.selectNotice.title,
            context : $scope.selectNotice.context,
            popupYn: $scope.selectNotice.popupYn,
            attachFiles : [],
            popupStartDate: '',
            popupEndDate:''
        };

        $scope.summernoteConfig = {
            height: "269px",
            focus: true,
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['height', ['height']]
            ]
        };

        var uploader = $scope.uploader = new FileUploader({
            url: '/file',
            queueLimit: $scope.curFileCount
        });

        uploader.filters.push({
            name: 'customFilter',
            fn: function () {
                return this.queue.length < $scope.curFileCount;
            }
        });

        loadFileList($scope.selectNotice.id);
    };

    $scope.change = function () {
//        //console.log($scope.form.title);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.save = function () {
        if ($scope.form.category == '' || $scope.form.category == null) {
            alert("분류 선택을 확인하세요.");
            return;
        }

        if (!helpDeskSvc.checkTitleByte($scope.form.title)) {
            alert("제목은 100byte가 넘을수 없습니다.");
            return;
        }

        if ($scope.form.title.length == 0 || $scope.form.context.length == 0) {
            alert("제목/내용을 확인하세요.");
            return;
        }
        updateHelpDeskNotice();
    };

    //기존파일 삭제여부
    $scope.deleteOldFile = function (id) {
        if (confirm('파일을 삭제하시겠습니까?')) {
            var deleteFileUrl = '/file/deleteFile';
            var tmpDeleteFileList = [];
            tmpDeleteFileList.push(id);
            $http.post(deleteFileUrl, tmpDeleteFileList).success(function () {
                loadFileList($scope.selectNotice.id);
            });
        }
    };

    function loadFileList(selectId) {
        var noticeSelectFilesUrl = "/file/getMetaFileLists?containerName=helpdesk&contentId=" + selectId;
        $http.get(noticeSelectFilesUrl).success(function (data) {
            $scope.selectNoticeFiles = data;
            $scope.curFileCount = (5 - $scope.selectNoticeFiles.length);
        });
    }

    /**
     * 공지사항 update
     */
    function updateHelpDeskNotice() {
        var updateHelpDeskNoticeUrl = '/helpdesk/notice/update';
        $scope.form.updater = userSvc.getUser().fullname;
        $scope.form.updaterId = userSvc.getUser().username;
        $scope.form.updateIp = userSvc.getUser().ip;
        $scope.form.id = selectId;
        $scope.form.popupStartDate = DateHelper.dateToParamString($scope.temp.popupStartDate);
        $scope.form.popupEndDate = DateHelper.dateToParamString($scope.temp.popupEndDate);

        $http.post(updateHelpDeskNoticeUrl, $scope.form).success(function (data) {
            if (data.code === '0000') {
                if ($scope.uploader.queue.length > 0) {
                    for (var i in $scope.uploader.queue) {
                        $scope.uploader.queue[i].formData = [
                            {"containerName": "helpdesk"},
                            {"contentId": data.contentId},
                            {"createId": $scope.form.updaterId}
                        ];
                    }
                    $scope.uploader.uploadAll();
                }
            }
            $modalInstance.close('update complete');
        });
    }
};
