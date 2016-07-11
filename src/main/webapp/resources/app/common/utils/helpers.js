/**
 * helpers > DateHelper
 */
var DateHelper = (function () {
    Date.prototype.ymd = function (type) {
        function pad2(n) {  // always returns a string
            return (n < 10 ? '0' : '') + n;
        }
        if (type === undefined) {
            return this.getFullYear() + '.' +
                pad2(this.getMonth() + 1) + '.' +
                pad2(this.getDate());
        } else {
            return this.getFullYear() + '-' +
                pad2(this.getMonth() + 1) + '-' +
                pad2(this.getDate());
        }
    };
    Date.prototype.ym = function () {
        return this.ymd().substring(0, 7);
    };

    return {
        /**
         * "YYYYMMDDHHMISS" -> dateObject
         * @param str
         * @returns {Date}
         */
        stringYYYYMMDDHHMISSToDate: function (str) {
            return new Date(str.replace(/^(\d{4})(\d\d)(\d\d)(\d\d)(\d\d)(\d\d)$/, '$4:$5:$6 $2/$3/$1'));
        },
        /**
         * "yyyy.mm.dd" -> date object
         * @param str
         * @returns {Date}
         */
        stringToDate: function (str) {
            var pattern = /(\d{4})\.(\d{2})\.(\d{2})/;
            return new Date(str.replace(pattern, '$1-$2-$3'));
        },
        /**
         * "yyyymmdd" -> date object
         * @param str
         * @returns {Date}
         */
        stringToDateObject: function (str) {
            var pattern = /(\d{4})(\d{2})(\d{2})/;
            return new Date(str.replace(pattern, '$1-$2-$3'));
        },
        /**
         * separator 를 기준으로 paramString을 변환한다.
         * "yyyymmdd" -> "yyyy-mm-dd" (separator: -)
         * @param str
         * @returns {string}
         */
        stringToYmdStr: function (str, separator) {
            return (str.substring(0, 4) + separator + str.substring(4, 6) + separator + str.substring(6, 8));
        },
        /**
         * "yyyymmdd" -> "yy.mm.dd" TODO refactoring with separator
         * @param str
         * @returns {string}
         */
        stringToYmdStr2: function (str) {
            return (str.substring(2, 4) + '.' + str.substring(4, 6) + '.' + str.substring(6));
        },
        /**
         * "yyyymmx" -> "yyyy.mm-x"
         *  yyyy:년도, mm:월, x:주차
         * @param str
         * @returns {string}
         */
        stringToYmdStr3: function (str) {
            if(str){
                return (str.substring(0, 4) + '.' + str.substring(4, 6) + '-' + str.substring(6)+'주차');
            }
            return '-';
        },
        /**
         * "yyyymmdd" -> "yyyy.mm.dd"
         * @param str
         * @returns {string}
         */
        stringToYmdStr4: function (str) {
            return (str.substring(0, 4) + '.' + str.substring(4, 6) + '.' + str.substring(6));
        },
        /**
         * date(yyyy.MM.dd)를 파라미터 String(yyyyMMdd)으로 변환한다.
         * @param date (dateObject || dateString)
         * @returns {*}
         */
        dateToParamString: function (date) {
            var returnDate = date;
            if (typeof date === 'object') {
                returnDate = date.ymd();
            }
            return returnDate.replace(/\./gi, "");
        },
        isValid: function(date, pattern) {
            return moment(date, pattern).isValid();
        },
        /**
         * unix timestamp 을 반환한다.
         * @returns {number}
         */
        timestamp: function () {
            return Math.round(+new Date());
        },
        /**
         * 현재 날짜를 'YYYY.MM.DD' 형식으로 출력한다.
         * @returns {*}
         */
        getCurrentDate: function () {
            return moment().format('YYYY.MM.DD');
            //return new Date().ymd();
        },
        /**
         * 두 날짜간의 간격 출력
         * @Return {number}
         */
        diff: function(from, to) {
          return moment(to, 'YYYYMMDD').diff(moment(from, 'YYYYMMDD'), 'days');
        },
        /**
         * 현재 날짜를 패턴별로 출력한다.
         * @returns {*}
         */
        getCurrentDateByPattern: function (pattern, locale) {
            if(locale){
                return moment().locale(locale).format(pattern);
            }
            return moment().format(pattern);
        },
        /**
         * 몇분전 시간 조회.
         * moment.js 제약사항. milliseconds 단위로 변환 후 call subtract
         * @returns {*}
         */
        getPreviousMin : function(mins, pattern) {
            var milliseconds = 1000 * 60 * mins;
            return moment().subtract(milliseconds, 'milliseconds').format(pattern);
        },
        /**
         * 몇분후 시간 조회.
         * moment.js 제약사항. milliseconds 단위로 변환 후 call subtract
         * @returns {*}
         */
        getAfterMin : function(mins, pattern) {
            var milliseconds = 1000 * 60 * mins;
            return moment().add(milliseconds, 'milliseconds').format(pattern);
        },
        /**
         * 몇일전 날짜 조회.
         * @returns {*}
         */
        getPreviousDate : function(days, pattern) {
            return moment().subtract(days, 'days').format(pattern);
        },
        /**
         * dateString을 기준으로 이전일을 구한 뒤 pattern 형태로 반환.
         * @returns {*}
         */
        getPreviousDateFromDate : function(dateString, days, pattern) {
            return moment(this.stringToDate(dateString)).subtract(days, 'days').format(pattern);
        },
        /**
         * 몇개월전 날짜 조회.
         * @returns {*}
         */
        getPreviousMonth : function(months, pattern) {
            return moment().subtract(months, 'months').format(pattern);
        },
        /**
         * dateString을 기준으로 몇개월전 날짜 조회
         * @param months
         * @param pattern
         * @returns {*}
         */
        getPreviousMonthFromDate : function(dateString, months, pattern) {
            return moment(dateString, pattern).subtract(months, 'months').format(pattern);
        },
         /**
         * date object를 기준으로 이전일을 구한다.
         * @param day
         */
        getPreviousDayDate: function (date, day) {
            return new Date(date.setDate(date.getDate() - day));
        },
        /**
         * Date 객체를 String(yy.mm.dd) 으로 변환한다.
         * @param date
         * @returns {string}
         */
        dateObjectToYymmdd: function (dateObject) {
            var date = this.dateToParamString(dateObject);
            return [date.substring(2, 4), date.substring(4, 6), date.substring(6, 8)].join('.');
        },
         /**
         * Date 객체를 String(yyyy{separator}mm{separator}dd) 으로 변환한다.
         * @param dateObject
         * @param separator
         * @returns {string}
         */
        dateObjectToYyyymmdd: function (dateObject, separator) {
            var dateParamString = this.dateToParamString(dateObject);
            return [dateParamString.substring(0, 4), dateParamString.substring(4, 6), dateParamString.substring(6, 8)].join(separator);
        },
        /**
         * 일 단위 차이의 milliseconds를 구한다.
         * @param criterionDate
         * @param day
         * @returns {*}
         */
        getDailyDiffMs: function (criterionDate, day) {
            var returnDate = new Date(criterionDate.getTime());  // deep copy
            return returnDate.setDate(returnDate.getDate() - day);
        },
         /**
         * 주 단위 차이의 milliseconds를 구한다.
         * @param criterionDate
         * @param week
         * @returns {*}
         */
        getWeeklyDiffMs: function (criterionDate, week) {
            var returnDate = new Date(criterionDate.getTime());
            return returnDate.setDate(returnDate.getDate() - (7 * week));
        },
        /**
         * 월 단위 차이의 milliseconds 를 구한다.
         * @param criterionDate
         * @param month
         * @returns {*}
         */
        getMonthlyDiffMs: function (criterionDate, month) {
            var returnDate = new Date(criterionDate.getTime());
            return returnDate.setMonth(returnDate.getMonth() - month);
        },
         /**
         * dateObject를 기준으로 해당 주의 월요일,일요일 정보를 반환한다.
         * @param dateObject
         * @returns {*[]}
         */
        getWeekendRange: function (dateObject) {
            var basicDate = new Date(dateObject);
            var monday = new Date(basicDate.getFullYear(), basicDate.getMonth(), basicDate.getDate());
            monday = new Date(monday.getTime() - (monday.getDay() > 0 ? (monday.getDay() - 1) * 1000 * 60 * 60 * 24 : 6 * 1000 * 60 * 60 * 24));
            var sunday = new Date(monday.getTime() + 1000 * 60 * 60 * 24 * 7 - 1);

            return {
                mondayYmd: this.dateObjectToYymmdd(monday),
                sundayYmd: this.dateObjectToYymmdd(sunday)
            };
        },
        checkGridEditable: function (searchDateType, rowData, currentDate) {
            if (searchDateType === 'month') {
                if (rowData.mappStrdDt >= currentDate.substring(0, 6)) {
                    return false;
                }
            } else if (searchDateType === 'week') {
                if (rowData.mappStrdDt > currentDate) {
                    return false;
                }
            } else {
                if (rowData.mappStrdDt >= currentDate) {
                    return false;
                }
            }
            return true;
        },
        /**
         * 지나간 일요일 날짜 리턴(현재가 일요일일경우 1주일전 일요일) - cookatrice
         * @returns {*}
         */
        getPastSundayDate: function () {
            var today = new Date();
            var diff = today.getDay() == 0 ? 7 : today.getDay();

            return DateHelper.getPreviousDate(diff, 'YYYY.MM.DD');
        }
    };
})();

