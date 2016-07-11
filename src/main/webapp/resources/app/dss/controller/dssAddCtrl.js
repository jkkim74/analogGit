/**
 * Created by seoseungho on 2014. 9. 12..
 */
function dssAddCtrl($scope, $state, userSvc, $http, FileUploader, $rootScope) {
    'use strict';
    $scope.$state = $state;

    $scope.init = function () {
        var uploader = $scope.uploader = new FileUploader({
            url: '/file'
        });

        // 파일 업로드실패 시 모두 끝나면 리포트 보기로 이동한다
        uploader.onCompleteAll = function () {
            if (uploader.failToUploadFile) {
                var dssId = $scope.display.contentId;
                $http.delete('/dss/' + dssId, {deleteId: userSvc.getUser().username}).success(function () {
                    alert('파일 업로드에 문제가 발생해 보고서 등록에 실패했습니다. 관리자에게 문의해주세요.');
                    $scope.isDisabledSave = false;
                });
            } else {
                $state.go('main.dss.detail', {dssId: $scope.display.contentId});
            }
        };

        uploader.onCompleteItem = function (fileItem, response) {
            if (response.code !== '0000') {
                uploader.failToUploadFile = true;
                return false;
            }
        };

        // 파일 종류 별로 (보고서 파일, 데이터, 쿼리) 하나씩만 등록되도록 처리
        //var oneFileForTypeFilter = {
        //    name: 'oneFileForTypeFilter',
        //    fn: function (file, options) {
        //        var sameTypeFileIndex;
        //        angular.forEach(uploader.queue, function (fileItem, index) {
        //            if (options.alias === fileItem.alias) {
        //                sameTypeFileIndex = index;
        //            }
        //        });
        //
        //        if (sameTypeFileIndex >= 0) {
        //            uploader.removeFromQueue(sameTypeFileIndex);
        //        }
        //        return true;
        //    }
        //};
        //uploader.filters.push(oneFileForTypeFilter);

        // multi-step form 정보를 담기 위한 변수
        $scope.form = {
            'bmIdList': [],
            'subject': '',
            'content': '',
            'dataStart': '',
            'dataEnd': '',
            'analysisStart': '',
            'analysisEnd': '',
            'variables': []
        };

        $scope.bms = AjaxHelper.syncGetJSON('/dss/bmList', null);

        // display는 폼 전송이 될 필요는 없고, 모델을 담기 위해 필요 용도로 사용한다.
        // $scope.selectedBm을 다이렉트로 연결하는 경우 모델 바인딩에 문제가 발생.
        $scope.display = {};
        // select에 바인딩되는 모델
        $scope.display.selectedBm = '';
        // 리포트에 연관된 BM을 관리하는 모델
        $scope.display.bmList = [];
        // 변수 정보
        $scope.form.variables = [];

        // 달력 컴포넌트 제어를 위한 객체 생성
        $scope.datePicker = {};
        $scope.datePicker.format = 'yyyy.MM.dd';
        $scope.datePicker.isOpen = {};

//        var testData = {
//            'bmIdList': [],
//            'subject': '이것이 바로 주제입니다.',
//            'content': '보고서 내용입니다.',
//            'conclusion': '결과는 망 입니다.',
//            'futureWork': '앞으로 뭘 해야 할까요?',
//            'dataSource': '동네 주민센터에서 구한 자료',
//            'analysisStart': new Date(),
//            'analysisEnd': new Date(),
//            'dataStart': new Date(),
//            'dataEnd': new Date(),
//            'sampleSize': '999999999',
//            'variables': []
//        };
//        $scope.form = testData;

        $scope.currentStep = 1;

        $scope.$watch('dssAddForm.$dirty', function() {
            if ($scope.dssAddForm.$dirty) {
                $rootScope.updating = true;
            }
        });

        $scope.$watch('form.analysisStart', function() {
            if (!$scope.form.analysisEnd) {
                $scope.form.analysisEnd = $scope.form.analysisStart;
            }
        });
        $scope.$watch('form.dataStart', function() {
            if (!$scope.form.dataEnd) {
                $scope.form.dataEnd = $scope.form.dataStart;
            }
        });
    };

    // 새로운 리포트를 등록한다.
    $scope.save = function () {
        // save가 이미 실행중인 경우는 아무런 동작을 하지 않는다.
        if (!$scope.isDisabledSave) {
            $scope.isDisabledSave = true;
            if ($scope.validateReport()) {
                $scope.form.createId = userSvc.getUser().username;

                $http.post('/dss/', $scope.form).success(function (response) {
                    // DB저장이 끝나면 완료 처리한다.
                    $rootScope.updating = false;
                    var contentId = $scope.display.contentId = response.id;

                    // 파일 업로드용 공통 정보
                    var commonUploadInfo = [
                        {contentId: contentId},
                        {containerName: 'dss'},
                        {createId: $scope.form.createId}
                    ];

                    // dss 저장 성공 후 file 업로드
                    angular.forEach($scope.uploader.queue, function (uploadFile) {
                        // 파일 업로드 실패 후 다시 올리기 위해 파일의 속성을 변경한다
                        uploadFile.isUploaded = false;
                        uploadFile.formData = commonUploadInfo.slice(0);
                        if (uploadFile.alias === 'report') {
                            uploadFile.formData.push({'category': 'report'});
                        } else if (uploadFile.alias === 'data') {
                            uploadFile.formData.push({'category': 'data'});
                        } else if (uploadFile.alias === 'query') {
                            uploadFile.formData.push({'category': 'query'});
                        }
                    });

                    $scope.uploader.uploadAll();
                }).error(function() {
                    alert('보고서 등록에 실패했습니다. 관리자에게 문의하세요.');
                    $scope.isDisabledSave = false;
                });
            } else {
                $scope.isDisabledSave = false;
            }
        }

        return false;
    };

    // 리포트와 관련있는 BM을 등록, 기 등록되어 있는 BM의 경우는 다시 추가하지 않는다.
    $scope.addBm = function () {
        var needToAdd = true;
        if (!$scope.display.selectedBm) {
            needToAdd = false;
            return;
        } else {
            angular.forEach($scope.display.bmList, function (value) {
                // 값이 없거나 이미 선택한 값은 무시
                if ($scope.display.selectedBm === value) {
                    needToAdd = false;
                    return;
                }
            });
        }

        // 추가가 필요한 경우에 BM추가
        if (needToAdd) {
            $scope.display.bmList.push($scope.display.selectedBm);
            $scope.form.bmIdList.push($scope.display.selectedBm.id);
            $scope.display.selectedBm = '';
        }
    };

    // 해당되는 BM을 제거
    $scope.removeBm = function (bm) {
        var bmList = $scope.display.bmList;
        bmList.splice(bmList.indexOf(bm), 1);
        $scope.form.bmIdList.splice($scope.form.bmIdList.indexOf(bm.id), 1);
    };

    $scope.addVariable = function () {
        $scope.form.variables.push({'name': '', 'description': ''});
    };

    $scope.removeVariable = function ($index) {
        $scope.form.variables.splice($index, 1);
    };

    $scope.openDatePicker = function ($event, which) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.closeAllDatePickers();
        // 달력 컴포넌트를 연다.
        $scope.datePicker.isOpen[which] = true;
    };

    // 모든 달력 컴포넌트를 닫는다.
    $scope.closeAllDatePickers = function () {
        angular.forEach($scope.datePicker.isOpen, function (value, key) {
            $scope.datePicker.isOpen[key] = false;
        });
    };

    // 파일을 업로드 한다.
    $scope.upload = function () {
        // validation 체크
        if ($scope.validateReport()) {
            $scope.uploader.uploadAll();
        }
    };

    // 보고서 validation 체크
    $scope.validateReport = function () {
        var form = $scope.form;
        if ((!form.bmIdList || form.bmIdList.length === 0) ||
            (!form.subject || form.subject.length === 0) ||
            (!form.content || form.content.length === 0) ||
            (!form.analysisStart || form.content.length === 0) ||
            (!form.analysisEnd || form.analysisEnd.length === 0) ||
            (!form.dataSource || form.dataSource.length === 0) ||
            (!form.dataStart || form.dataStart.length === 0) ||
            (!form.dataEnd || form.dataEnd.length === 0)
            ) {
            alert('필수항목을 입력해주세요.');
            return false;
        }

        // 보고서 파일 첨부 확인
        var isReportFileExist = false;
        angular.forEach($scope.uploader.queue, function (value) {
            if (value.alias === 'report') {
                isReportFileExist = true;
            }
        });
        if (!isReportFileExist) {
            alert('보고서 파일을 업로드 해주세요.');
            return false;
        }
        return true;
    };

    // 보고서 등록 취소
    $scope.cancel = function () {
        $state.go('main.dss.menu', {menu: 'all'});
    };

    // 첨부 파일 취소
    // validation 체크를 위해 기존에 등록했었던 데이터를 제거한다.
    $scope.removeFile = function (fileItem) {
        $scope.uploader.removeFromQueue(fileItem);
    };

    $scope.goStep = function(phase) {
        // 이전 단계로 갈때는 validate 생략
        if ($scope.currentStep > phase|| $scope.validate($scope.currentStep)) {
            $scope.currentStep = phase;
        }
    };

    $scope.validate = function() {
        if ($scope.currentStep === 1) {
            if ($scope.form.bmIdList.length === 0) {
                alert('BM 유형을 선택해주세요.');
                return false;
            } else if ($scope.dssAddForm.subject.$invalid) {
                alert('분석 주제를 입력해주세요.');
                return false;
            } else if ($scope.dssAddForm.content.$invalid) {
                alert('분석 내용을 입력해주세요.');
                return false;
            } else if ($scope.dssAddForm.analysisStart.$invalid ||
                $scope.dssAddForm.analysisEnd.$invalid) {
                alert('분석 기간을 입력해주세요.');
                return false;
            }
        } else if ($scope.currentStep === 2) {
            if ($scope.dssAddForm.dataSource.$invalid) {
                alert('데이터 소스를 입력해주세요.');
                return false;
            } else if ($scope.dssAddForm.dataStart.$invalid ||
                $scope.dssAddForm.dataEnd.$invalid) {
                alert('데이터 기간을 입력해주세요.');
                return false;
            }
        } /*else if ($scope.currentStep === 3) {
            // 보고서 파일이 첨부가 안되어 있으면 에러처리 한다.
        }*/
        return true;
    };
}
