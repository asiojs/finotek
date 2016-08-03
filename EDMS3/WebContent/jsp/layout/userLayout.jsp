<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width; initial-scale=1.0">

<title>eXrep ECM</title>
<!-- 키값 설정 -->
<script type="text/javascript">
var contextRoot = "${contextRoot}";
var windowType = 'userLayout';
</script>

<!-- 기본 js 선언 -->
<script type="text/javascript" src="${contextRoot}/js/plugins/jquery/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/common.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/include.js"></script>
<c:choose>
<c:when test="${language == 'KO'}"><script type="text/javascript" src="${contextRoot}/js/common/messages/messages_ko.js"></script></c:when>
<c:when test="${language == 'EN'}"><script type="text/javascript" src="${contextRoot}/js/common/messages/messages_en.js"></script></c:when>
</c:choose>
<script type="text/javascript">
jQuery(function() {

	$('#userContents').load('${contents}');

	exsoft.util.layout.lyrPopupWindowResize($(".loading"));					// 로딩이미지
	exsoft.util.layout.lyrPopupWindowResize($(".quickMenu"));				// 퀵메뉴
	$(window).resize(function(){		 
		exsoft.util.layout.lyrPopupWindowResize($(".loading"));
		exsoft.util.layout.lyrPopupWindowResize($(".quickMenu"));
	});
	
	exsoftLayoutFunc.init.quickTopMenuInit();									// 퀵메뉴 가져오기

	// 로그인 사용자 정보 추가
	exsoft.user.user_id = "${user_id}";
	exsoft.user.user_name = "${user_name}";
	exsoft.user.acl_menu_part = "${acl_menu_part}";
	exsoft.user.manage_group_id = "${manage_group_id}";
	exsoft.user.manage_group_nm = "${manage_group_nm}";
	exsoft.user.user_email = "${user_email}";
	
	// 파일 업로드 관련 정보 추가
	exsoft.common.file.prototype.wDefaultFileCnt = "${defaultFileCnt}";
	exsoft.common.file.prototype.wDefaultFileSize = "${defaultFileSize}";
	exsoft.common.file.prototype.wDefaultFileTotal = "${defaultFileTotal}";

});
</script>

</head>
<body>
  <div class="wrap">
  	<tiles:insertAttribute name="userTopMenu" />		
	<tiles:insertAttribute name="userHeader" />					
	<div id="userContents"></div>
    <tiles:insertAttribute name="userFooter" />			
  </div>
</body>
</html>