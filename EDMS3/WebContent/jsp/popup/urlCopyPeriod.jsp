<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${contextRoot}/js/popup/urlCopyPeriod.js"></script>
<!-- URL 복사  -->
<div class="url_copy_wrapper hide"></div>
<div class="url_copy hide">
	<div class="url_copy_title">
		URL 조회기간 설정
		<a href="#" class="url_copy_close"> <img src="${contextRoot}/img/icon/window_close.png" alt="" title=""></a>
	</div>
	<div class="url_copy_cnts">
		<form>
			<div class="url_copy_duration">
				<label>
					<input type="radio" name="url_date" id="urlDate1" value="unlimit"/>
					제한없음
				</label>
				<label>
					<input type="radio" name="url_date" id="urlDate2" checked value="limit" />
					<input type="text" class="numline" size="3" maxlength="3" id="urlExpired">
					일로 제한
				</label>
			</div>
			<div id="url_copy_confirmText" class="url_copy_confirm">
				URL 메일 송부를 하시겠습니까?
			</div>
			<div class="url_copy_btnGrp">
				<button type="button" onclick="javascript:urlCopyPeriod.event.getInfo();">확인</button>
				<button type="button" onclick="javascript:urlCopyPeriod.close();">취소</button>
			</div>
		</form>
	</div>
</div>