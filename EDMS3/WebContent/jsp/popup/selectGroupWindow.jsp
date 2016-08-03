<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/selectGroupWindow.js"></script>
<!-- 
	부서 선택
 -->
<div class="dept_choose_wrapper hide"></div>
<div class="dept_choose hide">
	<div class="window_title">
		부서 선택
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="">
		<select id="map_id" class="hide" onchange="javascript:selectGroupWindow.event.changeMap();">
			<option value="MYDEPT">부서</option>
			<option value="PROJECT">프로젝트</option>
		</select>
	</div>
    <div class="dept_choose_cnts">
    	<div id="popup_groupTree" class="dept_choose_tree"></div>
    	<div id="popup_projectTree" class="dept_choose_tree hide"></div>
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="deptChoose_btn bold" onclick="javascript:selectGroupWindow.event.selectGroupSubmit();">확인</button>
   		<button type="button" id="" class="deptChoose_cancel_btn cancel" onclick="javascript:selectGroupWindow.close();">취소</button>
   	</div>
</div>
<script type="text/javascript">
jQuery(function() {
	// 부서선택 - 창 닫기
    $('.window_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.dept_choose').addClass('hide');
    	$('.dept_choose_wrapper').addClass('hide');
    });

    // 부서선택 -  창 닫기 : 음영진 부분 클릭 시 닫기
    $('.dept_choose_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.dept_choose').addClass('hide');
    });
});
</script>