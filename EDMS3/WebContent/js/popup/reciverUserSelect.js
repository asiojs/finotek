/**
 * 쪽지 관련 스크립트
 */
$(function(){
	
});

var gChecked_ids = [];		// jstree 체크된 목록 리스트 :: 공통
var gJstreeLoad = "";			// jstree 로드여부 :: 공통
var selectGroupUserReciverWindow = {

		
		callback : null,
		isValidation : false,
		srcGroupUserInfo : null,
		currentWorkType : "",
		
		open : function() {
			exsoft.util.layout.divLayerOpen("noteuserTree", "note_group");
		},
		
		close : function() {
			exsoft.util.layout.divLayerClose("noteuserTree", "note_group");
		},
		
		tree : {
			groupuserTree : null
		},
		
		treeFunctions : {
			initTree : function(workType) {
				var treeOption = {						
						context : exsoft.contextRoot,
						url : "/group/groupList.do"
					};
				//new XFTree("basicTree", "${contextRoot}", "/group/groupList.do");
				// 부서 문서함
				treeOption.divId = "#selectGroupTreeDiv";
				
				//selectGroupUserWindow.currentWorkType = "WORK_MYDEPT"; //Constant.WORK_MYDEPT;
				
				if (selectGroupUserReciverWindow.tree.groupuserTree == null) {
					
					
					selectGroupUserReciverWindow.tree.groupuserTree = new XFTree(treeOption);
					//selectGroupUserWindow.tree.groupuserTree.template_multiCheck(true);//체크박스
					
					
					// 부서 TreeNode 선택
					selectGroupUserReciverWindow.tree.groupuserTree.callbackSelectNode = function (e, data) {
						
						// 부서-사용자 Tree 리스트
						$('#treeGroupUserList').empty();
						var buffer = "";

						exsoft.util.ajax.ajaxDataFunctionWithCallback({context:exsoft.contextRoot,groupId:data.node.id}, url+"/user/groupUserList.do", "note", function(data1, e) {
							//alert(data1.list[0].user_id);
							$(data1.list).each(function(index){
								buffer += "<tr>";
								buffer += "<td><input type='checkbox' id='adduser' class='' name='adduser' class=''></td>";
								buffer += "<td>"+data1.list[index].user_nm+"</td>";
								buffer += "<td>"+data1.list[index].user_id+"</td>";
								buffer += "<td>"+data1.list[index].group_nm+"</td>";
								buffer += "<td>"+data1.list[index].email+"</td>";
								buffer += "</tr>";
							
							});
							$('#treeGroupUserList').append(buffer);	
						});
						
						//selectGroupUserWindow.tree.groupuserTree = new XFTree(treeOption);
						//selectGroupUserWindow.tree.groupuserTree.init();
						//base.gridPostDataRefresh('treeGroupUserList','${contextRoot}/admin/groupUserList.do', {groupId:data.node.id});
					};

					selectGroupUserReciverWindow.tree.groupuserTree.callbackLoadNode = function (e, data) {
						gJstreeLoad = e.type;
					},
					
					// jsTree check box list
					selectGroupUserReciverWindow.tree.groupuserTree.callbackAllSelectNodeData = function (selectedNodeData) {
						gChecked_ids = selectedNodeData;
					},
					
					selectGroupUserReciverWindow.tree.groupuserTree.init();
				} else {
					selectGroupUserReciverWindow.tree.groupuserTree.refresh();
				}
				
			
			},
		},//treefunction END
		
		event : {
			
		},//event END
	
		
		init : {
			initAction : function(srcGroupUser, isValidation, callback) {
				selectGroupUserReciverWindow.srcGroupUserInfo = srcGroupUser;
				selectGroupUserReciverWindow.isValidation = isValidation;
				selectGroupUserReciverWindow.callback = callback;
				selectGroupUserReciverWindow.treeFunctions.initTree(selectGroupUserReciverWindow.currentWorkType);
				selectGroupUserReciverWindow.open();
	
			},
			
		},//init END
			
		
		ui : {
			
			//체크박스 선택 해제
			allCheckUnCheckBox : function(id,name){
				if($("#"+id).prop("checked")){
					$("input[name="+name+"]:checkbox").prop("checked", true);
				}else{
					$("input[name="+name+"]:checkbox").prop("checked", false);
				}
			},
					
			
		},//ui END
		
		event : {
			
			// 사용자명 & 그룹명으로 해당 유저 찾기
			searchUserGroupName : function(){
				//검색 조건 공백 제거
				var usernm =$.trim($('.filter_note_name').val());
				var groupnm= $.trim($('.filter_depart_name').val());
				if(usernm=="" && groupnm==""){
					jAlert("검색 조건을 입력해 주세요", "확인", 0);
				}
				var buffer="";
				$('#treeGroupUserList').empty();	
				exsoft.util.ajax.ajaxDataFunctionWithCallback({groupName:groupnm,userName:usernm}, url+"/user/searchUserList.do", "note", function(data, e) {
					$(data.list).each(function(index){
						buffer += "<tr>";
						buffer += "<td><input type='checkbox' class='' id='adduser' name='adduser' class=''></td>";
						buffer += "<td>"+data.list[index].user_name_ko+"</td>";
						buffer += "<td>"+data.list[index].user_id+"</td>";
						buffer += "<td>"+data.list[index].group_nm+"</td>";
						buffer += "<td>"+data.list[index].email+"</td>";
						buffer += "</tr>";
					
					});
					$('#treeGroupUserList').append(buffer);						
				});
			},
					
			
			//받는 사람을 부모창에 Setting
			reciverSet : function(){
				
				var target = $("#treeGroupUserList").find("tr:has(:checked)");
				if(target.size() ==0){
					jAlert("추가할 사용자를 선택해 주세요", "확인", 0);
					return false;
				}
				
				if(target.size() > 1){
					jAlert("추가할 사용자는 한명만 선택해 주세요", "확인", 0);
					return false;
				}
				//메일 주소 세팅
				var reciveEmail = "dio98@naver.com";//target.eq(0).find("td").eq(4).text();
				
				//부모창 받는 사람에 값 Setting
				$(".email_receiver", opener.document).val(reciveEmail);
				
				//현재창 닫기
				exsoft.util.layout.windowClose();
			},
			
		
		},//event END
	};