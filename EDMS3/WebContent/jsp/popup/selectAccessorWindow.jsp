<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/selectAccessorWindow.js"></script>
<!-- 
	Usage :
		문서등록 권한 추가 접근자 화면(문서권한)
		권한관리 등록/수정 권한추가 화면(폴더/문서)				 
 -->
<div class="doc_authSet_wrapper hide"></div>
<div class="doc_authSet hide">
	<div class="doc_authSet_title">
		부서/사용자 추가
		<a href="#" class="doc_authSet_close">
			<img src="${contextRoot}/img/icon/window_close.png" onclick="selectAccessorWindow.close();">
		</a>
	</div>
	<div class="doc_authSet_cnts">
		<div class="doc_authSet_srch">
			<input type="text" placeholder="검색어 입력" id="searchAccessorKeyword">
			<button type="button" onclick="selectAccessorWindow.event.searchAccessor();">검색</button>
			<button type="button" onclick="selectAccessorWindow.ui.resetSearchKeyword();">초기화</button>
			
		</div>
		<div class="authSet_sub_wrapper">
			<div class="authSet_choose_folder">
				<div class="authSet_choose_title">폴더 선택</div>
				<div class="authSet_choose_cnts">
					<span class="authSet_choose_classified">구분</span> 
					<select id="selectAccessorWindowMapList">
						<option value="GROUP">부서</option>
						<option value="PROJECT">프로젝트</option>
					</select>
					<div class="authSet_choose_tree" id="accessorWindow_groupTree"></div>
					<div class="authSet_choose_tree hide" id="accessorWindow_projectTree"></div>
					<div class="user_list_wrapper">
						<table class="tbl_choose_list" id="initSearchGroupUser">
						</table>
					</div>
				</div>
			</div>
			<div class="authSet_choose_user">
				<div class="authSet_choose_title">사용자</div>
				<div class="authSet_choose_cnts" id="accessorWindowGroupUserGridTarget">
					<label><input type="checkbox" id="checkAccessorAll">전사</label>
					<div class="user_list_wrapper">
						<table class="tbl_choose_list" id="initTreeGroupUser">
						</table>
					</div>
				</div>
			</div>
			<div class="authSet_choose_proceed">
				<input type="button" value="추가" onclick="selectAccessorWindow.event.addAccessor();"><p>
<!-- 				<input type="button" value="제거"> -->
			</div>
			<div class="authSet_choose_wrapper">
				<div class="authSet_chosen_user">
					<span class="choose_title">선택 대상</span>
					<div class="choose_wrapper">
						<ul class="chosen_user_list" id="accesorWindowSelectedList">
						</ul>
					</div>
				</div>
				
				<div class="authSet_choose_previlege">
					<span class="choose_title">권한 설정</span>
					<div class="choose_wrapper">
						<table class="tbl_authSet">
							<thead>
								<tr>
									<th>구분</th>
									<th>기본권한</th>
									<th>등록</th>
									<th>반출취소</th>
									<th>권한변경</th>
								</tr>
							</thead>
							<tbody>
								<tr id="selectAccessorWindowFolderTr">
									<th>폴더권한</th>
									<td>
										<select id="selectAccessorWindowDefaultFolderAcl">
											<option value="READ" selected="selected">조회</option>
											<option value="UPDATE">수정</option>
											<option value="BROWSE">목록</option>
											<option value="DELETE">삭제</option>
										</select>
									</td>
									<td><input type="checkbox" id="accessorWindowFolActCreate"></td>
									<td>해당없음</td>
									<td><input type="checkbox" id="accessorWindowFolActChangePermission"></td>
								</tr>
								<tr>
									<th>문서권한</th>
									<td>
										<select id="selectAccessorWindowDefaultDocAcl">
											<option value="READ" selected="selected">조회</option>
											<option value="UPDATE">수정</option>
											<option value="BROWSE">목록</option>
											<option value="DELETE">삭제</option>
										</select>
									</td>
									<td><input type="checkbox" id="accessorWindowDocActCreate"></td>
									<td><input type="checkbox" id="accessorWindowDocActCancelCheckout"></td>
									<td><input type="checkbox" id="accessorWindowDocActChangePermission"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="authSet_btnGrp">
			<button type="button" class="authSet_save" onclick="selectAccessorWindow.event.submit();">저장</button>
			<button type="button" class="authSet_cancel" onclick="selectAccessorWindow.close();">취소</button>
		</div>
	</div>
</div>