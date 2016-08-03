<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/registMail.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/base64.js"></script>
<!--
	URL 메일 송부
 -->
<div class="url_emailSend_wrapper hide"></div>
<div class="url_emailSend hide">
	<div class="url_emailSend_title">
		URL 메일 송부 
		<a href="#" class="url_emailSend_close">
			<img src="${contextRoot}/img/icon/window_close.png" alt="" title="">
		</a>
	</div>

	<div class="url_emailSend_cnts">
		<table>
			<tr>
				<th>보내는 사람<span class="required">*</span></th>
				<td>
					<input type="text" id="sender_name" name="" class="sender_name"> 
					<input type="text" id="sender_email" name="" class="sender_email"></td>
			</tr>
			<tr>
				<th>받는 사람<span class="required">*</span></th>
				<td>
					<input type="text" id="receiver_email" name="" class="receiver">
					<button type="button" class="srch_receiver" class="" onclick="javascript:registMail.open.addrOpen();">찾기</button>
				</td>
			</tr>
			<tr>
				<th>참조</th>
				<td>
					<input type="text" id="cc_email" name="" class="cc_txt">
					<button type="button" class="srch_cc" class="" onclick="javascript:registMail.open.addrOpen();">찾기</button>
				</td>
			</tr>
			<tr>
				<th>숨은 참조</th>
				<td>
					<input type="text" id="hcc_email" name="" class="hiddencc_txt">
					<button type="button" class="srch_hiddencc" class="" onclick="javascript:registMail.open.addrOpen();">찾기</button>
				</td>
			</tr>
			<tr>
				<th>메일제목<span class="required">*</span></th>
				<td>
					<input type="text" id="email_subject" name="" class="email_subject" value="">
				</td>
			</tr>
			<tr>
				<th>URL 첨부문서</th>
				<td>
					<table class="url_attached_docs" id="mailDocTable">
						<colgroup>
							<col width="320" />
							<col width="75" />
							<col width="47" />
						</colgroup>
						<thead>
							<tr>
								<th class="left">첨부파일명</th>
								<th>용량</th>
								<th>삭제</th>
							</tr>
						</thead>
					</table>
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea id="email_msgTxt" name="" class="email_cnts"></textarea></td>
			</tr>
		</table>
	</div>
	<div class="url_emailSend_btnGrp">
		<button type="button" class="url_emailSend_btn" class="" onclick="javascript:registMail.event.sendButtonClick();">보내기</button>
		<button type="button" class="url_emailCancel_btn" class="" onclick="javascript:registMail.event.sendCancelButtonClick();">취소</button>
	</div>
</div>

<div id="copyToMail" class="hide">
	<table style="border:1px solid #ccc; background:#fff; border-collapse:collapse;font-family:verdana,arial; font-size: 12px; ">
		<col style="width:30%">
		<col style="width:30%">
		<col style="width:20%">
		<col style="width:30%">
		<tr>
			<th style="text-align:left; background:#f7f7f7; border:1px solid #ccc;">내용</th>
			<td colspan="3" style="text-align:left; border:1px solid #ccc;"><label id="copy_msg_txt"></label></td>
		</tr>
		<tr>
			<th style="text-align:left; background:#f7f7f7; border:1px solid #ccc;">파일</th>
			<td colspan="3" style="text-align:left; border:1px solid #ccc;"><label id="copy_file_list"></label></td>
		</tr>
	</table>
</div>

<!-- Div Layer add -->
<jsp:include page="/jsp/popup/selectMailAddr.jsp" />	<!-- 찾기버튼 클릭시 -->
<jsp:include page="/jsp/popup/urlCopyPeriod.jsp" />	<!-- URL링크 조회기간설정 팝업창 -->