var updateSelectFaqCtrl = function ($scope, $http, userSvc, $modalInstance, faqList, selectId, curIndex, FileUploader) {

    $scope.modalInit = function () {
        $scope.selectFaq = faqList[curIndex];
        $scope.curFileCount = 5;

        $scope.form = {};
        $scope.form.title = $scope.selectFaq.title;
        $scope.form.context = $scope.selectFaq.context;
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

        loadFileList($scope.selectFaq.id);
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

        updateHelpDeskFaq();
    };

    //기존파일 삭제여부
    $scope.deleteOldFile = function (id) {
        if (confirm('파일을 삭제하시겠습니까?')) {
            var deleteFileUrl = '/file/deleteFile';
            var tmpDeleteFileList = [];
            tmpDeleteFileList.push(id);
            $http.post(deleteFileUrl, tmpDeleteFileList).success(function (data, status, headers, config) {
                loadFileList($scope.selectFaq.id);
            });
        }
    };

    function loadFileList(selectId) {
        var faqSelectFilesUrl = "/file/getMetaFileLists?containerName=helpdesk&contentId=" + selectId;
        $http.get(faqSelectFilesUrl).success(function (data, status, headers, config) {
            $scope.selectFaqFiles = data;
            $scope.curFileCount = (5 - $scope.selectFaqFiles.length);
        });
    }

    function updateHelpDeskFaq() {
        var updateHelpDeskFaqUrl = '/helpdesk/faq/update';

        $scope.form.updater = userSvc.getUser().fullname;
        $scope.form.updaterId = userSvc.getUser().username;
        $scope.form.updateIp = userSvc.getUser().ip;
        $scope.form.id = selectId;

        $http.post(updateHelpDeskFaqUrl, $scope.form).success(function (data, status, headers, config) {
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
