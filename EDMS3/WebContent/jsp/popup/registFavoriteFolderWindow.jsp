<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/registFavoriteFolderWindow.js"></script>
<!-- 
	즐겨찾기 폴더 등록(수정)
 -->
<div class="favorite_register_wrapper hide"></div>
<div class="favorite_register hide">
	<div class="favorite_register_title">
		즐겨찾기 폴더 등록
		<a href="#" class="favorite_register_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
	</div>
	<div class="favorite_register_cnts" id="registFavoriteFolder">
		<div class="">
			<label>
				폴더명
				<input type="text" class="favorite_register_name" id="fav_fol_name" name="" class="">
			</label>
		</div>
		<div class="favorite_btnGrp">
			<button type="button" class="favorite_register_btn" class="" onclick="javascript:favoriteFolderWindow.event.submit();">저장</button>
			<button type="button" class="favorite_cancel_btn" class="" onclick="javascript:favoriteFolderWindow.cancelButtonClick();">취소</button>
		</div>
	</div>
</div>