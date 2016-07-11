(function ($) {
    var _options = {};
    var _container = {};

    jQuery.fn.DigitalClock = function (options) {
        var id = $(this).get(0).id;
        _options[id] = $.extend({}, $.fn.DigitalClock.defaults, options);

        return this.each(function () {
            _container[id] = $(this);
            showClock(id);
        });

        function showClock(id) {
            var templateStr = _options[id].timeFormat;
            var now = moment().format("hhmmssdA");
            templateStr = templateStr.replace("{HH}", now[0] + now[1]);
            templateStr = templateStr.replace("{MM}", now[2] + now[3]);

            $("#" + id).html(templateStr);
            $('#ampm').html(now[7]+now[8]);
            var weekdays = $('.weekdays span');

            var dow = now[6];
            dow--;
            // Sunday!
            if(dow < 0){
                // Make it last
                dow = 6;
            }
            weekdays.removeClass('active').eq(dow).addClass('active');
            //toggle hands
            setTimeout(function () {
                showClock(id)
            }, 1000);
        }
    };

    //default values
    jQuery.fn.DigitalClock.defaults = {
        timeFormat: '{HH}:{MM}',
        bAmPm: true
    };
})(jQuery);