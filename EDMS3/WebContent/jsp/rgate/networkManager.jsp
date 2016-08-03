 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	네트워크 접속 허용관리
 -->
 <!-- 
	TODO : 허용IP목록 추가/삭제 버튼 위치 변경 및 허용IP추가 화면 누락
	
-->
 <div class="cnt_wrapper">
	<div class="depth_navigation">
		${menuInfo.su_menu_nm}  &gt; ${menuInfo.menu_nm}
	</div>
	
	<div class="rGate_config_menu">
		<label class="rGate_srch_lbl1">정책유형</label>
		<select id="strIndex">
			<option value="">전체</option>
			<option value="ALL">전사</option>
			<option value="GROUP">그룹명</option>
			<option value="USER">사용자명</option>
		</select>
		
		<label class="rGate_srch_lbl2">부서/사용자<input type="text" id="" name="" class="" value=""></label>
		<button type="button" id="" name="" class="rGate_info_srchBtn btn bold">검색</button>
	</div>
	
	<div class="rGateConfig_sub_wrapper">
		<div class="rGate_config_info">
			<div>
				<button type="button" id="" name="" class="net_add_btn btn1 bold">전사 추가</button>
				<button type="button" id="" name="" class="net_deptUser_btn btn1 bold">부서/사용자 추가</button>
				<button type="button" id="" name="" class="net_modify_btn btn1">정책 수정</button>
				<button type="button" id="" name="" class="delete_net_policy btn1">정책 삭제</button>
				<button type="button" id="" name="" class="regist_net_policy btn1" onclick="javascript:exsoftAdminRgateFunc.open.selectIPList();">허용 IP 관리</button>
			</div>
			<div class="extension_list">jqGrid 영역</div>
		</div>	
	</div>	
	<div class="pg_navi_wrapper">
        <ul class="pg_navi">
              	<li class="first"><a href="#"><img src="${contextRoot}/img/icon/pg_first.png" alt="" title=""></a></li>
                <li class="prev"><a href="#"><img src="${contextRoot}/img/icon/pg_prev.png" alt="" title=""></a></li>
              	<li class="curr"><a href="#">1</a></li>
                  <li><a href="#">2</a></li>
                  <li><a href="#">3</a></li>
                  <li><a href="#">4</a></li>
                  <li><a href="#">5</a></li>
                  <li><a href="#">6</a></li>
                  <li><a href="#">7</a></li>
                  <li><a href="#">8</a></li>
                  <li><a href="#">9</a></li>
                  <li><a href="#">10</a></li>
                  <li class="next"><a href="#"><img src="${contextRoot}/img/icon/pg_next.png" alt="" title=""></a></li>
			<li class="last"><a href="#"><img src="${contextRoot}/img/icon/pg_last.png" alt="" title=""></a></li>
    	</ul>    
	</div>
</div>

<!-- 허용 IP 목록 시작 -->
<div class="rGate_netList_wrapper hide"></div>
<div class="rGate_netList hide">
	<div class="window_title">
		허용 IP 목록
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="rGate_netList_cnts">
    	<div>
    		<button type="button" id="" class="regist_rGate_netList" onclick="javascript:exsoftAdminRgateFunc.open.addIP();">추가</button>
    		<button type="button" id="" class="delete_rGate_netList">삭제</button>
    	</div>
    	<div class="netList_cnts_subWrapper">
    		<table>
     		<colgroup>
     			<col width="38"/>
     			<col width="267"/>
     		</colgroup>
     		<tr>
     			<th><input type="checkbox" id="" name="" class="checkAll" value=""></th>
     			<th>허용 IP 목록</th>
     		</tr>
     		<tr>
    			<td><input type="checkbox" id="" name="" class="" value=""></td>
    			<td>localhost</td>
    		</tr>
    		<tr>
    			<td><input type="checkbox" id="" name="" class="" value=""></td>
    			<td>192.168.0.1</td>
    		</tr>
     	</table>
    	</div>
    </div>
    <div class="btnGrp">
   		 <button type="button" id="" name="" class="btn1 bold" value="">저장</button>
   		 <button type="button" id="" name="" class="btn1" value="">취소</button>
   	</div>
</div>
<!-- 허용 IP 목록 끝 -->
<!-- IP주소 추가 시작 -->
<div class="rGate_registIPAddr_wrapper hide"></div>
<div class="rGate_registIPAddr hide">
	<div class="window_subtitle">
		IP주소 입력
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close1.png" alt="" title=""></a>
    </div>
    <div class="rGate_registIPAddr_cnts">
    	<p>
    		IP주소를 입력해주세요<br>
    		eg)192.168.0.1
    	</p>
    	<div class="registIPAddr_txt">
     	<form>
     		<input type="text" id="" name="" class="" value="" maxlength="3"> .
     		<input type="text" id="" name="" class="" value="" maxlength="3"> .
     		<input type="text" id="" name="" class="" value="" maxlength="3"> .
     		<input type="text" id="" name="" class="" value="">
     	</form>
    	</div>
    </div>
    <div class="btnGrp">
   		 <button type="button" id="" name="" class="btn1 bold" value="">저장</button>
   		 <button type="button" id="" name="" class="btn1" value="">취소</button>
   	</div>
</div>
<!-- IP주소 추가 끝 -->

<script type="text/javascript" src="${contextRoot}/js/rgate/rgate.js"></script>
<script type="text/javascript">
jQuery(function() {	
	// 상단메뉴 선택 변경처리
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	exsoftAdminRgateFunc.init.initPage('${subSelect}');
	
	
	// 사용되는 레이아웃 POP DEFINE
	exsoft.util.layout.lyrPopupWindowResize($(".rGate_netList"));
	exsoft.util.layout.lyrPopupWindowResize($(".rGate_registIPAddr"));	
	exsoft.util.layout.lyrPopupWindowResize($(".rGate_deptUserPolicy"));			// 공통
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".rGate_netList"));
		exsoft.util.layout.lyrPopupWindowResize($(".rGate_registIPAddr"));
		exsoft.util.layout.lyrPopupWindowResize($(".rGate_deptUserPolicy"));		// 공통
	});
	
	// IP목록관리
	$('.rGate_netList_wrapper').bind("click", function(){
	    	$(this).addClass('hide');
	    	$('.rGate_netList').addClass('hide');
	});
	
	// IP추가
	$('.rGate_registIPAddr_wrapper').bind("click", function(){
	    	$(this).addClass('hide');
	    	$('.rGate_registIPAddr').addClass('hide');
	});
	
	// 부서/사용자 정책 설정 공통
    $('.rGate_deptUserPolicy_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.rGate_deptUserPolicy').addClass('hide');
    });
		
});
</script>
<jsp:include page="/jsp/rgate/extCommon.jsp" />