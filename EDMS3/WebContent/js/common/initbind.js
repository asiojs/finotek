/**
 * 공통화면에 대한 bind 처리
 */

$(function(){
	//우클릭 방지 관련 : 퍼블리싱 완료되면 주석 제거
	document.oncontextmenu=function(){return false;}
	
	exsoft.common.bind.leftMenuToggle(); 	// 좌측 메뉴 펼치기/숨기기
	exsoft.common.bind.quickMenuToggle();	// 퀵메뉴 펼치기/숨기기
	exsoft.common.bind.layoutViewToggle(); 	// 화면분활 메뉴 선택 펼치기/숨기기
	exsoft.common.bind.layoutViewDivide();	// 화면 상하좌우 분화에 따라 화면 보이기
	exsoft.common.bind.event.layoutDragHorizontal(); // 마우스 drag에 따라 화면 좌우 화면 비율 설정
	exsoft.common.bind.event.layoutDragVertical();	 // 마우스 drag에 따라 화면 상하 화면 비율 설정
	exsoft.common.bind.event.commentCentextMenu();	 // 문서 상세조회 의견 contextMenu
	exsoft.common.bind.event.divDropDown();			 // div dropDown(메뉴쳘치기/숨기기)
	
	exsoft.common.bind.event.urlEmailClose();      //url화면 닫기
	exsoft.common.bind.event.docDetailWindowClose(); //문서 상세조회 탭 닫기
	
	//일자 달력 설정(달력 한글 표시)
	$.datepicker.regional['ko'] = {
          closeText: '닫기',
          prevText: '이전달',
          nextText: '다음달',
          currentText: '오늘',
          monthNames: ['1월','2월','3월','4월','5월','6월',
          '7월','8월','9월','10월','11월','12월'],
          monthNamesShort: ['1월','2월','3월','4월','5월','6월',
          '7월','8월','9월','10월','11월','12월'],
          dayNames: ['일','월','화','수','목','금','토'],
          dayNamesShort: ['일','월','화','수','목','금','토'],
          dayNamesMin: ['일','월','화','수','목','금','토'],
          buttonImageOnly: false,
          weekHeader: 'Wk',
          dateFormat: 'yy-mm-dd',
          firstDay: 0,
          isRTL: false,
          duration:200,
          showAnim:'show',
          showMonthAfterYear: false
    };
    $.datepicker.setDefaults($.datepicker.regional['ko']);
});