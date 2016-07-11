function popupManagerCtrl($scope, adminSvc, $http, FileUploader, userSvc) {
    $(document).ready(function () {
        //use only number
        $(".popupOnlyNum").keyup(function () {
            $(this).val($(this).val().replace(/[^0-9]/g, ""));
        });

        //TODO, Why local function can't call? If you have enough time thinking more and refactoring.
        window.quickClosePopup = function(selectId){
            quickClosePopup(selectId);
        };

    });

    $scope.init = function () {
        $scope.dateFlag = false;

        callPopupList();
        popupDetailInit();
    };

    function callPopupList() {
        $scope.pageState = {
            'loading': true,
            'emptyData': true
        };
        var url = '/helpdesk/popup/popupList';

        $scope.tableId = 'popupListTableForGrid';
        adminSvc.getAdminApi(url).then(function(result) {
            var dataLength = result.length;
            if (dataLength != 0) {
                gridPopupList(result);
            } else {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = true;
            }
        });
    }

    function loadCategoryList() {
        var url = "/commonGroupCode/getCodes?groupCodeId=HD_NOTICE_TYPE";
        $http.get(url).success(function (data) {
            $scope.categoryList = data;
        });

        url = "/commonGroupCode/getCodes?groupCodeId=POPUP_USE";
        $http.get(url).success(function (data) {
            $scope.popupUseList = data;
        });
    }

    function clearPopup(){
        $scope.curFileCount = 5;

        $scope.temp = {
            popupStartDate : moment(new Date()).format('YYYY.MM.DD'),
            popupEndDate : '9999.12.31'
        };
        $scope.form = {
            newItem: true,
            title: '',
            context: '',
            category: '',
            popupStartDate: '',
            popupEndDate: '',
            popupYn: '',
            popupOrder: null
        };

        $scope.uploader.clearQueue();
    }

    function popupDetailInit() {
        adminSvc.bindingDatePicker();

        $scope.summernoteConfig = {
            height: "180px",
            focus: true,
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['height', ['height']]
            ]
        };

        $scope.curFileCount = 5;
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

        uploader.onCompleteItem = function (fileItem, response) {
            if (response.code !== '0000') {
                uploader.failToUploadFile = true;
                return false;
            }
        };

        uploader.onCompleteAll = function () {
            if (uploader.failToUploadFile) {
                alert('파일 업로드에 문제가 발생해 등록에 실패했습니다. 관리자에게 문의해주세요.');
            } else {

                if($scope.form.newItem){
                    alert('정상적으로 등록되었습니다.');
                }else{
                    alert('정상적으로 수정되었습니다.');
                }
                //uploader.clearQueue();
                clearPopup();
                callPopupList();
            }
        };

        clearPopup();
        loadCategoryList();
    }

    //기존 등록파일 삭제 확인
    $scope.deleteOldFile = function (id) {
        if (confirm('파일을 삭제하시겠습니까?')) {
            var deleteFileUrl = '/file/deleteFile';
            var tmpDeleteFileList = [];
            tmpDeleteFileList.push(id);
            $http.post(deleteFileUrl, tmpDeleteFileList).success(function () {
                loadFileList($scope.form.id);
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


    function gridPopupList(datas) {
        $scope.popupListForGridConfig = {
            data : datas,
            datatype: "local",
            height: "100%",
            shrinkToFit: true,
            rowNum: 10000,
            colNames: ['No','id', '구분', '제목', '등록일자', '게시시작', '게시종료', '활성화상태'],
            colModel: [
                {name: 'rnum', index: 'svcNm', editable: false, width: 40, align: "center", sortable: false},
                {name: 'id', index: 'id', editable: false, width: 40, align: "center", sortable: false, hidden:true},
                {name: 'category', index: 'category', editable: false, width: 80, align: "center", sortable: false},
                {name: 'title', index: 'title', editable: false, width: 200, align: "center", sortable: false},
                {name: 'writeDate', index: 'writeDate', editable: false, width: 80, align: "center", sortable: false},
                {name: 'popupStartDate', index: 'popupStartDate', editable: false, width: 80, align: "center", sortable: false},
                {name: 'popupEndDate', index: 'popupEndDate', editable: false, width: 80, align: "center", sortable: false},
                {name: 'popupYn', index: 'popupYn', editable: false, width: 80, align: "center", sortable: false, formatter: displayButtons}
            ],
            gridview: true,
            multiselect: false,
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            scroll: true,
            jsonReader: {
                root: "rows"
            },
            gridComplete: function () {
                $scope.pageState.loading = false;
                $scope.pageState.emptyData = false;
            },
            onSelectRow: function (rowid) {
                var curSelection = $(this).jqGrid("getRowData", rowid);
                selectOnePopup(curSelection);
            }
        };
        var $table = angular.element('<table id="' + $scope.tableId + '" />');
        angular.element("#popupListForGrid").html($table);
        angular.element("#" + $scope.tableId).jqGrid("GridUnload");
        angular.element("#" + $scope.tableId).jqGrid($scope.popupListForGridConfig);

        angular.element("#popupListForGrid .ui-jqgrid-bdiv div").first().height('100%');
    }

    function displayButtons(cellvalue, options, rowObject) {
        //TODO, Why local function can't call? If you have enough time thinking more and refactoring.
        if (rowObject.popupYn === 'Y') {
            return '<input type="button" value="바로내리기" onclick=\"quickClosePopup(' + rowObject.id + ');\" />';
        }
        return rowObject.popupYn;
    }

    function selectOnePopup(popupInfo){
        loadFileList(popupInfo.id);

        var url = '/helpdesk/popup/selectedPopupInfo?id='+popupInfo.id;
        $http.get(url).success(function (data) {
            var loadData = data[0];
            if(loadData){
                $scope.form.newItem = false;
                $scope.form.id = loadData.id;
                $scope.form.title = loadData.title;
                $scope.form.context = loadData.context;
                $scope.form.category = loadData.category;
                $scope.form.popupYn = loadData.popupYn;
                $scope.form.popupOrder = loadData.popupOrder;

                $scope.temp.popupStartDate = loadData.popupStartDate;
                $scope.temp.popupEndDate = loadData.popupEndDate;
            }
        });
    }

    function quickClosePopup(selectId) {
        if(confirm('공지사항을 내리시겠습니까?')){
            var tempForm = {
                updater: userSvc.getUser().fullname,
                updaterId: userSvc.getUser().username,
                updateIp: userSvc.getUser().ip,
                id: selectId
            };

            var quickCloseUrl = '/helpdesk/popup/quickClose';
            $http.post(quickCloseUrl, tempForm).success(function (data) {
                if(data > 0){
                    clearPopup();
                    callPopupList();
                    alert('정상적으로 활성화가 변경되었습니다.');
                }
            });
        }
    }

    /**
     * 검색 callback
     * @param result
     */
    $scope.search = function(result) {
        callPopupList();
    };

    $scope.initPopup = function(){
        clearPopup();
    };

    $scope.insertUpdatePopup = function(){
        if ($scope.form.category == '' || $scope.form.category == null) {
            alert("공지 구분을 선택하세요.");
            return;
        }
        if ($scope.form.category != 'important' && ($scope.form.popupYn == '' || $scope.form.popupYn == null)) {
            alert("활성화 상태를 확인하세요.");
            return;
        }
        if ($scope.form.title.length == 0 || $scope.form.context.length == 0) {
            alert("제목/내용을 확인하세요...");
            return;
        }

        doInsertUpdatePopup();
    };

    $scope.deletePopup = function(){
        if (confirm('공지사항을 삭제하시겠습니까?')) {
            doDeletePopup();
        }
    };

    function doDeletePopup() {
        var deleteForm = {
            updater: userSvc.getUser().fullname,
            updaterId: userSvc.getUser().username,
            updateIp: userSvc.getUser().ip,
            id: $scope.form.id
        };

        var deleteUrl = '/helpdesk/deleteSelection';
        $http.post(deleteUrl, deleteForm).success(function (data) {
            if(data > 0){
                clearPopup();
                callPopupList();
                alert('정상적으로 삭제되었습니다.');
            }
        });
    }

    function doInsertUpdatePopup() {
        var tmpUrl = '';

        if($scope.form.newItem){
            tmpUrl = '/helpdesk/notice/insert';
            $scope.form.writer = userSvc.getUser().fullname;
            $scope.form.writerId = userSvc.getUser().username;
            $scope.form.writeIp = userSvc.getUser().ip;
        }else{
            tmpUrl = '/helpdesk/notice/update';
            $scope.form.updater = userSvc.getUser().fullname;
            $scope.form.updaterId = userSvc.getUser().username;
            $scope.form.updateIp = userSvc.getUser().ip;
        }
        $scope.form.popupStartDate = DateHelper.dateToParamString($scope.temp.popupStartDate);
        $scope.form.popupEndDate = DateHelper.dateToParamString($scope.temp.popupEndDate);
        if($scope.form.popupYn ==='' || $scope.form.popupYn ==='N'){
            $scope.form.popupYn = 'N';
            $scope.form.popupOrder = null;
        }

        $http.post(tmpUrl, $scope.form).success(function (data) {
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
                }else{
                    clearPopup();
                    callPopupList();
                }
            } else {
                alert(data.errMessage);
            }
        });
    }
}
