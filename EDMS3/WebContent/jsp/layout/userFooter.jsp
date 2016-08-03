<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<footer class="footer">
	<ul class="footer_menu">
		<li class="ft_lyr_tooltip"><a href="javascript:void(0);">도움말</a></li>
		<li class="ft_lyr_tooltip"><a href="javascript:void(0);">프로그램설치</a></li>
		<c:if test="${role_id != 'CREATOR' }">
		<li class="ft_lyr_tooltip"><a href="javascript:void(0);" onclick="javascript:exsoftLayoutFunc.open.adminUrl('${role_id}');">시스템관리</a></li>
		</c:if>
		<li class="ft_lyr_tooltip"><a href="javascript:exsoftLayoutFunc.event.quickMenuConfig('quickMenu_wrapper','quickMenu');">퀵메뉴추가</a></li>
	</ul>
	<p class="copyright">Copyright eXsoft All Rights Reserved.</p>
</footer>

<!-- Ajax Loading 메세지 -->
<div class="loading_wrapper hide"></div>
<div class="loading hide">
	<div class="loading_cnts">
    	<img src="${contextRoot}/img/icon/ajax-loader.gif" alt="" title="">
		<p><strong>데이터 로딩중입니다.</strong><br>잠시만 기다려 주세요</p>
	</div>
</div>

   	<!-- 퀵메뉴 추가 시작 -->
<div class="quickMenu_wrapper hide"></div>
<div class="quickMenu hide">
   		<div class="quickMenu_title">
   			퀵메뉴 추가
            <a href="#" class="quickMenu_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
        </div>
        <div class="quickMenu_cnts">
        	<p class="right">퀵메뉴는 최대 5개까지 선택가능합니다.</p>
        	<div class="quickMenu_sub_wrapper"></div>
	        <div class="quickMenu_btnGrp">
	        	<button type="button" class="quickMenu_reset" onclick="javascript:exsoftLayoutFunc.init.quickMenuInit();">초기화</button>
	        	<button type="button" class="quickMenu_save" onclick="javascript:exsoftLayoutFunc.event.quickMenuUpdate();">저장</button>
	        	<button type="button" class="quickMenu_cancel" onclick="exsoft.util.layout.divLayerClose('quickMenu_wrapper', 'quickMenu');">취소</button>
	        </div>
        </div>
</div>
<!-- 퀵메뉴 추가 끝 -->
