var gMultiFolder_list = new Array();	// 현재 선택된 폴더 목록
var gMultiFolder_isValidation = false;	// 유효성 검사 여부
var gMultiFolder_currentTreeObject;		// 현재 선택된 XFTree Object
var gMultiFolder_currentTreeDivID;		// 현재 선택된 XFTree Div ID
var gMultiFolder_map_id = "MYDEPT";		// 현재 선택된 맵

var selectMultiFolderWindow = {
		
		
	currentMapId : "",		// 현재 선택된 맵
	currentWorkType : "",	// 현재 선택된 부서함
	currentFolderID : "",	// 현재 선택된 폴더 ID
	
	isValidation : false,	// 유효성 검사 여부
	docType : "", 			// 문서 등록/이동 등등 연계 사용시 해당 문서의 Type
	callback : null,		// 확인버튼 클릭시 결과를 반환할 함수
	
	treeDiv : {
		mypage 	: "#multiFolderMypageTree",
		mydept 	: "#multiFolderMydeptTree",
		alldept : "#multiFolderAlldeptTree",
		project : "#multiFolderProjectTree"
	},
	
	open : function() {
		exsoft.util.layout.divLayerOpen("multifolder_choose_wrapper", "doc_multifolder_choose");
	},
	
	close : function() {
		exsoft.util.layout.divLayerClose("multifolder_choose_wrapper", "doc_multifolder_choose");
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
					treeOption.divId = selectMultiFolderWindow.treeDiv.mypage;// "#multiFolderMypageTree";
					treeOption.mapId = Constant.MAP_MYPAGE;
					treeOption.workType = Constant.WORK_MYPAGE;
					
					selectMultiFolderWindow.ui.activeTreeDiv("mypage");
					selectMultiFolderWindow.currentMapId = Constant.MAP_MYPAGE;
					selectMultiFolderWindow.currentWorkType = Constant.WORK_MYPAGE;
					
					if (selectMultiFolderWindow.tree.mypageTree == null) {
						selectMultiFolderWindow.tree.mypageTree = new XFTree(treeOption);
						// 3. 다중 선택 옵션 설정
						selectMultiFolderWindow.tree.mypageTree.template_multiCheck(false);
						selectMultiFolderWindow.tree.mypageTree.init();
					} else {
						selectMultiFolderWindow.tree.mypageTree.refresh();
					}
					
					gMultiFolder_currentTreeObject = selectMultiFolderWindow.tree.mypageTree;
					gMultiFolder_currentTreeDivID = selectMultiFolderWindow.treeDiv.mypage; //"MULTI_FOLDER_WORK_MYDEPT";
				
					break;
				case Constant.WORK_MYDEPT : // 부서 문서함
					treeOption.divId = selectMultiFolderWindow.treeDiv.mydept; //"#multiFolderMydeptTree";
					
					treeOption.mapId = Constant.MAP_MYDEPT;
					treeOption.workType = Constant.WORK_MYDEPT;
					
					selectMultiFolderWindow.ui.activeTreeDiv("mydept");
					selectMultiFolderWindow.currentMapId = Constant.MAP_MYDEPT;
					selectMultiFolderWindow.currentWorkType = Constant.WORK_MYDEPT;
					
					if (selectMultiFolderWindow.tree.mydeptTree == null) {
						selectMultiFolderWindow.tree.mydeptTree = new XFTree(treeOption);
						// 3. 다중 선택 옵션 설정
						selectMultiFolderWindow.tree.mydeptTree.template_multiCheck(false);
						selectMultiFolderWindow.tree.mydeptTree.init();

					} else {
						selectMultiFolderWindow.tree.mydeptTree.refresh();
					}

					gMultiFolder_currentTreeObject = selectMultiFolderWindow.tree.mydeptTree;
					gMultiFolder_currentTreeDivID = selectMultiFolderWindow.treeDiv.mydept; //"MULTI_FOLDER_WORK_MYDEPT";
					
					break;
				case Constant.WORK_ALLDEPT : // 전사 문서함
					treeOption.divId = selectMultiFolderWindow.treeDiv.alldept;// "#multiFolderAlldeptTree";
					treeOption.mapId = Constant.MAP_MYDEPT;
					treeOption.workType = Constant.WORK_ALLDEPT;
					
					selectMultiFolderWindow.ui.activeTreeDiv("alldept");
					selectMultiFolderWindow.currentMapId = Constant.MAP_MYDEPT;
					selectMultiFolderWindow.currentWorkType = Constant.WORK_ALLDEPT;
					
					if (selectMultiFolderWindow.tree.allDeptTree == null) {
						selectMultiFolderWindow.tree.allDeptTree = new XFTree(treeOption);
						// 3. 다중 선택 옵션 설정
						selectMultiFolderWindow.tree.allDeptTree.template_multiCheck(false);
						selectMultiFolderWindow.tree.allDeptTree.init();
					} else {
						selectMultiFolderWindow.tree.allDeptTree.refresh();
					}

					gMultiFolder_currentTreeObject = selectMultiFolderWindow.tree.allDeptTree;
					gMultiFolder_currentTreeDivID = selectMultiFolderWindow.treeDiv.alldept;
					// 3. 다중 선택 옵션 설정
					gMultiFolder_currentTreeObject.template_multiCheck(false);
					break;
				case Constant.WORK_PROJECT : // 프로젝트 함
					treeOption.divId = selectMultiFolderWindow.treeDiv.project; //"#multiFolderProjectTree";
					treeOption.mapId = Constant.MAP_PROJECT;
					treeOption.workType = Constant.WORK_PROJECT;
					
					selectMultiFolderWindow.ui.activeTreeDiv("project");
					selectMultiFolderWindow.currentMapId = Constant.MAP_PROJECT;
					selectMultiFolderWindow.currentWorkType = Constant.WORK_PROJECT;
					
					if (selectMultiFolderWindow.tree.projectTree == null) {
						selectMultiFolderWindow.tree.projectTree = new XFTree(treeOption);
						// 3. 다중 선택 옵션 설정
						selectMultiFolderWindow.tree.projectTree.template_multiCheck(false);
						selectMultiFolderWindow.tree.projectTree.init();
					} else {
						selectMultiFolderWindow.tree.projectTree.refresh();
					}

					gMultiFolder_currentTreeObject = selectMultiFolderWindow.tree.projectTree;
					gMultiFolder_currentTreeDivID = selectMultiFolderWindow.treeDiv.project;
					
					break;
					
				default :
					console.error("[selectMultiFolderWindow] workType : {0} 이 올바르지 않습니다. ".format(workType));
					break;
			}
		},
		getCurrentTree : function() {
			switch(selectMultiFolderWindow.currentWorkType) {
				case Constant.WORK_MYPAGE :
					return selectMultiFolderWindow.tree.mypageTree;
				case Constant.WORK_MYDEPT :
					return selectMultiFolderWindow.tree.mydeptTree;
				case Constant.WORK_ALLDEPT :
					return selectMultiFolderWindow.tree.allDeptTree;
				case Constant.WORK_PROJECT :
					return selectMultiFolderWindow.tree.projectTree;
				default :
					console.error("[selectMultiFolderWindow] workType : {0} 이 올바르지 않습니다. ".format(selectMultiFolderWindow.currentWorkType));
			}
		}
	},
	
	ui : {
		activeTreeDiv : function(activeDivId) {
			// 1. Tree Div
			var keys = Object.keys(selectMultiFolderWindow.treeDiv);
			$(keys).each(function(idx) {
				if (this == activeDivId) {
					$(selectMultiFolderWindow.treeDiv[this]).removeClass("hide");
				} else {
					$(selectMultiFolderWindow.treeDiv[this]).addClass("hide");
				}
			});
			
			var _title = $("#lb_multiFolderWorkspace");
			var _selectOption = $("#doc_multifolder_list");
			
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
		
		selectMultiFolderWindow.open();
		selectMultiFolderWindow.callback = callback;
		selectMultiFolderWindow.currentMapId = mapId == undefined ? Constant.MAP_MYDEPT : mapId;
		selectMultiFolderWindow.currentWorkType = (workType == undefined || workType == null || workType == "null") ? Constant.WORK_MYDEPT : workType;
		selectMultiFolderWindow.isValidation = isValidation == undefined ? false : isValidation;
		selectMultiFolderWindow.docType = docType == undefined ? "" : docType;

		selectMultiFolderWindow.treeFunctions.initTree(selectMultiFolderWindow.currentWorkType);
				
		//검색 selectbox
		exsoft.util.common.ddslick('#doc_folder_list', 'doc_folder_list', '', 262, function(divId,selectedData){
			// 콤보박스 이벤트 
			selectMultiFolderWindow.event.changeMap(selectedData.selectedData.value);
		});
		
		// 초기화 :: 선택폴더 및 전달 객체 초기화
		$('#multiFolderSelectedfolderList').empty();		
		gMultiFolder_list = [];
	},
	
	event : {

		submit : function() {
			if (gMultiFolder_list.length == 0)	{
				jAlert('폴더를 선택하세요.', "확인", 0);				
				return false;
			} else {
				selectMultiFolderWindow.close();
				selectMultiFolderWindow.callback(gMultiFolder_list);
				//exsoft.util.common.popDivLayerClose("folder_choose_wrapper");
			}/*
			var currentNode = selectMultiFolderWindow.treeFunctions.getCurrentTree().getCurrentNode();			
			selectMultiFolderWindow.callback(currentNode);
			
			selectMultiFolderWindow.close();*/
		},
	
		changeMap : function(workType) {
			selectMultiFolderWindow.treeFunctions.initTree(workType);
		},
		
		// 목록에 folderId가 추가되어있는지 체크를 한다
		multiFolderIsExistFolder : function(folderId) {
			var isExists = false;
			$("#multiFolderSelectedfolderList input[type='checkbox']").each(function() {
				if ($(this).val() == folderId) {
					isExists = true;
					return false; // each문 종료
				}
			})
			return isExists;
		},
		
		// 선택된 폴더를 목록에 추가
		multiFolderAddFolder : function() {
			var isValid = false;
			var buffer="";
			buffer = "<ul>";
			// 트리에서 선택한 목록을 for문 처리
			$(exsoft.util.common.getReturnTreeObject(gMultiFolder_currentTreeObject, exsoft.util.common.getIdFormat(gMultiFolder_currentTreeDivID), selectMultiFolderWindow.currentMapId)).each(function(index) {
				// 중복이 아닐경우만 리스트에 추가를 한다
				if (!selectMultiFolderWindow.event.multiFolderIsExistFolder(this.id)) {
					
					// 1. 유효성 검사
					if (selectMultiFolderWindow.isValidation) {
						
						
						var nodeDetail = this.original;
						
						// 1-1. 폴더에 문서를 등록할수 있는지 체크 (is_save)
						if (nodeDetail.is_save != "Y") {
							jAlert("\"{0}\" 폴더에는 문서를 등록할 수 없습니다.".format(this.text));
							isValid = true;
						}
						
						// 1-2. 폴더에 문서를 등록할수 있는 권한이 있는지 체크 (acl_document_create)
						if (nodeDetail.map_id != "MYPAGE" && nodeDetail.acl_document_create != "T") {
							jAlert("\"{0}\" 폴더에 권한이 없습니다".format(this.text));
							isValid = true;
						}
						
						// 1-3. 문서 유형이 동일한지 체크(단, ALL_TYPE은 FOLDER의 TYPE과 관련없이 모두 저장이 가능)
						if(selectMultiFolderWindow.docType != "ALL_TYPE")	{
							if ((selectMultiFolderWindow.docType != nodeDetail.is_type) && nodeDetail.is_type != "ALL_TYPE") {
								jAlert("문서유형이 맞지 않습니다");
								isValid = true;
							}
						}
						
						// 1-#. 유효성 검사에 걸리는 항목이 있을 경우
						if (isValid) {
							// 체크 해제
		 	 				$(gMultiFolder_currentTreeObject.divId).jstree("deselect_node", this.id);
							
							// 이벤트 종료
							return false;
						}
					}
						
					// 유효성 검사 실패시
					if (isValid) {
						return;
					}
					
					// 1. 화면에 추가
					buffer += "<li><input type=\"checkbox\" value=\"{0}\"/>".format(this.id)+"<div>"+this.fullPath.join(' / ')+"</div></li>";
				
					// 2. Array에 추가
					gMultiFolder_list.push(this);
					
				}
			});
			
			buffer += "</ul>";
			$("#multiFolderSelectedfolderList").append(buffer);
		},
				
		// 목록에서 선택된 폴더를 제거
		multiFolderRemoveFolder : function() {
			$("#multiFolderSelectedfolderList input[type='checkbox']:checked").each(function() {
				// 1. 화면에서 삭제
				$(this).closest("li").remove();
				
				// 2. Array에서 삭제
				multiFolderRemoveArrayItem(gMultiFolder_list, "id", $(this).val());
				
			});
		},
		/**
		 * 선택된 폴더 목록에서 특정 폴더ID(value)를 찾아서 삭제한다
		 *
		 * @param arrays - 선택해서 추가한 폴더 목록
		 * @param key - 찾을 Key값
		 * @param value - 비교할 value 
		 **/
		multiFolderRemoveArrayItem : function(arrays, key, value) {
			
			$(arrays).each(function(index) {
				if(this[key] == value) {
					arrays.remove(this);
					return false; // each문 종료
				}
			});
		},
		
	},//event END

	callback : {
		changedDocFolderList : function(divId, selectObject, arrParams) {
			selectMultiFolderWindow.event.changeMap(selectObject.selectedData.value);
		}
	}
}
		
