<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="cnt_wrapper" id="myDocListBody">
	<jsp:include page="/jsp/layout/treeMenu.jsp" />	  
	<div class="contents">
		<div class="cnts_srch_wrapper">
			<div class="toggle_tree_menu"></div>
			<div class="cnts_srch_detail">
				<div class="cnts_srch">
					<select id="myDoc_select">
						<option value="doc_name">제목</option>
						<option value="doc_description">내용</option>
						<option value="creator_name">등록자</option>
					</select> <input type="text"class="srch_keyword" id="strKeyword1" name="srch_keyword" placeholder="검색어를 입력하세요">
					<button type="button" class="srch_btn" onclick="myDocList.event.searchDocument();"></button>
				</div>
				<jsp:include page="/jsp/popup/docDetailSearch.jsp" /><!-- 상세검색 Layer 공통영역으로 분리 -->
				<div id="myDocListNoty"></div>
			</div>
			<div class="depth_navi">
				<span>
					<strong id="nav_title"></strong>
					<span id="nav_fullpath" class="depth_navi_path hide">
					</span>
				</span>
			</div>
		</div>
		<div class="cnts_tbl_menu">
			<div class="tbl_menu_left">
				<button type="button" class="tbl_reg" onclick="exsoft.document.layer.docWriteCommonFrm('doc_register_wrapper','doc_register');">등록</button>
				<button type="button" class="tbl_del" onclick="documentListLayerWindow.event.documentDelete();">삭제</button>
				<div class="tbl_extFunction">
					<a class="dropDown_txt">
						<label>추가기능</label>
						<span class="dropDown_arrow down"></span>
					</a>
					<div class="extFunction_dropDown_wrapper hide">
						<ul class="extFunction_dropDown_menu">
							<li><a href="#" class="move" onclick="documentListLayerWindow.event.documentMove();">이동</a></li>
							<li><a href="#" class="copy" onclick="documentListLayerWindow.event.documentCopy();">복사</a></li>
							<li><a href="#" class="favorite" onclick="documentListLayerWindow.event.documentAddFavorite();">즐겨찾기 추가</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="tbl_menu_right">
				<ul class="tbl_thumbMenu">
					<li class="excel_download"><a href="#" class="menu"></a></li>
					<li class="layout_view"><a href="#" class="menu"></a>
						<div class="layout_view_dropDown hide">
							<ul>
								<li id="list_only"><a href="#">메일 목록만 보기</a></li>
								<li id="horizontal_divide"><a href="#">좌우 분할로 보기</a></li>
								<li id="vertical_divide"><a href="#">상하 분할로 보기</a></li>
							</ul>
						</div>
					</li>
					<li class="menu_refresh"><a href="#" class="menu"></a></li>
				</ul>
				<select id="myDocListRowCount">
					<option value="5">5개</option>
					<option value="10" selected>10개</option>
                    <option value="15">15개</option>
                    <option value="20">20개</option>
                    <option value="30">30개</option>
                    <option value="50">50개</option>
				</select>
			</div>
		</div>
		<div class="cnts_tbl_wrapper" id="myDocListTarget">
			<div class="cnts_list"><table id="myDocList"></table></div>
		</div>

		<div class="cnts_aside hide">
			<div class="horizontal_draggable"></div>
			<div class="aside_cnts_wrapper">
				<div class="cnts_sub_wrapper">
					<div class="aside_title">
                 			다음 설정을 통하여 편리하게 문서를 관리해보세요.
                  			<span class="regDate">2010-05-31</span>
                   	</div>
                   	<div class="aside_cnts">
                   		<p>
                   			- 분할모드 첫화면 첫번째 문서 자동 보기 설정을 할 수 있습니다.
                   			<a href="#" class="config_view">환경설정 보기</a>
                   		</p>
                   		<p>
                   			- 문서 삭제/이동 후 자동으로 다음/이전 문서로 이동할 수 있습니다.
                   			<a href="#" class="config_view">환경설정 보기</a>
                   		</p> 
                   		<table>
	                   		<tr>
	                   			<th>샘플</th>
	                   			<td>샘플</td>
	                   		</tr>
                   		</table>
                   	</div>
                   	<!-- preview document -->
                   	<div class="tab_form">
                   		<strong><label data-bind="doc_name"></label></strong><br>
	                   	<div class="doc_detail_attach hide">
							<a class="dropDown_txt">
								<label>
									<strong>첨부파일</strong>
									<span class="attach_cnt" data-bind="previewAttachFileCount"></span>
									<span class="attach_size"></span>
								</label>
							</a>
							<button class="download_all">모두 저장</button>
							<span class="dropDown_arrow down"></span>
							<div class="hide">
								<div class="attach_docs_wrapper" id="previewPageListHorizon">
								</div>
							</div>
						</div>
	                   	<div class="doc_detail_info hide">
							<a class="dropDown_txt">
								<strong><label>문서 정보</label></strong>
							</a>
							<span class="dropDown_arrow down"></span>
							<div class="tab_form hide">
								<table>
									<tr>
										<th>기본폴더</th>
										<td colspan="3"><label data-bind="folderPath"></label></td>
									</tr>
									<tr>
										<th>문서유형</th>
										<td data-bind="doc_type"></td>
										<th>보존연한</th>
										<td><label data-bind="preservation_year"></label></td>
									</tr>
									<tr>
										<th>보안등급</th>
										<td><label data-bind="security_level"></label></td>
										<th>조회등급</th>
										<td><label data-bind="access_grade"></label></td>
									</tr>
									<tr>
										<th>등록자(소유자)</th>
										<td><label data-bind="creator_name"></label></td>
										<th>등록일</th>
										<td><label data-bind="create_date"></label></td>
									</tr>
									<tr>
										<th>수정자</th>
										<td><label data-bind="updater_name"></label></td>
										<th>수정일</th>
										<td><label data-bind="update_date"></label></td>
									</tr>
									<tr>
										<th>다차원 분류</th>
										<td colspan="3"><label data-bind="multiFolderList"></label></td>
									</tr>
									<tr>
										<th>키워드</th>
										<td colspan="3"><label data-bind="keyword"></label></td>
									</tr>
									<tr>
										<th>설명</th>
										<td colspan="3"><label data-bind="doc_description"></label></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="doc_detail_auth hide">
							<a class="dropDown_txt">
								<strong>권한 : </strong><label data-bind="aclName"></label>
								<span class="dropDown_arrow down"></span>
							</a>
							<div class="doc_auth_cnts hide" >
								<table id="detail_docAclItemListHorizon">
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
								<table id="detail_docExAclItemListHorizon">
									<thead>
				       					<tr><th colspan="5">추가 접근자</th></tr>
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
                   		</div>
                   	</div>
				</div>
			</div>
		</div>
		<div class="cnts_bottom hide">
			<div class="vertical_draggable"></div>
			<div class="bottom_cnts_wrapper">
				<div class="bottom_title">
					다음 설정을 통하여 편리하게 문서를 관리해보세요.
					<span class="regDate">2010-05-31</span>
				</div>
				<div class="bottom_cnts">
					<p>
						- 분할모드 첫화면 첫번째 문서 자동 보기 설정을 할 수 있습니다.
						<a href="#" class="config_view">환경설정 보기</a>
					</p>
                	<p>
                		- 문서 삭제/이동 후 자동으로 다음/이전 문서로 이동할 수 있습니다.
                		<a href="#" class="config_view">환경설정 보기</a>
                	</p> 
                	<table>
                	<tr>
               			<th>샘플</th>
               			<td>샘플</td>
               		</tr>
               		</table>
               	</div>
				<!-- preview document -->
				<div class="tab_form">
					<strong><label data-bind="doc_name"></label></strong><br>				
					<div class="doc_detail_attach hide">
						<a class="dropDown_txt">
							<label>
								<strong>첨부파일</strong>
								<span class="attach_cnt" data-bind="previewAttachFileCount"></span>
								<span class="attach_size"></span>
							</label>
						</a>
						<button class="download_all">모두 저장</button>
						<span class="dropDown_arrow down"></span>
						<div class="hide">
							<div class="attach_docs_wrapper" id="previewPageListVertical">
							</div>
						</div>
					</div>
					<div class="doc_detail_info hide">
						<a class="dropDown_txt">
							<strong><label>문서 정보</label></strong>
						</a>
						<span class="dropDown_arrow down"></span>
						<div class="tab_form hide">
							<table>
								<tr>
									<th>기본폴더</th>
									<td colspan="3"><label data-bind="folderPath"></label></td>
								</tr>
								<tr>
									<th>문서유형</th>
									<td data-bind="doc_type"></td>
									<th>보존연한</th>
									<td><label data-bind="preservation_year"></label></td>
								</tr>
								<tr>
									<th>보안등급</th>
									<td><label data-bind="security_level"></label></td>
									<th>조회등급</th>
									<td><label data-bind="access_grade"></label></td>
								</tr>
								<tr>
									<th>등록자(소유자)</th>
									<td><label data-bind="creator_name"></label></td>
									<th>등록일</th>
									<td><label data-bind="create_date"></label></td>
								</tr>
								<tr>
									<th>수정자</th>
									<td><label data-bind="updater_name"></label></td>
									<th>수정일</th>
									<td><label data-bind="update_date"></label></td>
								</tr>
								<tr>
									<th>다차원 분류</th>
									<td colspan="3"><label data-bind="multiFolderList"></label></td>
								</tr>
								<tr>
									<th>키워드</th>
									<td colspan="3"><label data-bind="keyword"></label></td>
								</tr>
								<tr>
									<th>설명</th>
									<td colspan="3"><label data-bind="doc_description"></label></td>
								</tr>
							</table>
						</div>
					</div>
					<div class="doc_detail_auth hide">
							<a class="dropDown_txt">
								<strong>권한 : </strong><label data-bind="aclName"></label>
								<span class="dropDown_arrow down"></span>
							</a>
							<div class="doc_auth_cnts hide" >
								<table id="detail_docAclItemListVertical">
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
								<table id="detail_docExAclItemListVertical">
									<thead>
				       					<tr><th colspan="5">추가 접근자</th></tr>
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
                   		</div>
				</div>
			</div>
		</div>
		<div class="pg_navi_wrapper">
			<ul id="myDocPager" class="pg_navi">
				<!-- 문서목록 Pager -->
			</ul>
		</div>
	</div>
