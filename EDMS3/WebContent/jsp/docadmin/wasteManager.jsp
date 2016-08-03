<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	시스템 휴지통 관리
 -->
<div class="cnt_wrapper">
<div class="depth_navigation">${menuInfo.su_menu_nm} &gt; ${menuInfo.menu_nm}</div>

	<div class="bin_sub_wrapper">
		<div class="sub_left">
			<div class="srch_form">
				<table>
					<tr>
						<th>삭제일 </th>
						<td>							
							<input type="text" name="sdate" id="sdate" class="cal" value="" placeholder="">							
							~							
							<input type="text" name="edate" id="edate" class="cal" value="" placeholder="">							
						</td>
					</tr>
					<tr>
						<th>제목</th>
						<td><input type="text"  name="strKeyword" id="strKeyword" class="bin_srch_keyword" maxlength="100"></td>
					</tr>
					<tr>
						<th>소유자</th>
						<td>
							<input type="text" name="ownerKeyword" id="ownerKeyword" class="" maxlength="32">
							<button type="button" id="" class="" onClick="javascript:exsoftAdminDocFunc.search.searchFunc();">검색</button>
						</td>
					</tr>
				</table>
			</div>
			<div class="srch_result_wrapper">
				<div class="waste_btnGrp">
					<button type="button" id="" class="btn1" onclick="javascript:exsoftAdminDocFunc.event.selectedDelete();">영구삭제</button>
					<button type="button" id="" class="btn1" onclick="javascript:exsoftAdminDocFunc.event.allDelete();" >휴지통 비우기</button>
					<!--  엑셀다운로드 Refresh 버튼 필요 -->
					
					<div class="menu_imgBtnGrp">
						<a href="javascript:void(0);" class="menu_excel" onClick="javascript:exsoft.util.grid.excelDown('wasteGridList','${contextRoot}/admin/documentList.do');"><img src="${contextRoot}/img/icon/menu_download.png" alt="" title=""></a>
						<a href="javascript:void(0);" class="menu_refresh" onclick='javascript:exsoft.util.grid.gridRefresh("wasteGridList","${contextRoot}/admin/documentList.do");'><img src="${contextRoot}/img/icon/menu_refresh.png" alt="" title=""></a>
					</div>
				</div>
				<div class="srch_result" id="targetWasteGrid">			
					<table id="wasteGridList"></table>
				</div>				
			</div>
			<div class="pg_subWrapper">
				<div class="pg_navi_wrapper">
					<ul class="pg_navi" id="wasteGridPager"></ul>     
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/docadmin/docDetail.jsp" /><!--  만기문서관리/소유권변경/휴지통관리 상세화면 공통 -->
	</div>
</div>
<script type="text/javascript" src="${contextRoot}/js/docadmin/docadmin.js"></script>
<script type="text/javascript">
jQuery(function() {		
		
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');

	// 시스템 휴지통 관리
	exsoftAdminDocFunc.init.initPage('#wasteGridList','/admin/documentList.do','${pageSize}','${part}');		
	exsoftAdminDocFunc.event.fWasteListGrid();
	
	exsoft.util.grid.gridResize('wasteGridList','targetWasteGrid',20,0);
	exsoft.util.grid.gridResize('detail_docHistoryList','targetDocHistoryList',20,0);
	
	// CheckBox Browser Bug Fix
	$('#cb_wasteGridList').click(function(e) {
		exsoft.util.grid.checkBox(e);
    });
	
});
</script>