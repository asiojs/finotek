var gMultiFolder_list = new Array();	// 현재 선택된 폴더 목록
var gMultiFolder_isValidation = false;	// 유효성 검사 여부
var gMultiFolder_currentTreeObject;		// 현재 선택된 XFTree Object
var gMultiFolder_currentTreeDivID;		// 현재 선택된 XFTree Div ID
var gMultiFolder_map_id = "MYDEPT";		// 현재 선택된 맵

var selectrelativeDocWindow = {
		
		
	currentMapId : "",		// 현재 선택된 맵
	currentWorkType : "",	// 현재 선택된 부서함
	currentFolderID : "",	// 현재 선택된 폴더 ID
	
	isValidation : false,	// 유효성 검사 여부
	docType : "", 			// 문서 등록/이동 등등 연계 사용시 해당 문서의 Type
	callback : null,		// 확인버튼 클릭시 결과를 반환할 함수
	
	treeDiv : {
		mypage 	: "#relativeAddMypageTree",
		mydept 	: "#relativeAddMydeptTree",
		alldept : "#relativeAddAlldeptTree",
		project : "#relativeAddProjectTree"
	},
	
	open : function() {
		exsoft.util.layout.divLayerOpen("doc_relativeAdd_wrapper", "doc_relativeAdd");
	},
	
	close : function() {
		exsoft.util.layout.divLayerClose("doc_relativeAdd_wrapper", "doc_relativeAdd");
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
				case Constant.WORK_MYDEPT : // 부서 문서함
					treeOption.divId = selectrelativeDocWindow.treeDiv.mydept;; //"#multiFolderMydeptTree";
					treeOption.mapId = Constant.MAP_MYDEPT;
					treeOption.workType = Constant.WORK_MYDEPT;
					
					selectrelativeDocWindow.ui.activeTreeDiv("mydept");
					selectrelativeDocWindow.currentMapId = Constant.MAP_MYDEPT;
					selectrelativeDocWindow.currentWorkType = Constant.WORK_MYDEPT;
					
					if (selectrelativeDocWindow.tree.mydeptTree == null) {
						selectrelativeDocWindow.tree.mydeptTree = new XFTree(treeOption);
						selectrelativeDocWindow.tree.mydeptTree.callbackSelectNode = selectrelativeDocWindow.event.relDoc_selectNode_callback;
						selectrelativeDocWindow.tree.mydeptTree.init();
					} else {
						selectrelativeDocWindow.tree.mydeptTree.refresh();
					}
					break;
				case Constant.WORK_ALLDEPT : // 전사 문서함
					treeOption.divId = selectrelativeDocWindow.treeDiv.alldept;// "#multiFolderAlldeptTree";
					treeOption.mapId = Constant.MAP_MYDEPT;
					treeOption.workType = Constant.WORK_ALLDEPT;
					
					selectrelativeDocWindow.ui.activeTreeDiv("alldept");
					selectrelativeDocWindow.currentMapId = Constant.MAP_MYDEPT;
					selectrelativeDocWindow.currentWorkType = Constant.WORK_ALLDEPT;
					
					if (selectrelativeDocWindow.tree.allDeptTree == null) {
						selectrelativeDocWindow.tree.allDeptTree = new XFTree(treeOption);
						selectrelativeDocWindow.tree.allDeptTree.callbackSelectNode = selectrelativeDocWindow.event.relDoc_selectNode_callback;
						selectrelativeDocWindow.tree.allDeptTree.init();
					} else {
						selectrelativeDocWindow.tree.allDeptTree.refresh();
					}
					break;
				case Constant.WORK_PROJECT : // 프로젝트 함
					treeOption.divId = selectrelativeDocWindow.treeDiv.project; //"#multiFolderProjectTree";
					treeOption.mapId = Constant.MAP_PROJECT;
					treeOption.workType = Constant.WORK_PROJECT;
					
					selectrelativeDocWindow.ui.activeTreeDiv("project");
					selectrelativeDocWindow.currentMapId = Constant.MAP_PROJECT;
					selectrelativeDocWindow.currentWorkType = Constant.WORK_PROJECT;
					
					if (selectrelativeDocWindow.tree.projectTree == null) {
						selectrelativeDocWindow.tree.projectTree = new XFTree(treeOption);
						selectrelativeDocWindow.tree.projectTree.callbackSelectNode = selectrelativeDocWindow.event.relDoc_selectNode_callback;
						selectrelativeDocWindow.tree.projectTree.init();
					} else {
						selectrelativeDocWindow.tree.projectTree.refresh();
					}

					break;
					
				default :
					console.error("[selectrelativeDocWindow] workType : {0} 이 올바르지 않습니다. ".format(workType));
					break;
			}
		},
		getCurrentTree : function() {
			switch(selectrelativeDocWindow.currentWorkType) {
				case Constant.WORK_MYDEPT :
					return selectrelativeDocWindow.tree.mydeptTree;
				case Constant.WORK_ALLDEPT :
					return selectrelativeDocWindow.tree.allDeptTree;
				case Constant.WORK_PROJECT :
					return selectrelativeDocWindow.tree.projectTree;
				default :
					console.error("[multiSingleFolderWindow] workType : {0} 이 올바르지 않습니다. ".format(selectrelativeDocWindow.currentWorkType));
			}
		}
	},
	
	ui : {
		activeTreeDiv : function(activeDivId) {
			// 1. Tree Div
			var keys = Object.keys(selectrelativeDocWindow.treeDiv);
			$(keys).each(function(idx) {
				if (this == activeDivId) {
					$(selectrelativeDocWindow.treeDiv[this]).removeClass("hide");
				} else {
					$(selectrelativeDocWindow.treeDiv[this]).addClass("hide");
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
		
		selectrelativeDocWindow.open();
		selectrelativeDocWindow.callback = callback;
		selectrelativeDocWindow.currentMapId = mapId == undefined ? Constant.MAP_MYDEPT : mapId;
		selectrelativeDocWindow.currentWorkType = (workType == undefined || workType == null || workType == "null") ? Constant.WORK_MYDEPT : workType;
		selectrelativeDocWindow.isValidation = isValidation == undefined ? false : isValidation;
		selectrelativeDocWindow.docType = docType == undefined ? "" : docType;
		
		selectrelativeDocWindow.treeFunctions.initTree(selectrelativeDocWindow.currentWorkType);
				
		/*//검색 selectbox		
		exsoft.util.common.ddslick('#doc_folder_list', 'doc_folder_list', '', 262, function(divId,selectedData){
			// 콤보박스 이벤트 
			selectSingleFolderWindow.event.changeMap(selectedData.selectedData.value);
		});*/
		
	},
	
	event : {
		// 폴더 트리 클릭 시 callback
		relDoc_selectNode_callback : function(e, data) {
			//1. 문서목록에 폴더명 title 변경
			$('#relDoc_folderTitle').html(data.node.text);
			//2. acl에 의한 문서 목록
			gRelDoc_currentFolderId = data.node.id;
			if ($("#relDoc_document_gridList").getGridParam("records") == undefined) {
				$("#relDoc_document_gridList").jqGrid('GridUnload');
				$("#relDoc_select_gridList").jqGrid('GridUnload');
				selectrelativeDocWindow.event.relDoc_initDocumentGridList(gRelDoc_currentFolderId);
			} else {
				selectrelativeDocWindow.event.relDoc_documentListByFolderId();
			}
		},
		
		//문서 목록 초기화
		relDoc_initDocumentGridList : function(folderId) {
			
			// 1.문서 목록
			if ($("#relDoc_document_gridList")[0].grid == undefined) {
				$('#relDoc_document_gridList').jqGrid({		
					url:exsoft.contextRoot+'/document/workDocumentList.do',
					mtype:"post",
					datatype:'json',
					postData : {folder_id:folderId},
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					cmTemplate: { title: false }, // false시 jqgrid tooltip 사용 안함
					colNames:['doc_id','제목','문서유형','등록자','등록일','acl_create','acl_changePermission','acl_checkoutCancel','root_id','doc_type','lock_date','lock_owner'],
					colModel:[
					    /** 표시할 컬럼**/
						{name:'doc_id',index:'doc_id',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'doc_name',index:'doc_name',width:50, editable:false,sortable:true,resizable:true,title:true},
						{name:'type_name',index:'type_name',width:20, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'creator_name',index:'creator_name',width:15, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'create_date',index:'create_date',width:15, editable:false,sortable:true,resizable:true,align:'center'},
						/** 비활성 변수 **/
						{name:'acl_create',index:'acl_create',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'acl_changePermission',index:'acl_changePermission',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'acl_checkoutCancel',index:'acl_checkoutCancel',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'root_id',index:'root_id',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'doc_type',index:'doc_type',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'lock_date',index:'lock_date',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'lock_owner',index:'lock_owner',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
					], 
					autowidth:true,
					viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					sortname : "create_date",
					sortorder:"desc",	
					scrollOffset : 0,
					viewsortcols:'vertical',
					//rowNum : parseInt('${pageSize}',10),		
					rowNum : 10,
					rowList:exsoft.util.grid.listArraySize(),
					pager:'#relDoc_documentGridPager',		
					emptyDataText: "데이터가 없습니다.",	
					caption:'문서목록'
					,gridComplete : function() {
						//tooltip 개행 처리
						$("td[title]").each(function(){
							   
							$(this).tooltip({ 
											  track : true, // tooltip이 마우스 따라 다닌다.
											  content: $(this).attr("title"),
											  tooltipClass: "table_tooltip" // table_tooltip :: custom css
											});
							});
					}
					,loadBeforeSend: function() {						
						exsoft.util.grid.gridNoDataMsgInit('relDoc_document_gridList');
						exsoft.util.grid.gridTitleBarHide('relDoc_document_gridList');
					}
					,loadComplete: function() {
						if ($("#relDoc_document_gridList").getGridParam("records")==0) {
							
							exsoft.util.grid.gridPagerViewHide('relDoc_document_gridList');
							exsoft.util.grid.gridNoDataMsg('relDoc_document_gridList','nolayer_data');				
							exsoft.util.grid.gridPagerHide('relDoc_document_gridList');			
			
							// 상세화면 데이터없음 이미지
							
						}else {
							exsoft.util.grid.gridPagerViewHide('detaildocHistoryList');
							exsoft.util.grid.gridPagerShow('relDoc_document_gridList');
							
							// 조회화면 DISPLAY
						}
						
						exsoft.util.grid.gridInputInit(false); // 페이지 창 숫자만
						exsoft.util.grid.gridResize('relDoc_document_gridList','relDoc_targetDocumentGrid',55); //페이지 div 맨 하단에
						
					}		
					,loadError:function(xhr, status, error) {       
						exsoft.util.error.isErrorChk(xhr);
					 }		
					,onPaging: function (pgButton) {
						// 사용자 입력한 페이지 숫자
						var pagerId = this.p.pager.substr(1); 
						var inputPage = $('input.ui-pg-input', "#pg_" + $.jgrid.jqID(pagerId)).val();
						exsoft.util.grid.onPager('relDoc_document_gridList',inputPage,pgButton);
					 }
			
				});
        		// 컬럼 헤더 정렬 및 다국어 변경 처리 
				var headerData = '{"doc_name":"제목","type_name":"문서유형","creator_name":"등록자","create_date":"등록일"}';
				exsoft.util.grid.gridColumHeader('relDoc_document_gridList',headerData,'center');
        		
			}
			
			// 2. 선택 목록
			// 3. 그룹의 기존 멤버를 설정함
			if ($("#relDoc_select_gridList")[0].grid == undefined) {
				$('#relDoc_select_gridList').jqGrid({		
					url:exsoft.contextRoot+'/document/workDocumentList.do',
					mtype:"post",
					datatype:'json',		
					postData : {folder_id:folderId},
					jsonReader:{
						page:'page',total:'total',root:'list'
					},		
					colNames:['doc_id','제목','문서유형','등록자','등록일','root_id'],
					colModel:[
						/** 표시할 컬럼**/
						{name:'doc_id',index:'doc_id',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'doc_name',index:'doc_name',width:50, editable:false,sortable:true,resizable:true,title:true},
						{name:'type_name',index:'type_name',width:20, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'creator_name',index:'creator_name',width:15, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'create_date',index:'create_date',width:15, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'root_id',index:'root_id',width:0, align:'center',editable:false,sortable:false,key:true,hidden:true},
					], 
					autowidth:true,
					height:"auto",
					viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					sortname : "create_date",			
					sortorder:"desc",					 		   
					rowNum : 10,						
					rowList : exsoft.util.grid.listArraySize(),
					emptyDataText: "데이터가 없습니다.",				
					caption:'사용자 목록',	
					pagerpos: 'center',  
				    pginput: true,
					loadError:function(xhr, status, error) {       
						exsoft.util.error.isErrorChk(xhr);
					 }
					,loadBeforeSend: function() {					
						exsoft.util.grid.gridNoDataMsgInit('relDoc_select_gridList');
						exsoft.util.grid.gridTitleBarHide('relDoc_select_gridList');	
					}
					,loadComplete: function() {
						// 기존 데이터를 삭제함
						$("#relDoc_select_gridList").jqGrid("clearGridData");
						exsoft.util.grid.gridResize('relDoc_select_gridList','relDoc_targetSelectGrid',55); //페이지 div 맨 하단에
					}
				});
				
				// Grid 컬럼정렬 처리
				var headerData = '{"group_nm":"그룹명","user_name_ko":"사용자명", "user_id":"사용자 ID"}';
				exsoft.util.grid.gridColumHeader('relDoc_select_gridList',headerData,'center');

//		 		headerData = null;
			} else {
				// 기존 데이터를 삭제함
				$("#relDoc_select_gridList").jqGrid("clearGridData");
				
			}
		},
		// 현재 폴더 초기 페이지
		relDoc_documentListByFolderId : function () {
			try {
				var postData = {folder_id:gRelDoc_currentFolderId,page_init:'true'} ; //page 1번으로 이동으하기 위해 page_init:'true'
				// 기존 postData는 전체 초기화 시키는 function 호출
				exsoft.util.grid.gridPostDataInitRefresh('relDoc_document_gridList',exsoft.contextRoot+'/document/workDocumentList.do',postData);
			}finally{
				postData = null;
			}
		},
		// 맵 변경 시 트리 초기화(or 갱신 한다)
		relDoc_changeMap : function(workType) {
			
			// 선택한 탭을 설정한다.
			selectrelativeDocWindow.currentWorkType = workType;
			
			// 트리를 초기화 or 갱신 한다.
			selectrelativeDocWindow.treeFunctions.initTree(workType);
		},
		// 관련문서 리스트에 추가
		relDoc_addDocument : function () {
			var documentList = $("#relDoc_document_gridList").getGridParam("selarrrow");
			var selectedDocumentList = exsoft.util.grid.gridSelectArrayDataAllRow("relDoc_select_gridList", "doc_id", "doc_id"); 
			
			// 선택된 문서를 추가한다
			$(documentList).each(function() {
				var row = $("#relDoc_document_gridList").getRowData(this);
				var isDuplicate = false;
				
				// 중복 문서 체크
				$(selectedDocumentList).each(function() {
					if (this.doc_id == row.doc_id) {
						isDuplicate = true;
					}
				});
				
				if (!isDuplicate) {
					$("#relDoc_select_gridList").jqGrid("addRowData", row.doc_id, row);
				}
			});
		},

		submit : function() {
			// 선택된 목록이 있는지 확인
			var docIdList = $("#relDoc_select_gridList").jqGrid("getDataIDs");
			var returnDataList = new Array();
			
			if (docIdList.length == 0) {
				jAlert("추가된 항목이 없습니다.");
				return;
			}
			
			// 선택된한 문서 목록 구성
			$(docIdList).each(function(index) {
				if (this != "") {
					var row = $("#relDoc_select_gridList").getRowData(this);
					returnDataList.push(row);
				}
			});
			
			// 콜백함수로 전달
			//exsoft.document.callback.relDocWindow();
			selectrelativeDocWindow.callback(returnDataList);
			
			// 팝업화면 숨기기
			//base.layerClose("popRelDocWindow");
		},
	
	
	
		
	},//event END

	callback : {
		
	}
}
		
