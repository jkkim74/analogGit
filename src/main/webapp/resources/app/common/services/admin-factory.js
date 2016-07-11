var factories = angular.module('app.commonFactory');

/**
 *  factories > bpmResultSvc (경영실적 상세조회)
 */
(factories.lazy || factories).factory('bpmResultSvc', function ($http) {
    return {
        /**
         * 서비스 코드 조회
         */
        getBpmSvcs: function () {
            return $http.get('/bpmSvcCd/svcs').then(function (result) {
                return result.data;
            });
        },

        /**
         * 지표 목록 조회
         * @param svcId
         * @returns {*}
         */
        getBpmCycleToGrps: function (svcId) {
            return $http.get('/bpmSvcCd/cycleToGrps?svcId=' + svcId + '&lnkgCyclCd=D').then(function (result) {
                return result.data;
            });
        },

        /**
         * 지표 조회
         * @param svcId
         * @param idxClGrpCd
         * @returns {*}
         */
        getBpmGrpToCls: function (svcId, idxClGrpCd) {
            return $http.get('/bpmSvcCd/grpToCls?svcId=' + svcId + '&idxClGrpCd=' + idxClGrpCd).then(function (result) {
                return result.data;
            });
        },

        /**
         * 주차 정보 조회
         * @param wkStcStrdYmw
         */
        getBpmWkStrds: function(wkStcStrdYmw) {
            return $http.get('/bpmSvcCd/wkStrds/' + wkStcStrdYmw).then(function (result) {
                return result.data;
            });
        },

        /**
         * 경영실적 상세조회 기본 시작일/종료일 조회
         * @param dateType
         * @returns {{startDate: *, endDate: *, startDateStr: *, endDateStr: *}}
         */
        defaultCal: function (dateType) {
            var startDate;
            var endDate;
            var isDay = (dateType == 'day');

            if(isDay) {
                startDate = new Date(DateHelper.getDailyDiffMs(new Date(), 7));// 1주일 전
                endDate = new Date(DateHelper.getDailyDiffMs(new Date, 1)); // 어제
            } else {
                startDate = new Date(DateHelper.getMonthlyDiffMs(new Date(), 1)); // 1달전
                endDate = new Date(); // 오늘
            }
            return {
                'startDate': startDate,  // Date
                'endDate': endDate,
                'startDateStr': isDay ? startDate.ymd() : startDate.ym(),  // yyyy.mm.dd || yyyy.mm
                'endDateStr': isDay ? endDate.ymd() : endDate.ym(),
                'startDateStrPlain': (dateType == 'month') ? moment(startDate).format('YYYMM') : moment(startDate).format('YYYYMMDD'), // yyyymm || yyyymmdd
                'endDateStrPlain': (dateType == 'month') ? moment(endDate).format('YYYYMM') : moment(endDate).format('YYYYMMDD')
            };
        },

        /**
         * 경영실적 일별/주별/월별 조회
         * @param wkStcStrdYmw
         */
        getBpmResultSums: function (params) {
            var url;
            if (params.dateType == 'week') {
                url = '/bpmResultSum/weekly';
            } else if (params.dateType == 'month') {
                url = '/bpmResultSum/monthly';
            } else {
                url = '/bpmResultSum/daily';
            }
            url += '?' + $.param(params);

            return $http.get(url).then(function (result) {
                return result.data;
            });
        },

        /**
         * dateType에 따른 pivot cols 반환
         * @param dateType
         * @returns {string[]}
         */
        getPivotCols: function (dateType) {
            if(dateType == 'week') {
                return ['wkStrdVal'];
            } else if(dateType == 'month') {
                return ['mthStcStrdYm'];
            } else {
                return ['dlyStrdDt'];
            }
        },

        /**
         * dateType에 따른 pivot vals 반환
         * @param dateType
         * @returns {string[]}
         */
        getPivotVals: function (dateType) {
            if(dateType == 'week') {
                return ['wkStcRsltVal'];
            } else if(dateType == 'month') {
                return ['mthStcRsltVal'];
            } else {
                return ['dlyRsltVal'];
            }
        }

    };
});

/**
 *  factories > adminReportSvc
 */
