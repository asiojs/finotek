//우클릭 방지 관련 : 퍼블리싱 완료되면 주석 제거
//document.oncontextmenu=function(){return false;}

$(function(){

//	//[수정완료]div 드롭다운(동적 사용을 위해 on 사용)
//	$("body").on("click", 'a[class="dropDown_txt"]', function(e){
//		e.preventDefault();
//		var span = $(this).parent().find("span");
//		var divLength = $(this).parent().find('div').children('div').length;
//
//		if(span.hasClass("down")) {
//			span.removeClass("down");
//			span.addClass("up");
//
//			$(this).parent().find('div').removeClass('hide');
//
//			if(divLength == 0) {
//				$(this).parent().find('div').removeClass('hide');
//			} else {
//				$(this).next().removeClass('hide');
//			}
//
//		} else {
//			span.removeClass("up");
//			span.addClass("down");
//
//			$(this).parent().find('div').addClass('hide');
//
//			if(divLength == 0) {
//				$(this).parent().find('div').addClass('hide');
//			} else {
//				$(this).next().addClass('hide');
//			}
//
//		}
//	});

	/*
	$('a[class="dropDown_img"]').bind("click", function(e){
		e.preventDefault();
		var div = $(this).next();

		if(div.hasClass('hide')) {
			div.removeClass('hide');
		} else {
			div.addClass('hide');
		}
	});
	*/

	//문서 상세조회 > 드롭다운 - 추가기능 선택, 메뉴 클릭 시
	$('.doc_detail').find('.extFunction_dropDown_menu').find('li > a').bind("click", function(e){
		e.preventDefault();
		$(this).parents('.extFunction_dropDown_wrapper').prev().trigger('click');

		var clsName = $(this).attr('class');

		if(clsName == 'copy') {
			//이벤트 기술
			$('.doc_folder_choose2').removeClass('hide');
			$('.doc_folder_choose2').find('input:hidden.context_choose').val(clsName);
		} else if(clsName == 'move') {
			//이벤트 기술
			$('.doc_folder_choose2').removeClass('hide');
			$('.doc_folder_choose2').find('input:hidden.context_choose').val(clsName);
		} else if(clsName == 'favorite') {
			//이벤트 기술
			$('.doc_favorite_choose').removeClass('hide');
		} else if(clsName == 'tempbox') {
			//이벤트 기술
			/*jConfirm("작업카트에 추가하시겠습니까?", "확인", 0, function(r){
				if(r){
					jAlert("작업카트에 추가 되었습니다.", "확인", 0);
					//jAlert("이미 작업카트에 추가된<br>문서입니다.", "확인", 0);
					//jAlert("작업카트에는 30개 이상의 문서를 추가할 수 없습니다.", "확인", 0);
				}
			});*/
		} else if(clsName == 'email_send') {
			$('.url_email').removeClass('hide');
		}
	});

	//컨텍스트메뉴 - 메뉴 클릭 시
	$('.tbl_context_menu > ul').find('li > a').bind("click", function(e){
		e.preventDefault();
		var clsName = $(this).attr('class');

		//버튼을 누르면 컨텍스트메뉴가 닫힘
		$('.tbl_context_menu').addClass('hide');

		if(clsName == 'copy') {
			var chkLength = $('.tbl_data_body > li.check').find('input:checkbox:checked').length;

			if(chkLength != 0) {
				//이벤트 기술
				$('.doc_folder_choose2').removeClass('hide');
				$('.doc_folder_choose2').find('input:hidden.context_choose').val(clsName);
			} else {
				jAlert("복사할 문서를 선택하세요!", "확인", 0);
			}
		} else if(clsName == 'move') {
			var chkLength = $('.tbl_data_body > li.check').find('input:checkbox:checked').length;

			if(chkLength != 0) {
				//이벤트 기술
				$('.doc_folder_choose2').removeClass('hide');
				$('.doc_folder_choose2').find('input:hidden.context_choose').val(clsName);
			} else {
				jAlert("이동할 문서를 선택하세요!", "확인", 0);
			}
		} else if(clsName == 'delete') {
			jConfirm("삭제 하시겠습니까?", "확인", 0, function(r){
				if(r){
					var isChkOut = true;
					if(isChkOut){
						jConfirm("체크아웃된 문서가 존재합니다.<br>체크아웃 취소 후 다시<br>작업하시기 바랍니다.", "확인", 0, function(r){

						});
					} else {
						jAlert("삭제 확인", "확인", 0);
					}
				} else {
					jAlert("삭제 취소", "취소", 0);
				}
			});
		} else if(clsName == 'favorite'){
			$('.doc_favorite_choose').removeClass('hide');
		} else if(clsName == 'tempbox'){
			jConfirm("작업카트에 추가하시겠습니까?", "확인", 0, function(r){
				if(r){
					jAlert("이미 작업카트에 추가된<br>문서입니다.", "확인", 0);
					//jAlert("작업카트에는 30개 이상의 문서를 추가할 수 없습니다.", "확인", 0);
				}
			});
		}
	});


	//통합 - 일자 달력 선택 활성화
	//$("#datepicker3").datepicker({dateFormat:'yy-mm-dd'});
	//$("#datepicker4").datepicker({dateFormat:'yy-mm-dd'});

	//통계 로그인이력 검색
	//$("#datepicker6").datepicker({dateFormat:'yy-mm-dd'});
	//$("#datepicker7").datepicker({dateFormat:'yy-mm-dd'});

	//통합검색 작성기간 검색
	$("#datepicker8").datepicker({dateFormat:'yy-mm-dd'});
	$("#datepicker9").datepicker({dateFormat:'yy-mm-dd'});

	//관련문서 추가 - 일자 달력 선택 활성화
	$("#relativeAdd_datepicker1").datepicker({dateFormat:'yy-mm-dd'});
	$("#relativeAdd_datepicker2").datepicker({dateFormat:'yy-mm-dd'});

	//풀스크린
	$('a.full_screen').bind("click", function(e){
		e.preventDefault();
		var el = document.documentElement
			, rfs = // for newer Webkit and Firefox
				   el.requestFullScreen
				|| el.webkitRequestFullScreen
				|| el.mozRequestFullScreen
				|| el.msRequestFullscreen
		;
		if(typeof rfs!="undefined" && rfs){
		  rfs.call(el);
		} else if(typeof window.ActiveXObject!="undefined"){
		  // for Internet Explorer
		  var wscript = new ActiveXObject("WScript.Shell");
		  if (wscript!=null) {
			 wscript.SendKeys("{F11}");
		  }
		}
	});

	//depth navigation
	$('.depth_navi > span').mouseover(function(){
		var path = $(this).parent().find(".depth_navi_path");
		if(!path.is(":visible")) {
			path.removeClass('hide');
		}
	}).mouseout(function(){
		var path = $(this).parent().find(".depth_navi_path");
		if(path.is(":visible")) {
			path.addClass('hide');
		}
	});

    //문서등록 - 창 닫기
    $('.doc_register_close').bind("click", function(e){
        e.preventDefault();
        $(this).parents('.doc_register').addClass('hide');
        $('.doc_register_wrapper').addClass('hide');
    });

    //문서등록 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.doc_register_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.doc_register').addClass('hide');
    });

    //문서수정 - 창 닫기
    $('.doc_modify_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.doc_modify').addClass('hide');
    	$('.doc_modify_wrapper').addClass('hide');
    });

    //문서수정 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.doc_modify_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.doc_modify').addClass('hide');
    });

    //내 문서등록 - 창 닫기
    $('.myDoc_register_close').bind("click", function(e){
        e.preventDefault();
        $(this).parents('.myDoc_register').addClass('hide');
        $('.myDoc_register_wrapper').addClass('hide');
    });

    //내 문서등록 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.myDoc_register_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.myDoc_register').addClass('hide');
    });



    //탭 요소 클릭 시 폼 변경
    $('.tab_element').bind("click", function(){
        var idx = $(this).index();
        var targetFrm = $(this).parent().parent().parent().find('div[class^="tab_form"]');
        targetFrm.addClass('hide');
        targetFrm.eq(idx).removeClass('hide');

        $('.tab_element').removeClass('selected');
        $(this).addClass('selected');
    });

    /*
    //문서 상세조회 - 창 닫기
    $('.doc_detail_close').delegate("click", function(e){
        e.preventDefault();
        $(this).parents('.doc_detail').addClass('hide');
        $('.doc_detail_wrapper').addClass('hide');
    });

    //문서 상세조회 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.doc_detail_wrapper').delegate("click", function(){
    	$(this).addClass('hide');
    	$('.doc_detail').addClass('hide');
    });
    */

    //문서 상세조회 > 폴더 선택 - 창 닫기
    $('.doc_folder_close').bind("click", function(e){
        e.preventDefault();
        $(this).parents('div[id^="doc_folder_choose"]').addClass('hide');
        $('.folder_choose_wrapper').addClass('hide');
    });

    //문서등록 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('div[class^="folder_choose_wrapper"]').bind("click", function(){
    	$(this).addClass('hide');
    	$('.doc_folder_choose').addClass('hide');
    });

    //컨텍스트메뉴 > 폴더 선택 - 확인버튼
    $('.doc_folder_btnmenu2').find('button.confirm').bind("click", function(){
    	var r = true;
    	var contextChoose = $('.doc_folder_choose2').find('input:hidden.context_choose').val();
    	var chooseVal = "";
    	var removeWindow = function() {
    		$('div[id^="doc_folder_choose"]').addClass('hide');
        	$('div[id^="doc_folder_choose"]').prev().addClass('hide');
    	};

    	if(contextChoose == 'copy') chooseVal = "복사";
    	else if(contextChoose == 'move') chooseVal = "이동";

    	if(r){
    		jAlert("문서 " + chooseVal + "에 성공하였습니다.", "확인", 0, function(r){
    			removeWindow();
    		});
    	} else {
    		jAlert("문서 " + chooseVal + "에 실패하였습니다.", "확인", 0, function(r){
    			removeWindow();
    		});
    	}
    });

    //컨텍스트메뉴 > 폴더 선택 - 취소버튼
    $('.doc_folder_btnmenu2').find('button.cancel').bind("click", function(){
    	$(this).parents('div[id^="doc_folder_choose"]').addClass('hide');
    	$(this).parents('div[id^="doc_folder_choose"]').prev().addClass('hide');

    });

    //컨텍스트메뉴 > 즐겨찾기 선택 - 창 닫기
    $('.doc_favorite_close').bind("click", function(e){
        e.preventDefault();
        $(this).parents('div[id="doc_favorite_choose"]').addClass('hide');
    });

    //컨텍스트메뉴 > 즐겨찾기 선택 - 확인
    $('.doc_favorite_btnmenu').find('button.confirm').bind("click", function(){
    	var r = true;

    	if(r){
    		jAlert("즐겨찾기 추가에 성공하였습니다.", "확인", 0);
    	} else {
    		jAlert("이미 즐겨찾기에 추가에 추가된<br>문서입니다.", "확인", 0);
    	}
    });

    //컨텍스트메뉴 > 즐겨찾기 선택 - 취소
    $('.doc_favorite_btnmenu').find('button.cancel').bind("click", function(){
    	$(this).parents('div[id="doc_favorite_choose"]').addClass('hide');
    });

    //문서 상세조회 > 폴더 선택 - 창 열기
    $('.doc_register_cnts').find('button.doc_folder_srch').bind("click", function(){
    	$('.doc_folder_choose').removeClass('hide');
    });

    //문서 상세조회 - 자물쇠 모양 over시 정보 표출
    $('.cnts_locked').find('img').mouseover(function(){
    	$('.locked_info').removeClass('hide');
    }).mouseout(function(){
    	$('.locked_info').addClass('hide');
    });

    //문서 상세조회 - 버튼메뉴
    //삭제버튼
    $('.doc_detail').find('button.delete').bind("click", function(){
    	var tabIdx = $('.doc_detail').find('span[class*="selected"]').index();
    	//0 : 기본, 1 : 버전, 2: 이력, 3 : 의견
    	if(tabIdx == 0) {
    		jConfirm("삭제하시겠습니까?", "확인", 0, function(r){
    			if(r){
    				jAlert('삭제 되었습니다.', "확인", 0, function(r){
    					$('.doc_detail').addClass('hide');
    				});
    			}
    		});
    	} else if(tabIdx == 1) {
    		jConfirm("해당 버전을<br>영구 삭제하시겠습니까?", "확인", 0, function(r){
    		});
    	} else if(tabIdx == 3) {
    		jConfirm("의견을 삭제하시겠습니까?", "확인", 0, function(r){
    		});
    	}
    });

    //체크아웃 취소
    $('.doc_detail').find('button.cancel_checkout').bind("click", function(){
    	/*jConfirm("체크아웃을 취소하시겠습니까?", "확인", 0, function(r){
    		if(r){
    			jAlert("체크아웃이 취소 되었습니다.", "확인", 0);
    		}
		});*/
    });


    //최근문서 리스트 지우기
    $('.recent_del').bind("click", function(e){
    	e.preventDefault();
    	$(this).parent().remove();
    });

    //권한변경 - 창 닫기
    $('.doc_authModify_close').bind("click", function(e){
        e.preventDefault();
        $(this).parents('.doc_folder_authModify').addClass('hide');
        $('.folder_authModify_wrapper').addClass('hide');
    });

    //권한변경 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.folder_authModify_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.doc_folder_authModify').addClass('hide');
    });

    //URL복사 - 창 열기
    $('.btn_urlCopy').bind("click", function(){
    	$('.url_copy').removeClass('hide');
    });

    //URL복사 - 창 닫기
    $('.url_copy_close').bind("click", function(e){
        e.preventDefault();
        $(this).parents('.url_copy').addClass('hide');
    });

    //URL붙여넣기 - 창 닫기
    $('.url_paste_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.url_paste').addClass('hide');
    });

    //URL메일발송 - 창 닫기
    $('.url_email_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.url_email').addClass('hide');
    });

    //권한설정 닫기
    $('.doc_authSet_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.doc_authSet').addClass('hide');
    	$('.doc_authSet_wrapper').addClass('hide');
    });

    //권한설정 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.doc_authSet_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.doc_authSet').addClass('hide');
    });

    //권한설정 - 선택대상 지우기
    $('.chosen_user_list > li').find('a').bind("click", function(e){
    	e.preventDefault();
    	var idVal = $(this).parent().find('input:hidden').val();

    	$(this).parent().remove();

    	//사용자 체크 된 부분 체크 해제
    	$('.tbl_choose_list > tr').find('input:checkbox[id="' + idVal + '"]').attr('checked', false);
    });

    //하위폴더 추가 - 창 닫기
    $('.subFolder_add_close').bind("click", function(e){
        e.preventDefault();
        $(this).parents('.subFolder_add').addClass('hide');
        $('.subFolder_add_wrapper').addClass('hide');
    });

    //하위폴더 추가 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.subFolder_add_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.subFolder_add').addClass('hide');
    });

    //하위폴더 수정 - 창 닫기
    $('.subFolder_modify_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.subFolder_modify').addClass('hide');
    	$('.subFolder_modify_wrapper').addClass('hide');
    });

    //하위폴더 수정 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.subFolder_modify_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.subFolder_modify').addClass('hide');
    });

    //하위폴더 추가 > 권한변경 -  창 닫기
    $('.subFolder_authModify_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.subFolder_authModify').addClass('hide');
    	$('.subFolder_authModify_wrapper').addClass('hide');
    });

    //하위폴더 추가 > 권한변경 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.subFolder_authModify_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.subFolder_authModify').addClass('hide');
    });

    //하위폴더 추가 > 권한변경 -  창 닫기
    $('.subFolder_authModifyCopy_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.subFolder_authModifyCopy').addClass('hide');
    	$('.subFolder_authModifyCopy_wrapper').addClass('hide');
    });

    //하위폴더 추가 > 권한변경 > 권한등록 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.subFolder_authModifyCopy_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.subFolder_authModifyCopy').addClass('hide');
    });

    //하위폴더 추가 > 권한변경 -  창 닫기
    $('.subFolder_authModifyUpdate_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.subFolder_authModifyUpdate').addClass('hide');
    	$('.subFolder_authModifyUpdate_wrapper').addClass('hide');
    });

    //하위폴더 추가 > 권한변경 > 권한등록 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.subFolder_authModifyUpdate_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.subFolder_authModifyUpdate').addClass('hide');
    });

    //보존기간 연장 - 창 닫기
    $('.extend_preserve_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.extend_preserve').addClass('hide');
    	$('.extend_preserve_wrapper').addClass('hide');
    });

    //보존기간 연장 -  창 닫기 : 음영진 부분 클릭 시 닫기
    $('.extend_preserve_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.extend_preserve').addClass('hide');
    });

    //즐겨찾기 등록 - 창 닫기
    $('.favorite_choose_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.favorite_choose').addClass('hide');
    	$('.favorite_choose_wrapper').addClass('hide');
    });

    //즐겨찾기 등록 -  창 닫기 : 음영진 부분 클릭 시 닫기
    $('.favorite_choose_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.favorite_choose').addClass('hide');
    });


    //즐겨찾기 폴더 등록 - 창 닫기
    $('.favorite_register_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.favorite_register').addClass('hide');
    	$('.favorite_register_wrapper').addClass('hide');
    });

    //즐겨찾기 폴더 등록 -  창 닫기 : 음영진 부분 클릭 시 닫기
    $('.favorite_register_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.favorite_register').addClass('hide');
    });

    //즐겨찾기 폴더 수정 - 창 닫기
    $('.favorite_modify_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.favorite_modify').addClass('hide');
    	$('.favorite_modify_wrapper').addClass('hide');
    });

    //즐겨찾기 폴더 수정 -  창 닫기 : 음영진 부분 클릭 시 닫기
    $('.favorite_modify_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.favorite_modify').addClass('hide');
    });

    //즐겨찾기 폴더 이동 - 창 닫기
    $('.favorite_move_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.favorite_move').addClass('hide');
    	$('.favorite_move_wrapper').addClass('hide');
    });

    //즐겨찾기 폴더 이동 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.favorite_move_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.favorite_move').addClass('hide');
    });

    //작업카트 > 관련문서 - 창 닫기
    $('.relativeDocs_choose_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.relativeDocs_choose').addClass('hide');
    	$('.relativeDocs_choose_wrapper').addClass('hide');
    });

    //작업카트 > 관련문서 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.relativeDocs_choose_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.relativeDocs_choose').addClass('hide');
    });

    //작업카트 > 관련문서 - 창 닫기
    $('.url_emailSend_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.url_emailSend').addClass('hide');
    	$('.url_emailSend_wrapper').addClass('hide');
    });

    //작업카트 > 관련문서 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.url_emailSend_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.url_emailSend').addClass('hide');
    });

    //이메일 송부 > 사용자 선택 - 창 닫기
    $('.user_choose_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.user_choose').addClass('hide');
    	$('.user_choose_wrapper').addClass('hide');
    });

    //이메일 송부 > 사용자 선택 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.user_choose_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.user_choose').addClass('hide');
    });

    //협업 등록 - 창 닫기
    $('.coop_register_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.coop_register').addClass('hide');
    	$('.coop_register_wrapper').addClass('hide');
    });

    //협업 등록 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.coop_register_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.coop_register').addClass('hide');
    });

    //협업 등록 > 사용자 선택 - 창 닫기
    $('.coopUser_choose_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.coopUser_choose').addClass('hide');
    	$('.coopUser_choose_wrapper').addClass('hide');
    });

    //협업 등록 > 사용자 선택 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.coopUser_choose_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.coopUser_choose').addClass('hide');
    });

    //협업 업무상세조회  - 창 닫기
    $('.coopUser_detail_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.coopUser_detail').addClass('hide');
    	$('.coopUser_detail_wrapper').addClass('hide');
    });

    //협업 업무상세조회 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.coopUser_detail_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.coopUser_detail').addClass('hide');
    });

    /*
     * 버튼 액션
     */
    //문서 등록
    $('.reg_doc').bind("click", function(){
    	var doc = $('.doc_register');
    	doc.removeClass('hide');
    	doc.prev().removeClass('hide');

    	lyrPopupWindowResize($('.doc_register'));
    });

    //문서 수정
    $('.modify_docs').bind("click", function(){

    });

    //문서 수정 취소
    $('.cancel_modify').bind("click", function(){
    	jConfirm("수정작업을 취소하시겠습니까?<br>&quot;예&quot;선택시 잠금상태가 해제됩니다.", "확인", 0, function(r){

    	});
    });

    //보존기간 연장 팝업 호출
    $('.extend_preserve_btn').bind("click", function(){
    	var lyrWindow = $('.extend_preserve');
    	lyrWindow.removeClass('hide');
    	lyrWindow.prev().removeClass('hide');

    	//보존기간 연장
        lyrPopupWindowResize($('.extend_preserve'));
    });


    //즐겨찾기
    $('.favorite_confirm_btn').bind("click", function(){
    	var isDup = false;
    	if(isDup){
    		jAlert("이미 즐겨찾기에 추가된 폴더입니다.", "확인", 1);
    	} else {
    		jAlert("즐겨찾기에 추가 되었습니다.", "확인", 1, function(r){
    			$('.favorite_choose').addClass('hide');
    			$('.favorite_choose').prev().addClass('hide');
    		});
    	}
    });

    //즐겨찾기 제외
    $('.exclude_favorite').bind("click", function(){
    	jConfirm("즐겨찾기를 제외하시겠습니까?", "확인", 3, function(r){
    		if(r){
    			jAlert("즐겨찾기가 제외되었습니다.", "확인", 3, function(r){

    			});
    		}
    	});
    });

    //즐겨찾기 폺더 구성
    //저장버튼
    $('.favorite_save_btn').bind("click", function(){

    });

    //작업카트 - 관련문서 설정
    $('.set_relative_docs').bind("click", function(){
    	var length = $('.cnts_list').find('input:checked').length;

    	if(length < 2) {
    		jAlert("관련문서 설정 시 2개 이상의 문서를<br>선택해주세요!", "확인", 4);
    	} else {
    		$('.relativeDocs_choose').removeClass('hide');
    		$('.relativeDocs_choose').prev().removeClass('hide');

    		//작업카트 - 관련문서 선택
    	    lyrPopupWindowResize($('.relativeDocs_choose'));
    	}
    });

    //작업카트 - 관련문서 등록
    $('.relativeDocs_confirm_btn').bind("click", function(){
    	jConfirm("선택한 메인문서에 이미 관련문서가 있습니다.<br>나머지 문서를 추가하시겠습니까?", "확인", 4, function(r){

    	});
    });

    //작업카트 -  URL메일송부
    $('.url_email_send').bind("click", function(){
    	//작업카트 - URL 메일 송부
    	$('.url_emailSend').removeClass('hide');
		$('.url_emailSend').prev().removeClass('hide');
        lyrPopupWindowResize($('.url_emailSend'));
    });

    //작업카트 - 작업카트 제외
    $('.exclude_tempbox').bind("click", function(){
    	var chkLength = $('.tbody').find("input:checkbox:checked").length;
    	if(chkLength > 0) {
    		jConfirm("선택한 문서를 작업카트에서<br>제외하시겠습니까?", "확인", 4, function(r){

        	});
    	} else {
    		jAlert("작업카트에서 제외할 문서를 선택해 주세요.", "확인", 4);
    	}
    });

    //작업카트 - 즐겨찾기 추가
    $('.add_favorite').bind("click", function(){

    });

    //작업카트 - 다운로드
    $('.docs_download').bind("click", function(){
    	var chkLength = $('.tbody').find("input:checkbox:checked").length;

    	if(chkLength > 10) {
    		jAlert("최대 10개까지 다운로드가 가능합니다.<br>확인 바랍니다.", "확인", 4);
    	} else if(chkLength > 0 && chkLength <= 10) {
    		jConfirm("선택한 문서를 일괄 다운로드<br>하시겠습니까?", "확인", 4, function(r){

    		});
    	} else {
    		jAlert("다운로드 할 문서를 선택해 주세요.", "확인", 4);
    	}

    });

    //협업 등록 버튼
    $('.tbl_coop_reg').bind("click", function(){
    	$('.coop_register').removeClass('hide');
		$('.coop_register').prev().removeClass('hide');
        lyrPopupWindowResize($('.coop_register'));
    });

    //협업요청
    $('.requestApproval_btn').bind("click", function(){
    	var cnts = $(this).next().val();

    	if(cnts != ""){
    		jConfirm("승인 하시겠습니까?", "확인", 7, function(r){

        	});
    	} else {
    		jAlert("승인요청 사항을 입력하시기 바랍니다.", "확인", 7);
    	}
    });

    //협업요청 삭제
    $('.delete_requestApproval_btn').bind("click", function(){
    	jConfirm("삭제하시겠습니까?", "확인", 7, function(r){

    	});
    });

    //협업요청 승인
    $('.requestApproval_accept').bind("click", function(){
    	jPrompt("승인 내용을 입력해주세요", "", "확인", 7, function(r){

    	});
    });

    //협업요청 반려
    $('.requestApproval_reject').bind("click", function(){
    	jPrompt("반려 내용을 입력해주세요", "", "확인", 7, function(r){

    	});
    });

    //통계 그래프 레이어 팝업 보기
    $('.tbl_thumbMenu').find('li.chart_view > a').bind("click", function(e){
    	e.preventDefault();
    	var div = $('.statics_view')
    	div.removeClass('hide');
    	div.prev().removeClass('hide');

        //통계 그래프 보기
        lyrPopupWindowResize($('.statics_view'));
    });

    //문서 상세조회 > 전체 선택
    $('.relative_docs_checkAll').bind("click", function(){
    	var checkbox = $('.relative_docs_wrapper').find('.relative_docs_checkbox');
		checkbox.prop("checked",true);
    });

    //문서 상세조회 > 전체 해제
    $('.relative_docs_uncheckAll').bind("click", function(){
    	var checkbox = $('.relative_docs_wrapper').find('.relative_docs_checkbox');
    	checkbox.prop("checked",false);
    });

    //통합검색 버튼 - 토글
    $('.integratedSrch_detail_btn').bind("click", function(){
    	var div = $(this).parent().find('.integratedCnts_dropDown_menu');
    	if(div.hasClass('hide')) {
    		div.removeClass('hide');
    	} else {
    		div.addClass('hide');
    	}
    });

    //휴지통 삭제버튼
    $('.tbl_completeDel').bind("click", function(){
    	jConfirm("선택한 문서를 삭제하시겠습니까?", "삭제", 2, function(r){

    	});
    });

    //휴지통 복원 버튼
    $('.tbl_restore').bind("click", function(){
    	jConfirm("선택한 문서를 복원하시겠습니까?", "복원", 2, function(r){

    	});
    });


    /*
     * 메뉴관련
     */
    //공통메뉴 리스트 설명 열기/닫기
	$('.header_icon_menu').find('li').mouseover(function(){
		var menu_tooltip = $(this).find('div');
		if(menu_tooltip.hasClass('hide')){
			menu_tooltip.removeClass('hide');
			menu_tooltip.css({
				left : (-1) * (menu_tooltip.outerWidth()-$(this).width())/2
			});
			$(this).find('a').addClass('selected');
		}
	}).mouseout(function(){
		var menu_tooltip = $(this).find('div');
		menu_tooltip.addClass('hide');

		if(!$(this).hasClass('selected')){
			$(this).find('a').removeClass('selected');
		}
	}).click(function(){
		$(this).parent().find('li, li > a').removeClass('selected');
		$(this).addClass('selected');
		$(this).find('a').addClass('selected');
	});

	//탑 사용자 메뉴 드롭다운
	$('.header_user_menu').find('li > a').bind('click', function(e){
		//e.preventDefault();
		var dropDown_menu = $(this).parent().find('div[class*="dropDown_menu"]');

		if(dropDown_menu.hasClass('hide')){
			$('.header_user_menu').find('li > div[class*="dropDown_menu"]').addClass('hide');
			dropDown_menu.removeClass('hide');
		} else {
			dropDown_menu.addClass('hide');
		}
	});

	//협업관리 승인/열람 툴팁 보기
	$('.tbl_data_list li[class^="data_list"]').find('li[class^="coop_approval"], li[class^="coop_read"]').mouseover(function(){
		var tooltip = $(this).find('div');
		if(tooltip.hasClass('hide')){
			tooltip.removeClass('hide');
		}
	}).mouseout(function(){
		var tooltip = $(this).find('div');
		tooltip.addClass('hide');
	});

	//문서 새창으로 열기
	$('.tblRow > span.subject').find('a.open_doc_newWindow').bind("click", function(e){
		window.open("about:blank", "", "width=500, height=500");
	});

	//문서리스트 - 마우스 오버 시 행 색 변경
	$('.tblRow > span.subject').mouseover(function(){
		$(this).parents('.overview').find('.tblRow').removeClass('current');
		$(this).parents('.tblRow').addClass('current');
	});


	//문서리스트 - 우클릭 시 나오는 context 메뉴
	$('.tblRow').find('span.subject').mousedown(function(e){
		//var pgType = "mydoc";
		//var pgType = "doclist";
		//var pgType = "mydocmodify"
		//var pgType = "bin";
		//var pgType = "favorite";
		var pgType;
		var context_menu;

		if(pgType == "mydoc") {
			context_menu = $('.mydoc_context_menu');
		} else if(pgType == "mydocmodify") {
			context_menu = $('.mydocmodify_context_menu');
		} else if(pgType == "bin"){
			context_menu = $('.mydocbin_context_menu');
		} else if(pgType == "favorite"){
			return false;
		} else {
			context_menu = $('.tbl_context_menu');
		}

		if(e.which == 3) {
			context_menu.css({
				left:e.pageX,
				top:e.pageY
			});

			$(this).parents('div.tbody').find('ul[class^="tbl_data_list"]').removeClass('current');
			$(this).parents('ul[class^="tbl_data_list"]').addClass('current');
			$(this).parent().find('input:checkbox').attr('checked', true);

			if(context_menu.hasClass('hide')){
				context_menu.removeClass('hide');
			}
		} else if(e.which == 1) {
			context_menu.addClass('hide');
		}
	});

	//첨부파일 열기
	$('img[class="attach_file"]').bind("click", function(e){
		e.preventDefault();
		var lyr_popup = $('.attach_window');
		if(lyr_popup.hasClass('hide')){
			lyr_popup.removeClass('hide');
			lyr_popup.css({
				left:e.pageX,
				top:e.pageY
			});
		}
	});

	//첨부파일 닫기
	$('.attach_window').find('a[class="close"]').bind("click", function(e){
		e.preventDefault();
		$('.attach_window').addClass('hide');
	});

	//관련문서 열기
	$('img[class="relative_docs"]').bind("click", function(e){
		e.preventDefault();
		var lyr_popup = $('.relative_docs_window');
		if(lyr_popup.hasClass('hide')){
			lyr_popup.removeClass('hide');
			lyr_popup.css({
				left:e.pageX,
				top:e.pageY
			});
		}
	});

	//관련문서 닫기
	$('.relative_docs_window').find('a[class="close"]').bind("click", function(e){
		e.preventDefault();
		$(this).parents('.relative_docs_window').addClass('hide');
	});

	//반출정보 열기/닫기
	$('img[class="doc_lock"]').mouseover(function(e){
		e.preventDefault();
		var lyr_popup = $('div[class^="doc_lock_info"]');
		if(lyr_popup.hasClass('hide')){
			lyr_popup.removeClass('hide');
			lyr_popup.css({
				left:e.pageX+($(this).width()-e.offsetX)+5,
				top:e.pageY
			});
		}
	}).mouseout(function(){
		$('div[class^="doc_lock_info').addClass('hide');
	});

	//권한조회 열기/닫기
	$('img[class="previlege_grade"]').mouseover(function(e){
		e.preventDefault();
		var lyr_popup = $('.previlege_inquiry');
		if(lyr_popup.hasClass('hide')){
			lyr_popup.removeClass('hide');
			lyr_popup.css({
				left:e.pageX + ((-1) * (lyr_popup.width()+25+e.offsetX)),
				top:e.pageY
			});
		}
	}).mouseout(function(){
		$('.previlege_inquiry').addClass('hide');
	});

	//권한정보도움말 열기/닫기
	$('.previlege_listImg').mouseover(function(){
		var lyr_popup = $('.previlege_list');
		if(lyr_popup.hasClass('hide')){
			lyr_popup.removeClass('hide');

			lyr_popup.css({
				left:(-1) * (lyr_popup.width()+30)/2
			});

		}
	}).mouseout(function(){
		$('.previlege_list').addClass('hide');
	});

	//문서 상세조회 - 의견 컨텍스트 메뉴(우클릭 시 나오는 context)
	$('.opinion_wrapper > table').find('tbody > tr > td').mousedown(function(e){
		var context_menu = $('.opinion_contextMenu');
		if(e.which == 3) {
			var offsetX = e.pageX - $('.opinion_wrapper').offset().left;
			var offsetY = e.pageY - $('.opinion_wrapper').offset().top;
			context_menu.css({
				left:offsetX,
				top:offsetY
			});

			context_menu.removeClass('hide');
			$(this).parents('table').find('tr').removeClass('current');
			$(this).parent().addClass('current');
		} else if(e.which == 1) {
			context_menu.addClass('hide');
		}
	});

	//트리 컨텍스트 메뉴
	$('.lnb_tree_list > ul').find('li[class^="jqtree_common"]').bind("mousedown", function(e){
		var context_menu = $('.doc_tree_context');

		if(e.which == 3) {
			context_menu.css({
				left:e.pageX+10,
				top:e.pageY+10
			});

			if(context_menu.hasClass('hide')){
				context_menu.removeClass('hide');
			}
		} else if(e.which == 1) {
			context_menu.addClass('hide');
		}
	});

	//트리 컨텍스트 메뉴요소 클릭 시
	$('.doc_tree_context > ul').find('li > a').bind("click", function(e){
		e.preventDefault();
		var clsNm = $(this).attr('class');

		if(clsNm == 'modify') {

		} else if(clsNm == 'delete') {
			var disabledFolder = false;
			var hasSubFolderFile = false;

			if(disabledFolder) {
				jAlert("비활성화된 폴더가 존재합니다.<br>확인하시기 바랍니다.", "확인", 1);
			}
			if(hasSubFolderFile){
				jAlert("폴더삭제시 하위 폴더 또는<br>문서 존재 시 삭제할 수 없습니다.<br>하위 폴더 및 문서를 확인하시기 바랍니다.", "확인", 1);
			}

			if(!disabledFolder && !hasSubFolderFile) {
				jConfirm("삭제하시겠습니까?", "확인", 1, function(r){

				});
			}

		} else if(clsNm == 'move') {

		} else if(clsNm == 'copy') {

		} else if(clsNm == 'favorite') {

		} else if(clsNm == 'tempbox') {

		}
	});

	//휴지통 컨텍스트 메뉴 클릭
	$('.mydocbin_context_menu > ul').find('li > a').bind("click", function(e){
		e.preventDefault();
		var clsNm = $(this).attr('class');

		if(clsNm == 'delete') {
			jConfirm("휴지통의 모든 문서를 영구삭제 하시겠습니까?", "확인", 2, function(r){
				$('.mydocbin_context_menu').addClass('hide');
			});
		} else if(clsNm == 'restore') {
			jConfirm("선택한 문서를 복원 하시겠습니까?", "확인", 2, function(r){
				$('.mydocbin_context_menu').addClass('hide');
				$('.doc_folder_choose').removeClass('hide');
				$('.doc_folder_choose').prev().removeClass('hide');
			});
		}
	});

	//즐겨찾기 트리 우클릭 - 컨텍스트 메뉴 호출
	$('.favorite_folder_tree').mousedown(function(e){
		var context_menu = $('.favorite_context_menu');
		if(e.which == 3) {
			var offsetX = e.pageX - $('.favorite_choose_cnts').offset().left;
			var offsetY = e.pageY - $('.favorite_choose_cnts').offset().top;
			context_menu.css({
				left:offsetX,
				top:offsetY
			});

			context_menu.removeClass('hide');
		} else if(e.which == 1) {
			context_menu.addClass('hide');
		}
	});

	//즐겨찾기 컨텍스트 메뉴 > 메뉴 항목 클릭
	$('.favorite_context_menu > ul').find('li > a').bind("click", function(e){
		e.preventDefault();
		var clsNm = $(this).attr('class');

		if(clsNm == 'register') {
			//즐겨찾기 등록 열기
			$('.favorite_register').removeClass('hide');
			$('.favorite_register').prev().removeClass('hide');
			lyrPopupWindowResize($('.favorite_register'));
		} else if(clsNm == 'modify') {
			//즐겨찾기 수정 열기
			$('.favorite_modify').removeClass('hide');
			$('.favorite_modify').prev().removeClass('hide');
			lyrPopupWindowResize($('.favorite_modify'));
		} else if(clsNm == 'delete') {
			jConfirm("삭제하시겠습니까?", "확인", 1, function(r){
				if(r){

				} else {

				}
			});
		} else if(clsNm == 'move') {
			//즐겨찾기 이동 열기
			$('.favorite_choose_close').trigger('click');
			$('.favorite_move').removeClass('hide');
			$('.favorite_move').prev().removeClass('hide');
			lyrPopupWindowResize($('.favorite_move'));
		}
		$('.favorite_context_menu').addClass('hide');
	});

	//협업상세 - 수신자 툴팁
