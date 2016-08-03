<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/registAclWindow.js"></script>
<!-- 
	Usage :
		권한등록 메인
 -->
<div class="subFolder_authModifyCopy_wrapper hide"></div>
<div class="subFolder_authModifyCopy hide">
	<div class="subFolder_authModifyCopy_title">
		<label id="registAclWindowTitle">[권한] 등록</label>
		<a href="#" class="subFolder_authModifyCopy_close" onclick="registAclWindow.close();">
			<img src="${contextRoot}/img/icon/window_close1.png">
		</a>
	</div>
	<div class="subFolder_authModifyCopy_cnts">
		<form id="">
			<div class="authModifyCopy_info">
				<table>
					<colgroup>
						<col width="78" />
						<col width="570" />
					</colgroup>
					<tr>
						<th>권한명</th>
						<td><input type="text" class="auth_name" id="registAclWindowAclName" placeholder="권한명을 입력하세요."></td>
					</tr>
					<tr>
						<th rowspan="2" class="top">공유범위</th>
						<td>
							<label><input type="radio" name="registAclWindowOpenRange" value="PRIVATE"	onclick="registAclWindow.event.selectOpenRange(this);" checked>공유안함</label> 
							<label><input type="radio" name="registAclWindowOpenRange" value="TEAM"		onclick="registAclWindow.event.selectOpenRange(this);">부서</label> 
							<label><input type="radio" name="registAclWindowOpenRange" value="DEPT"		onclick="registAclWindow.event.selectOpenRange(this);">하위부서포함</label> 
							<label><input type="radio" name="registAclWindowOpenRange" value="ALL"		onclick="registAclWindow.event.selectOpenRange(this);">전사</label>
						</td>
					</tr>
					<tr>
						<td>
							<input type="text" class="auth_share_target" id="registAclWindowOpenName" readonly="readonly">
							<button type="button" class="auth_share_find" id="registAclWindowSearch" onclick="registAclWindow.event.selectShareTargetWindow();">찾기</button>
						</td>
					</tr>
				</table>
			</div>
			<div class="authModifyCopy_infoData">
				<div class="authModifyCopy_infoData_btnGrp">
					<button type="button" class="authModifyCopy_info_add" onclick="registAclWindow.event.addAclItemRow();">추가</button>
					<button type="button" class="authModifyCopy_info_sub" onclick="registAclWindow.event.deleteAclItemRow();">제외</button>
				</div>
				<table id="registAclItemGridList">
				</table>
			</div>
		</form>
	</div>
	<div class="authModifyCopy_btnGrp">
		<button type="button" class="authModifyCopy_save" 	onclick="registAclWindow.event.submit();">저장</button>
		<button type="button" class="authModifyCopy_cancel" onclick="registAclWindow.event.cancel();">취소</button>
	</div>
</div>