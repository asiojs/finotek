<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	시스템관리 - 메뉴접근권한관리
 -->
 <div class="cnt_wrapper">
	<div class="depth_navigation">
		${menuInfo.su_menu_nm} &gt; ${menuInfo.menu_nm}
	</div>
	
	<div class="lnb_menuAcl">
		<div class="menuAcl_role">
			<form>
				<label class="menuAcl_role_lbl">
					역할명
					<input type="text" name="strKeyword" id="strKeyword" class="menuAcl_roleName" maxlength="30" onkeydown="javascript:if(event.keyCode == 13) { exsoftAdminMenuFunc.event.searchFunc(); return false;}" >
				</label>
				<button type="button" class="menuAcl_roleName_find" onclick="javascript:exsoftAdminMenuFunc.event.searchFunc();"></button>
			</form>
		</div>
		<div class="menuAcl_btnGrp">
			<button type="button" class="btn1" onclick="javascript:exsoftAdminMenuFunc.open.writeFunc();">등록</button>
			<button type="button" class="btn1" onclick="javascript:exsoftAdminMenuFunc.close.delFunc();">삭제</button>
			<a href="javascript:void(0);" class="menuAcl_refresh" onclick="javascript:exsoft.util.grid.gridRefresh('roleGrid','${contextRoot}/admin/codePage.do')">
				<img src="${contextRoot}/img/icon/menu_refresh.png" alt="" title="">
			</a>
		</div>
		<div class="menuAcl_grid" id="targetRoleGrid">
			<table id="roleGrid"></table>			
			<div class="pg_subWrapper"><div class="pg_navi_wrapper"><ul class="pg_navi" id="rolePager"></ul></div></div>
		</div>		
	</div>

	<div class="sub_contents">
		<div class="depth_navigation">
			접근권한 :: <span id="roleTitle"></span>
		</div>
		<div class="authManager btnGrp right">
			<button type="button" id="" class="btn1" onclick="javascript:exsoftAdminMenuFunc.event.modifyFunc();">수정하기</button>
		</div>
		<div class="grid_tree" id="targetMenuGrid">			
			<table id="menuGrid"></table>
		</div>
		<div class="grid_btnGrp">
			<div class="grid_btnGrp_subWrapper">
				<a href="javascript:void(0);" class="" onclick="javascript:exsoftAdminMenuFunc.ui.treeGridAdd();"><img src="${contextRoot}/img/icon/add_user.png" alt="" title=""></a>
				<a href="javascript:void(0);" class="" onclick="javascript:exsoftAdminMenuFunc.ui.treeGridDel();"><img src="${contextRoot}/img/icon/sub_user.png" alt="" title=""></a>
			</div>
		</div>
		<div class="menuAcl_list">
			<div class=""  id="targetMenuAuthGrid">				
				<table id="menuAuthGrid"></table>
			</div>
		</div>
		<div class=""></div>
		</div>
</div>
<!--  역할 등록/수정 -->
<div class="user_role_wrapper hide"></div>
<div class="user_role hide">
	<div class="window_title">
		역할 관리
        <a href="javascript:void(0);" class="window_close" onclick="exsoft.util.layout.divLayerClose('user_role_wrapper', 'user_role');"><img src="/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="user_role_cnts">
    	<form name="frm" id="frm">
    	<input type="hidden" name="type" id="type"> 
    	<input type="hidden" name="gcode_id" id="gcode_id" value="ROLE">
    	<table>
    		<tr>
    			<th>역할ID</th>
    			<td><input type="text" name="code_id" id="code_id" placeholder="역할ID를 입력하세요" maxlength="30" onkeyup="javascript:exsoftAdminMenuFunc.ui.upper(this);"></td>
    		</tr>
    		<tr>
    			<th>역할명</th>
    			<td><input type="text" name="code_nm" id="code_nm" placeholder="역할명을 입력하세요" maxlength="30" ></td>
    		</tr>
    	</table>
		</form>
    </div>
   	<div class="btnGrp">
   		<button type="button" onclick="javascript:exsoftAdminMenuFunc.event.managerFunc();">저장</button>
   		<button type="button" onclick="exsoft.util.layout.divLayerClose('user_role_wrapper', 'user_role');">닫기</button>
   	</div>
</div>
<!--  역할 등록/수정 -->
<script type="text/javascript" src="${contextRoot}/js/sysadmin/menuAuthManager.js"></script>
 <script type="text/javascript">
jQuery(function() {		
		
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	exsoftAdminMenuFunc.init.initPage("${pageSize}","roleGrid");
	
	exsoftAdminMenuFunc.event.roleGridList();							// 작업권한 유형
	exsoftAdminMenuFunc.event.menuGridList();						// 메뉴목록
	
	exsoft.util.grid.gridResize('roleGrid','targetRoleGrid',20,0);		// Role TreeGrid
	exsoft.util.grid.gridResize('menuGrid','targetMenuGrid',20,0);
	exsoft.util.grid.gridResize('menuAuthGrid','targetMenuAuthGrid',20,0);
	
	// 사용되는 레이아웃 POP DEFINE
	exsoft.util.layout.lyrPopupWindowResize($(".user_role"));		
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".user_role"));	
	});
	
	// 역할명 대문만 입력처리
	$('#code_id').focusout(function(event) { 
		this.value=this.value.replace(/[^A-Z]/g,'')
	});
	
	// CheckBox Browser Bug Fix
	$('#cb_roleGrid').click(function(e) {
		exsoft.util.grid.checkBox(e);
    });

});
</script>