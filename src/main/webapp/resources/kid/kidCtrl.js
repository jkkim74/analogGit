function kidCtrl($scope, apiSvc) {

    function syncDate() {
        var toDay = DateHelper.getCurrentDateByPattern('YYYY.MM.DD dddd');
        var toDay2 = DateHelper.getCurrentDateByPattern('YYYY.MM.DD dddd','ko');
        var toDay3 = DateHelper.getCurrentDateByPattern('YYYY.MM.DD dd','ko');
        var yesterDay = DateHelper.getPreviousDate(1, 'YYYY.MM.DD');
    }

    function changeView(){
        $('#changeView').click();
    }

    function funCron() {
        var cron = new Cron();
        cron.add(new Cron.Job("*/5 * * * * *", function () {
            changeView();
        }));
        cron.start();
    }

    $scope.kidInit = function () {
        syncDate();
        funCron();
    };

}
