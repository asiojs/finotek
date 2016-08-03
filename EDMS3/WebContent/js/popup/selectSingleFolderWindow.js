var selectSingleFolderWindow = {
	currentMapId : "",		// 현재 선택된 맵
	currentWorkType : "",	// 현재 선택된 부서함
	currentFolderID : "",	// 현재 선택된 폴더 ID
	
	isValidation : false,	// 유효성 검사 여부
	docType : "", 			// 문서 등록/이동 등등 연계 사용시 해당 문서의 Type
	callback : null,		// 확인버튼 클릭시 결과를 반환할 함수
	
	treeDiv : {
		mypage 	: "#singleFolderMypageTree",
		mydept 	: "#singleFolderMydeptTree",
		alldept : "#singleFolderAlldeptTree",
		project : "#singleFolderProjectTree",
		freedoc : "#singleFolderFreeDocTree"
	},
	
	open : function() {
		exsoft.util.layout.divLayerOpen("folder_choose_wrapper", "doc_folder_choose");
	},
	
	close : function() {
		exsoft.util.layout.divLayerClose("folder_choose_wrapper", "doc_folder_choose");
	},
	tree : {
		mypageTree : null,	// 개인 문서함
		mydeptTree : null,	// 부서 문서함
		allDeptTree : null,	// 전사 문서함
		projectTree : null	// 프로젝트 함
	},
	
	treeFunctions : {
		initTree : function(workType) {
			var treeOption = {
					context : exsoft.contextRoot,
					url : "/folder/folderList.do",
				};
			
			switch(workType) {
				case Constant.WORK_MYPAGE :	// 개인 문서함
					treeOption.divId = "#singleFolderMypageTree";
					treeOption.mapId = Constant.MAP_MYPAGE;
					treeOption.workType = Constant.WORK_MYPAGE;
					
					selectSingleFolderWindow.ui.activeTreeDiv("mypage");
					selectSingleFolderWindow.currentMapId = Constant.MAP_MYPAGE;
					selectSingleFolderWindow.currentWorkType = Constant.WORK_MYPAGE;
					
					if (selectSingleFolderWindow.tree.mypageTree === null) {
						selectSingleFolderWindow.tree.mypageTree = new XFTree(treeOption);
						selectSingleFolderWindow.tree.mypageTree.init();
					} else {
						selectSingleFolderWindow.tree.mypageTree.refresh();
					}
					
					break;
				case Constant.WORK_MYDEPT : // 부서 문서함
					treeOption.divId = "#singleFolderMydeptTree";
					treeOption.mapId = Constant.MAP_MYDEPT;
					treeOption.workType = Constant.WORK_MYDEPT;
					
					selectSingleFolderWindow.ui.activeTreeDiv("mydept");
					selectSingleFolderWindow.currentMapId = Constant.MAP_MYDEPT;
					selectSingleFolderWindow.currentWorkType = Constant.WORK_MYDEPT;
					
					if (selectSingleFolderWindow.tree.mydeptTree === null) {
						selectSingleFolderWindow.tree.mydeptTree = new XFTree(treeOption);
						selectSingleFolderWindow.tree.mydeptTree.init();
					} else {
						selectSingleFolderWindow.tree.mydeptTree.refresh();
					}
					break;
				case Constant.WORK_ALLDEPT : // 전사 문서함
					treeOption.divId = "#singleFolderAlldeptTree";
					treeOption.mapId = Constant.MAP_MYDEPT;
					treeOption.workType = Constant.WORK_ALLDEPT;
					
					selectSingleFolderWindow.ui.activeTreeDiv("alldept");
					selectSingleFolderWindow.currentMapId = Constant.MAP_MYDEPT;
					selectSingleFolderWindow.currentWorkType = Constant.WORK_ALLDEPT;
					
					if (selectSingleFolderWindow.tree.allDeptTree === null) {
						selectSingleFolderWindow.tree.allDeptTree = new XFTree(treeOption);
						selectSingleFolderWindow.tree.allDeptTree.init();
					} else {
						selectSingleFolderWindow.tree.allDeptTree.refresh();
					}
					break;
				case Constant.WORK_PROJECT : // 프로젝트 함
					treeOption.divId = "#singleFolderProjectTree";
					treeOption.mapId = Constant.MAP_PROJECT;
					treeOption.workType = Constant.WORK_PROJECT;
					
					selectSingleFolderWindow.ui.activeTreeDiv("project");
					selectSingleFolderWindow.currentMapId = Constant.MAP_PROJECT;
					selectSingleFolderWindow.currentWorkType = Constant.WORK_PROJECT;
					
					if (selectSingleFolderWindow.tree.projectTree === null) {
						selectSingleFolderWindow.tree.projectTree = new XFTree(treeOption);
						selectSingleFolderWindow.tree.projectTree.init();
					} else {
						selectSingleFolderWindow.tree.projectTree.refresh();
					}
					break;
					
					
				case "FREEDOC" : // 문서 상세
					treeOption.divId = "#singleFolderFreeDocTree";
					//treeOption.mapId = Constant.MAP_PROJECT;
					treeOption.workType = "FREEDOC";
					
					selectSingleFolderWindow.ui.activeTreeDiv("freedoc");
					//selectSingleFolderWindow.currentMapId = Constant.MAP_PROJECT;
					selectSingleFolderWindow.currentWorkType = Constant.WORK_FREEDOC;
					
					if (selectSingleFolderWindow.tree.freedocTree === null) {
						selectSingleFolderWindow.tree.freedocTree = new XFTree(treeOption);
						selectSingleFolderWindow.tree.freedocTree.init();
					} else {
						selectSingleFolderWindow.tree.freedocTree.refresh();
					}
					break;
					
				default :
					console.error("[selectSingleFolderWindow] workType : {0} 이 올바르지 않습니다. ".format(workType));
					break;
			}
		},
		getCurrentTree : function() {
			switch(selectSingleFolderWindow.currentWorkType) {
				case Constant.WORK_MYPAGE :
					return selectSingleFolderWindow.tree.mypageTree;
				case Constant.WORK_MYDEPT :
					return selectSingleFolderWindow.tree.mydeptTree;
				case Constant.WORK_ALLDEPT :
					return selectSingleFolderWindow.tree.allDeptTree;
				case Constant.WORK_PROJECT :
					return selectSingleFolderWindow.tree.projectTree;
				case Constant.WORK_FREEDOC :
					return selectSingleFolderWindow.tree.freedocTree;
				default :
					console.error("[selectSingleFolderWindow] workType : {0} 이 올바르지 않습니다. ".format(selectSingleFolderWindow.currentWorkType));
			}
		}
	},
	
	ui : {
		activeTreeDiv : function(activeDivId) {
			// 1. Tree Div
			var keys = Object.keys(selectSingleFolderWindow.treeDiv);
			$(keys).each(function(idx) {
				if (this == activeDivId) {
					$(selectSingleFolderWindow.treeDiv[this]).removeClass("hide");
				} else {
					$(selectSingleFolderWindow.treeDiv[this]).addClass("hide");
				}
			});
			
			var _title = $("#lb_singleFolderWorkspace");
			var _selectOption = $("#doc_folder_list");
			
			// 2. Titles
			if (activeDivId == "mypage") {
				_title.text("개인함");
				_selectOption.hide();
			} else {
				_title.text("문서함");
				_selectOption.show();
			}
		},
		activeTitle : function(activeDivId) {
			
		}
	},
	
	init : function(callback, mapId, workType, isValidation, docType) {
		
		selectSingleFolderWindow.open();
		selectSingleFolderWindow.callback = callback;
		selectSingleFolderWindow.currentMapId = mapId == undefined ? Constant.MAP_MYDEPT : mapId;
		selectSingleFolderWindow.currentWorkType = (workType == undefined || workType == null || workType == "null") ? Constant.WORK_MYDEPT : workType;
		selectSingleFolderWindow.isValidation = isValidation == undefined ? false : isValidation;
		selectSingleFolderWindow.docType = docType == undefined ? "" : docType;
		
		selectSingleFolderWindow.treeFunctions.initTree(selectSingleFolderWindow.currentWorkType);
				
		//검색 selectbox		
		exsoft.util.common.ddslick('#doc_folder_list', 'doc_folder_list', '', 262, function(divId,selectedData){
			// 콤보박스 이벤트 
			selectSingleFolderWindow.event.changeMap(selectedData.selectedData.value);
		});
	},
	//문서 상세에서 불려짐
	Detailinit : function(callback) {
		
		selectSingleFolderWindow.open();
		selectSingleFolderWindow.callback = callback;
		//selectSingleFolderWindow.currentMapId = mapId == undefined ? Constant.MAP_MYDEPT : mapId;
		selectSingleFolderWindow.currentWorkType = (workType == undefined || workType == null || workType == "null") ? Constant.WORK_MYDEPT : workType;
		selectSingleFolderWindow.isValidation = isValidation == undefined ? false : isValidation;
		//selectSingleFolderWindow.docType = docType == undefined ? "" : docType;
		
		selectSingleFolderWindow.treeFunctions.initTree(selectSingleFolderWindow.currentWorkType);
				
		//검색 selectbox		
		exsoft.util.common.ddslick('#doc_folder_list', 'doc_folder_list', '', 262, function(divId,selectedData){
			// 콤보박스 이벤트 
			selectSingleFolderWindow.event.changeMap(selectedData.selectedData.value);
		});
	},
	
	event : {
		changeMap : function(workType) {
			selectSingleFolderWindow.treeFunctions.initTree(workType);
		},
		
		submit : function() {
			var currentNode = selectSingleFolderWindow.treeFunctions.getCurrentTree().getCurrentNode();
			selectSingleFolderWindow.callback(currentNode);
			selectSingleFolderWindow.close();
		}
	},

	callback : {
		changedDocFolderList : function(divId, selectObject, arrParams) {
			selectSingleFolderWindow.event.changeMap(selectObject.selectedData.value);
		}
	}
}
		
