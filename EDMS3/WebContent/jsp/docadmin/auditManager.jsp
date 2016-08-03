<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	대량문서 열람 감사 관리
 -->
<div class="cnt_wrapper">
	<div class="depth_navigation">${menuInfo.su_menu_nm} > ${menuInfo.menu_nm}</div>
	
	<div class="massAudit_sub_wrapper">
		<div class="sub_left">
			<div class="srch_form">
				<table>
					<tr>
						<th>감사일</th>
						<td>
							<input type="text" id="sdate" class="cal" readonly="readonly">
							~								
							<input type="text" id="edate" class="cal" readonly="readonly">
							<a href="javascript:void(0);" class="search" onclick="javascript:auditManager.event.searchFunc();"><img src="${contextRoot}/img/icon/search_btn.png" alt="" title=""></a>
						</td>
					</tr>
				</table>
			</div>
			<div class="srch_result_wrapper">
				<div class="srch_result" id="targetAuditGrid">
					<table id="auditGridList"></table>
				</div>
			</div>
			<div class="pg_subWrapper">
				<div class="pg_navi_wrapper">
					<ul class="pg_navi" id="auditGridPager"></ul> 	            
	           	</div>					
			</div>
		</div>
		
		<div class="sub_right">
			<div>
				<table>
					<colgroup>
						<col width="300"/>
						<col width="650"/>
					</colgroup>
					<tr>
						<th><span class="bold" id="detailTitle"></span></th>
						<td>&nbsp;</td>
					</tr>
				</table>
			</div>
			<div class="dup_file_list" id="tagetAuditDetail">
				<!-- jqGrid 영역 -->
				<table id="auditDetailList"></table>
			</div>
			<div class="pg_navi_wrapper">
	           	<ul class="pg_navi" id="auditDetailPager"></ul>    
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="${contextRoot}/js/docadmin/auditManager.js"></script>
<script type="text/javascript">
jQuery(function() {		
		
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	auditManager.init.initPage('${part}','${sdate}','${edate}');
	
	$("#sdate").datepicker({dateFormat:'yy-mm-dd'});
	$("#edate").datepicker({dateFormat:'yy-mm-dd'});
	
	exsoft.util.grid.gridResize('auditGridList', 'targetAuditGrid', 20,0);
	exsoft.util.grid.gridResize('auditDetailList','tagetAuditDetail',0,0);
});
</script>