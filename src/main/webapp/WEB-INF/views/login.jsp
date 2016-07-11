<%@ page contentType="text/html;charset=utf-8" session="false" trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="ko" class="no-js" ng-app="Login">
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
	<link href="resources/bs3/css/bootstrap.min.css" rel="stylesheet" />
	<link href="resources/js/jquery-ui/jquery-ui-1.10.1.custom.min.css" rel="stylesheet" />
	<link href="resources/css/bootstrap-reset.css" rel="stylesheet" />
	<link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
	<link href="resources/css/style.css" rel="stylesheet" />
	<link href="resources/css/reform.css" rel="stylesheet" />
	<link href="resources/css/style-responsive.css" rel="stylesheet" />
	<link href="resources/carousel/dist/angular-carousel.min.css" rel="stylesheet" />
	<!-- Fav and touch icons -->
	<link rel="shortcut icon" href="/resources/images/favicon.ico" />
	<script src="resources/js/modernizr-2.8.3.min.js"></script>
	<script src="resources/js/jquery-1.10.2.min.js"></script>
	<script src="resources/js/jquery.placeholder.js"></script>
	<script src="resources/js/jquery-ui/jquery-ui-1.10.1.custom.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/scripts.js"></script>
</head>
<body class="main">
<!-- header start -->
<header class="header fixed-top clearfix">
	<div class="container">
		<h1 class="navbar-brand brand">
			<a href="/login" class="logo">
				Voyager <span>Beta</span>
			</a>
			<div class="sidebar-toggle-box visible-xs">
				<div id="bg-control" class="fa fa-bars"></div>
			</div>
		</h1>
		<div class="navbar-collapse collapse header-nav">
			<ul class="nav navbar-nav navbar-left">
				<li>
					<a href="#" data-target="#login-popup" data-toggle="modal">Dashboard</a>
				</li>
				<li>
					<a href="#" data-target="#login-popup" data-toggle="modal">Report</a>
				</li>
				<li>
					<a href="#" data-target="#login-popup" data-toggle="modal">Dataset Sharing</a>
				</li>
				<li>
					<a href="#" data-target="#login-popup" data-toggle="modal">Data White Book</a>
				</li>
				<li>
					<a href="#" data-target="#login-popup" data-toggle="modal">Help Desk</a>
				</li>
				<li class="search-bar">
					<button type="button" class="search-toggle" data-target="#login-popup" data-toggle="modal">
						<i class="fa fa-search"></i>
					</button>
				</li>
			</ul>
		</div>
	</div>
