<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	소유권변경
	소유권이전Layer 포함됨
	사용자선택 - selectSingleUserWindow.jsp
 -->
<div class="cnt_wrapper">
<div class="depth_navigation">${menuInfo.su_menu_nm} &gt; ${menuInfo.menu_nm}</div>

<div class="transfer_sub_wrapper">
	<div class="sub_left">
		<div class="srch_form">
			<form id="">
				<table>					
					<tr>
						<th>검색대상</th>
						<td>
							<label><input type="radio" value="MYWORK" name="search" checked onclick="javascript:exsoftAdminDocFunc.ui.checkBoxChanged('MYWORK',event);">개인문서함</label>
							<label class="mar_left_10px"><input type="radio" value="WORKSPACE" name="search" onclick="javascript:exsoftAdminDocFunc.ui.checkBoxChanged('WORKSPACE',event);">업무문서함</label>
						</td>
					</tr>
					<tr>
						<th>소유자</th>
						<td>
							<input type="text" id="user_name" class="readonly" maxlength="20" readonly="readonly">
							<input type="hidden" id="user_id"/>
							<button type="button" id="" class="" onclick="javascript:selectSingleUserWindow.init.initSingleUserWindow(exsoftAdminDocFunc.callback.returnSelectUser);">선택</button>
						</td>
					</tr>
					<tr>
						<th>등록일</th>
						<td>							
							<input type="text" id="sdate" name="" class="cal" value="" placeholder="">							
							~							
							<input type="text" id="edate" name="" class="cal" value="" placeholder="">
						</td>
					</tr>
					<tr>
						<th>제목</th>
						<td>
							<input type="text" id="doc_name" name="" class="" placeholder="제목을 입력하세요" maxlength="50">
							<button type="button" id="" class="" onClick="javascript:exsoftAdminDocFunc.search.searchDocList();">검색</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="srch_result_wrapper">
			<div class="transfer_btnGrp">
				<button type="button" id="" class="btn2" onclick="javascript:exsoftAdminDocFunc.open.changeOwner();">소유권 변경</button>
				
				<div class="menu_imgBtnGrp1">
					<a href="javascript:void(0);" class="menu_refresh" onclick='javascript:exsoft.util.grid.gridRefresh("ownerDocGrid","${contextRoot}/admin/documentList.do");'><img src="${contextRoot}/img/icon/menu_refresh.png" alt="" title=""></a>
				</div>
			</div>
			<!-- Refresh 버튼 -->
			<div class="srch_result"  id="ownerDocGridTarget">
				<table id="ownerDocGrid"></table>
			</div>
		</div>
		<!--  PAGER 위치 -->
		<div class="pg_subWrapper">
			<div class="pg_navi_wrapper">
				<ul class="pg_navi" id=""></ul>
			</div>
		</div>
	</div>
	<jsp:include page="/jsp/docadmin/docDetail.jsp" /><!--  만기문서관리/소유권변경/휴지통관리 상세화면 공통 -->
	</div>
</div>
<!-- 소유권 이전 시작 -->
<div class="transfer_owner_wrapper hide"></div>
<div class="transfer_owner hide">
	<div class="window_title">
		소유권 이전
        <a href="javascript:void(0);" class="window_close" onclick="javascript:exsoft.util.layout.divLayerClose('transfer_owner_wrapper','transfer_owner');"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="transfer_owner_cnts">
    <form name="frm" id="frm">
		<input type="hidden" name="type" id="type"> 
		<input type="hidden" name="gcode_id" id="gcode_id" value="ROLE">
		<table>
			<tr>
				<th>현소유자</th>
				<td><label id="currentOwnerName"></label></td>
			</tr>
			<tr>
				<th>인수인계 범위</th>
				<td>
					<select  id="changeType">
						<option value="SELECT_DOC">선택한 문서</option>
						<option value="SEARCH_DOC">검색 결과 문서</option>
						<option value="ALL_DOC">인계자의 모든 문서</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>인수자</th>
				<td>
					<input type="hidden" id="targetUserId" />
					<input type="text" id="targetUserName" class="readonly" readonly="readonly" >
					<button type="button" id="" class="btn2" onclick="javascript:selectSingleUserWindow.init.initSingleUserWindow(exsoftAdminDocFunc.callback.returnTargetUser);">선택</button>
				</td>
			</tr>
		</table>
		<div class="confirm_txt">
			해당 문서를 인수인계하시겠습니까?
		</div>
  	</form>		
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="btn2 bold"  onclick="javascript:exsoftAdminDocFunc.ui.changeOwnership();">확인</button>
   		<button type="button" id="" class="btn2 cancel" onclick="javascript:exsoft.util.layout.divLayerClose('transfer_owner_wrapper','transfer_owner');">취소</button>
   	</div>
</div>
<jsp:include page="/jsp/popup/selectSingleUserWindow.jsp" />	<!-- 소유권변경 - 사용자 선택 -->
<!-- 소유권 이전 끝 -->
<script type="text/javascript" src="${contextRoot}/js/docadmin/docadmin.js"></script>
<script type="text/javascript">
jQuery(function() {				
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	exsoftAdminDocFunc.init.initPage('#ownerDocGrid','/admin/documentList.do','${pageSize}','${part}');
	exsoftAdminDocFunc.event.ownerGrid();
	
	exsoft.util.grid.gridResize('ownerDocGrid','ownerDocGridTarget',20,0);
	exsoft.util.grid.gridResize('detail_docHistoryList','targetDocHistoryList',20,0);
	
	
	// CheckBox Browser Bug Fix
	$('#cb_ownerDocGrid').click(function(e) {
		exsoft.util.grid.checkBox(e);
    });
	
	// 사용되는 레이아웃 POP DEFINE	
	exsoft.util.layout.lyrPopupWindowResize($(".transfer_owner"));			
	exsoft.util.layout.lyrPopupWindowResize($(".grant_transfer"));			
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".transfer_owner"));		
		exsoft.util.layout.lyrPopupWindowResize($(".grant_transfer"));		
	});
	
	// 소유권연장 Layer
    $('.transfer_owner_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.transfer_owner').addClass('hide');
    });
});
</script>