/**
 * Created by cookatrice on 2014. 5. 23..
 */

function radarChartCtrl($scope, $http, API_BASE_URL) {

    $scope.init = function () {

        $scope.drawRadarChart();

    };

    $scope.drawRadarChart = function () {


    };
}

var w = 500, h = 500;
var new_w = 300, new_h = 300;
var colorscale = d3.scale.category10();
var curWindowWidth = $(window).width();
var curWindowHeight = $(window).height();
var LegendOptions = ['Type A', 'Type B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R'];

var d = [
    [
        {axis: "Email", value: 0.59},
        {axis: "Social Networks", value: 0.56},
        {axis: "Internet Banking", value: 0.42},
        {axis: "News Sportsites", value: 0.34},
        {axis: "Search Engine", value: 0.48},
//        {axis: "View Shopping sites", value: 0.14},
//        {axis: "Paying Online", value: 0.11},
//        {axis: "Buy Online", value: 0.05},
//        {axis: "Stream Music", value: 0.07},
//        {axis: "Online Gaming", value: 0.12},
//        {axis: "Navigation", value: 0.27},
//        {axis: "App connected to TV program", value: 0.03},
//        {axis: "Offline Gaming", value: 0.12},
//        {axis: "Photo Video", value: 0.4},
//        {axis: "Reading", value: 0.03},
//        {axis: "Listen Music", value: 0.22},
        {axis: "Watch TV", value: 0.03},
        {axis: "TV Movies Streaming", value: 0.03},
        {axis: "Listen Radio", value: 0.07},
        {axis: "Sending Money", value: 0.18},
        {axis: "Other", value: 0.07},
        {axis: "Use less Once week", value: 0.08}
    ],
    [
        {axis: "Email", value: 0.48},
        {axis: "Social Networks", value: 0.41},
        {axis: "Internet Banking", value: 0.27},
        {axis: "News Sportsites", value: 0.28},
        {axis: "Search Engine", value: 0.46},
//        {axis: "View Shopping sites", value: 0.29},
//        {axis: "Paying Online", value: 0.11},
//        {axis: "Buy Online", value: 0.14},
//        {axis: "Stream Music", value: 0.05},
//        {axis: "Online Gaming", value: 0.19},
//        {axis: "Navigation", value: 0.14},
//        {axis: "App connected to TV program", value: 0.06},
//        {axis: "Offline Gaming", value: 0.24},
//        {axis: "Photo Video", value: 0.17},
//        {axis: "Reading", value: 0.15},
//        {axis: "Listen Music", value: 0.12},
        {axis: "Watch TV", value: 0.1},
        {axis: "TV Movies Streaming", value: 0.14},
        {axis: "Listen Radio", value: 0.06},
        {axis: "Sending Money", value: 0.16},
        {axis: "Other", value: 0.07},
        {axis: "Use less Once week", value: 0.17}
    ]
];

var mycfg = {
    w: w,
    h: h,
    maxValue: 0.2,
    levels: 10,
    ExtraWidthX: 300
};

RadarChart.draw("#chart", d, mycfg);
notes();

function notes() {
/////////// Initiate legend ////////////////
    var svg = d3.select('#radarChart')
        .selectAll('svg')
        .append('svg')
        .attr("width", w + 300)
        .attr("height", h)

    var text = svg.append("text")
        .attr("class", "title")
        .attr('transform', 'translate(90,0)')
        .attr("x", w - 70)
        .attr("y", 10)
        .attr("font-size", "12px")
        .attr("fill", "#404040")
        .text("Apple...Apple...Apple...Apple...Apple...Apple...");

    var legend = svg.append("g")
        .attr("class", "legend")
        .attr("height", 100)
        .attr("width", 200)
        .attr('transform', 'translate(90,20)');
//Create colour squares
    legend.selectAll('rect')
        .data(LegendOptions)
        .enter()
        .append("rect")
        .attr("x", w - 65)
        .attr("y", function (d, i) {
            return i * 20;
        })
        .attr("y", function (d, i) {
            return i * 20;
        })
        .attr("width", 10)
        .attr("height", 10)
        .style("fill", function (d, i) {
            return colorscale(i);
        });

//Create text next to squares
    legend.selectAll('text')
        .data(LegendOptions)
        .enter()
        .append("text")
        .attr("x", w - 52)
        .attr("y", function (d, i) {
            return i * 20 + 9;
        })
        .attr("font-size", "11px")
        .attr("fill", "#737373")
        .text(function (d) {
            return d;
        });

}


d3.select(window).on('resize', resize);

function resize() {
    //console.log("curWindowWidth : " + curWindowWidth + " / curWindowHeight : " + curWindowHeight);
    // remove old svg if any -- otherwise resizing adds a second one
    d3.select('svg').remove();

    var newWindowWidth = $(window).width();
    var newWindowHeight = $(window).height();

    //console.log("w : " + w + " / h : " + h);
    new_w = w - ((curWindowWidth - newWindowWidth) * 0.7);
    new_h = h - ((curWindowHeight - newWindowHeight) * 0.7);


    //console.log("new_w : " + new_w + " / new_h : " + new_h);

    var tmpCfg = {
        w: w - (curWindowWidth - newWindowWidth),
        h: h - (curWindowHeight - newWindowHeight),
        maxValue: 0.6,
        levels: 5,
        ExtraWidthX: 100
    };

    //redraw
    RadarChart.draw("#chart", d, tmpCfg);

    curWindowWidth = newWindowWidth;
    curWindowHeight = newWindowHeight;
    w = new_w;
    h = new_h;

    notes();
}

