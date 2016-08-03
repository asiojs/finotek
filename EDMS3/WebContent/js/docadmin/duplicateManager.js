/**
 * 중복파일관리
 */
var exsoftAdminDupFunc = {

		gPage_name : "",
		gPage_size : 0,
		gridId : "",
		gridUrl : "",



		init : {

			// 초기값 설정
			initPage : function(gridId,gridUrl,pageSize,part) {

				exsoftAdminDupFunc.gridId = gridId;
				exsoftAdminDupFunc.gridUrl = gridUrl;
				exsoftAdminDupFunc.pageSize = pageSize;
				exsoftAdminDupFunc.part = part;

				// DatePicker
				$("#sdate").datepicker({dateFormat:'yy-mm-dd'});
				$("#edate").datepicker({dateFormat:'yy-mm-dd'});

				//exsoft.util.common.ddslick('#decade', 'srch_type1', '', 58, exsoftAdminDupFunc.callback.changeDecade);
				exsoft.util.common.ddslick('#decade', 'srch_type1', '', 83, exsoftAdminDupFunc.callback.changeDecade);
				exsoft.util.date.changeDate("one_month", "sdate", "edate");
			}
		},

		open : {

			// 상세화면 갱신처리
			dupViewRefresh : function(gridIds,rowid)	{

				exsoftAdminDupFunc.gPage_name = $("#"+gridIds).getRowData(rowid).page_name;
				exsoftAdminDupFunc.gPage_size = $("#"+gridIds).getRowData(rowid).page_size;


				exsoftAdminDupFunc.open.dupDetailCall(exsoftAdminDupFunc.gPage_name,exsoftAdminDupFunc.gPage_size);
			},

			//상세보기 요청
			dupDetailCall : function(page_name,page_size) {

				if($('#dupDocGridList')[0].grid != undefined)	{
					var postData = { page_name:page_name,file_size:page_size,sdate:$("#sdate").val(),edate:$("#edate").val(),is_search:'true'};
					exsoft.util.grid.gridPostDataRefresh('dupDocGridList',exsoft.contextRoot+'/admin/duplicateDocList.do',postData);
				}else {
					exsoftAdminDupFunc.event.fDocDetailListGrid(page_name,page_size);
				}

				$("#detailTitle").html(exsoftAdminDupFunc.gPage_name);
			}


		},
		layer : {

		},

		close : {

		},

		event : {

			// 검색처리
			searchFunc : function() {

				if(exsoft.util.check.searchValid($("#sdate").val(),$("#edate").val()) ) 	 {
					var postData = {strKeyword:$("#strKeyword").val(),is_search:'true',sdate:$("#sdate").val(),edate:$("#edate").val()} ;
					exsoft.util.grid.gridPostDataRefresh('dupGridList',exsoft.contextRoot+'/admin/duplicatePage.do',postData);
				}
			},

			// 중복파일 리스트 가져오기
			fDupListGrid : function() {
				$('#dupGridList').jqGrid({
					url:exsoft.contextRoot+'/admin/duplicatePage.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',toatl:'toatl',root:'list'
					},
					colNames:['page_name','page_size','page_count','fsize'],
					colModel:[
						{name:'page_name',index:'page_name',width:150, editable:false,sortable:true,resizable:true,align:'left'},
					    {name:'page_size',index:'page_size',width:10, editable:false,sortable:true,resizable:true,align:'right',hidden:true},
					    {name:'fsize',index:'page_size',width:70, editable:false,sortable:true,resizable:true,align:'right'},
					    {name:'page_count',index:'page_count',width:50, editable:false,sortable:true,resizable:true,align:'center',formatter:'currency',formatoptions:{thousandsSeparator:",",decimalPlaces: 0}}
					   ],
					autowidth:true,
					height:"auto",
					viewrecords: true,
					multiselect:false,
					sortname : "page_name",
					sortorder:"desc",
					sortable: true,
					shrinkToFit:true,
					scrollOffset: 0,
					gridview: true,
					viewsortcols:'vertical',
					rowNum : 20,
					emptyDataText: "데이터가 없습니다.",
					caption:'중복파일 목록',
					postData : {sdate:$("#sdate").val(),edate:$("#edate").val(),is_search:'true'}
					,onCellSelect : function(rowid,iCol,cellcontent,e){

						exsoft.util.grid.checkBox(e);

						exsoftAdminDupFunc.open.dupViewRefresh('dupGridList',rowid);

						// 선택된 row '>' 표시
			            //$("#select_list").remove();
			            //$("#"+rowid).find('td:eq(0)').prepend("<span id='select_list' class='select_list_icon'></span>");

					}
					,loadBeforeSend: function() {
						exsoft.util.grid.gridNoDataMsgInit('dupGridList');
						exsoft.util.grid.gridTitleBarHide('dupGridList');
					}
					,loadComplete: function(data) {

						if ($("#dupGridList").getGridParam("records")==0) {
							// base.js 공통처리 변경
							$(".sub_right").addClass("hide");
							exsoft.util.grid.gridNoRecords('dupGridList','no_data');
						}else {
							// base.js 공통처리 변경
							$(".sub_right").removeClass("hide");
							exsoft.util.grid.gridViewRecords('dupGridList');

							// 조회화면 DISPLAY
							var rowId = $("#dupGridList").getDataIDs()[0];
							exsoftAdminDupFunc.gPage_name = $("#dupGridList").getRowData(rowId).page_name;
							exsoftAdminDupFunc.gPage_size = $("#dupGridList").getRowData(rowId).page_size;

							exsoftAdminDupFunc.open.dupDetailCall(exsoftAdminDupFunc.gPage_name,exsoftAdminDupFunc.gPage_size);
						}

						exsoft.util.grid.gridInputInit(false);

						exsoft.util.grid.gridPager("#dupGridPager",data);

					}
					,loadError:function(xhr, status, error) {
						exsoft.util.grid.isErrorChk(xhr);
			        }

				});

				var headerData = '{"page_name":"파일명","fsize":"크기","page_count":"중복수"}';
				exsoft.util.grid.gridColumHeader('dupGridList',headerData,'center');
			},

			// 중복문서 완전삭제
			delDoc : function() {

				var jsonArr = [];
				var jsonArrIndex = 0;

				if(!exsoft.util.grid.gridSelectCheck('dupDocGridList'))	{
					jAlert("완전삭제할 문서를 선택하세요.","확인",0);
					return false;

				}else {

					var id = $("#dupDocGridList").getGridParam('selarrrow');
					for (var i = 0; i < id.length; i++) {

						var rowData = {doc_id:"",root_id:"",is_locked:"",doc_type:""};
						var rowId = $("#dupDocGridList").getRowData(id[i]);

						if(rowId.is_locked == 'T') {
							jAlert("잠금문서는 삭제할 수 없습니다.","확인",0);
							return false;
						}

						// jsonObject
						rowData['doc_id'] = rowId.doc_id;
						rowData['root_id'] = rowId.root_id;
						rowData['is_locked'] = rowId.is_locked;
						rowData['doc_type'] = rowId.doc_type;

						if(rowData.doc_id){
							jsonArr[jsonArrIndex] = rowData;
							jsonArrIndex++;
						}

					}

					if (id.length > 0) {
						jConfirm('선택한 문서를 완전삭제하시겠습니까?', 'Confirm',0,
							function(r){
								var jsonObject = { "type":"delete", "delDocList":JSON.stringify(jsonArr)};
								if(r) exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject,exsoft.contextRoot+'/admin/terminateDoc.do','docDelete',exsoftAdminDupFunc.callback.returnAjaxDataFunction);
							});
					 }
				}
			},

			// 중복 파일 포함 문서 목록 얻기.
			fDocDetailListGrid : function(page_name,page_size) {
				$('#dupDocGridList').jqGrid({
					url:exsoft.contextRoot+'/admin/duplicateDocList.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',toatl:'toatl',root:'list'
					},
					colNames:['doc_id','root_id','doc_name','doc_type','type_name','creator_id','creator_name','create_date','doc_status','is_locked','lock_image'],
					colModel:[
						{name:'doc_id',index:'doc_id',width:5, editable:false,sortable:true,resizable:true,align:'left',hidden:true},
						{name:'root_id',index:'root_id',width:5, editable:false,sortable:true,resizable:true,align:'left',hidden:true},
					    {name:'doc_name',index:'doc_name',width:120, editable:false,sortable:true,resizable:true,align:'left',
							cellattr: function (rowId, cellValue, rowObject) {
								return 'style="cursor: pointer;"';
				          }
					    },
					    {name:'doc_type',index:'doc_type',width:5, editable:false,sortable:true,resizable:true,align:'center',hidden:true},
					    {name:'type_name',index:'type_name',width:50, editable:false,sortable:true,resizable:true,align:'center',
					    	cellattr: function (rowId, cellValue, rowObject) {
								return 'style="cursor: pointer;"';
				          }
					    },
					    {name:'creator_id',index:'creator_id',width:5, editable:false,sortable:true,resizable:true,align:'left',hidden:true},
					    {name:'creator_name',index:'creator_name',width:40, editable:false,sortable:true,resizable:true,align:'center',
					    	cellattr: function (rowId, cellValue, rowObject) {
								return 'style="cursor: pointer;"';
				          }
					    },
					    {name:'create_date',index:'create_date',width:40, editable:false,sortable:true,resizable:true,align:'center',
					    	cellattr: function (rowId, cellValue, rowObject) {
								return 'style="cursor: pointer;"';
				          }
					    },
					    {name:'doc_status',index:'doc_status',width:5, editable:false,sortable:true,resizable:true,align:'center',hidden:true},
					    {name:'is_locked',index:'is_locked',width:15, editable:false,sortable:true,resizable:true,align:'center',hidden:false,
					    	formatter : function (cellValue, option) {
								return cellValue == 'T' ? "<img src='"+ exsoft.contextRoot +"/img/icon/lock1.png' alt='' class='doc_lock'>" : "";
							}
					    },
					    {name:'lock_image',index:'is_locked',width:40, editable:false,sortable:true,resizable:true,align:'center',hidden:true}
					   ],
					autowidth:true,
					height:"auto",
					viewrecords: true,
					multiselect:true,
					sortname : "doc_name",
					sortorder:"desc",
					sortable: true,
					shrinkToFit:true,
					scrollOffset: 0,
					gridview: true,
					multikey: "ctrlKey",
					viewsortcols:'vertical',
					rowNum : 20,
					emptyDataText: "데이터가 없습니다.",
					caption:'중복파일 포함 문서 목록',
					postData : {page_name:page_name,file_size:page_size,is_search:'true',sdate:$("#sdate").val(),edate:$("#edate").val()}

					,onCellSelect : function(rowid,iCol,cellcontent,e){

						exsoft.util.grid.checkBox(e);

						if(iCol == 0){
							$("#dupDocGridList").jqGrid('setSelection',rowid);
						}else {
							var doc_id = $("#dupDocGridList").getRowData(rowid).doc_id;

				 			// 문서상세보기(새창) :: INCLUDE PAGE TODO
				 			//initDocumentViewWindow(doc_id);
						}
					}
					,loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('dupDocGridList');
						exsoft.util.grid.gridNoDataMsgInit('dupDocGridList');
					}
					,loadComplete: function() {

						if ($("#dupDocGridList").getGridParam("records")==0) {
							exsoft.util.grid.gridPagerViewHide('dupDocGridList');
							exsoft.util.grid.gridNoDataMsg('dupDocGridList','no_data');
							exsoft.util.grid.gridPagerHide('dupDocGridList');

							// 상세화면 데이터없음 이미지
						}else {
							exsoft.util.grid.gridPagerViewHide('dupDocGridList');
							exsoft.util.grid.gridPagerShow('dupDocGridList');
						}

						exsoft.util.grid.gridInputInit(false);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.grid.isErrorChk(xhr);
			        }
				});

				var headerData = '{"doc_name":"제목","type_name":"문서유형","creator_name":"등록자","create_date":"등록일","doc_status":"상태","is_locked":"잠금"}';
				exsoft.util.grid.gridColumHeader('dupDocGridList',headerData,'center');
			},


			// 페이지이동 처리(공통)
			gridMainPage : function(nPage)	{
				$("#dupGridList").setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
			},

			gridSubPage : function(nPage)	{
				$("#dupDocGridList").setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
			},

		},

		callback : {

			returnAjaxDataFunction : function(data,param) {

				if(param == 'docDelete') {
					if(data.result == "true")	{
						exsoft.util.grid.gridRefresh('dupDocGridList',exsoft.contextRoot+'/admin/duplicateDocList.do');
						exsoft.util.grid.gridRefresh('dupGridList',exsoft.contextRoot+'/admin/duplicatePage.do');
					}else {
						jAlert(data.message,"확인",0);
					}
				}
				else if(param == 'commonDocView') {
					// 문서보기 :: 공통함수 호출처리

				}
			},

			// 날짜목록 변경선택시
			changeDecade : function(divId, selectedData){
				exsoft.util.date.changeDate(selectedData.selectedData.value, "sdate", "edate");
			},


		},

}