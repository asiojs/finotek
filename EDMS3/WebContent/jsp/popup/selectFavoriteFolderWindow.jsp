<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/selectFavoriteFolderWindow.js"></script>
<!-- 
	Usage : 즐겨찾기 선택 화면(폴더/문서)
	문서인 경우 하위폴더 즐겨찾기 포함 Hidden 처리한다.
	
 -->
<div class="favorite_choose_wrapper hide"></div>
<div class="favorite_choose hide">
	<div class="favorite_choose_title">
		즐겨찾기 선택 
		<a href="#" class="favorite_choose_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
	</div>
	<div class="favorite_choose_cnts">
		<div id="includeSubFolderDiv" class="include_subfolder">
			<label><input type="checkbox" id="selectFavoriteIncludeSubFolderChk">하위폴더 즐겨찾기 포함</label>
		</div>
		<div id="selectFavoriteTreeDiv" class="favorite_folder_tree"></div>
	</div>
	<div class="favorite_btnGrp">
		<button type="button" class="favorite_confirm_btn" onclick="selectFavoriteFolderWindow.event.submit();">확인</button>
		<button type="button" class="favorite_cancel_btn" onclick="selectFavoriteFolderWindow.close();">취소</button>
	</div>
</div>