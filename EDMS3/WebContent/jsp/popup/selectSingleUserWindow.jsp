<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/selectSingleUserWindow.js"></script>
<!-- 
	Usage : 
		- 나의문서 소유권 이전 사용자 선택
		- 관리자 소유권 변경 사용자 선택
		- 권한관리(등록/변경) 사용자 선택
		- 권한등록 사용자 선택
		
 -->
<div class="grant_transfer_wrapper hide"></div>
<div class="grant_transfer hide">
	<div class="grant_transfer_title">
		사용자 선택 
		<a href="#" class="grant_transfer_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
	</div>
	<div class="grant_transfer_cnts">
		<div class="transfer_wrapper">
			<div class="transfer_left">
				<div class="transfer_left_title">부서</div>
				<div class="transfer_left_cnts">
					<div id="transfer_left_tree"></div>
				</div>
			</div>
			<div class="transfer_right">
				<div class="transfer_right_cnts">
					<div class="transfer_srch_form">
						<form>
							<label>성명<input type="text" id="pop_sg_userName" name="" class="" value="" onkeypress="javascript:return selectSingleUserWindow.event.enterKeyPress(event);"></label> 
							<label>부서<input type="text" id="pop_sg_groupName" name="" class="" value="" onkeypress="javascript:return selectSingleUserWindow.event.enterKeyPress(event);"></label>
							<button type="button" id="" onclick="javascript:selectSingleUserWindow.event.searchGroupUser();">검색</button>
						</form>
					</div>
					<!-- 사용자목록 -->
					<table id="pop_searchUserList" ></table>
				</div>
			</div>
		</div>

		<div class="grant_transfer_btnGrp">
			<button type="button" class="grant_confirm_btn" class="" onclick="javascript:selectSingleUserWindow.event.okButtonClick();">확인</button>
			<button type="button" class="grant_cancel_btn" class="" onclick="javascript:selectSingleUserWindow.event.cancelButtonClick();">취소</button>
		</div>
	</div>
</div>