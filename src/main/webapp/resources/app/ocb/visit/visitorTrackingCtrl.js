function visitorTrackingCtrl($scope, $http, DATE_TYPE_DAY, reportSvc) {
    $scope.chartId = '#visitorTracking';
    var bounce = '이탈';
    var nodeId = 0;
    var margin = {top: 10, right: 10, bottom: 10, left: 10},
        width = 1200 - margin.left - margin.right,
        height = 900 - margin.top - margin.bottom;
    var formatNumber = d3.format(",.0f"),    // zero decimal places
        format = function(d) { return formatNumber(d); },
        color = d3.scale.category50();

    $scope.init = function () {
        var defaultCal = reportSvc.defaultCal();
        $scope.searchConfig = {
            startDate : defaultCal.startDateStrPlain,
            endDate: defaultCal.endDateStrPlain
        };
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.searchStartDate = defaultCal.startDateStrPlain;
        $scope.searchEndDate = defaultCal.endDateStrPlain;
        drawFlowChart();
    };

    function drawFlowChart() {
        var url = '/ocb/visit/tracking/chart?dateType=' + $scope.searchDateType +
            '&startDate=' + $scope.searchStartDate + '&endDate=' + $scope.searchEndDate;
        reportSvc.getReportApi(url).then(function(result) {
            convertData(result.rows);
            initDepth();

            var svg = d3.select($scope.chartId).append('svg')
                .attr('width', width + margin.left + margin.right)
                .attr('height', height + margin.top + margin.bottom)
                .attr("transform",
                "translate(" + margin.left + "," + margin.top + ")");
            drawChart(svg, $scope.depthData[0]);
        });
    }

    /**
     * drawFlow Chart
     * @param  target selection
     * @param  tree  데이터 배열
     */
    function drawChart(target, tree) {
        // nodes와 links 데이터 추출.
        var cluster = d3.layout.cluster().children(function(d) { return d ? d.values : []; })
                .value(function(d) { return d.values.cnt; });
        var nodes = cluster.nodes(tree);

        _.each(nodes, function (node) {
            node.idx = node.idx ? node.idx : nodeId++;
        });
        var links = cluster.links(nodes);
        angular.forEach(links, function (link) {
            link.value = link.target.value;
        });

        // sankey diagram 레이아웃 설정.
        var sankey = d3.sankey()
            .nodeWidth(120)
            .nodePadding(5)
            .size([width, height])
            .nodes(nodes)
            .links(links)
            .layout(32);
        //
        var path = sankey.link();
        // 노드 그리기.
        var nodeSel = target.selectAll('.node').data(nodes, function(d) { return d.idx; });
        nodeSel.transition().attr('transform', function(d) { return "translate(" + d.x + "," + d.y + ")"; })
            .attr('opacity', function(d) { return (d.key != bounce) ? 0.5 : 0; });
        // 노드 사각형 그리기
        nodeSel.select('rect').transition().attr('width', function(d) { return d.dx; })
            .attr('height', function(d) { return Math.max(10, d.dy); })
            .attr('fill', function(d) { return color(d.key); });
        nodeSel.select('text').transition().style('opacity', function(d) { return (d.dy < 12 || d.key == bounce) ? 0 : 1; });

        // 신규 노드 생성.
        var nodeEnter = nodeSel.enter().append('g').attr('class', 'node')
            .attr('transform', function(d) { return "translate(" + d.x + "," + d.y + ")"; })
            .attr('cursor', 'pointer')
            .attr('opacity', function(d) { return (d.key != bounce) ? 0.5 : 0; });
        nodeEnter.append('rect').attr('width', function(d) { return d.dx; })
            .attr('height', function(d) { return Math.max(10, d.dy); })
            .attr('fill', function(d) { return color(d.key); });
        // node 정보 셋팅
        nodeEnter.append('text').text(function(d) {return d.key + '(' + d.value + ')';})
            .attr('dy', 12).style('opacity', function(d) { return (d.dy < 12 || d.key == bounce) ? 0 : 1; });
        // 노드 클릭시 처리
        nodeEnter.on('click', function (d) {
            if (d.parent) {
                if (d3.select($scope.chartId).selectAll('svg')) {
                    d3.select($scope.chartId).selectAll('svg').remove();
                }
                var svg = d3.select($scope.chartId).append('svg')
                    .attr('width', width + margin.left + margin.right)
                    .attr('height', height + margin.top + margin.bottom)
                    .attr("transform",
                    "translate(" + margin.left + "," + margin.top + ")");
                drawChart(svg, d.parent);
            }
        });

        // 링크 그리기.
        var linkSel = target.selectAll('.link').data(links);//, function(d) {return d.source.idx + ' - ' + d.target.idx;}
        linkSel.transition().attr('opacity', function(d) { return (d.target.key != bounce) ? 0.5 : 0; });
        linkSel.select('path').transition().attr('stroke-width', function(d) { return Math.max(1, d.dy); })
            .attr("d", function(d) { return (d.key != bounce) ? path(d) : ""; });

        // 신규 링크 생성.
        var linkEnter = linkSel.enter().append('g').attr('class', 'link');
        linkEnter.append('path').attr('stroke', 'grey').attr('fill', 'none')
            .attr('d', function(d) { return (d.key != bounce) ? path(d) : ""; })
            .attr('stroke-width', function(d) { return d.dy; })
            .attr('opacity', 0.5);
        linkEnter.append("title").text(function(d) { return d.source.key + " -> " + d.target.key + "\n" + format(d.value); });

    }

    /**
     * tree구조로 변경하기 위한 sid 기준으로 배열 정렬
     * @param data json 데이터
     */
    function convertData(data) {
        $scope.sankeyData = [];
        var tmpArr = [], i = 0, oldSid = 0;
        angular.forEach(data, function (obj) {
            var currentSid = obj.sid;
            if (oldSid != currentSid) {
                $scope.sankeyData.push(tmpArr);
                 i = 0;
                tmpArr = [];
            }
            tmpArr.push(obj);
            oldSid = currentSid;
        });
    }

    /**
     * flow 분석 경로 깊이 지정.
     */
     function initDepth() {
         $scope.depthData = d3.nest()
             .key(function(d) { return d[0] ? d[0].path : bounce; })
             .key(function(d) { return d[1] ? d[1].path : bounce; })
             .key(function(d) { return d[2] ? d[2].path : bounce; })
             .key(function(d) { return d[3] ? d[3].path : bounce; })
             .key(function(d) { return d[4] ? d[4].path : bounce; })
             .key(function(d) { return d[5] ? d[5].path : bounce; })
             .rollup(function(values) {
                 return {cnt: values.length, children: values}
             })
             .entries($scope.sankeyData);
     }

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;
        $scope.searchMeasureCode = result.searchCode;
        if (d3.select($scope.chartId).selectAll('svg'))
            d3.select($scope.chartId).selectAll('svg').remove();
        drawFlowChart();
    };
}
