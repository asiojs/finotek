<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width; initial-scale=1.0">
<title>eXrep ECM</title>
<script type="text/javascript">
var contextRoot = "${contextRoot}";
var windowType = 'adminLayout';
</script>
<script type="text/javascript" src="${contextRoot}/js/plugins/jquery/jquery-2.1.3.min.js"></script>  
<script type="text/javascript" src="${contextRoot}/js/common/common.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/include.js"></script>
<c:choose>
<c:when test="${language == 'KO'}"><script type="text/javascript" src="${contextRoot}/js/common/messages/messages_ko.js"></script></c:when>
<c:when test="${language == 'EN'}"><script type="text/javascript" src="${contextRoot}/js/common/messages/messages_en.js"></script></c:when>
</c:choose>
<script type="text/javascript">
jQuery(function() {	
	
	$('#admContents').load('${contents}');
	
	// 파일 업로드 관련 정보 추가
	exsoft.common.file.prototype.wDefaultFileCnt = "${defaultFileCnt}";
	exsoft.common.file.prototype.wDefaultFileSize = "${defaultFileSize}";
	exsoft.common.file.prototype.wDefaultFileTotal = "${defaultFileTotal}";
});
</script>

</head>
<body>
<div class="wrap">
	<tiles:insertAttribute name="adminHeader" />		
	<div id="admContents"></div>
	<tiles:insertAttribute name="adminFooter" />			
</div>
</body>
</html>