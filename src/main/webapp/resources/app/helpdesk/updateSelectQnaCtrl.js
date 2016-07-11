var updateSelectQnaCtrl = function ($scope, $http, userSvc, $modalInstance, selectItem, categoryList, FileUploader) {
    $scope.categoryList = categoryList;


    $scope.modalInit = function () {
        $scope.selectQna = selectItem;
        $scope.curFileCount = 5;

        $scope.form = {};
        $scope.form.category = $scope.selectQna.category;
        $scope.form.title = $scope.selectQna.title;
        $scope.form.context = $scope.selectQna.context;
        $scope.form.attachFiles = [];

        $scope.summernoteConfig = {
            height: "269px",
            focus: true,
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
//            ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']]
            ]
        };

        var uploader = $scope.uploader = new FileUploader({
            url: '/file',
            queueLimit: $scope.curFileCount
        });

        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < $scope.curFileCount;
            }
        });

        loadFileList($scope.selectQna.id);
    };

    $scope.change = function () {
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.save = function () {
        if ($scope.form.title.length == 0 || $scope.form.context.length == 0) {
            alert("제목/내용을 확인하세요...");
            return;
        }
        updateHelpDeskQna();
    };

    //기존파일 삭제여부
    $scope.deleteOldFile = function (id) {
        if (confirm('파일을 삭제하시겠습니까?')) {
            var deleteFileUrl = '/file/deleteFile';
            var tmpDeleteFileList = [];
            tmpDeleteFileList.push(id);
            $http.post(deleteFileUrl, tmpDeleteFileList).success(function (data, status, headers, config) {

                loadFileList($scope.selectQna.id);
            });
        }
    };

    function loadFileList(selectId) {
        var qnaSelectFilesUrl = "/file/getMetaFileLists?containerName=helpdesk&contentId=" + selectId;
        $http.get(qnaSelectFilesUrl).success(function (data, status, headers, config) {
            $scope.selectQnaFiles = data;
            $scope.curFileCount = (5 - $scope.selectQnaFiles.length);
        });
    }

    function updateHelpDeskQna() {
        var updateHelpDeskQnaUrl = '/helpdesk/qna/update';

        $scope.form.updater = userSvc.getUser().fullname;
        $scope.form.updaterId = userSvc.getUser().username;
        $scope.form.updateIp = userSvc.getUser().ip;
        $scope.form.id = selectItem.id;

        $http.post(updateHelpDeskQnaUrl, $scope.form).success(function (data, status, headers, config) {
            if (data.code === '0000') {
                if ($scope.uploader.queue.length > 0) {
                    for (var i in $scope.uploader.queue) {
                        $scope.uploader.queue[i].formData = [
                            {"containerName": "helpdesk"},
                            {"contentId": data.contentId},
                            {"createId": $scope.form.updaterId}
                        ];
                    }
//                    console.log($scope.uploader.queue);
                    $scope.uploader.uploadAll();
                }
            }
            $modalInstance.close('update complete');
        });
    }

};
