/**
 * 휴지통관리/중복파일관리/대량문서 열람감사관리
 * 만기문서관리/소유권변경/휴지통관리 - 상세정보는 공통
 */
var exsoftAdminDocFunc = {

		gDoc_id : "",
		part : "",
		gridId : "",
		gridUrl : "",
		currentTab : "defaultDoc",
		tabClass :  ['defaultDoc','historyDoc'],
		tabForms :  ['defaultDocFrm','historyDocFrm'],

		init : {

			// 초기값 설정
			initPage : function(gridId,gridUrl,pageSize,part) {

				exsoftAdminDocFunc.gridId = gridId;
				exsoftAdminDocFunc.gridUrl = gridUrl;
				exsoftAdminDocFunc.pageSize = pageSize;
				exsoftAdminDocFunc.part = part;

				// DatePicker
				$("#sdate").datepicker({dateFormat:'yy-mm-dd'});
				$("#edate").datepicker({dateFormat:'yy-mm-dd'});

				if(exsoftAdminDocFunc.gridId =="#expiredGridList" )	{
					// 보존기간 연장  SELECT BOX
					exsoft.util.common.ddslick('#preservationYear', 'set_extend_preserve', '', 58, function(){});
				}else if(exsoftAdminDocFunc.gridId =="#ownerDocGrid" )	{
					// TODO CSS
					// 소유권변경 SELECT BOX
					exsoft.util.common.ddslick('#changeType', 'set_extend_preserve', '', 130, function(){});
				}

			},

			// 탭선택 초기화
			tabSelectInit : function() {
				for (var n in exsoftAdminDocFunc.tabClass) {
					$("#"+exsoftAdminDocFunc.tabClass[n]).removeClass("selected");
				}
			},

			tabFormInit : function() {
				for (var n in exsoftAdminDocFunc.tabForms) {
					$("#"+exsoftAdminDocFunc.tabForms[n]).addClass("hide");
				}
			},

		},

		open : {

			// 보존기간연장
			extendPreserve : function() {

				if(!exsoft.util.grid.gridSelectCheck("expiredGridList")) {
					jAlert("보존기간을 연장할 문서를 선택하세요.", "보존기간 연장", 0);
					return false;
				} else {
					exsoft.util.layout.divLayerOpen('extend_preserve_wrapper', 'extend_preserve');
				}
			},

			// 소유권변경
			changeOwner : function() {
				if ($("#user_id").val() == "") {
					jAlert("소유자를 선택해야 합니다.","확인",0);
					return;
				}

				exsoft.util.layout.divLayerOpen('transfer_owner_wrapper', 'transfer_owner');

				// 데이터 초기화
				$("#targetUserId").val('');
				$("#targetUserName").val('');
				$("#currentOwnerName").html($("#user_name").val());
				$("#targetSpace").html($("#MYWORK").prop('checked') ? '개인문서함' : '업무문서함');

			}

		},

		layer : {

			//

		},

		close : {

		},

		event : {

			// 보존기간 연장처리
			extensionDocument : function() {

				jConfirm('선택한 문서의 보존기간을 연장하시겠습니까?', '보존기간연장',0,
						function(r){
							var docIdList = exsoft.util.grid.gridSelectData('expiredGridList','doc_id');
							var year = exsoft.util.layout.getSelectBox('preservationYear','option');
							var jsonObject = {"docIdList":docIdList, "preservation_year":year};

							if(r) exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+'/admin/extendDocument.do','extensionDocument',exsoftAdminDocFunc.callback.returnAjaxDataFunction);
						}
					);

			},

			// 만기문서 폐기처리
			terminateDocument : function() {

				if(!exsoft.util.grid.gridSelectCheck('expiredGridList')) {
					jAlert("폐기할 문서를 선택하세요.","확인",0);
					return false;
				} else {

					var docIdList =  exsoft.util.grid.gridSelectData('expiredGridList','doc_id');
					var rowDataList = new Array();

					$(docIdList.split(",")).each(function(i) {
						if (this != "") {
							var row = $("#expiredGridList").getRowData(this);
							rowDataList.push(row);
						}
					});

					if(rowDataList.length > 0){
						jConfirm('선택한 문서를 폐기하시겠습니까? \n 삭제 후 복구는 불가능합니다.', '폐기',0,
							function(r){
								if(r) exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({delDocList:JSON.stringify(rowDataList)},exsoft.contextRoot+'/admin/terminateDoc.do','terminateDocument',exsoftAdminDocFunc.callback.returnAjaxDataFunction);
							});
					}

				}

			},


			// 휴지통 목록 GRID
			fWasteListGrid : function() {

				$('#wasteGridList').jqGrid({
					url:exsoft.contextRoot+'/admin/documentList.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['doc_id','제목','문서유형','소유자','삭제일','doc_type','root_id','is_locked'],
					colModel:[
						{name:'doc_id',index:'doc_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'doc_name',index:'doc_name',width:80, editable:false,sortable:true,resizable:true
							/*
							,formatter: function (cellValue, option,rowObject) {
								if(cellValue.length > 10){
									return cellValue.substr(0,10) + "...";
								}else {
									return cellValue;
								}
							}
							*/
						},
						{name:'type_name',index:'doc_type',width:40, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'owner_name',index:'user_name_ko',width:35, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'waste_date',index:'waste_date',width:35, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'doc_type',index:'doc_type',width:10, editable:false,sortable:true,resizable:true,align:'left',hidden:true},
						{name:'root_id',index:'root_id',width:10, editable:false,sortable:false,resizable:true,align:'left',hidden:true},
						{name:'is_locked',index:'is_locked',width:10, editable:false,sortable:false,resizable:true,align:'left',hidden:true},
					],
					autowidth:true,viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					height:"auto",
					sortname : "doc_name",
					sortorder:"desc",
					rowNum : 10,
					multikey: "ctrlKey",
					emptyDataText: "데이터가 없습니다.",
					caption:'휴지통 관리',
					postData : {sdate:$("#sdate").val(),edate:$("#edate").val(),is_search:'true', LIST_TYPE:'TRASHCAN',part:exsoftAdminDocFunc.part} //controller param 값
					,loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('wasteGridList');
						exsoft.util.grid.gridNoDataMsgInit('wasteGridList');
					}
					,loadComplete: function(data) {

						if ($("#wasteGridList").getGridParam("records")==0) {
							$(".sub_right").addClass("hide");
							exsoft.util.grid.gridNoRecords('wasteGridList','no_data');
						}else {
							$(".sub_right").removeClass("hide");
							exsoft.util.grid.gridViewRecords('wasteGridList');

							var rowId = $("#wasteGridList").getDataIDs()[0];
							exsoftAdminDocFunc.ui.docDetailCall($("#wasteGridList").getRowData(rowId).doc_id,"XR_DOCUMENT_DEL",'wasteGridList');
						}

						exsoft.util.grid.gridInputInit(false);
						exsoft.util.grid.gridPager("#wasteGridPager",data);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
					 }
					,onCellSelect : function(rowid,iCol,cellcontent,e){

						exsoft.util.grid.checkBox(e);

						if(iCol == 0){
							$("#wasteGridList").jqGrid('setSelection',rowid);
						}else {

							exsoftAdminDocFunc.currentTab = "defaultDoc";
							exsoftAdminDocFunc.init.tabSelectInit();
							exsoftAdminDocFunc.init.tabFormInit();
							exsoftAdminDocFunc.ui.tabActiveStatus(exsoftAdminDocFunc.currentTab);

							$(".dropDown_wrapper").removeClass("hide");

							exsoftAdminDocFunc.ui.docDetailCall(rowid,"XR_DOCUMENT_DEL",'wasteGridList');

							// 선택된 row '>' 표시
				            //$("#select_list").remove();
				            //$("#"+rowid).find('td:eq(2)').prepend("<span id='select_list' class='select_list_icon'></span>");
						}
					}
				});

			 	// 헤더컬럼 변경없음

			},

			// 문서이력정보 출력
			historyGridList : function() {
				$('#detail_docHistoryList').jqGrid({
					url:exsoft.contextRoot+'/document/docHistoryList.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						root:'list'
					},
					colNames:['일시','수행작업','작업자','버전','비고'],
					colModel:[
						{name:'action_date',index:'action_date',width:110, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'action_name',index:'action_id',width:80, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'actor_nm',index:'actor_nm',width:70, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'version_no',index:'version_no',width:50, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'etc',index:'etc',width:150, editable:false,sortable:false,resizable:true,align:'center'},
					],
					autowidth:true,
					height:"auto",
					viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
					sortname : "action_date",
					sortorder:"desc",
					//scroll: true,
					rowNum : 20,
					emptyDataText: "조회된 결과가 없습니다.",
					caption:'문서이력',
					rownumbers:true,
					rownumWidth:40,
					postData : {doc_id:exsoftAdminDocFunc.gDoc_id}
					,gridComplete: function(){
						//exsoft.util.grid.gridResize('detail_docHistoryList','targetDocHistoryList',20,0);
					}
					,loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('detail_docHistoryList');
						exsoft.util.grid.gridNoDataMsgInit('detail_docHistoryList');
					}
					,loadComplete: function(data) {

						if ($("#detail_docHistoryList").getGridParam("records")==0) {
							exsoft.util.grid.gridNoRecords('detail_docHistoryList','no_data');
						}else {
							exsoft.util.grid.gridViewRecords('detail_docHistoryList');
						}

						exsoft.util.grid.gridInputInit(false);

						// 패이저 부분
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.grid.isErrorChk(xhr);
					 }

				});

			},

			// 만기문서관리 GRID
			expiredGridList : function() {
				$('#expiredGridList').jqGrid({
					url:exsoft.contextRoot+'/admin/documentList.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['doc_id','doc_type','doc_name','type_name','owner_name','expired_date', 'is_locked', 'root_id'],
					colModel:[
						{name:'doc_id',index:'doc_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'doc_type',index:'doc_type',width:5, align:'center',editable:false,sortable:false,hidden:true},
						{name:'doc_name',index:'doc_name',width:70, editable:false,sortable:true,resizable:true},
						{name:'type_name',index:'doc_type',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'owner_name',index:'user_name_ko',width:35, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'expired_date',index:'expired_date',width:35, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'is_locked',index:'is_locked',width:0, editable:false,sortable:false,resizable:true,align:'center',hidden:true},
						{name:'root_id',index:'root_id',width:0, editable:false,sortable:false,resizable:true,align:'center',hidden:true},
					],
					autowidth:true,
					viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					multikey: "ctrlKey",
					sortname : "doc_name",
					sortorder:"desc",
					scrollOffset: 0,
					viewsortcols:'vertical',
					rowNum : 20,
					emptyDataText: "데이터가 없습니다.",
					caption:'만기문서 관리',
					postData : {LIST_TYPE:'EXPIRED',part:exsoftAdminDocFunc.part}
					,loadBeforeSend: function() {
						exsoft.util.grid.gridNoDataMsgInit('expiredGridList');
						exsoft.util.grid.gridTitleBarHide("expiredGridList");
					}
					,loadComplete: function(data) {

						if ($("#expiredGridList").getGridParam("records")==0) {
							$(".sub_right").addClass("hide");
							exsoft.util.grid.gridNoRecords('expiredGridList','no_data');
						}else {
							$(".sub_right").removeClass("hide");
							exsoft.util.grid.gridViewRecords('expiredGridList');

							var rowId = $("#expiredGridList").getDataIDs()[0];
							exsoftAdminDocFunc.ui.docDetailCall($("#expiredGridList").getRowData(rowId).doc_id,"XR_DOCUMENT","expiredGridList");
						}

						exsoft.util.grid.gridInputInit(false);
						exsoft.util.grid.gridPager("#expiredMgrPager",data);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.grid.isErrorChk(xhr);
					 }
					,onCellSelect : function(rowid,iCol,cellcontent,e){

						exsoft.util.grid.checkBox(e); // CheckBox Browser Bug Fix

						if(iCol == 0){
							$("#expiredGridList").jqGrid('setSelection',rowid);
						}else {

							exsoftAdminDocFunc.currentTab = "defaultDoc";
							exsoftAdminDocFunc.init.tabSelectInit();
							exsoftAdminDocFunc.init.tabFormInit();
							exsoftAdminDocFunc.ui.tabActiveStatus(exsoftAdminDocFunc.currentTab);

							$(".dropDown_wrapper").removeClass("hide");
							exsoftAdminDocFunc.ui.docDetailCall(rowid,"XR_DOCUMENT","expiredGridList");

							// 선택된 row '>' 표시
				            //$("#select_list").remove();
				            //$("#"+rowid).find('td:eq(3)').prepend("<span id='select_list' class='select_list_icon'></span>");
						}
					}
				});

				// Grid 컬럼정렬 처리
				var headerData = '{"doc_id":"doc_id","doc_name":"제목","type_name":"문서유형","owner_name":"소유자","expired_date":"만기일","is_locked":"잠금여부","root_id":"부모ID"}';
				exsoft.util.grid.gridColumHeader('expiredGridList',headerData,'center');

			},

			// 휴지통관리 - 영구삭제
			selectedDelete : function() {

				var jsonArr = [];
				var jsonArrIndex = 0;

				if(!exsoft.util.grid.gridSelectCheck('wasteGridList')) {
					jAlert("영구 삭제할 문서를 선택하세요.",'확인',0);
					return false;
				} else {


					var id = $("#wasteGridList").getGridParam('selarrrow');

					for(var i = 0; i < id.length; i++){

						var rowData = {doc_id:"", root_id:"", is_locked:"", doc_type:""};
						var rowId = $("#wasteGridList").getRowData(id[i]);

						//jsonObject
						rowData['doc_id'] = rowId.doc_id;
						rowData['root_id'] = rowId.root_id;
						rowData['is_locked'] = rowId.is_locked;
						rowData['doc_type'] = rowId.doc_type;

						if(rowData.doc_id){
							jsonArr[jsonArrIndex] = rowData;
							jsonArrIndex++;
						}
					}

					if(id.length > 0){
						jConfirm('선택한 문서를 영구삭제하시겠습니까? \n 삭제 후 복구는 불가능합니다.', '영구 삭제',0,
							function(r){
								var jsonObject = {"type":"delete", "delDocList":JSON.stringify(jsonArr)};
								if(r) exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+'/admin/wasteDocDelete.do','lscDocDelete',exsoftAdminDocFunc.callback.returnAjaxDataFunction);
							});
					}


				}
			},

			// 휴지통 비우기
			allDelete : function(){
				jConfirm('휴지통의 모든 문서가 영구삭제되며, \n 삭제 후 복구는 불가능합니다. \n 정말 삭제하시겠습니까?' ,'휴지통 비우기',0,
					function(r){
					var jsonObject = {"type":"allDelete"};
					if(r) exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+'/admin/wasteAllDocDelete.do','lscDocDelete',exsoftAdminDocFunc.callback.returnAjaxDataFunction);

				});
			},


			// 소유권변경 GRID
			ownerGrid : function() {
				$('#ownerDocGrid').jqGrid({
					url:exsoft.contextRoot+'/admin/documentList.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',total:'total',root:'list'
					},
					colNames:['doc_id','doc_type','doc_name','type_name','owner_name','create_date', 'is_locked', 'root_id'],
					colModel:[
						{name:'doc_id',index:'doc_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'doc_type',index:'doc_type',width:5, align:'center',editable:false,sortable:false,hidden:true},
						{name:'doc_name',index:'doc_name',width:175, editable:false,sortable:true,resizable:true},
						{name:'type_name',index:'type_name',width:70, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'owner_name',index:'user_name_ko',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'create_date',index:'create_date',width:50, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'is_locked',index:'is_locked',width:5, editable:false,sortable:false,align:'center',hidden:true},
						{name:'root_id',index:'root_id',width:5, editable:false,sortable:false,align:'center',hidden:true},
					],
					autowidth:true,
					viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					multikey: "ctrlKey",
					sortname : "doc_name",
					sortorder:"desc",
					scrollOffset : 0,
					viewsortcols:'vertical',
					rowNum : 20,
					emptyDataText: "데이터가 없습니다.",
					caption:'소유권 이전 관리',
					postData : {LIST_TYPE:'OWNER'}
					,loadBeforeSend: function() {
						exsoft.util.grid.gridNoDataMsgInit('ownerDocGrid');
						exsoft.util.grid.gridTitleBarHide("ownerDocGrid");
					}
					,loadComplete: function(data) {

						if ($("#ownerDocGrid").getGridParam("records")==0) {
							$(".sub_right").addClass("hide");
							exsoft.util.grid.gridNoRecords('ownerDocGrid','no_data');

						}else {
							$(".sub_right").removeClass("hide");
							exsoft.util.grid.gridViewRecords('ownerDocGrid');
							exsoft.util.grid.gridPager("#ownerDocGridPager",data);		// 데이터있는 경우에만 표시

							// 조회화면 DISPLAY
							var rowId = $("#ownerDocGrid").getDataIDs()[0];
							exsoftAdminDocFunc.ui.docDetailCall($("#ownerDocGrid").getRowData(rowId).doc_id,"XR_DOCUMENT","ownerDocGrid");
						}

						exsoft.util.grid.gridInputInit(false);

					}
					,loadError:function(xhr, status, error) {
						exsoft.util.grid.isErrorChk(xhr);
					 }
					,onCellSelect : function(rowid,iCol,cellcontent,e){

						exsoft.util.grid.checkBox(e); // CheckBox Browser Bug Fix

						if(iCol == 0){
							$("#ownerDocGrid").jqGrid('setSelection',rowid);
						}else {
							exsoftAdminDocFunc.currentTab = "defaultDoc";
							exsoftAdminDocFunc.init.tabSelectInit();
							exsoftAdminDocFunc.init.tabFormInit();
							exsoftAdminDocFunc.ui.tabActiveStatus(exsoftAdminDocFunc.currentTab);

							$(".dropDown_wrapper").removeClass("hide");
							exsoftAdminDocFunc.ui.docDetailCall(rowid,"XR_DOCUMENT","ownerDocGrid");

							//ownerDetailCall(rowid);
							// 선택된 row '>' 표시
				            //$("#select_list").remove();
				            //$("#"+rowid).find('td:eq(3)').prepend("<span id='select_list' class='select_list_icon'></span>");
						}
					}
				});

				var headerData = '{"doc_id":"doc_id","doc_name":"제목","type_name":"문서유형","owner_name":"소유자","create_date":"등록일","is_locked":"잠금여부","root_id":"부모ID"}';
				exsoft.util.grid.gridColumHeader('ownerDocGrid',headerData,'center');
			}

		},

		ui : {

			// 탭활성화처리
			tabActiveStatus : function(tabType){
				$("#"+tabType).addClass("selected");
				$("#"+tabType+"Frm").removeClass("hide");
			},

			// 탠선택시
			tabSelectFunc : function(tabType) {

				if(exsoftAdminDocFunc.currentTab != tabType) {

					exsoftAdminDocFunc.currentTab = tabType;
					exsoftAdminDocFunc.init.tabSelectInit();
					exsoftAdminDocFunc.init.tabFormInit();

					exsoftAdminDocFunc.ui.tabActiveStatus(tabType);		// 선택된 탭 및 컨텐츠 활성화

					if(exsoftAdminDocFunc.currentTab == "versionDoc")	{

						$(".dropDown_wrapper").addClass("hide");

					}else if(exsoftAdminDocFunc.currentTab == "historyDoc")	{

						$(".dropDown_wrapper").addClass("hide");

						if($('#detail_docHistoryList')[0].grid != undefined)	{
							exsoft.util.grid.gridPostDataRefresh('detail_docHistoryList',exsoft.contextRoot+'/document/docHistoryList.do'
									,{doc_id:exsoftAdminDocFunc.gDoc_id});
						}else {
							exsoftAdminDocFunc.event.historyGridList();
						}

					}else if(exsoftAdminDocFunc.currentTab == "defaultDoc")	{

						// 기본속성은 HIDE/SHOW 만 처리한다. 서버CALL 없음
						$(".dropDown_wrapper").removeClass("hide");
					}
				}

				// 현재 활성화된 TAB을 누를 경우 PASS

			},

			// 문서상세보기
			docDetailCall : function(select_id,table_nm,gridType) {

				exsoftAdminDocFunc.init.tabSelectInit();
				exsoftAdminDocFunc.init.tabFormInit();
				exsoftAdminDocFunc.ui.tabActiveStatus(exsoftAdminDocFunc.currentTab);

				exsoftAdminDocFunc.gDoc_id = select_id;
				var jsonObject = {doc_id : select_id,table_nm:table_nm};		// 휴지통관리인 경우 XR_DOCUMENT_DEL

				// 서버 CALL
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot + "/admin/wasteDocDetail.do" , "lscWasteDocDetail",
						function(data,e) {
							if(data.result == "true"){

								// 문서기본정보
								$("#detailTitle").html(data.documentVO.doc_name);
								$("#doc_title").html(data.documentVO.doc_name);
								$("#doc_type").html(data.documentVO.type_name);
								$("#doc_ver").html(data.documentVO.version_no);
								$("#doc_creator_name").html(data.documentVO.creator_name);
								$("#doc_create_date").html(data.documentVO.create_date);
								$("#vtemp_content").html(data.documentVO.doc_description.replace('&lt;','<').replace('& lt;','<').replace('&gt;', '>').replace('& gt;', '>'));
								$('#vIframe_editor').attr("src",exsoft.contextRoot+"/editor/doc_admin_view.jsp");
								$("#doc_security").html(exsoft.util.common.findCodeName(data.securityList,data.documentVO.security_level));

								if(gridType == "expiredGridList")	{
									// 소유자,만기일
									$("#docUserInfo").show();
									$("#doc_owner").html("소유자");
									$("#doc_date").html("만기일");
									$("#doc_owner_name").html(data.documentVO.owner_name);
									$("#doc_waste_date").html(data.documentVO.expired_date);
								}else 	if(gridType == "ownerDocGrid")	{
									$("#docUserInfo").hide();
								}else if(gridType == "wasteGridList")	{
									// 소유자/삭제일
									$("#docUserInfo").show();
									$("#doc_owner").html("소유자");
									$("#doc_date").html("삭제일");
									$("#doc_owner_name").html(data.documentVO.owner_name);
									$("#doc_waste_date").html(data.documentVO.waste_date);
								}

								if(data.documentVO.preservation_year == 0) {
									$("#doc_preservation_year").html('무기한');
								}else {
									$("#doc_preservation_year").html(data.documentVO.preservation_year+"년");
								}

								// 권한속성
								exsoftAdminDocFunc.ui.printAcl(data,"#wAclName");

							}else {
								jAlert(data.message,'확인',0);
							}

					});
			},

			// 첨부파일 출력
			attacheList : function(data) {

				var buffer = "";

				$('#attachFiles tr:gt(0)').remove();			// 첨부파일 목록 초기화

				// 첨부파일 리스트 화면 출력 처리
				if (data.pageList != undefined && data.pageList.length > 0) {

					$(data.pageList).each(function(index){
						var serial = index + 1;
						buffer += "<tr>";
						buffer += "<td>"+serial+"</td>";
						buffer += "<td>"+data.pageList[index].page_name+"</td>";
						buffer += "<td>"+data.pageList[index].fsize+"</td>";
						buffer += "<td><a href='javascript:void(0);'>" +
								"<img src="+exsoft.contextRoot+"'/img/icon/attach_download.png' onclick=\"javascript:exsoft.util.common.fileDown('"+data.pageList[index].page_id+"');\"></a></td>";
						buffer += "</tr>";
					});

					$("#attachFiles").append(buffer);
				}
			},

			// 권한속성::
			printAcl : function(data,aclName) {

				$(aclName).html("권한 : "+data.aclDetail.acl_name);

				// 기본권한 :: default - 기본권한 사용안함 권한
				exsoftAdminDocFunc.ui.aclItemData(data,"aclDocTable");

				// 확장권한 :: T-사용/F-사용안함
				exsoftAdminDocFunc.ui.aclItemData(data,"exAclDocTable");

				// 첨부파일 출력처리
				exsoftAdminDocFunc.ui.attacheList(data);
			},

			// 기본권한::추가권한 출력
			aclItemData :  function(data,divIds) {

				if(divIds == "aclDocTable")	{
					exsoft.util.table.tableDocumentAclItemPrintList('aclDocTable',data.aclItemList);
				}else {
					exsoft.util.table.tableDocumentAclItemPrintList('exAclDocTable',data.aclExItemList);
				}

			},

			// 검색 업무분서함 선택 변경처리
			checkBoxChanged : function(value,e) {
				e = e || event;
				e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

				$("input:checkbox[name='search']").each(function() {
					var str = $(this).val();
					if(str.indexOf(value) != -1) {
						$(this).prop("checked",true);
					}
				});
			},

			// 관리자 소유권 변경 실행
			changeOwnership : function() {

				// 인수자 선택 여부 체크
				if ($("#targetUserId").val() == "") {
					jAlert("인수자를 선택해주세요","확인",0);
					return;
				}

				// 인수자와 인계자가 동일한지 체크
				if ($("#targetUserId").val() == $("#user_id").val()) {
					jAlert("인수자와 인계자는 같을 수 없습니다.","확인",0);
					return;
				}

				// 선택한 문서를 소유권 변경하는경우 : 선택된문서가 있는지 체크
				if (exsoft.util.layout.getSelectBox('changeType','option') == "SELECT_DOC") {
					if(!exsoft.util.grid.gridSelectCheck("ownerDocGrid")) {
						jAlert("선택된 문서가 없습니다.","확인",0);
						return;
					}
				}

				// 소유권 변경 수행
				var jsonObject = {
					strKeyword:$("#doc_name").val(),
					ownerKeyword:$("#user_id").val(), //ownerId
					sdate:$("#sdate").val(),
					edate:$("#edate").val(),
					search_type: $("#MYWORK").prop('checked') ? 'MYWORK' : 'WORKSPACE',
					changeType :exsoft.util.layout.getRadioVal('view_type'),
					targetUserId : $("#targetUserId").val(),
					selectDocList : exsoft.util.grid.gridSelectData("ownerDocGrid", "doc_id")
				}

				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+'/document/changeOwner.do','changeOwner',exsoftAdminDocFunc.callback.returnAjaxDataFunction);

			}

		},

		callback : {

			returnAjaxDataFunction : function(data,param) {

				 if (param == 'lscDocDelete') {
					 	// 영구삭제처리
						if(data.result == "true") {
							exsoft.util.grid.gridRefresh('wasteGridList', exsoft.contextRoot+'/admin/documentList.do');
						} else {
							jAlert(data.message,"확인",0);
						}
				 }else  if (param == 'extensionDocument') {
					 // 보존기간 연장
					 exsoft.util.layout.divLayerClose("extend_preserve_wrapper", "extend_preserve");
					 exsoftAdminDocFunc.search.searchFunc();
				 }else  if (param == 'terminateDocument') {
					 // 폐기문서처리
					 exsoftAdminDocFunc.search.searchFunc();
				 }else if(param == "changeOwner")	{
					 exsoftAdminDocFunc.search.searchDocList();
					 exsoft.util.layout.divLayerClose("transfer_owner_wrapper", "transfer_owner");
				 }


			},

			// 사용자선택 후
			returnSelectUser : function(data) {
				$("#user_name").val(data.user_name_ko);
				$("#user_id").val(data.user_id);
			},

			// 소유권변경에서 사용자 선택 후
			returnTargetUser : function(data) {
				$("#targetUserName").val(data.user_name_ko);
				$("#targetUserId").val(data.user_id);
			}


		},

		search : {


			// 휴지통관리 검색처리
			searchFunc : function() {

				var postData = {strIndex:'DOC_NAME',
						strKeyword:$("#strKeyword").val(),
						ownerKeyword:$("#ownerKeyword").val(),
						sdate:$("#sdate").val(),edate:$("#edate").val(),
						part : exsoftAdminDocFunc.part,
						is_search:'true' };
				exsoft.util.grid.gridPostDataRefresh(exsoftAdminDocFunc.gridId,exsoft.contextRoot+exsoftAdminDocFunc.gridUrl,postData);
			},

			// 소유권변경 검색처리
			searchDocList : function() {

				var postData = {
						strIndex:'DOC_NAME',
						strKeyword:$("#doc_name").val(),
						ownerKeyword:$("#user_id").val(), //ownerId
						sdate:$("#sdate").val(),
						edate:$("#edate").val(),
						search_type: $("#MYWORK").prop('checked') ? 'MYWORK' : 'WORKSPACE'
					}
				exsoft.util.grid.gridPostDataRefresh(exsoftAdminDocFunc.gridId,exsoft.contextRoot+exsoftAdminDocFunc.gridUrl, postData);
			}
		}

}