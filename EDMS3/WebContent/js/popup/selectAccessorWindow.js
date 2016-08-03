var selectAccessorWindow = {
		callback : null,
		accessorCheckedIds : null,	// Tree에서 선택된 접근자 Id목록
		accessorParentList : null,	// 현재 grid상의 접근자 목록
		isInitialized : false,
		
		tree : {
			groupTree : null,		// 그룹 트리 오브젝트
			projectTree : null,		// 프로젝트 트리 오브젝트
			
			initTree : function (type) {
				var _treeName = type == "GROUP" ? "groupTree" : "projectTree";
				var _treeOpt = {
						divId : "accessorWindow_" + _treeName,
						context : exsoft.contextRoot,
						url : "/group/groupList.do",
						mapId : type != "GROUP" ? "PROJECT" : null,
						mapId : type != "GROUP" ? Constant.MAP_PROJECT : Constant.MAP_MYDEPT,
						workType : type != "GROUP" ? Constant.WORK_PROJECT : Constant.WORK_ALLDEPT
				};
				
				
				if (type == "GROUP") {
					
					if (selectAccessorWindow.tree.groupTree == null) {
						selectAccessorWindow.tree.groupTree = new XFTree(_treeOpt);
						selectAccessorWindow.tree.groupTree.template_multiCheck(false);
						selectAccessorWindow.tree.groupTree.callbackSelectNode = selectAccessorWindow.callbackFunctions.selectTreeNode; 
						selectAccessorWindow.tree.groupTree.callbackAllSelectNodeData = selectAccessorWindow.callbackFunctions.checkedChangeTreeNode;
						selectAccessorWindow.tree.groupTree.init();
					} else {
						selectAccessorWindow.tree.groupTree.refresh();
					}
				} else {
					if (selectAccessorWindow.tree.projectTree == null) {
						selectAccessorWindow.tree.projectTree = new XFTree(_treeOpt);
						selectAccessorWindow.tree.projectTree.template_multiCheck(false);
						selectAccessorWindow.tree.projectTree.callbackSelectNode = selectAccessorWindow.callbackFunctions.selectTreeNode; 
						selectAccessorWindow.tree.projectTree.callbackAllSelectNodeData = selectAccessorWindow.callbackFunctions.checkedChangeTreeNode;
						selectAccessorWindow.tree.projectTree.init();
					} else {
						selectAccessorWindow.tree.projectTree.refresh();
					}
				}
				
			}
		},
		
		init : function(callback, parentGridId, type) {
			selectAccessorWindow.callback = callback;
			selectAccessorWindow.functions.getAccessorParentList(parentGridId);
			selectAccessorWindow.initOnce();
			
			console.log(type);
			if (type == "DOCUMENT") {
				$("#selectAccessorWindowFolderTr").hide();
			} else {
				$("#selectAccessorWindowFolderTr").show();
			}
			
			selectAccessorWindow.open();
		},
		
		initOnce : function() {
			if (!selectAccessorWindow.isInitialized) {
				selectAccessorWindow.isInitialized = true;
				selectAccessorWindow.grid.initTreeGroupUser();
				selectAccessorWindow.grid.initSearchGroupUser();
				
				exsoft.util.common.ddslick("#selectAccessorWindowMapList", "selectAccessorWindowMapList", "", 262, function(divId,selectedData){
					// 콤보박스 이벤트 
					selectAccessorWindow.event.changeMap(selectedData.selectedData.value);
				});
				
				exsoft.util.common.ddslick("#selectAccessorWindowDefaultFolderAcl", "selectAccessorWindowDefaultFolderAcl", "", 80, function(divId,selectedData){
				});
				
				exsoft.util.common.ddslick("#selectAccessorWindowDefaultDocAcl", "selectAccessorWindowDefaultDocAcl", "", 80, function(divId,selectedData){
				});
			}
		},
		
		open : function() {
			exsoft.util.layout.divLayerOpen("doc_authSet_wrapper", "doc_authSet");
		},
		
		close : function() {
			exsoft.util.layout.divLayerClose("doc_authSet_wrapper", "doc_authSet");
		},
		
		grid : {
			initTreeGroupUser : function(postData) {
				if ($("#initTreeGroupUser")[0].grid == undefined) {
					$("#initTreeGroupUser").jqGrid({		
						url: exsoft.contextRoot + "/user/groupUserList.do",
						mtype:"post",
						datatype:"json",		
						postData : postData,
						list : "",
						jsonReader:{
							root:"list"
						},		
						colNames:["user_id","사용자명"],
						colModel:[
							{name:"user_id",index:"user_id",width:10, editable:false,sortable:true,resizable:true,hidden:true},
							{name:"user_nm",index:"user_nm",width:70, editable:false,sortable:true,resizable:true,hidden:false,align:"left"}			
						], 
						autowidth:true,
						viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
						sortname : "user_nm",			
						sortorder:"desc",
						scroll:true, // virtual Scrolling
						scrollOffset : 0, 
						rowNum : 15,				
						rowList : exsoft.util.grid.listArraySize(),
						emptyDataText: "데이터가 없습니다.",				
						caption:"사용자 목록",	
						pagerpos: "center",  
					    pginput: true,
						loadError:function(xhr, status, error) {       
							exsoft.util.error.isErrorChk(xhr);
						 }
						,loadBeforeSend: function() {					
							exsoft.util.grid.gridNoDataMsgInit("initTreeGroupUser");
							exsoft.util.grid.gridTitleBarHide("initTreeGroupUser");
						}		
						,loadComplete: function() {			
	
							if ($("#initTreeGroupUser").getGridParam("records")==0) {				
								exsoft.util.grid.gridNoDataMsg("initTreeGroupUser","nolayer_data");
							}
							
							exsoft.util.grid.gridInputInit(false); // 페이지 창 숫자만  test
							exsoft.util.grid.gridResize('initTreeGroupUser','accessorWindowGroupUserGridTarget',20,0);
						}
					});
				} else {
					exsoft.util.grid.gridPostDataRefresh("initTreeGroupUser", exsoft.contextRoot + "/user/groupUserList.do", postData);
				}
			},
			
			initSearchGroupUser : function(postData) {
				if ($("#initSearchGroupUser")[0].grid == undefined) {
					$("#initSearchGroupUser").jqGrid({		
						url:exsoft.contextRoot + "/user/searchGroupUser.do",
						mtype:"post",
						postData : postData,
						datatype:"json",	
						list : "",
						jsonReader:{
							root:"list"
						},		
						colNames:["is_group","is_group_nm","성명/부서명","사용자/부서ID"],
						colModel:[  
							{name:"is_group",index:"is_group",width:10, editable:false,sortable:true,resizable:true,hidden:true},
							{name:"is_group_nm",index:"is_group_nm",width:10, editable:false,sortable:true,resizable:true,hidden:true,align:"center"},			
							{name:"unique_nm",index:"unique_nm",width:30, editable:false,sortable:false,resizable:true,hidden:false,align:"center"},			
							{name:"unique_id",index:"unique_id",width:30, editable:false,sortable:true,resizable:true,hidden:false,align:"center"}
						], 
						autowidth:true,
						height:"auto",
						viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
						sortname : "unique_nm",			
						sortorder:"desc",					 		   
						rowNum : 15,						
						rowList : exsoft.util.grid.listArraySize(),
						emptyDataText: "데이터가 없습니다.",				
						caption:"확장자 목록",	
						pagerpos: "center",  
					    pginput: true,
						loadError:function(xhr, status, error) {       
							exsoft.util.error.isErrorChk(xhr);
						 }
						,loadBeforeSend: function() {					
							exsoft.util.grid.gridNoDataMsgInit("initSearchGroupUser");
							exsoft.util.grid.gridTitleBarHide("initSearchGroupUser");	
						}
						,loadComplete: function() {
							
							if ($("#initSearchGroupUser").getGridParam("records")==0) {				
								exsoft.util.grid.gridNoDataMsg("initSearchGroupUser","nolayer_data");
							}
							
							exsoft.util.grid.gridInputInit(false); // 페이지 창 숫자만  test
							exsoft.util.grid.gridResize('initSearchGroupUser','accessorWindowGroupUserGridTarget',20,0);
						}
					});
				} else {
					exsoft.util.grid.gridPostDataRefresh("initSearchGroupUser",exsoft.contextRoot + "/user/groupUserList.do", postData);
				}
			}
		},
		
		event : {
			changeMap : function(type) {
				selectAccessorWindow.tree.initTree(type);
			},
			
			searchAccessor : function() {
				var _post = {
						strIndex : "ALL",
						strKeyword : $("#searchAccessorKeyword").val() 
				};
				
				if (_post.strKeyword.length == 0) {
					jAlert("검색어를 입력하세요.");
					return;
				}
				
				exsoft.util.grid.gridPostDataRefresh("initSearchGroupUser", exsoft.contextRoot + "/user/searchGroupUser.do", _post);
			},
			
			addAccessor : function() {
				/**
				 * 좌측 선택 그리드
				 * 1. initSearchGroupUser
				 * 2. initTreeGroupUser
				 * 
				 * 선택된 트리 아이템
				 * 1. selectAccessorWindow.accessorCheckedIds
				 */
				
				// accesorWindowSelectedList 선택자를 뿌릴 곳
				var _accessorList = [];
				var _select = {
						treeGroup : selectAccessorWindow.accessorCheckedIds.join(","),			// 선택된 부서 트리 ID 목록
						treeGroupUser : $("#initTreeGroupUser").getGridParam("selarrrow"),		// 선택된 부서 사용자 ID 목록
						searchGroupUser : $("#initSearchGroupUser").getGridParam("selarrrow"),	// 선택된 검색 결과 ID 목록
						checkAllUser : $("#checkAccessorAll").is(":checked") ? "T" : "F",		// 전사 체크 여부
				}
				
				if (_select.treeGroup.length == 0 &&_select.treeGroupUser.length == 0 &&_select.searchGroupUser.length == 0 && _select.checkAllUser == "F") {
					jAlert("권한을 적용할 사용자나 부서를 선택하세요.");
					return;
				}
				
				// 1-1. 전사 체크
				if (_select.checkAllUser == "T") {
					_accessorList.push({type : "ALL", id : "WORLD", name : "전체"});
				}
				
				// 1-2. 부서 트리
				$(_select.treeGroup.split(",")).each(function() {
					var _tem = this.split("#");
					_accessorList.push({type : "GROUP", id : _tem[0], name : _tem[1]});
				});
				
				// 1-3. 사용자 목록
				$(_select.treeGroupUser).each(function() {
					var _row = $("#initTreeGroupUser").getRowData(this);
					_accessorList.push({type : "USER", id : _row.user_id, name : _row.user_nm});
				});

				// 1-4. 검색 목록
				$(_select.searchGroupUser).each(function() {
					var _row = $("#initSearchGroupUser").getRowData(this);
					_accessorList.push({type : _row.is_group, id : _row.unique_id, name : _row.unique_nm});
				});
				
				// 2. 아이템 추가
				$(_accessorList).each(function() {
					// 2-1. 이미 있는지 확인
					if (!selectAccessorWindow.functions.checkDuplicate(this.id)) {
						console.info(this.id + " 새로 추가");
						// 2-2. 추가
						selectAccessorWindow.ui.addSelectedAccessor(this);
					} else {
						console.info(this.id + " 이미 있음");
					}
				});
			},
			
			submit : function() {
				var _list = [];
				
				$("#accesorWindowSelectedList span").each(function() {
					var _row = {
						accessor_id : $(this).data("id"),
						accessor_isgroup : $(this).data("type") == "GROUP" ? "T" : "F",
						accessor_isalias : $(this).data("type") == "ALL" ? "T" : "F",
						accessor_name : $(this).data("name"),
						fol_default_acl : exsoft.util.common.getDdslick("#selectAccessorWindowDefaultFolderAcl"),
						fol_act_create : $("#accessorWindowFolActCreate").is(":checked") ? "T" : "F",
						fol_act_change_permission : $("#accessorWindowFolActChangePermission").is(":checked") ? "T" : "F",
						doc_default_acl : exsoft.util.common.getDdslick("#selectAccessorWindowDefaultDocAcl"),
						doc_act_create : $("#accessorWindowDocActCreate").is(":checked") ? "T" : "F",
						doc_act_cancel_checkout : $("#accessorWindowDocActCancelCheckout").is(":checked") ? "T" : "F",
						doc_act_change_permission : $("#accessorWindowDocActChangePermission").is(":checked") ? "T" : "F"
					};
					
					_list.push(_row);
				});
				
				selectAccessorWindow.callback(_list);
				selectAccessorWindow.close();
			},
			
		},
		
		ajax : {
			
		},
		
		functions : {
			/*
			 * 부모폼에 이미 있는 "추가 접근자"목록을 리스트화 해서 저장 함
			 * - 사용자/부서 추가 시 중복 데이터를 걸러내기 위함임.
			 */ 
			getAccessorParentList : function(parentGridId) {
				var arrayId = [];
				var rowIDs = $("#"+parentGridId).jqGrid("getDataIDs");
				var _cnt = rowIDs.length;
				
				for (var i = 0; i < _cnt; i++) {
					var row =$("#"+parentGridId).getRowData(rowIDs[i]);
					arrayId[i] = row.accessor_id;
				}
				// 현재 grid상의 접근자 목록
				selectAccessorWindow.accessorParentList = arrayId.join(",");	
			},
			
			checkDuplicate : function(itemId) {
				var _isDuplicate = false;
				
				// 1. 이미 선택된 값을 체크해본다
				_isDuplicate = $("#accesorWindowSelectedList span").filter(function() {return $(this).data("id") == itemId ? true : false}).length > 0 ? true : false;
				
				// 2. 부모창에서 가지고있는 값을 비교해 본다.
				if (_isDuplicate == false)
					_isDuplicate = $.inArray(itemId, selectAccessorWindow.accessorParentList) > -1 ? true : false;
				
				return _isDuplicate;
			}
		},
		ui : {
			resetSearchKeyword : function() {
				$("#searchAccessorKeyword").val("");
			},
			
			addSelectedAccessor : function(obj) {
				var _str = "<li>";
					_str += "<span class='chosen_user' data-id='{0}' data-name='{1}' data-type='{2}'> {1}</span>".format(obj.id, obj.name, obj.type);
					_str += "<a href='#' class='remove' onclick='selectAccessorWindow.ui.removeSelectedAccessor(\"{0}\")'><img src='{1}/img/icon/window_close3.png'></a>".format(obj.id, exsoft.contextRoot);
					_str += "</li>";
					
				$("#accesorWindowSelectedList").append(_str);
			},
			
			removeSelectedAccessor : function(itemId) {
				$("#accesorWindowSelectedList span").filter(function() {
					return ($(this).data("id") == itemId) ? true : false;
				}).parent().remove();
			}
		},
		
		callbackFunctions : {
			selectTreeNode : function(e, data) {
				selectAccessorWindow.grid.initTreeGroupUser({groupId:data.node.id});
			},
			
			checkedChangeTreeNode : function(selectedNodedata) {
				selectAccessorWindow.accessorCheckedIds = selectedNodedata;
			}
		}
}