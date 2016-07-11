<%@ page contentType="text/html;charset=utf-8" session="false" trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="ko" class="no-js" data-ng-app="App">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title> Voyager </title>
    <meta name="description" content="Voyager">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="/resources/images/favicon.ico"/>
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="No-Cache">

    <link href="resources/bs3/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="resources/js/jquery-ui/jquery-ui.css" rel="stylesheet"/>
    <link href="resources/css/bootstrap-reset.css" rel="stylesheet"/>
    <link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="resources/sweetalert/dist/sweet-alert.css">
    <link href="resources/css/style.css" rel="stylesheet"/>
    <link href="resources/css/reform.css" rel="stylesheet"/>
    <link href="resources/css/style-responsive.css" rel="stylesheet"/>
    <link href="resources/css/datepicker.css" rel="stylesheet"/>
    <link href="resources/jqgrid/css/ui.jqgrid.css" media="screen" rel="stylesheet" type="text/css">
    <link href="resources/jqgrid/css/jqGrid.bootstrap.css" media="screen" rel="stylesheet" type="text/css">
    <link href="resources/d3/nv3d/nv.d3.css" media="screen" rel="stylesheet" type="text/css">
    <link href="resources/ngtable/ng-table.css" rel="stylesheet"/>
    <link href="resources/summernote/dist/summernote.css" rel="stylesheet"/>
    <link href="resources/summernote/dist/summernote-bs3.css" rel="stylesheet"/>
    <link href="resources/css/angular-ui-tree.min.css" rel="stylesheet"/>
    <link href="resources/carousel/dist/angular-carousel.min.css" rel="stylesheet"/>
    <link href="resources/angular-block-ui/angular-block-ui.min.css" rel="stylesheet"/>
    <link href="resources/css/ellipsis.css" rel="stylesheet"/>
    <link rel="stylesheet" href="resources/ngDialog/css/ngDialog.css">
    <link rel="stylesheet" href="resources/ngDialog/css/ngDialog-theme-default.css">
    <link rel="stylesheet" href="resources/ngDialog/css/ngDialog-theme-plain.css">
    <script src="resources/js/modernizr-2.8.3.min.js"></script>
</head>
<body data-ng-class="{'main':mainCssState}" data-ng-controller="appCtrl" data-ng-init="pop()">
<!-- main -->
<div ui-view></div>
<!--// main -->
<!-- script for this page -->
<script type="text/javascript">
    var CONTEXT_ROOT = '${pageContext.request.contextPath}';
    var API_BASE_URL = '${apiBaseUrl}';
</script>
<script src="resources/js/jquery-1.10.2.min.js"></script>
<script src="resources/js/jquery.cookie.js"></script>
<script src="resources/js/jquery-ui/jquery-ui-1.10.1.custom.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
<script src="resources/js/jquery.dcjqaccordion.2.7.js"></script>
<script src="resources/js/jquery.scrollTo.min.js"></script>
<script src="resources/js/jquery.slimscroll.min.js"></script>
<script src="resources/js/jquery.nicescroll.js"></script>
<script src="resources/js/bootstrap-datepicker.js"></script>
<script src="resources/js/voyager.extends.js"></script>
<script type="text/javascript" src="resources/js/moment-with-locales.min.js"></script>
<script type="text/javascript" src="resources/angularjs/angular.min.js"></script>
<script type="text/javascript" src="resources/angularjs/angular-touch.min.js"></script>
<script type="text/javascript" src="resources/angularjs/Scope.SafeApply.js"></script>
<script type="text/javascript" src="resources/carousel/dist/requestAnimationFrame.min.js"></script>
<script type="text/javascript" src="resources/carousel/dist/angular-carousel.min.js"></script>
<script type="text/javascript" src="resources/angularjs/angular-sanitize.min.js"></script>
<script type="text/javascript" src="resources/angularjs/angular-ui/angular-ui-router.min.js"></script>
<script type="text/javascript" src="resources/angularjs/angular-ui/sortable.js"></script>
<script type="text/javascript" src="resources/angularjs/angular-ui/ui-bootstrap-tpls-0.11.0.min.js"></script>
<script type="text/javascript" src="resources/angularjs/angular-ui/angular-ui-tree.min.js"></script>
<script type="text/javascript" src="resources/angularjs/angular-cookies.min.js"></script>
<script type="text/javascript" src="resources/d3/3.4.6/d3-custom.js"></script>
<script type="text/javascript" src="resources/d3/nv3d/nv.d3.min.js"></script>
<script type="text/javascript" src="resources/d3/angularjs-nvd3-directives/angularjs-nvd3-directives.js"></script>
<script type="text/javascript" src="resources/jqgrid/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="resources/jqgrid/js/jquery.jqGrid.js"></script>
<script type="text/javascript" src="resources/app/common/controllers/common-controllers.js"></script>
<script type="text/javascript" src="resources/app/common/services/common-factory.js"></script>
<script type="text/javascript" src="resources/app/common/directives/common-directive.js"></script>
<script type="text/javascript" src="resources/app/common/utils/helpers.js"></script>
<script type="text/javascript" src="resources/ngtable/ng-table.js"></script>
<script type="text/javascript" src="resources/summernote/dist/summernote.min.js"></script>
<script type="text/javascript" src="resources/summernote/angular-dist/angular-summernote.min.js"></script>
<script type="text/javascript" src="resources/angular-file-upload/angular-file-upload.js"></script>
<script type="text/javascript" src="resources/truncate/truncate.js"></script>
<script type="text/javascript" src="resources/app/app.js"></script>
<script type="text/javascript" src="resources/ngDialog/js/ngDialog.min.js"></script>

<script type="text/javascript" src="resources/ng-infinite-scroll/ng-infinite-scroll.js"></script>
<script type="text/javascript" src="resources/angular-block-ui/angular-block-ui.min.js"></script>
<script type="text/javascript" src="resources/ellipsis/jquery.ellipsis.js"></script>
<script type="text/javascript" src="resources/underscore/underscore-min.js"></script>
<script type="text/javascript" src="resources/underscore/angular-underscore.min.js"></script>
<script type="text/javascript" src="resources/sweetalert/dist/sweet-alert-custom.js"></script>
<script type="text/javascript" src="resources/highchart/4.1.7/js/highcharts.js"></script>
<script type="text/javascript" src="resources/highchart/4.1.7/js/modules/exporting.js"></script>
<script type="text/javascript" src="resources/highchart/4.1.7/js/themes/gray.js"></script>
<%--<script type="text/javascript" src="resources/angularjs/hint.js"></script>--%>

</body>
</html>
