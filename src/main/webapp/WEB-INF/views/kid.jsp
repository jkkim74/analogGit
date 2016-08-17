<!DOCTYPE html>
<html lang="ko" ng-app="kid">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%--<meta http-equiv="Expires" content="-1">--%>
    <%--<meta http-equiv="Pragma" content="no-cache">--%>
    <%--<meta http-equiv="Cache-Control" content="No-Cache">--%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>

    <title> Key Index Dashboard Display </title>
    <link href="resources/bs3/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="resources/js/jquery-ui/jquery-ui-1.10.1.custom.min.css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-reset.css" rel="stylesheet"/>
    <link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="resources/kid/pageTransitions/css/default.css" rel="stylesheet"/>
    <link href="resources/kid/pageTransitions/css/multilevelmenu.css" rel="stylesheet"/>
    <link href="resources/kid/pageTransitions/css/component-kid.css" rel="stylesheet"/>
    <link href="resources/kid/pageTransitions/css/animations.css" rel="stylesheet"/>
    <link href="resources/d3/nv3d/nv.d3.css" media="screen" rel="stylesheet"/>
    <link href="resources/css/kid.css" rel="stylesheet" type="text/css">


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

    <script src="resources/kid/pageTransitions/js/modernizr.custom.js"></script>
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

    <script type="text/javascript" src="resources/kid/kid.js"></script>
    <script type="text/javascript" src="resources/kid/kidCtrl.js"></script>

</head>
<body data-ng-controller="kidCtrl" data-ng-init="kidInit()">

<div class="pt-triggers">
    <span id="changeView" data-ng-show="false">cookatrice</span>
</div>

<div id="pt-main" class="pt-perspective">
    <div class="pt-page pt-page-1">
        <div class="test"></div>
    </div>
    <div class="pt-page pt-page-2">
        <div class="test2"></div>
    </div>
    <div class="pt-page pt-page-3">
        <div class="test3"></div>
    </div>
    <div class="pt-page pt-page-4">
        <div class="test4"></div>
    </div>
    <div class="pt-page pt-page-5">
        <div class="test5"></div>
    </div>
</div>

<div class="pt-message">
    <p>Your browser does not support CSS animations.</p>
</div>

<script src="resources/kid/pageTransitions/js/jquery.dlmenu.js"></script>
<script src="resources/kid/pageTransitions/js/pagetransitions.js"></script>
</body>
</html>
