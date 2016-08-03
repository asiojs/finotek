<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	중복파일 관리
 -->
 <div class="cnt_wrapper">
	<div class="depth_navigation">${menuInfo.su_menu_nm} &gt; ${menuInfo.menu_nm}</div>
	
	<div class="dup_sub_wrapper">
		<div class="sub_left">
			<div class="srch_form">
				<table>
					<tr>
						<th>등록일</th>
						<td>			
							<div class="decade_srch_date">				
								<input type="text" id="sdate" name="" class="cal" value="" placeholder="">							
								~							
								<input type="text" id="edate" name="" class="cal" value="" placeholder="">
							</div>
							<select id="decade">
								<option value="all">전체</option>
								<option value="one_month">1개월</option>
								<option value="three_month">3개월</option>
								<option value="half_year">6개월</option>
								<option value="one_year">1년</option>
								<option value="input">직접입력</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>파일명</th>
						<td>
							<input type="text" name="strKeyword" id="strKeyword" maxlength="30" onkeydown="javascript:if(event.keyCode == 13) { exsoftAdminDupFunc.event.searchFunc(); return false;}">
							<button type="button" id="" class="" onclick="javascript:exsoftAdminDupFunc.event.searchFunc();">검색</button>
						</td>
					</tr>
				</table>
			</div>
			<div class="srch_result_wrapper">
				<div class="srch_result" id="targetDupGrid">					
					<table id="dupGridList"></table>
				</div>
			</div>
			<div class="pg_subWrapper">
				<div class="pg_navi_wrapper">
					<ul class="pg_navi" id="dupGridPager"></ul>
				</div>
			</div>
		</div>
		
		<div class="sub_right">
			<div>
				<table>
					<colgroup>
						<col width="1000"/>
					</colgroup>
					<tr>
						<th><span class="bold" id="detailTitle"></span></th>
					</tr>
				</table>
			</div>
			<div class="dup_file_list"  id="targetDupDocGrid">
				<div class="dup_list_btnGrp">
					<button type="button" id="" name="" class="btn1" onclick="javascript:exsoftAdminDupFunc.event.delDoc();">삭제</button>
				</div>
					<table id="dupDocGridList"></table>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="${contextRoot}/js/docadmin/duplicateManager.js"></script>
<script type="text/javascript">
jQuery(function() {		
		
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	exsoftAdminDupFunc.init.initPage('#dupGridList','/admin/duplicatePage.do','${pageSize}','${part}');
	exsoftAdminDupFunc.event.fDupListGrid();
	
	exsoft.util.grid.gridResize('dupGridList','targetDupGrid',20,0);
	exsoft.util.grid.gridResize('dupDocGridList','targetDupDocGrid',20,0);
	
	// CheckBox Browser Bug Fix
	$('#cb_dupGridList').click(function(e) {
		exsoft.util.grid.checkBox(e);
    });

	$('#cb_dupDocGridList').click(function(e) {
		exsoft.util.grid.checkBox(e);
    });
	
});
</script>