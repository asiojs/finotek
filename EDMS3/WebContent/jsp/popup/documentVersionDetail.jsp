<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width; initial-scale=1.0">
<title>Document Detail</title>
<script type="text/javascript">
var contextRoot = "${contextRoot}";
var windowType = 'userLayout';
</script>
<script type="text/javascript" src="${contextRoot}/js/plugins/jquery/jquery-2.1.3.min.js"></script>  
<script type="text/javascript" src="${contextRoot}/js/common/common.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/include.js"></script>
<script type="text/javascript" src="${contextRoot}/js/popup/documentVersionDetail.js"></script>
<script>document.domain = exsoft.contextRoot;</script>
<script type="text/javascript">
var url = exsoft.contextRoot;
$(document).ready(function() {
	
	exsoftDocVersionDetailFunc.init.initDocumentViewWindow('${docId}');
	
});

</script>
</head>
			
<body>

	<div class="">
					<div class="">
						<span class="">버전 상세 조회</span>
					</div>
					<div class="">
						<span class="s_title">제목 | <label id="view_doc_name"></label></span><span class="lock_circle_icon"></span><span class="ver" id="view_doc_version"></span>
					</div>
	</div>				
<div class="">
						<div class="">
	<table class="">
	<col style="width:20%">
	<col style="width:30%">
	<col style="width:20%">
	<col style="width:30%">
	<tr>
		<th>기본 폴더</th>
			<td colspan="3"><label id="view_folderPath"></label>
				</td>
	</tr>
	<tr>
	<th>문서유형</th>
									<td><label id="view_type_name"></label></td>
									<th>보존연한</th>
									<td><label id="view_preservation_year"></label></td>
								</tr>
								<tr>
									<th>보안등급</th>
									<td><label id="view_security_level_name"></label></td>
									<th>조회등급</th>
									<td><label id="view_access_grade_name"></label></td>
								</tr>
								<tr>
									<th>등록자(소유자)</th>
									<td><label id="view_creator_name"></label></td>
									<th>등록일</th>
									<td><label id="view_create_date"></label></td>
								</tr>
								<tr>
									<th>다차원분류</th>
									<td colspan="3"><label id="view_multiLink"></label></td>
								</tr>
								<tr>
									<th>키워드</th>
									<td colspan="3"><label id="view_keyword"></label></td>
								</tr>
								<tr>
									<th>설명</th>
									<td colspan="3">
										<span style='display: none'><TEXTAREA id="vtemp_content"></TEXTAREA></span> 
										<iframe src="" id="vIframe_editor" name="vIframe_editor" style="border: 0 solid transparent; padding: 10px 0 0; margin: 0; height: 120px; width: 99%"></iframe>
									</td>
								</tr>
							</table>
							</div>		
							</div>		 
</body>
</html>
