var selectGroupWindow = {
		callbackFunction : null,				// 확인 버튼 클릭시 결과를 반환할 함수
		groupTree : null,				// JSTree 그룹 트리 오브젝트
		projectTree : null,				// JSTree 프로젝트 트리 오브젝트
		currentTree : null,				// JSTree 현재 선택된 트리 오브젝트
		mapId : "MYDEPT",				// 맵
		popTreeOption : null,					// 트리 옵션 (그룹 / 프로젝트 / 전체)
		gSelectGroup_rootId : null,			// 트리 root가 관리부서를 사용할지 여부
		
		// 0. 초기화
		init : {
			initPage : function(callback, treeOption, manage_group_id) {
				selectGroupWindow.callbackFunction = callback;
				selectGroupWindow.popTreeOption = treeOption;
				if( manage_group_id != undefined || manage_group_id != null)
					selectGroupWindow.gSelectGroup_rootId = manage_group_id;
				
				// 그룹 팝업일 경우 "프로젝트" 탭 숨김
				if (selectGroupWindow.popTreeOption == "GROUP" || selectGroupWindow.popTreeOption == "MYDEPT") {
					selectGroupWindow.mapId = "MYDEPT";
					$("#popup_groupTree").removeClass("hide");
					$("#popup_projectTree").addClass("hide");
				} else if (selectGroupWindow.popTreeOption == "PROJECT"){
					selectGroupWindow.mapId = "PROJECT";
					$("#popup_projectTree").removeClass("hide");
					$("#popup_groupTree").addClass("hide");
				} else {
					selectGroupWindow.mapId = "MYDEPT";
				}
				
				selectGroupWindow.init.initTree();
				selectGroupWindow.open();
			},
			
			initTree : function() {
				var treeOption = null;
				
				if (selectGroupWindow.mapId == "MYDEPT") {
					treeOption = {
						context : exsoft.contextRoot,
						url : "/group/groupList.do",
						divId : "#popup_groupTree",
						mapId : Constant.MAP_MYDEPT,
						workType : Constant.WORK_MYDEPT,
						manageGroupId : selectGroupWindow.gSelectGroup_rootId
					};
					
					if(selectGroupWindow.groupTree == undefined) {
						selectGroupWindow.groupTree = new XFTree(treeOption);
						selectGroupWindow.groupTree.init();
					} else {
						selectGroupWindow.groupTree.refresh();
					}
					
					selectGroupWindow.currentTree = selectGroupWindow.groupTree;
					
				} else if (selectGroupWindow.mapId == "PROJECT") {
					treeOption = {
							context : exsoft.contextRoot,
							url : "/group/groupList.do",
							divId : "#popup_projectTree",
							mapId : Constant.MAP_PROJECT,
							workType : Constant.WORK_PROJECT
						};
					if(selectGroupWindow.projectTree == undefined) {
						selectGroupWindow.projectTree = new XFTree(treeOption);
						selectGroupWindow.projectTree.init();
					} else {
						selectGroupWindow.projectTree.refresh();
					}
					selectGroupWindow.currentTree = selectGroupWindow.projectTree;
				}
			}
		},

		// 1. 팝업
		open : function() {
			exsoft.util.layout.divLayerOpen("dept_choose_wrapper", "dept_choose");
		},

		//2. layer + show
		layer : {
		},

		//3. 닫기 + hide
		close : function() {
			exsoft.util.layout.divLayerClose("dept_choose_wrapper", "dept_choose");
		},

		//4. 화면 이벤트 처리
		event : {
			changeMap : function() {
				var map_id = $("#map_id").val();
				if(map_id == "MYDEPT") {
					$("#popup_groupTree").removeClass("hide");
					$("#popup_projectTree").addClass("hide");
				} else {
					$("#popup_projectTree").removeClass("hide");
					$("#popup_groupTree").addClass("hide");
				}
				selectGroupWindow.mapId = map_id
				selectGroupWindow.init.initTree();
			},
			
			// 확인 버튼 클릭시
			selectGroupSubmit : function() {
				var returnObjects = exsoft.util.common.getReturnTreeObject(selectGroupWindow.currentTree, 
						exsoft.util.common.getIdFormat(selectGroupWindow.currentTree.divId), selectGroupWindow.mapId);
				
				// 그룹 선택 팝업창 파라메터로 온 콜백함수를 호출한다.
				selectGroupWindow.callbackFunction(returnObjects);
				
				// 팝업을 숨긴다.
				selectGroupWindow.close();
			}
		},

		//5. 화면 UI 변경 처리
		ui : {
		},

		//6. callback 처리
		callback : {
		},
}