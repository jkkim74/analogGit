/**
 * Created by seoseungho on 2014. 10. 27..
 */
/* jshint -W097 */
'use strict';
describe('App', function() {
    var $scope,
        mockState = {};

    beforeEach(module(function ($provide) {
        $provide.value('$state', mockState);
    }));

    beforeEach(inject(function ($rootScope, $controller) {
        $scope = $rootScope.$new();
        $controller('dssCtrl', {$scope: $scope});
    }));

    it('$scope should have value', inject(function ($controller) {
        // todo test something here
    }));
});