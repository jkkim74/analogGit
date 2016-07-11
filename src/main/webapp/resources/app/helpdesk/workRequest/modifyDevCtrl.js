function modifyDevCtrl($scope, $http, userSvc, helpDeskSvc, FileUploader, HELPDESK_WORK_REQUEST_TYPES) {

    function loadCode() {
        helpDeskSvc.getIssueTypes().then(function (data) {
            $scope.issueTypeList = data;
        });
        helpDeskSvc.getComponentTypes().then(function (data) {
            $scope.componentTypeList = data;
        });
    }

    function resetForm() {
        $scope.datePickerMinDate = new Date();
        $scope.btnState = false;
        $scope.form = {
            issueType: HELPDESK_WORK_REQUEST_TYPES.MODIFY,
            component: null,
            summary: null,
            description: null,
            dueDate: null,
            applicant: userSvc.getUser().username,  //userSvc.getUser().fullname
            approval: null,
            reference: null,
            attachFiles: null
        };
        getApprovalPerson();
        clearReferencePersonAutoComplete();
        $scope.uploader.clearQueue();
    }

    function deleteWasSavedFile(){
        var jiraApiUrl = '/jiraApi/deleteWas';
        $http.post(jiraApiUrl, $scope.form).success(function (data, status, headers, config) {
            resetForm();  //등록 확인 후 reset
        });
    }

    function clearReferencePersonAutoComplete(){
        $('li.token-input-token-voyager').remove();
    }

    function setData() {
        //Change date format Date() -> yyyy-mm-dd
        if ($scope.form.dueDate != null) {
            var tmpDate = $scope.form.dueDate;
            $scope.form.dueDate = tmpDate.ymd('-');
        }
        //Set reference parameter format
        if($scope.form.reference != null){
            var tmpStr = $scope.form.reference;
            var tmpList = tmpStr.split(',');
            $scope.form.reference = tmpList;
        }
        //Set attachFiles
        if($scope.uploader.queue.length > 0){
            //console.log('File is exist.....' + $scope.uploader.queue.length);
            var fileList = $scope.uploader.queue;
            var fileListLength = $scope.uploader.queue.length;
            var tmpList = [];

            for(var i=0; i<fileListLength; i++){
                //console.log(fileList[i].file.name);
                tmpList.push(fileList[i].file.name);
            }

            //console.log(tmpList);
            $scope.form.attachFiles = tmpList;
        }
    }

    function getApprovalPerson() {
        var getUrl = '/helpdesk/getApproval?searchId=' + userSvc.getUser().username;
        $http.get(getUrl).success(function (data, status, headers, config) {
            if (data != null && data.length > 0) {
                var approvalInfo = data[0];
                $scope.form.approval = approvalInfo.loginId;
                //$scope.form.approval = '1000714';//이욱환M
                //$scope.form.approval = 'mimul';
            } else {
                //TODO 결재자가 없는경우 jira전송 안되도록 설정 or 디폴트 결재자 선택필요(현, 전송 막음)
                alert('필수 입력 항목인 결재자 정보를 알 수 없어 JIRA로 데이터를 전송할 수 없습니다.\n관리자에게 문의하세요.');
                $scope.form.approval = '결재자 정보를 알 수 없습니다!';
                $scope.btnState = true;
                $scope.approvalColor = 'red';
            }
        });
    }

    function getReferencePerson(searchName) {
        var getUrl = '/helpdesk/getReference?searchName=' + searchName;
        $http.get(getUrl).success(function (data, status, headers, config) {
            $("#reference").tokenInput(data,
                {
                    onResult: function (results) {
                        $.each(results, function (index, value) {
                            //value.name = "OMG: " + value.name;
                        });
                        return results;
                    },
                    onAdd: function (item) {
                        //alert("Added " + item.name);
                    },
                    onDelete: function (item) {
                        //alert("Deleted " + item.name);
                    }
                }
            );
        });
    }

    function sendJiraApi(){
        if (confirm('JIRA에 등록하시겠습니까?')) {
            setData();

            //console.log('send form......');
            //console.log($scope.form);

            var jiraApiUrl = '/jiraApi';
            $http.post(jiraApiUrl, $scope.form).success(function (data, status, headers, config) {
                if (data != null && status == '200') {
                    alert("JIRA 전송 등록이 정상적으로 이루어졌습니다.\nhttp://jira.skplanet.com/browse/" + data.key + "에서 확인 가능합니다.");
                    deleteWasSavedFile();//was에 임시저장된 파일삭제
                    //resetForm();  //등록 확인 후 reset
                }
            });
        } else {
            //등록취소
        }
    }

    function setFileUpload() {
        var uploader = $scope.uploader = new FileUploader({
            url: '/jiraApi/saveWas?userId=' + userSvc.getUser().username,
            queueLimit: 5
        });

        uploader.filters.push({
            name: 'customFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 5;
            }
        });

        uploader.onCompleteAll = function () {
            sendJiraApi();
        };
    }

    $scope.setColor = function (color) {
        if (angular.isUndefined(color)) {
            color = 'black';
        }
        return { 'color': color };
    };

    $scope.init = function () {
        loadCode();
        getReferencePerson('');
        setFileUpload();
        resetForm();
    };

    $scope.openDatePicker = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.datePickerOpened = true;
    };

    $scope.sendWorkRequestForm = function () {

        //setData();
        //console.log($scope.uploader);
        //console.log($scope.uploader.queue);
        //console.log($scope.form);

        //If when attach file is exist, after the fileupload callback onCompleteAll(). or not just call sendJiraApi();
        if ($scope.uploader.queue.length > 0) {
            $scope.uploader.uploadAll();
        }else{
            sendJiraApi();
        }
    };
}
