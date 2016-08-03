<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 협업 업무등록 - 사용자 선택 시작 -->
<div class="coopUser_choose_wrapper hide"></div>
<div class="coopUser_choose hide">
 		<div class="coopUser_choose_title">
	사용자 선택
	<a href="javascript:exsoftProcessCoworkWindow.close.layerClose();" class="coopUser_choose_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
</div>
<div class="coopUser_choose_cnts">
	<div class="coopUserChoose_sub_wrapper">
		<div class="coopUser_group">
			<div class="coopUser_group_title">부서 선택</div>
			<div id="coopUser_tree" class="coopUser_tree"></div>
		</div>
		<div class="coopUser_list">
			<div class="coopUser_srch_form">
				<form>
					<label>성명<input type="text" id='coworkUserName' class="filter_coopUser_name"></label>
					<label>부서<input type="text" id='coworkGroupName' class="filter_depart_name"></label>
					<button type="button" class="coopUser_filter_btn" onclick="javascript:exsoftProcessCoworkWindow.event.searchGroupUser();">선택</button>
				</form>
			</div>
			<div class="coopUser_list_result">
				<!-- 사용자목록 -->
				<table id="coopUser_searchUserList" ></table>
			</div>
		</div>
		<div class="selected_coopUser_list">
			<div class="selected_receiver">
				<div class="selected_btnGrp1">
					<a href="javascript:void(0);" onclick='javascript:exsoftProcessCoworkWindow.event.addTableRow("tableAuthor");'>
						<img src="${contextRoot}/img/icon/add_user.png" alt="작성자 추가">
					</a>
					<a href="javascript:void(0);" onclick='javascript:exsoftProcessCoworkWindow.event.delTableRow("tableAuthor","tableAuthorAllChk");'>
						<img src="${contextRoot}/img/icon/sub_user.png" alt="작성자 제외">
					</a>
				</div>
				<div class="selected_cnts">
					<span class="bold">작성자</span>
					<span class="required">*</span>
					<div class="selected_sub_wrapper1">
						<table id='tableAuthor'>
							<thead>
								<tr>
									<th name='user_id'><input type="checkbox" id='tableAuthorAllChk' onclick='javascript:exsoftProcessCoworkWindow.event.selectChkBoxAll("tableAuthorAllChk","tableAuthor_user_id");'></th>
									<th name='group_nm'>부서명</th>
									<th name='user_nm'>사용자명</th>
									<th name='user_id'>사용자 ID</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>						
					</div>
				</div>
			</div>
			<div class="coop_additional_user">
				<div class="selected_btnGrp">
					<a href="javascript:void(0);" onclick='javascript:exsoftProcessCoworkWindow.event.addTableRow("tableCoauthor");'>
						<img src="${contextRoot}/img/icon/add_user.png" alt="작성자 추가">
					</a>
					<a href="javascript:void(0);" onclick='javascript:exsoftProcessCoworkWindow.event.delTableRow("tableCoauthor","tableCoauthorAllChk");'>
						<img src="${contextRoot}/img/icon/sub_user.png" alt="작성자 제외">
					</a>
				</div>
				<div class="selected_cnts">
					<span class="bold">공동 작성자</span>
					<div class="selected_sub_wrapper">
						<table id='tableCoauthor'>
							<thead>
								<tr>
									<th name='user_id'><input type="checkbox" id='tableCoauthorAllChk' onclick='javascript:exsoftProcessCoworkWindow.event.selectChkBoxAll("tableCoauthorAllChk","tableCoauthor_user_id");'></th>
									<th name='group_nm'>부서명</th>
									<th name='user_nm'>사용자명</th>
									<th name='user_id'>사용자 ID</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="coop_approve_user">
				<div class="selected_btnGrp">
					<a href="javascript:void(0);" onclick='javascript:exsoftProcessCoworkWindow.event.addTableRow("tableApprver");'>
						<img src="${contextRoot}/img/icon/add_user.png" alt="승인자 추가">
					</a>
					<a href="javascript:void(0);" onclick='javascript:exsoftProcessCoworkWindow.event.delTableRow("tableApprver","tableApprverAllChk");'>
						<img src="${contextRoot}/img/icon/sub_user.png" alt="승인자 제외">
					</a>
				</div>
				<div class="selected_cnts">
					<span class="bold">승인자</span>
					<span class="required">*</span>
					<div class="selected_sub_wrapper">
						<table id='tableApprver'>
							<thead>
								<tr>
									<th name='user_id'><input type="checkbox" id='tableApprverAllChk' onclick='javascript:exsoftProcessCoworkWindow.event.selectChkBoxAll("tableApprverAllChk","tableApprver_user_id");'></th>
									<th name='group_nm'>부서명</th>
									<th name='user_nm'>사용자명</th>
									<th name='user_id'>사용자 ID</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="coop_receive_user">
				<div class="selected_btnGrp">
					<a href="javascript:void(0);" onclick='javascript:exsoftProcessCoworkWindow.event.addTableRow("tableReceiver");'>
						<img src="${contextRoot}/img/icon/add_user.png" alt="승인자 추가">
					</a>
					<a href="javascript:void(0);" onclick='javascript:exsoftProcessCoworkWindow.event.delTableRow("tableReceiver","tableReceiverAllChk");'>
						<img src="${contextRoot}/img/icon/sub_user.png" alt="승인자 제외">
					</a>
				</div>
				<div class="selected_cnts">
					<span class="bold">수신자</span>
					<div class="selected_sub_wrapper">
						<table id='tableReceiver'>
							<thead>
								<tr>
									<th name='user_id'><input type="checkbox" id='tableReceiverAllChk' onclick='javascript:exsoftProcessCoworkWindow.event.selectChkBoxAll("tableReceiverAllChk","tableReceiver_user_id");'></th>
									<th name='group_nm'>부서명</th>
									<th name='user_nm'>사용자명</th>
									<th name='user_id'>사용자 ID</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="coopUser_choose_btnGrp">
	<button type="button" class="coopUser_chooseConfirm_btn" onclick="javascript:exsoftProcessCoworkWindow.event.submit();">확인</button>
	<button type="reset" class="coopUser_chooseCancel_btn" onclick="javascript:exsoftProcessCoworkWindow.close.layerClose();">취소</button>
</div>
</div>
<!-- 사용자 선택 끝 -->
<!-- script add -->
<script type="text/javascript" src="${contextRoot}/js/process/processCoworkWindow.js"></script> 
<script type="text/javascript">
jQuery(function() {
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".coopUser_choose"));
	});
	
});
</script>