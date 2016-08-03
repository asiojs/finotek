<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width; initial-scale=1.0">
<title>Note</title>
<script type="text/javascript">
var contextRoot = "${contextRoot}";
var windowType = 'noteUserSelect';
</script>
<script type="text/javascript" src="${contextRoot}/js/plugins/jquery/jquery-2.1.3.min.js"></script>  
<script type="text/javascript" src="${contextRoot}/js/common/common.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/include.js"></script>
<script type="text/javascript" src="${contextRoot}/js/common/ecm_note.js"></script> <!-- TODO noteMain.js 합친 후 삭제 -->   
<script type="text/javascript" src="${contextRoot}/js/note/noteMain.js"></script>  
<script type="text/javascript" src="${contextRoot}/js/popup/reciverUserSelect.js"></script>

<!-- jstree js include -->
<link href="${contextRoot}/css/plugins/themes/jquery-ui.css" rel="stylesheet" type="text/css">
<link href="${contextRoot}/js/plugins/jstree/dist/themes/default/style.css" rel="stylesheet" type="text/css">
<script src="${contextRoot}/js/plugins/jstree/dist/jstree.js"></script>
<script src="${contextRoot}/js/common/tree.js"></script>
<script>document.domain = exsoft.contextRoot;</script>
<script type="text/javascript">
var url = exsoft.contextRoot;
$(document).ready(function() {
	
	 var jsonObject = {
			
			mode : "SELECT",
			only_virtual : "N",
			type : "WORK_ALLDEPT"
		};
	 selectGroupUserReciverWindow.init.initAction(jsonObject,false,function(returnObject) {
		exsoft.util.ajax.ajaxDataFunctionWithCallback(returnObject, exsoft.contextRoot+"/group/groupList.do", "", function(data, param) {
			if (data.result == "true") {
				jAlert("그룹폴더 완료");
			} else {
				jAlert(data.message);
			}
		});
	});
	 
});


</script>
</head>

<body>
<div class="wrap"> 
   	<!-- 쪽지 수신자 시작 -->
	<div class="note_reciverchoose">
   		<div class="note_choose_title">
			사용자 선택
			<a href="javascript:void(0);" class="note_reciverchoose_close">
			<img src="${contextRoot}/img/icon/window_close.png" onclick="javascript:exsoft.util.layout.windowClose();"></a>
		</div>
		<div class="note_choose_cnts">
			<div class="noteChoose_sub_wrapper" id="noteuserTree">
				<div class="note_group">
					<div class="note_group_title">그룹 선택</div>
					<div class="note_tree" id="selectGroupTreeDiv"></div>
				</div>
				<div class="note_reciverlist">
					<div class="note_srch_form">
						<form>
							<label>성명<input type="text" class="filter_note_name" name="" class="" value=""></label>
							<label>부서<input type="text" class="filter_depart_name" name="" class="" value=""></label>
							<button type="button" class="note_filter_btn" onclick="javascript:selectGroupUserReciverWindow.event.searchUserGroupName();">검색</button>
						</form>
					</div>
					<div class="note_list_result" >
						<table id='table1'>
						<thead>
						<tr>
						<th><input onclick="javascript:selectGroupUserReciverWindow.ui.allCheckUnCheckBox('selectUserList','adduser')" type="checkbox" class="" id="selectUserList" class=""></th>
						<th>사용자</th>
						<th>ID</th>
						<th>부서</th>
						<th>메일 ID</th>
						</tr>
						</thead>
						<tbody id="treeGroupUserList">
						
						</tbody>
						</table>
					</div>
				</div>				
			</div>
		</div>
		<div class="note_choose_btnGrp">
			<button type="button" class="note_chooseConfirm_btn" class="" onclick="javascript:selectGroupUserReciverWindow.event.reciverSet();">확인</button>
			<button type="button" class="note_chooseCancel_btn" onclick="javascript:exsoft.util.layout.windowClose();">취소</button>
		</div>
    </div>
   	<!-- 쪽지 수신자 끝 -->
  </div>
</body>
</html>
