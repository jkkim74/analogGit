function handInputSyrupCtrl($scope, $stateParams, DATE_TYPE_DAY, adminSvc, userSvc) {
    $scope.init = function () {
        $scope.menuCode = $stateParams.menuCode;
        $scope.categoryCode = $stateParams.categoryCode;
        $scope.dateFlag = true;
        $scope.searchDate = DateHelper.getCurrentDateByPattern('YYYYMMDD');
        $scope.currentDate = $scope.searchDate;
        $scope.searchDateType = DATE_TYPE_DAY;
        $scope.adminDateTypes = [{key: 'day', label: '일간'}, {key: 'month', label: '월간'}];
        $scope.randomValue = 1;
        callHandInputSyrupResult();
    };

    $scope.saveGridData = function () {
        var table;
        adminSvc.checkBatchProcessing(8).then(function(result){
            var gridData = [];
            if (result.code === 200) {
                table = $scope.handInputSyrupGrid;
                var gr = table.jqGrid("getGridParam","selarrrow");	// 선택된 Rows
                if (gr.length > 0) {
                    //for (var i = 0; i < gr.length; i++) {
                    var grLength = gr.length;
                    grLength.times(function (i) {
                        //선택된 값들을 Local 에 저장한다.
                        table.jqGrid("saveRow", gr[i], false, "clientArray");
                        var ret = table.jqGrid("getRowData", gr[i]);
                        if (ret.rsltVal)
                            ret.rsltVal = parseFloat(ret.rsltVal);
                        if (ret.rsltVal1)
                            ret.rsltVal1 = parseFloat(ret.rsltVal1);
                        if (ret.rsltVal2)
                            ret.rsltVal2 = parseFloat(ret.rsltVal2);
                        if (ret.rsltVal3)
                            ret.rsltVal3 = parseFloat(ret.rsltVal3);
                        if (ret.rsltVal4)
                            ret.rsltVal4 = parseFloat(ret.rsltVal4);
                        if (ret.rsltVal5)
                            ret.rsltVal5 = parseFloat(ret.rsltVal5);
                        if(DateHelper.checkGridEditable($scope.searchDateType, ret, $scope.currentDate)) {
                            gridData.push(ret);
                        }
                    });
                    // 변환한 데이터를 가지고 서버에 저장요청을 한다.
                    $scope.setGridData = {
                        dateType : $scope.searchDateType,
                        username : userSvc.getUser().username,
                        handInputs : gridData
                    };

                    adminSvc.createGridData(8, $scope.setGridData).then(function(createResult) {
                        if (createResult.code === 200) {
                            alert("실적 처리가 정상적으로 이루어졌습니다.");
                        } else {
                            alert("실적 처리가 실패했습니다.");
                        }
                    });
                    $scope.randomValue = Math.random();
                    callHandInputSyrupResult();
                } else {
                    alert("선택된 데이터가 없습니다.");
                }
            }
        });
        return false;
    };

    function callHandInputSyrupResult() {
        var url = '/admin/boss/handInputSyrup?dateType=' + $scope.searchDateType
            + '&basicDate=' + $scope.searchDate +  '&reloadable=' + $scope.randomValue;
        var lastSel;
        var colNames = [];
        var colModels = [];
        if ($scope.searchDateType === 'day') {
            colNames = ['기준날짜', '기간', '신규가입자수', '총회원수', 'UV', '일누적 주UV', '일누적 월UV'];
            colModels = [
                {name:"mappStrdDt",index:"mappStrdDt",hidden:true},
                {name: 'dispDt', index: 'dispDt', editable: false, width: 160, align: "center", sortable: true},
                {name: 'rsltVal1', index: 'rsltVal1', editable: true, formatter: 'integer', width: 140, align: "right", sortable: false},
                {name: 'rsltVal2', index: 'rsltVal2', editable: true, formatter: 'integer', width: 140, align: "right", sortable: false},
                {name: 'rsltVal3', index: 'rsltVal3', editable: true, formatter: 'integer', width: 150, align: "right", sortable: false},
                {name: 'rsltVal4', index: 'rsltVal4', editable: true, formatter: 'integer', width: 160, align: "right", sortable: false},
                {name: 'rsltVal5', index: 'rsltVal5', editable: true, formatter: 'integer', width: 160, align: "right", sortable: false}
            ];
        } else {
            colNames = ['기준날짜', '기간', '월UV'];
            colModels = [
                {name:"mappStrdDt",index:"mappStrdDt",hidden:true},
                {name: 'dispDt', index: 'dispDt', editable: false, width: 250, align: "center", sortable: true},
                {name: 'rsltVal', index: 'rsltVal', editable: true, formatter: 'integer', width: 670, align: "right", sortable: false}
            ];
        }
        $scope.handInputSyrupConfig = {
            url: url,
            type: "GET",
            datatype: "json",
            height: '100%',
            shrinkToFit: true, //width scroll
            loadui: "block",
            rowNum: 10000,
            id: 'handInputSyrupForGrid',
            colNames: colNames,
            colModel: colModels,
            sortname: 'dispDt',
            sortorder: 'asc',
            multiselect: true,
            onSelectRow: function(id, state){
                var rowData = $(this).jqGrid("getRowData", id);
                if(state) {	// 선택되었을 때
                    if(DateHelper.checkGridEditable($scope.searchDateType, rowData, $scope.currentDate)) {
                        lastSel = id;
                        $("#" + id).addClass("skp-grid-edit-mode");
                        $(this).jqGrid("editRow", id);
                    } else {
                        alert("미래 데이터는 입력하실 수 없습니다.");
                    }
                } else {
                    $(this).jqGrid("saveRow",id, false, "clientArray");
                }
            },
            editurl: "clientArray",
            gridComplete: function(){
                $scope.handInputSyrupGrid = $(this);
            },
            gridview: true,
            useColSpanStyle: true,
            autowidth: true,
            jsonReader: {
                root: "rows"
            }
        };
    }

    /**
     * 검색 callback
     * @param result
     */
    $scope.search = function(result) {
        $scope.searchDate = result.searchDate;
        $scope.searchDateType = result.searchDateType;

        callHandInputSyrupResult();
    };
}
