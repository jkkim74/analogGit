<%@ page contentType="text/html;charset=utf-8" session="false" trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="ko" ng-app="DAU">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%--<meta http-equiv="Expires" content="-1">--%>
    <%--<meta http-equiv="Pragma" content="no-cache">--%>
    <%--<meta http-equiv="Cache-Control" content="No-Cache">--%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>

    <title> DAU Display </title>
    <link href="resources/bs3/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="resources/js/jquery-ui/jquery-ui-1.10.1.custom.min.css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-reset.css" rel="stylesheet"/>
    <link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="resources/dau/pageTransitions/css/default.css" rel="stylesheet"/>
    <link href="resources/dau/pageTransitions/css/multilevelmenu.css" rel="stylesheet"/>
    <link href="resources/dau/pageTransitions/css/component-voyager.css" rel="stylesheet"/>
    <link href="resources/dau/pageTransitions/css/animations.css" rel="stylesheet"/>
    <link href="resources/d3/nv3d/nv.d3.css" media="screen" rel="stylesheet"/>
    <link href="resources/css/dau.css" rel="stylesheet" type="text/css">


    <!-- Fav and touch icons -->
    <link rel="shortcut icon" href="/resources/images/favicon.ico"/>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="resources/js/html5shiv.js"></script>
    <script src="resources/js/respond.min.js"></script>
    <![endif]-->
    <script src="resources/js/jquery-1.10.2.min.js"></script>
    <script src="resources/js/jquery.placeholder.js"></script>
    <script src="resources/js/jquery-ui/jquery-ui-1.10.1.custom.min.js"></script>
    <script src="resources/js/bootstrap.min.js"></script>
    <script src="resources/angularjs/angular.min.js"></script>
    <script src="resources/angularjs/Scope.SafeApply.js"></script>

    <script src="resources/dau/pageTransitions/js/modernizr.custom.js"></script>
    <script src="resources/flip/jquery.flip.js"></script>
    <%--<script src="resources/d3/3.4.6/d3.min.js"></script>--%>
    <%--<script src="resources/d3/nv3d/nv.d3.min.js"></script>--%>
    <%--<script src="resources/d3/angularjs-nvd3-directives/angularjs-nvd3-directives.js"></script>--%>
    <script src="resources/d3/3.4.6/d3-custom.js"></script>
    <script src="resources/d3/nv3d/nv.d3-custom.js"></script>
    <script src="resources/d3/angularjs-nvd3-directives/angularjs-nvd3-directives-custom.js"></script>

    <script type="text/javascript" src="resources/js/moment-with-locales.min.js"></script>
    <script type="text/javascript" src="resources/jquery/jquery.DigitalClock.js"></script>
    <script type="text/javascript" src="resources/app/common/utils/helpers.js"></script>
    <script type="text/javascript" src="resources/cron/cron.debug.js"></script>

    <script type="text/javascript" src="resources/dau/dau.js"></script>
    <script type="text/javascript" src="resources/dau/dauCtrl.js"></script>

</head>
<body data-ng-controller="dauCtrl" data-ng-init="dauInit()">

<div class="pt-triggers">
    <span id="sendDauError" data-ng-show="false">error occur</span>
</div>

<div id="pt-main" class="pt-perspective">
    <div class="pt-page pt-page-1">
        <div class="realTime">
            <ul class="mainBox">
                <li class="boxTitle">
                    <div class="dauTimer">
                        <span class="timer-font-c timer-font-l dau142"><img src="../../resources/images/dau/timerIcon.png"/></span>&nbsp;&nbsp;
                        <span id="clock1" class="timer-font-c timer-font-l dau142"></span>
                        <span id="ampm" class="timer-font-c timer-font-m dau142"></span>
                    </div>
                    <div class="skpLogo">
                        <img src="../../resources/images/dau/skplogo.png"/>
                    </div>
                </li>
                <li class="boxContent">
                    <ul class="dauBox">
                        <li class="box1">
                            <div id="totalBox">
                                <h2 class="dau142">SOS&nbsp;DAU</h2>
                                <h4 class="dau142"> (OCB +Syrup)</h4>
                                <span class="num-c num-l dau152"
                                      id="curTotalValidUser">{{dauDataSet.totalValidUserCount | number : 0}}</span>
                            </div>
                            <div id="yesterBox">
                                <h2 class="dau142">최근 30일 평균</h2>
                                <h3 class="dau142">{{yesterdayDauDataSet.acmTotal | number : 0}}</h3>
                                <div class="time"><!--시간-->
                                    <span id = "manPointIndex"><img ng-src="../../resources/images/dau/{{manPointIndex}}hour.png"></span>
                                </div>
                                <img src="../../resources/images/dau/man_bg.png" class="man_bg">
                                <div class="man">
                                    <div class="bar"></div><!--progress bar-->
                                </div>
                            </div>
                        </li>
                        <li class="box3">
                            <div id="ocbBox">
                                <h3 class="dau142">OCB</h3>
                                <span class="num-c num-m dau152" id="curOcbValidUser">{{dauDataSet.ocbValidUserCount | number : 0}}</span>
                                <h4 class="dau142">최근 30일 평균<font>{{yesterdayDauDataSet.ocbAcmTotal | number : 0}}</font></h4><!--추가-->
                            </div>
                        </li>
                        <li class="box4">
                            <div id="syrupBox">
                                <h3 class="dau142">Syrup</h3>
                                <span class="num-c num-m dau152" id="curSyrupValidUser">{{dauDataSet.syrupValidUserCount | number : 0}}</span>
                                <h4 class="dau142">최근 30일 평균<font>{{yesterdayDauDataSet.syrupAcmTotal | number : 0}}</font></h4><!--추가-->
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div class="pt-page pt-page-2">
        <div class="pastTime">
            <ul class="dauBox">
                <li class="boxTitle">
                    <div><h1 class="dau142">Notice.</h1></div>
                </li>
                <li>
                    <span id="dauErrorState">{{errorMessage}}</span>
                </li>
            </ul>
        </div>
    </div>
</div>

<div class="pt-message">
    <p>Your browser does not support CSS animations.</p>
</div>

<script src="resources/dau/pageTransitions/js/jquery.dlmenu.js"></script>
<script src="resources/dau/pageTransitions/js/pagetransitions.js"></script>
</body>
</html>
