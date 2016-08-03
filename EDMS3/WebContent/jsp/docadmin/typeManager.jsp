<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	문서유형관리
 --> 
<div class="cnt_wrapper">
	<div class="depth_navigation">${menuInfo.su_menu_nm} > ${menuInfo.menu_nm}</div>
	
	<div class="doc_sub_wrapper">
		<div class="sub_left">
			<div class="srch_form">
				<span class="srch_doc_lbl">문서유형명</span>
				<input type="text" name="strKeyword" id="strKeyword" class="srch_doc_keyword" maxlength="20" onkeydown="javascript:if(event.keyCode == 13) { exsoftAdminTypeFunc.event.searchFunc(); return false;}" >
				<button type="button" class="srch_doc_btn " onclick="javascript:exsoftAdminTypeFunc.event.searchFunc();"onclick="javascript:exsoftAdminTypeFunc.event.searchFunc();">검색</button>
			</div>
			<div class="srch_result_wrapper">
				<div class="left">
					<button type="button" id="" name="" class="btn1" onclick="javascript:exsoftAdminTypeFunc.open.typeAdd();">등록</button>
					<button type="button" id="" name="" class="btn1" onclick="javascript:exsoftAdminTypeFunc.event.typeDel();">삭제</button>
					
					<div class="menu_imgBtnGrp1">
						<a href="javascript:void(0);" class="menu_refresh" onclick="javascript:exsoft.util.grid.gridRefresh('typeGridList','${contextRoot}/admin/typeList.do')"><img src="${contextRoot}/img/icon/menu_refresh.png" alt="" title=""></a>
					</div>
				</div>
				<div class="srch_result" id="targetTypeGrid">
					<table id="typeGridList"></table>					
				</div>
			</div>
			<div class="pg_subWrapper">
				<div class="pg_navi_wrapper">
					<ul class="pg_navi" id="typeGridPager"></ul> 	            
	           	</div>					
			</div>
		</div>
					
		<div class="sub_right">
		<form name='frmView' id="frmView">
			<input type="hidden" name="attrArrayList"> 
			<input type="hidden" name="type">
			<div class="doc_config_info">
				<table>
					<colgroup>
						<col width="108"/>
						<col width="774"/>
					</colgroup>
					<tr>
					<th><span class="bold" id="attrTitle" data-bind="attrTitle" ></span></th>
					<th><button type="button" id="atrrDelBtn" name="" class="btn1" onclick="javascript:exsoftAdminTypeFunc.event.typeViewDel();">삭제</button></th>
					</tr>
					<tr>
					<th class="normal">문서유형 ID<span class="required">*</span></th>
					<td>	<input type="text"  name="type_id" id="type_id" data-bind="type_id" class="docType_id" readonly></td>
					</tr>
					<tr>
					<th class="normal">문서유형명<span class="required">*</span></th>
					<td><input type="text" name="type_name" id="type_name" data-bind="type_name" class="docType_name"></td>
					</tr>
					<tr>
					<th class="normal">정렬순서</th>
					<td><input type="text" name="sortIndex" id="sort_index" data-bind="sort_index" class="sort_index" maxlength="3"></td>
					</tr>
					<tr>
					<th class="normal">사용여부</th>
					<td>
						<select id="is_hiddenU" data-bind="is_hidden">
							<option value="F">사용</option>
							<option value="T">중지</option>
						</select>
					</td>
					</tr>
					<tr>
					<th class="normal">등록일</th>
					<td><span id="create_date" data-bind="create_date"></span></td>
					</tr>
				</table>
			</div>
			
			<div class=""  id="targetAttrViewList">
				<div class="config_list_btnGrp">
					<button type="button" id="attrItemAdd" name="" class="btn1"  onclick="javascript:exsoftAdminTypeFunc.ui.addRowReg('update');">추가</button>
					<button type="button" id="attrItemDel" name="" class="btn1" onclick="javascript:exsoftAdminTypeFunc.ui.delRowReg('update','attrViewList');">제외</button>
				</div>				
				<table id="attrViewList"></table>
			</div>
		</form>	
		</div>
	</div>
	<div class="btnGrp">
		<button type="button" class="btn1 bold"  id="attrUpdateBtn" onclick="javascript:exsoftAdminTypeFunc.event.applyTypeWrite('update');">저장</button>
		<button type="button" class="btn1"  id="attrCancelBtn" onclick="javascript:exsoftAdminTypeFunc.ui.cancelProc();">취소</button>
	</div>
