function service2Ctrl($scope, $stateParams, $http, API_BASE_URL) {
    $scope.serviceName = $stateParams.serviceName;

    $scope.menuInit4 = function () {

        $scope.sumNewEnterCntPerDay = {
            url: '/ocb/member/enter/grid?dateType='+$scope.searchDateType+'&startDate='+$scope.searchStartDate+'&endDate='+$scope.searchEndDate,
            datatype: "json",
            id: 'sumNewEnterCntPerDay',
            height: '100%',
            rowNum: -1,
            rowList: [10, 20, 30],
            colNames: ['기준일자', '신규가입자수', '누적가입자수'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, width: 90, align: "center", sortable: true},
                {name: 'newEntrCnt', index: 'newEntrCnt', editable: false, width: 90, formatter: 'integer', align: "center", sortable: true},
                {name: 'result', index: 'result', editable: false, width: 90, formatter: 'integer', align: "center", sortable: true}
            ],
//            pager: "#sumNewEnterCntPerDayPager",
            viewrecords: true,
            add: true,
            addtext: 'Add',
            edittext: 'Edit',
            caption: "회원가입 리스트",
            sortname: 'result',
            sortorder: 'asc',
            multiselect: false,
            autowidth: true,
            hidegrid: false,
//            scroll: true,
//            loadonce: true,
//            footerrow: true,
//            userDataOnFooter:true,
//            altRows:true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            },
            loadComplete: function() {
                if($(this).getGridParam('records') == 0) {
                    $(this).append('<tr><td colspan="3" class="text-center"><h3>no Data</h3></td></tr>');
                }
            }

        };

    }//menuInit4(--end

    $scope.menuInit5 = function () {
        var prevCell1 = { cellId: undefined, value: undefined };
        var prevPrevCell1 = "";
        var prevCell2 = { cellId: undefined, value: undefined };


        $scope.sumNewEnterCntPerDayBySexAge = {
            url: API_BASE_URL+'/obsDMbrentSta/sumNewEnterCntPerDayBySexAge',
            datatype: "json",
            id: 'sumNewEnterCntPerDayBySexAge',
            height: '100%',
            rowNum: -1,
            rowList: [10, 20, 30],
            colNames: ['기준일자', '성별', '연령대', '신규가입자수', '누적가입자수'],
            colModel: [
                {name: 'stdDt', index: 'stdDt', editable: false, width: 90, align: "center", sortable: true,
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
                    }},
                {name: 'sexIndCd', index: 'sexIndCd', editable: false, width: 90, formatter: '', align: "center", sortable: true, cellattr: function (rowId, val, rawObject, cm, rdata) {
                    var result;
                    if (prevPrevCell1 == rawObject.stdDt && prevCell2.value == val) {
                        result = ' style="display: none" rowspanid="' + prevCell2.cellId + '"';
                    }
                    else {
                        var cellId = this.id + '_row_' + rowId + '_' + cm.name;
                        result = ' rowspan="1" id="' + cellId + '"';
                        prevCell2 = { cellId: cellId, value: val };
                    }

                    return result;
                }},
                {name: 'ageLgrpCd', index: 'ageLgrpCd', editable: false, width: 90, formatter: 'integer', align: "center", sortable: true},
                {name: 'newEntrCnt', index: 'newEntrCnt', editable: false, width: 90, formatter: 'integer', align: "center", sortable: true},
                {name: 'result', index: 'result', editable: false, width: 90, formatter: 'integer', align: "center", sortable: true}
            ],
//            pager: "#sumNewEnterCntPerDayBySexAgePager",
            viewrecords: true,
            add: true,
            addtext: 'Add',
            edittext: 'Edit',
            caption: "회원가입(성별,연령) 리스트",
            sortname: 'result',
            sortorder: 'asc',
            multiselect: false,
            autowidth: true,
            hidegrid: false,
//            scroll: true,
//            loadonce: true,
//            footerrow: true,
//            userDataOnFooter:true,
//            altRows:true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            },
            beforeSelectRow: function () {
                return false;
            },
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
            }
        };

    }//menuInit5()--end

    $scope.init = function () {
        $("#jqGrid02").jqGrid("GridUnload");
        $("#jqGrid02").jqGrid({
            url: API_BASE_URL+'/salesUser/sumCount/pagination',
            //data: mydata,
            datatype: "json",
            height: "300",
            //width:"",
            rowNum: 10,
            rowList: [10, 20, 30],
            colNames: ['가입 년월', '가입자 수'],
            colModel: [
                {name: 'key', index: 'key', editable: false, search: false, sortable: true},
                {name: 'value', index: 'value', editable: false, align: "right", search: false, sorttype: "float", sortable: true}
            ],
            //pager: "#jqGridPager02",
            //viewrecords: true,
            add: true,
            edit: true,
            addtext: 'Add',
            edittext: 'Edit',
            caption: "월별 가입자 데이터",
            autowidth: true,
            hidegrid: false,
            loadonce: true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            }
        });

