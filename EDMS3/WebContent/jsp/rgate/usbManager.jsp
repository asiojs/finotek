 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	USB 저장 허용관리
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
				<button type="button" id="" name="" class="usb_add_btn btn1 ">전사 추가</button>
				<button type="button" id="" name="" class="usb_deptUser_btn btn1 " onclick="javascript:exsoftAdminRgateFunc.open.addGroupWrite();">부서/사용자 추가</button>
				<button type="button" id="" name="" class="delete_usb_policy btn1">정책 삭제</button>
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
<script type="text/javascript" src="${contextRoot}/js/rgate/rgate.js"></script>
<script type="text/javascript">
jQuery(function() {	
	// 상단메뉴 선택 변경처리
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	exsoftAdminRgateFunc.init.initPage('${subSelect}');
	
	
	// 사용되는 레이아웃 POP DEFINE
	exsoft.util.layout.lyrPopupWindowResize($(".rGate_deptUserPolicy"));			// 공통
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".rGate_deptUserPolicy"));		// 공통
	});

	// 부서/사용자 정책 설정 공통
    $('.rGate_deptUserPolicy_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.rGate_deptUserPolicy').addClass('hide');
    });
		
});
</script>
<jsp:include page="/jsp/rgate/extCommon.jsp" />