/**
 * workDocList.js
 */
var	workDocList = {

	/**
	 * member variables
	 */
	workType : Constant.WORK_MYDEPT,
	folderId : null,

	tree : {
		mydeptTree : null,
		allDeptTree : null,
		projectTree : null
	},

	// 0. 초기화
	init : {
		isInitialized : false,

		//page 초기화
		initPage : function(pageSize){

			// 트리 초기화
			workDocList.treeFunctions.initTree(Constant.WORK_MYDEPT);

			// UI 초기화
			workDocList.init.initUi(pageSize);

		},

		// 화면 구성 요소 초기화 (ddslick 등)
		initUi : function(pageSize) {
			if (!workDocList.init.isInitialized) {
				//검색 selectbox
				exsoft.util.common.ddslick('#workDoc_select', 'srch_type1', '', 79, function(divId, selectedData){
				});

				// 목록 출력 갯수
				exsoft.util.common.ddslick('#workDocListRowCount', 'srch_type1', '', 68, function(divId, selectedData){
					$("#workDocList").setGridParam({page:1, rowNum:selectedData.selectedData.value}).trigger("reloadGrid");
				});

				/**
				 * Tab index 클릭 이벤트 초기화
				 */
				$(".tree_menu_list li").each(function(idx) {
					$(this).on("click", workDocList.event.tabClick);
				})

				//depth navigation
				$('.depth_navi > span').mouseover(function(){
					var path = $(this).parent().find(".depth_navi_path");
					if(!path.is(":visible")) {
						path.removeClass('hide');
					}
				}).mouseout(function(){
					var path = $(this).parent().find(".depth_navi_path");
					if(path.is(":visible")) {
						path.addClass('hide');
					}
				});

				workDocList.grid.pageSize = pageSize;

				$("#treeRefresh").on("click", function() {
					 workDocList.treeFunctions.refresh();
				});

				// 페이지목록 값 설정
				exsoft.util.layout.setSelectBox('workDocListRowCount',workDocList.grid.pageSize);

				// 쿠키값에 설정된 화면 상하좌우 분활 자동으로 보이기
				exsoft.common.bind.doFunction.layoutViewCookie();

				isInitialized = true;
			}
		}
	},

	treeContextAction : {
		// Tree Context : 폴더 생성 Callback
		createFolder : function(node) {
			exsoft.util.layout.divLayerOpen(folderWindow.wrapperClass, folderWindow.layerClass);
			folderWindow.callback = workDocList.callback.refreshTree;
			folderWindow.initForm(node)
		},

		// Tree Context : 폴더 수정 Callback
		modifyFolder : function(node) {
			exsoft.util.layout.divLayerOpen(folderWindow.wrapperClass, folderWindow.layerClass);
			folderWindow.callback = workDocList.callback.refreshTree;
			folderWindow.initForm(node, node.id)

		},

		// Tree Context : 폴더 이동 Callback
		moveFolder : function(node) {
//			selectSingleFolderWindow.callback = workDocList.callback.refreshTree;
			selectSingleFolderWindow.init(workDocList.callback.moveFolder);
		},

		// Tree Context : 폴더 삭제 Callback
		deleteFolder : function(node) {
			selectSingleFolderWindow.callback = workDocList.callback.refreshTree;

			var jo = {
					folder_id : node.id,
					folder_name_ko : node.text,
					type : "DELETE"
			}

			jConfirm("폴더를 삭제 하시겠습니까?", "폴더 삭제", 0, function(r) {

				if (r) {

					exsoft.util.ajax.ajaxDataFunctionWithCallback(jo, exsoft.contextRoot+"/folder/folderControl.do", "", function(data) {

						if (data.result == "true") {
							workDocList.treeFunctions.refresh();
						} else {
							jAlert(data.message);
						}
					});
				}
			});
		},

		// Tree Context : 즐겨찾기 추가 callback
		addFavoriteFolder : function(node) {

			// 1. 이미 추가된 폴더인지 체크
			exsoft.util.ajax.ajaxDataFunctionWithCallback({folder_id : node.id, type : "CHECK_EXISTS"}, exsoft.contextRoot+"/folder/favoriteControl.do", "", function(data, param) {
				if (data.result == "true") {
					var jsonObject = {
						folder_id : node.id,
						folder_nm : node.text,
						mode : "ADD_FAVORITE",
						only_virtual : "Y"
					}

					// 즐겨찾기 부모 선택창 팝업 (selectFavoriteFolderWindow.jsp 필요함)
					selectFavoriteFolderWindow.init(jsonObject, false, function(returnObject) {
						returnObject.type = "ADD_TO_FAVORITES";

						exsoft.util.ajax.ajaxDataFunctionWithCallback(returnObject, exsoft.contextRoot+"/folder/favoriteControl.do", "", function(data, param) {
							if (data.result == "true") {
								jAlert("즐겨찾기 폴더 등록 완료");
							} else {
								jAlert(data.message);
							}
						});
					});

				} else {
					jAlert("이미 즐겨찾기 폴더로 등록 됐습니다.");
					return;
				}
			});
		}
	},

	functions : {
		updateWorkType : function (workType) {
			switch (workType) {
				case "mydeptTree" :
					workDocList.workType = Constant.WORK_MYDEPT;
					break;
				case "alldeptTree" :
					workDocList.workType = Constant.WORK_ALLDEPT;
					break;
				case "projectTree" :
					workDocList.workType = Constant.WORK_PROJECT;
					break;
			}
		}
	},

	grid : {
//		page : 1,
		pageSize : 10,
		initGrid : function() {
			// 초기화 여부
			if ($("#workDocList")[0].grid != undefined) {
				$('#workDocList').jqGrid('GridUnload');
			}

			var _postData = {
					folder_id : workDocList.folderId,
					strIndex : exsoft.util.layout.getSelectBox('workDoc_select','option'),
					strKeyword1 : $("#strKeyword1").val(),
			}

			// Grid 세팅
			$('#workDocList').jqGrid({
				url: exsoft.contextRoot + '/document/workDocumentList.do',
				mtype:"post",
				datatype:'json',
				jsonReader:{
					page:'page',total:'total',root:'list'
				},
				colNames : ['doc_id','page_cnt','relation_doc','is_locked','doc_name','type_name','creator_name','create_date',
				            'acl_create','acl_changePermission','acl_checkoutCancel','root_id','doc_type','lock_date','lock_owner','is_inherit_acl','lock_status', 'folder_id','acl_level'],
				colModel : [
					{name:'doc_id', index:'doc_id', width:1, editable:false, sortable:false, key:true, align:'center', hidden:true},
					{name:'page_cnt', index:'page_cnt', width:10, editable:false, sortable:false, resizable:true, align:'center',
						formatter : function(cellValue, option, rowObject) {
							return cellValue > 0 ? "<li class='icon' id='file_"+rowObject.doc_id+"'><img src='"+ exsoft.contextRoot +"/img/icon/attach.png' class='attach_file'></li>" : "";
						}
					},
					{name:'relation_doc', index:'relation_doc', width:10, editable:false, sortable:false, resizable:true, align:'center',
						formatter : function(cellValue, option, rowObject) {
							return cellValue > 0 ? "<li class='icon' id='relation _"+rowObject.doc_id+"'><img src='"+ exsoft.contextRoot +"/img/icon/link.png' class='relative_docs'></li>" : "";
						}
					},
					{name:'is_locked', index:'is_locked', width:10, editable:false, sortable:false, resizable:true, align:'center',
						formatter : function(cellValue, option) {
							return cellValue == 'T' ? "<li class='icon'><img src='"+ exsoft.contextRoot +"/img/icon/lock1.png' alt='' class='doc_lock'></li>" : "";
						},
						cellattr : function(rowId, cellValue, rowObject) {
							var tooltip = '반출자 : '+rowObject.lock_owner+'\n';
							tooltip += '반출일시 : '+rowObject.lock_date+'\n';
							return rowObject.is_locked == 'T' ? ' title="'+tooltip+'"' : "";
						}
					},
					{name:'doc_name', index:'doc_name', width:150, editable:false, sortable:true, resizable:true, title:true,
						formatter : function(cellValue, option, rowObject){
							return	"<img src='{0}{1}' class='extension'>".format(exsoft.contextRoot, rowObject.page_extension_img) +
									"<a href='#' onclick='exsoft.preview.event.getPreview(\"{0}\")'>{1}</a>".format(rowObject.doc_id, cellValue) +
									"<a href='#' onclick='workDocList.event.popDocDetail(\"{0}\")'><img src='{1}/img/icon/new_window.png'></a>".format(rowObject.doc_id, exsoft.contextRoot);

						},
						cellattr : function(rowId, cellValue, rowObject) {
							return ' title="'+rowObject.doc_name+'"';
						}
					},
					{name:'type_name', index:'type_name', width:20, editable:false, sortable:true, resizable:true, align:'center'},
					{name:'creator_name', index:'creator_name', width:30, editable:false, sortable:true, resizable:true, align:'center'},
					{name:'create_date', index:'create_date', width:30, editable:false, sortable:true, resizable:true, align:'center'},
					{name:'acl_level', index:'acl_level', width:20, editable:false, sortable:false, resizable:true, align:'center',
						formatter : function(cellValue, option) {
							return "<li class='previlege'><img src='"+ exsoft.contextRoot +"/img/icon/prev_"+ (cellValue.toLowerCase()).substring(0,1) +".png' class='previlege_grade'><label class='hide'>" + exsoft.util.grid.getAclItemTitle(cellValue) + "</label</li>";
						},
						cellattr: function (rowId, cellValue, rowObject) {
							var tooltip = '소유자 : '+rowObject.owner_name+'\n';
								tooltip += '기본권한 : '+ exsoft.util.grid.getAclItemTitle(rowObject.acl_level) + '\n';
								tooltip += '반출취소 : '+(rowObject.acl_checkoutCancel == 'T' ? "가능" : "없음")+'\n';
								tooltip += '권한변경 : '+(rowObject.acl_changePermission == 'T' ? "가능" : "없음");
							return ' title="'+tooltip+'"';
			            }
					},

					{name:'acl_create', index:'acl_create', width:1, editable:false, sortable:false, align:'center', hidden:true},
					{name:'acl_changePermission', index:'acl_changePermission', width:1, editable:false, sortable:false, align:'center', hidden:true},
					{name:'acl_checkoutCancel', index:'acl_checkoutCancel', width:1, editable:false, sortable:false, align:'center', hidden:true},
					{name:'root_id', index:'root_id', width:1, editable:false, sortable:false, align:'center', hidden:true},
					{name:'doc_type', index:'doc_type', width:1, editable:false, sortable:false, align:'center', hidden:true},
					{name:'lock_date', index:'lock_date', width:1, editable:false, sortable:false, align:'center', hidden:true},
					{name:'lock_owner', index:'lock_owner', width:1, editable:false, sortable:false, align:'center', hidden:true},
					{name:'is_inherit_acl', index:'is_inherit_acl', width:1, editable:false, sortable:false, align:'center', hidden:true},
					{name:'lock_status',index:'lock_status',width:1,editable:false,sortable:false,align:'center',hidden:true},
					{name:'folder_id',index:'folder_id',width:1, editable:false,sortable:false,align:'center',hidden:true},
				],
				autowidth:true,viewrecords: true,multikey: "ctrlKey",multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
				height:"auto",
				sortname : "create_date",
				sortorder:"desc",
				scrollOffset: 0,
				viewsortcols:'vertical',
				rowNum : workDocList.grid.pageSize,
				emptyDataText: "데이터가 없습니다.",
				caption:'문서목록',
				postData : _postData,
				onCellSelect : function(rowid, iCol, cellcontent, e) {

					var setCol = "";
					var preview = 'doc_preview';
					var file = 'attach_file';
					var relation = 'relative_docs';
					var lock = 'doc_lock';

					if(~cellcontent.indexOf(preview)){
						setCol = preview;
					} else if(~cellcontent.indexOf(file)) {
						setCol = file;
					} else if (~cellcontent.indexOf(relation)) {
						setCol = relation;
					} else if (~cellcontent.indexOf(lock)) {
						setCol = lock;
					}

					if(iCol == 0){
						// 체크시 row값을 set한다.(선택시 : rowid셋팅, 해제시 : rowid제거)
						$("#workDocList").jqGrid('setSelection',rowid);
					} else if(setCol == preview){
					} else if(setCol == file && cellcontent != ''){
						var row = $("#workDocList").getRowData(rowid);
						documentListLayerWindow.open.openAttachWindow(row);
					} else if(setCol == relation && cellcontent != ''){
						var row = $("#workDocList").getRowData(rowid);
						documentListLayerWindow.open.openRelationWindow(row);
					} else if(setCol == lock && cellcontent != ''){
					}
//					if(iCol != 0){
//						// 선택된 row '>' 표시
//			            $("#select_list").remove();
//						$("#"+rowid).find('#doc_preview').prepend("<span id='select_list' class='select_list_icon'></span>");
//					}
				},
				loadBeforeSend: function() {
					exsoft.util.grid.gridTitleBarHide('workDocList');
					exsoft.util.grid.gridNoDataMsgInit('workDocList');
				}
				,loadComplete: function(data) {

					if ($("#workDocList").getGridParam("records")==0) {
						exsoft.util.grid.gridNoDataMsg("workDocList","nolayer_data");
					}else {
						exsoft.util.grid.gridViewRecords('workDocList');
					}

					exsoft.util.grid.gridInputInit(false);

					exsoft.util.grid.gridPager("#workDocPager",data);

					$("tr.jqgrow", this).contextMenu('documentListLayer_context_menu', {
						bindings: {
				            // 수정
				            'documentListLayer_update' : function(trigger) {
				            	var row = $("#workDocList").getRowData(trigger.id);
				            	var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(row.acl_level));

				            	if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
				            		jAlert("문서 수정 권한이 없습니다.", "수정", 0);
				            		return false;
				            	}
//				            	documentUpdate(trigger.id, fRefreshDocumentList);
				            	jAlert("문서 수정 연동 해야함.");
							},
							// 삭제
							'documentListLayer_delete': function(trigger) {
								var row = $("#workDocList").getRowData(trigger.id);
								var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(row.acl_level));
								var jsonArr = [{
									doc_id : row.doc_id
									, root_id : row.root_id
									, is_locked : row.lock_status
									, doc_type : row.doc_type
								}];

								if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("DELETE")) {
	        						jAlert("문서 삭제 권한이 없습니다.", "삭제", 0);
	        						return false;
	        					}

								documentListLayerWindow.gObjectID = "workDocList";
								documentListLayerWindow.event.documentDeleteSend(jsonArr, "ONLY");
				            },
							// 이동
				            'documentListLayer_move': function(trigger) {
				            	var row = $("#workDocList").getRowData(trigger.id);
				            	var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(row.acl_level));
				            	var jsonArr = [{
				            		doc_id : row.doc_id
				            		, doc_name : exsoft.util.common.stripHtml(row.doc_name)
				            		, is_locked : row.lock_status
				            		, root_id : row.root_id
				            		, doc_type : row.doc_type
				            		, is_inherit_acl : row.is_inherit_acl
				            		, folder_id : workDocList.folderId
				            	}];

				            	if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
		        					jAlert("문서 이동 권한이 없습니다.", "이동", 0);
		        					return false;
		        				}

				            	documentListLayerWindow.gObjectID = "workDocList";
				            	documentListLayerWindow.gWorkType = null;
								documentListLayerWindow.event.documentMove("ONLY", jsonArr);
				            },
				            // 복사
				            'documentListLayer_copy': function(trigger) {
				            	var row = $("#workDocList").getRowData(trigger.id);
				            	var aclLevel = exsoft.util.common.getAclItemTitleEn(exsoft.util.common.stripHtml(row.acl_level));
				            	var jsonArr = [{
				            		doc_id : row.doc_id
				            		, doc_name : exsoft.util.common.stripHtml(row.doc_name)
				            		, is_locked : row.lock_status
				            		, root_id : row.root_id
				            		, doc_type : row.doc_type
				            		, is_inherit_acl : row.is_inherit_acl
				            		, folder_id : workDocList.folderId
				            	}];

				            	if(exsoft.util.common.getAclLevel(aclLevel) < exsoft.util.common.getAclLevel("UPDATE")) {
		        					jAlert("문서 복사 권한이 없습니다.", "복사", 0);
		        					return false;
		        				}

				            	documentListLayerWindow.gObjectID = "workDocList";
				            	documentListLayerWindow.gWorkType = null;
								documentListLayerWindow.event.documentCopy("ONLY", jsonArr);
				            } ,
				            // 즐겨찾기 추가
				            'documentListLayer_favorite_add' : function(trigger) {
								var row = $('#workDocList').getRowData(trigger.id);
								var jsonArr = [{
									doc_id : row.doc_id
									,root_id : row.root_id
								}];

								documentListLayerWindow.event.documentAddFavoriteSend(jsonArr);
							},
				            // 작업카트 추가
							'documentListLayer_work_add': function(trigger) {
				            	var row = $("#workDocList").getRowData(trigger.id);
				            	var jsonArr = [{
				            		doc_id : row.doc_id
				            		, root_id : row.root_id
				            		, is_locked : row.lock_status
				            	}];

				            	documentListLayerWindow.event.documentTempworkSend(jsonArr);
				            } ,
							// 체크아웃 취소
				            'documentListLayer_checkout_cancel':function(trigger) {
								var row = $('#workDocList').getRowData(trigger.id);
								var jsonArr = [{
									doc_id : row.doc_id
									, root_id : row.root_id
									, is_locked : row.lock_status
									, doc_type : row.doc_type
								}];

								documentListLayerWindow.gObjectID = "workDocList";
								documentListLayerWindow.event.documentCancelCheckoutSend(jsonArr, "ONLY");
							},

				        },
				        onContextMenu: function(event) {
				        	var row = $('#workDocList').getRowData(event.currentTarget.id);
							$("#documentListLayer_update").removeClass('hide');
			        		$("#documentListLayer_delete").removeClass('hide');
			        		$("#documentListLayer_move").removeClass('hide');
			        		$("#documentListLayer_copy").removeClass('hide');
			        		$("#documentListLayer_favorite_add").removeClass('hide');
			        		$("#documentListLayer_work_add").removeClass('hide');

			        		if (row.lock_status == "T")
			        			$("#documentListLayer_checkout_cancel").removeClass('hide');
			        		else
			        			$("#documentListLayer_checkout_cancel").addClass("hide");
				            return true;
				        }
					});

				}
				,loadError:function(xhr, status, error) {
					exsoft.util.error.isErrorChk(xhr);
				 }

			});

		 	// 컬럼 헤더 정렬 및 다국어 변경 처리
			var headerData = '{"doc_id":"doc_id","page_cnt":"<img src=\'{0}/img/icon/attach.png\' class=\'attach_file\'>","relation_doc":"<img src=\'{0}/img/icon/link.png\' class=\'relative_docs\'>","is_locked":"<img src=\'{0}/img/icon/lock.png\' class=\'doc_lock\'>","doc_name":"제목","type_name":"문서유형","creator_name":"등록자","create_date":"등록일","acl_level":"권한"}'.format(exsoft.contextRoot);
			exsoft.util.grid.gridColumHeader('workDocList',headerData,'center');
			headerData = null;
		},

		refresh : function(page) {
			$("#workDocList").setGridParam({page:page}).trigger("reloadGrid");
		}
	},

	treeFunctions : {
		initTree : function(workType) {
			var treeOption = {
				context : exsoft.contextRoot,
				contextAction : workDocList.treeContextAction,
				url : "/folder/folderList.do",
			};

			switch(workType) {
				case Constant.WORK_MYDEPT : // 부서 문서함
					if (workDocList.tree.mydeptTree === null) {
						treeOption.divId = "#mydeptTree";
						treeOption.mapId = Constant.MAP_MYDEPT;
						treeOption.workType = Constant.WORK_MYDEPT;

						workDocList.tree.mydeptTree = new XFTree(treeOption);
						workDocList.tree.mydeptTree.template_context(); // 우클릭 메뉴
						workDocList.tree.mydeptTree.callbackSelectNode = workDocList.callback.selectTreeNode;
						workDocList.tree.mydeptTree.init(); //부서 rootId는 서버에서 처리
					} else {
						// 해당 폴더 문서목록 새로고침
						workDocList.folderId = workDocList.tree.mydeptTree.getCurrentNodeId();
	//						$('#folder_title').html(workDocList.mydeptTree.getCurrentNodeName());
	//						fDocumentListByfolderId();
					}
					break;
				case Constant.WORK_ALLDEPT : // 전사 문서함
					if (workDocList.tree.allDeptTree === null) {
						treeOption.divId = "#alldeptTree";
						treeOption.mapId = Constant.MAP_MYDEPT;
						treeOption.workType = Constant.WORK_ALLDEPT;

						workDocList.tree.allDeptTree = new XFTree(treeOption);
						workDocList.tree.allDeptTree.template_context(); // 우클릭 메뉴
						workDocList.tree.allDeptTree.callbackSelectNode = workDocList.callback.selectTreeNode;
						workDocList.tree.allDeptTree.init();
					} else {
						// 해당 폴더 문서목록 새로고침
						workDocList.folderId = workDocList.tree.allDeptTree.getCurrentNodeId();
	//						$('#folder_title').html(gAlldeptFolderTree.getCurrentNodeName());
	//						fDocumentListByfolderId();
					}
					break;
				case Constant.WORK_PROJECT : // 프로젝트 함
					if (workDocList.tree.projectTree === null) {
						treeOption.divId = "#projectTree";
						treeOption.mapId = Constant.MAP_PROJECT;
						treeOption.workType = Constant.WORK_PROJECT;

						workDocList.tree.projectTree = new XFTree(treeOption);
						workDocList.tree.projectTree.template_context(); // 우클릭 메뉴
						workDocList.tree.projectTree.callbackSelectNode = workDocList.callback.selectTreeNode;
						workDocList.tree.projectTree.init();
					} else {
						// 해당 폴더 문서목록 새로고침
						workDocList.folderId = workDocList.tree.projectTree.getCurrentNodeId();
	//						$('#folder_title').html(gProjectFolderTree.getCurrentNodeName());
	//						fDocumentListByfolderId();
					}
					break;
				default :
					console.error("[workDocList] workType : {0} 이 올바르지 않습니다. ".format(workType));
				break;
			}
		},

		getCurrentTree : function() {
			switch(workDocList.workType) {
				case Constant.WORK_MYDEPT :
					return workDocList.tree.mydeptTree;
				case Constant.WORK_ALLDEPT :
					return workDocList.tree.allDeptTree;
				case Constant.WORK_PROJECT :
					return workDocList.tree.projectTree;
				default :
					console.error("[workDocList] workType : {0} 이 올바르지 않습니다. ".format(workType));
			}
		},

		refresh : function() {
			workDocList.treeFunctions.getCurrentTree().refresh();
		}
	},

	ui : {
		switchTab : function(obj) {
			var siblings = $(obj).parent().children();

			siblings.each(function(idx) {
				if (obj === this) {
					$(this).children().addClass("focus");

					// 현재 선택된 Tab의 WorkType을 갱신
					workDocList.functions.updateWorkType($(this).children().data("id"));
				} else {
					$(this).children().removeClass("focus");
				}
			})
		},
		switchTreeDiv : function() {
			$("[data-group=TREE_DIV]").each(function(idx) {
				if ($(this).attr("id") == workDocList.ui.getCurrentTabId()) {
					$(this).removeClass("hide");
				} else {
					$(this).addClass("hide");
				}
			})
		},
		getCurrentTabId : function() {
			var retID = "";
			$(".tree_menu_list li").each(function(idx) {
				if ($(this).children().hasClass("focus")) {
					retID = $(this).children().data("id");
					return false;
				}
			});

			return retID;
		},
		setNavigationText : function(nodeTitle, path) {
			$("#nav_title").text(nodeTitle);
			$("#nav_fullpath").text(path);
		}
	},

	event : {
		// 탭 변경
		tabClick : function() {
			// UI 변경
			workDocList.ui.switchTab(this);

			// 선택한 WorkType의 Tree를 Show
			workDocList.ui.switchTreeDiv();

			// 선택한 WorkType의 Tree를 초기화 (초기화가 이미 됐을경우 Refresh)
			workDocList.treeFunctions.initTree(workDocList.workType);
		},

		// 검색
		searchDocument : function() {

			var _post = {
				strIndex:exsoft.util.common.getDdslick("#workDoc_select"),
				strKeyword1:$("#strKeyword1").val(),
				folder_id:workDocList.folderId,
				is_search:'true'
			};

			exsoft.util.grid.gridPostDataInitRefresh("workDocList", exsoft.contextRoot + "/document/workDocumentList.do", _post);
		},

		popDocDetail : function(docId) {
			exsoft.document.layer.docCommonFrm('doc_detail_wrapper', 'doc_detail', docId);
		}

	},

	callback : {
		// registFolderWindow.js 에서 등록/수정 후 호출하는 callback
		refreshTree : function (e, data) {
			workDocList.treeFunctions.refresh();
		},
		moveFolder : function (parentFolder) {
			var _tree = workDocList.treeFunctions.getCurrentTree();

			// 1. 이동 대상 폴더가 현재 폴더와 동일한 위치인지
			if (parentFolder.original.parentId == _tree.getCurrentNodeParentId()) {
				jAlert("동일한 위치로 이동할 수 없습니다.");
				return;
			}

			for (var i = 0; i < parentFolder.parentIdList.length; i++) {
				if (parentFolder.parentIdList[i] == _tree.getCurrentNodeId() || parentFolder.id == _tree.getCurrentNodeParentId()) {
					jAlert("현재 폴더 및 현재 폴더 하위로 이동할 수 없습니다.");
					return;
				}
			}

			var targetRootFolder = parentFolder.mapId == "PROJECT" && parentFolder.parentGroup == null ? parentFolder.id : parentFolder.parentGroup.id;
			var changeRootFolder = _tree.getFolderGroupId(_tree.selectedNode[0]).id != targetRootFolder ? "T" : "F";

			var jsonObject = {
					type : "MOVE",
					folder_id : _tree.getCurrentNodeId(),
					folder_name_ko : _tree.getCurrentNodeName(),
					parent_id : parentFolder.id,
					map_id : parentFolder.mapId,
					parentGroup_id : targetRootFolder,
					root_folder_change : changeRootFolder
			};

			exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot+"/folder/folderControl.do", "", function(data, param) {
				if (data.result == "true") {
					workDocList.treeFunctions.refresh();
				}
			});
		},
		selectTreeNode : function (e, data) {
			workDocList.folderId = data.node.id;
			workDocList.grid.page = 1;
			documentListLayerWindow.gCurrentFolderId = data.node.id;
			docDetailSearch.functions.changeFolderId(data.node.id);
			
			// 상세검색 postdata세팅
			docDetailSearch.folder_id = workDocList.folderId;
			docDetailSearch.url = "/document/workDocumentList.do";

			// 1. 목록 조회
			workDocList.grid.initGrid();

			// 2. Navigation 변경
			workDocList.ui.setNavigationText(data.node.text, data.node.full_path.join(" < "));
		},

	}
}
