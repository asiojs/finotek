<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>		
<div class="cnt_wrapper">
	<div class="error_page">
		<div class="error_page_left"><img src="${contextRoot}/img/icon/stricted.png" alt="" title=""></div>
		<div class="error_page_right">
		<c:choose>    
			<c:when test="${errorCode =='403'}">
				<p class="error_title">403 ERROR</p>
				<p class="error_cnts">
					${errorMessage}
				</p>
			</c:when>
			<c:when test="${errorCode =='505'}">
				<p class="error_title">505 ERROR</p>
				<p class="error_cnts">
					${errorMessage}
				</p>
			</c:when>
			<c:otherwise>
				<p class="error_title">페이지를 찾을 수 없습니다.</p>
				<p class="error_cnts">
					찾으시는 페이지의 주소가 잘못 입력되었거나,<br>
					페이지의 주소가 변경 혹은 서버에서 삭제되었을 수 있습니다.
				</p>
			</c:otherwise>
		</c:choose>						
		</div>
	</div>
</div>