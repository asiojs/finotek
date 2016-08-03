<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="top_menu">
	<div class="top_menu_custom">
		<ul class="custom_menu_list">
			<!--  
			<li class="full_screen">
				<a href="javascript:void(0);" class="full_screen">
					<img src="${contextRoot}/img/icon/fullscreen.png" alt="" title="">
				</a>
			</li>
			-->
			<li class="quick_menu">
				<a href="#" class="quick_menu">
					<img src="${contextRoot}/img/icon/quickmenu.png" alt="" title="">퀵메뉴
					<span class="quick_menu"></span>
				</a>
				<div class="quick_sub_wrapper">
					<ul class="quick_sub_menu">
					<!--  사용자 QuickMenu -->
					</ul>
				</div>
			</li>
		</ul>
	</div>
	<div class="top_menu_shape"></div>
	<div class="top_menu_search">
		<span class="lbl_integrated_search">통합검색</span>
		<div class="search_input_form">
				<input type="text" class="search_text" placeholder="검색어를 입력하세요">
				<button type="button" class="search_submit" onclick="javascript:exsoftLayoutFunc.event.goSearch();"></button>			
		</div>

		<div class="search_detail">
			<a href="javascript:void(0);" class="dropDown_img" onclick="javascript:exsoftLayoutFunc.open.searchDetail();"><img src="${contextRoot}/img/icon/search_detail_btn.png" alt=""></a>
			<div class="integrated_dropDown_menu hide" id="searchDetailView">
				<form id="searchForm">
					<table class="srch_integrated_tbl">
						<tr>
						<th>기본검색</th>
						<td>
							<input type="text" class="srch_integrated_txt" placeholder="검색어를 입력하세요">
							<!-- <label><input type="checkbox" class="" name="" class="" value="">결과내 재 검색</label> -->
						</td>
						</tr>
						<tr>
						<th>등록기간</th>
						<td>
							<a href="javascript:void(0);" onclick="javascript:exsoft.util.date.changeDate('all', 'strSdate', 'strEdate');" class="srch_integrated_regDate">전체</a>
							<a href="javascript:void(0);" onclick="javascript:exsoft.util.date.changeDate('one_month', 'strSdate', 'strEdate');" class="srch_integrated_regDate">1개월</a>
							<a href="javascript:void(0);" onclick="javascript:exsoft.util.date.changeDate('three_month', 'strSdate', 'strEdate');" class="srch_integrated_regDate">3개월</a>
							<a href="javascript:void(0);" onclick="javascript:exsoft.util.date.changeDate('half_year', 'strSdate', 'strEdate');" class="srch_integrated_regDate">6개월</a>
							<a href="javascript:void(0);" onclick="javascript:exsoft.util.date.changeDate('one_year', 'strSdate', 'strEdate');" class="srch_integrated_regDate">1년</a>			
							<input type="text" id="strSdate" name="" class="srch_integrated_startDate input-text calend" name="start_date">
							<span class="dateto">-</span>
							<input type="text" id="strEdate" name="" class="srch_integrated_endDate input-text calend" name="end_date">
						</td>
						</tr>
						<tr>
						<th>검색범위</th>
						<td>
							<label><input type="radio" name="strLange" class="" value="">전체</label>
							<label><input type="radio" name="strLange" class="" value="">제목</label>
							<label><input type="radio" name="strLange" class="" value="">본문</label>
							<label><input type="radio" name="strLange" class="" value="">등록자</label>
							<label><input type="radio" name="strLange" class="" value="">첨부파일명</label>
							<label><input type="radio" name="strLange" class="" value="">첨부내용</label>
						</td>
						</tr>
						<tr>
						<th>문서유형</th>
						<td>
							<select id="strDocType">
								<option value="" selected="selected">전체</option>
								<option value="">일반문서</option>
								<option value="">파일문서</option>
							</select>							
						</td>
						</tr>
						<tr>
						<th>폴더위치</th>
						<td>
							<input type="text" id="" name="" class="srch_integrated_folder">
							<button type="button" id="" name="" class="srch_integrated_folderChoose">선택</button>
							<label><input type="checkbox" id="" name="" class="" value="">하위폴더포함</label>
						</td>
						</tr>
						<tr>
						<td colspan="2" class="srch_integrated_btnGrp">
							<button type="button" id="srch_integrated_submit" class="srch_integrated_btn" onclick="javascript:exsoftLayoutFunc.event.goSearch();">검색</button>
							<button type="reset" class="srch_integrated_btn">초기화</button>
						</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</div>