var directives = angular.module('app.commonDirectives');

directives.lazy.directive('simplePagination', function () {
    //function prevPage(){
    //    console.log('prevPage()');
    //}
    //
    //function nextPage(){
    //    console.log('nextPage()');
    //}


    return {
        restrict: 'E',
        templateUrl: 'page/templates/simplePagination.tpl.html',
        scope: {
            hdQnaTableInfo: '='
        },
        link: function (/*scope, element, attrs*/) {
            //console.log(scope);
            //console.log(element);
            //console.log(attrs);



//            scope.prevPage = function () {
//                if (scope.hdQnaTableInfo.curPage > 1) {
//                    scope.setPage(scope.hdQnaTableInfo.curPage - 1);
//                }
//            };
//
//            scope.nextPage = function () {
//                if (scope.hdQnaTableInfo.curPage < scope.hdQnaTableInfo.maxPage) {
//                    scope.setPage(scope.hdQnaTableInfo.curPage + 1);
//                }
//            };


        }
    };

});
