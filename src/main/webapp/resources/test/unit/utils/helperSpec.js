describe('helpers tests', function () {
    describe('DateHelper tests', function() {
        it('should diff value', function() {
            expect(DateHelper.diff('20140105', '20140108')).toEqual(3);
        });
        it('should previous date value', function() {
            expect(DateHelper.getPreviousDateFromDate('2014.01.05', 1, 'YYYYMMDD')).toEqual('20140104');
        });

        it('should conversion pattern date value', function() {
            expect(DateHelper.stringToYmdStr('20140105', '.')).toEqual('2014.01.05');
        });

        it('should date yyyymmdd pattern value', function() {
            expect(DateHelper.dateToParamString('2014.01.05')).toEqual('20140105');
        });

        it('should valid date value', function() {
            expect(DateHelper.isValid('20140132', 'YYYYMMDD')).toBeFalsy();
        });

        it('should date to string(yymmdd) value', function() {
            var preDate = new Date(DateHelper.getDailyDiffMs(DateHelper.stringToDateObject('20140105'), 1));
            expect(DateHelper.dateObjectToYymmdd(preDate)).toEqual('14.01.04');
        });

        it('should date to string(yyyymmdd) value', function() {
            var preDate = new Date(DateHelper.getDailyDiffMs(DateHelper.stringToDateObject('20140105'), 1));
            expect(DateHelper.dateObjectToYyyymmdd(preDate, '.')).toEqual('2014.01.04');
        });

        it('should date to string(yyyymmdd) value', function() {
            var weekDate = DateHelper.getWeekendRange(new Date('2014-01-05'));
            expect(weekDate.mondayYmd).toBe('13.12.30');
            expect(weekDate.sundayYmd).toBe('14.01.05');
        });

        it('should date to string(yyyy.mm.dd) value', function() {
            var weekDate = new Date('2014-01-05');
            expect(weekDate.ymd()).toBe('2014.01.05');
        });
    });

    describe('StringHelper tests', function() {
        it('should start with value', function() {
            expect(StringHelper.startWith('pivottable', 'pivot')).toBeTruthy();
        });

        it('should empty value', function() {
            var param1 = '';
            expect(StringHelper.isNotEmpty(param1)).toBeFalsy();
            var param2 = null;
            expect(StringHelper.isNotEmpty(param2)).toBeFalsy();
        });
    });

    describe('CalcHelper tests', function() {
        it('should percentage value', function() {
            //console.log(CalcHelper.percentageTwoNumbers(56550, 2600));
            expect(CalcHelper.percentageTwoNumbers(56550, 2600)).toBe('-95.4');
        });

        it('should number slice value', function() {
            //console.log(CalcHelper.sliceByMinutes(300, 450));
            expect(CalcHelper.sliceByMinutes(300, 450)).toBe('2.500');
        });
    });

    describe('FormatHelper tests', function() {
        it('should padding left value', function() {
            expect(FormatHelper.padLeft(100, 5, '0')).toBe('00100');
        });

        it('should format value', function() {
            expect(1234..numberFormat()).toBe('1,234');
        });
    });
});
