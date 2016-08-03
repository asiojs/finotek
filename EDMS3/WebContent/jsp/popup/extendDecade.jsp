<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/extendDecade.js"></script>
<!-- 
	보존기간연장
 -->
<div class="extend_preserve_wrapper hide"></div>
<div class="extend_preserve hide">
	<div class="extend_preserve_title">
		보존기간 연장 
		<a href="#" class="extend_preserve_close">
			<img src="${contextRoot}/img/icon/window_close.png" alt="" title="">
		</a>
	</div>
	<div class="extend_preserve_cnts">
		<div>
	    	<p><span class="cnts_txt">선택한 문서의 보존기간을</span></p>
	    	<p>
	    		<select id="preservationYear">
	     			<option value="1">1년</option>
					<option value="2">2년</option>
					<option value="3" selected>3년</option>
					<option value="5">5년</option>
					<option value="10">10년</option>
					<option value="0">영구</option>
	     		</select>
	      		<span class="cnts_txt left">연장합니다.</span>
	    	</p>
    	</div>    	
		<div class="extend_preserve_btnGrp">
			<button type="button" id="preserve_confirm_btn" class="" onclick="javascript:extendDecade.event.okButtonClick();">확인</button>
			<button type="button" id="preserve_cancel_btn" class="" onclick="javascript:extendDecade.event.cancelButtonClick();">취소</button>
		</div>
	</div>
</div>