(factories.lazy || factories).factory('adminReportSvc', function () {
    /**
     * 리포트 기본 milliseconds를 구한다.
     * @param dateType
     * @param date
     * @returns {*}
     */
    function getReportDiffMs (dateType, date) {
        if (dateType == 'week') {
            return DateHelper.getWeeklyDiffMs(date, 6); // 최근 6주 (주별)
        } else if (dateType == 'month') {
            return DateHelper.getMonthlyDiffMs(date, 6); // 최근 6개월 (월별)
        } else {
            return DateHelper.getDailyDiffMs(date, 10);// 최근 10일 (일별)
        }
    }
    return {
        /**
         * 리포트 기본 시작일/종료일 조회
         *
         * @returns {{startDate: Date, endDate: Date, startDateStr: *, endDateStr: *, startDateStrPlan: *, endDateStrPlan: *}}
         */
        defaultCal: function (dateType) {
            var diffDate = getReportDiffMs(dateType, new Date());
            var startDate = new Date(diffDate);
            var endDate = DateHelper.getPreviousDayDate(new Date(), 1);     //report 조회 날짜 하루전 표시
            return {
                'startDate': startDate,  // Date
                'endDate': endDate,
                'startDateStr': moment(startDate).format('YYYY.MM.DD'),  // yyyy.mm.dd
                'endDateStr': moment(endDate).format('YYYY.MM.DD'),
                'startDateStrPlain': (dateType == 'month') ? moment(startDate).format('YYYMM') : moment(startDate).format('YYYYMMDD'), // yyyymm || yyyymmdd
                'endDateStrPlain': (dateType == 'month') ? moment(endDate).format('YYYYMM') : moment(endDate).format('YYYYMMDD')

            };
        }
    };
});

/**
 *  factories > adminSvc
 */
