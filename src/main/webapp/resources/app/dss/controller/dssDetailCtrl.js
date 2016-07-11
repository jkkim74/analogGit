/**
 * Created by seoseungho on 2014. 9. 26..
 */
function dssDetailCtrl($scope, $state, $stateParams, $http, userSvc, $window, $location, $log) {
    'use strict';
    $scope.$state = $state;

    $scope.init = function() {
        //console.log('dssDetailCtrl init dssId:', $stateParams);
        $scope.dss = AjaxHelper.syncGetJSON('/dss/'+$stateParams.dssId);
        if (!$scope.dss) {
            alert('존재하지 않는 리포트입니다. 다시 선택해주세요.');
            $state.go('main.dss.menu', {menu: 'all'});
            return false;
        }
//        $scope.handleLineBreakForTextareaValue();

        // convert string date to Date type
        $scope.dss.analysisStart = DateHelper.stringToDateObject($scope.dss.analysisStart);
        $scope.dss.analysisEnd = DateHelper.stringToDateObject($scope.dss.analysisEnd);
        $scope.dss.dataStart = DateHelper.stringToDateObject($scope.dss.dataStart);
        $scope.dss.dataEnd = DateHelper.stringToDateObject($scope.dss.dataEnd);
        // parse json string to object
        if ($scope.dss.variablesJSON) {
            $scope.dss.variables = angular.fromJson($scope.dss.variablesJSON);
        }
//        console.log($scope.dss.fileMetaList);

        $scope.dss.reportFileMetas = [];
        $scope.dss.dataFileMetas = [];
        $scope.dss.queryFileMetas = [];
        angular.forEach($scope.dss.fileMetaList, function (fileMeta, index) {
            if (fileMeta.category === 'report') {
                $scope.dss.reportFileMetas.push(fileMeta);
            }  else if (fileMeta.category === 'data') {
                $scope.dss.dataFileMetas.push(fileMeta);
            } else if (fileMeta.category === 'query') {
                $scope.dss.queryFileMetas.push(fileMeta);
            }
        });

        $scope.loginUser = userSvc.getUser();

        // 기존 댓글 조회
        $scope.loadComment();


        // 댓글 등록을 위한 폼 설정
        $scope.commentForm = {};
        $scope.commentForm.dssId = $scope.dss.id;
        // 댓글 수정을 위함 폼 설정
        $scope.updateCommentForm ={};
        //console.log($scope.loginUser);
    };

    $scope.handleLineBreakForTextareaValue = function() {
        $scope.dss.content = $scope.dss.content.replace(/\r\n/g, "\n").split("\n").join('<br/>');
        if ($scope.dss.conclusion) {
            $scope.dss.conclusion = $scope.dss.conclusion.replace(/\r\n/g, "\n").split("\n").join('<br/>');
        }
        if ($scope.dss.futureWork) {
            $scope.dss.futurework = $scope.dss.futureWork.replace(/\r\n/g, "\n").split("\n").join('<br/>');
        }
    };

    // 보고서 삭제
    $scope.delete = function(dssId) {
        var deleteConfirm = confirm('보고서를 삭제하시겠습니까?');
        if (deleteConfirm) {
            $http.delete('/dss/' + dssId, {deleteId: $scope.loginUser.username}).success(function (response) {
                $state.go('main.dss.menu', {menu: 'all'});
            }).error(function(data, status, headers, config) {
                alert('보고서 삭제 실패했습니다. 관리자에게 문의하세요.');
                $state.go('main.dss.menu', {menu: 'all'});
                return false;
            });
        }
    };

    // 메일로 보고서 URL 주소 보내기
    $scope.sendMail = function() {
        $window.location = "mailto:" + $scope.loginUser.email + "?body=" + $location.absUrl();
    };

    $scope.update = function(dssId) {
        $state.go('main.dss.update', {dssId: dssId});
    };

    // 댓글 남기기
    $scope.saveComment = function() {
        $http.post('/dssComment', $scope.commentForm).success(function (response, status, headers, config) {
            if (response.code === '0000') {
                //console.log('success', response);
                $scope.loadComment();
                $scope.commentForm.comment = '';
            } else {
                $log.debug('error', response);
            }
        });
    }

    // 이전 보고서 상세 가기
    $scope.goPrevious = function() {
        if ($scope.dss.previousDss) {
            $state.go('main.dss.detail', {dssId: $scope.dss.previousDss.id});
        } else {
            alert('이전 페이지가 없습니다.');
        }
    };

    // 다음 보고서 상세 가기
    $scope.goNext = function() {
        if ($scope.dss.nextDss) {
            $state.go('main.dss.detail', {dssId: $scope.dss.nextDss.id});
        } else {
            alert('다음 페이지가 없습니다.');
        }
    };

    // 커멘트 수정 폼을 보인다.
    $scope.readyForUpdateComment = function(comment) {
        // 기존에 열렸던 댓글 수정 폼을 닫는다.
        $scope.cancelForUpdateComment();
        comment.isUpdate = true;
        $scope.updateCommentForm.comment = comment.comment;
        $scope.updateCommentForm.id = comment.id;
        comment.updatedComment = comment.comment;
    };

    // 댓글을 수정한다.
    $scope.updateComment = function() {
        $http.put('/dssComment/', $scope.updateCommentForm).success(function (response) {
            if (response.code === '0000') {
                $scope.loadComment();
            } else {
                alert('댓글 수정 실패, ' + response.message);
            }
        });
    };

    // 기존에 열렀던 댓글의 모든 수정 폼을 닫는다.
    $scope.cancelForUpdateComment = function() {
        // 모든 커멘트의 수정 폼을 닫는다.
        angular.forEach($scope.comments.data, function(comment, data) {
            comment.isUpdate = false;
        });
    };

    // 댓글을 삭제한다.
    $scope.deleteComment = function(comment) {
        var confirmDeleteComment = confirm('댓글을 삭제하시겠습니까?');
        if (confirmDeleteComment) {
            $http.delete('/dssComment/' + comment.id).success(function (response) {
                if (response.code === '0000') {
                    $scope.loadComment();
                } else {
                    alert('삭제 실패, ' + response.message);
                }
            });
        }
    };

    // 댓글을 서버에서 다시 가져온다.
    $scope.loadComment = function() {
        $scope.comments = AjaxHelper.syncGetJSON('/dssComment/'+$stateParams.dssId, null);
        if ($scope.comments.code === '0000') {
            angular.forEach($scope.comments.data, function(comment, index) {
                comment.createDate = DateHelper.stringYYYYMMDDHHMISSToDate(comment.createDate);
            });
        }
    };
}
