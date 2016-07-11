describe("Unit: appCtrl Controllers", function () {
    var scope, createController;
    beforeEach(module('App'));
    beforeEach(inject(function($rootScope, $controller) {
        //declare the controller and inject our empty scope
        scope = $rootScope.$new();
        createController = function() {
            return $controller('dssAllCtrl', {
                '$scope': scope
            });
        };
    }));

    it('bmTypes을 검색 인자로 사용할지 여부를 결정하는 함수 확인. true일 경우 true 리턴, false면 false 리턴', function() {
        createController();
        expect(scope.useBmTypesForSearching()).toBe(false);
        expect(scope.useBmTypesForSearching(true)).toBe(true);
    });

    it('bmTypes id를 수집하는 함수, 입력되는 객체에서 프로퍼티명이 bm0으로 시작되는 항목에 대해서 id를 수집되야 한다.', function() {
        createController();
        var testData = {
                'bm01': {
                    'id': 51
                },
                'bm03': {
                    'id': 53
                }
            },
            testData2 = {
                'bm01': {
                    'id': 21
                },
                'bm2': {
                    'id': 25
                }
            };
        expect(scope.collectBmIds(testData)).toEqual([51,53]);
        expect(scope.collectBmIds(testData2)).toEqual([21]);

    });
});

