<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	만기문서 관리
 -->
<div class="cnt_wrapper">
	<div class="depth_navigation">${menuInfo.su_menu_nm} &gt; ${menuInfo.menu_nm}</div>
	
	<div class="expired_sub_wrapper">
		<div class="sub_left">
			<div class="srch_form">
				<form id="">
					<table>
						<tr>
							<th>만기일</th>
							<td>
								<input type="text" id="sdate" name="" class="cal" value="" placeholder="">
								~
								<input type="text" id="edate" name="" class="cal" value="" placeholder="">							
							</td>
						</tr>
						<tr>
							<th>제목</th>
							<td>
								<input type="text" id="strKeyword" name="" class="" maxlength="100">
							</td>
						</tr>
						<tr>
							<th>소유자</th>
							<td>
								<input type="text" name="ownerKeyword" id="ownerKeyword" class="" placeholder="소유자를 입력하세요" >
								<button type="button" class="" onclick="javascript:exsoftAdminDocFunc.search.searchFunc();">검색</button>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div class="srch_result_wrapper">
				<div class="expired_btnGrp">
					<button type="button" id="" class="btn1" onclick="javascript:exsoftAdminDocFunc.open.extendPreserve();">보존기간 연장</button>
					<button type="button" id="" class="btn1" onclick="javascript:exsoftAdminDocFunc.event.terminateDocument();">폐기</button>
					
					<div class="menu_imgBtnGrp">
						<a href="javascript:void(0);" class="menu_excel" onclick="javascript:exsoft.util.grid.excelDown('expiredGridList','${contextRoot}/admin/documentList.do')"><img src="${contextRoot}/img/icon/menu_download.png" alt="" title=""></a>
						<a href="javascript:void(0);" class="menu_refresh"  onclick="javascript:exsoft.util.grid.gridRefresh('expiredGridList','${contextRoot}/admin/documentList.do')"><img src="${contextRoot}/img/icon/menu_refresh.png" alt="" title=""></a>
					</div>
				</div>
				<div class="srch_result" id="targetExpiredGrid">
					<table id="expiredGridList"></table>
				</div>
			</div>
			<div class="pg_subWrapper">
				<div class="pg_navi_wrapper">
					<ul class="pg_navi" id="expiredMgrPager"></ul>
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/docadmin/docDetail.jsp" /><!--  만기문서관리/소유권변경/휴지통관리 상세화면 공통 -->
	</div>
</div>
<!-- 보존기간 연장 시작 -->
<div class="extend_preserve_wrapper hide"></div>
<div class="extend_preserve hide">
	<div class="window_title">
		보존기간 연장
        <a href="javascript:void(0);" class="window_close" onclick="javascript:exsoft.util.layout.divLayerClose('extend_preserve_wrapper','extend_preserve');"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="extend_preserve_cnts">
    	<div>
    	<p><span class="cnts_txt">선택한 문서의 보존기간을</span></p>
    	<p>
    		<select id="preservationYear">
     			<option value="1">1년</option>
				<option value="2">2년</option>
				<option value="3" selected>3년</option>
				<option value="5">5년</option>
				<option value="10">10년</option>
				<option value="0">영구</option>
     		</select>
      		<span class="cnts_txt left">연장합니다.</span>
    	</p>
    	</div>    	
    	<div class="btnGrp">
    		<button type="button" id="" class="btn1 bold" onclick="javascript:exsoftAdminDocFunc.event.extensionDocument();">확인</button>
    		<button type="button" id="" class="btn1" onclick="javascript:exsoft.util.layout.divLayerClose('extend_preserve_wrapper','extend_preserve');">취소</button>
    	</div>
    </div>
</div>
<!-- 보존기간 연장 끝 -->
<script type="text/javascript" src="${contextRoot}/js/docadmin/docadmin.js"></script>
<script type="text/javascript">
jQuery(function() {		
		
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	// 만기문서관리
	exsoftAdminDocFunc.init.initPage('#expiredGridList','/admin/documentList.do','${pageSize}','${part}');
	exsoftAdminDocFunc.event.expiredGridList();
	
	exsoft.util.grid.gridResize('expiredGridList','targetExpiredGrid',20,0);
	exsoft.util.grid.gridResize('detail_docHistoryList','targetDocHistoryList',20,0);

	// CheckBox Browser Bug Fix
	$('#cb_expiredGridList').click(function(e) {
		exsoft.util.grid.checkBox(e);
    });
	
	// 사용되는 레이아웃 POP DEFINE
	exsoft.util.layout.lyrPopupWindowResize($(".extend_preserve"));			// 보존기간연장
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".extend_preserve"));		// 보존기간연장
	});
	
	// 보존기간 연장 Layer
    $('.extend_preserve_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.extend_preserve').addClass('hide');
    });
	
});
</script>

