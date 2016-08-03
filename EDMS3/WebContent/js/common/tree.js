/******************************************************************************************************
 * @since 2014/07
 * @author 패키지 개발팀
 * @version 1.1.0.0
 * 
 * ver 1.1
 * - parameter전달 방식을 json으로 변경
 * - contextAction 추가
 *****************************************************************************************************/

//--------------------------------
// eXsoft Framework Tree Object
//--------------------------------
//var XFTree = function(divId, context, url, mapId, workType, manageGroupId){
var XFTree = function(opt){
	
	// 구버전 var XFTree = function(divId, context, url, mapId, workType, manageGroupId)
	//----------------------------------------------------------------
	// 1.Default variable
	//----------------------------------------------------------------
	// divId : 트리를 그릴 DIV의 ID
	// url : 트리가 데이터를 요청할 서버 Url
	// selectedNode : 체크 박스로 체크된 전체 노드 배열
	// rootId : 특정위치의 그룹이나 폴더를 Tree의 Root로 지정하고 싶을 경우 최상단의 ID를 설정
	// isBasicTree : 기본형태의 트리인지 체크. 기본형식이 아니라면 최초 로드 시 강제 선택을 빼야함.
	//----------------------------------------------------------------
	this.divId = opt.divId;
	this.url = opt.context + opt.url;
	this.contextRoot = opt.context;
	this.selectedNode = new Array();
	this.selectedNodeData = new Array();
	this.rootId = opt.manageGroupId == null ? null : opt.manageGroupId;
	this.mapId = opt.mapId == null ? "MYDEPT" : opt.mapId;
	this.workType = opt.workType == null ? "WORK_MYDEPT" : opt.workType; //WORK_MYDEPT(부서), WORK_ALLDEPT(전사), WORK_PROJECT(프로젝트)
	this.isBasicTree = true;
	this.refreshNodeId = "";
	this.refreshNodeParentId = "";
	this.isFavoriteFolder = false;	// Context 메뉴 분기처리를 위한 변수
	this.onlyVirtual = opt.onlyVirtual == null ? null : opt.onlyVirtual;
	this.contextAction = opt.contextAction;
	this.isSelectHiddenRoot = opt.isSelectHiddenRoot == null ? false : opt.isSelectHiddenRoot;
	this.treeType = opt.treeType != "" ? opt.treeType : ""; // 그룹관리 > 구성원추가시 미소속그룹을 보이기위한 변수
	
	//----------------------------------------------------------------
	// 2.Overloading functions & options
	//----------------------------------------------------------------
	// plugins : 트리에 적용할 플러그인 배열
	// isMultiplecheck : 체크박스 플러그인 사용시 다중 체크 여부
	//----------------------------------------------------------------
	this.plugins = ["wholerow","types"];
	this.isMultipleCheck = false;

	//----------------------------------------------------------------
	// 3.Overloading default functions
	//----------------------------------------------------------------
	// dataFunction : core::data::data의 오버라이딩 구현부분
	// filterFunction : core::data::dataFilter의 오버라이딩 구현부분
	// contextMenuFunction : contextmenu의 오버라이딩 구현부분
	// checkBoxOptions : checkbox의 오버라이딩 구현부분
	// callbackOpenNode : open_node.jstree의 오버라이딩 구현부분
	// callbackSelectNode : select_node.jstree의 오버라이딩 구현부분
	//----------------------------------------------------------------
	this.dataFunction = null;

	this.filterFunction = function(jsonString) {
		var items = [];
		var msg = JSON.parse(jsonString);
		var groupList = msg["groupList"];
		var folderList = msg["folderList"]; //FolderVO List 객체
		var favoriteList = msg["favoriteList"];

		if (groupList != null) {

			$.each(groupList, function(i) {
				var node = groupList[i];

				var item = {
		    		id : node.group_id,
		    		parent : node.parent_id == "" ? "#" : node.parent_id,
		    		text : node.group_name_ko,
		    		children : node.children_count > 0 ? true : false,
		    		childrenCnt : node.children_count,
		    		type : "group",
		    		contextRoot : opt.context
				};
				items.push(item);
			});

		} else if (folderList != null) {
			$.each(folderList, function(i) {
				var node = folderList[i];
				var folderType = "default";

				if (node.folder_type == "MYDEPT") {
					folderType = "group_folder";
				} else {
					if (node.folder_status == "D") {
						folderType = "inactive";
					} else {
						folderType = "default";
					}
				}

				var item = {
		    		id : node.folder_id,
		    		parent : node.parent_id == "" ? "#" : node.parent_id,
		    		text : node.folder_name_ko,
		    		children : node.children_count > 0 ? true : false,
		    		acl_id : node.acl_id,
		    		acl_level : node.acl_level,
		    		acl_create : node.acl_create,
		    		acl_changePermission : node.acl_changePermission,
		    		acl_document_create : node.acl_document_create,
		    		is_save : node.is_save,
		    		is_type : node.is_type,
		    		childrenCnt : node.children_count,
		    		map_id : node.map_id,
		    		contextRoot : opt.context,
		    		type : folderType,
		    		is_groupFolder : node.folder_type == "MYDEPT" ? true : false
				};
				items.push(item);
			});
		} else if (favoriteList != null) {
			$.each(favoriteList, function(i) {
				var node = favoriteList[i];
				var item = {
					id : node.folder_id,
					parent : node.parent_folder_id == node.folder_id ? "#" : node.parent_folder_id,
					text : node.favorite_nm,
					children : node.children_count > 0 ? true : false,
					childrenCnt : node.children_count,
					is_virtual : node.is_virtual,
					type : node.is_virtual == "Y" ? "virtual" : "link",
					contextRoot : opt.context,
					sorts : node.sorts
				};
				items.push(item);
			});
		}

		return JSON.stringify(items);
	};

	this.contextMenuFunction = function(node) {

		// 권한 레벨 계산하는 function 만든다.
		var isAuthCheck = function(acl_level, type) {
			if( acl_level == 'DELETE' && type == 'delete') {
				return true;
			} else if( (acl_level == 'DELETE' || acl_level == 'UPDATE') && (type == 'update' || type == 'move')) {
				return true;
			}
			return false;
		};

		node.full_path = this.get_path(node.id);
		return {

			createFolder : {
				separator_before	: false,
				separator_after		: false,
				_disabled			: node.original.acl_create == 'F' ? false : false, // 주의 :: false가 사용할 수 있음
				label				: "폴더 생성",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_write.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.createFolder !== undefined) {
						opt.contextAction.createFolder(node); // 호출한 페이지에서 구현 되어 있어야 함
					} else {
						jAlert("폴더 생성 콜백함수가 구현되있지 않습니다.");
						return;
					}

				}
			},
			modifyFolder : {
				separator_before	: false,
				separator_after		: false,
				_disabled			: !isAuthCheck(node.original.acl_level, 'update'), // true : 메뉴비활성화, false : 메뉴 활성화 하여 !조건 추가
				label				: "폴더 수정",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_modify.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.modifyFolder !== undefined) {
						opt.contextAction.modifyFolder(node); // 호출한 페이지에서 구현 되어 있어야 함
					} else {
						jAlert("폴더 수정 콜백함수가 구현되있지 않습니다.");
						return;
					}
				}
			},
			moveFolder : {
				separator_before	: false,
				separator_after		: false,
				_disabled			: !isAuthCheck(node.original.acl_level, 'move'), // true : 메뉴비활성화, false : 메뉴 활성화 하여 !조건 추가
				label				: "폴더 이동",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_move.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.moveFolder !== undefined) {
						opt.contextAction.moveFolder(node); // 호출한 페이지에서 구현 되어 있어야 함
					} else {
						jAlert("폴더 이동 콜백함수가 구현되있지 않습니다.");
						return;
					}
				}
			},
			deleteFolder : {
				separator_before	: false,
				separator_after		: false,
				_disabled			: !isAuthCheck(node.original.acl_level, 'delete'), // true : 메뉴비활성화, false : 메뉴 활성화 하여 !조건 추가
				label				: "폴더 삭제",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_delete.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.deleteFolder !== undefined) {
						opt.contextAction.deleteFolder(node);
					} else {
						jAlert("폴더 삭제 콜백함수가 구현되있지 않습니다.");
						return;
					}
				}
			},
			addFavoriteFolder : {
				separator_before	: true,
				separator_after		: false,
				_disabled			: false,
				label				: "폴더 즐겨찾기 추가",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_favorite_add.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.addFavoriteFolder !== undefined) {
						opt.contextAction.addFavoriteFolder(node); // 호출한 페이지에서 구현 되어 있어야 함
					} else {
						jAlert("폴더 즐겨찾기 추가 콜백함수가 구현되있지 않습니다.");
						return;
					}
				}
			}
		};
	};

	this.contextMenuForFavorite = function(node) {

		// 권한 레벨 계산하는 function 만든다.
		var isAuthCheck = function(acl_level, type) {

			if( acl_level == 'DELETE' && type == 'delete') {
				return true;
			} else if( (acl_level == 'DELETE' || acl_level == 'UPDATE') && (type == 'update' || type == 'move')) {
				return true;
			}

			return false;
		};

		node.full_path = this.get_path(node.id);

		return {
			createFavoriteFolder : {
				separator_before	: false,
				separator_after		: false,
				_disabled			: false,
				label				: "등록",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_write.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.createFavoriteFolder !== undefined) {
						opt.contextAction.createFavoriteFolder(node); // 호출한 페이지에서 구현 되어 있어야 함
					} else {
						jAlert("즐겨찾기 생성 콜백함수가 구현되있지 않습니다.");
						return;
					}
				}
			},
			modifyFavoriteFolder : {
				separator_before	: false,
				separator_after		: false,
				_disabled			: false,
				label				: "이름 변경",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_modify.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.modifyFavoriteFolder !== undefined) {
						opt.contextAction.modifyFavoriteFolder(node); // 호출한 페이지에서 구현 되어 있어야 함
					} else {
						jAlert("즐겨찾기 이름 변경 콜백함수가 구현되있지 않습니다.");
						return;
					}
				}
			},
			moveFavoriteFolder : {
				separator_before	: false,
				separator_after		: false,
				_disabled			: false,
				label				: "폴더 이동",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_move.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.moveFavoriteFolder !== undefined) {
						opt.contextAction.moveFavoriteFolder(node); // 호출한 페이지에서 구현 되어 있어야 함
					} else {
						jAlert("즐겨찾기 폴더 이동 콜백함수가 구현되있지 않습니다.");
						return;
					}
				}
			},
			deleteFavoriteFolder : {
				separator_before	: false,
				separator_after		: false,
				_disabled			: false,
				label				: "폴더 삭제",
				icon				: "{0}/js/plugins/jstree/img/tree/context_folder_delete.png".format(opt.context),
				action				: function(data) {
					if (opt.contextAction !== undefined && opt.contextAction.deleteFavoriteFolder !== undefined) {
						opt.contextAction.deleteFavoriteFolder(node); // 호출한 페이지에서 구현 되어 있어야 함
					} else {
						jAlert("즐겨찾기 폴더 삭제 콜백함수가 구현되있지 않습니다.");
						return;
					}
				}
			}
		};
	};

	this.checkBoxOptions = {

			visible 			: true,		// 체크박스 숨김여부 	[true : 보임, false : 숨김]
			three_state 		: false,	// 하위 폴더 자동 체크	[true : 자동체크, false : 개별체크]
			whole_node 			: false,	// 토글시 체크칸을 정확히 클릭해야 해제 가능
			keep_selected_style : false,
			cascade 			: "up+down+undetermined",
			two_state			: true,
			tie_selection		: true,
	};

	this.callbackAllSelectNode = function(selectedNode) {};

	this.callbackAllSelectNodeData = function(selectedNodeData) {};

	this.callbackLoadNode = function(e, data) {};

	this.callbackBeforeOpenNode = function(e, data) {};

	this.callbackOpenNode = function(e, data) {};

	this.callbackCloseNode = function(e, data) {}

	this.callbackSelectNode = function(e, data) {};

	//==================================================================================================================
	// 4.Template Methods
	//==================================================================================================================
	// template_context : 우클릭 메뉴를 가진 트리 옵션을 추가한다
	// template_singleCheck : 단일 체크가 가능한 트리 옵션을 추가한다
	// template_multiCheck(checkSubFolder) : 다중 선택이 가능한 트리 옵션을 추가한다
	//                     checkSubFolder : true = 부모 폴더 체크 시 하위 폴더 전체를 체크한다
	//                                     false = 부모 폴더 체크 시 하위 폴더에 영향을 주지 않는다
	//==================================================================================================================

	this.template_context = function() {

		// 1. plugin 추가
		this.plugins.push("contextmenu");

	};

	this.template_singleCheck = function() {
		this.plugins.push("checkbox");
		this.isMultipleCheck = false;
		this.isBasicTree = false;
		this.checkBoxOptions.cascade = "up";
	};

	this.template_multiCheck = function(checkSubFolder) {
		this.plugins.push("checkbox");
		this.isMultipleCheck = true;
		this.isBasicTree = false;
		this.checkBoxOptions.three_state = checkSubFolder;
		this.checkBoxOptions.whole_node = true;
		this.checkBoxOptions.cascade = "up"; /* jsTree 3.0.0.9 버전에 맞춰서 추가*/
	};

	//==================================================================================================================
	// 5.Util Methods
	//==================================================================================================================
	// refresh : 트리 객체를 갱신한다
	// destroy : 트리 객체를 제거한다
	// refreshNode(nodeId) : 특정 노드를 갱신한다
	//           - nodeId : 갱신할 nodeId, null일 경우 현재 선택된 Node를 갱신함
	//==================================================================================================================

	this.reset = function() {
		$(exsoft.util.common.getIdFormat(this.divId)).jstree().refresh();
		$(exsoft.util.common.getIdFormat(this.divId)).jstree("deselect_all");
		this.selectedNode = new Array();
		this.selectedNodeData = new Array();
	}

	this.refresh = function() {
		$(this.divId).jstree().refresh();
		return true;
	}

	this.destroy = function() {
		$(this.divId).jstree("destroy");
	}

	this.expandNode = function(nodeId) {
		$(this.divId).jstree().open_node(nodeId);
		return nodeId;
	}

	this.expandNodeCallBack = function(nodeId, callback) {
		$(this.divId).jstree().open_node(nodeId, callback);
	}

	this.selectNode = function(nodeId) {
		$(this.divId).jstree("select_node", nodeId);
	}

	this.selectNodeForInvoke = function(nodeId) {
		$(this.divId).jstree("deselect_node", nodeId);
		$(this.divId).jstree("select_node", nodeId);
	}

	this.refreshNode = function(nodeId) {
		if(nodeId == undefined) {
			nodeId = $(this.divId).jstree("get_selected");
		}

		// 갱신하려는 노드의 정보를 가져온다
		var nodeObj = $(this.divId).jstree("get_node", nodeId);

		// 갱신하려는 노드의 부모폴더 정보를 가져온다
		var parentId = nodeObj.parent == "#" ? nodeId : nodeObj.parent;
		var parentObj = $(this.divId).jstree("get_node", parentId);

		// 부모 노드가 Root일 경우 refresh()만 한다.
		if (parentObj.parent == "#") {
			$(this.divId).jstree().refresh();
		} else {
			$(this.divId).jstree("refresh_node", parentId);
		}

		// 갱신하려는 아이디를 기억해둔다
		this.refreshNodeId = nodeId;
		this.refreshNodeParentId = parentId;

	}

	this.refreshNodeForAddChildren = function(nodeId) {
		// param이 null일 경우는 없으나 혹시나 하는 마음에 처리함.
		if (nodeId == undefined) {
			nodeId = this.getCurrentNodeParentId();
		}
		var node = $(this.divId).jstree("get_node", nodeId);

		// nodeId가 refresh하려는 Tree Object에 아직 load되지 않았을 경우 로직 종료
		if (node == false) {
			return;
		}

		// 자식 Node가 없을 경우만 임시 폴더를 추가하여 expend 가능상태로 만든다
		if (node.original.childrenCnt == 0) {
			$(this.divId).jstree().create_node(nodeId);
			node.original.childrenCnt++;
		}

		// 트리가 expend있는지 체크해서 collapsed일 경우만 토글하여 expend 한다
		if (!node.state.opened) {
			$(this.divId).jstree().toggle_node(nodeId);
		}

		$(this.divId).jstree("refresh_node", nodeId);
	}

	this.refreshCurrentNode = function(nodeId) {
		if(nodeId == undefined) {
			return true;
		}

		// 부모 노드가 Root일 경우 refresh()만 한다.
		if (nodeId == "#") {
			$(this.divId).jstree().refresh();
		} else {
			$(this.divId).jstree("refresh_node", nodeId);
		}

	}

	this.getCurrentNode = function(isDOM) {
		// isDOM is true : return DOM Object
		// isDOM is false or undefined : return jsTree node object
		isDOM = isDOM == undefined ? false : isDOM;

		var current_id = $(this.divId).jstree("get_selected");
		var current_node_object = $(this.divId).jstree("get_node", current_id, isDOM);
		return current_node_object;
	}
	this.getCurrentNodeById = function(current_id) {
		var current_node_object = $(this.divId).jstree("get_node", current_id);
		return current_node_object;
	}
	this.getCurrentNodeIds = function() {
		return $(this.divId).jstree("get_selected");
	}
	this.getCurrentNodeId = function() {
		var cId = $(this.divId).jstree("get_selected");
		var cNode = $(this.divId).jstree("get_node", cId);  // 첫번째 배열 값 id 리턴
		return cNode.id;
	}
	this.getCurrentNodeName = function() {
		var cId = $(this.divId).jstree("get_selected");
		var cNode = $(this.divId).jstree("get_node", cId);
		return cNode.text;
	}
	this.getCurrentNodeFullPath = function() {
		var currentId = $(this.divId).jstree("get_selected");
		return $(this.divId).jstree("get_path", currentId);
	}
	this.getCurrentNodeFullPathIds = function() {
		var currentId = $(this.divId).jstree("get_selected");
		return $(this.divId).jstree("get_path", currentId, "", true);
	}
	this.getCurrentNodeParentId = function() {
		var currentNodeId = this.getCurrentNodeId();
		var parentId;

		try {
			// 현재 선택된 노드 ID가 있을경우 진행함
			if (currentNodeId != undefined) {
				parentId = $(this.divId).jstree("get_node", currentNodeId).parent;

				return parentId;
			}
		} finally {
			currentNodeId = null;
			parentId = null;
		}
	}
	this.setInitSelectById = function(nodeId) {
		$(this.divId).jstree("deselect_all");
		$(this.divId).jstree("select_node", nodeId);
	}

	this.unChecked = function(nodeId) {

	}

	this.is_loaded = function(obj){
		return $(this.divId).jstree("is_loaded", obj);
	}

	this.is_open = function(obj){
		return $(this.divId).jstree("is_open", obj);
	}

	// 이동할 수 있는 형제 노드가 있는지 체크
	this.SiblingMovable = function() {
		var curNodeDOM = this.getCurrentNode(true);
		return {
			prevSibling : $(this.divId).jstree()._previousSibling(curNodeDOM[0]),
			nextSibling : $(this.divId).jstree()._nextSibling(curNodeDOM[0])
		};
	}

	// 현재 노드의 이동 가능한 범위를 구한다 (형제 노드의 Start ~ End index를 구함)
	this.getMoveRange = function() {
		var info = {
				range : new Array(),	// 선택한 노드의 형제노드 index 목록
				pointer : -1			// 선택한 노드의 Index
		}

		var curNode = this.getCurrentNode();
		var curAllNode = this.getAllNodeToJstree();

		$(curAllNode).each(function(i) {
			// 형제 노드일 경우 인덱스 정보 추가
			if (curNode.parent == this.parent)
				info.range.push({node : this, index : i});

			// 선택한 노드의 인덱스
			if (curNode.id == this.id)
				info.pointer = i;
		})

		return info;
	}

	// 형제 노드의 순서중 현재 노드의 POS(index)를 구한다
	this.getNodeIndex = function() {
		var index = -1;
		var curNode = this.getCurrentNode();
		var curAllNode = this.getAllNodeToJstree();

		$(curAllNode).each(function(i) {

			// 1. '즐겨찾기' 폴더가 아님폴더 중 형제 폴더들을 카운팅함
			if (this.parent != this.id && this.parent == curNode.parent) {
				index++;

				// 2. 자기 자신이 나올경우 인덱스를 반환
				if (this.id == curNode.id) {
					return false;
				}
			}
		})
		return index;
	}

	// 모든 Node를 구한다 (DOM)
	this.getAllNodeToDOM = function() {
		var curContainer = $(this.divId).jstree().get_container();
		return curContainer.find("li");
	}

	// 모든 Node를 구한다 (Tree Nodes)
	this.getAllNodeToJstree = function() {
		var treeObj = this;
		var nodeArrays = new Array();
		var curContainer = $(this.divId).jstree().get_container();

		$(curContainer.find("li")).each(function(index) {
			nodeArrays.push(treeObj.getCurrentNodeById(this.id));
		})
		return nodeArrays;
	}

	// 선택한 노드부터 최상위 노드까지 폴더 ID 목록
	this.getParentIdList = function(nodeId) {
		return $(this.divId).jstree("get_path", nodeId, "", true);
	}
	
	// 노드의 Text를 변경한다
	this.setNodeText = function(nodeId, str) {
		$(this.divId).jstree("set_text", nodeId, str);
	}

	this.moveToPrev = function(callback) {
		// 1. 이동이 가능한지 체크한다
		var siblings = this.SiblingMovable();
		if  (siblings.prevSibling == null) {
			jAlert("현재 폴더를 위로 이동할 수 없습니다.");
			return;
		}
		var tempDivId = this.divId;
		var curNode = this.getCurrentNode();
		var curNodeIndex = this.getNodeIndex();
		var prevNode = $(this.divId).jstree("get_node", siblings.prevSibling);
		var prevNodeIndex = curNodeIndex-1;

		var jsonObject = {
				sourceFolderId : curNode.id,
				sourceFolderIndex : prevNodeIndex,
				targetFolderId : prevNode.id,
				targetFolderIndex : curNodeIndex,
				type : "SWAP_INDEX"
		}

		exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, this.contextRoot + "/folder/favoriteControl.do" , "SWAP_INDEX", function(param, data) {
			$(tempDivId).jstree().move_node(curNode.id, curNode.parent, curNodeIndex-1);
		});

	}

	this.moveToNext = function() {
		// 1. 이동이 가능한지 체크한다
		var siblings = this.SiblingMovable();
		if  (siblings.nextSibling == null) {
			jAlert("현재 폴더를 아래로 이동할 수 없습니다.");
		}

		var tempDivId = this.divId;
		var curNode = this.getCurrentNode();
		var curNodeIndex = this.getNodeIndex();
		var nextNode = $(this.divId).jstree("get_node", siblings.nextSibling);
		var nextNodeIndex = curNodeIndex+1;

		var jsonObject = {
				sourceFolderId : curNode.id,
				sourceFolderIndex : nextNodeIndex,
				targetFolderId : nextNode.id,
				targetFolderIndex : curNodeIndex,
				type : "SWAP_INDEX"
		}

		exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, this.contextRoot + "/folder/favoriteControl.do" , "SWAP_INDEX", function(param, data) {
			$(tempDivId).jstree().move_node(curNode.id, curNode.parent, curNodeIndex+2);
		});

	}

	// Node.original.childCnt를 -1 한다
	this.removeChildCnt = function(nodeId) {
		var node = $(this.divId).jstree("get_node", nodeId);

		node.original.childrenCnt--;

		// 자식이 없을경우 state.opened를 false로 변경
		if (node.original.childrenCnt == 0) {
			node.state.opened = false;
		}
	}

	// 노드 아이콘 갱신
	this.updateNodeIcon = function(nodeId) {

		var defaultType = $(this.divId).jstree("get_type", nodeId).replace("_open", "");

		var openState = $(this.divId).jstree("get_node", nodeId).state.opened;

		if (openState) {
			$(this.divId).jstree("set_type", nodeId, "{0}_open".format(defaultType));
		} else {
			$(this.divId).jstree("set_type", nodeId, "{0}".format(defaultType));
		}
	}

	// 선택한 폴더의 소속 부서를 조회한다
	this.getFolderGroupId = function(nodeId) {
		var returnObj = null;
		var div = this.divId;
		// nodeId가 null일경우 현재 선택된 Node의 ID를 설정함.
		if (nodeId == null) {
			nodeId = this.getCurrentNodeId();
		}

		// 전체 경로(id)를 역순으로 가져온다
		var parentList = $(div).jstree("get_path", nodeId, "", true).reverse();

		// 트리 조회
		$(parentList).each(function(index) {

			var node = $(div).jstree("get_node", this);

			// [업무문서함] || [프로젝트함] 케이스에 따라 소속부서(프로젝트)를 가져오는 로직을 분기함
			if (node.original.map_id == "MYDEPT") {
				if (node.original.is_groupFolder) {
					returnObj = node;
					return false; // Break
				}
			} else if (node.original.map_id == "PROJECT") {
				if (parentList.length == index+2) {
					returnObj = node;
					return false;
				}
			}

		});

		return returnObj;
	}
	//==================================================================================================================
	// 6.Initialize JS Tree
	//==================================================================================================================
	this.init = function() {

		//--------------------------------
		// 6-1.localize tree object
		//--------------------------------
		var currentTree = this;

		//--------------------------------
		// 6-2.Validation functions & options
		//--------------------------------

		// **. div의 ID에 '#'이 없을 경우 앞에 붙여준다. (없을 경우 Jquery의 Object 접근 방식에 맞지 않아서 오류남)
		if (this.divId.indexOf("#") == -1) {
			this.divId = "#" + this.divId;
		}

		// 1. Node정보를 가져올 url
		if (this.url == null) {
			jAlert("대상 Url은 꼭 필요합니다.");
			return;
		}

		//----------------------------------------------------------------
		// 6-3.Create JS Tree
		//----------------------------------------------------------------
		// core::multiple : 여러개의 Node를 체크할수 있는지 여부 (true : 다중 체크, false : 단일체크)
		// core::data::url : 요청을 보낼 url
		// core::data::type : 전송 방식 GET, POST
		// core::data::data : 전송 파라메터 구성 부분
		// core::data::dataFilter : 응답받은 Response를 조작하여 트리 구조에 맞게 변경하는 부분
		// contextmenu::items : 컨텍스트 메뉴의 아이템 객체
		// checkbox : 체크박스 옵션 객체
		// plugins : 플러그인 옵션 배열 객체
		//----------------------------------------------------------------
		$(function () {
			$(currentTree.divId).jstree({
				core : {
					multiple : currentTree.isMultipleCheck,
					data : {
						url : currentTree.url,
						type : "POST",
						data : currentTree.dataFunction != null ?
								currentTree.dataFunction :
								function(currentNode) {
									return {
										map_id : currentTree.mapId,
										root_id : currentTree.rootId,
										parent_id : currentNode.id == "#" ? null : currentNode.id,
										work_Type : currentTree.workType,
										only_virtual : currentTree.onlyVirtual,
										treeType : currentTree.treeType
									};
								},
						dataFilter : currentTree.filterFunction
					},
					check_callback : true
				},
				contextmenu : {
					items : currentTree.isFavoriteFolder ? currentTree.contextMenuForFavorite : currentTree.contextMenuFunction
				},
				checkbox : currentTree.checkBoxOptions,
				plugins : currentTree.plugins,
				types : {
					"default" : {		// 기본폴더
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_normal.png"
					},
					"default_open" : {	// 기본폴더 (확장됨)
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_normal_open.png"
					},
					"link" : {			// 바로가기 폴더
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_link.png"
					},
					"link_open" : {		// 바로가기 폴더 (확장됨)
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_link_open.png"
					},
					"virtual" : {		// 가상폴더
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_imagine.png"
					},
					"virtual_open" : {	// 가상폴더 (확장됨)
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_imagine_open.png"
					},
					"group" : {			// 그룹 폴더
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_group.png"
					},
					"group_open" : {	// 그룹 폴더 (확장됨)
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_group_open.png"
					},
					"inactive" : {		// 비활성
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_inactive.png"
					},
					"inactive_open" : {	// 비활성 (확장됨)
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_inactive_open.png"
					},
					"group_folder" : {		// 그룹 폴더
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_division.png"
					},
					"group_folder_open" : {	// 그룹 폴더 (확장됨)
						icon : currentTree.contextRoot + "/js/plugins/jstree/img/folder_division_open.png"
					}

				}
			});

			//----------------------------------------------------------------
			// 6-3-1.Binding JS Tree event
			//----------------------------------------------------------------
			// changed.jstree		: 체크박스의 상태가 변화할때 발생하는 이벤트
			// open_node.jstree 	: Tree를 확장할 경우 발생하는 이벤트
			// select_node.jstree	: Tree의 Node를 선택할 경우 발생하는 이벤트
			// load_node.jstree		: 해당 Node의 조회가 끝났을때 발생하는 이벤트
			//----------------------------------------------------------------
			$(currentTree.divId)
			.on('changed.jstree', function (e, data) {
				currentTree.selectedNode = new Array();
				currentTree.selectedNodeData = new Array();
				$.each(data.selected, function(i) {
					currentTree.selectedNode.push(data.instance.get_node(data.selected[i]).id);
					currentTree.selectedNodeData.push(data.instance.get_node(data.selected[i]).id +"#" + data.instance.get_node(data.selected[i]).text);
				});

				currentTree.callbackAllSelectNode(currentTree.selectedNode);
				currentTree.callbackAllSelectNodeData(currentTree.selectedNodeData);

			})
			.on('open_node.jstree', function(e, data) {
				if (currentTree.refreshNodeParentId == data.node.id) {
					$(currentTree.divId).jstree("deselect_all");
					$(currentTree.divId).jstree("select_node", currentTree.refreshNodeId);
				}

				var curClass = $(currentTree.divId).jstree("get_type", data.node.id);
				$(currentTree.divId).jstree("set_type", data.node.id, "{0}_open".format(curClass));

				currentTree.callbackOpenNode(e, data);
			})
			.on('close_node.jstree', function(e, data) {
				var curClass = $(currentTree.divId).jstree("get_type", data.node.id);
				$(currentTree.divId).jstree("set_type", data.node.id, "{0}".format(curClass.replace("_open", "")));

				currentTree.callbackCloseNode(e, data);
			})
			.on('before_open.jstree', function(e, data) {
				currentTree.callbackBeforeOpenNode(e, data);
			})
			// 폴더트리 선택시 유효성 체크 로직을 추가함.
			.on('select_node.jstree', function (e, data) {
				data.node.full_path = currentTree.getCurrentNodeFullPath();
				data.node.parentIdList = currentTree.getParentIdList(data.node.id);
				data.node.parentGroup = currentTree.getFolderGroupId(data.node.id);
				data.node.mapId = currentTree.mapId;
				currentTree.callbackSelectNode(e, data);
			})
			.on('load_node.jstree', function (e, data) {
				if(data.node.id === "#") {
					// 1. 자식이 있을경우 : load한 node가 Root(#)일경우 첫번째 자식 Node를 확장한다
					if (data.node.children.length > 0 && $(currentTree.divId).jstree("get_node", data.node.children[0]).original.childrenCnt > 0) {
						$(exsoft.util.common.getIdFormat(currentTree.divId)).jstree("deselect_all");
						$(currentTree.divId).jstree("toggle_node", data.node.children[0]);

					// 2. Root("#")를 select event Trigging할 경우
					} else if (currentTree.isSelectHiddenRoot && data.node.children.length == 0) {
						// hidden root "#" select 이벤트 발생
						var _node = {
								id : "##",
								text : "공유 폴더가 없습니다."
						};
						$(currentTree.divId).jstree().create_node("#", _node);
						$(currentTree.divId).jstree("activate_node", _node.id, true);
					// 3. 자식이 없을경우 (다중 선택 모드일 경우엔 사용하지 않음)
					} else if (currentTree.isBasicTree){
						$(currentTree.divId).jstree("activate_node", data.node.children[0], true);
					}	

				} else if (data.node.parent === "#") {
					// load한 node가 Root(#)의 자식일 경우 해당 노드를 선택한다.
					// 단, 기본 형태의 트리에서만 사용한다
					if (currentTree.isBasicTree) {
						$(currentTree.divId).jstree("select_node", data.node.id, true);
					}
				}

			})
			.on('loaded.jstree', currentTree.callbackLoadNode)
			.on('move_node.jstree', function (e, data) {
					$.get('?operation=move_node', { 'id' : data.node.id, 'parent' : data.parent, 'position' : data.position })
						.fail(function () {
							data.instance.refresh();
						});
				})
		});
	};
};

