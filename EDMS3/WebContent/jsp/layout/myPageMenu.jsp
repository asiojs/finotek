<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>		
<nav class="lnb_menu">	
	<jsp:include page="/jsp/layout/userLeftTop.jsp" /><!-- 목록관련 공통적용화면 --> 	
	<!-- 나의문서의 경우 -->
	<section class="lnb_tree">
		<div class="my_tree_menu">
			<div class="tree_menu_list1"><span>나의 문서</span></div>
			<div class="tree_menu_more"><ul class="menu_more_list"></ul></div>
		</div>
		<div id="lnb_mydoc_menu">
			<div class="mydoc_menu_list">
				<div class="mydoc_hold" class="selected"><label><a href="javascript:exsoftMypageFunc.init.menuInitPage('OWNER');">내 소유문서</a></label></div>
				<div class="mydoc_edit"><label><a href="javascript:exsoftMypageFunc.init.menuInitPage('CHECKOUT');">내 수정중인 문서</a></label></div>
				<div class="mydoc_expired"><label><a href="javascript:exsoftMypageFunc.init.menuInitPage('EXPIRED');">내 만기 문서</a></label></div>
				<div class="mydoc_bin">
					<label><a href="javascript:exsoftMypageFunc.init.menuInitPage('TRASHCAN');">휴지통</a></label>
					<button type="button" class="" onclick="javascript:exsoftMypageFunc.event.docFunctions.allDocumentDelete();">비우기</button>
				</div>
				<div class="mydoc_favorite">
					<label><a href="javascript:void(0);" >즐겨찾기</a></label>
					<button type="button" onclick="javascript:configFavoriteFolder.init.initWindow();event.stopPropagation();">편집</button>
					<a href="javascript:exsoftMypageFunc.event.treeFunctions.favoriteRefresh();" class="refresh"><img src="${contextRoot}/img/icon/mydoc_list_refresh.png" alt="" title=""></a>
				</div>
				<div id="mydoc_favorite_tree" style="display:none;"></div>
				
				<div class="mydoc_shared_doc"><label><a href="javascript:exsoftMypageFunc.init.menuInitPage('SHARE');">공유받은 문서</a></label></div>
				<div class="mydoc_shared_folder">
					<label>
						<a href="javascript:void(0);" >공유받은 폴더</a>
						<a href="javascript:exsoftMypageFunc.event.treeFunctions.sharedRefresh();" class="refresh"><img src="${contextRoot}/img/icon/mydoc_list_refresh.png" alt="" title=""></a>
					</label>
				</div>
				<div id="mydoc_shared_folderTree" style="display:none;"></div>
				<div class="mydoc_tempbox"><label><a href="javascript:exsoftMypageFunc.init.menuInitPage('TEMPDOC');">작업카트</a></label></div>
				<div class="mydoc_recent"><label><a href="javascript:exsoftMypageFunc.init.menuInitPage('RECENTLYDOC');">최신문서함</a></label></div>
			</div>
		</div>
	</section>
</nav>