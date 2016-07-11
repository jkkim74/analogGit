function mstrCtrl($scope, reportSvc, apiSvc, REPORT_DATE_TYPES) {
    var mstrContainer = null;
    var mstrProjectId = null;
    var mstrMenuCode = null;
    var mstrMultiple = null;

    $scope.init = function() {
        mstrMenuCode = $scope.menuContext.category.code + "_" + $scope.menuContext.menu.code;
        $scope.mstrId = "mstr_" + mstrMenuCode;
        $scope.mstrIframeId = "iframeForMstr_" + mstrMenuCode;

        loadPrompts();
    };

    $scope.$on("$destroy", function() {
        $("#" + $scope.mstrIframeId).remove();
        mstrContainer = null;
        mstrProjectId = null;
        mstrMenuCode = null;
        mstrMultiple = null;
        $scope.mstrId = null;
        $scope.mstrIframeId = null;
        $scope.dateTypes = null;
        $scope.searchDateType = null;
        $scope.searchStartDate = null;
        $scope.searchEndDate = null;
        $scope.init = null;
        $scope.loadReport = null;
        $scope.search = null;
        $scope.changeDate = null;
    });

    function getHttpConfig() {
        var menuSearchOptions = $scope.menuContext.menu.menuSearchOption.label.split(",");
        mstrProjectId = menuSearchOptions[0];
        var mstrObjectIds = [];
        var mstrObjectTypes = [];
        for (var i in menuSearchOptions) {
            if (i > 0) {
                mstrObjectIds.push(menuSearchOptions[i].split("_")[0]);
                mstrObjectTypes.push(menuSearchOptions[i].split("_")[1]);
            }
        }
        mstrMultiple = false;
        if (mstrObjectIds.length > 1)
            mstrMultiple = true;

        $scope.dateTypes = [];
        for (var j in mstrObjectIds) {
            var result = apiSvc.getMstrDateTypes(mstrProjectId, mstrObjectIds[j]);
            if (result && result.dateTypes) {
                angular.forEach(REPORT_DATE_TYPES, function(obj) {
                    if (result.dateTypes.indexOf(obj.key) > -1) {
                        obj.mstrObjectId = mstrObjectIds[j];
                        obj.mstrObjectType = mstrObjectTypes[j];
                        $scope.dateTypes.push(obj);
                    }
                });
            } else {
                //if (mstrMultiple) {
                //    swal("기간검색이 없는 리포트가 포함된\n다중 리포트입니다.\n리포트 관리자에게 문의 하시기 바랍니다.");
                //    $scope.removeReportTab($scope.menuContext);
                //    return false;
                //}
                $scope.dateTypes.push({
                    "key": "",
                    "label": "",
                    "sort": 9,
                    "mstrObjectId": mstrObjectIds[j],
                    "mstrObjectType": mstrObjectTypes[j]
                });
            }
        }
        if (_.uniq($scope.dateTypes).length != $scope.dateTypes.length) {
            swal("일/주/월 중복으로 들어간 리포트입니다.\n리포트 관리자에게 문의 하시기 바랍니다.");
            $scope.removeReportTab($scope.menuContext);
            return false;
        }
        $scope.dateTypes.sort(function (a, b) {
            var aSort = a.sort;
            var bSort = b.sort;
            return ((aSort < bSort) ? -1 : ((aSort > bSort) ? 1 : 0));
        });

        if (!$scope.searchDateType) {
            $scope.searchDateType = $scope.dateTypes[0].key;

            var defaultCal = reportSvc.defaultCal($scope.searchDateType);
            $scope.searchStartDate = defaultCal.startDateStrPlain;
            $scope.searchEndDate = defaultCal.endDateStrPlain;
        }

        var mstrOption = _.find($scope.dateTypes, function(obj) {
            return ($scope.searchDateType == obj.key);
        });

        return {
            url: '/mstr/getPrompts',
            params: {
                searchDateType: $scope.searchDateType,
                searchStartDate: $scope.searchStartDate,
                searchEndDate: $scope.searchEndDate,
                projectId: mstrProjectId,
                objectId: mstrOption.mstrObjectId,
                objectType: mstrOption.mstrObjectType,
                menuCode: mstrMenuCode
            }
        };
    }

    function loadPrompts() {
        var httpConfig = getHttpConfig();
        if (!httpConfig) return;
        apiSvc.voyagerHttpSvc(httpConfig).then(function(result) {
            mstrContainer = angular.element("#" + $scope.mstrId); //init
            mstrContainer.find("mstr-search-box").hide();
            var receiveData = result.data;
            if (receiveData.prompts && httpConfig.params.objectType == 3) {
                mstrContainer.find("mstr-search-box").show();
            }
            if (receiveData) {
                $scope.$safeApply(function() {
                    setFormData(receiveData);
                    displayPrompt(receiveData.prompts);
                    $scope.loadReport();
                });
            } else {
                displayError();
            }
        });
    }

    function setFormData(receiveData) {
        mstrContainer.find("#mstrReportForm").attr("target", $scope.mstrIframeId);
        mstrContainer.find("#mstrReportForm").attr("action", receiveData.baseUrl);
        mstrContainer.find("#usrSmgr").val(receiveData.usrSmgr);
        mstrContainer.find("#hiddenSections").val(receiveData.hiddenSections);
        mstrContainer.find("#currentViewMedia").val(receiveData.currentViewMedia);
        mstrContainer.find("#visMode").val(receiveData.visMode);
        mstrContainer.find("#evt").val(receiveData.evt);
        mstrContainer.find("#src").val(receiveData.src);
        mstrContainer.find("#reportID").val(receiveData.reportId);
        mstrContainer.find("#documentID").val(receiveData.documentId);
    }

    function displayPrompt(prompts) {
        if (!prompts) return;
        mstrContainer.find("#custom-prompt-area").html(prompts);

        //날짜 검색에 대한 처리
        var expressionDatePrompts = mstrContainer.find(".expression-prompt.mstr-date");
        if (expressionDatePrompts.length == 0)
            mstrContainer.find("#date-prompt-area").remove();
        var datePrompts = mstrContainer.find(".constant-prompt.mstr-date");
        if (datePrompts.length > 0) {
            var calendars = "<div class='date-picker-area date-picker-area-custom noMargin'>";
            angular.forEach(datePrompts, function(child, index) {
                child = angular.element(child);
                var id = "search-" + child.find("input[type=text]").attr("id");
                if (index > 0)
                    calendars += "<span class=\"to\">~</span>";
                calendars += "<div class=\"date-picker input-append success date\">"
                    + "<input class=\"form-control date-pick ng-valid ng-dirty\" type=\"text\" id=\"" + id + "\" value=\"" + (DateHelper.stringToYmdStr4(index == 0 ? $scope.searchStartDate : $scope.searchEndDate)) + "\">"
                    + "<span class=\"add-on pull-left\">"
                    + "<span class=\"arrow\"></span>"
                    + "<i class=\"fa fa-th\"></i>"
                    + "<label for=\"" + id + "\" class=\"hidden-text\">조회 " + (index == 0 ? "시작" : "마지막") + "일 입력</label>"
                    + "</span>"
                    + "</div>";
            });
            calendars += "</div>";
            mstrContainer.find("#custom-prompt-area tbody").prepend("<tr><th>기간검색</th><td>" + calendars + "</td></tr>");
            EventBindingHelper.initDatePicker(mstrContainer.find(".date-picker-area-custom"), null);
        }
        var dropDownItems = mstrContainer.find(".drop-down-item");
        angular.forEach(dropDownItems, function(child, index) {
            child = angular.element(child);
            if (index == 0) {
                child.find("td").html(mstrContainer.find(".drop-down-item .select-box"));
            } else {
                child.remove();
            }
        });
    }

    function displayError() {
        var displayStr = "<div style='text-align:center; line-height:25px; padding-top:50px;'>MicroStrategy 서버와의 접속이 원활하지 않습니다."
            + "<br>페이지를 새로고침 해주시기 바랍니다.<br>이 메세지가 지속될 시 재접속 하시거나 관리자에게 문의하여 주십시오.</div>";
        mstrContainer.find("mstr-search-box").hide();
        mstrContainer.find("mstr-report").html(displayStr);
    }

    $scope.loadReport = function() {
        var session = apiSvc.getMstrSession(mstrProjectId);
        if (!session || !session.sessionId) {
            displayError();
            return false;
        }
        mstrContainer.find("#usrSmgr").val(session.sessionId);
        mstrContainer.find(".date-" + $scope.searchDateType).prop("checked",true);

        var promptsAnswerXML = "";
        //Constant Prompt
        var constantPrompts = mstrContainer.find(".constant-prompt");
        angular.forEach(constantPrompts, function(child) {
            child = angular.element(child);
            var answer = child.find("input[type=text]").data("answer");
            var values = child.find("input[type=text]").val();
            if (child.hasClass("mstr-date")) {
                values = DateHelper.dateToParamString(mstrContainer.find("#search-" + child.find("input[type=text]").attr("id")).val());
                if ($scope.searchDateType == "month") {
                    values = values.substr(0,6);
                } else if ($scope.searchDateType == "week") {
                    var weekType = "week1";
                    if (child.hasClass("week2")) {
                        weekType = "week2";
                    } else if (child.hasClass("week3")) {
                        weekType = "week3";
                    }
                    var answers = apiSvc.getMstrWeeks(weekType, values, values);
                    values = answers.searchStartDate;
                }
            }
            promptsAnswerXML += answer.replace("#constant-answer#", values);
        });
        //Object Prompt, Elements Prompt
        var promptOnes = mstrContainer.find(".mstr-one");
        angular.forEach(promptOnes, function(child) {
            child = angular.element(child);
            if (child.hasClass("mstr-radio")) {
                promptsAnswerXML += child.find("input[type=radio]:checked").val();
            } else {
                promptsAnswerXML += child.find("option:selected").val();
            }
        });
        var promptMulti = mstrContainer.find(".mstr-multi");
        angular.forEach(promptMulti, function(child) {
            child = angular.element(child);
            promptsAnswerXML += child.data("prefix");
            var answerCount = 0;
            var childCheckboxes = child.find("input[type=checkbox]:checked");
            angular.forEach(childCheckboxes, function(sub_child) {
                answerCount++;
                promptsAnswerXML += angular.element(sub_child).val();
            });
            promptsAnswerXML += child.data("suffix");
            promptsAnswerXML = promptsAnswerXML.replace(/#objects-answerCount#/g, answerCount);
        });
        //Expression Prompt
        var expressionPrompt = mstrContainer.find(".expression-prompt");
        angular.forEach(expressionPrompt, function(child) {
            child = angular.element(child);
            var values = child.find("input[type=radio]:checked").val();
            if (child.hasClass("mstr-date")) {
                var answer1 = $scope.searchStartDate;
                var answer2 = $scope.searchEndDate;
                if ($scope.searchDateType == "month") {
                    answer1 = answer1.substr(0,6);
                    answer2 = answer2.substr(0,6);
                } else if ($scope.searchDateType == "week") {
                    var weekType = "week1";
                    if (child.hasClass("week2")) {
                        weekType = "week2";
                    } else if (child.hasClass("week3")) {
                        weekType = "week3";
                    }
                    var answers = apiSvc.getMstrWeeks(weekType, $scope.searchStartDate, $scope.searchEndDate);
                    answer1 = answers.searchStartDate;
                    answer2 = answers.searchEndDate;
                }
                values = values.replace("#expression-date-answer1#", answer1);
                values = values.replace("#expression-date-answer2#", answer2);
            }
            promptsAnswerXML += values;
        });
        mstrContainer.find("#mstrReportForm #promptsAnswerXML").val("<rsl>" + promptsAnswerXML + "</rsl>");
        mstrContainer.find("#mstrReportForm").submit();
    };

    /**
     * 검색 조회 callback 함수
     *
     * @params result
     */
    $scope.search = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;
        $scope.loadReport();
    };

    /**
     * 날짜 타입 변경 callback 함수
     *
     * @param result
     */
    $scope.changeDate = function(result) {
        $scope.searchDateType = result.searchDateType;
        $scope.searchStartDate = result.searchStartDate;
        $scope.searchEndDate = result.searchEndDate;
        mstrContainer.find(".date-" + $scope.searchDateType).prop("checked",true);
        if (mstrMultiple)
            loadPrompts();
    };
}

/**
 * 외부(MSTR)에서의 세션 체크 함수
 */
function sessionCheck(mstrMenuCode) {
    var mstrDom = document.getElementsByName("mstr");
    angular.forEach(mstrDom, function(child, index) {
        var scope = angular.element(child).scope();
        scope.$apply(function() {
            scope.loadReport();
        });
    });
}
