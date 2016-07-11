describe('reportSvc tests', function () {
    var $injector, $state, reportSvc;
    beforeEach(module("App"));
    beforeEach(module("app.commonFactory"));

    beforeEach(function () {
        inject(function (_reportSvc_, $timeout, $http, $q, _$injector_, _$state_) {
            reportSvc = _reportSvc_;
            $injector = _$injector_;
            $state = _$state_;
        });
    })

    describe('when invoked', function () {
        it('should have an defaultCal function', function () {
            expect(reportSvc.defaultCal('day')).toBeDefined();
        });

        it('should have an newMenuItem function', function () {;
            var service = {
                code: 'ocb',
                categories: [{code: 'summary'},{code: '01'}, {code: '02'}]
            };
            var category = {code: 'summary', menus : [{code: 'report'}]};
            var menu = {code: 'report', name: '요약리포트'};
            var menuItem = reportSvc.newMenuItem(service, category, menu);
            expect(menuItem.service.code).toBe('ocb');
        });
    });

});
