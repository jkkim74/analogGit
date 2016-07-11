function service3Ctrl($scope, $stateParams, $http, API_BASE_URL) {
    $scope.serviceName = $stateParams.serviceName;
    $scope.menuCode = $stateParams.menuCode;
    // 최초 로딩되는 함수
    $scope.init = function () {
        // 월별 매출액 Grid처리
        $("#jqGrid01").jqGrid("GridUnload");
        $("#jqGrid01").jqGrid({
            url: '/sales/sumAmt/pagination?startDate=20101023000000&endDate=20141023999999',
            //data: mydata,
            datatype: "json",
            id: 'service3CtrlJqGrid01',
            height: 220,
            rowNum: 10,
            rowList: [10,20,30],
            colNames:['매출발생 년월','매출 금액'],
            colModel:[
                {name:'key',index:'key', editable: true, search:false, sortable:true},
                {name:'value',index:'value', editable: true, align:"right", search:false, sorttype:"float", sortable:true}
            ],
            pager: "#jqGridPager01",
            viewrecords: true,
            add: true,
            edit: true,
            addtext: 'Add',
            edittext: 'Edit',
            sortname: 'key',
            sortorder: "asc",
            multiselect: false,
            autowidth: true,
            hidegrid:false,
            excel: true,
            subGrid : true,
            subGridOptions: {
                "plusicon"  : "ui-icon-triangle-1-e",
                "minusicon" : "ui-icon-triangle-1-s",
                "openicon"  : "ui-icon-arrowreturn-1-e",
                // load the subgrid data only once
                // and the just show/hide
                "reloadOnExpand" : false,
                // select the row when the expand column is clicked
                "selectOnExpand" : true
            },
            subGridRowExpanded: function(subgrid_id, id) {
                var subgrid_table_id, pager_id;
                subgrid_table_id = subgrid_id+"_t";
                pager_id = "p_"+subgrid_table_id;
                $("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
                $("#"+subgrid_table_id).jqGrid({
                    url:"/sales/sumAmt/sex?key=" + id,
                    datatype: "json",
                    colNames: ['성별','매출 금액'],
                    colModel: [
                        {name:"key",index:"key",width:60,key:true, sortable:false},
                        {name:"value",index:"value",width:100,align:"right",sortable:false}
                    ],
                    rowNum:5,
                    pager: pager_id,
                    sortname: 'key',
                    sortorder: "asc",
                    height: '100%',
                    width: '180'
                });
                $("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,{edit:false,add:false,del:false})
            },
            jsonReader : {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: true,
                cell: "cell",
                id : "key",
                subgrid : {root : "rows" , repeatitems : true , cell : "cell" }
            }
        });

        // Setup buttons
        $("#jqGrid01").jqGrid('navGrid','#jqGridPager01',
            {edit:false,add:false,del:false,search:false},
            {height:200,reloadAfterSubmit:true}
        );

        // Setup filters
        $("#jqGrid01").jqGrid('filterToolbar',{defaultSearch:true,stringResult:true});

        // Set grid width to #content
        $("#jqGrid01").jqGrid('setGridWidth', $("#content").width(), true);

        $scope.getLineChartData();
    }

    // d3 포멧 파싱 함수
    var parseDate = d3.time.format("%Y-%m").parse;

    //y축 입력값 지정 및 포멧팅
    $scope.yFunction = function() {
        return function(d){
            return d.y;
        }
    };

    // x 축 입력값 지정 및 포멧팅
    $scope.xFunction = function() {
        return function(d){
            return parseDate(d.x);
        }
    };

    // x축 디스플레이 값 포멧 지정
    $scope.xAxisTickFormatFunction = function(){
        return function(d){
            return d3.time.format('%Y-%m')(new Date(d));
        }
    };
    // 월별 매출액 Line Chart 그리기
    $scope.getLineChartData = function () {
        $http.get('/sales/sumAmt/payDate?startDate=20101023000000&endDate=20141023999999').success(function(result) {
            if (result.length != 0) {
                var values = result.map(function(d, i) {
                    return {x:d.key, y:d.value};
                });
                $scope.linesDatas = [{"values": values, "key": "매출액 추이"}];
            } else {
            }
        }).error(function() {
            alert('error!');
        });
    };

    // pivot 테이블 그리기 함수
    function drawPivot() {
        $http.get('/salesUser/signupCount?startDate=20101023000000&endDate=2014102399999').success(function(result) {
            if (result.length != 0) {
                var pivotData =  result.rows;
                $("#output").pivotUI(
                    pivotData,
                    {
                        rows: ["sex", "ageGroup"],
                        cols: ["signupDate"],
                        vals: ['signupCnt'],
                        aggregatorName: 'intSum',
                        rendererName: 'table'
                    },
                    false
                );
                $("#output > table").find("tr").each(function(index, domElement) {
                    if (index == 0 || index == 1)
                        $(domElement).css("display", "none");
                });
                $(".pvtAxisContainer.pvtRows").hide();
            } else {
            }
        }).error(function() {
            alert('error!');
        });
    };

    // rowSpan 기능 반영
    function arrtSetting(rowId, val, rawObject, cm) {
        var result;

        if (prevCellVal.value == val) {
            result = ' style="display: none" rowspanid="' + prevCellVal.cellId + '"';
        }
        else {
            var cellId = this.id + '_row_' + rowId + '_' + cm.name;

            result = ' rowspan="1" id="' + cellId + '"';
            prevCellVal = { cellId: cellId, value: val };
        }

        return result;
    };

    // 회원가입(성별, 연령별)

    $scope.searchStartDate = '20101023000000';
    $scope.searchEndDate = '20141023999999';
    $scope.signupStatistics = function() {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevCell2 = { cellId: undefined, value: undefined };
        $("#jqGrid03").jqGrid("GridUnload");
        $("#jqGrid03").jqGrid({
            url: '/salesUser/signupCount?startDate='+$scope.searchStartDate+'&endDate=' +$scope.searchEndDate,
            datatype: "json",
            height: "100%",
            useColSpanStyle: true,
            rowNum: -1,
            rowList: [10,20,30],
            colNames:['기준일자','성별','연령별','신규 가입자수'],
            colModel:[
                {name:'signupDate', index:'signupDate', editable:false, width:20, align:"center", sortable:true,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        prevPrevCell1 = prevCell1.value;
                        if (prevPrevCell1 == val) {
                            result = ' style="display: none" rowspanid="' + prevCell1.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell1 = { cellId: cellId, value: val };
                        }

                        return result;
                    }
                },
                {name:'sex',index:'sex', editable:false, width:20, align:"center", sortable:true,
                    cellattr: function (rowId, val, rawObject, cm, rdata) {
                        var result;
                        if (prevPrevCell1 == rawObject.signupDate && prevCell2.value == val) {
                            result = ' style="display: none" rowspanid="' + prevCell2.cellId + '"';
                        }
                        else {
                            var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                            result = ' rowspan="1" id="' + cellId + '"';
                            prevCell2 = { cellId: cellId, value: val };
                        }

                        return result;
                    }
                },
                {name:'ageGroup',index:'ageGroup', editable:false, width:20, align:"center", sortable:true},
                {name:'signupCnt', index:'signupCnt', editable: false, width:30, align:"right", search:false, sortable:false}
            ],
            //pager: "#jqGridPager03",
            viewrecords: false,
            add: false,
            edit: false,
            addtext: 'Add',
            edittext: 'Edit',
            sortname: 'signupDate',
            sortorder: "asc",
            multiselect: false,
            autowidth: true,
            excel: true,
            hidegrid:false,
            jsonReader : {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: true,
                cell: "cell"
            },
            beforeSelectRow: function () {
                return false;
            },
            // rowspan 갯수 산정해서 병합 처리.
            gridComplete: function () {
                var grid = this;
                $('td[rowspan="1"]', grid).each(function () {
                    var spans = $('td[rowspanid="' + this.id + '"]', grid).length + 1;

                    if (spans > 1) {
                        $(this).attr('rowspan', spans);
                    }
                });
                prevCell1 = { cellId: undefined, value: undefined };
                prevPrevCell1 = "";
                prevCell2 = { cellId: undefined, value: undefined };

                $(this).find('td:hidden').remove();
            },
            loadComplete: function() {
                if($(this).getGridParam('records') === 0) {
                    $(this).append('<tr><td colspan="4" class="text-center">no Data</td></tr>');
                }
            }
        });

    };

    // 회원가입(성별, 연령별)
    $scope.signupStatisticsForPivot = function() {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevCell2 = { cellId: undefined, value: undefined };
        $("#jqGrid04").jqGrid("GridUnload");
        var jqGrid = $("#jqGrid04").jqGrid('jqPivot',
            "/salesUser/signupCount?startDate=20101023000000&endDate=20141023999999",
            // pivot options
            {
                xDimension : [
                    {
                        dataName: 'sex',
                        label : '성별'
                    } ,{
                        dataName: 'ageGroup',
                        label : '나이'
                    }
                ],
                yDimension : [
                    {
                        dataName: 'signupDate'
                        //label : '기준일자'
                    }
                ],
                aggregates : [
                    {
                        member : 'signupCnt',
                        aggregator : 'sum',
                        label:'가입자수',
                        formatter:'integer',
                        align:'right',
                        summaryType: 'sum'
                    }
                ]
            },
            // grid options
            {
                width: "100%",
                rowNum : 5000,
                pager: "#jqGridPager04",
                height: "100%",
                //autowidth: true,
                sortname: 'ageGroup',
                sortorder: "asc",
//                jsonReader: {
//                    root: "rows",
//                    page: "page",
//                    total: "total",
//                    records: "records",
//                    repeatitems: true,
//                    cell: "cell"
//                },
                loadError: function(xhr, status, error){
                    alert("그룹목록 에러발생 : "+ xhr.responseText);
                },
                loadComplete: function() {
                    $('#jqgh_jqGrid04_ageGroup').trigger('click');
                    $('#jqgh_jqGrid04_ageGroup').trigger('click');
                },
                onSelectRow: function(rowid, status, e) {
                    alert("asdf : " + rowid + "/" + status);
                },
            }
        );

        //$('#jqgh_jqGrid04_ageGroup .ui-icon-desc').trigger('click');

//        jqGrid.jqGrid('navButtonAdd','#jqGridPager04',{
//              caption:"",
//              onClickButton : function () {
//                  jqGrid.jqGrid('excelExport',{"url":"querygrid.php"});
//              }
//          });

    };

    /**
     * searchBox callback function.
     */
    $scope.search = function(result) {
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate  = result.searchEndDate;
        $scope.signupStatistics();
    };

    $scope.drawRadarChart =  function() {
        var d = [
            [
                {axis: "UV", value: 13},
                {axis: "LV", value: 15},
                {axis: "신규 가입", value: 8},
                {axis: "총 회원수", value: 20},
                {axis: "카드 발급 건수", value: 3}
            ]
        ];

        RadarChart.draw("#radarChart", d);
    };
}