/**
 * helpers > StringHelper
 */
var StringHelper = (function () {
    return {
        startWith: function (orgStr, str) {
            return str.length > 0 && orgStr.substring(0, str.length) === str;
        },
        isNotEmpty : function(_str){
            var obj = String(_str);
            if(obj == null || obj == undefined || obj == 'null' || obj == 'undefined' || obj == '' )
                return false;
            else
                return true;

        }
    };
})();

/**
 * helpers > EventBindingHelper
 */
var EventBindingHelper = (function () {

    function getYesterday() {
        var newDate = new Date();
        var yy = newDate.getFullYear();
        var mm = newDate.getMonth() + 1;
        var dd = newDate.getDate() - 1;

        return yy + '.' + mm + '.' + dd;
    }

    return {
       /**
         * datepicker 초기화
         * @param element
         * @param changeDateCallback
         */
        initDatePicker: function (element, changeDateCallback) {
            element.find('.input-append.date').datepicker({
                format: 'yyyy.mm.dd',
                autoclose: true,
                weekStart: 1,
                endDate: getYesterday()  //disable select date from today(include today)
            }).on('changeDate', function (ev) {
                if ($.isFunction(changeDateCallback)) {
                    changeDateCallback(ev);
                }
            });
        },

        /**
         * 요략 리포트 지표 팝업 초기화
         * @param element
         * @param contentHtml
         * @param closeCallbackFunc
         */
        initSummaryReportMeasurePopup: function (element, contentHtml, closeCallbackFunc) {
            var $el = element.find('.indicator-select');
            $el.popover({
                animation: true,
                placement: 'bottom',
                content: contentHtml,
                html: true,
                delay: { show: 100, hide: 100 }
            }).click(function (e) {
                if (element.find('.control-area .close').size()) {
                    return;
                }

                // 닫기버튼 바인딩
                $(this).popover('show');
                element.find('.control-area')
                    .prepend('<button type="button" class="close">×</button>')
                    .find('.close').click(function () {
                        closeCallbackFunc();
                        $el.popover('hide');
                    });

                e.preventDefault();
            });
        },

        /**
         * 레포트 사이드바 바인딩
         */
        initReportSidebar: function () {
            /*==Left Navigation Accordion ==*/
            if ($.fn.dcAccordion) {
                $('#nav-accordion').dcAccordion({
                    eventType: 'click',
                    autoClose: true,
                    saveState: true,
                    disableLink: true,
                    speed: '300',
                    showCount: false,
                    autoExpand: true,
                    classExpand: 'dcjq-current-parent'
                });
            }
            var $leftNav = $(".leftside-navigation");
            /*== Nice Scroll ==*/
            if ($.fn.niceScroll) {
                $leftNav.niceScroll({
                    cursorcolor: "#b8b8b8",
                    cursorborder: "0px solid #fff",
                    cursorborderradius: "0px",
                    cursorwidth: "3px"
                });

                $leftNav.getNiceScroll().resize();
                if ($('#sidebar').hasClass('hide-left-bar')) {
                    $leftNav.getNiceScroll().hide();
                } else {
                    $leftNav.getNiceScroll().show();
                }

                $(".right-stat-bar").niceScroll({
                    cursorcolor: "#1FB5AD",
                    cursorborder: "0px solid #fff",
                    cursorborderradius: "0px",
                    cursorwidth: "3px"
                });
            }

            /*==Collapsible==*/
            $('.widget-head').bind('click', function (e) {
                var widgetElem = $(this).children('.widget-collapse').children('i');
                $(this).next('.widget-container')
                    .slideToggle('slow');
                if ($(widgetElem).hasClass('ico-minus')) {
                    $(widgetElem).removeClass('ico-minus');
                    $(widgetElem).addClass('ico-plus');
                } else {
                    $(widgetElem).removeClass('ico-plus');
                    $(widgetElem).addClass('ico-minus');
                }
                e.preventDefault();
            });

            /*== Sidebar Toggle ==*/
            $(".leftside-navigation .sub-menu > a").bind('click', function () {
                var o = ($(this).offset());
                var diff = 80 - o.top;
                if (diff > 0)
                    $leftNav.scrollTo("-=" + Math.abs(diff), 500);
                else
                    $leftNav.scrollTo("+=" + Math.abs(diff), 500);
            });

            $('.sidebar-toggle-box .fa-bars').unbind('click').bind('click', function (e) {
                $(".leftside-navigation").niceScroll({
                    cursorcolor: "#1FB5AD",
                    cursorborder: "0px solid #fff",
                    cursorborderradius: "0px",
                    cursorwidth: "3px"
                });

                $('#sidebar').toggleClass('hide-left-bar');
                if ($('#sidebar').hasClass('hide-left-bar')) {
                    $leftNav.getNiceScroll().hide();
                } else {
                    $leftNav.getNiceScroll().show();
                }

                $('#main-content').toggleClass('merge-left');
                e.stopPropagation();
                if ($('#container').hasClass('open-right-panel')) {
                    $('#container').removeClass('open-right-panel');
                }
                if ($('.right-sidebar').hasClass('open-right-bar')) {
                    $('.right-sidebar').removeClass('open-right-bar');
                }

                if ($('.header').hasClass('merge-header')) {
                    $('.header').removeClass('merge-header');
                }
            });
            $('.toggle-right-box .fa-bars').bind('click', function (e) {
                $('#container').toggleClass('open-right-panel');
                $('.right-sidebar').toggleClass('open-right-bar');
                $('.header').toggleClass('merge-header');

                e.stopPropagation();
            });

            $('.header,#main-content,#sidebar').bind('click', function () {
                if ($('#container').hasClass('open-right-panel')) {
                    $('#container').removeClass('open-right-panel');
                }
                if ($('.right-sidebar').hasClass('open-right-bar')) {
                    $('.right-sidebar').removeClass('open-right-bar');
                }

                if ($('.header').hasClass('merge-header')) {
                    $('.header').removeClass('merge-header');
                }
            });
        },

        /**
         * 대시보드 초기화
         */
        initDashboard: function () {
            var $leftNav = $(".leftside-navigation");
            if ($.fn.dcAccordion) {
                $('#nav-accordion').dcAccordion({
                    eventType: 'click',
                    autoClose: true,
                    saveState: true,
                    disableLink: true,
                    speed: '300',
                    showCount: false,
                    autoExpand: true,
                    classExpand: 'dcjq-current-parent'
                });
            }

            if ($.fn.niceScroll) {
                $(".bookmark-area").niceScroll({
                    cursorcolor: "#b8b8b8",
                    cursorborder: "0px solid #fff",
                    cursorborderradius: "0px",
                    cursorwidth: "3px"
                });

                $leftNav.getNiceScroll().resize();
                if ($('#sidebar').hasClass('hide-left-bar')) {
                    $leftNav.getNiceScroll().hide();
                } else {
                    $leftNav.getNiceScroll().show();
                }

                $(".right-stat-bar").niceScroll({
                    cursorcolor: "#1FB5AD",
                    cursorborder: "0px solid #fff",
                    cursorborderradius: "0px",
                    cursorwidth: "3px"
                });

                $(".bookmark-area").getNiceScroll().show();
            }

            /*== Sidebar Toggle ==*/
            $(".leftside-navigation .sub-menu > a").bind('click', function () {
                var o = ($(this).offset());
                var diff = 80 - o.top;
                if (diff > 0)
                    $leftNav.scrollTo("-=" + Math.abs(diff), 500);
                else
                    $leftNav.scrollTo("+=" + Math.abs(diff), 500);
            });

            $('.sidebar-toggle-box .fa-bars').unbind('click').bind('click', function (e) {
                $leftNav.niceScroll({
                    cursorcolor: "#1FB5AD",
                    cursorborder: "0px solid #fff",
                    cursorborderradius: "0px",
                    cursorwidth: "3px"
                });

                $('#sidebar').toggleClass('hide-left-bar');
                if ($('#sidebar').hasClass('hide-left-bar')) {
                    $leftNav.getNiceScroll().hide();
                } else {
                    $leftNav.getNiceScroll().show();
                }
                $('#main-content').toggleClass('merge-left');
                e.stopPropagation();
                if ($('#container').hasClass('open-right-panel')) {
                    $('#container').removeClass('open-right-panel');
                }
                if ($('.right-sidebar').hasClass('open-right-bar')) {
                    $('.right-sidebar').removeClass('open-right-bar');
                }

                if ($('.header').hasClass('merge-header')) {
                    $('.header').removeClass('merge-header');
                }
            });
            $('.toggle-right-box .fa-bars').bind('click', function (e) {
                $('#container').toggleClass('open-right-panel');
                $('.right-sidebar').toggleClass('open-right-bar');
                $('.header').toggleClass('merge-header');

                e.stopPropagation();
            });

            $('.header,#main-content,#sidebar').bind('click', function () {
                if ($('#container').hasClass('open-right-panel')) {
                    $('#container').removeClass('open-right-panel');
                }
                if ($('.right-sidebar').hasClass('open-right-bar')) {
                    $('.right-sidebar').removeClass('open-right-bar');
                }

                if ($('.header').hasClass('merge-header')) {
                    $('.header').removeClass('merge-header');
                }
            });
        },
        /**
         * 즐겨찾기 bar 초기화
         * @param element
         */
        initFavoriteBar: function (element) {
            $('.bookmark-list li > a.service-item').unbind('click');

            // from script.js
            if ($.fn.dcAccordion) {
                element.find('.bookmark-list').dcAccordion({
                    eventType: 'click',
                    autoClose: true,
                    saveState: true,
                    disableLink: true,
                    speed: '300',
                    showCount: false,
                    autoExpand: true,
                    classExpand: 'dcjq-current-parent'
                });
            }

            /*== Nice Scroll ==*/
            if ($.fn.niceScroll) {
                element.find(".bookmark-area").niceScroll({
                    cursorcolor: "#b8b8b8",
                    cursorborder: "0px solid #fff",
                    cursorborderradius: "0px",
                    cursorwidth: "3px"
                });

                element.find(".bookmark-area").getNiceScroll().resize();
                if (element.find('#sidebar').hasClass('hide-left-bar')) {
                    element.find(".leftside-navigation").getNiceScroll().hide();
                    element.find(".bookmark-area").getNiceScroll().hide();
                } else {
                    element.find(".bookmark-area").getNiceScroll().show();
                }
            }

            // 즐겨찾기 추가 바인딩
            element.find("#addBookmark").unbind('click').bind('click', function () {
                angular.element("#bookmarkModal").modal();
            });
        }
    };
})();