//        // Setup buttons
//        $("#jqGrid02").jqGrid('navGrid', '#jqGridPager02',
//            {edit: true, add: true, del: true, search: true},
//            {height: 200, reloadAfterSubmit: true}
//        );
//
//        // Setup filters
//        $("#jqGrid02").jqGrid('filterToolbar', {defaultSearch: true, stringResult: true});
//
//        // Set grid width to #content
//        $("#jqGrid02").jqGrid('setGridWidth', $("#content").width(), true);

        $scope.getLineChartData();


        /**
         * menu2 here..
         */
        $scope.visitorListPerDayPagination = {
            url: API_BASE_URL+'/ocbDayVisitStc/visitorList/pagination?startDate=20140101&endDate=20141231',
            datatype: "json",
            id: 'visitorListPerDayPagination',
            height: 300,
            rowNum: 1000000,
            rowList: [10, 20, 30],
            colNames: ['기준일자', 'UV', 'LV', '신규방문자수', '재방문자수'],
            colModel: [
                {name: 'baseDt', index: 'baseDt', editable: false, width: 90, sorttype: 'date'},
                {name: 'uvCnt', index: 'uvCnt', editable: false, width: 90, sorttype: 'float', formatter: 'integer'},
                {name: 'lvCnt', index: 'lvCnt', editable: false, width: 90, sorttype: 'float', formatter: 'integer'},
                {name: 'newVisitCnt', index: 'newVisitCnt', editable: false, width: 90, sorttype: 'float', formatter: 'integer'},
                {name: 'reVisitCnt', index: 'reVisitCnt', editable: false, width: 90, sorttype: 'float', formatter: 'integer'}
            ],
//            pager: "#myPager",
            viewrecords: true,
            add: true,
            addtext: 'Add',
            edittext: 'Edit',
            caption: "방문자 일별 조회 리스트",
            autowidth: true,
            hidegrid: false,
            scroll: true,
            loadonce: true,
//            footerrow: true,
//            userDataOnFooter:true,
//            altRows:true,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            }
        };
