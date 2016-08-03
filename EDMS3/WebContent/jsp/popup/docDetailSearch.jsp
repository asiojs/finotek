<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="srch_detail_dropDown">
	<a class="dropDown_txt"><label>상세</label><span class="dropDown_arrow down" id="docDetailDropDown"></span></a>
	<div class="detail_dropDown_menu hide">
	<form id="frm_docListLayer_detail_search" name="frm_docListLayer_detail_search">
		<input type="hidden" name="is_extended" value='F'> 	<!-- 확장문서 검색 여부 -->
		<table class="srch_detail_tbl">
		<tr>
			<th>기본검색</th>
			<td>
				<select id="docDetailIndex_select" name="strIndex">
					<option value="doc_name">제목</option>
	          		<option value="doc_description">내용</option>
	          		<option value="creator_name">등록자</option>
				</select>
				<input type="text" name='strKeyword1' class="srch_detail_txt" placeholder="검색어를 입력하세요">
			</td>
		</tr>
		<tr>
			<th>&nbsp;</th>
			<td>
				<label class="srch_detail_lbl">
					<input type="checkbox" name='child_include' class="srch_detail_checkbox">
					<span>하위폴더 포함</span>
				</label>
			</td>
		</tr>
		<tr>
			<th>등록기간</th>
			<td>
				<a href="javascript:void(0);" id="all_month" class="srch_detail_regDate">전체</a>
				<a href="javascript:void(0);" id="one_month" class="srch_detail_regDate">1개월</a>
				<a href="javascript:void(0);" id="three_month" class="srch_detail_regDate">3개월</a>
				<a href="javascript:void(0);" id="half_year" class="srch_detail_regDate">6개월</a>
				<a href="javascript:void(0);" id="one_year" class="srch_detail_regDate">1년</a>			
				<input type="text" id="datepicker1" name="sdate" class="srch_detail_startDate input-text calend" readonly="readonly">
				<span class="dateto">-</span>
				<input type="text" id="datepicker2" name="edate" class="srch_detail_endDate input-text calend" readonly="readonly">
			</td>
		</tr>
		<tr>
			<th>첨부파일명</th>
			<td><input type="text" name="page_name" class="srch_detail_keyword"></td>
		</tr>
		<tr>
			<th>키워드</th>
			<td><input type="text" name="keyword" class="srch_detail_keyword"></td>
		</tr>
		<tr>
			<th>문서유형</th>
			<td>
				<select id="docDetailType_select" name="doc_type">
					<option value="ALL_TYPE" selected="selected">전체</option>
					<c:choose>
						<c:when test="${fn:length(typeList) > 0}">
							<c:set var="count" value="${fn:length(typeList) }"></c:set>
							<c:forEach items="${typeList}" var="m" varStatus="type_id">
								<option value="${m.type_id}">${m.type_name}</option>
							</c:forEach>
						</c:when>
					</c:choose>
				</select>							
			</td>
		</tr>
		<tr class="hide">
			<td colspan="2">
				<!-- 확장문서유형 START -->
		         <table class='srch_detail_tbl' id="docDetailSearch_docType">
		         	<thead></thead>
		         	<tbody></tbody>    
		         </table>
		         <!-- 확장문서유형 END  -->
			</td>
		</tr>
		<tr>
			<td colspan="2" class="srch_detail_btnGrp">
				<button type="button" class="srch_detail_btn" onClick='javascript:docDetailSearch.event.search();'>검색</button>
				<button type="button" class="srch_detail_btn" onClick='javascript:docDetailSearch.event.docDetailSearchInit("frm_docListLayer_detail_search");'>초기화</button>
			</td>
		</tr>
		</table>
		
         
	</form>
	</div>
</div>
<!-- script add -->
<script type="text/javascript" src="${contextRoot}/js/popup/docDetailSearch.js"></script>
<script type="text/javascript">
jQuery(function() {
	docDetailSearch.init.initPage();
});
</script>