<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	// contextPath가 있는 경우 대비
	String contextRoot = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>eXpert EDMS</title>
	<frameset border="0">
		<frame src="<%=contextRoot%>/login.do" scrolling="no" noresize></frame>
	</frameset>
</head>
<body class="${theme}">
</body>
</html>
