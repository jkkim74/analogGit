/**
 * common-controllers.js에서 main.index.html 전용 controller로 분리
 * cookatrice
 */
function mainIndexCtrl($rootScope, $scope, $http, $sce, $location) {

    $scope.init = function(){
        setDefaultValue();
        setMainPageNotice();
        callPopupYList();
        setLatestDss();
    };

    function setDefaultValue(){
        $scope.popupYList = [];
        $scope.popupYListLength = 0;
        $scope.mainNotice = {
            category: '',
            title: '등록된 공지사항이 없습니다.',
            context: '',
            popupOrder: "0"
        };
        $scope.mainDss = null;
    }

    function checkPopupCookie(popupIndex){
        var curPopup = $scope.popupYList[popupIndex];
        var curPopupCookie = $.cookie('voyager_popup_id_'+curPopup.id);

        return angular.isDefined(curPopupCookie);
    }

    function showPopup(popupIndex){
        if (popupIndex >= $scope.popupYListLength) {
            window.sweetAlert.close();
            return;
        }

        if(checkPopupCookie(popupIndex)){
            showPopup(++popupIndex);
            return;
        }

        var curPopup = $scope.popupYList[popupIndex];
        swal({
            title: curPopup.title,
            //text: removeHtml(curPopup.context),
            text: curPopup.context,
            html:true,
            showCancelButton: true,
            confirmButtonColor: '#AEDEF4',
            confirmButtonText: "확인",
            cancelButtonText: "그만보기",
            closeOnConfirm: false,
            closeOnCancel: false,
            allowEscapeKey:false,
            template : 'resources/sweetalert/custom-template/voyager-popup.html',
            noticeCategory : curPopup.category,
            customWidth : 510,
            customHeight : 510
        }, function (isConfirm) {
            if (isConfirm) {
                if(popupIndex === $scope.popupYListLength){
                    window.sweetAlert.close();
                }else{
                    showPopup(++popupIndex);
                }
            }else{
                var date = new Date();
                var hour = 24;   //cookie time setting 1min.
                date.setTime(date.getTime() + (hour * 60 * 60 * 1000));
                $.cookie('voyager_popup_id_'+curPopup.id, 'YES', {expires: date});
                if(popupIndex === $scope.popupYListLength){
                    window.sweetAlert.close();
                }else{
                    showPopup(++popupIndex);
                }
            }
        });
    }

    function setMainPageNotice() {
        var url = '/helpdesk/mainPage/notice';
        $http.get(url).success(function (data) {
            if (data.length > 0) {
                $scope.mainNotice = data[0];
                $scope.mainNotice.context = $scope.mainNotice.context;    //no remove html tags.
            }
        });
    }

    $scope.goDetailNotice = function () {
        $rootScope.showMainPageNotice = true;
        $location.url('/helpdesk/notice/notice');
    };

    $scope.popupMainPageNotice = function() {
        if($scope.mainNotice.context == '') {
            $location.url('/helpdesk/notice/notice');
            return;
        }

        swal({
            title: $scope.mainNotice.title,
            //text: removeHtml(curPopup.context),
            text: $scope.mainNotice.context,
            html:true,
            //showCancelButton: true,
            confirmButtonColor: '#AEDEF4',
            confirmButtonText: "확인",
            cancelButtonText: "그만보기",
            closeOnConfirm: false,
            closeOnCancel: false,
            allowEscapeKey:false,
            template : 'resources/sweetalert/custom-template/voyager-popup.html',
            noticeCategory : $scope.mainNotice.category,
            customWidth : 510,
            customHeight : 510
        }, function (isConfirm) {
            if (isConfirm) {
                window.sweetAlert.close();
            }else{
                window.sweetAlert.close();
            }
        });
    };

    function callPopupYList() {
        //var url = API_BASE_URL + '/helpdesk/popup/popupYList';
        var url = '/helpdesk/popup/popupYList';
        $http.get(url).success(function (data) {
            $scope.popupYList = data;
            $scope.popupYListLength = $scope.popupYList.length;
            showPopup(0);
        });
    }

    function setLatestDss(){
        //var url = API_BASE_URL + '/dss/getLatestDss';
        var url = '/dss/getLatestDss';
        $http.get(url).success(function (data) {
            $scope.mainDss = data;
        });
    }
}