(factories.lazy || factories).factory('adminSvc', function ($http, $q, menuSvc, urlHandleSvc) {
    return {
        /**
         * Admin 메뉴 조회
         * @returns Array menu items
         */
        //getAdminMenuItems: function () {
        //    var categories = [];
        //    var adminMenu = menuSvc.getAdminService();
        //    angular.forEach(adminMenu.categories, function (obj) {
        //        if (obj.visibleYn != 'N') {
        //            categories.push(obj);
        //        }
        //    });
        //
        //    adminMenu.categories = categories;
        //    return adminMenu;
        //},
        /**
         * categoryCode, menuCode에 대한 Admin service, category, menu를 조회한다.
         * @param categoryCode, menuCode
         * @returns {{service: *, category: *, menu: {}}}
         */
        //getAdminByCategoryOrMenuCode: function (categoryCode, menuCode) {
        //    var adminService = menuSvc.getAdminService();
        //    var adminCategory = {};
        //    angular.forEach(adminService.categories, function (obj) {
        //        if (categoryCode == obj.code) {
        //            return adminCategory = obj;
        //        }
        //    });
        //    var adminMenu = {};
        //    angular.forEach(adminCategory.menus, function (obj) {
        //        if (menuCode == obj.code) {
        //            return adminMenu = obj;
        //        }
        //    });
        //
        //    return {
        //        service: adminService,
        //        category: adminCategory,
        //        menu: adminMenu
        //    };
        //},
        /**
         * menuUrl에 해당하는 item을 반환한다.
         * @param menuUrl
         * @returns {{category: *, menu: *, templateUrl: string}}
         */
        //getAdminMenuItemByPath: function (categoryCode, menuCode) {
        //    var categoryItem = null;
        //    var menuItem = null;
        //    var adminMenuItem = menuSvc.getAdminService();//this.getAdminMenuItems();
        //    // service에서 categoryId 기준으로 category를 찾는다.
        //    angular.forEach(adminMenuItem.categories, function (obj) {
        //        if (categoryCode == obj.code) {
        //            return categoryItem = obj;
        //        }
        //    });
        //
        //    // category에서 menuId 기준으로 menu를 찾는다.
        //    angular.forEach(categoryItem.menus, function (obj) {
        //        if (menuCode == obj.code) {
        //            return menuItem = obj;
        //        }
        //    });
        //
        //    return this.newAdminMenuItem(categoryItem, menuItem);
        //},
        /**
         * url을 정보를 이용하여 menuItem을 생성한다..
         * @returns {{service: *, category: *, menu: *, resourceUrl: string}}
         */
        urlToMenuItem: function () {
            var menuUrl = urlHandleSvc.getMenuUrl();
            return this.getItemByMenuUrl(menuUrl);
        },
        getItemByMenuUrl: function (menuUrl) {
            var serviceItem;
            var categoryItem ;
            var menuItem;
            serviceItem = menuSvc.getAdminService();
            categoryItem = _.find(serviceItem.categories, function(obj) {
                return (menuUrl.categoryCode == obj.code);
            });
            if (!categoryItem) {
                return null;
            }
            menuItem = _.find(categoryItem.menus, function(obj) {
                return (menuUrl.menuCode == obj.code);
            });
            if (!menuItem) {
                return null;
            }
            return this.newAdminMenuItem(serviceItem, categoryItem, menuItem);
        },
        /**
         * 새로운 menuItem 을 생성한다.
         * @param category
         * @param menu
         * @returns {{category: *, menu: *, templateUrl: string}}
         */
        newAdminMenuItem: function (service, category, menu) {
            var templateUrl = 'page/admin/' + category.code + "/" + menu.code + '.html';
            return {
                service: service,
                category: category,
                menu: menu,
                templateUrl: templateUrl
            };
        },
        /**
         * 배치프로세스가 진행중인지 체크한다.
         * @param svcId 서비스 아이디
         * @returns {code: 200,  message: string}
         */
        checkBatchProcessing: function (svcId) {
            var url = '/admin/boss/checkBatchProcessing?svcId=' + svcId;
            return $http.get(url).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * Tmap/Syrup 선택된 그리드 데이터 저장.
         * @param svcId 서비스 아이디
         * @param setGridData 변경할 그리드 데이터
         * @returns {code: 200,  message: string}
         */
        createGridData: function (svcId, setGridData) {
            var url;
            if (svcId === 4) {
                url = '/admin/boss/createTmapGridData';
            } else {
                url = '/admin/boss/createSyrupGridData';
            }
            return $http.post(url, setGridData).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 권한의 선택된 그리드 데이터 저장.
         * @param setGridData 변경할 그리드 데이터
         * @returns {code: 200,  message: string}
         */
        updateComRole: function (setGridData) {
            var url = '/role/updateComRole';
            return $http.post(url, setGridData).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 배치프로세스가 진행중인지 체크한다.
         * @param svcId 서비스 아이디
         * @returns {code: 200,  message: string}
         */
        getMailData: function (basicDate) {
            var url = '/admin/boss/mailDatas?basicDate=' + basicDate;
            return $http.get(url).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 경영실적 메일 발송폼에 반영될 서비스별 코멘트 저장.
         * @param comments 코멘트 정보.
         * @returns {code: 200,  message: string}
         */
        createMailComments: function (comments) {
            var url = '/admin/boss/createMailComments';
            return $http.post(url, comments).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 조직도 정보 조회.
         * @param orgId 조직도 아이디.
         */
        getOrgUserTrees : function(orgCd) {
            var url = '/admin/boss/orgUserTrees?searchCondition=orgCd&searchKeyword=' + orgCd;
            return $http.get(url).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 경영실적 메일링 리스트 삭제 처리.
         * @param setMailGridData 삭제할 사용자 정보.
         * @returns {code: 200,  message: string}
         */
        deleteMailUser: function (setMailGridData) {
            var url = '/admin/boss/deleteMailUsers';
            return $http.post(url, setMailGridData).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 경영실적 메일링 리스트 등록 처리.
         * @param setMailGridData 삭제할 사용자 정보.
         * @returns {code: 200,  message: string}
         */
        createMailUser: function (createMailGridData) {
            var url = '/admin/boss/createMailUsers';
            return $http.post(url, createMailGridData).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 경영실적 메일 콘텐츠 조회.
         * @param getMailGridData 요청 정보.
         * @returns BpmMgntEmailSndObj
         */
        getMailContents: function (getMailGridData) {
            var url = '/admin/boss/getMailContents';
            return $http.post(url, getMailGridData).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 경영실적 메일 콘텐츠 조회.
         * @param getMailGridData 요청 정보.
         * @returns BpmMgntEmailSndObj
         */
        sendMails: function (mailData) {
            var url = '/admin/boss/sendMails';
            return $http.post(url, mailData).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * 이름으로 직원 정보 조회.
         * @param fullName 사용자 이름.
         */
        getOrgUsers : function(searchParam) {
            var url = '/admin/boss/orgUsers';
            return $http.post(url, searchParam).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },

        /**
         * Admin API Wrapper(get)
         * @param url
         * @returns {*}
         */
        getAdminApi: function (url) {
            return $http.get(url).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    return $q.reject(response.data);
                }
            }, function (response) {
                return $q.reject(response.data);
            });
        },
        /**
         * Admin API Wrapper(post)
         * @param url
         * @param requestData
         * @returns {code: 200,  message: string}
         */
        postAdminApi: function (url, requestData) {
            return $http.post(url, requestData).then(function (response) {
                if (typeof response.data === 'object') {
                    return response.data;
                } else {
                    // invalid response
                    return $q.reject(response.data);
                }
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        /**
         * datePicker binding(cookatrice)
         */
        bindingDatePicker: function () {
            $('.input-append.date').datepicker({
                format: 'yyyy.mm.dd',
                autoclose: true,
                weekStart: 1
            });
        }

    };
});

/**
 *  factories > menuManagementSvc
 */
(factories.lazy || factories).factory('menuManagementSvc', function ($http, $log) {
    return {
        /**
         * 메뉴를 업데이트한다.
         * @param menu
         * @param onSuccess
         * @param onError
         */
        updateMenu: function(menu, onSuccess, onError) {
            $http.post('/menu/update', menu).then(onSuccess, onError);
        },

        /**
         * 메뉴 순서를 갱신한다.
         *
         * @param modelValues 변경할 menu models
         * @param onSuccess
         */
        updateOrderIdx: function (modelValues, onSuccess) {
            angular.forEach(modelValues, function (obj, idx) {
                var index = FormatHelper.padLeft(idx, 2, '0').toString();
                var oldOrderIdx = obj.orderIdx.toString();
                var newOrderIdx = oldOrderIdx.substring(0, oldOrderIdx.length - 2) + index;
                obj.orderIdx = parseInt(newOrderIdx);
            });

            $http.post('/menu/updateMenus', modelValues).then(onSuccess, function () {
                $log.error('update menu error.');
            });
        }
    };
});
