var factories = angular.module('app.commonFactory');
(factories.lazy || factories).factory('helpDeskSvc', function (menuSvc, $http) {
    return {
        /**
         * 헬프데스크 메뉴 조회 (visible false 제외)
         * @returns Array menu items
         */
        getHelpDeskMenuItems: function () {
            var categories = [];
            var helpDeskMenu = menuSvc.getHelpDeskService();
            angular.forEach(helpDeskMenu.categories, function (obj) {
                // TODO visibleYn 제거 처리 - 권한 개념이 생기면 visibleYn이 필요없음.
                if (obj.visibleYn != 'N') {
                    categories.push(obj);
                }
            });
            helpDeskMenu.categories = categories;
            return helpDeskMenu;
        },

        getIssueTypes: function () {
            var issueTypeUrl = "/commonGroupCode/getCodes?groupCodeId=ISSUE_TYPE";
            return $http.get(issueTypeUrl).then(function (result) {
                    return result.data;
            });
        },

        getComponentTypes: function () {
            var componentTypeUrl = "/commonGroupCode/getCodes?groupCodeId=COMPONENT_TYPE";
            return $http.get(componentTypeUrl).then(function (result) {
                    return result.data;
            });
        }
        ,
        getStatusTypes: function(){
            //var stateTypeUrl = "/"
            //return $http.get(stateTypeUrl).then(function (result) {
            //        return result.data;
            //});
            return [
                {codeId:'Open',codeName:'Open'},
                {codeId:'In Progress',codeName:'In Progress'},
                {codeId:'Resolved',codeName:'Resolved'},
                {codeId:'Reopened',codeName:'Reopened'},
                {codeId:'Closed',codeName:'Closed'}
            ];
        },
        bindingDatePicker: function () {
            $('.input-append.date').datepicker({
                format: 'yyyy.mm.dd',
                autoclose: true,
                weekStart: 1
            });
        },
        /**
         * check title byte under 100.
         * @param rawTitle
         * @returns {boolean} title bytes > 100 is false or true.
         */
        checkTitleByte: function (rawTitle) {
            var titleByte = (function (s, b, i, c) {
                for (b = i = 0; c = s.charCodeAt(i++); b += c >> 11 ? 3 : c >> 7 ? 2 : 1);
                return b;
            })(rawTitle);
            return titleByte <= 100;
        }
    };
});
//
//
//
//(factories.lazy || factories).factory('createDialog', ["$document", "$compile", "$rootScope", "$controller", "$timeout",
//    function ($document, $compile, $rootScope, $controller, $timeout) {
//        var defaults = {
//            id: null,
//            template: null,
//            templateUrl: null,
//            title: 'Default Title',
//            backdrop: true,
//            success: {label: 'OK', fn: null},
//            cancel: {label: 'Close', fn: null},
//            controller: null, //just like route controller declaration
//            backdropClass: "modal-backdrop",
//            backdropCancel: true,
//            footerTemplate: null,
//            modalClass: "modal",
//            css: {
//                top: '100px',
//                left: '30%',
//                margin: '0 auto'
//            }
//        };
//        var body = $document.find('body');
//
//        return function Dialog(templateUrl/*optional*/, options, passedInLocals) {
//
//            // Handle arguments if optional template isn't provided.
//            if (angular.isObject(templateUrl)) {
//                passedInLocals = options;
//                options = templateUrl;
//            } else {
//                options.templateUrl = templateUrl;
//            }
//
//            options = angular.extend({}, defaults, options); //options defined in constructor
//
//            var key;
//            var idAttr = options.id ? ' id="' + options.id + '" ' : '';
//            var defaultFooter = '<button class="btn" ng-click="$modalCancel()">{{$modalCancelLabel}}</button>' +
//                '<button class="btn btn-primary" ng-click="$modalSuccess()">{{$modalSuccessLabel}}</button>';
//            var footerTemplate = '<div class="modal-footer">' +
//                (options.footerTemplate || defaultFooter) +
//                '</div>';
//            var modalBody = (function () {
//                if (options.template) {
//                    if (angular.isString(options.template)) {
//                        // Simple string template
//                        return '<div class="modal-body">' + options.template + '</div>';
//                    } else {
//                        // jQuery/JQlite wrapped object
//                        return '<div class="modal-body">' + options.template.html() + '</div>';
//                    }
//                } else {
//                    // Template url
//                    return '<div class="modal-body" ng-include="\'' + options.templateUrl + '\'"></div>'
//                }
//            })();
//            //We don't have the scope we're gonna use yet, so just get a compile function for modal
//            var modalEl = angular.element(
//                    '<div class="' + options.modalClass + ' fade"' + idAttr + ' style="display: block;">' +
//                    '  <div class="modal-dialog">' +
//                    '    <div class="modal-content">' +
//                    '      <div class="modal-header">' +
//                    '        <button type="button" class="close" ng-click="$modalCancel()">&times;</button>' +
//                    '        <h2>{{$title}}</h2>' +
//                    '      </div>' +
//                    modalBody +
//                    footerTemplate +
//                    '    </div>' +
//                    '  </div>' +
//                    '</div>');
//
//            for (key in options.css) {
//                modalEl.css(key, options.css[key]);
//            }
//            var divHTML = "<div ";
//            if (options.backdropCancel) {
//                divHTML += 'ng-click="$modalCancel()"';
//            }
//            divHTML += ">";
//            var backdropEl = angular.element(divHTML);
//            backdropEl.addClass(options.backdropClass);
//            backdropEl.addClass('fade in');
//
//            var handleEscPressed = function (event) {
//                if (event.keyCode === 27) {
//                    scope.$modalCancel();
//                }
//            };
//
//            var closeFn = function () {
//                body.unbind('keydown', handleEscPressed);
//                modalEl.remove();
//                if (options.backdrop) {
//                    backdropEl.remove();
//                }
//            };
//
//            body.bind('keydown', handleEscPressed);
//
//            var ctrl, locals,
//                scope = options.scope || $rootScope.$new();
//
//            scope.$title = options.title;
//            scope.$modalClose = closeFn;
//            scope.$modalCancel = function () {
//                var callFn = options.cancel.fn || closeFn;
//                callFn.call(this);
//                scope.$modalClose();
//            };
//            scope.$modalSuccess = function () {
//                var callFn = options.success.fn || closeFn;
//                callFn.call(this);
//                scope.$modalClose();
//            };
//            scope.$modalSuccessLabel = options.success.label;
//            scope.$modalCancelLabel = options.cancel.label;
//
//            if (options.controller) {
//                locals = angular.extend({$scope: scope}, passedInLocals);
//                ctrl = $controller(options.controller, locals);
//                // Yes, ngControllerController is not a typo
//                modalEl.contents().data('$ngControllerController', ctrl);
//            }
//
//            $compile(modalEl)(scope);
//            $compile(backdropEl)(scope);
//            body.append(modalEl);
//            if (options.backdrop) body.append(backdropEl);
//
//            $timeout(function () {
//                modalEl.addClass('in');
//            }, 200);
//        };
//    }]);