/**
 * helpers > CalcHelper
 */
var CalcHelper = (function () {
    return {

        /**
         * 두 숫자의 퍼센티지를 구한다.
         * @param original
         * @param discounted
         * @returns {string|*}
         */
        percentageTwoNumbers: function (original, discounted) {
            return (((discounted - original) / original) * 100).toFixed(1);
        },
        sliceByMinutes: function (original, expected) {
            if (!$.isNumeric(original) && $.isNumeric(expected)) {
                return 0;
            }
            return ((expected - original) / 60).toFixed(3);
        }
    };
})();

/**
 * helpers > FormatHelper
 */
var FormatHelper = (function () {
    /**
     * Number의 prototype을 확장하여 numberFormat을 제공한다.
     * ex)
     * 1234..numberFormat();           // "1,234"
     * 12345..numberFormat(2);         // "12,345.00"
     * 123456.7.numberFormat(3, 2);    // "12,34,56.700"
     * 123456.789.numberFormat(2, 4);  // "12,3456.79"
     *
     * @param integer n: 소수 길이
     * @param integer x: 단위 길이
     */
    Number.prototype.numberFormat = function (n, x) {
        var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\.' : '$') + ')';
        return this.toFixed(Math.max(0, ~~n)).replace(new RegExp(re, 'g'), '$&,');
    };
    /**
     * Number의 for loop 기능을 대체한다.
     * ex)
     * 5.times(function (i) {alert(this + ' ' + i);});
     *
     * @param integer iterator: 반복 횟수 숫자.
     * @param Function context: 함수.
    */
    Number.prototype.times = function(iterator, context) {
        function toInteger(o) {
            var n = +o,
                r = n;
            if (isNaN(n)) {
                r = 0;
            } else if (n !== 0 && isFinite(n)) {
                r = (n < 0 ? -1 : 1) * Math.floor(Math.abs(n));
            }
            return r;
        }
        function isFunction(o) {
            return Object.prototype.toString.call(o) === '[object Function]';
        }
        if (!isFunction(iterator)) {
            throw new TypeError(iterator + ' is not a function');
        }
        var i = 0, t =  Math.abs(toInteger(this));
        while (i < t) {
            iterator.call(context, i);
            i += 1;
        }
        return t;
    };

    return {
        /**
         * num 왼쪽에 character를 length 만큼 padding을 준다.
         *
         * padLeft(100, 3, '0') -> 000100
         * @param num
         * @param length
         * @param character
         * @returns {string}
         */
        padLeft: function (num, length, character) {
            num = num.toString();
            return new Array(length - num.length + 1).join(character || '0') + num;
        }
    };
})();

