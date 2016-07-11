function sparklineCtrl($scope, $http, API_BASE_URL) {
    $scope.myData = [5, 6, 7, 9, 9, 5, 3, 2, 2, 4, 6, 7];
    $scope.myData1 = [1, 2, 3, 6, 7, 7, 5, 5, 2, 2, 6, 1];
    $scope.myData2 = [5, 6, 7, 2, 0, -4, -2, 4];
    $scope.myData3 = [30, 30, 15];

    $("#sparkline").sparkline($scope.myData, {
        type: 'line',
        width: '300',
        height: '100',
        lineColor: '#ef1a5d',
        fillColor: '#fccfcf',
        lineWidth: 2,
        spotColor: '#aa610d',
        minSpotColor: '#7f0000',
        maxSpotColor: '#007f00',
        highlightSpotColor: '#666666',
        highlightLineColor: '#bf00bf',
        spotRadius: 5,
        chartRangeMin: 0,
        chartRangeMax: 10,
        chartRangeMinX: 0,
        chartRangeMaxX: 0,
        normalRangeMin: 5,
        normalRangeMax: 6,
        drawNormalOnTop: false});

    $("#sparkline").sparkline($scope.myData1, {
        composite: true,
        type: 'line',
        width: '300',
        height: '100',
        fillColor: false,
        lineColor: '#CB1BCB',
        lineWidth: 2,
        spotColor: '#aa610d',
        minSpotColor: '#7f0000',
        maxSpotColor: '#007f00',
        highlightSpotColor: '#666666',
        highlightLineColor: '#bf00bf',
        spotRadius: 5,
        chartRangeMin: 0,
        chartRangeMax: 10,
        chartRangeMinX: 0,
        chartRangeMaxX: 0,
        normalRangeMin: 5,
        normalRangeMax: 6,
        drawNormalOnTop: false});


    $("#sparkline2").sparkline($scope.myData2, {
        type: 'bar',
        height: '100',
        barWidth: 20,
        barSpacing: 5,
        barColor: '#4933cc',
        negBarColor: '#d33939',
        zeroColor: '#25d128'});

    $("#sparkline3").sparkline($scope.myData3, {
        type: 'pie',
        width: '100',
        height: '100',
        offset: NaN,
        borderWidth: 3,
        borderColor: '#b2b2b2'});


    $scope.init = function () {
        $scope.drawMouseSpeedDemo();
    }

    $scope.drawMouseSpeedDemo = function () {
        var mrefreshinterval = 500; // update display every 500ms
        var lastmousex = -1;
        var lastmousey = -1;
        var lastmousetime;
        var mousetravel = 0;
        var mpoints = [];
        var mpoints_max = 30;
        $('html').mousemove(function (e) {
            var mousex = e.pageX;
            var mousey = e.pageY;
            if (lastmousex > -1) {
                mousetravel += Math.max(Math.abs(mousex - lastmousex), Math.abs(mousey - lastmousey));
            }
            lastmousex = mousex;
            lastmousey = mousey;
        });
        var mdraw = function () {
            var md = new Date();
            var timenow = md.getTime();
            if (lastmousetime && lastmousetime != timenow) {
                var pps = Math.round(mousetravel / (timenow - lastmousetime) * 1000);
                mpoints.push(pps);
                if (mpoints.length > mpoints_max)
                    mpoints.splice(0, 1);
                mousetravel = 0;
                $('#mousespeed').sparkline(mpoints, { //width: mpoints.length * 2, tooltipSuffix: ' pixels per second' });
                    type: 'line',
                    width: '700',
                    height: '200',
                    lineColor: '#ef1a5d',
                    fillColor: '#fccfcf',
                    lineWidth: 2,
                    spotColor: '#aa610d',
                    minSpotColor: '#7f0000',
                    maxSpotColor: '#007f00',
                    highlightSpotColor: '#666666',
                    highlightLineColor: '#bf00bf',
                    spotRadius: 5,
                    chartRangeMin: 0,
                    chartRangeMax: 10,
                    chartRangeMinX: 0,
                    chartRangeMaxX: 0,
                    normalRangeMin: 5,
                    normalRangeMax: 6,
                    drawNormalOnTop: false});


            }
            lastmousetime = timenow;
            setTimeout(mdraw, mrefreshinterval);
        }
        // We could use setInterval instead, but I prefer to do it this way
        setTimeout(mdraw, mrefreshinterval);
    }

}
