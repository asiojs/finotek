<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	사용자관리
 -->
 <div class="cnt_wrapper">
	<div class="depth_navigation">
		시스템 관리 &gt; 사용자 관리
	</div>
	
	<div class="lnb_fullTree">
		<div id="groupTree"></div>
	</div>

	<div class="sub_contents">
		<div class="search_menu">
			<form>
				<select id="search_type" name="" class="">
					<option value="user_name">성명</option>
					<option value="group_name">부서</option>
				</select>
				<input type="text" id="search_txt" name="" class="" placeholder="검색어를 입력하세요" onkeypress="javascript:return userManager.event.enterKeyPress(event);">
				<button type="button" onclick="javascript:userManager.event.searchFunction();" class="btn1">검색</button>
			</form>
		</div>
		<div class="tbl_btn_menu">
			<button type="button" class="btn1" onclick="javascript:userManager.event.userRegist();">등록</button>
			<button type="button" class="btn1" onclick="javascript:userManager.event.userDelete();">삭제</button>
			<button type="button" class="btn1" onclick="javascript:userManager.event.userMoveGroup();">이동</button>
			<button type="button" class="btn1" onclick="javascript:userManager.event.userStatusUpdate('C');">사용</button>
			<button type="button" class="btn1" onclick="javascript:userManager.event.userStatusUpdate('D');">중지</button>
			<button type="button" class="btn1" onclick="javascript:userManager.event.userPassReset();">비밀번호 초기화</button>
			<button type="button" class="btn1" onclick="javascript:excelUploadWindow.event.userUploadView();">일괄 업로드</button>
			<button type="button" class="btn1" onclick="javascript:excelUploadWindow.event.userDownload();">업로드 샘플</button>
		</div>
		<div id="targetUserGrid" class="tbl_user_grid">
			<table id="userGridList"></table>
		</div>
		</div>
</div>
<script type="text/javascript" src="${contextRoot}/js/sysadmin/userManager.js"></script>
<script type="text/javascript">
jQuery(function() {		
		
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	userManager.init();
	
	exsoft.util.common.ddslick('#search_type','srch_type1','',70, function(){});
	
	// 사용자 목록 그리드 초기화
	if($('#userGridList')[0].grid != undefined)	{	
		var postData = {groupId:''} ;
		exsoft.util.grid.gridPostDataRefresh('userGridList','${contextRoot}/user/searchUserList.do',postData);
	}else {
		userManager.event.userGridList();
	}
	
	// 사용자 목록 그리드 리사이즈
	exsoft.util.grid.gridResize('userGridList','targetUserGrid',55,0);
	
});
</script>

<jsp:include page="/jsp/popup/registUserWindow.jsp" /><!-- 등록버튼 클릭 -->
<jsp:include page="/jsp/popup/selectGroupWindow.jsp" /><!-- 관리부서선택 / 이동 -->
<jsp:include page="/jsp/popup/excelUploadWindow.jsp" /><!-- 엑셀 일괄 업로드 -->