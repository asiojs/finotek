<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/selectAclWindow.js"></script>
<!-- 
	Usage :
		권한변경 - 문서등록(수정),폴더등록(수정)
 -->
<!--  문서등록 권한변경 -->
<div class="folder_authModify_wrapper hide"></div>
<div id="pRegDoc" class="doc_folder_authModify hide">
	<div class="doc_authModify_title">
		문서 권한 변경 
		<a href="#" class="doc_authModify_close" onclick="selectAclWindow.close();"><img src="${contextRoot}/img/icon/window_close1.png"></a>
	</div>
	<div class="doc_authModify_cnts">
		<div class="authModify_subTitle">
			<span>권한 목록</span> 
<!-- 			<label><input type="checkbox" id="" name="">기본권한 사용 안함</label> -->
		</div>
		<div class="authModify_applyList">
			<table>
				<tr>
					<th><input type="radio" id="pCurAclId" name="radio_selectAcl">현재 적용 권한</th>
					<td></td>
				</tr>
				<tr>
					<th><input type="radio" id="pInheritAclName" name="radio_selectAcl">상속 권한</th>
					<td></td>
				</tr>
			</table>
		</div>
		<div class="tab_menu">
			<div class="tab_elem_wrapper">
				<span class="tab_element selected">공유권한</span> 
				<span class="tab_element">개인권한</span>
			</div>
			<div class="tab_btn_wrapper">
				<form>
					<label> 권한명 <input type="text" class="srch_txt">
					</label>
					<button type="button">검색</button>
				</form>
			</div>
		</div>
		<div class="tab_form">
			<table class="auth_list" id="selAclWindowDocAclList">
				
			</table>
			<table class="auth_previlege_list" id="selAclWindowDocAclItemList">
				<thead>
   					<tr><th colspan="5">기본 접근자</th></tr>
     				<tr>
      				<th>접근자</th>
      				<th>기본권한</th>
      				<th>문서등록</th>
      				<th>반출취소</th>
      				<th>권한변경</th>
     				</tr>
     			</thead>
     			<tbody></tbody>
			</table>
		</div>
		<div class="tab_form hide">
			<table>
				<tr>
					<th>기본폴더</th>
					<td colspan="3">부서함/경영지원팀/경영일반/규정관리</td>
				</tr>
				<tr>
					<th>문서유형</th>
					<td>일반문서</td>
					<th>보존연한</th>
					<td>5년</td>
				</tr>
				<tr>
					<th>보안등급</th>
					<td>일반</td>
					<th>조회등급</th>
					<td>사원이상</td>
				</tr>
				<tr>
					<th>등록자(소유자)</th>
					<td>홍길동(최영호)</td>
					<th>등록일</th>
					<td>2014.09.15 15:12:23</td>
				</tr>
				<tr>
					<th>수정자</th>
					<td>홍길동</td>
					<th>수정일</th>
					<td>2014.09.15 15:12:23</td>
				</tr>
				<tr>
					<th>다차원 분류</th>
					<td colspan="3">프로젝트/EDMS구축/설계/전산기획팀/경영일반/공통관리</td>
				</tr>
			</table>
		</div>
		<div class="authModify_subTitle">
			<span>추가접근자</span>
		</div>
		<div class="authModify_extApproach">
			<button type="button" onclick="selectAclWindow.event.addAccessor();">추가</button>
			<button type="button" onclick="selectAclWindow.event.removeAccessor();">제외</button>
			<table id="selAclWindowDocExtAclItemList">
			</table>
		</div>

		<div class="doc_authModify_btnGrp">
			<button onclick="selectAclWindow.event.submit();">확인</button>
			<button onclick="selectAclWindow.close();">취소</button>
		</div>
	</div>
</div>

<!--  폴더등록 권한변경 -->
<div class="subFolder_authModify_wrapper hide"></div>
<div id="pRegFolder" class="subFolder_authModify hide">
	<div class="subFolder_authModify_title">
		폴더 권한변경 
		<a href="#" class="subFolder_authModify_close"><img src="${contextRoot}/img/icon/window_close1.png" onclick="selectAclWindow.close();"></a>
	</div>
	<div class="subFolder_authModify_cnts">
		<div class="authModify_btnGrp">
			<button onclick="selectAclWindow.event.createAclPopup();">권한등록</button>
			<button onclick="selectAclWindow.event.modifyAclPopup();">권한수정</button>
			<button onclick="selectAclWindow.event.deleteAclPopup();">권한삭제</button>
			<button onclick="selectAclWindow.event.copyAclPopup();">권한복사</button>
		</div>
		<div class="subFolder_authModify_choose">
			<table>
				<colgroup>
					<col width="127" />
					<col width="518" />
				</colgroup>
				<tr>
					<th>
						<label><input id="pCurAclId" type="radio" name="radio_selectAcl" onclick="selectAclWindow.event.selectAclScope(this);" checked>현재 적용 권한</label>
					</th>
					<td id="pCurAclName"></td>
				</tr>
				<tr>
					<th>
						<label><input id="pInheritAclId" type="radio" name="radio_selectAcl" onclick="selectAclWindow.event.selectAclScope(this);">상속 권한</label>
					</th>
					<td id="pInheritAclName"></td>
				</tr>
			</table>
		</div>

		<div class="tab_menu">
			<div class="tab_elem_wrapper">
				<span class="tab_element selected" name="public" onclick="selectAclWindow.event.changeTab(this);">공유권한</span> 
				<span class="tab_element" name="private" onclick="selectAclWindow.event.changeTab(this);">개인권한</span>
			</div>
			<div class="tab_btn_wrapper">
				<form>
					<label>권한명 <input id="strKeyword2" type="text" class="srch_txt">
					</label>
					<button type="button" onclick="selectAclWindow.event.searchAcl();">검색</button>
				</form>
			</div>
		</div>
		<div class="tab_form">
			<table id="selAclWindowFolderAclList" class="auth_list">
			</table>
			<table id="selAclWindowFolderAclItemList" class="auth_previlege_list">
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
		<div class="tab_form hide">
			<table>
				<tr>
					<th>기본폴더</th>
					<td colspan="3">부서함/경영지원팀/경영일반/규정관리</td>
				</tr>
				<tr>
					<th>문서유형</th>
					<td>일반문서</td>
					<th>보존연한</th>
					<td>5년</td>
				</tr>
				<tr>
					<th>보안등급</th>
					<td>일반</td>
					<th>조회등급</th>
					<td>사원이상</td>
				</tr>
				<tr>
					<th>등록자(소유자)</th>
					<td>홍길동(최영호)</td>
					<th>등록일</th>
					<td>2014.09.15 15:12:23</td>
				</tr>
				<tr>
					<th>수정자</th>
					<td>홍길동</td>
					<th>수정일</th>
					<td>2014.09.15 15:12:23</td>
				</tr>
				<tr>
					<th>다차원 분류</th>
					<td colspan="3">프로젝트/EDMS구축/설계/전산기획팀/경영일반/공통관리</td>
				</tr>
			</table>
		</div>
		<div class="subFolder_authModify_btnGrp">
			<button type="button" onclick="selectAclWindow.event.submit();">저장</button>
			<button type="button" onclick="selectAclWindow.close();">취소</button>
		</div>
	</div>
</div>
