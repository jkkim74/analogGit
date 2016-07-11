var addNewQnaCtrl = function ($scope, $http, userSvc, $modalInstance, categoryList, FileUploader) {

    $scope.categoryList = categoryList;

    $scope.modalInit = function () {

        $scope.form = {
            newItem: true,
            title: '',
            context: '',
            category: ''
        };

        $scope.summernoteConfig = {
            height: "269px",
            focus: true,
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']]
            ]
        };

        var uploader = $scope.uploader = new FileUploader({
            url: '/file',
            queueLimit: 5
        });

        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 5;
            }
        });
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.upload = function () {

        $scope.uploader.uploadAll();
    };

    $scope.save = function () {
        if ($scope.form.category == '') {
            alert("분류 선택을 확인하세요...");
            return;
        }

        if ($scope.form.title.length == 0 || $scope.form.context.length == 0) {
            alert("제목/내용을 확인하세요...");
            return;
        }

        insertHelpDeskQna();
    };


    /**
     * QnA 저장
     */
    function insertHelpDeskQna() {
        var insertHelpDeskQnaUrl = '/helpdesk/qna/insert';

        $scope.form.writer = userSvc.getUser().fullname;
        $scope.form.writerId = userSvc.getUser().username;
        $scope.form.writeIp = userSvc.getUser().ip;

        $http.post(insertHelpDeskQnaUrl, $scope.form).success(function (data, status, headers, config) {
            if (data.code === '0000') {
                if ($scope.uploader.queue.length > 0) {
                    for (var i in $scope.uploader.queue) {
                        $scope.uploader.queue[i].formData = [
                            {"containerName": "helpdesk"},
                            {"contentId": data.contentId},
                            {"createId": $scope.form.writerId}
                        ];
                    }
//                    console.log($scope.uploader.queue);
                    $scope.uploader.uploadAll();
                }
            }
            $modalInstance.close('save');
        });
    };
};