</div>
<!-- Div Layer add -->


<jsp:include page="/jsp/popup/documentListLayerWindow.jsp" />
<jsp:include page="/jsp/popup/registFolderWindow.jsp"/>
<jsp:include page="/jsp/popup/selectSingleFolderWindow.jsp"/>
<jsp:include page="/jsp/popup/selectAclWindow.jsp"/>
<jsp:include page="/jsp/popup/selectFavoriteFolderWindow.jsp"/>
<jsp:include page="/jsp/popup/registAclWindow.jsp"/>
<jsp:include page="/jsp/popup/selectAccessorWindow.jsp"/>
<jsp:include page="/jsp/popup/registDocWindow.jsp" /><!-- 문서 등록화면 -->
<jsp:include page="/jsp/popup/documentDetail.jsp" /><!-- 문서 상세 조회화면 -->

<!-- script add -->
<script type="text/javascript" src="${contextRoot}/js/mydoc/myDocList.js"></script>
<script type="text/javascript">
jQuery(function() {
	//page 초기화
	myDocList.init.initPage("${pageSize}");
	exsoft.util.layout.topMenuSelect("${menuType}");
	
	// listLayerWindow 초기화
	documentListLayerWindow.gObjectID = "myDocList";
	documentListLayerWindow.gWorkType = myDocList.workType;
	
	// 상세검색 초기화
	docDetailSearch.targetGridId = "#myDocList";
	exsoft.util.grid.gridResize('myDocList','myDocListTarget',20,0);
	
	// 알림 아이디 설정
	exsoft.notyDivId = "#myDocNoty";
});
</script>