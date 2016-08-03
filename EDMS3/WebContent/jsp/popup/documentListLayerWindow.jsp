<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 
	Usage : 
	- 나의문서/개인문서/업무문서함 목록 공통 적용 화면
 -->
 <!-- 테이블 첨부파일 시작 -->
<div id="attachWindowPopup" class="attach_window hide">
	<div class="attach_title">
		첨부파일
		<a href="#" class="close"><img src="${contextRoot}/img/icon/window_close.png" onclick="documentListLayerWindow.close.closeAttachWindow();"></a>
	</div>
	<div class="attach_cnts">	
		<div class="attach_summary">
			<span class="file_cnt"></span>
			<a href="#" class="attach_save_all" onclick="documentListLayerWindow.event.downloadAllFiles();">모두저장</a>
		</div>
		<ul class="attach_file_list">
		</ul>
	</div>
</div>
<!-- 테이블 첨부파일 끝 -->

<!-- 테이블 관련문서 시작 -->
<div id="relationWindowPopup" class="relative_docs_window hide">
	<div class="relative_docs_title">
		관련문서
		<a href="#" class="close"><img src="${contextRoot}/img/icon/window_close.png" onclick="documentListLayerWindow.close.closeRelationWindow();"></a>
	</div>
	<div class="relative_docs_cnts">	
		<table id="relationWindowDocList">
			<colgroup>
			<col width="32">
			<col width="210">
			<col width="52">
			<col width="86">
			</colgroup>
			<thead>
				<tr>
					<th name="no">No.</th>
					<th name="doc_name">제목</th>
					<th name="creator_name">등록자</th>
					<th name="create_date">등록일</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>
<!-- 테이블 관련문서 끝 -->

<!-- 반출정보 시작 -->
<div class="doc_lock hide">
	<p>반출자 : <span class="lock_usr">홍길동</span></p>
	<p>반출일시 : <span class="lock_date">2014.08.28</span></p>
</div>
<!-- 반출정보 끝 -->
    
<!-- 권한조회 시작 -->
<div class="previlege_inquiry hide">
	<p>소유자 : <span class="previlege_owner">홍길동</span></p>
	<p>기본권한 : <span class="previlege_default">조회</span></p>
	<p>반출취소 : <span class="lock_cancel">없음</span></p>
	<p>권한변경 : <span class="previlege_modify">없음</span></p>
</div>
<!-- 권한조회 끝 -->
    
    
<!-- 공통 우클릭 컨텍스트 메뉴 시작 -->
<div id="documentListLayer_context_menu" class="tbl_context_menu hide">
	<ul>
		<li id="documentListLayer_update" class="hide"><a href="#" class="modify"><span class="left"></span><span class="right">수정</span></a></li>
		<li id="documentListLayer_delete" class="hide"><a href="#" class="delete"><span class="left"></span><span class="right">삭제</span></a></li>
		<li id="documentListLayer_move" class="hide"><a href="#" class="move"><span class="left"></span><span class="right">이동</span></a></li>
		<li id="documentListLayer_copy" class="hide"><a href="#" class="copy"><span class="left"></span><span class="right">복사</span></a></li>
		<li id="documentListLayer_favorite_add" class="hide"><a href="#" class="favorite"><span class="left"></span><span class="right">즐겨찾기 추가</span></a></li>
		<li id="documentListLayer_work_add" class="hide"><a href="#" class="tempbox"><span class="left"></span><span class="right">작업카트 추가</span></a></li>
		<li id="documentListLayer_checkout_cancel" class="hide"><a href="#" class="cancel_checkout"><span class="left"></span><span class="right">체크아웃 취소</span></a></li>
		<!-- 나의문서-휴지통-삭제 -->
		<li id="documentListLayer_authWaste_delete" class="hide"><a href="#" class="delete"><span class="left"></span><span class="right">삭제</span></a></li>
		<!-- 나의문서-휴지통-복원 -->
		<li id="documentListLayer_restore" class="hide"><a href="#" class="restore"><span class="left"></span><span class="right">복원</span></a></li>
	</ul>
</div>
<!-- 공통 우클릭 컨텍스트 메뉴 끝 -->

<script type="text/javascript" src="${contextRoot}/js/popup/documentListLayerWindow.js"></script>