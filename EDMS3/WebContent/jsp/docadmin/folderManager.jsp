<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/docadmin/folderManager.js"></script>
<!-- 
	폴더관리
 -->
 <div class="cnt_wrapper">
	<div class="depth_navigation">
		문서관리 &gt; 폴더 관리
	</div>
	
	<div class="folder_sub_wrapper">
		<div class="sub_left">
			<div class="folder_type">
				<span id="classified">구분</span>
				<select id="folder_work_type">
					<option value="WORK_MYDEPT">부서</option>
					<option value="WORK_PROJECT">프로젝트</option>
				</select>
			</div>
			<div class="srch_result_wrapper">
				<div class="folder_btnGrp left">
					<button type="button" id="" name="" class="btn1" onclick="javascript:folderManager.event.registFolder();">등록</button>
					<button type="button" id="" name="" class="btn1" onclick="javascript:folderManager.event.moveFolder();">이동</button>
					<button type="button" id="" name="" class="btn1" onclick="javascript:folderManager.event.deleteFolder();">삭제</button>
					<div class="menu_imgBtnGrp1">
						<a href="javascript:void(0);" class="menu_refresh" onclick="javascript:folderManager.gFolderTree.refresh();"><img src="${contextRoot }/img/icon/menu_refresh.png" alt="" title=""></a>
					</div>
					
				</div>
				<!-- 트리영역 -->
				<div id="myDeptFolderTree" class="folder_tree"></div>
				<div id="projectFolderTree" class="folder_tree hide"></div>
			</div>
		</div>
		
		<div class="sub_right">
			<div class="doc_config_info">
				<form id='form_details' name="form_details">
					<!-- 기본 hidden 값 -->
					<input type="hidden" id="folder_id" data-bind="folder_id" /> 
					<input type="hidden" id="parent_id" data-bind="parent_id" /> 
					<input type="hidden" data-bind="map_id" /> 
					<input type="hidden" id="acl_id" data-bind="acl_id" />
					<input type="hidden" id="folder_type" data-bind="folder_type" />
					<input type="hidden" data-bind="is_inherit_acl" />
					<table>
						<tr>
							<th id="folder_name_title" class="bold">엑스소프트</th>
							<td colspan="3">
								<button type="button" id="" name="" class="register_folder_btn" onclick="javascript:folderManager.event.registFolder();">등록</button>
								<button type="button" id="" name="" class="delete_folder_btn" onclick="javascript:folderManager.event.deleteFolder();">삭제</button>
							</td>
						</tr>
						<tr>
							<th>폴더명</th>
							<td colspan="3"><input type="text" id="folder_name_ko" name="folder_name_ko" data-bind="folder_name_ko" maxlength="32" class="" value=""></td>
						</tr>
						<tr>
							<th>폴더명(영문)</th>
							<td colspan="3"><input type="text" id="folder_name_en" name="folder_name_en" data-bind="folder_name_en" maxlength="32" class="" value=""></td>
						</tr>
						<tr>
							<th>폴더경로</th>
							<td colspan="3" id="folder_full_path">엑스소프트</td>
						</tr>
						<tr>
							<th>문서저장여부</th>
							<td>
								<select id="is_save" name="is_save" data-bind="is_save" data-select='true'>
									<option value="Y">가능</option>
									<option value="N">불가능</option>
								</select>
							</td>
							<th>사용여부</th>
							<td>
								<select id="folder_status" name="folder_status" data-bind="folder_status" data-select='true'>
									<option value="C">사용</option>
									<option value="D">미사용</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>저장문서유형</th>
							<td id="docType_template">
								<select id="is_type" data-bind="is_type" data-select='true'>
									<option value="ALL_TYPE">모든문서</option> <!-- 다이내믹 적용 -->
								</select>
							</td>
							<th>정렬순서</th>
							<td><input type="text" id="sort_index" name="sort_index" data-bind="sort_index" class="" maxlength="4" onkeydown="return exsoft.util.filter.numInput(event);" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"></td>
						</tr>
						<tr>
							<th>스토리지 할당량</th>
							<td>
								<label><input type="checkbox" class="" id="folder_storage_quota_chk" name="storage_quota_chk" class="" onclick="javascript:folderManager.event.folderQuotaCheckBox();">체크시 용량제한 없음</label>
  								<input type="text" id="storage_quota" name="storage_quota" class="folder_storage_limit" 
  								onkeydown="return exsoft.util.filter.numInput(event);" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')">GB
							</td>
							<th>스토리지 사용량</th>
							<td><span id="storage_usage"></span></td>
						</tr>
						<tr>
							<th>권한변경 여부</th>
							<td colspan="3">
								<label><input type="checkbox" id="is_inherit_acl_chk" name="is_inherit_acl_chk" class="">상위 폴더 권한 변경 시 현재 권한 유지</label>
							</td>
						</tr>
						<tr>
							<th>공유폴더 여부</th>
							<td colspan="3">
								<label><input type="checkbox" id="" name="" class="">권한을 부여받은 사용자에게 '공유받은 폴더' 제공</label>
							</td>
						</tr>
						<tr>
							<th>등록일자</th>
							<td><span id="create_date"></span></td>
							<th>등록자</th>
							<td><span id="create_name"></span></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div class="dropDown_wrapper">
				<a class="dropDown_txt" onclick="javascript:exsoft.util.common.showHide('#aclShowHide','#aclView');">
					<label>
						<strong>[<span id="acl_name_title"></span>]권한</strong>
					</label>
				</a>
				<span class="dropDown_arrow down" id="aclShowHide"></span>
				<div class="dropDown_subWrapper hide" id="aclView">
					<div>
						<button type="button" id="" name="" class="acl_change" onclick="javascript:folderManager.event.changeAcl();">권한변경</button>
						<label><input type="checkbox" id="" name="is_acl_batch" class=""> 하위 폴더/문서 권한 일괄변경</label>
					</div>
					<div class="auth_tbl_wrapper">
	       				<table id="aclDocTable">
	       					<thead>
	       						<tr>
		         				<th rowspan="2">접근자</th>
		         				<th colspan="3">폴더권한</th>
		         				<th colspan="4">문서권한</th>
		         				</tr>
		         				<tr>
			         				<th>기본권한</th>
			         				<th>폴더등록</th>
			         				<th>권한변경</th>
			         				<th>기본권한</th>
			         				<th>문서등록</th>
			         				<th>반출취소</th>
			         				<th>권한변경</th>
		         				</tr>
	       					</thead>
		       				<tbody></tbody>
	       				</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="btnGrp">
		<button type="button" class="btn1 bold" onclick="javascript:folderManager.event.submitUpdate();">저장</button>
		<button type="button" class="btn1" onclick="javascript:folderManager.event.cancelUpdate();">취소</button>
	</div>
