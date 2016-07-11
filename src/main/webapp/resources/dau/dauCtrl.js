function dauCtrl($scope, apiSvc, $window) {

    function initDataForm() {
        $scope.errorMessage = 'No matter...';

        $scope.manPointIndex = '00';

        $scope.yesterdayDauDataSet = {
            ocbAcmPerHour: null,
            syrupAcmPerHour: null,
            ocbAcmTotal: 0,
            syrupAcmTotal: 0,
            acmTotal:0
        };

        $scope.dauDataSet = {
            totalValidUserCount: 0,
            ocbValidUserCount: 0,
            syrupValidUserCount: 0,
            mobileShopperValidUserCount: 0
        };

        $scope.changeValue = {
            tOld:0,
            t:0,
            ocbOld : 0,
            ocb : 0,
            syrupOld : 0,
            syrup : 0,
            mobileShopperOld : 0,
            mobileShopper : 0
        };

        $scope.min30Data ={
            ocb : 0,
            syrup : 0
        };
    }

    function checkIsMin(curTime, checkMin){
        var curMin = curTime.substring(10, 12);
        return curMin == checkMin;
    }

    function makeTimeIndex(time){
        var tmpTime = (time * 1);
        if(tmpTime >= 30){
            tmpTime = tmpTime - 30;
        }else {
            tmpTime = tmpTime + 30;
        }
        return (tmpTime < 10 ? '0' : '') + tmpTime;
    }

    //The count calc about current min.
    function calcData(data, curTime){

        var CYCLE = 6; //sec
        var calCycle = 60 / CYCLE;
        var seedData = data;
        var curTimeIndex = makeTimeIndex(curTime.substring(10, 12));
        var afterTimeIndex = makeTimeIndex((curTime.substring(10, 12) * 1) + 1);

        //current time check xx:29 or xx:30 -> set xx:29 for change value
        if (checkIsMin(curTime, '29') || checkIsMin(curTime, '30')) {
            curTimeIndex = 58;
            afterTimeIndex = 59;
        }

        return Math.round(((seedData[afterTimeIndex] - seedData[curTimeIndex]) / calCycle) * ((Math.floor(new Date().getSeconds()/CYCLE))+1));
    }

    function loadFirstData(){
        var curTime = makeTimeIndex(DateHelper.getCurrentDateByPattern('mm'));

        $scope.dauDataSet.ocbValidUserCount = $scope.ocbUser[curTime];
        $scope.dauDataSet.syrupValidUserCount = $scope.syrupUser[curTime];
        $scope.dauDataSet.totalValidUserCount = $scope.dauDataSet.ocbValidUserCount + $scope.dauDataSet.syrupValidUserCount;
        $scope.changeValue.tOld = curTime.substring(10, 12);
    }

    function resetCurData() {

        var curTime = DateHelper.getCurrentDateByPattern('YYYYMMDDHHmm');
        var curTimeIndex = makeTimeIndex(DateHelper.getCurrentDateByPattern('mm'));

        $scope.changeValue.t = curTimeIndex;
        if($scope.changeValue.tOld == $scope.changeValue.t){
            $scope.changeValue.ocbOld = $scope.changeValue.ocb;
            $scope.changeValue.syrupOld = $scope.changeValue.syrup;
        }else{
            $scope.changeValue.ocbOld = 0;
            $scope.changeValue.syrupOld = 0;
            $scope.changeValue.mobileShopperOld = 0;
        }

        $scope.changeValue.tOld = $scope.changeValue.t;
        $scope.changeValue.ocb = calcData($scope.ocbUser, curTime);
        $scope.changeValue.syrup = calcData($scope.syrupUser, curTime);


        if (checkIsMin(curTime,'29')) {
            curTimeIndex = 59;
            $scope.min30Data.ocb = $scope.ocbUser[curTimeIndex] + $scope.changeValue.ocb;
            $scope.min30Data.syrup = $scope.syrupUser[curTimeIndex] + $scope.changeValue.syrup;
        }

        if(curTimeIndex != '00'){
            $scope.dauDataSet.ocbValidUserCount = $scope.ocbUser[curTimeIndex] + $scope.changeValue.ocb;
            $scope.dauDataSet.syrupValidUserCount = $scope.syrupUser[curTimeIndex] + $scope.changeValue.syrup;
            $scope.dauDataSet.totalValidUserCount = $scope.ocbUser[curTimeIndex] + $scope.syrupUser[curTimeIndex]
                                                    + $scope.changeValue.ocbOld + $scope.changeValue.syrupOld;
        }else{
            $scope.dauDataSet.ocbValidUserCount = $scope.min30Data.ocb + $scope.changeValue.ocb;
            $scope.dauDataSet.syrupValidUserCount = $scope.min30Data.syrup + $scope.changeValue.syrup;
            $scope.dauDataSet.totalValidUserCount = $scope.min30Data.ocb + $scope.min30Data.syrup
                                                    + $scope.changeValue.ocbOld + $scope.changeValue.syrupOld;
        }
    }

    function calcCurPointRate(time){

        time = (time < 2 ? 2 : time); //set min time. for calc rawTime.(defalut 2)

        var rawTime = ((time - 2) < 10 ? '0' : '') + (time-2);
        var diffTime = ((time - 1) < 10 ? '0' : '') + (time-1);
        var ocbAcm = $scope.yesterdayDauDataSet.ocbAcmPerHour[rawTime]
                    + ($scope.yesterdayDauDataSet.ocbAcmPerHour[diffTime] - $scope.yesterdayDauDataSet.ocbAcmPerHour[rawTime]) / 2;

        var syrupAcm = $scope.yesterdayDauDataSet.syrupAcmPerHour[rawTime]
                    + ($scope.yesterdayDauDataSet.syrupAcmPerHour[diffTime] - $scope.yesterdayDauDataSet.syrupAcmPerHour[rawTime]) / 2;

        var curAcm = ocbAcm + syrupAcm;
        var yesterdayAcm = $scope.yesterdayDauDataSet.acmTotal;

        return (Math.round(curAcm / yesterdayAcm * 100)-5) + '%'; //-5 is to fix time image's center.
    }

    function pointManRate(){
        var curTime = DateHelper.getAfterMin(60,'HH');  //pointMan rate is after current hours.
        curTime *= 1;   //make '0X' -> 'X'

        $scope.manPointIndex = (curTime < 10  ? '0' : '') + curTime;

        if ($scope.manPointIndex === '00') {
            angular.element('#manPointIndex').css('left', '100%');
            $scope.manPointIndex = '23';
            return;
        }
        angular.element('#manPointIndex').css('left', calcCurPointRate($scope.manPointIndex));
    }

    function makeYesterdayDauData(){

        var ocbAcmPerHour = {};
        var syrupAcmPerHour = {};

        (24).times(function (i) {
            var mm = (i < 10 ? '0' : '') + i;
            ocbAcmPerHour[mm] = ($scope.dauData.ocbs[i].hauAcm != null ? $scope.dauData.ocbs[i].hauAcm : $scope.dauData.ocbs[i].fct);
            syrupAcmPerHour[mm] = $scope.dauData.syrups[i].hauAcm;
        });

        $scope.yesterdayDauDataSet = {
            ocbAcmPerHour: ocbAcmPerHour,
            syrupAcmPerHour: syrupAcmPerHour,
            ocbAcmTotal: ocbAcmPerHour[23],
            syrupAcmTotal: syrupAcmPerHour[23],
            acmTotal : ocbAcmPerHour[23]+syrupAcmPerHour[23]
        };
    }

    function loadData(loadFirstTime) {

        apiSvc.getCommonApi('/ocb/customer/dau').then(function (result) {
            if (result && result.dau) {
                $scope.dauData = result;
                // OCB 실제값과 예측치값으로 분단위로 나눔
                var ocbSlice = CalcHelper.sliceByMinutes($scope.dauData.dau.ocbActiveUserCnt, $scope.dauData.dau.expectedOcbActiveUserCnt);
                // Syrup 실제값과 예측치값으로 분단위로 나눔
                var syrupSlice = CalcHelper.sliceByMinutes($scope.dauData.dau.syrupActiveUserCnt, $scope.dauData.dau.expectedSyrupActiveUserCnt);

                getExpectedData(ocbSlice, syrupSlice);

                if(loadFirstTime){
                    loadFirstData();
                }

                makeYesterdayDauData();

                if (loadFirstTime) {
                    $scope.$safeApply(function () {
                        pointManRate();
                    });
                }

            } else {
                $scope.$safeApply(function () {
                    $scope.errorMessage = 'Some problem occured to loading DAU data. (' + DateHelper.getCurrentDateByPattern('YYYY.MM.DD HH:mm:ss')+')';
                    //console.log('Some problem occured to loading DAU data. (' + DateHelper.getCurrentDateByPattern('YYYY.MM.DD HH:mm:ss')+')');
                    //$( '#sendDauError' ).click();
                });
            }
        });
    }

    function doBoxAnimation(curBox, boxAnimateTime){
        var boxList = ['box3', 'box4'];
        var boxLength = boxList.length;

        for(var i=0; i<boxLength; i++){
            if(boxList[i] == curBox){
                $('.'+boxList[i]).animate({width: "780px"}, boxAnimateTime);
                $('.'+boxList[i]+' h3').animate({fontSize: "40px"}, boxAnimateTime);
                $('.'+boxList[i]+' span').animate({fontSize: "60px"}, boxAnimateTime);
            }else{
                $('.'+boxList[i]).animate({width: "390px"}, boxAnimateTime);
                $('.'+boxList[i]+' h3').animate({fontSize: "33px"}, boxAnimateTime);
                $('.'+boxList[i]+' span').animate({fontSize: "50px"}, boxAnimateTime);
            }
        }
    }

    function getManRate() {
        var curAcm = $scope.dauDataSet.totalValidUserCount;
        var yesterdayAcm = $scope.yesterdayDauDataSet.acmTotal;

        return (Math.round(curAcm / yesterdayAcm * 100) > 100 ? 100 : Math.round(curAcm / yesterdayAcm * 100)) + '%';
    }

    function refreshDataBox() {

        var boxAnimateTime = 1000;

        if(!isNaN($scope.changeValue.ocb) && !isNaN($scope.changeValue.ocbOld)){

            doBoxAnimation('box3', boxAnimateTime);

            setTimeout(function(){
                var ocbDiff = $scope.changeValue.ocb - $scope.changeValue.ocbOld;

                magic_number("#curOcbValidUser", $scope.dauDataSet.ocbValidUserCount);
                $scope.dauDataSet.totalValidUserCount += ocbDiff;
                magic_number("#curTotalValidUser", $scope.dauDataSet.totalValidUserCount);

                angular.element('.man .bar').css('width',getManRate());

            },boxAnimateTime);
        }

        setTimeout(function(){
            if(!isNaN($scope.changeValue.syrup) && !isNaN($scope.changeValue.syrupOld)) {

                doBoxAnimation('box4', boxAnimateTime);

                setTimeout(function(){

                    var syrupDiff = $scope.changeValue.syrup - $scope.changeValue.syrupOld;

                    magic_number("#curSyrupValidUser", $scope.dauDataSet.syrupValidUserCount);
                    $scope.dauDataSet.totalValidUserCount += syrupDiff;
                    magic_number("#curTotalValidUser", $scope.dauDataSet.totalValidUserCount);

                },boxAnimateTime);
            }
        },3000);
    }

    // 예상치 데이터를 분단위로 슬라이싱 처리함.
    function getExpectedData(ocbSlice, syrupSlice) {
        $scope.ocbUser = {};
        $scope.syrupUser = {};
        $scope.shopperUser = {};
        var ocbActiveUserCnt = parseFloat($scope.dauData.dau.ocbActiveUserCnt);
        var syrupActiveUserCnt = parseFloat($scope.dauData.dau.syrupActiveUserCnt);

        (60).times(function (i) {
            var mm = (i < 10 ? '0' : '') + i;
            // OCB 유효 고객수 분단위 예측치값
            $scope.ocbUser[mm] = Math.round(ocbActiveUserCnt);
            // Syrup 유효 고객수 분단위 예측치값
            $scope.syrupUser[mm] = Math.round(syrupActiveUserCnt);
            ocbActiveUserCnt += parseFloat(ocbSlice);
            syrupActiveUserCnt += parseFloat(syrupSlice);
        });
    }

    function funCron() {
        var cron = new Cron();

        cron.add(new Cron.Job("*/6 * * * * *", function () {
            resetCurData();
            refreshDataBox();
            syncDate();
        }));

        cron.add(new Cron.Job("0 30 * * * *", function () {
            loadData(false);
        }));

        cron.add(new Cron.Job("0 0 */1 * * *", function () {
            $scope.$safeApply(function () {
                pointManRate();
            });
        }));

        cron.start();
    }

    function centerDauWindow(){
        var curHeight = angular.element($window).height();
        var avgMargin = (curHeight-720)/2;
        if(avgMargin > 0){
            angular.element('.realTime').css('margin-top',avgMargin);
        }
    }

    $scope.dauInit = function () {
        syncDate();
        syncClock();
        initDataForm();
        loadData(true); //if load first time like init. set true.
        funCron();

        //for ipad and other resolution
        centerDauWindow();
    };

    function syncDate() {
        $scope.toDay = DateHelper.getCurrentDateByPattern('YYYY.MM.DD');
        $scope.yesterDay = DateHelper.getPreviousDate(1, 'YYYY.MM.DD');
    }

    function syncClock() {
        $("#clock1").DigitalClock({
            timeFormat: '{HH}:{MM}'
        });
    }

    var counts = {};

    function format_number(text) {
        return text.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
    }

    function magic_number(element_name, value) {
        var elem = $(element_name);
        var current = counts[element_name] || 0;
        $({count: current}).animate({count: value}, {
            duration: 500,
            step: function () {
                elem.text(format_number(String(parseInt(this.count))));
            },
            complete: function () {
                elem.text(format_number(String(parseInt(this.count))));
            }
        });
        counts[element_name] = value;
    }
}
