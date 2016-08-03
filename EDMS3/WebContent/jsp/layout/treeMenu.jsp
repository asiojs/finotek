<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--  
	// 개인문서함/업무문서함 좌측메뉴
 -->
<nav class="lnb_menu">
	<jsp:include page="/jsp/layout/userLeftTop.jsp" /><!-- 목록관련 공통적용화면 --> 
	<section class="lnb_tree">
		<div class="tree_menu">
			<ul class="tree_menu_list">
				<li><a href="#" data-group="treeTabWorkspace" data-id="mydeptTree" class="focus">부서</a></li>
				<li><a href="#" data-group="treeTabWorkspace" data-id="alldeptTree">전사</a></li>
				<li><a href="#" data-group="treeTabWorkspace" data-id="projectTree">프로젝트</a></li>
				<li><a href="#" data-group="treeTabMypage" data-id="mypageTree" class="hide">개인 문서함</a></li>
			</ul>
			<div class="tree_menu_more">
				<ul class="menu_more_list">
	            	<li><a href="#"><img src="${contextRoot}/img/icon/tree_more.png"></a></li>
	                <li><a href="#" id="treeRefresh"><img src="${contextRoot}/img/icon/tree_refresh.png"></a></li>
				</ul>
			</div>
		</div>
		<div id="lnb_tree_list" class="hide"></div>
		<div id="mydeptTree"  data-group="workspaceTree"></div>
		<div id="alldeptTree" data-group="workspaceTree" class="hide"></div>
		<div id="projectTree" data-group="workspaceTree" class="hide"></div>
		<div id="mypageTree" data-group="mypageTree" class="hide"></div>
	</section>
</nav>