/**
 * helpers > AjaxHelper
 */
var AjaxHelper = (function () {
    return {
        /**
         * ajax를 이용하여 synchronous 방식으로 json을 가져온다.
         * @param url
         * @param data
         * @returns {*}
         */
        syncGetJSON: function (url, data) {
            var resp = $.ajax({
                type: 'GET',
                url: url,
                data: data,
                dataType: 'json',
                cache: false,
                timeout: 10000,
                async: false
            });

            if (resp.status == 200 && resp.responseText) {
                return $.parseJSON(resp.responseText);
            } else {
                return null;
            }
        }
    };
})();

/**
 * Url 관련 헬퍼
 */
var UrlHelper = (function () {
    return {
        buildUrl: function (url, params) {
            if (!params) return url;
            var parts = [];
            angular.forEach(params, function (value, key) {
                if (value === null || angular.isUndefined(value)) {
                    return;
                }
                parts.push(encodeURI(key) + '=' + encodeURI(value));
            });
            if (parts.length > 0) {
                url += (url.indexOf('?') == -1 ? '?' : '&') + parts.join('&');
            }
            return url;
        }
    };
})();

/**
 * helpers > DomHelper
 */
var DomHelper;
DomHelper = (function () {
    var FOR_LOOK_GOOD_WIDTH = 322;
    var SIDE_BAR_DEFAULT_WIDTH = 250;

    function mergeColumn(id, simpleColMerge) {
        var REPEAT = simpleColMerge.length;

        for (var i = 0; i < REPEAT; i++) {
            var row = simpleColMerge[i].row + 1;        //position of <tr>tag. header's row is 1. so plus 1 is right.
            var start = simpleColMerge[i].start - 1;    //position of <th>tag. <th>tag start zero. so minus 1 is right.
            var end = simpleColMerge[i].end - 1;
            var diff = (end - start + 1);

            $('#' + id + ' .pvtTable tr:nth-child(' + row + ') th').each(function (index, obj) {
                if (index > start && index <= end)
//                    $(obj).remove();
                    $(obj).hide();
            });
            $('#' + id + ' .pvtTable tr:nth-child(' + row + ') th:nth-child(' + (start + 1) + ')').attr('colspan', diff);
        }
    }

    function hidePivotTbaleOption(id) {
        $('#' + id + ' > table').find('tr').each(function (index, domElement) {
            if (index == 0 || index == 1)
                $(domElement).css("display", "none");
        });
        $('.pvtAxisContainer.pvtRows').hide();
    }

    function extendTableWidth(id, model, extendWidth) {
        var $oriTable = $('#' + id + ' .pvtTable');
        var $fakeTable = $('#' + id + '_h .fixedTableHeader');

        var curTableWidth = $oriTable.width();
        var modelLength = model.length;
        var totalTableColumnLength = 0;
        for (var i = 0; i < modelLength; i++) {
            totalTableColumnLength += model[i].width;
        }

        if (totalTableColumnLength >= curTableWidth) {
            $oriTable.css('width', (parseFloat(totalTableColumnLength) + extendWidth));
            $fakeTable.css('width', (parseFloat(totalTableColumnLength) + extendWidth));
        }
    }

    function setMinCellWidth(id, model, minCellWidth) {
        var $oriTable = $('#' + id + ' .pvtTable');
        var $fakeTable = $('#' + id + '_h .fixedTableHeader');

        var fakeTableCellCount = $fakeTable.find('tr > th').size();

        var modelLength = model.length;
        var totalTableColumnLength = 0;
        for (var i = 0; i < modelLength; i++) {
            totalTableColumnLength += model[i].width;
        }
        var extendWidth = ((fakeTableCellCount - modelLength) * minCellWidth);

        $oriTable.css('width', (parseFloat(totalTableColumnLength) + extendWidth));
        $fakeTable.css('width', (parseFloat(totalTableColumnLength) + extendWidth));
    }

    /**
     * 고정 헤더 테이블 헤더 엘리먼트를 구하는 함수
     */
    function getFixedHeaderElement(fixedHeaderId) {
        var table;
        // 기존에 고정 헤더로 사용된 엘리먼트를 재사용,
        table = document.querySelector('#' + fixedHeaderId);

        if (table) {
            // 고정 헤더 재사용시 기존 자식 노드를 모두 삭제해준다.
            while (table.hasChildNodes()) {
                table.removeChild(table.lastChild);
            }
            return table;
        }
        table = document.createElement('table');
        table.id = fixedHeaderId;
        table.className = 'pvtTable fixed-header';
        return table;
    }

    return {
        empty: function(element) {
            if (!element) {
                return;
            }
            var i;
            for (i = element.childNodes.length - 1; i >= 0; i--)
                element.removeChild(element.childNodes(i));
        },
        /**
         * Jqrid에서 입력한 문자값과 cell 값이 같으면 문자열의 시작 인덱스를 지정하여 반환
         * @param cellValue, stIndex, conStr
         * @returns cellValue
         */
        subCell: function (cellValue, stIndex, conStr) {
            if (cellValue == conStr) {
                return cellValue.substring(stIndex, cellValue.length);
            } else {
                return cellValue;
            }
        },
        /**
         * pivotUI의 드래그 기능 등 추가 옵션 기능을 제거한다.
         */
        hidePivotTableOption: hidePivotTbaleOption,
        /**
         * 왼쪽 슬라이드 메뉴처리
         * @param
         * @returns
         */
        toggleSlide: function () {
            var $sideBar = angular.element("#sidebar");
            if ($sideBar.hasClass('slide_hide')) {
                $sideBar.removeClass('slide_hide');
                $sideBar.find('.slide_btn i').removeClass('fa-angle-right');
                $sideBar.find('.slide_btn i').addClass('fa-angle-left');
                $('.container-wrap').removeClass('slide_hide');
            } else {
                $sideBar.addClass('slide_hide');
                $sideBar.find('.slide_btn i').removeClass('fa-angle-left');
                $sideBar.find('.slide_btn i').addClass('fa-angle-right');
                $('.container-wrap').addClass('slide_hide');
            }
        },
        /**
         * redraw pivot table header one-line to fix.
         */
        getDateList: function (id) {
            var tmpDateList = [];
            $('#' + id + ' .pvtTable tr.head-group th.pvtColLabel').each(function (i, obj) {
                tmpDateList.push($(obj).text());
            });
            return tmpDateList;
        },

        /**
         * substr custom order header
         */
        customOrder_h: function (list, cut) {
            var listLength = list.length;
            var tmpList = [];
            for (var i = 0; i < listLength; i++) {
                tmpList.push(list[i].substring(cut, list[i].length));
            }
            return tmpList;
        },

        /**
         * redraw pivot table header one-line to fix.
         * 2014-10-31 cookatrice
         */
        reDrawPivotTableHeader: function (config) {
            var id = config.id;
            var id_h = config.id + '_h';
            var simpleColMerge = (config.simpleColMerge === undefined) ? false : config.simpleColMerge;
            var model = config.modelData;
            var customOrder = (config.customOrder === undefined) ? 0 : config.customOrder;
            var extendWidth = (config.extendWidth === undefined) ? false : config.extendWidth;
            //var minCellWidth = (config.minCellWidth === undefined) ? false : config.minCellWidth;

            //clear old fixed header
            $('#' + id_h + ' .fixedTableHeader tr .fixCategory').remove();

            //default setting
            $('#' + id + ' .pvtRendererArea table.pvtTable').pivotBoard();
            $('#' + id + ' .pvtTotal, .pvtTotalLabel, .pvtGrandTotal').remove();

            //category column text align and remove custom order number
            $('#' + id + ' th.pvtRowLabel').each(function (i, obj) {
                $(obj).css('text-align', 'left');
                if (customOrder > 0) {
                    if (id == "ocbDauPivot") {
                        $(obj).html($(obj).text().substring(customOrder));
                    } else {
                        $(obj).text($(obj).text().substring(customOrder));
                    }
                }
            });

            //scroll setting
            $('.data-area:visible').on('scroll', function () {
                $('#' + id_h + ' .fixedTableHeader').css('top', $(this).scrollTop());
            });

            var sideBarWidth = $("#sidebar").hasClass("slide_hide") ? SIDE_BAR_DEFAULT_WIDTH : 0;
            var pivotTableWidth = $('#' + id + ' .pvtTable').width();
            var curWindowWidth = $(window).width() + sideBarWidth - FOR_LOOK_GOOD_WIDTH;

            if (pivotTableWidth < curWindowWidth) {
                pivotTableWidth = curWindowWidth;
            }
            $('#' + id_h + ' .fixedTableHeader').css('width', pivotTableWidth);   //fix header table width
            $('#' + id + ' .pvtTable').css('width', pivotTableWidth);             //pivot header table width

            var MODEL_INDEX = model.length - 1;
            //draw new fixed header (reverse)
            var $tableHeader = $('#' + id_h + ' .fixedTableHeader tr');
            for (var j = MODEL_INDEX; j >= 0; j--) {
                $tableHeader.prepend('<th>' + model[j].name + '</th>');
                $tableHeader.find('th:first-child').css('width', model[j].width);
                $tableHeader.find('th:first-child').addClass('fixCategory');
            }

            //pivot table header and body control
            var $tableBody = $('#' + id + ' .pvtTable');
            $tableBody.find('tr:nth-child(1) th:nth-child(1)').remove();
            $tableBody.find('tr').eq(1).remove(); // instead of " $tableBody.find('tr:nth-child(2)').remove() ". iPad's safari's selector problem;
            $tableBody.find('tr:nth-child(1) th').each(function (i, obj) {
                $(obj).attr('rowspan', 1);
            });

            //reset pivot table's colspan
            for (var i = (MODEL_INDEX); i >= 0; i--) {
//            for(var i=(MODEL_INDEX+1); i > 0; i--){
//                $tableBody.find('tr th:nth-child('+i+')').each(function (i, obj) {$(obj).attr('colspan', 1)});    //nth-child's index start 1.
                $tableBody.find('tr th').each(function (i, obj) {
                    $(obj).attr('colspan', 1);
                });   //.eq()'s index start 0.
            }

            if (simpleColMerge) {
                mergeColumn(id, simpleColMerge);
            }

            //replace and append pivot table header's name (addClass 'pivotHeaderCategory' for downloaded excel file's header.)
            $tableBody.find('tr:first-child th:first-child').text(model[MODEL_INDEX].name).removeClass('pvtAxisLabel').addClass('pivotHeaderCategory');   //first category name - default <th>변경
            for (var k = (MODEL_INDEX - 1); k >= 0; k--) {
                $tableBody.find('tr:nth-child(1)').prepend('<th>' + model[k].name + '</th>');
                $tableBody.find('tr:nth-child(1) th:nth-child(1)').addClass('pivotHeaderCategory');
            }

            //set pivot table body's column size
            for (var m = MODEL_INDEX; m >= 0; m--) {
//                $tableBody.find('tr:first-child th:nth-child('+(i+1)+')').css('width',model[i].width);
                $tableBody.find('tr:first-child th').eq(m).css('width', model[m].width);
            }

            //hide pivot table default header option
            hidePivotTbaleOption(id);

            //set min table width
            if (extendWidth) {
                extendTableWidth(id, model, extendWidth);   //id, model, extendWidth
            }
        },

        /**
         * set the pivot table minium cell size
         * @param config
         */
        reDrawPivotTableMinCellWidth: function (config) {
            var id = config.id;
            var model = config.modelData;
            var minCellWidth = (config.minCellWidth === undefined) ? false : config.minCellWidth;

            //set min table's cell width
            if (minCellWidth) {
                setMinCellWidth(id, model, minCellWidth);   //id, model, minCellWidth
            }
        },

        /**
         * create Pivot Table Dynamic Header
         * @param pivotId QuerySelector for Pivot Table
         * @param pivotHeaderParentSelector for Parent Element Pivot Table Header should locate
         */
        createDynamicHeaderForPivot: function (pivotId) {
            var $pivotWrapper, $pivotHeaderParent, $pivot, table, fixedHeaderId = pivotId + 'FixedHeader';
            $pivotWrapper = angular.element('#' + pivotId);
            $pivot = $pivotWrapper.find('.multi-header-pivot');

            $pivotHeaderParent = angular.element('.data-area:visible');
            // 기존에 고정 헤더로 사용된 엘리먼트를 재사용, 기존 헤더가 없으면 새로 생성
            table = getFixedHeaderElement(fixedHeaderId);
            // pivot table의 헤더 부분을 copy 해서 header 테이블에 추가
            $pivotWrapper.find('tr').each(function (index, tr) {
                var $tr, newTr;
                $tr = angular.element(tr);
                if ($tr.hasClass('pivot-header')) {
                    newTr = document.createElement('tr');
                    $tr.children().each(function (index, th) {
                        var newTh;
                        //$th = angular.element(th);
                        newTh = th.cloneNode(true);
                        // 브라우저 확대/축소시에 문제가 없도록 하기 위해 두 테이블 모두 헤더 width를 설정해준다.
                        newTh.style.width = $(th).outerWidth() + 'px';
                        $(th).outerWidth(newTh.style.width);
                        newTh.textContent = $(th).text();
                        newTr.appendChild(newTh);
                    });
                    table.appendChild(newTr);
                }
            });

            // table 의 width를 잡아준다. 잡아주지 않으면 사이즈가 작아질 경우, 테이블이 변함
            $pivot.css('width', $pivot.width() + 'px');
            $(table).css('width', $pivot.width() + 'px');

            //scroll setting
            // TODO 횡 스크롤에서 문제 발생해서 스크롤 이벤트로 처리, 결국 돌아왔다. ㅠㅠ
            $('.data-area:visible').on('scroll', function () {
                $('#' + pivotId + 'FixedHeader').css('top', $(this).scrollTop());
            });

            // 고정 헤더를 헤더 상위 엘리먼트에 자식으로 추가
            $pivotHeaderParent.prepend(table);
        },

        /**
         * resize the olap pivot table width at first time - cookatrice
         * @param pivotId
         */
        fitDynamicHeaderForPivot: function (pivotId) {
            var olapPivotTableWidth = $('#' + pivotId + ' .multi-header-pivot').width();
            var curWidth = $(window).width();
            var sideBarWidth = $('#sidebar').hasClass('slide_hide') ? SIDE_BAR_DEFAULT_WIDTH : 0;
            var newWidth = curWidth + sideBarWidth - FOR_LOOK_GOOD_WIDTH;
            if (olapPivotTableWidth < newWidth) {
                olapPivotTableWidth = newWidth;
                $('#' + pivotId + ' .multi-header-pivot').width(olapPivotTableWidth + 'px');
                $('#' + pivotId + 'FixedHeader').width(olapPivotTableWidth + 'px');
            }
        },

        /**
         * olap pivot table's two-line header to make one-line header. - cookatrice
         * @param pivotId
         */
        makeOneLineDynamicHeader: function (pivotId, columnWidth){
            var $tableHeader = $('#' + pivotId + 'FixedHeader');
            var $tableBody = $('#' + pivotId + ' .multi-header-pivot');

            var sumColumnWidth = 0;
            var tmpHeaderText = [];
            $tableHeader.find('tr:nth-child(2) th').each(function(i,obj){
                sumColumnWidth += $(obj).width();
                tmpHeaderText.push($(obj).text());
            });
            $tableHeader.find('tr').eq(1).remove();
            $tableHeader.find('tr th:first-child').remove();//remove first th
            $tableHeader.find('tr th:first-child').remove();//remove second th


            var avgHeaderWidth = sumColumnWidth / _.size(_.compact(tmpHeaderText));
            avgHeaderWidth = (columnWidth === undefined) ? Math.ceil(avgHeaderWidth) : columnWidth;

            var $tmpHeader = $('#' + pivotId + 'FixedHeader tr');
            $tmpHeader.find('th').each(function(i,obj){
                $(obj).width('');
                $(obj).attr('data-col',i);
            });
            _.each(_.compact(tmpHeaderText).reverse(), function(element){
                $tmpHeader.prepend('<th>' + element + '</th>');
                $tmpHeader.find('th:first-child').css('width', avgHeaderWidth);
            });

            $tableBody.find('tr:nth-child(1) th:nth-child(1)').remove();//remove first th
            $tableBody.find('tr:nth-child(1) th:nth-child(1)').remove();//remove second th
            $tableBody.find('tr').eq(1).remove(); // instead of " $tableBody.find('tr:nth-child(2)').remove() ". iPad's safari's selector problem;

            var $tmpBody = $('#' + pivotId + ' .multi-header-pivot tr:first-child');
            $tmpBody.find('th').each(function (i, obj) {
                $(obj).width('');
            });
             _.each(_.compact(tmpHeaderText).reverse(), function(element){
                $tmpBody.prepend('<th>' + element + '</th>');
                $tmpBody.find('th:first-child').css('width', avgHeaderWidth);
            });

            $tableBody.find('tr:nth-child(1) th').each(function (i, obj) {
                $(obj).attr('rowspan', 1);
            });
            $tableBody.find('tr th').each(function (i, obj) {
                $(obj).attr('colspan', 1);
            });   //.eq()'s index start 0.

            //set the data-col attr for EXCEL DOWNLOAD. this code MUST be run after redraw pivot table.
            $tmpBody.find('th').each(function (i, obj) {
                $(obj).attr('data-col', i);
            });
            $tableBody.find('tr').each(function (i, obj) {
                $(obj).find('td').each(function (i, obj) {
                    $(obj).attr('data-col', $(obj).attr('data-col') - 1);   //minus 1 for fix data-col attr's value
                });
            });
            $tableBody.attr('data-totalcol', $tableBody.attr('data-totalcol')-1);
        },

        /**
         * remove Pivot Table Dynamic Header
         * @param pivotId QuerySelector for Pivot Table
         */
        removeDynamicHeaderForPivot: function (pivotId) {
            var fixedHeaderId = pivotId + 'FixedHeader';
            var $pivot = angular.element('#' + fixedHeaderId);
            $pivot.remove();
        },

        /**
         * destroy highChart
         * when the pivot table data is none, the latest highChart remove.
         * @param highChartId
         */
        destroyHighChart: function (highChartId) {
            var highChartContainer = highChartId + 'Container';
            var $highChart = angular.element('#'+highChartContainer).highcharts();
            if($highChart){
                $highChart.destroy();
                $('#'+highChartContainer).height('0px');
            }
        },

        /**
         * resize funnel width
         * @param funnelId
         */
        resizeFunnelWidth: function (funnelId) {
            if (!angular.element("#" + funnelId)) {
                return;
            }

            var winWidth = angular.element(window).width();
            var sidebarWidth = angular.element("#sidebar").hasClass("slide_hide") ? 0 : angular.element("#sidebar").width();
            var newWidth = winWidth - sidebarWidth - 85;
            var minChildWidth = 200;
            var maxChildCount = 0;
            var floorStep = new Array(angular.element("#" + funnelId + " .floor").length);

            angular.element("#" + funnelId + " .floor").find(".custom-funnel").remove();
            angular.forEach(angular.element("#" + funnelId + " .floor"), function(child) {
                var childNodes = angular.element(child).find(".entry, .step, .exit");
                if (childNodes.length > maxChildCount) {
                    maxChildCount = childNodes.length;
                }
                var index = angular.element("#" + funnelId + " .floor").index(angular.element(child));
                floorStep[index] = new Array(2);
                floorStep[index][0] = childNodes.length / 3;
                var maxChildHeight = Math.max.apply(null, angular.element(child).find(".entry, .exit").map(function() {
                    var paddingTop = 45;
                    var minHeight = 240;
                    var summaryHeight = $(this).find(".summary").height();
                    var detailHeight = $(this).find(".detail").height();
                    if (detailHeight > 0) {
                        detailHeight += 60;
                    }
                    if (paddingTop + summaryHeight + detailHeight > minHeight) {
                        return paddingTop + summaryHeight + detailHeight;
                    } else {
                        return minHeight;
                    }
                }).get());
                floorStep[index][1] = maxChildHeight;

                // resize entry, step, exit
                childNodes.css("height", maxChildHeight);
                // resize step's detail
                angular.element(child).find(".step .summary.no-border").css("height", angular.element(child).find(".step .summary").height() + 2);
                angular.element(child).find(".step .detail").css("height", (maxChildHeight - angular.element(child).find(".step .summary").height()));
            });

            var floorStepCount = floorStep.length;
            for (var i = 0 ; i < floorStepCount; i++) {
                if (floorStep[i + 1] == null) {
                    break;
                }
                if (floorStep[i][0] < floorStep[i + 1][0]) {
                    var loopCount = floorStep[i + 1][0] - floorStep[i][0];
                    for (var j = 0; j <= loopCount; j++) {
                        var extRate = angular.element("#" + funnelId + " .floor").eq(i + 1).find(".ext-rate-temp").eq(j);
                        if (extRate.length > 0) {
                            if (j == 0) {
                                angular.element("#" + funnelId + " .floor").eq(i).find(".ext-rate").html(extRate.html());
                            } else {
                                angular.element("#" + funnelId + " .floor").eq(i).append(
                                    "<div class=\"custom-funnel\">"
                                    + "<div class=\"entry\">"
                                    + "<ul class=\"summary\"></ul>"
                                    + "</div>"
                                    + "<div class=\"step\">"
                                    + "<ul>"
                                    + "<li class=\"summary no-border\"></li>"
                                    + "<li class=\"detail\">"
                                    + "<div class=\"ext-arrow\"><img src=\"/resources/images/funnel/ext-arrow-down-tail.jpg\" /></div>"
                                    + "<div class=\"ext-rate\">" + extRate.html() + "</div>"
                                    + "</li>"
                                    + "</ul>"
                                    + "</div>"
                                    + "<div class=\"exit\">"
                                    + "<ul class=\"summary\"></ul>"
                                    + "</div>"
                                    + "</div>"
                                );
                            }
                            extRate.hide();
                        }
                    }
                    angular.element("#" + funnelId + " .floor").eq(i).find(".step .summary.no-border").css("height", angular.element("#" + funnelId + " .floor").eq(i).find(".step .summary").height() + 2);
                    angular.element("#" + funnelId + " .floor").eq(i).find(".step .detail").css("height", (floorStep[i][1] - angular.element("#" + funnelId + " .floor").eq(i).find(".step .summary").height()));
                }
            }

            if (maxChildCount > 0) {
                var newChildWidth = Math.floor(newWidth / maxChildCount) - 10;
                angular.element("#" + funnelId + " .funnel").find(".entry, .step, .exit").css("width", (newChildWidth < minChildWidth ? minChildWidth : newChildWidth));
                angular.element("#" + funnelId + " .funnel").find(".entry .detail .key, .exit .detail .key").css("width", (newChildWidth < minChildWidth ? minChildWidth : newChildWidth) - 52);
                angular.element("#" + funnelId + " .funnel").find(".entry .detail .value, .exit .detail .value").css("width", 50);

                var funnelWidth = maxChildCount * (angular.element("#" + funnelId + " .entry").width() + 10);
                if (newWidth > funnelWidth) {
                    funnelWidth = newWidth;
                }
                angular.element("#" + funnelId + " .funnel").css("width", funnelWidth);
            }
        }
    };
})();


/**
 * Chart 관련 헬퍼
 */
var ChartHelper = (function () {
    return {
        /**
         * get grid column's id, name info
         * @param gridInfo
         * @returns {*}
         */
        getGridInfo: function (gridInfo) {
            var colId = _.map(gridInfo.colModel, function (value) {
                return value.name;
            });
            var colName = gridInfo.colNames;
            return {colId:colId, colName:colName};
        }
    };
})();


/**
 * StringBuffer 관련 헬퍼
 */
var StringBuffer = function() {
    this.buffer = [];
};
StringBuffer.prototype.append = function(str) {
    this.buffer[this.buffer.length] = str;
};
StringBuffer.prototype.toString = function() {
    return this.buffer.join("");
};
