/**
 * Created by cookatrice on 2014. 6. 5..
 */

function jqueryUiCtrl($scope, $http, API_BASE_URL) {

    $scope.myKnob = {
        data: 88,
        id: "myKnob",
        min: -100,
        max: 100,
        step: 5,
        angleOffset: 60,
        angleArc: 180,
        stopper: true,
        readOnly: false,
        width: 120
    };
    $scope.myKnob2 = {
        data: 3,
        id: "myKnob2",
        min: 0,
        max: 100,
        step: 1,
        angleOffset: 60,
        angleArc: 180,
        stopper: true,
        readOnly: false,
        cursor: 5,
        lineCap: 'round',
        displayInput: true,
        width: 120,
        displayPrevious: true,
        fgColor: 'FF9382',
        font: 'apple'
    };
    $scope.myKnob3 = {
        data: 30,
        id: "myKnob3",
        min: 0,
        max: 100,
        step: 1,
        angleOffset: 60,
        angleArc: 180,
        stopper: true,
        readOnly: false,
        cursor: 5,
        lineCap: 'round',
        displayInput: true,
        width: 120,
        displayPrevious: true,
        fgColor: 'FF9382',
        bgColor: 'FFFF34'
    };

    $scope.init = function () {

        $scope.drawSlider();
        $scope.drawDefaultKnob();
        $scope.drawTestKnob();

    };


    function changeValue() {
        $(".myKnob").each(function (i, e) {
            if ($(e).val() > 0) {
                $(e).val($(e).val() * Math.random() * 3);
            } else {
                $(e).val(10);
            }
        }).trigger('change');

        $("#testKnob7").val(
                (Math.random() * 40) + "%"
        ).trigger('change');
    };

    $("#btnClick").click(function () {
        changeValue();
    });

    $scope.drawSlider = function () {
        $("#slider-range").slider({
            range: true,
            min: -100,
            max: 100,
            values: [ -50, 50 ],
            slide: function (event, ui) {
                $("#amount").val("$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ]);
            }
        });
        $("#amount").val("$" + $("#slider-range").slider("values", 0) + " - $" + $("#slider-range").slider("values", 1));
    };

    $scope.drawDefaultKnob = function () {
        $(".defaultKnob").knob();
    };


    /**
     *
     // Config
     min : this.$.data('min') !== undefined ? this.$.data('min') : 0,
     max : this.$.data('max') !== undefined ? this.$.data('max') : 100,
     stopper : true,
     readOnly : this.$.data('readonly') || (this.$.attr('readonly') === 'readonly'),

     // UI
     cursor : (this.$.data('cursor') === true && 30) ||
     this.$.data('cursor') || 0,
     thickness : (
     this.$.data('thickness') &&
     Math.max(Math.min(this.$.data('thickness'), 1), 0.01)
     ) || 0.35,
     lineCap : this.$.data('linecap') || 'butt',
     width : this.$.data('width') || 200,
     height : this.$.data('height') || 200,
     displayInput : this.$.data('displayinput') == null || this.$.data('displayinput'),
     displayPrevious : this.$.data('displayprevious'),
     fgColor : this.$.data('fgcolor') || '#87CEEB',
     inputColor: this.$.data('inputcolor'),
     font: this.$.data('font') || 'Arial',
     fontWeight: this.$.data('font-weight') || 'bold',
     inline : false,
     step : this.$.data('step') || 1,
     rotation: this.$.data('rotation'),

     // Hooks
     draw : null, // function () {}
     change : null, // function (value) {}
     cancel : null, // function () {}
     release : null, // function (value) {}

     // Output formatting, allows to add unit: %, ms ...
     format: function(v) {
            return v;
        },
     parse: function (v) {
            return parseFloat(v);
        }
     */
    $scope.drawTestKnob = function () {
        $("#testKnob").knob({
            min: -100,
            max: 100,
            step: 5,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            width: 120
        });
        $("#testKnob0").knob({
            min: -100,
            max: 100,
            step: 5,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            thickness: 0.05,
            width: 120
        });
        $("#testKnob1").knob({
            min: -100,
            max: 100,
            step: 5,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            width: 120,
            ticks: 8,
            draw: function () {
                // "tron" case
                if (this.$.data('skin') == 'tron') {

                    this.cursorExt = 0.3;

                    var a = this.arc(this.cv)  // Arc
                        , pa                   // Previous arc
                        , r = 1;

                    this.g.lineWidth = this.lineWidth;

                    if (this.o.displayPrevious) {
                        pa = this.arc(this.v);
                        this.g.beginPath();
                        this.g.strokeStyle = this.pColor;
                        this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, pa.s, pa.e, pa.d);
                        this.g.stroke();
                    }

                    this.g.beginPath();
                    this.g.strokeStyle = r ? this.o.fgColor : this.fgColor;
                    this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, a.s, a.e, a.d);
                    this.g.stroke();

                    this.g.lineWidth = 2;
                    this.g.beginPath();
                    this.g.strokeStyle = this.o.fgColor;
                    this.g.arc(this.xy, this.xy, this.radius - this.lineWidth + 1 + this.lineWidth * 2 / 3, 0, 2 * Math.PI, false);
                    this.g.stroke();

                    return false;
                }
            }
        });

        $("#testKnob2").knob({
            min: -20,
            max: 50,
            step: 2,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            cursor: 30,
            lineCap: 'round',
            width: 120,
            displayInput: false

        });
        $("#testKnob3").knob({
            min: 0,
            max: 100,
            step: 1,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            cursor: 10,
            lineCap: 'buzz',
            width: 120
        });
        $("#testKnob4").knob({
            min: 0,
            max: 100,
            step: 1,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            cursor: 5,
            lineCap: 'round',
            displayInput: true,
            width: 120,
            inputColor: 'F028ff',
            fontWeight: '9px'

        });
        $("#testKnob5").knob({
            min: 0,
            max: 100,
            step: 1,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            cursor: 5,
            lineCap: 'round',
            displayInput: true,
            width: 120,
            displayPrevious: true,
            fgColor: 'FF9382',
            font: 'apple'

        });
        $("#testKnob6").knob({
            min: 0,
            max: 100,
            step: 1,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            cursor: 5,
            lineCap: 'round',
            displayInput: true,
            width: 120,
            displayPrevious: true,
            fgColor: 'chartreuse',
            bgColor: 'FFFF34',
            release: function (v) {
                //console.log("6 release : " + v);
            },
            change: function (v) {
                //console.log("6 change : " + v);
            },
            draw: function () {
            },
            cancel: function (v) {
                //console.log("6 cancel : " + v);
            }
        });
        $("#testKnob7").knob({
            min: 0,
            max: 100,
            step: 1,
            angleOffset: 60,
            angleArc: 180,
            stopper: true,
            readOnly: false,
            cursor: 5,
            lineCap: 'round',
            displayInput: true,
            width: 120,
            displayPrevious: true,
            fgColor: 'FF9382',
            bgColor: 'FFFF34',
            release: function (v) {
                //console.log("7 release : " + v);
            },
            change: function (v) {
                //console.log("change : " + v);
            },
            draw: function () {
            },
            cancel: function (v) {
                //console.log("cancel : " + v);
            },
            format: function (v) {
                return v + "%";
//                return v;
            }
        });

        $('#testKnob8').val(0).trigger('change').delay(2000);
        $("#testKnob8").knob({
            'min': 0,
            'max': 100,
            'readOnly': true,
            'width': 120,
            'height': 120,
            'fgColor': '#b9e672',
            'dynamicDraw': true,
            'thickness': 0.2,
            'tickColorizeValues': true,
            'skin': 'tron'
        });

        var tmr = self.setInterval(function () {
            myDelay();
            changeValue();
        }, 1000);
        var m = 0;

        function myDelay() {
            m += 100;
            $('#testKnob8').val(m).trigger('change');
            if (m > 100) {
                window.clearInterval(tmr);
            }
        }

    };
}
