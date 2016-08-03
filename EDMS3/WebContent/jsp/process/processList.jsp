<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="cnt_wrapper">
	<jsp:include page="/jsp/layout/processMenu.jsp" />	  
	<div class="contents">
		<div class="cnts_srch_wrapper">
			<div class="toggle_tree_menu"></div>
				<div class="cnts_srch_detail">	
					<div class="cnts_srch">
						<select id="processList_select" name='strIndex'>
							<option value="ALL_TYPE" selected="selected">전체</option>
							<option value="name">제목</option>
							<option value="creator_name">작성자</option>
						</select>
						<input type="text" class="srch_keyword" name="strKeyword" placeholder="검색어를 입력하세요">
						<button type="button" class="srch_btn"></button>						
					</div>
				<jsp:include page="/jsp/popup/processDetailSearch.jsp" /><!-- 상세검색 Layer 공통영역으로 분리 -->
				</div>
				<div class="depth_navi"><span id="processTitle"></span></div>
		</div>
		<div class="cnts_tbl_menu">
				<div class="tbl_menu_left">
					<button type="button" class="tbl_coop_del">삭제</button>
				</div>
				<div class="tbl_menu_right">			
					<ul class="tbl_thumbMenu">
						<li class="excel_download"><a href="javascript:void(0);" class="menu" onClick="javascript:exsoft.util.grid.excelDown('processDocGridList','${contextRoot}/process/processList.do');"></a></li>
						<li class="menu_refresh"><a href="javascript:void(0);" class="menu" onclick="javascript:exsoft.util.grid.gridRefresh('processDocGridList','${contextRoot}/process/processList.do');"></a></li>
					</ul>
					 
					<select id="tbl_rowCount">
					<option value="5">5개</option>
					<option value="10" selected>10개</option>
                    <option value="15">15개</option>
                    <option value="20">20개</option>
                    <option value="30">30개</option>
                    <option value="50">50개</option>
					</select>
				</div>
		</div>	
        <div class="cnts_tbl_wrapper" id="targetProcessDocGridList">
			<div class="cnts_list"><table id="processDocGridList"></table></div>
        </div>
        <div class="pg_navi_wrapper">
			<ul id="processDocGridPager" class="pg_navi"></ul>
		</div>
	</div>
</div>
<div class='tooltip hide'></div>
<!-- script add -->
<script type="text/javascript" src="${contextRoot}/js/process/processList.js"></script>
<script type="text/javascript">
jQuery(function() {
	exsoftProcessFunc.init.initPage("${menuType}", "${processType}", 'processTitle', '${pageSize}');
	exsoft.util.grid.gridResize('processDocGridList','targetProcessDocGridList',20,0);
	
	// 상세검색 초기화
    processDetailSearch.targetGridId = "processDocGridList";
	
});
</script>
<jsp:include page="/jsp/process/processView.jsp" />
<jsp:include page="/jsp/popup/selectSingleFolderWindow.jsp"/>