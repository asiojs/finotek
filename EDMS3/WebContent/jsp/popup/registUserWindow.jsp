<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/registUserWindow.js"></script>
<!-- 
	사용자 등록 시작 
-->
<div class="user_regist_wrapper hide"></div>
<div class="user_regist hide">
	<div id="popupTitle" class="window_title">
		<!-- 사용자 추가 -->
        <a href="#" class="window_close"><img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
    </div>
    <div class="user_regist_cnts">
    	<form id="pop_user_form" name="form_details">
		<input type="hidden" data-bind="type">
		<input type="hidden" data-bind="group_id">
		<input type="hidden" data-bind="position_nm">
		<input type="hidden" data-bind="manage_group">
		<input type="hidden" id="storage_quota" data-bind="storage_quota">
		
	    	<table>
	    		<colgroup>
	    			<col width="150"/>
	    			<col width="200"/>
	    			<col width="150"/>
	    			<col width="200"/>
	    		</colgroup>
	    		<tr>
					<th>사용자ID</th>
					<td><input id="pop_user_id" name="user_id" data-bind="user_id" type="text" class="grayline" placeholder="사용자 ID를 입력하세요" style="width:160px;" maxlength="32" /></td>
					<th>부서</th>
					<td><input id="pop_group_nm" name="group_nm" data-bind="group_nm" type="text" class="grayline" style="width:160px;" readonly="readonly"></td>
				</tr>
				<tr>
					<th>성명(한글)</th>
					<td><input id="pop_user_name_ko" name="user_name_ko" data-bind="user_name_ko" type="text" class="grayline" placeholder="성명(한글)을 입력하세요" style="width:160px;" maxlength="128"/></td>
					<th>직위</th>
					<td><span class="selectbox"><select id="pop_jobtitle" data-bind="jobtitle" name="jobtitle">
								<c:forEach var="jobtitle" items="${jobtitleList}" begin="0" varStatus="status">
									<option value="${jobtitle.code_id}">${jobtitle.code_nm}</option>
								</c:forEach>
						</select> </span>
					</td>
				</tr>
				<tr>
					<th>성명(영문)</th>
					<td><input id="pop_user_name_en" name="user_name_en" data-bind="user_name_en" type="text" class="grayline" placeholder="성명(영어)을 입력하세요" style="width:160px;" maxlength="128"/></td>
					<th>직급</th>
					<td><span class="selectbox"><select id="pop_position" data-bind="position" name="position">
								<c:forEach var="position" items="${positionList}" begin="0" varStatus="status">
									<option value="${position.code_id}">${position.code_nm}</option>
								</c:forEach>
						</select> </span>
					</td>
				</tr>
				<tr>
					<th>성명(중문)</th>
					<td><input id="pop_user_name_zh" name="user_name_zh" data-bind="user_name_zh" type="text" class="grayline" placeholder="성명(중국)을 입력하세요" style="width:160px;" maxlength="128"/></td>
					<th>역할</th>
					<td><span class="selectbox"><select id="pop_role_id" data-bind="role_id" name="role_id">
								<c:forEach var="role" items="${roleList}" begin="0" varStatus="status">
									<c:choose>
										<c:when test="${role.code_id == 'CREATOR'}">
											<option value="${role.code_id}" selected>${role.code_nm}</option>
										</c:when>
										<c:otherwise>
											<option value="${role.code_id}">${role.code_nm}</option>
										</c:otherwise>
									</c:choose>
									
								</c:forEach>
						</select> </span>
					</td>
				</tr>
				<tr>
					<th>전화번호</th>
					<td><input id="pop_telephone" name="telephone" data-bind="telephone" type="text" class="grayline" name="order" placeholder="전화번호를 입력하세요" style="width:160px;"/></td>
					<th>잠금여부</th>
					<td><span class="selectbox"> <select id="pop_user_status" name="user_status" data-bind="user_status">
								<option value="C">사용</option>
								<option value="D">미사용</option>
						</select>
					</span></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td colspan="3"><input id="pop_email" name="email" data-bind="email" type="text" class="grayline" placeholder="이메일을 입력하세요"  style="width:160px;" maxlength="64"/></td>
				</tr>
				<tr>
					<th>관리부서 선택</th>
					<td colspan="3">
						<input id="pop_manage_group_nm" name="manage_group_text" data-bind="manage_group_text" type="text" class="grayline" placeholder="관리부서" style="width:260px;" disabled/>
						<button type="button" onclick="javascript:registUserWindow.event.selectManageGroup()">
							<span>선택</span>
						</button>
					</td>
				</tr>
				<tr>
				<th>스토리지 할당량</th>
					<td colspan="3">
						<input id="pop_storage_quota" name="storage_quota" type="text" class="numline" style="width:140px;" placeholder="할당량을 숫자로 입력하세요" maxlength="15" onkeydown="return exsoft.util.filter.numInput(event);" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"/>
						<input type="checkbox" id="pop_storage_quota_chk" name="pop_storage_quota_chk" onclick="javascript:registUserWindow.event.userQuotaCheckBox();"> 
						<span style="font-size: 11px; color: #999;">체크 시 용량 제한 없음 (단위 GB)</span>
					</td>
				</tr>
				<tr id="pop_storage_usage_info">
				<th>스토리지 사용량</th>
					<td colspan="3">
						<span id="pop_storage_usage" class="selectbox"></span>
					</td>
				</tr>
	    	</table>
    	</form>
    </div>
   	<div class="btnGrp">
   		<button type="button" id="" class="user_regist_btn bold" onclick="javascript:registUserWindow.event.registUser();">확인</button>
   		<button type="button" id="pop_btn_deleteUser" class="" onclick="javascript:registUserWindow.event.deleteUser();">삭제</button>
   		<button type="button" id="" class="user_regist_btn cancel" onclick="javascript:registUserWindow.close();">취소</button>
   	</div>
</div>
<script type="text/javascript">
jQuery(function() {
	// 사용자 추가 / 수정 - 창 닫기
    $('.window_close').bind("click", function(e){
    	e.preventDefault();
    	$(this).parents('.user_regist').addClass('hide');
    	$('.user_regist_wrapper').addClass('hide');
    });

    // 사용자 추가 / 수정 -  창 닫기 : 음영진 부분 클릭 시 닫기
    $('.user_regist_wrapper').bind("click", function(){
    	$(this).addClass('hide');
    	$('.user_regist').addClass('hide');
    });
	
    exsoft.util.common.ddslick('#pop_jobtitle','srch_type1','jobtitle',140, function(){});
    exsoft.util.common.ddslick('#pop_position','srch_type1','position',140, function(){});
    exsoft.util.common.ddslick('#pop_role_id','srch_type1','role_id',140, function(){});
    exsoft.util.common.ddslick('#pop_user_status','srch_type1','user_status',140, function(){});
});
</script>