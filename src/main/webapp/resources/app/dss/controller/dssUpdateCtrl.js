/**
 * Created by seoseungho on 2014. 9. 26..
 */
function dssUpdateCtrl($scope, $state, $stateParams, FileUploader, $http, $rootScope) {
    'use strict';
    $scope.$state = $state;

    $scope.init = function() {
        //console.log('dssUpdateCtrl init dssId:', $stateParams);

        var uploader = $scope.uploader = new FileUploader({
            url: '/file'
        });

        $scope.dss = AjaxHelper.syncGetJSON('/dss/'+$stateParams.dssId, null);
        $scope.form = {};
        $scope.bms = AjaxHelper.syncGetJSON('/dss/bmList', null);
        $scope.form.bmIdList = [];

        // display는 폼 전송이 될 필요는 없고, 모델을 담기 위해 필요 용도로 사용한다.
        // $scope.selectedBm을 다이렉트로 연결하는 경우 모델 바인딩에 문제가 발생.
        $scope.display = {
            'selectedBm': '',
            'bmList': []
        };

        // form.bmIdList 와 display.bmList 를 채워준다.
        angular.forEach($scope.dss.bmList, function(bm, index) {
            $scope.form.bmIdList.push(bm.bmId);
            $scope.display.bmList.push({
                id: bm.bmId,
                name: bm.bmName
            });
        });

        // copy dss data to form so that update
        $scope.form.id = $scope.dss.id;
        $scope.form.subject = $scope.dss.subject;
        $scope.form.content = $scope.dss.content;
        $scope.form.conclusion = $scope.dss.conclusion;
        $scope.form.futureWork = $scope.dss.futureWork;
        $scope.form.dataSource = $scope.dss.dataSource;
        $scope.form.sampleSize = $scope.dss.sampleSize;

        new FileUploader.FileItem(uploader, {lastModifiedDate: null});
        angular.forEach($scope.dss.fileMetaList, function (fileMeta, index) {
            var fileItem = new FileUploader.FileItem(uploader, {lastModifiedDate: null});
            fileItem.fileMetaId = fileMeta.id;
            fileItem.isUploaded = true;
            fileItem.file.name = fileMeta.fileName;
            fileItem.alias = fileMeta.category;
            $scope.uploader.queue.push(fileItem);
        });

        // convert string date to Date type
        $scope.form.analysisStart = DateHelper.stringToDateObject($scope.dss.analysisStart);
        $scope.form.analysisEnd = DateHelper.stringToDateObject($scope.dss.analysisEnd);
        $scope.form.dataStart = DateHelper.stringToDateObject($scope.dss.dataStart);
        $scope.form.dataEnd = DateHelper.stringToDateObject($scope.dss.dataEnd);
        // parse json string to object
        if ($scope.dss.variablesJSON) {
            $scope.form.variables = angular.fromJson($scope.dss.variablesJSON);
        }

        // 달력 컴포넌트 제어를 위한 객체 생성
        $scope.datePicker = {};
        $scope.datePicker.format = 'yyyy.MM.dd';
        $scope.datePicker.isOpen = {};

        $scope.$watch('dssUpdateForm.$dirty', function() {
            if ($scope.dssUpdateForm.$dirty) {
                $rootScope.updating = true;
            }
        });
    };

    // 리포트와 관련있는 BM을 등록, 기 등록되어 있는 BM의 경우는 다시 추가하지 않는다.
    $scope.addBm = function() {
        var needToAdd = true;
        if (!$scope.display.selectedBm) {
            needToAdd = false;
            return;
        } else {
            angular.forEach($scope.display.bmList, function (value, key) {
                // 값이 없거나 이미 선택한 값은 무시
                if ($scope.display.selectedBm.id === value.id) {
                    needToAdd = false;
                    return;
                }
            });
        }

        // 추가가 필요한 경우에 BM추가
        if (needToAdd) {
            $scope.display.bmList.push($scope.display.selectedBm);
            $scope.form.bmIdList.push($scope.display.selectedBm.id);
        }
        $scope.display.selectedBm = '';
    };

    // 해당되는 BM을 제거
    $scope.removeBm = function(bm) {
        var bmList = $scope.display.bmList;
        bmList.splice(bmList.indexOf(bm), 1);
        $scope.form.bmIdList.splice($scope.form.bmIdList.indexOf(bm.id), 1);
    };

    $scope.addVariable = function() {
        $scope.form.variables.push({'name':'', 'description':''});
    };

    $scope.removeVariable = function($index) {
        $scope.form.variables.splice($index, 1);
    };

    // 달력 컴포넌트를 연다.
    $scope.openDatePicker = function($event, which) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.closeAllDatePickers();
        // 달력 컴포넌트를 연다.
        $scope.datePicker.isOpen[which]= true;
    };

    // 모든 달력 컴포넌트를 닫는다.
    $scope.closeAllDatePickers = function() {
        angular.forEach($scope.datePicker.isOpen, function(value, key) {
            $scope.datePicker.isOpen[key] = false;
        });
    };

    // 첨부 파일 취소
    // validation 체크를 위해 기존에 등록했었던 데이터를 제거한다.
    $scope.removeFile = function(fileItem) {
        fileItem.isCancel = true;
    };

    // 새로운 리포트를 등록한다.
    $scope.update = function() {
        var hasFileToUpload = false,
            hasFileToDelete = false,
            deleteFileIdList = [];

        if (!$scope.isDisabledSave) {
            $scope.isDisabledSave = true;
            $rootScope.updating = false;
            if ($scope.validateReport()) {
                // 파일 업로드용 공통 정보
                var commonUploadInfo = [
                    {contentId: $scope.dss.id},
                    {containerName: 'dss'}
                ];


                // 수정시엔 파일 업로드 후 파일 삭제(삭제한 경우만) 후 보고서 정보를 수정한다.
                angular.forEach($scope.uploader.queue, function (uploadFile, index) {
                    if (uploadFile.isCancel && uploadFile.isUploaded) {
                        hasFileToDelete = true;
                        deleteFileIdList.push(uploadFile.fileMetaId);
                        return;
                    } else if (uploadFile.isUploaded) {
                        return;
                    }
                    hasFileToUpload = true;

                    // 파일 업로드 실패 후 다시 올리기 위해 파일의 속성을 변경한다
                    uploadFile.formData = commonUploadInfo.slice(0);
                    if (uploadFile.alias === 'report') {
                        uploadFile.formData.push({'category': 'report'});
                    } else if (uploadFile.alias === 'data') {
                        uploadFile.formData.push({'category': 'data'});
                    } else if (uploadFile.alias === 'query') {
                        uploadFile.formData.push({'category': 'query'});
                    }
                });

                if (hasFileToUpload) {
                    $scope.uploader.onCompleteAll = function() {
                        $http.put('/dss/', $scope.form).success(function (response, status, headers, config) {
                            if (hasFileToDelete) {
                                $http.post('/file/deleteFile', deleteFileIdList).success(function (response, status, headers, config) {
                                    $state.go('main.dss.detail', {dssId: $scope.dss.id});
                                });
                            } else {
                                $state.go('main.dss.detail', {dssId: $scope.dss.id});
                            }
                        }).error(function (response, status, headers, config) {
                            $scope.isDisabledSave = false;
                            $rootScope.updating = true;
                            alert('보고서 수정 에실패했습니다. 관리자에게 문의하세요.');
                        });
                    };
                    $scope.uploader.uploadAll();
                } else {
                    $http.put('/dss/', $scope.form).success(function (response, status, headers, config) {
                        if (hasFileToDelete) {
                            $http.post('/file/deleteFile', deleteFileIdList).success(function (response, status, headers, config) {
                                $state.go('main.dss.detail', {dssId: $scope.dss.id});
                            });
                        } else {
                            $state.go('main.dss.detail', {dssId: $scope.dss.id});
                        }
                    }).error(function (response, status, headers, config) {
                        $scope.isDisabledSave = false;
                        $rootScope.updating = true;
                        alert('보고서 수정 에실패했습니다. 관리자에게 문의하세요.');
                    });
                }
//                console.log($scope.uploader.queue);
//                $http.put('/dss/', $scope.form).success(function (response, status, headers, config) {
//                    $state.go('main.dss.detail', {dssId: response.id});
//                }).error(function (response, status, headers, config) {
//                    $scope.isDisabledSave = false;
//                    alert('보고서 수정 에실패했습니다. 관리자에게 문의하세요.');
//                });
//            }  else {
//                $scope.isDisabledSave = false;
            } else {
                $scope.isDisabledSave = false;
                $rootScope.updating = true;
            }
        }
        return false;
    };

    // 보고서 validation 체크
    $scope.validateReport = function() {
        var form = $scope.dssUpdateForm;
        if ($scope.form.bmIdList.length === 0) {
            alert('BM 유형을 선택해주세요.');
            return false;
        } else if (form.subject.$invalid) {
            alert('분석 주제를 입력해주세요.');
            return false;
        } else if (form.content.$invalid) {
            alert('분석 내용을 입력해주세요.');
            return false;
        } else if (form.analysisStart.$invalid || form.analysisEnd.$invalid) {
            alert('분석 기간을 입력해주세요.');
            return false;
        } else if (form.dataSource.$invalid) {
            alert('데이터 소스를 입력해주세요.');
            return false;
        } else if (form.dataStart.$invalid || form.dataEnd.$invalid) {
            alert('데이터 기간을 입력해주세요.');
            return false;
        }


        // 보고서 파일 첨부 확인
        var isReportFileExist = false;
        angular.forEach($scope.uploader.queue, function (fileMeta, index) {
            if (fileMeta.alias === 'report' && !fileMeta.isCancel ) {
                isReportFileExist = true;
            }
        });
        if (!isReportFileExist) {
            alert('보고서 파일 첨부해주세요.');
            return false;
        }

        return true;
    };

    // 수정 취소
    $scope.cancel = function() {
        var cancelConfirm = confirm("취소하시겠습니까?");
        if (cancelConfirm) {
            $rootScope.updating = false;
            $state.go('main.dss.detail', {dssId: $scope.dss.id});
        }
    };
}
