//우클릭 방지 관련 : 퍼블리싱 완료되면 주석 제거
//document.oncontextmenu=function(){return false;}

$(window).load(function(){


	//배열로 한번에 initializing
	lyrPopupWindowResizeArr([

	]);
});

$(window).resize(function(){
	//윈도우 위치 배치
	//저장금지 확장자 선택

	//배열로 한번에 initializing
	lyrPopupWindowResizeArr([

	]);
});

$(function(){
	$('.header_icon_menu').bind("mouseover", function(){
		/*
		var currDiv = $(this).find('div.subMenu_element');
		var div = $(this).parent().find('.subMenu_element');

		div.addClass('hide');
		currDiv.removeClass('hide');
		*/

		$('.header').stop().animate({
			height:287
		}, 500, function(){
			$('.header').css('overflow','visibility');
		});
	}).bind("mouseout", function(){
		$('.header').stop().animate({
			height:65
		}, 500, function(){
			$('.header').css('overflow','hidden');
		});
	});



	/*
	 * 드롭다운
	 */
	//그룹관리 - 정렬순서
	/*
	$('#group_order').ddslick({
		width:79,
		background:"rgba(255, 255, 255, 0)",
		onSelected: function(selectedData){}
	});

	//그룹관리 - 정렬순서
	$('#use_yn').ddslick({
		width:79,
		background:"rgba(255, 255, 255, 0)",
		onSelected: function(selectedData){}
	});

	//문서관리 속성 추가 삭제
	//필수
	$('#attr_required1').ddslick({
		width:76,
		background:"rgba(255, 255, 255, 0)",
		onSelected: function(selectedData){}
	});

	//편집
	$('#attr_editable1').ddslick({
		width:76,
		background:"rgba(255, 255, 255, 0)",
		onSelected: function(selectedData){}
	});

	//검색
	$('#attr_searchable1').ddslick({
		width:76,
		background:"rgba(255, 255, 255, 0)",
		onSelected: function(selectedData){}
	});

	//입력형태
	$('#attr_inputType1').ddslick({
		width:96,
		background:"rgba(255, 255, 255, 0)",
		onSelected: function(selectedData){}
	});
*/
});

//레이어팝업 show action
function layerPopupShow(obj){
	obj.removeClass('hide');
	obj.prev().removeClass('hide');
	lyrPopupWindowResize(obj);
}

//레이어팝업 수직가운데 정렬
function lyrPopupWindowResize(obj) {
	var wrap = $('.wrap');

	obj.prev().css({
		height:$(document).height(),
		width:$(document).width()
	});
	obj.css({
		top:(wrap.height()-obj.height())/2,
		left:(wrap.width()-obj.width())/2
	});
}

//레이어팝업 수직가운데 정렬(배열)
function lyrPopupWindowResizeArr(arr) {
	var wrap = $('.wrap');

	for(var i = 0; i < arr.length; i++) {
		arr[i].prev().css({
			height:$(document).height(),
			width:$(document).width()
		});
		arr[i].css({
			top:(wrap.height()-arr[i].height())/2,
			left:(wrap.width()-arr[i].width())/2
		});
	}
}