</div>

<!-- 폴더 이동 관련 form -->
<form id='form_move' name="form_move">
	<!-- 기본 hidden 값 -->
	<input type="hidden" id="move_action" name='type' value='MOVE' />
	<input type="hidden" id="move_folder_name_ko" name="folder_name_ko">
	<input type="hidden" id="move_folder_id" name='folder_id' /> 
	<input type="hidden" id="move_parent_id" name='parent_id' /> 
	<input type="hidden" id="move_map_id" name='map_id' /> 
	<input type="hidden" id="parentGroup_id" name='parentGroup_id' />
	<input type="hidden" id="root_folder_change" name='root_folder_change' />
</form>
<script type="text/javascript">
jQuery(function() {		
		
	exsoft.util.filter.maxNumber();			// maxlength
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	exsoft.util.common.ddslick('#is_save','srch_type1','is_save',70, function(){});
	exsoft.util.common.ddslick('#folder_status','srch_type1','folder_status',70, function(){});
	exsoft.util.common.ddslick('#folder_work_type', 'srch_type1', '', 90, function(divId,selectedData){
		// 콤보박스 이벤트 
		folderManager.event.changeWorkType(selectedData.selectedData.value);
	});
	
});
</script>

<jsp:include page="/jsp/popup/registFolderWindow.jsp"/> <!-- 폴더등록 -->
<jsp:include page="/jsp/popup/selectSingleFolderWindow.jsp"/> <!-- 폴더선택 -->
<jsp:include page="/jsp/popup/selectAclWindow.jsp"/> <!-- 권한변경 -->
<jsp:include page="/jsp/popup/registAclWindow.jsp"/> <!-- 권한변경시 등록창 -->
<jsp:include page="/jsp/popup/selectAccessorWindow.jsp"/> <!-- 권한 등록시 접근자 추가창 -->