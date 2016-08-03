<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="lnb_menu">
	<jsp:include page="/jsp/layout/userLeftTop.jsp" /><!-- 목록관련 공통적용화면 --> 
	<section class="lnb_tree">
		<div class="statics_tree_menu">
			<div class="tree_menu_list1">
				<span>현황 및 통계</span>
			</div>
		</div>
		<div class="lnb_tree_list tree">
			<div class="statics_mydocs">
				<a href="#" class="bold" onclick="exsoft.util.layout.goUserContent('${contextRoot}/statistics/statisticsMenu.do?type=myStatistics');">내 문서 현황</a>
			</div>
			<div class="statics_menu expand">
				<a href="#" class="bold">통계</a>
			</div>
			<div class="statics_submenu">
				<ul>			 
					<c:choose>               
					<c:when test="${fn:length(menuAuthList) > 0}">
					<c:set var="count" value="${fn:length(menuAuthList) }"></c:set>
						<c:forEach items="${menuAuthList}" var="m" varStatus="menu_cd">	   	
						<c:choose>  
							<c:when test="${staticsMenu == m.su_menu_cd && staticsMenu != m.menu_cd}">
								<li id="${fn:replace(fn:replace(m.link_path,'/statistics/', ''),'.do','')}"><a href="javascript:void(0);" onclick="exsoft.util.layout.goUserContent('${contextRoot}/statistics/statisticsMenu.do?type=${m.menu_cd}&menu_cd=${m.menu_cd}');">${m.menu_nm}</a></li>	
						</c:when>
				 	</c:choose>	
					</c:forEach>	       
            		</c:when>
		 			</c:choose>
				</ul>
			</div>
		</div>
	</section>
</nav>
<script type="text/javascript">
$('.statics_menu').off('click'); // 중복 bind 방지를 위한 코드
$('.statics_menu').on('click', function(e){
	var menu = $(this);
	var subMenu = $('.statics_submenu');

	if(!subMenu.hasClass('hide')){
		subMenu.addClass('hide');
		menu.removeClass('expand').addClass('fold');
	} else {
		subMenu.removeClass('hide');
		menu.removeClass('fold').addClass('expand');
	}
});

// 통계좌측메뉴 제어처리
if("${acl_menu_part}" == "CREATOR")	{
	$(".statics_menu").addClass("hide");
	$(".statics_submenu").addClass("hide");
}else {
	$(".statics_menu").removeClass("hide");
	$(".statics_submenu").removeClass("hide");
}
</script>