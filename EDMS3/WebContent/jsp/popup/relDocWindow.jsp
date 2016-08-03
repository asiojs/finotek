<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/relDocWindow.js"></script>

<!-- 
	Usage :
		문서등록(수정)시 관련문서 추가 선택 화면			 
 -->
<div class="doc_relativeAdd_wrapper hide"></div>
<div class="doc_relativeAdd hide">
	<div class="doc_relativeAdd_title">
		관련문서 추가
		<a href="#" class="doc_relativeAdd_close">
			<img src="${contextRoot}/img/icon/window_close.png" alt="" title="" onclick="javascript:selectrelativeDocWindow.close();">
		</a>
	</div>
	<div class="doc_relativeAdd_cnts">
		<div class="relativeAdd_left">
			<div class="tree_menu">
				<ul class="tree_menu_list">
					<li><a href="javascript:void(0);" onclick="javascript:selectrelativeDocWindow.event.relDoc_changeMap('WORK_MYDEPT')" class="focus">부서</a></li>
					<li><a href="javascript:void(0);" onclick="javascript:selectrelativeDocWindow.event.relDoc_changeMap('WORK_ALLDEPT')" >전사</a></li>
					<li><a href="javascript:void(0);" onclick="javascript:selectrelativeDocWindow.event.relDoc_changeMap('WORK_PROJECT')">프로젝트</a></li>
				</ul>
				<div class="tree_menu_more">
					<ul class="menu_more_list">
						<li><a href="#"><img src="${contextRoot}/img/icon/tree_more.png" alt="" title=""></a></li>
						<li><a href="#"><img src="${contextRoot}/img/icon/tree_refresh.png" alt="" title=""></a></li>
					</ul>
				</div>
			</div>
			
			
			<!-- <div class="relativeAdd_tree_list" class="tree"></div> -->
			

			<div id="relativeAddMypageTree" class="relativeAdd_tree_list"></div>
			<div id="relativeAddMydeptTree" class="relativeAdd_tree_list hide"></div>
			<div id="relativeAddAlldeptTree" class="relativeAdd_tree_list hide"></div>
			<div id="relativeAddProjectTree" class="relativeAdd_tree_list hide"></div>
				
			
		</div>
		<div class="relativeAdd_right">
			<div class="relativeAdd_docs_list">
				<span class="relativeAdd_docs_category" id="relDoc_folderTitle">20. 안전관리규정</span>
				<div class="relativeAdd_srch_form">
					<form class="">
						<select id="relativeAdd_srch_category" name="">
							<option value="">제목</option>
							<option value="">등록자</option>
						</select> 
						<input type="text" id="" name="" class="relativeAdd_srch_txt border-radius-right" placeholder="검색어를 입력하세요"> 
						<span class="relativeAdd_regdate_lbl">등록기간</span>

						<!-- 달력 -->
						<input type="text" id="relativeAdd_datepicker1" class="input-text calend" readonly="readonly"> 
						<span>-</span>
						<input type="text" id="relativeAdd_datepicker2" class="input-text calend" readonly="readonly">
						<button type="button" class="relativeAdd_doc_srch">검색</button>
						<button type="button" class="relativeAdd_doc_allsrch">전체검색</button>
					</form>
				</div>
				<div class="relativeAdd_list">
					<table class="relativeAdd_list_tbl" id="relDoc_document_gridList">
						<%-- <colgroup>
							<col width="38" />
							<col width="323" />
							<col width="88" />
							<col width="88" />
							<col width="98" />
						</colgroup>
						<thead>
							<tr>
								<th><input type="checkbox" class="" name="" value=""></th>
								<th>제목 
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown_up.png" alt="" title=""></a>
								</th>
								<th>문서유형</th>
								<th>등록자 
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown_up.png" alt="" title=""></a>
								</th>
								<th>등록일 
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown_up.png" alt="" title=""></a>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><input type="checkbox" class="" name="" value=""></td>
								<td><a href="#">자재업무 메뉴얼</a></td>
								<td>일반문서</td>
								<td>홍길동</td>
								<td>2014.03.02</td>
							</tr>
							
						</tbody> --%>
					</table>
					<div id="relDoc_documentGridPager"></div>
				</div>
				<div class="relativeAdd_pg_navigation">
					<select id="relativeAdd_pg_cnt">					
						<option>10</option>
						<option>20</option>
						<option>30</option>
					</select>
					<ul class="pg_navi">
						<li class="first"><a href="#"><img src="${contextRoot}/img/icon/pg_first.png" alt="" title=""></a></li>
						<li class="prev"><a href="#"><img src="${contextRoot}/img/icon/pg_prev.png" alt="" title=""></a></li>
						<li class="curr"><a href="#">1</a></li>
						<li><a href="#">2</a></li>
						<li><a href="#">3</a></li>
						<li><a href="#">4</a></li>
						<li><a href="#">5</a></li>
						<li><a href="#">6</a></li>
						<li><a href="#">7</a></li>
						<li><a href="#">8</a></li>
						<li><a href="#">9</a></li>
						<li><a href="#">10</a></li>
						<li class="next"><a href="#"><img src="${contextRoot}/img/icon/pg_next.png" alt="" title=""></a></li>
						<li class="last"><a href="#"><img src="${contextRoot}/img/icon/pg_last.png" alt="" title=""></a></li>
					</ul>
				</div>
				<div class="relativeAdd_list_btnGrp">
					<button type="button" class="relativeAdd_selected">추가</button>
					<button type="button" class="relativeAdd_unselected">제외</button>
				</div>
			</div>
			<div class="relativeAdd_docs_selected">
				<span class="bold">선택문서</span>
				<div class="relativeAdd_selected_list">
					<table class="relativeAdd_selectedList_tbl" id="relDoc_select_gridList">
						<colgroup>
							<col width="38" />
							<col width="323" />
							<col width="88" />
							<col width="88" />
							<col width="98" />
						</colgroup>
						<thead>
							<tr>
								<th><input type="checkbox" class="" name="" value=""></th>
								<th>제목 
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown_up.png" alt="" title=""></a>
								</th>
								<th>문서유형</th>
								<th>등록자 
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown_up.png" alt="" title=""></a>
								</th>
								<th>등록일 
									<a href="#"><img src="${contextRoot}/img/icon/head_dropdown_up.png" alt="" title=""></a>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><input type="checkbox" class="" name="" value=""></td>
								<td><a href="#">자재업무 메뉴얼</a></td>
								<td>일반문서</td>
								<td>홍길동</td>
								<td>2014.03.02</td>
							</tr>
							<tr>
								<td><input type="checkbox" class="" name="" value=""></td>
								<td><a href="#">자재업무 메뉴얼</a></td>
								<td>일반문서</td>
								<td>홍길동</td>
								<td>2014.03.02</td>
							</tr>
							<tr>
								<td><input type="checkbox" class="" name="" value=""></td>
								<td><a href="#">자재업무 메뉴얼</a></td>
								<td>일반문서</td>
								<td>홍길동</td>
								<td>2014.03.02</td>
							</tr>
							<tr>
								<td><input type="checkbox" class="" name="" value=""></td>
								<td><a href="#">자재업무 메뉴얼</a></td>
								<td>일반문서</td>
								<td>홍길동</td>
								<td>2014.03.02</td>
							</tr>
							<tr>
								<td><input type="checkbox" class="" name="" value=""></td>
								<td><a href="#">자재업무 메뉴얼</a></td>
								<td>일반문서</td>
								<td>홍길동</td>
								<td>2014.03.02</td>
							</tr>
							<tr>
								<td><input type="checkbox" class="" name="" value=""></td>
								<td><a href="#">자재업무 메뉴얼</a></td>
								<td>일반문서</td>
								<td>홍길동</td>
								<td>2014.03.02</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="relativeAdd_btnGrp">
				<button type="button" class="relativeAdd_save" class="" onClick="javascript:selectrelativeDocWindow.submit();">확인</button>
				<button type="button" class="relativeAdd_cancel" class="" onclick="javascript:selectrelativeDocWindow.close();">취소</button>
			</div>
		</div>
	</div>
</div>

