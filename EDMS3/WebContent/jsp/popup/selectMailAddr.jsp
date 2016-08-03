<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--
	메일주소 선택
 -->
<div class="user_choose_wrapper hide"></div>
<div class="user_choose hide">
	<div class="user_choose_title">
		사용자 선택 
		<a href="#" class="user_choose_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
	</div>
	<div class="user_choose_cnts">
		<div class="userchoose_sub_wrapper">
			<div class="user_group">
				<div class="user_group_title">그룹 선택</div>
				<div id="user_tree"></div> <!-- 트리영역 -->
			</div>
			<div class="user_list">
				<div class="user_srch_form">
					<form>
						<label>성명<input type="text" id="mail_userName" name="" class="filter_user_name" value="" onkeypress="javascript:return registMail.event.enterKeyPress(event);"></label> 
						<label>부서<input type="text" id="mail_groupName" name="" class="filter_depart_name" value="" onkeypress="javascript:return registMail.event.enterKeyPress(event);"></label>
						<button type="button" id="" name="" class="user_filter_btn" onclick="javascript:registMail.event.searchGroupUser();">검색</button>
					</form>
				</div>
				<div class="user_list_result">
					<table id="mail_user_table"></table>
				</div>
			</div>
			<div class="selected_user_list">
				<div class="selected_receiver">
					<div class="selected_btnGrp">
						<a href="#" class="" onclick="javascript:registMail.event.mailReceiverPlus('받는사람', 'receiver_table');"><img src="${contextRoot}/img/icon/add_user.png" alt="" title="" ></a> 
						<a href="#" class="" onclick="javascript:registMail.event.mailReceiverMinus('받는사람', 'receiver_table');"><img src="${contextRoot}/img/icon/sub_user.png" alt="" title="" ></a>
					</div>
					<div class="selected_cnts">
						<span class="bold">받는사람</span>
						<div class="selected_sub_wrapper">
							<table id="receiver_table">
								<thead>
									<tr>
										<th><input type="checkbox" id="checkAll_receiver_table" onclick="javascript:exsoft.util.common.allCheckBox('checkAll_receiver_table', 'chb_receiver_table');"></th>
										<th>사용자</th>
										<th>메일 ID</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
				<div class="selected_cc">
					<div class="selected_btnGrp">
						<a href="#" class="" onclick="javascript:registMail.event.mailReceiverPlus('참조', 'cc_table');"><img src="${contextRoot}/img/icon/add_user.png" alt="" title="" ></a> 
						<a href="#" class="" onclick="javascript:registMail.event.mailReceiverMinus('참조', 'cc_table');"><img src="${contextRoot}/img/icon/sub_user.png" alt="" title="" ></a>
					</div>
					<div class="selected_cnts">
						<span class="bold">참조</span>
						<div class="selected_sub_wrapper">
							<table id="cc_table">
								<thead>
									<tr>
										<th><input type="checkbox" id="checkAll_cc_table" onclick="javascript:exsoft.util.common.allCheckBox('checkAll_cc_table', 'chb_cc_table');"></th>
										<th>사용자</th>
										<th>메일 ID</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
				<div class="selected_hiddencc">
					<div class="selected_btnGrp">
						<a href="#" class="" onclick="javascript:registMail.event.mailReceiverPlus('숨은참조', 'hcc_table');"><img src="${contextRoot}/img/icon/add_user.png" alt="" title="" ></a> 
						<a href="#" class="" onclick="javascript:registMail.event.mailReceiverMinus('숨은참조', 'hcc_table');"><img src="${contextRoot}/img/icon/sub_user.png" alt="" title="" ></a>
					</div>
					<div class="selected_cnts">
						<span class="bold">숨은참조</span>
						<div class="selected_sub_wrapper">
							<table id="hcc_table">
								<thead>
									<tr>
										<th><input type="checkbox" id="checkAll_hcc_table" onclick="javascript:exsoft.util.common.allCheckBox('checkAll_hcc_table', 'chb_hcc_table');"></th>
										<th>사용자</th>
										<th>메일 ID</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="user_choose_btnGrp">
		<button type="button" class="user_chooseConfirm_btn" onclick="javascript:registMail.event.okButtonClick();">확인</button>
		<button type="button" class="user_chooseCancel_btn" onclick="javascript:registMail.event.selectUserCancelButtonClick();">취소</button>
	</div>
</div>