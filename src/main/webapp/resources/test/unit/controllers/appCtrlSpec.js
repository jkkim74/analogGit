describe("Unit: appCtrl Controllers", function () {
    var $scope, $location;

    beforeEach(function() {
        module("App");
        module("app.commonControllers");
        module("app.commonFactory");
        inject(['$injector', '$rootScope', '$cookies', '$controller', '$location', 'userSvc',
            function($injector, $rootScope, $cookies, $controller, _$location, userSvc) {
                //voyager cookie 셋팅.
                $cookies.voyager_master = "59cd1QY79s0WQSIJrL9Xxo3LunsXeowVO4NfGGfdswKralbj9QYMur%2FgmzMnLvK7DiYhM8qiFCLUoc2%2BvkHECLyU0fnsCZF5r5PIkvAIXEjl6MplixOqBbrK1o7el03cjRLF8OggqGoPdO6V%2BgZNZw%3D%3D";
                $location = _$location;
                //declare the controller and inject our empty scope
                $scope = $rootScope.$new();
                userSvc.setUser({id: 502, username: 'PP39093'});
                $controller('appCtrl', {$scope: $scope});
            }
        ]);
    });

    it('should have a method to check if the path is active', function() {
        $location.path('/index');
        expect($location.path()).toBe('/index');
    });

    it('should have variable isLogin = true', function() {
        expect($scope.isLogin).toEqual(true);
    });

    it('should have isAdmin function = true', function() {
        expect($scope.isAdmin()).toEqual(true);
    });
});

