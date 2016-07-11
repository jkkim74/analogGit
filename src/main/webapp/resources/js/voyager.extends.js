/**
 * scripts.js에서 사용하는 jquery extends plugin 모음
 */
(function ($) {
    "use strict";
	// 반응형 클래스
	$.fn.extend({
		screenCheck:function() {
			var _this = $('body');
			function resizeChk(){
				if ($(window).width() <= 1599 && $(window).width() > 769) {
					_this.removeClass('responsive-body');
				}
				/* less than desktop */
				if ($(window).width() <= 768 && $(window).width() > 581) {
					_this.addClass('responsive-body');
					_this.removeClass('responsive-mobile');
					$("#sidebar").removeClass("hide-left-bar");
					$("#main-content").removeClass("merge-left");
				}
				/* //less than desktop */
			}
			resizeChk();
			$(window).bind("orientationchange resize", function (e) {
				if(e.type == 'resize'){
					resizeChk();
				}else{
					resizeChk();
				}
			})
		},

		// pivot table 관련 클래스명 지정
		pivotBoard:function() {
			var _this = $(this);
			if (!_this.get(0)){return false};
			var rowcheck = _this.find('th.pvtRowLabel'),
				rowcheck2 = _this.find('tr');
			$.each(rowcheck,function() {
				if ($(this).attr('rowspan') > 1){
					$(this).addClass("sub-tit");
					$(this).parent().addClass("row-group");
				}
				if ($(this).attr('colspan')) {
					$(this).addClass("num-scope")
				}
			})
			$.each(rowcheck2,function() {
				var headCheck = $(this).find("td").length;
				var totalCheck = $(this).find(".pvtTotal").length;
				if (headCheck < 1){
					$(this).addClass("head-group");
				}
				if (totalCheck > 1){
					$(this).addClass("total-group");
				}
			})
			_this.find(".row0").parent('tr').addClass("fir-data");
		},
		//대시보드 lnb
		dashboardLnb:function(){
			var _this = $(this);
			if (!_this.get(0)){return false};
			var userSet = _this.find("#user_set"),
				modifyOption = _this.find(".sub > li"),
				addBoard = _this.find(".btn-add"),
				bookmark = _this.find(".bookmark-area");

			//즐겨찾기
			$.each(bookmark,function(){
				var activeBookmark = $(this).find('.bookmark-active'),
					responseCheck = $(this).find('.bookmark-list > li > a');
				activeBookmark.bind('click',function(){
					var heightCheck = $(window).height() - 50;
					if ($(this).parent().hasClass('on')){
						$(this).parent().removeClass('on').css('margin-top', '-98px');
						$(this).find('.fa-angle-up').removeClass('fa-angle-down');
					}else {
						$(this).parent().addClass('on').css('margin-top', '-' + heightCheck + 'px');
						$(this).find('.fa-angle-up').addClass('fa-angle-down');
					}
				})
			});

			//설정 버튼 클릭시
			userSet.bind('click', function(){
				_this.toggleClass('user-set-mode');
				$(this).toggleClass('on');
				$(".con-dimmed").css('position','fixed').toggleClass('on');
			});

			//대시보드 추가 버튼 클릭시
			addBoard.bind('click', function(){
				var copyList = $('<li><div class="dropdown"><a href="#" class="board-tit"></a><button class="btn btn-white dropdown-toggle" data-toggle="dropdown"><i class="fa fa-caret-down"></i></button><div class="dropdown-menu board-modify-option"><div><button class="modify-name">이름변경</button></div><div><button class="delete-list">삭제</button></div></div></div><div class="modify-form"><input type="text" name="" id="" class="form-control board-name" title="대시보드 이름 입력" /><div class="modify-select"><button class="modify-board">추가</button>&nbsp;<span>|</span>&nbsp;<button class="modify-cancel">취소</button></div></div></li>');
				copyList.appendTo('.combine-dashboard .sub');

				var _this = $('.combine-dashboard .sub li:last-child'),
					addList = _this.find('.modify-board'),
					addCancel = _this.find('.modify-cancel');

				_this.find('.dropdown').hide();
				_this.find('.modify-form').show();
				addList.text('추가');
				addList.bind('click',function(){
					var parentList = $(this).parentsUntil('.sub');
					$(this).text('변경');
					parentList.find('.modify-cancel').addClass('reform-modify').removeClass('modify-cancel');
					parentList.find('.dropdown').show();
					parentList.find('.modify-form').hide();
				});
				addCancel.bind('click',function(){
					var parentList = $(this).parentsUntil('.sub');
					if ($(this).is('.modify-cancel')) {
						$(this).parent().parent().parent().remove();
					} else {
						parentList.find('.dropdown').show();
						parentList.find('.modify-form').hide();
					}
				});
				$(this).parent().parent().find(".sub > li").each(function(){
					var boardName = $(this).find(".modify-name"),
						deleteList = $(this).find(".delete-list"),
						cancelModify = $(this).find(".modify-cancel"),
						modifyName = $(this).find(".modify-board");

					boardName.bind('click',function(){
						var copyName = $(this).parentsUntil('li').find('.board-tit').text(),
							parentList = $(this).parentsUntil('.sub');
						parentList.find('.dropdown').hide();
						parentList.find('.modify-form').show();
						parentList.find('.board-name').val(copyName);
					});
					deleteList.bind('click',function(){
						var parentList = $(this).parentsUntil('.sub');
						parentList.remove();
					});
					modifyName.bind('click',function(){
						var prevName = $(this).parentsUntil('.sub').find('.board-tit'),
							parentList = $(this).parentsUntil('.sub'),
							copyName = parentList.find('.board-name').val();
						$(this).parent().find('.reform-cancle').addClass('modify-cancle').removeClass('reform-cancle');
						prevName.text(copyName);
						parentList.find('.dropdown').show();
						parentList.find('.modify-form').hide();
					});
					cancelModify.bind('click',function(){
						var parentList = $(this).parentsUntil('.sub');
						parentList.find('.dropdown').show();
						parentList.find('.modify-form').hide();
					});
				});
			});
			$.each(modifyOption,function(){
				var boardName = $(this).find(".modify-name"),
					deleteList = $(this).find(".delete-list"),
					cancelModify = $(this).find(".modify-cancel"),
					modifyName = $(this).find(".modify-board");

				boardName.bind('click',function(){
					var copyName = $(this).parentsUntil('li').find('.board-tit').text(),
						parentList = $(this).parentsUntil('.sub');
					parentList.find('.dropdown').hide();
					parentList.find('.modify-form').show();
					parentList.find('.board-name').val(copyName);
				});
				deleteList.bind('click',function(){
					var parentList = $(this).parentsUntil('.sub');
					parentList.remove();
				});
				modifyName.bind('click',function(){
					var prevName = $(this).parentsUntil('.sub').find('.board-tit'),
						parentList = $(this).parentsUntil('.sub'),
						copyName = parentList.find('.board-name').val();
					prevName.text(copyName);
					parentList.find('.dropdown').show();
					parentList.find('.modify-form').hide();
				});
				cancelModify.bind('click',function(){
					var parentList = $(this).parentsUntil('.sub');
					parentList.find('.dropdown').show();
					parentList.find('.modify-form').hide();
				});
			});
		},
		bookmarkModal: function(){
			var _this = $(this);
			if (!_this.get(0)){return false};
			var folderCheck = _this.find('.tree-folder-header'),
				treeSelected = _this.find('.tree-item');
			$.each(folderCheck,function(){
				$(this).click(function(){
					var folderIcoCheck = $(this).find('.fa');
					if (folderIcoCheck.is('.fa-folder-open')){
						folderIcoCheck.removeClass('fa-folder-open');
						folderIcoCheck.addClass('fa-folder');
						$(this).next().removeClass('open');
					}else {
						folderIcoCheck.removeClass('fa-folder');
						folderIcoCheck.addClass('fa-folder-open');
						$(this).next().addClass('open');
					}

				});
			});
			$.each(treeSelected,function(){
				$(this).click(function(){
					var iconCheck = $(this).find('i');

					if ($(this).is('.tree-selected')){
						$(this).removeClass('tree-selected');
						iconCheck.removeClass();
					}else {
						$(this).addClass('tree-selected');
						iconCheck.removeClass();
						iconCheck.addClass('fa fa-check');
					}

					var listCheck = _this.find('.tree-selected').length;

					if (listCheck == 0){
						_this.find('.control-area button').attr('disabled','disabled');
					}else {
						_this.find('.control-area button').removeAttr('disabled');
					}
				});
			});
		}
	});
})(jQuery);