</header>
<section class="main-container" resize>
	<section class="container">
		<div class="main-banner">
            <ul rn-carousel rn-carousel-indicator rn-carousel-auto-slide="3" rn-carousel-control="true"
                rn-carousel-pause-on-hover="true" class="my-slider standard">
                <li>
                    <img src="resources/images/main/carousel01.jpg" class="img-responsive" >
                </li>
                <li>
                    <img src="resources/images/main/carousel02.jpg" class="img-responsive" >
                </li>
                <li>
                    <img src="resources/images/main/carousel03.jpg" class="img-responsive" >
                </li>
                <li>
                    <img src="resources/images/main/carousel04.jpg" class="img-responsive" >
                </li>
                <li>
                    <img src="resources/images/main/carousel05.jpg" class="img-responsive" >
                </li>
                <li>
                    <img src="resources/images/main/carousel06.jpg" class="img-responsive" >
                </li>
                <!--<li style="text-align: center;">-->
                    <!--<iframe width="100%" height="334px" src="//www.youtube.com/embed/Xgz0_iiiRYA" frameborder="0" allowfullscreen></iframe>-->
                <!--</li>-->
            </ul>
			<%--<img src="/resources/images/main/main_banner_lg.jpg" alt="main_banner_lg" style="width:100%;height:334px;" />--%>
		</div>
		<article class="row main-article">
			<div class="login-area obj-clear">
				<div class="login-con">
					<div class="input-box">
						<div class="input-id">
							<label for="login-username" class="hidden-text">Id</label>
							<input type="text" id="login-username" class="form-control" placeholder="Pnet ID" />
						</div>
						<div class="input-password">
							<label for="login-password"class="hidden-text">Password</label>
							<input type="password" id="login-password" class="form-control" placeholder="Pnet Password" />
						</div>
						<a href="#" id="login-btn" class="btn btn-login">로그인</a>
					</div>
					<div class="checkbox check-default">
						<input id="login-check" type="checkbox" value="1">
						<label for="login-check">로그인 상태 유지</label>
					</div>
					<ul class="util obj-clear">
						<li><a href="javascript:alert('Pnet 아이디/비밀번호로 로그인하세요.')">아이디·비밀번호 찾기</a> <span class="division-line">|</span>&nbsp;</li>
						<li><a href="javascript:alert('Pnet 아이디/비밀번호로 로그인하세요.')"> 회원가입</a></li>
					</ul>
				</div>
				<section class="column user-number-area">
					<h1 class="banner-title">Voyager 업무요청</h1>
					<div class="description">
						JIRA 서비스데스크를 통해 아래와 같은 Voyager 업무요청을 할 수 있습니다.
						<li>Voyager 권한 발급 요청</li>
						<li>Voyager 데이터 관련 문의</li>
						<li>Voyager 리포트 신규/수정 개발 요청</li>
					</div>
					<div class="page-link">
						<span>업무요청 바로가기
							<a href="http://jira.skplanet.com/secure/Dashboard.jspa?selectPageId=12005" target="_blank">
								<img src="resources/images/btn/btn_page_link.png"/>
							</a>
						</span>
					</div>
				</section>
			</div>
			<div class="main-ad-column02 obj-clear">
				<section class="notice-area">
					<div class="notice">
						<div class="content">
							<h1 class="banner-title">공지사항</h1>
							<h2 class="banner-title">Voyager 1차 베타 오픈</h2>
							<div class="description">
								<p>OCB 5.0 클릭로그 통계정보 제공을 위해 Voyager를 1차 베타 오픈합니다.</p>
							</div>
							<div class="page-link">
								<span>모든 공지사항 보기</span>
								<a href="#" data-target="#login-popup" data-toggle="modal">
									<img src="resources/images/btn/btn_page_link_g.png" alt="공지사항 페이지 이동" />
								</a>
							</div>
						</div>
					</div>
				</section>
				<section class="column qna-area">
					<h1 class="banner-title">Q&amp;A</h1>
					<h2 class="banner-title">Voyager 접속 아이디와 비밀번호는?</h2>
					<div class="description">
						<p>P net 접속 아이디/비밀번호와 동일합니다.</p>
					</div>
					<div class="page-link">
						<span>담당자 : Data Analysis Service그룹 이욱환 매니저</span>
					</div>
				</section>
				<section class="column column-section indicator-area">
					<h1 class="banner-title">데이터활용가이드</h1>
					<div class="description">
                        <p>Data에 대한 경험이 없는 구성원이 자신에게 필요한 정보를 직관적이고도  쉽게 접근할 수 있도록 하는 가이드입니다.</p><br/>
					</div>
					<div class="page-link">
						데이터활용가이드 보기
						<a href="http://wiki.skplanet.co.kr/pages/viewpage.action?pageId=58056382" target="_blank">
							<img src="resources/images/btn/btn_page_link.png"/>
						</a>
					</div>
				</section>
			</div>
			<section class="main-ad-column01 column-section service-page-view" style="padding-top:33px;">
					<h1 class="banner-title">Dataset Sharing</h1>
					<h2 class="banner-title"></h2>
					<div class="description">
						<p>최근에 등록된 Dataset Sharing을 로그인 후 확인하세요.</p>
					</div>
					<div class="page-link">
						<span>상세 보고서 보기</span>
						<a href="#" data-target="#login-popup" data-toggle="modal">
							<img src="resources/images/btn/btn_page_link.png"/>
						</a>
					</div>
			</section>
		</article>
	</section>
</section>
<!-- footer start -->
<footer>
	<div class="footer-section">
		<div class="container">
			<div class="footer-sitemap">
				<a href="http://wiki.skplanet.co.kr/pages/viewpage.action?pageId=58056382" class="btn btn-white" target="_blank">데이터활용가이드</a>
				<div class="btn-group dropup statistics">
					<button id="" class="btn btn-white">
						통계 사이트
					</button>
					<button class="btn btn-white btn-dropdown-toggle" data-toggle="dropdown">
						<span class="caret"></span>
					 </button>
					<ul class="dropdown-menu familysite-list">
						<li><a href="http://bins.skplanet.com" target="_blank">BINS</a></li>
					</ul>
				</div>
			</div>
			<div class="footer-info obj-clear">
				<h1><img src="resources/images/common/f_logo.gif" alt="SK planet" /></h1>
				<address>
                    SK planet 구성원을 위한 사내 시스템입니다. 불법으로 사용시에는 법적 제재를 받을 수 있습니다.<br />
                    COPYRIGHTS 2013 SK PLANET. ALL RIGHTS RESERVED.
				</address>
			</div>
		</div>
	</div>
