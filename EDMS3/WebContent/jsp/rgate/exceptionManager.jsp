 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	프로세스 예외폴더 설정
 -->
  <div class="cnt_wrapper">
	<div class="depth_navigation">
		${menuInfo.su_menu_nm}  &gt; ${menuInfo.menu_nm}
	</div>	
	<div class="rGateConfig_sub_wrapper">
		<div class="rGate_config_info">
			<div class="config_info_btnGrp">
				<button type="button" id="" name="" class="appFolder_add_btn btn1 bold" onclick="javascript:exsoftAdminRgateFunc.open.selectExceptProcManager();">추가</button>
				<button type="button" id="" name="" class="delete_appFolder_btn btn1" onclick="javascript:exsoftAdminRgateFunc.open.addGroupWrite();" >삭제</button>
				
				<div class="config_info_tooltip">
					* 작업폴더가 여러개일 경우 ';' 으로 구분해주세요!
				</div>
			</div>			
			<div class="extension_list">jqGrid 영역</div>			
		</div>	
	</div>	
	<div class="btnGrp">
    	<button type="button" id="" class="appFolder_save_btn btn1 bold">저장</button>
        <button type="button" id="" class="appFolder_cancel_btn btn1 cancel">취소</button>
	</div>
</div>

<!-- 프로그램 선택 시작 -->
<div class="rGate_application_wrapper hide"></div>
	   	<div class="rGate_application hide">
	   		<div class="window_title">
	   			프로그램 선택
	            <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
	        </div>
	        <div class="rGate_application_cnts">
	        	<table>
	        		<colgroup>
	        			<col width="38"/>
	        			<col width="267"/>
	        		</colgroup>
	        		<tr>
	        			<th><input type="checkbox" id="" name="" class="checkAll" value=""></th>
	        			<th>프로그램 선택</th>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>excel.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>notepad.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>calc.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>powerpoint.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>format.com</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>chrome.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>hwp.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>wordpad.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>photoshop.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>mspaint.exe</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>services.msc</td>
	        		</tr>
	        		<tr>
	        			<td><input type="checkbox" id="" name="" class="" value=""></td>
	        			<td>msconfig</td>
	        		</tr>
	        	</table>
	        </div>
	        <div class="btnGrp">
        		 <button type="button" id="" name="" class="btn1 bold" value="">확인</button>
        		 <button type="button" id="" name="" class="btn1" value="">취소</button>
        	</div>
	    </div>
	   	
<script type="text/javascript" src="${contextRoot}/js/rgate/rgate.js"></script>
<script type="text/javascript">
jQuery(function() {	
	// 상단메뉴 선택 변경처리
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	exsoftAdminRgateFunc.init.initPage('${subSelect}');
	
	
	// 사용되는 레이아웃 POP DEFINE
	exsoft.util.layout.lyrPopupWindowResize($(".rGate_application"));
	exsoft.util.layout.lyrPopupWindowResize($(".rGate_deptUserPolicy"));			// 공통
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".rGate_application"));
		exsoft.util.layout.lyrPopupWindowResize($(".rGate_deptUserPolicy"));		// 공통
	});
	
	// 프로그램 선택화면
	$('.rGate_application_wrapper').bind("click", function(){
	    $(this).addClass('hide');
	    $('.rGate_application').addClass('hide');
	});
	
	// 부서/사용자 정책 설정 공통
    $('.rGate_deptUserPolicy_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.rGate_deptUserPolicy').addClass('hide');
    });
		
});
</script>
<jsp:include page="/jsp/rgate/extCommon.jsp" />