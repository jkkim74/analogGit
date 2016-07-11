/**
 * 공지사항 등록 Ctrl
 */
var addNewNoticeCtrl = function ($scope, $http, userSvc, $modalInstance, categoryList, FileUploader, helpDeskSvc) {
    $scope.categoryList = categoryList;

    // 공지사항 등록 모달창 초기화 함수.
    $scope.modalInit = function () {
        helpDeskSvc.bindingDatePicker();
        $scope.temp = {
            popupStartDate : moment(new Date()).format('YYYY.MM.DD'),
            popupEndDate : '9999.12.31'
        };
        $scope.form = {
            newItem: true,
            title: '',
            context: '',
            popupYn: 'N',
            category: '',
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
            queueLimit: 5
        });

        uploader.filters.push({
            name: 'customFilter',
            fn: function () {
                return this.queue.length < 5;
            }
        });
    };

    // 모달창 취소나 종료 버튼 이벤트 처리.
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    // 공지사항 저장 처리.
    $scope.save = function () {
        if ($scope.form.category == '') {
            alert("분류 선택을 확인하세요...");
            return;
        }

        if(!helpDeskSvc.checkTitleByte($scope.form.title)){
            alert("제목은 100byte가 넘을수 없습니다.");
            return;
        }

        if ($scope.form.title.length == 0 || $scope.form.context.length == 0) {
            alert("제목/내용을 확인하세요...");
            return;
        }
        insertHelpDeskNotice();
    };

    /**
     * 공지사항 저장
     */
    function insertHelpDeskNotice() {
        var insertHelpDeskNoticeUrl = '/helpdesk/notice/insert';
        $scope.form.writer = userSvc.getUser().fullname;
        $scope.form.writerId = userSvc.getUser().username;
        $scope.form.writeIp = userSvc.getUser().ip;
        $scope.form.popupStartDate = DateHelper.dateToParamString($scope.temp.popupStartDate);
        $scope.form.popupEndDate = DateHelper.dateToParamString($scope.temp.popupEndDate);

        $http.post(insertHelpDeskNoticeUrl, $scope.form).success(function (data) {
            if (data.code === '0000') {
                if ($scope.uploader.queue.length > 0) {
                    for (var i in $scope.uploader.queue) {
                        $scope.uploader.queue[i].formData = [
                            {"containerName": "helpdesk"},
                            {"contentId": data.contentId},
                            {"createId": $scope.form.writerId}
                        ];
                    }
                    $scope.uploader.uploadAll();
                }
            } else {
                alert(data.errMessage);
            }
            $modalInstance.close('save');
        });
    }
};
