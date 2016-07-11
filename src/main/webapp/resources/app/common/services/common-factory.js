var factories = angular.module('app.commonFactory', ['ui.router']);
factories.config(
    function ($provide) {
        factories.lazy = {
            factory: $provide.factory
        };
    }
);

/**
 *  factories > user Service
 */
factories.factory('userSvc', function ($http, $q) {
    var loginUser = null;
    var reportAdminIds = ['PP44632', 'PP45941', 'PP50532', 'PP39093',
        '1000714', '1002303', '1002305', '1000395', '1001471', '1001463', '1001577',
        '1001940', '1000610', '1002302', '1000518', '1000019', '1002445', '1001529', '1000967', '1002396',
        '1000001', '1000036', '1001305', '1001341', '1001362', '1001501', '1001592', '1002217', '7000001', '7000003', '7000007', '7000011', '7000013', '7000016', '7000018', '7000019', '7000022', '7000023', '7000029', '7000040', '7000041', '7000042', '7000045', '7000047', '7000050', '7000051', '7000052', '7000054', '7000057', '7000062', '7000063', '7000064', '7000066', '7000067', '7000068', '7000070', '7000071', '1000018', '1000058', '1000071', '1000150', '1000318', '1000457', '1000524', '1000030', '1000138', '1000238', '1000644', '1001339', 'P031071', '1000409', '1000410', '1000746', '1000049', '1002222', '1000553', '1000714', '1000072', '1000108', '1000386', '1001010',
        '1000437', '1000392', '1001183', '1001479', '1000724', '1002266', '1001510', 'PP44651', '1001636', '1002592',
        '1000675', '1001617', '7000080', '7000081', '1001271', '1000641', '9000765', '1000103', '5001298',
        '1002305', '1002204', '1002507', '1000521', '1001852', '1002409', '1001128', '1001185', '1002518', '1001519', '1000179', '1000989', '1002516', '1002411', '1001637', '1001944', '1002384', '1000741', '1002278', '1002481', '5001900', '5001844',
        '1000446','1000195','1000410','1002175','1000342','1000294','1000348','1001495','1000746','1000049','1000086','1000311','1000130','1000298', '1001588'
    ];
    var helpDeskAdminIds = ['PP39093', 'PP44632', 'PP45941', 'PP50532', '1000714', '1000392', '1002409', '1002516', '1002507'];

    //This group is not equal report admin. This guys view ONLY open reports(now ocb) and khub reports.
    var khubAdminIds = ['PP39093','1001510', '1001617', '1001479','1001689', 'PP42155','PP41875','PP42154','PP42161','PP42243','PP41873', 'PP50532'];
    //admin menu access
    var menuAdminIds = ['PP44651','1000714', '1002303', '1000367', '1001179', '1000409','1000061','1000446','1000195','5001259','1000410','1002175','1000342','1000294','1000348','1001495','1001480','1000746','1000034', '1000038', '5001298',
        '1000446','1000195','1000410','1002175','1000342','1000294','1000348','1001495','1000746','1000049','1000086','1000311','1000130', '1000376', 'PP50532', '1001636'];
    var emailAdminIds = ['1000714', '1002303', 'PP50532', '1000437', 'PP39093'];
    var tcloudAdmins = ['PP39093', '1000714', 'PP50532',
                        '1001172', '1002278', '1001617', '1002305', '1001637', 'PP07642'];
    var stickinessAdmins = ['1001617', '1001838', 'PP50532', '1001689', '5001770', '1001459', 'PP39093'];
    var testAdmins = ['PP50532','PP39093','1000714'];
    var oneidAdmins = ['PP50532','PP39093','1000714','1002384','1002575','5001859','9000358'];
    var mktPushAdmins = ['PP50532','PP39093','1000714','1001838','1000119','1002516','1002305','1001838','1001689'];
    var ssbiAdminUsers = ['1000437', '1002507', '1001940', '1002409', '1000392', '1002396', '1001479', '1002592', '1002587',
        '1002516', '1000714', '1002562', 'PP57333', 'PP50532', 'PP39093', '1002575', '1000509', '1000503', '1001745'];
    var mstrEduUsers = ['1000437','1002507','1001940','1002409','1000392','1002396','1001479','1002592','1002587'
        ,'1002516','1000714','1002562','PP57333','PP50532','PP39093','1000395','1002305','1000179','1002302','1002411'
        ,'1001617','1002481','1002278','1001852','1000521','1002518','1000553','1002644'
        //SSBI 워크샵 임시권한
        ,'1002575','1002591','1000127','1000509','1000470','1001961','1000912','1000467','1000503','1000431','5001742'
        ,'1001745','1001776','1000999','1001553','1000612','1001569','1000827'];

    /** DASHBOARD temporary auth */
    var dashboardOcAdmins = ['1001463', '1000967', '1000392', 'PP39093'];
    //var CODETYPE_CATEGORY = [3];
    //var CODETYPE_MENU = [4];
    //var dashboardAdmins =
    //    [
    //        {category: 926, menu: [928], admin: ['none']},    //kid-kid
    //        {category: 74, menu: [75, 76, 77, 78, 79, 80, 81, 82], admin: ['none']},//bc- all
    //        //{category: 74, menu: [75, 76], admin: ['PP39095asdf']}, //bc-ocb,syrup
    //        //{category: 74, menu: [77, 78, 79], admin: ['PP50532']}, //bc-gifticon, sk11st, tstore
    //        //{category: 74, menu: [75, 76, 77, 78, 79, 80, 81, 82], admin: ['PP39093']},//bc- all
    //        {category: 929, menu: [930, 931], admin: ['1001463', '1000967', '1000392', 'PP39093']},  //oc-platform, platformWeek
    //        {category: 929, menu: [930], admin: ['PP50532']}    //oc-platform
    //    ];

    //function checkDashboardAuth(id, codeType){
    //    var checkFlag = false;
    //    _.some(dashboardAdmins, function(element){
    //        if(_.contains(CODETYPE_CATEGORY, codeType)){
    //            if(element.category == id){
    //               if(element.admin.length == 0){
    //                   checkFlag = true;
    //               }else{
    //                   _.each(element.admin, function(value){
    //                       if(value == loginUser.username){
    //                           checkFlag = true;
    //                       }
    //                   });
    //               }
    //            }
    //        }else if(_.contains(CODETYPE_MENU, codeType)){
    //            var tmpAdmin = element.admin;
    //            _.each(element.menu, function (value) {
    //                if (value == id) {
    //                    if(tmpAdmin.length == 0){
    //                        checkFlag = true;
    //                    }else{
    //                        _.each(tmpAdmin, function (value) {
    //                            if (value == loginUser.username) {
    //                                checkFlag = true;
    //                            }
    //                        });
    //                    }
    //               }
    //            });
    //        }
    //        return checkFlag;
    //    });
    //    return checkFlag;
    //}

    return {
        getCntSign : function(loginId) {
            var url = '/sign/cnt?loginId=' + loginId;
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
        getSign : function(loginId) {
            var url = '/sign/get?loginId=' + loginId;
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
        createSign : function(loginId) {
            var url = '/sign/create';
            return $http.post(url, {loginId : loginId}).then(function (response) {
                    return response.data;
            }, function (response) {
                // something went wrong
                return $q.reject(response.data);
            });
        },
        setUser: function (user) {
            loginUser = user;
        },
        getUser: function () {
            return loginUser;
        },
        //isReportAdmin: function () {
        //    return _(reportAdminIds).contains(loginUser.username);
        //},
        //isDashboardOcAdmin: function () {
        //    return _(dashboardOcAdmins).contains(loginUser.username);
        //},
        //isHelpDeskAdmin: function () {
        //    return _(helpDeskAdminIds).contains(loginUser.username);
        //},
        //isKhubAdmin: function () {
        //    return _(khubAdminIds).contains(loginUser.username);
        //},
        //isMenuAdmin: function () {
        //    return _(menuAdminIds).contains(loginUser.username);
        //},
        //isEmailAdmin: function () {
        //    return _(emailAdminIds).contains(loginUser.username);
        //},
        //isOneidAdmin: function () {
        //    return _(oneidAdmins).contains(loginUser.username);
        //},
        //isTcloudAdmins: function () {
        //    return _(tcloudAdmins).contains(loginUser.username);
        //},
        //isStickinessAdmins: function () {
        //    return _.contains(stickinessAdmins,loginUser.username);
        //},
        //isMktPushAdmins: function () {
        //    return _.contains(mktPushAdmins,loginUser.username);
        //},
        //isSsbiAdminUsers: function () {
        //    return _.contains(ssbiAdminUsers,loginUser.username);
        //},
        //isMstrEduUsers: function () {
        //    return _.contains(mstrEduUsers,loginUser.username);
        //},
        //isTestAdmins: function () {
        //    return _.contains(testAdmins,loginUser.username);
        //},
        //checkAdminWrite: function(replyerId){
        //    return _(helpDeskAdminIds).contains(replyerId);
        //},
        isLogin : function() {
            return ($.cookie('voyager_master') != null);
        },
        isLoginRefer : function() {
            return ($.cookie('voyager_login_referer') != null);
        },
        getLoginReferer : function() {
            return $.cookie('voyager_login_referer');
        }
        //isDashboardAdmins: function(code, commonCodeId){
        //    return checkDashboardAuth(code,commonCodeId);
        //}
    };
});

/**
 *  factories > url handle Service
 */
factories.factory('urlHandleSvc', function ($location, $stateParams) {
    return {
        getMenuUrl: function () {
           return {
                serviceCode: $stateParams.serviceCode,
                categoryCode: $location.search()['categoryCode'],
                menuCode: $location.search()['menuCode']
           };
        },
        /**
         * menuContext를 url로 갱신하다.
         * @param item
         */
        updateMenuUrl: function (item) {
            $location.search({
                categoryCode: item.category.code
            });

            if (item.menu) {
                $location.search('menuCode', item.menu.code);
            }
        }
    };
});

/**
 *  factories > access Service
 */
factories.factory('accessSvc', function ($http, $q, userSvc, urlHandleSvc) {
    return {
        getVoyagerCookie: function () {
            return AjaxHelper.syncGetJSON('/login/cookie', null);
        },
        createAccessLog: function () {
            var username = userSvc.getUser().username;
            var menuUrl = urlHandleSvc.getMenuUrl();

            var uri = '/log/access?username=' + username + '&serviceCode=' + menuUrl.serviceCode + '&categoryCode=' + menuUrl.categoryCode;
            if (!angular.isUndefined(menuUrl.menuCode)) {
                uri += '&menuCode=' + menuUrl.menuCode;
            }

            return $http.get(uri).then(function (response) {
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
        }
    };
});

/**
 *  factories > menu Service
 */
factories.factory('menuSvc', function ($http, $q, $log, userSvc) {
    var reportService, dashboardService, adminService, helpDeskService;
    var navigationMenu;
    return {
        loadDashboardService: function () {
            return dashboardService = AjaxHelper.syncGetJSON('/menu/dashboardService/' + userSvc.getUser().username, null);
        },
        loadReportService: function () {
            return reportService = AjaxHelper.syncGetJSON('/menu/reportServices/' + userSvc.getUser().username, null);
        },
        loadAdminService: function () {
            return adminService = AjaxHelper.syncGetJSON('/menu/adminService/' + userSvc.getUser().username, null);
        },
        loadHelpDeskService: function () {
            return helpDeskService = AjaxHelper.syncGetJSON('/menu/helpDeskService/' + userSvc.getUser().username, null);
        },
        loadNavigationMenu: function () {
            return navigationMenu = AjaxHelper.syncGetJSON('/menu/navigationMenu/' + userSvc.getUser().username, null);
        },
        getNavigationMenu: function() {
            if (!navigationMenu) {
                $log.debug('navigationMenu is not loaded!');
                return this.loadNavigationMenu();
            }
            return navigationMenu;
        },
        /**
         * 리포트 서비스 목록을 조회한다.
         * @returns {*}
         */
        getReportService: function () {
            if (!reportService) {
                $log.debug('reportServices is not loaded!');
                return this.loadReportService();
            }
            return reportService;
        },

        /**
         * 대시보드 서비스 목록을 조회한다.
         * @returns {*}
         */
        getDashboardService: function () {
            if (!dashboardService) {
                $log.debug('dashboardService is not loaded!');
                return this.loadDashboardService();
            }
            return dashboardService;
        },

        /**
         * admin 서비스를 조회한다.
         * @returns {*}
         */
        getAdminService: function () {
            if (!adminService) {
                $log.debug('adminService is not loaded!');
                return this.loadAdminService();
            }
            return adminService;
        },

        /**
         * helpDesk 서비스를 조회한다.
         * @returns {*}
         */
        getHelpDeskService: function () {
            if (!helpDeskService) {
                $log.debug('helpDeskService is not loaded!');
                return this.loadHelpDeskService();
            }
            return helpDeskService;
        },
        getService: function(serviceId) {
            var reportServiceItem;
            var reportServices = this.loadReportService();
            reportServiceItem = _.find(reportServices, function(obj) {
                return (serviceId == obj.id);
            });
            if (!reportServiceItem) {
                return null;
            }
            return reportServiceItem;
        },
        getMenu: function(serviceName, categoryName, menuId) {
            if (!serviceName || !categoryName || !menuId) {
                $log.info(serviceName, categoryName, menuId);
                return null;
            }
            if (serviceName === 'Dashboard') {
                var dashboardCategoryItem;
                var dashboardServices = this.loadDashboardService();
                dashboardCategoryItem = _.find(dashboardServices.categories, function(obj) {
                    return (categoryName == obj.name);
                });
                if (!dashboardCategoryItem) {
                    return null;
                }
                var dashboardMenuItem;
                dashboardMenuItem = _.find(dashboardCategoryItem.menus, function(obj) {
                    return (menuId == obj.id);
                });
                if (!dashboardMenuItem) {
                    return null;
                }
                return dashboardMenuItem;
            } else if (serviceName === '관리자 페이지') {
                var adminCategoryItem;
                var adminServices = this.loadAdminService();
                adminCategoryItem = _.find(adminServices.categories, function(obj) {
                    return (categoryName == obj.name);
                });
                if (!adminCategoryItem) {
                    return null;
                }
                var adminMenuItem;
                adminMenuItem = _.find(adminCategoryItem.menus, function(obj) {
                    return (menuId == obj.id);
                });
                if (!adminMenuItem) {
                    return null;
                }
                return adminMenuItem;
            } else {
                var reportServiceItem;
                var reportServices = this.loadReportService();
                reportServiceItem = _.find(reportServices, function(obj) {
                    return (serviceName == obj.name);
                });
                if (!reportServiceItem) {
                    return null;
                }
                var reportCategoryItem;
                reportCategoryItem = _.find(reportServiceItem.categories, function(obj) {
                    return (categoryName == obj.name);
                });
                if (!reportCategoryItem) {
                    return null;
                }
                var reportMenuItem;
                reportMenuItem = _.find(reportCategoryItem.menus, function(obj) {
                    return (menuId == obj.id);
                });
                if (!reportMenuItem) {
                    return null;
                }
                return reportMenuItem;
            }
            return null;
        },
        getCategory : function(serviceId, categoryId, commonCodeId) {
            if (commonCodeId === 3) {//Dashboard
                var dashboardCategoryItem;
                var dashboardServices = this.loadDashboardService();
                dashboardCategoryItem = _.find(dashboardServices.categories, function(obj) {
                    return (categoryId == obj.id);
                });
                if (!dashboardCategoryItem) {
                    return null;
                }
                return dashboardCategoryItem;
            } else if (commonCodeId === 6) {//Report
                var reportServiceItem;
                var reportServices = this.loadReportService();
                reportServiceItem = _.find(reportServices, function(obj) {
                    return (serviceId == obj.id);
                });
                if (!reportServiceItem) {
                    return null;
                }
                var reportCategoryItem;
                reportCategoryItem = _.find(reportServiceItem.categories, function(obj) {
                    return (categoryId == obj.id);
                });
                if (!reportCategoryItem) {
                    return null;
                }
                return reportCategoryItem;
            } else {// Admin
                var adminCategoryItem;
                var adminServices = this.loadAdminService();
                adminCategoryItem = _.find(adminServices.categories, function(obj) {
                    return (categoryId == obj.id);
                });
                if (!adminCategoryItem) {
                    return null;
                }
                return adminCategoryItem;
            }
            return null;
        },
        /**
         * 조직도 정보 조회.
         * @param searchCondition 조회 조건.
         */
        getTreeMenus : function(searchCondition) {
            var url = '/menu/getTreeMenus?' + searchCondition;
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
         * 조직도 정보 조회.
         * @param searchCondition 조회 조건.
         */
        getMstrSubMenuTrees : function(searchCondition) {
            var url = '/menu/mstrSubMenuTrees?' + searchCondition;
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
         * 서비스별로 첫번째 메뉴 정보 셋. (첫번째 카테고리의 첫번째 메뉴)
         * @param navName 네비게이션바 이름.
         * @param serviceCode 서비스 코드.
         * @returns {string} defaultUrl
         */
        loadDefaultUrl: function (navName, serviceCode) {
            if (navName === 'dashboard') {
                var dashboardServiceItem = null;
                var dashboardServices = this.getDashboardService();
                angular.forEach(dashboardServices, function (obj) {
                    if (obj.authority === 'Y' && serviceCode == obj.code) {
                        return dashboardServiceItem = obj;
                    }
                });
                if (!dashboardServiceItem) {
                    return 'unauthorized';
                }
                var dashboardCategoryItem = dashboardServiceItem.categories[0];
                var dashboardMenuItem = dashboardCategoryItem.menus[0];
                return ['/dashboard/', serviceCode,'?categoryCode=',dashboardCategoryItem.code,
                    '&menuCode=', dashboardMenuItem.code].join('');
            } else if (navName === 'reports') {
                var reportServiceItem = null;
                var reportServices = this.getReportService();
                angular.forEach(reportServices, function (obj) {
                    if (obj.authority === 'Y' && serviceCode == obj.code) {
                        return reportServiceItem = obj;
                    }
                });
                if (!reportServiceItem) {
                    return 'unauthorized';
                }
                var reportCategoryItem = reportServiceItem.categories[0];
                var reportMenuItem = reportCategoryItem.menus[0];
                return ['/reports/', serviceCode,'?categoryCode=',reportCategoryItem.code,
                    '&menuCode=', reportMenuItem.code].join('');
            } else if (navName === 'helpdesk') {
                var helpDeskService = this.getHelpDeskService();
                var categoryItem = helpDeskService.categories[0];
                var menuItem = categoryItem.menus[0];
                return ['/helpdesk/', categoryItem.code,
                    '/', menuItem.code].join('');
            } else {// admin
                var adminServiceItem = null;
                var adminServices = this.getAdminService();
                angular.forEach(adminServices, function (obj) {
                    if (obj.authority === 'Y' && serviceCode == obj.code) {
                        return adminServiceItem = obj;
                    }
                });
                if (!adminServiceItem) {
                    return 'unauthorized';
                }
                var adminCategoryItem = adminServiceItem.categories[0];
                var adminMenuItem = adminCategoryItem.menus[0];
                return ['/admin/', serviceCode,'?categoryCode=',adminCategoryItem.code,
                    '&menuCode=', adminMenuItem.code].join('');
            }
        },
        getDashboardDefaultUrl : function() {
            var dashboardServiceItem = this.getDashboardService();
            var categorySize = dashboardServiceItem.categories.length;
            if (categorySize == 0) {
                return '0';
            }
            var categoryItem = "", firstCategoryItem = "";
            var menuItem = "", firstMenuItem = "";
            var menuNotFound = true, menuFlag = false;
            for (var i = 0; i < categorySize; i++) {
                categoryItem = dashboardServiceItem.categories[i];
                var menuSize = categoryItem.menus.length;
                if (menuSize > 0) {
                    menuItem = "";
                    for (var j = 0; j < menuSize; j++) {
                        menuItem = categoryItem.menus[j];
                        if ((menuItem.visibleYn == 'Y' || menuItem.visibleYn == 'P') &&
                            menuItem.deleteYn != 'Y') {
                            menuNotFound = false;
                            if (!firstMenuItem) {
                                firstMenuItem = menuItem;
                                firstCategoryItem = categoryItem;
                            }
                            if (menuItem.authority == 'Y') {
                                menuFlag = true;
                                break;
                            } else {
                                menuItem = "";
                            }
                        }
                    }
                    if (menuFlag) {
                        break;
                    }
                }
                categoryItem = "";
            }
            if (categoryItem == "" || menuItem == "") {
                if (menuNotFound) {
                    return '0';// 등록된 메뉴가 없는 경우
                }
                return ['/dashboard/bs?categoryCode=', firstCategoryItem.code, '&menuCode=', firstMenuItem.code].join('');//등록된 메뉴가 있는 경우
            }
            return ['/dashboard/bs?categoryCode=', categoryItem.code, '&menuCode=', menuItem.code].join('');
        },
        /**
         * 리포트 서비스의 기본 url을 조회한다. (첫번째 카테고리의 첫번째 메뉴)
         * @param serviceCode 서비스 코드
         * @returns {string} defaultUrl
         */
        getReportDefaultUrl: function (serviceCode) {
            var serviceItem = null;
            var reportServices = this.getReportService();
            angular.forEach(reportServices, function (obj) {
                if (serviceCode == obj.code) {
                    return serviceItem = obj;
                }
            });
            var categorySize = serviceItem.categories.length;
            if (categorySize == 0) {
                return '0';
            }
            var categoryItem = "", firstCategoryItem = "";
            var menuItem = "", firstMenuItem = "";
            var menuNotFound = true, menuFlag = false;
            for (var i = 0; i < categorySize; i++) {
                categoryItem = serviceItem.categories[i];
                var menuSize = categoryItem.menus.length;
                if (menuSize > 0) {
                    menuItem = "";
                    for (var j = 0; j < menuSize; j++) {
                        menuItem = categoryItem.menus[j];
                        if ((menuItem.visibleYn == 'Y' || menuItem.visibleYn == 'P') &&
                            menuItem.deleteYn != 'Y') {
                            menuNotFound = false;
                            if (!firstMenuItem) {
                                firstMenuItem = menuItem;
                                firstCategoryItem = categoryItem;
                            }
                            if (menuItem.authority == 'Y') {
                                menuFlag = true;
                                break;
                            } else {
                                menuItem = "";
                            }
                        }
                    }
                    if (menuFlag) {
                        break;
                    }
                }
                categoryItem = "";
            }
            if (categoryItem == "" || menuItem == "") {
                if (menuNotFound) {
                    return '0';// 등록된 메뉴가 없는 경우
                }
                return ['/reports/', serviceCode,'?categoryCode=', firstCategoryItem.code, '&menuCode=', firstMenuItem.code].join('');//등록된 메뉴가 있는 경우
            }
            return ['/reports/', serviceCode,'?categoryCode=', categoryItem.code, '&menuCode=', menuItem.code].join('');
        },
        getAdminDefaultUrl : function() {
            var adminServiceItem = this.getAdminService();
            var categorySize = adminServiceItem.categories.length;
            if (categorySize == 0) {
                return '0';
            }
            var categoryItem = "";
            var menuItem = "";
            var menuFlag = false;
            for (var i = 0; i < categorySize; i++) {
                categoryItem = adminServiceItem.categories[i];
                var menuSize = categoryItem.menus.length;
                if (menuSize > 0) {
                    menuItem = "";
                    for (var j = 0; j < menuSize; j++) {
                        menuItem = categoryItem.menus[j];
                        if (menuItem.authority == 'Y') {
                            menuFlag = true;
                            break;
                        } else {
                            menuItem = "";
                        }
                    }
                    if (menuFlag) {
                        break;
                    }
                }
                categoryItem = "";
            }
            if (categoryItem == "" || menuItem == "") {
                return '0';// 권한허용된 메뉴가 없는 경우
            }
            return ['/admin/admin?categoryCode=', categoryItem.code, '&menuCode=', menuItem.code].join('');
        },
        //getReportDefaultUrl: function (serviceCode) {
        //    var serviceItem = null;
        //    var reportServices = this.getReportService();
        //    angular.forEach(reportServices, function (obj) {
        //        if (serviceCode == obj.code) {
        //            return serviceItem = obj;
        //        }
        //    });
        //    var categorySize = serviceItem.categories.length;
        //    if (categorySize == 0) {
        //        return '0';
        //    }
        //    var categoryItem = "";
        //    var menuItem = "";
        //    var menuFlag = false, tcloudFlag = false, oneidFlag = false, menuNotFound = true;
        //    for (var i = 0; i < categorySize; i++) {
        //        categoryItem = serviceItem.categories[i];
        //        if (categoryItem.summaryReportYn == 'Y' && userSvc.isReportAdmin()) {
        //            menuItem = categoryItem.menus[0];
        //            break;
        //        }
        //        var menuSize = categoryItem.menus.length;
        //        if (menuSize > 0) {
        //            menuItem = "";
        //            for (var j = 0; j < menuSize; j++) {
        //                menuItem = categoryItem.menus[j];
        //                if (menuItem.deleteYn != 'Y') {
        //                    menuNotFound = false;
        //                }
        //                if ((menuItem.id == 673 || menuItem.id == 674 || menuItem.id == 675 ||
        //                    menuItem.id == 676 || menuItem.id == 677) && userSvc.isTcloudAdmins()) {
        //                    tcloudFlag = true;
        //                }
        //                if (menuItem.id == 936 && userSvc.isOneidAdmin()) {
        //                    oneidFlag = true;
        //                }
        //                if (tcloudFlag || oneidFlag ||
        //                    (menuItem.code == 'report' && userSvc.isReportAdmin()) ||
        //                    (menuItem.code != 'report' && menuItem.visibleYn == 'Y' && menuItem.deleteYn != 'Y')) {
        //                    menuFlag = true;
        //                    break;
        //                } else {
        //                    menuItem = "";
        //                    continue;
        //                }
        //            }
        //            if (menuFlag) {
        //               break;
        //            }
        //        }
        //        categoryItem = "";
        //    }
        //    if (categoryItem == "" || menuItem == "") {
        //        if (menuNotFound) {
        //            return '0';// 등록된 메뉴가 없는 경우
        //        }
        //        return '1';//등록된 메뉴가 있는 경우
        //    }
        //    return ['/reports/', serviceCode,'?categoryCode=',categoryItem.code, '&menuCode=', menuItem.code].join('');
        //},
        /**
         * help desk default url 조회
         * @returns {string} defaultUrl
         */
        getHelpDeskDefaultUrl: function () {
            var helpDeskService = this.getHelpDeskService();
            var categoryItem = helpDeskService.categories[0];
            var menuItem = categoryItem.menus[0];
            return ['/helpdesk/', categoryItem.code, '/', menuItem.code].join('');
        }
    };
});

/**
 *  factories > string Service
 */
factories.factory('strSvc', function () {
    function checkNull(str) {
        if (str == null || str == undefined || str.length == 0) {
            return true;
        }
        return false;
    }

    return {
        /**
         * 문자열 --> 유니코드 문자열
         * @param str
         * @returns {string}
         */
        getStringToUnicode: function (str) {
            if (checkNull(str)){
                return null;
            }
            str = str || '';
            var ret = [];
            var strs = str.split('');
            for (var i = 0, length = strs.length; i < length; i++) {
                ret.push(escape(strs[i]).replace('%', '\\'));
            }
            return ret.join('');
        }
        /**
         * 유니코드 문자열 --> 문자열
         * @param unicodes
         * @returns {string}
         */
        //getUnicodeToString: function (unicodes) {
        //    unicodes = unicodes || [];
        //    var ret = [];
        //    for (var i = 0, length = unicodes.length; i < length; i++) {
        //        ret.push(unescape(unicodes[i]));
        //    }
        //    return ret.join('');
        //}
    };
});

factories.factory('apiSvc', function ($rootScope, $http) {
    return {
        getMstrSession: function (mstrProjectId) {
            if (!mstrProjectId)
                return null;
            return AjaxHelper.syncGetJSON('/mstr/getCheckSession?saveServerInfo=off&projectId=' + mstrProjectId, null);
        },
        getComeRoleUser: function (loginId, roleId) {
            if (!loginId || !roleId)
                return null;
            return AjaxHelper.syncGetJSON('/role/comRoleUser?loginId=' + loginId + '&roleId=' + roleId, null);
        },
        getMstrDateTypes: function (mstrProjectId, mstrObjectId) {
            if (!mstrProjectId)
                return null;
            return AjaxHelper.syncGetJSON('/mstr/getDateTypes?projectId=' + mstrProjectId + '&objectId=' + mstrObjectId, null);
        },
        getMstrWeeks: function (weekType, searchStartDate, searchEndDate) {
            if (!searchStartDate || !searchEndDate)
                return null;
            return AjaxHelper.syncGetJSON('/mstr/getWeeks?weekType=' + weekType + '&searchStartDate=' + searchStartDate + '&searchEndDate=' + searchEndDate, null);
        },
        getMstrMenus: function (menuCode) {
            if (!menuCode)
                return null;
            return AjaxHelper.syncGetJSON('/menu/mstrMenus?code=' + menuCode, null);
        },
        /**
         * voyager common http service :) - cookatrice
         * @param config
         * @returns {*}
         */
        voyagerHttpSvc: function (config) {
            //explicit notification about defualt method is 'GET'!
            if (!config.method) {
                config.method = 'GET';
            }
            return $http(config)
                .success(function (data) {
                    return data;
                })
                .error(function (data, status) {
                    alert('error! (status : ' + status + ')');
                    return data;
                });
        }
    };
});