</div>
<!-- 문서유형 등록 시작 -->
 <div class="register_docuType_wrapper hide"></div>
	<div class="register_docuType hide">
		<div class="window_title">
			문서유형 등록
         <a href="javascript:void(0);" class="window_close" onclick="javascript:exsoft.util.layout.divLayerClose('register_docuType_wrapper','register_docuType');"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
     </div>
    <form name="frm" id="frm">
	<input type="hidden" name="attrArrayList">
	<input type="hidden" name="type">
     <div class="register_docuType_cnts">
     	<table>
     		<tr>
     			<th>문서유형ID</th>
     			<td><input type="text" id="" name="type_id"  data-bind="type_id" class="" maxlength="30" onkeypress="return  exsoft.util.filter.inputBoxFilter('[A-Z]');"  maxlength="30" onkeyup="this.value=this.value.replace(/[^a-zA-Z]/g,'')"></td>
     			<th>문서유형명</th>
     			<td><input type="text" id="" name="type_name" data-bind="type_name" class="" maxlength="20"></td>
     		</tr>
     		<tr>
     			<th>사용여부</th>
     			<td>
     				<select id="is_hiddenC" data-bind="is_hidden">
     					<option value="F">사용</option>
						<option value="T">중지</option>
     				</select>
     			</td>
     			<th>정렬순서</th>
     			<td><input type="text" class="numline" value="0" name="sortIndex" size="15" maxlength="3" /></td>
     		</tr>
     	</table>
     	<div class="register_docuType_btnGrp">
     		<button type="button" id="" class="btn1" onclick="javascript:exsoftAdminTypeFunc.ui.addRowReg('regist');">추가</button>
     		<button type="button" id="" class="btn1" onclick="javascript:exsoftAdminTypeFunc.ui.delRowReg('regist','attrGridList');">삭제</button>
     	</div>
     	<div class="register_docuType_field">
     		<table  id="attrGridList" id="targetAttrGridList"></table>
     	</div>
     </div>
     </form>
    	<div class="btnGrp">
    		<button type="button" id="" class="register_docuType_btn bold" onclick="javascript:exsoftAdminTypeFunc.event.applyTypeWrite('regist');">저장</button>
    		<button type="button" id="" class="cancel_docuType_btn cancel"  onclick="javascript:exsoft.util.layout.divLayerClose('register_docuType_wrapper','register_docuType');">취소</button>
    	</div>
 </div>
<!-- 문서유형 등록 끝 -->
	   	
<!-- 항목 설정 시작 -->
<div class="register_docuCateType_wrapper hide"></div>
<div class="register_docuCateType hide">
	<div class="window_subtitle">
		항목 설정
        <a href="javascript:void(0);" class="window_close" onclick="javascript:exsoft.util.layout.divLayerClose('register_docuCateType_wrapper','register_docuCateType');"><img src="${contextRoot}/img/icon/window_close1.png" alt="" title=""></a>
    </div>
    <div class="register_docuCateType_cnts">
    	<div class="register_docuCateType_btnGrp">
    		<button type="button" id="" class="" onclick="javascript:exsoftAdminTypeFunc.ui.attrItemAdd();" >추가</button>
    		<button type="button" id="" class="" onclick="javascript:exsoftAdminTypeFunc.ui.attrItemDel();">삭제</button>
    	</div>
   		<table id="attrItemList">
    	<col style="width:10%">
		<col style="width:60%">
		<col style="width:30%">
		<tr>
			<th>
			<input type="checkbox" id="itemCheck" onclick="javascript:exsoft.util.common.allCheckBox('itemCheck','itemIdx');" /></th>
			<th>항목명</th>
			<th class="center">기본여부</th>
		</tr>
    	</table>
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="register_docuType_btn bold" onclick="javascript:exsoftAdminTypeFunc.ui.applyAttrItem();">확인</button>
   		<button type="button" id="" class="cancel_docuType_btn cancel" onclick="javascript:exsoft.util.layout.divLayerClose('register_docuCateType_wrapper','register_docuCateType');">취소</button>
   	</div>
</div>
<!-- 항목 설정 끝 -->
<script type="text/javascript" src="${contextRoot}/js/docadmin/typeManager.js"></script>
<script type="text/javascript">  
jQuery(function() {		
	
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');

	exsoftAdminTypeFunc.init.initPage('#typeGridList','/admin/typeList.do','${pageSize}');
	exsoftAdminTypeFunc.event.fTypeListGrid();	
	exsoft.util.grid.gridResize('typeGridList','targetTypeGrid',20,0);
	exsoft.util.grid.gridResize('attrViewList','targetAttrViewList',20,0);

	// CheckBox Browser Bug Fix
	$('#cb_typeGridList').click(function(e) {
		exsoft.util.grid.checkBox(e);
    });
	
	$('#cb_attrViewList').click(function(e) {
		exsoft.util.grid.checkBox(e);
    });
	
	// 사용되는 레이아웃 POP DEFINE	

	exsoft.util.layout.lyrPopupWindowResize($(".register_docuType"));			
	exsoft.util.layout.lyrPopupWindowResize($(".register_docuCateType"));		
	exsoft.util.layout.lyrPopupWindowResizeArr([$(".register_docuType"),$(".register_docuCateType")]);	              
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".register_docuType"));		
		exsoft.util.layout.lyrPopupWindowResize($(".register_docuCateType"));		
		exsoft.util.layout.lyrPopupWindowResizeArr([$(".register_docuType"),$(".register_docuCateType")]);
	});
	
});
</script>
 