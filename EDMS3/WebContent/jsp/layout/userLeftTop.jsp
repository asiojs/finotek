<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<section class="lnb_menu_info">
	<div class="lnb_menu_reg">
		<ul>
			<li class="reg_doc"><a href="#" onclick="exsoft.document.layer.docWriteCommonFrm('doc_register_wrapper','doc_register');">문서등록</a></li>
			<li class="reg_work"><a href="javascript:void(0);" onclick='javascript:exsoft.process.write("coop_register_wrapper","coop_register");'>업무등록</a></li>
		</ul>
	</div>
	<div class="lnb_menu_summary">
		<table class="summary_table">
		<caption>문서등록 요약</caption>
		<colgroup>
			<col width="144"/>
			<col width="25"/>
		</colgroup>
		<!-- 
		<tr>
		<td class="summary_info">새 쪽지</td>
		<td class="summary_cnts"><span id='leftNoteNewCnt'></span></td>
		</tr>
		 -->
		<tr>
		<td class="summary_info">신규문서</td>
		<td class="summary_cnts"><span id='newDocCnt'></span></td>
		</tr>
		<tr>
		<td class="summary_info">승인대상문서</td>
		<td class="summary_cnts"><span id='process_approval'></span></td>
		</tr>
		<tr>
		<td class="summary_info">업무작성중문서</td>
		<td class="summary_cnts"><span id='process_work'></span></td>
		</tr>
		</table>
	</div>
</section>
<!--  공통부분 -->
<script type="text/javascript">
jQuery(function() {
	// 좌측 공통 영역 개수 구하기
	exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({'type' : Constant.PROCESS.APPROVAL_ING},'${contextRoot}/process/processCount.do', 
			'#process_approval', exsoftLayoutFunc.callback.infoCount);		// 승인대상문서
	exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({'type' : Constant.PROCESS.WRITE_ING},'${contextRoot}/process/processCount.do',
			'#process_work', exsoftLayoutFunc.callback.infoCount);			// 업무작성중문서
			
	exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({'LIST_TYPE' : 'RECENTLYDOC','map_id' : 'MYDEPT'},'${contextRoot}/mypage/authDocumentList.do',
			'#newDocCnt', exsoftLayoutFunc.callback.infoCount);				// 신규문서
			
});
</script>
<jsp:include page="/jsp/process/processWrite.jsp" />