</footer>
<!-- //footer end -->
<div id="login-popup" class="modal fade login-modal" tabindex="-1" role="dialog" aria-labelledby="LoginPopupLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h4 class="modal-title" id="myModalLabel">로그인</h4>
			</div>
			<div class="caution">
				<p class="bul-point">로그인 후 확인하실 수 있는 내용입니다.</p>
			</div>
			<div class="modal-body obj-clear">

				<div class="login-area">
					<div class="login-con">
						<div class="input-box">
							<div class="input-id">
								<label for="popup-login-username" class="hidden-text">Id</label>
								<input type="text" id="popup-login-username" class="form-control" placeholder="Pnet ID" />
							</div>
							<div class="input-password">
								<label for="popup-login-password" class="hidden-text">Password</label>
								<input type="password" id="popup-login-password" class="form-control" placeholder="Pnet Password" />
							</div>
							<a href="javascript:;" id="pop-login-btn" class="btn btn-login">로그인</a>
						</div>
						<div class="checkbox check-default">
							<input id="popup-login-check" type="checkbox" value="1">
							<label for="popup-login-check">로그인 상태 유지</label>
						</div>
						<ul class="util obj-clear">
							<li><a href="javascript:alert('P net 아이디/비밀번호로 로그인하세요.')">아이디·비밀번호 찾기</a> <span class="division-line">|</span>&nbsp;</li>
							<li><a href="javascript:alert('P net 아이디/비밀번호로 로그인하세요.')"> 회원가입</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$('#login-btn').click(function (e) {
		login($(this), $('#login-username'), $('#login-password'), $('#login-check'))
	});
	$('#pop-login-btn').click(function (e) {
		login($(this), $('#popup-login-username'), $('#popup-login-password'), $('#popup-login-check'))
	});

	$("#login-username, #login-password").bind("keydown", function(e) {
		triggerClickLogin(e, $('#login-btn'));
	});
	$("#popup-login-username, #popup-login-password").bind("keydown", function(e) {
		triggerClickLogin(e, $('#pop-login-btn'));
	});

    function login($loginBtn, $username, $password, $loginCheck) {
        var username = $username.val().trim();
        var password = $password.val().trim();

        if(!username.length) {
            alert('아이디를 입력해주세요.');
            $username.focus();
            return;
        } else if (!password.length) {
            alert('비밀번호를 입력해주세요.');
            $password.focus();
            return;
        }

        $loginBtn.addClass('disabled');
        $.post('/login', {
            username: username,
            password: password,
            loginCheck: $loginCheck.is(":checked"),
            returnUrl: getUrlVar('returnUrl')
        }, function(data) {
            document.location = '/';
        }).fail(function() {
            alert("아이디나 또는 비밀번호 오류입니다.");
            $loginBtn.removeClass('disabled');
            $username.focus();
        });
    }

    function triggerClickLogin(e, $obj) {
        if (e.keyCode == 13) { // enter key
            $obj.trigger('click');
            return false;
        }
    }

	$(function() {
		$('input').placeholder();
		$('#login-username').focus();
	});

    function getUrlVar(key){
        var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search);
        return result && unescape(result[1]) || "";
    }
</script>

<script type="text/javascript" src="resources/angularjs/angular.min.js"></script>
<script type="text/javascript" src="resources/angularjs/angular-touch.min.js"></script>
<script type="text/javascript" src="resources/carousel/dist/requestAnimationFrame.min.js"></script>
<script type="text/javascript" src="resources/carousel/dist/angular-carousel.min.js"></script>
<script type="text/javascript">
    angular.module('Login', ['angular-carousel','login.Directives']);
    var directives = angular.module('login.Directives', []);
    directives.directive('resize', function ($window) {
        return {
            link: function (scope, element) {
                var w = angular.element($window);
                scope.getWindowDimensions = function () {
                    return {
                        'h': w.height(),
                        'w': w.width()
                    };
                };
                scope.$watch(scope.getWindowDimensions, function (newValue, oldValue) {
                    //css변경에 따른 height 조절, 가로 768이상일 경우만
                    angular.element('.rn-carousel-container').css('width','100%');
                    if (newValue.w >= 768) {
                        angular.element('.container-wrap').css('height', (newValue.h - 105));
                        angular.element('.main-container').css('height', (newValue.h - 105));
                    }
                }, true);
                w.unbind('resize').bind('resize', function () {
                    scope.$apply();
                });
            }
        };
    });
</script>
</body>
</html>
