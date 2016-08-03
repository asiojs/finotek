<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	그룹관리
 -->
 <div class="cnt_wrapper">
	<nav class="lnb_menu">
		<div class="lnb_menu_tab">
			<ul>
				<li id="tab_group" class="menu_dept selected"><a href="javascript:void(0);" onclick="javascript:groupManager.init.tabControl('tab_group');">부서</a></li>
				<li id="tab_project" class="menu_project" ><a href="javascript:void(0);" onclick="javascript:groupManager.init.tabControl('tab_project');">프로젝트</a></li>
			</ul>
		</div>
		<div class="lnb_tree_menu">
			<div class="lnb_tree_tab">
				<ul>
					<li><a href="javascript:void(0);" onclick="javascript:groupManager.event.registGroup();">등록</a></li>
					<li><a href="javascript:void(0);" onclick="javascript:groupManager.event.moveGroup();">이동</a></li>
					<li><a href="javascript:void(0);" onclick="javascript:groupManager.event.deleteGroup();">삭제</a></li>
					<li id="upload_btn"><a href="javascript:void(0);" onclick="javascript:excelUploadWindow.event.groupUploadView();">일괄 업로드</a></li>
					<li id="upload_sample"><a href="javascript:void(0);" onclick="javascript:excelUploadWindow.event.groupDownload();">업로드 샘플</a></li>
				</ul>
			</div>
			<div class="lnb_tree_refresh"><a href="javascript:void(0);" class="refresh" onclick="javascript:groupManager.event.refreshTree();"></a></div><!-- TODO REFRESH-->
		</div>
		<div class="lnb_systemTree"><!-- CSS TODO -->
			<div id="groupTree"></div>
			<div id="projectTree" class="hide"></div>
		</div>
    </nav>

	<div class="contents">
		<div class="depth_navigation">${menuInfo.su_menu_nm}  &gt; ${menuInfo.menu_nm}</div>
		<div class="cnts_sub_wrapper">
			<div class="group_info">
				<form id="group_form" name="form_details">
					<input type="hidden" id="group_id" name="group_id" data-bind="group_id" />
					<input type="hidden" id="parent_id" name="parent_id" data-bind="parent_id" />
					<input type="hidden" id="map_id" name="map_id" data-bind="map_id" />
					<input type="hidden" id="user_id_list" name="user_id_list" data-bind="user_id_list" />
					<table>
					<colgroup>
						<col width="108"/>
						<col width="851"/>
					</colgroup>
					<tr>
					<th><span id="group_name_title" class="bold"></span></th>
					<th><button type="button" class="delete_group btn1" onclick="javascript:groupManager.event.deleteGroup();">삭제</button></th>
					</tr>
					<tr>
					<th>부서명<span class="required">*</span></th>
					<td>	<input type="text" id="group_name_ko" name="group_name_ko" data-bind="group_name_ko" maxlength="32" class=""></td>
					</tr>
					<tr>
					<th>부서명(영문)<span class="required">*</span></th>
					<td><input type="text" id="group_name_en" name="group_name_en" data-bind="group_name_en" maxlength="32" class=""></td>
					</tr>
					<tr>
					<th>정렬순서</th>
					<td><input type="text" id="sort_index" name="sort_index" data-bind="sort_index" maxlength="4" onkeydown="return exsoft.util.filter.numInput(event);" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"></td>
					</tr>
					<tr>
					<th>사용여부</th>
					<td>
						<select id="group_status" data-bind="group_status">
							<option value="C">사용</option>
							<option value="D">미사용</option>
						</select>
					</td>
					</tr>
					<tr>
					<th>등록일</th>
					<td><span id="create_date" class="darkgraytext" data-bind="create_date" ></span></td>
					</tr>
					<tr>
					<th>부서경로</th>
					<td><span id="group_full_path" class="darkgraytext" data-bind="group_full_path" ></span></td>
					</tr>
					</table>
				</form>
			</div>
			
			<div class="group_list">
				<table>
				<colgroup>
					<col width="108"/>
					<col width="851"/>
				</colgroup>
				<tr>
				<th><span class="bold">부서원목록</span></th>
				<th>
					<button type="button" id="" name="" class="add_groupList bold btn1" onclick="javascript:groupManager.event.addUser();">추가</button>
					<button type="button" id="" name="" class="remove_groupList btn1" onclick="javascript:groupManager.event.removeUser();">제외</button>
				</th>
				</tr>
				</table>
				
				<div class="cnts_list" id="targetUserList">
					<!-- jqGrid 영역 -->
					<table id="userList" style="height:500px"></table>
                </div>
			</div>
			<div class="btnGrp">
				<button type="button" class="btn1 bold" onclick="javascript:groupManager.event.updateGroup();">저장</button>
				<button type="button" class="btn1" onclick="javascript:groupManager.init.initPage('MYDEPT', '${pageSize}');">취소</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="${contextRoot}/js/sysadmin/groupManager.js"></script>
<script type="text/javascript">
jQuery(function() {	
	// 상단메뉴 선택 변경처리
	exsoftAdminLayoutFunc.init.menuInit();
	exsoftAdminLayoutFunc.init.menuSelected('${topSelect}','${subSelect}');
	
	groupManager.init.initPage('MYDEPT', '${pageSize}');
	
	exsoft.util.grid.gridResize('userList','targetUserList',55);
	exsoft.util.filter.maxNumber(); 

});
</script>

<jsp:include page="/jsp/popup/registGroupWindow.jsp" /><!-- 등록버튼 클릭, 하위부서 추가 -->
<jsp:include page="/jsp/popup/selectGroupWindow.jsp" /><!-- 부서 선택 -->
<jsp:include page="/jsp/popup/excelUploadWindow.jsp" /><!-- 엑셀 일괄 업로드 -->
<jsp:include page="/jsp/popup/selectMultiUserWindow.jsp" /><!-- 구성원추가 -->