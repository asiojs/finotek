$(function(){
	//탭 요소 클릭 시 폼 변경
    $('.tab_element').bind("click", function(){
        var idx = $(this).index();
        var targetFrm = $(this).parent().parent().parent().find('div[class^="tab_form"]');
        targetFrm.addClass('hide');
        targetFrm.eq(idx).removeClass('hide');

        $('.tab_element').removeClass('selected');
        $(this).addClass('selected');
    });

	//쪽지검색 검색분류항목
	$('#myNote_srch_type').ddslick({
		width:93,
		background:"rgba(255, 255, 255, 0)",
		onSelected: function(selectedData){}
	});

    //쪽지 쓰기
	/*
    $('.myNote_compose').bind("click", function(){
    	//작업카트 - URL 메일 송부
    	$('.myNoteWrite').removeClass('hide');
		//$('.myNoteWrite').prev().removeClass('hide');
        //lyrPopupWindowResize($('.myNoteWrite'));
    });
    */

    $('.note_reply').bind("click", function(){
    	var tabIdx = $('[class^="tab_element"][class$="selected"]').index();

    	//답장을 보낼 대상자 정보를 받아서
    	//뿌려주고 show 시킴
    	//tabIdx == 2 : 보낸 쪽지함
    	//보낸쪽지함은 전달로, 나머지는 답장 창 호출
    	if(tabIdx == 2) {
    		$('.myNoteForward').removeClass('hide');
    	} else {
    		$('.myNoteReply').removeClass('hide');
    	}

    });

    //쪽지관리 - 창 닫기
    /*
    $('.myNote_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.myNote').addClass('hide');
    	$('.myNote_wrapper').addClass('hide');
    });
*/
    /*
    //쪽지관리 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.myNote_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.myNote').addClass('hide');
    });
    */
    //쪽지쓰기 - 창 닫기
    /*
    $('.myNoteWrite_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.myNoteWrite').addClass('hide');
    	$('.myNoteWrite_wrapper').addClass('hide');
    });
	*/
    /*
    //쪽지쓰기 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.myNoteWrite_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.myNoteWrite').addClass('hide');
    });
    */
    //쪽지 사용자 선택 - 창 닫기
    /*
    $('.note_choose_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.note_choose').addClass('hide');
    	//$('.note_choose_wrapper').addClass('hide');
    });
*/
    /*
    //쪽지 사용자 선택 - 창 닫기 : 음영진 부분 클릭 시 닫기
    $('.note_choose_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.note_choose').addClass('hide');
    });
    */

    //쪽지 전달 - 창 닫기
    $('.myNoteForward_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.myNoteForward').addClass('hide');
//    	$('.note_choose_wrapper').addClass('hide');
    });

    //쪽지 답장 - 창 닫기
    $('.myNoteReply_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.myNoteReply').addClass('hide');
//    	$('.note_choose_wrapper').addClass('hide');
    });

	//쪽지 관리 - 대화함 내용 지우기
	$('.myNote_cnts').find('a.delete_myNote_chat').bind("click", function(e){
		e.preventDefault();
		jConfirm("전체 대화 내역을 삭제하시겠습니까?", "확인", 6, function(r){

		});
	});

	//쪽지 관리 - 쪽지 지우기
	$('.myNote_cnts').find('a.delete_myNote').bind("click", function(e){
		e.preventDefault();
		jConfirm("쪽지를 삭제하시겠습니까?", "확인", 6, function(r){

		});
	});

	//쪽지 관리 - 쪽지 보관
	$('.myNote_cnts').find('a.inbox_myNote').bind("click", function(e){
		e.preventDefault();
		jConfirm("선택한 쪽지를 보관함에 보관 하시겠습니까?", "확인", 6, function(r){

		});
	});
});


//레이어팝업 수직가운데 정렬
function lyrPopupWindowResize(obj) {
	var wrap = $('.wrap');
	obj.prev().css({
		height:$(document).height(),
	  	width:$(document).width()
	});
	obj.css({
	    top:(wrap.height()-obj.height()-50)/2,
	    left:(wrap.width()-obj.width())/2
	});
}