//	$('.approvalResult_receiver').mouseover(function(){
//		var div = $(this).find('div');
//
//		if(div.hasClass('hide')){
//			div.removeClass('hide');
//		}
//	}).mouseout(function(){
//		$(this).find('div').addClass('hide');
//	});


	//메시지창, 컨텍스트 메뉴 다른 곳 클릭 시 없어지게 하기
	$('body').on("click", '.wrap', function(e){
		var headerTargetDiv = $(e.target).parents(".header_user_menu").attr('class');
		var extFunctionTargetDiv = $(e.target).parents(".tbl_extFunction").attr('class');
		var integratedSearchTargetDiv = $(e.target).parents('.search_detail').find('.integrated_dropDown_menu').attr('class');

		if(headerTargetDiv == undefined) {
			//헤더 사용자 메뉴 hide
			var headerUserMenu = $('.header_user_menu').find('div[class*="dropDown_menu"]');
			$.each(headerUserMenu, function(i){
				headerUserMenu.eq(i).addClass('hide');
			});
		}

		if(extFunctionTargetDiv == undefined) {
			//추가기능 드롭다운 메뉴 hide
			var dd_txt = $('.tbl_extFunction > a.dropDown_txt');
			var extFuncDDMenu = $('.extFunction_dropDown_wrapper');

			if(!extFuncDDMenu.hasClass('hide')){
				dd_txt.trigger("click");
			}
		}

		// 문서상세보기 FocusOut - 소스정리대상
		var detailSearchDiv = $(e.target).parents(".srch_detail_dropDown").attr('class');
		if(detailSearchDiv == undefined) {

			if(!$(".detail_dropDown_menu").hasClass('hide')) {
				$(".detail_dropDown_menu").addClass('hide');
				$("#docDetailDropDown").removeClass('up');
				$("#docDetailDropDown").addClass('down');
			}
		}

		if(integratedSearchTargetDiv == undefined) {
			//통합검색 메뉴 hide
			if(!$('#searchDetailView').hasClass('hide')){
				$('#searchDetailView').addClass('hide');
			}
		}


	});


});
