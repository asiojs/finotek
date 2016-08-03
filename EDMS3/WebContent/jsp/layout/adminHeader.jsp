<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	TODD - 권한에 따라 메뉴를 변경처리해야한다
 -->
<header class="header">
<div class="header_subWrapper">
	<div class="logo"><h1><a href="javascript:void(0);"><img src="${contextRoot}/img/logo.png"></a></h1></div>
	<div class="header_menu_wrapper">
		<div class="header_icon">
			<ul class="header_icon_menu">
				<li class="main_menu">
					<a href="javascript:void(0);" class="system" id="systemMenu">시스템 관리</a>
					<div class="subMenu_element">
						<ul>
						<c:choose>               
						<c:when test="${fn:length(menuAuthList) > 0}">
							<c:set var="count" value="${fn:length(menuAuthList) }"></c:set>
							<c:forEach items="${menuAuthList}" var="m" varStatus="menu_cd">	   	
								<c:choose>  
								<c:when test="${sysMenu == m.su_menu_cd && sysMenu != m.menu_cd}">
									<li><a href="javascript:void(0);" 
									onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}${m.link_path}?menu_cd=${m.menu_cd}');" id="${fn:replace(fn:replace(m.link_path,'/admin/', ''),'.do','')}">${m.menu_nm}</a></li>
								</c:when>
				 				</c:choose>	
							</c:forEach>	       
            			</c:when>
		 				</c:choose>
						</ul>
					</div>
				</li>
				<li class="main_menu">
					<a href="javascript:void(0);" class="document"  id="documentMenu">문서 관리</a>
					<div class="subMenu_element">
						<ul>
						<c:choose>              
						<c:when test="${fn:length(menuAuthList) > 0}">
							<c:set var="count" value="${fn:length(menuAuthList) }"></c:set>
							<c:forEach items="${menuAuthList}" var="m" varStatus="menu_cd">	   	
							<c:choose>  
								<c:when test="${roleMenu == m.su_menu_cd && roleMenu != m.menu_cd && fn:length(m.link_path) > 0 }">
								<c:choose>
									<c:when test="${m.menu_cd == 'M051' }">
									<c:if test="${m.part == 'ALL' }">
										<li><a href="javascript:void(0);" 
									onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}${m.link_path}?menu_cd=${m.menu_cd}');" id="${fn:replace(fn:replace(m.link_path,'/admin/', ''),'.do','')}">${m.menu_nm}</a></li>
									</c:if>									
									</c:when>
									<c:otherwise>
										<li><a href="javascript:void(0);" 
									onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}${m.link_path}?menu_cd=${m.menu_cd}');" id="${fn:replace(fn:replace(m.link_path,'/admin/', ''),'.do','')}">${m.menu_nm}</a></li>
									</c:otherwise>
								</c:choose>						
								</c:when>
								<c:when test="${docMenu == m.su_menu_cd && docMenu != m.menu_cd}">
									<li><a href="javascript:void(0);" 
									onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}${m.link_path}?menu_cd=${m.menu_cd}');" id="${fn:replace(fn:replace(m.link_path,'/admin/', ''),'.do','')}">${m.menu_nm}</a></li>
								</c:when>				
				 			</c:choose>	
							</c:forEach>	       
            			</c:when>
		 				</c:choose>
						</ul>
					</div>
				</li>
				<li class="main_menu hide">
					<a href="javascript:void(0);" class="rGate" id="rGateMenu">rGate 관리</a>
					<div class="subMenu_element">
						<ul>
							<li><a href="javascript:void(0);" onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}/admin/extManager.do?menu_cd=M030');" id="extManager">저장금지 확장자 관리</a></li>
							<li><a href="javascript:void(0);" id="procManager" onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}/admin/procManager.do?menu_cd=M031');">저장 허용 프로그램 관리</a></li>
							<li><a href="javascript:void(0);" id="controlManager" onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}/admin/controlManager.do?menu_cd=M032');">로컬 저장 허용 관리</a></li>
							<li><a href="javascript:void(0);" id="usbManager" onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}/admin/usbManager.do?menu_cd=M033');">USB 저장 허용 관리</a></li>
							<li><a href="javascript:void(0);" id="networkManager" onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}/admin/networkManager.do?menu_cd=M034');">네트워크 접속 허용관리</a></li>
							<li><a href="javascript:void(0);" id="exceptionManager" onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}/admin/exceptionManager.do?menu_cd=M035');">프로세스 예외폴더 설정</a></li>
							<li><a href="javascript:void(0);" id="uninstallManager" onclick="javascript:exsoft.util.layout.goAdminContent('${contextRoot}/admin/uninstallManager.do?menu_cd=M036');">Client 제거 비밀번호 설정</a></li>
						</ul>
					</div>
				</li>
			</ul>
		</div>
		<div class="header_user">
			<ul class="header_user_menu">
				<li class="user_info"><a href="#"><img src="${contextRoot}/img/icon/user_info.png" alt=""><strong>${user_name}</strong>님 환영합니다.</a></li>
				<li class="user_group"><a href="javascript:void(0);" onclick="javascript:exsoftAdminLayoutFunc.open.passwdConfig('passwd_set_wrapper','passwd_set')"><img src="${contextRoot}/img/icon/admin/pwd.png" alt=""></a></li>
				<c:choose>               			
				<c:when test="${location == 'A'}">
				<li class="user_logout"><a href="javascript:void(0)" onclick="exsoft.util.layout.logout('${contextRoot}/logout.do')"><img src="${contextRoot}/img/icon/user_logout.png" alt=""></a></li>
				</c:when>
				</c:choose>		
			</ul>
		</div>
	</div>
</div>
</header>
<!-- 비밀번호 변경 시작 -->
<div class="passwd_set_wrapper hide"></div>
<div class="passwd_set hide">
<form id="adminPasswd">
	<div class="window_title">
		비밀번호 변경
        <a href="javascript:void(0);" class="window_close" onclick="exsoft.util.layout.divLayerClose('passwd_set_wrapper','passwd_set');"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="passwd_set_cnts">    
	<table>
	<tr>	
		<th>현재 비밀번호</th>
		<td><input type="password" name="current_pass" data-bind="current_pass" ex-valid="require" ex-display="현재 비밀번호" maxlength="10"></td>
	</tr>
	<tr>	
		<th>비밀번호</th>
		<td><input type="password" name="passwd1" data-bind="passwd1" ex-length="4,10" ex-display="새 비밀번호" maxlength="10"></td>
	</tr>
	<tr>	
		<th>비밀번호 확인</th>
		<td><input type="password" name="passwd2" data-bind="passwd2" ex-length="4,10" ex-display="새 비밀번호 확인" ex-equalTo="passwd1,passwd2" maxlength="10"></td>
	</tr>
	</table>    	
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="btn1" onclick="javascript:exsoftAdminLayoutFunc.event.updateConfigProc();">저장</button>
   		<button type="button" class="btn1" onclick="exsoft.util.layout.divLayerClose('passwd_set_wrapper','passwd_set');">취소</button>
   	</div>
</form>   	
</div>
<!-- 비밀번호 변경 끝 -->
<script type="text/javascript">  
jQuery(function() {	
	exsoft.util.layout.lyrPopupWindowResize($(".passwd_set"));			
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".passwd_set"));		
	});
});
</script>