//        $("#visitorListPerDayPagination").jqGrid('sortableRows');


    }//init()


    $scope.testInit = function () {
        var testGridData = [
            {id: "1", invdate: "2010-05-24", name: "test", note: "note", tax: "10.00", total: "2111.00"} ,
            {id: "2", invdate: "2010-05-25", name: "test2", note: "note2", tax: "20.00", total: "320.00"},
            {id: "3", invdate: "2007-09-01", name: "test3", note: "note3", tax: "30.00", total: "430.00"},
            {id: "4", invdate: "2007-10-04", name: "test", note: "note", tax: "10.00", total: "210.00"},
            {id: "5", invdate: "2007-10-05", name: "test2", note: "note2", tax: "20.00", total: "320.00"},
            {id: "6", invdate: "2007-09-06", name: "test3", note: "note3", tax: "30.00", total: "430.00"},
            {id: "7", invdate: "2007-10-04", name: "test", note: "note", tax: "10.00", total: "210.00"},
            {id: "8", invdate: "2007-10-03", name: "test2", note: "note2", amount: "300.00", tax: "21.00", total: "320.00"},
            {id: "9", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00"},
            {id: "11", invdate: "2007-10-01", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00"},
            {id: "12", invdate: "2007-10-02", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00"},
            {id: "13", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00"},
            {id: "14", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00"},
            {id: "15", invdate: "2007-10-05", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00"},
            {id: "16", invdate: "2007-09-06", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00"},
            {id: "17", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00"},
            {id: "18", invdate: "2007-10-03", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00"},
            {id: "19", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00"},
            {id: "21", invdate: "2007-10-01", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00"},
            {id: "22", invdate: "2007-10-02", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00"},
            {id: "23", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00"},
            {id: "24", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00"},
            {id: "25", invdate: "2007-10-05", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00"},
            {id: "26", invdate: "2007-09-06", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00"},
            {id: "27", invdate: "2007-10-04", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00"},
            {id: "28", invdate: "2007-10-03", name: "test2", note: "note2", amount: "300.00", tax: "20.00", total: "320.00"},
            {id: "29", invdate: "2007-09-01", name: "test3", note: "note3", amount: "400.00", tax: "30.00", total: "430.00"}
        ];


        var lastsel;
        $("#testGrid").jqGrid("GridUnload");
        $("#testGrid").jqGrid({
            data: testGridData,
            datatype: "local",
            height: 300,
            colNames: ['Inv No', 'Date', 'Client', 'Amount', 'Tax', 'Total', 'Notes'],
            colModel: [
                {name: 'id', index: 'id', width: 55},
                {name: 'invdate', index: 'invdate', width: 90, editable: true},
                {name: 'name', index: 'name asc, invdate', width: 100, editable: true},
                {name: 'amount', index: 'amount', width: 80, align: "right", editable: true, editrules: {number: true}},
                {name: 'tax', index: 'tax', width: 80, align: "right", editable: true, editrules: {number: true}},
                {name: 'total', index: 'total', width: 80, align: "right", editable: true, editrules: {number: true}},
                {name: 'note', index: 'note', width: 150, sortable: false, editable: true}
            ],
            rowNum: 10,
            rowList: [10, 20, 30],
            pager: '#testGridPager',
            sortname: 'id',
            viewrecords: true,
            sortorder: "desc",
            multiselect: false,
            caption: "Test...",
            hiddengrid: false,
            loadComplete: function () {
                var ret = $("#testGrid").jqGrid('getRowData', 7);
                // alert(ret.id+ "/" +ret.total + "/" +ret.tax);
                if (ret.id == '7') {
                    $("#testGrid").jqGrid('setRowData', ret.id, {note: "<font color='red'>Row 13 is updated!</font>"})
                }
            },
            gridComplete: function () {
                var ids = $("#testGrid").jqGrid('getDataIDs');
//                alert(ids.length);
                for (var i = 0; i < ids.length; i++) {
                    var cl = ids[i];
                    be = "<input style='height:22px;width:20px;' type='button' value='E' onclick=\"$('#testGrid').editRow('" + cl + "');\"  />";
                    se = "<input style='height:22px;width:20px;' type='button' value='S' onclick=\"$('#testGrid').saveRow('" + cl + "');\"  />";
                    ce = "<input style='height:22px;width:20px;' type='button' value='C' onclick=\"$('#testGrid').restoreRow('" + cl + "');\" />";
                    $("#testGrid").jqGrid('setRowData', ids[i], {act: be + se + ce});
                }
            },
            onSelectRow: function (id) {
                if (id && id !== lastsel) {
                    $('#testGrid').jqGrid('restoreRow', lastsel);
                    $('#testGrid').jqGrid('editRow', id, true);
                    lastsel = id;
                }
            },
            afterInsertRow: function (rowid, data) {
                switch (data.name) {
                    case 'test':
                        $("#testGrid").jqGrid('setCell', rowid, 'total', '', {color: 'green'});
                        break;
                    case 'test2':
                        $("#testGrid").jqGrid('setCell', rowid, 'total', '', {color: 'red'});
                        break;
                    case 'test3':
                        $("#testGrid").jqGrid('setCell', rowid, 'total', '', {color: 'blue'});
                        break;

                }
            }

        });
        $("#testGrid").jqGrid('navGrid', '#testGridPager', {del: false, add: false, edit: false}, {}, {}, {}, {multipleSearch: true});


        /**
         * menu3 here..
         */
        $scope.salesUsersSumCount = {
            url: API_BASE_URL+'/salesUser/sumCount/pagination',
            id: 'salesUsersSumCount',
            datatype: "json",
            height: 250,
            rowNum: 10,
            rowList: [10, 20, 30],
            colNames: ['년-월', '가입자 수'],
            colModel: [
                {name: 'key', index: 'key', editable: false, width: 90},
                {name: 'value', index: 'value', editable: false, width: 100}
            ],
            pager: "#myPager",
            viewrecords: true,
            add: true,
            addtext: 'Add',
            edittext: 'Edit',
            caption: "사용자 리스트",
            autowidth: true,
            hidegrid: false,
            jsonReader: {
                root: "rows",
                page: "page",
                total: "total",
                records: "records",
                repeatitems: false,
                cell: "cell"
            }
        };

        $http.get(API_BASE_URL+'/salesUser/sumCount/month').success(function (result) {
            if (result.length != 0) {
                var values = result.map(function (d) {
                    return {x: d.key, y: d.value};
                });
//                alert(values);
                $scope.salesUserSumCountData = [
                    {"values": values, "key": "가입자수"}
                ];
            }
        });

    }


    var parseDate = d3.time.format("%Y-%m").parse;

    $scope.yFunction = function () {
        return function (d) {
            return d.y;
        }
    };

    $scope.xFunction = function () {
        return function (d) {
            return parseDate(d.x);
        }
    };

    $scope.xAxisTickFormatFunction = function () {
        return function (d) {
            return d3.time.format('%Y-%m')(new Date(d));
        }
    };

    $scope.getLineChartData = function () {
        $http.get(API_BASE_URL+'/salesUser/sumCount/month').success(function (result) {
            if (result.length != 0) {
                var values = result.map(function (d, i) {
                    return {x: d.key, y: d.value};
                });
                $scope.linesDatas = [
                    {"values": values, "key": "가입자수 추이"}
                ];
            } else {

            }
        }).error(function () {
            alert('error!');
        });
    }

    var colorArray = ['#FF4699', '#0000FF', '#FFFF00', '#00FFFF'];
    $scope.colorFunction = function () {
        return function (d, i) {
            return colorArray[i];
        }

    }

    $scope.toolTipContentFunction = function () {
        return function (key, x, y, e, graph) {
            return '<p>' + x + '</p>' + '<h3>' + y + '</h3>'

        }
    }

    /**
     * jqGrid Test
     */
    $("#a2").click(function () {
        var su = $("#testGrid").jqGrid('delRowData', 9);
        if (su) {
            alert("success. delete raw clear...")
        }
    });

    $("#a1").click(function () {
        var id = $("#testGrid").jqGrid('getGridParam', 'selrow');

        if (id) {
            var ret = $("#testGrid").jqGrid('getRowData', id);
            alert("id = " + ret.id + " amount = " + ret.amount);
        } else {
            alert("select raw first!")
        }
    });
    $("#a4").click(function () {
        var datarow = {id: "99", amount: "2893.023", tax: "342", invdate: "2009-09-09"}
        var ids = $("#testGrid", opener.document).getDataIDs();
        alert("ids : " + ids.length);
        var su = $("#testGrid").jqGrid('addRowData', ids.length + 1, datarow);

    });

    $("#a5").click(function () {
        var number = $("#testGrid").jqGrid('getGridParam', 'records');
        if (number) {
            alert("this grids record :" + number);
        }
    });

    $("#a6").click(function () {
        var multi = $("#testGrid").jqGrid('getGridParam', 'selarrrow');
        alert(multi);
    });

    $("#a7").click(function () {
        $("#testGrid").jqGrid('hideCol', ["tax"]);
    });

    $("#a8").click(function () {
        $("#testGrid").jqGrid('showCol', ["tax"]);
    });

    $("#a9").click(function () {
        this.disable = 'true';
        $("#a10, #a11").attr('disable', false);
        $("#testGrid").jqGrid('editRow', "7");
    });
    $("#a10").click(function () {
        $("#a10, #a11").attr('disable', true);
        $("#a9").attr('disable', false);
        $("#testGrid").jqGrid('saveRow', "7");
    });
    $("#a11").click(function () {
        $("#a10, #a11").attr('disable', true);
        $("#a9").attr('disable', false);
        $("#testGrid").jqGrid('restoreRow', "7");
    });

    $("#a12").click(function () {
        $("#testGrid").jqGrid('editGridRow', "new", {height: 280, reloadAfterSubmit: false});
    });


}
