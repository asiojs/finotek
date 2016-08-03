/**
 * 문서유형관련 스크립트
 */
var exsoftAdminTypeFunc = {

		buttons : ['atrrDelBtn','attrUpdateBtn','attrItemAdd','attrItemDel','is_hidden','attrCancelBtn'],
		gRowData : [ { attr_id:"", attr_name:"", attr_size:100,sort_index:0,is_mandatory:"F",is_editable:"T",is_search:"F",display_type:"INPUT",config:"" } ],
		gRowId : 0,
		mRowId : 0,
		gTypeId : "",
		mList : 0,
		gridId : "",
		gridUrl : "",
		pageSize : "",
		gAttrItemIdx : 0,			// 항목설정 INDEX 값
		gRowItemId : "",			// 항목설정 BTN의 GRID ROW 정보
		gAttrItemType : "",

		binder : new DataBinder("#frmView"),

		gParameters : {
			  "successfunc" : null,
			  "url" : 'clientArray',
			  "extraparam" : {},
			  "aftersavefunc" : function( response ) {},
			  "errorfunc": null,
			  "afterrestorefunc" : null,
			  "restoreAfterError" : true,
			  "mtype" : "POST"

		},

		init : {

			// 로그인 이력 초기 함수
			initPage : function(gridId,gridUrl,pageSize) {

				// 메인 GRID
				exsoftAdminTypeFunc.gridId = gridId;
				exsoftAdminTypeFunc.gridUrl = gridUrl;
				exsoftAdminTypeFunc.pageSize = pageSize;

				exsoft.util.common.ddslick('#is_hiddenU','use_yn','is_hiddenU',79, function(){});
				exsoft.util.common.ddslick('#is_hiddenC','use_yn','is_hiddenC',79, function(){});
			},

			// 버튼 상태 변경처리 :: TODO
			btnStateChange : function(flag) {

				$.each(exsoftAdminTypeFunc.buttons,function(index,item){
					 if(flag == 'T')	{
						 $("#"+item).prop('disabled', true);
						 $("#"+item).addClass("disabled");
					 }else {
						 $("#"+item).prop('disabled', false);
						 $("#"+item).removeClass("disabled");
					 }
				 });
			},

		},

		open : {

			// 문서유형등록
			typeAdd : function() {
				exsoft.util.layout.divLayerOpen('register_docuType_wrapper', 'register_docuType');
				exsoft.util.common.formClear('frm');
				exsoftAdminTypeFunc.event.fAttrGridList();
				exsoft.util.grid.gridInputInit(false);
			}
		},

		layer : {

		},

		close : {

		},

		event : {

			// 문서유형 목록 GRID :: TO DO 컬럼헤더 사이즈 조정
			fTypeListGrid : function() {
				$("#typeGridList").jqGrid({
					url:exsoft.contextRoot+exsoftAdminTypeFunc.gridUrl,
					mtype:"post",
					datatype:'json',
					jsonReader:{
						page:'page',toatl:'toatl',root:'list'
					},
					colNames:['type_id','type_name','is_base','create_date','status_nm','is_system'],
					colModel:[
						{name:'type_id',index:'type_id',width:25, editable:false,sortable:true,resizable:true,align:'left'},
					    {name:'type_name',index:'type_name',width:25, editable:false,sortable:true,resizable:true,align:'center'},
					    {name:'is_base',index:'is_base',width:10, editable:false,sortable:false,resizable:true,align:'center',hidden:true},
					    {name:'create_date',index:'create_date',width:10, editable:false,sortable:false,resizable:true,align:'center',hidden:true},
					    {name:'status_nm',index:'status_nm',width:20, editable:false,sortable:false,resizable:true,align:'center'},
					    {name:'is_system',index:'is_system',width:10, editable:false,sortable:false,resizable:true,align:'center',hidden:true}
					   ],
					autowidth:true,height:"auto",viewrecords: true,
					multiselect:true,
					sortname : "type_name",
					sortorder:"desc",
					sortable: true,
					shrinkToFit:true,
					gridview: true,
					multikey: "ctrlKey",
					viewsortcols:'vertical',
					rowNum : exsoftAdminTypeFunc.pageSize,
					emptyDataText: "데이터가 없습니다.",
					caption:'문서유형 목록'
					,onCellSelect : function(rowid,iCol,cellcontent,e){

						exsoft.util.grid.checkBox(e);

						if(iCol == 0){
							$("#typeGridList").jqGrid('setSelection',rowid);
						}else {
							exsoftAdminTypeFunc.ui.typeViewRefresh('typeGridList',rowid);

							//$("#select_list").remove();
				            //$("#"+rowid).find('td:eq(1)').prepend("<span id='select_list' class='select_list_icon'></span>");
						}
					}
					,loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('typeGridList');
						exsoft.util.grid.gridNoDataMsgInit('typeGridList');
					}
					,loadComplete: function(data) {

						if ($("#typeGridList").getGridParam("records")==0) {
							$(".sub_right").addClass("hide");
							exsoft.util.grid.gridNoRecords('typeGridList','no_data');
						}else {
							$(".sub_right").removeClass("hide");
							exsoft.util.grid.gridViewRecords('typeGridList');

							// 조회화면 DISPLAY
							var rowId = $("#typeGridList").getDataIDs()[0];
							exsoftAdminTypeFunc.gTypeId = $("#typeGridList").getRowData(rowId).type_id;
							exsoftAdminTypeFunc.ui.typeViewRefresh('typeGridList',rowId);
							exsoft.util.grid.gridPager("#typeGridPager",data);
						}

						exsoft.util.grid.gridInputInit(false);
					}
					,loadError:function(xhr, status, error) {
						exsoft.util.error.isErrorChk(xhr);
			        }
				});

				var headerData = '{"type_id":"문서유형ID","type_name":"문서유형명","status_nm":"상태"}';
			 	exsoft.util.grid.gridColumHeader('typeGridList',headerData,'center');

			},

			// 문서유형목록 GRID
			fAttrViewList : function() {
				$('#attrViewList').jqGrid({
					url:exsoft.contextRoot+'/admin/attrList.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{
						root:'list'
					},
					colNames:['attr_id','attr_name','attr_size','sort_index','is_mandatory','is_editable','is_search','display_type','config','has_item','has_item_list','default_item_index','is_locked'],
					colModel:[
								{name:'attr_id',index:'attr_id',width:40, editable:true,sortable:false,resizable:true,align:'center',edittype:'text',
									editoptions: {
										dataInit: function (elem) {
					                         $(elem).keypress(function(){
					                        	return exsoft.util.filter.inputBoxFilter('^[A-Za-z0-9_]');
											 });
					                         $(elem).keyup(function(){
												elem.value = elem.value.replace(/[^a-zA-Z0-9_]/g,'')
					                        	elem.value = elem.value.toUpperCase();
											});
											// 기 attr_id 에디터모드 후 신규항목 에디터모드인 경우 attr_id 오류 패치
											$(elem).click(function (e) {
												var row = jQuery(e.target).closest('tr.jqgrow');
												var rowid = row.attr('id');
												 if(!exsoftAdminTypeFunc.ui.isSystem(exsoftAdminTypeFunc.gTypeId))	{
													 exsoftAdminTypeFunc.ui.gridNoEditColum('attrViewList','attr_id',rowid);
													 $('#attrViewList').editRow(rowid,false);
												}
											});

											$(elem).focusout(function () {
												elem.value = elem.value.replace(/[^a-zA-Z0-9_]/g,'')
											});

										},
										size:'15', maxlength:'20'
									}
								},
								{name:'attr_name',index:'attr_name',width:50, editable:true,sortable:false,resizable:true,align:'center',
									editoptions: {
										dataInit: function (elem) {
											  $(elem).keypress(function(){
													// 특수문자 입력불가
												  return exsoft.util.filter.inputBoxFilter('[A-Za-z0-9]');
												 })
										},
										size:'15',maxlength:'20'
									}
								},
								{name:'attr_size',index:'attr_size',width:30, editable:true,sortable:false,resizable:true,align:'center',
									editoptions: {
										dataInit: function (elem) {
											// chrome version
											 $(elem).keyup(function(){
					                        	 elem.value =  elem.value.replace(/[^0-9]/g,'')
											 });

											// chrome version
											 $(elem).focusout(function () {
											 	elem.value = elem.value.replace(/[^0-9]/g,'')
											 });
										},
										size:'5',maxlength:'4'
									}
								},
								{name:'sort_index',index:'sort_index',width:40, editable:true,sortable:false,resizable:true,align:'center',
									editoptions: {
										size:'5',maxlength:'2',
										dataInit: function (elem) {

											// chrome version
											 $(elem).keyup(function(){
					                        	 elem.value =  elem.value.replace(/[^0-9]/g,'')
											});

											// chrome version
											 $(elem).focusout(function () {
											 	elem.value = elem.value.replace(/[^0-9]/g,'')
											 });
										}
									}
								},
								{name:'is_mandatory',index:'is_mandatory',width:40, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',editoptions:{value:"T:T;F:F"}},
								{name:'is_editable',index:'is_editable',width:40, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',editoptions:{value:"T:T;F:F"}},
								{name:'is_search',index:'is_search',width:40, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',editoptions:{value:"T:T;F:F"}},
								{name:'display_type',index:'display_type',width:100, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',
									editoptions:{
										value:"RADIO:RADIO;INPUT:INPUT;CHECK:CHECK;SELECT:SELECT"
											,dataEvents:[{ type:'change', fn: function(e){
												var row = jQuery(e.target).closest('tr.jqgrow');
												if($(e.target).val() == "INPUT") {
													$("#vconf"+row.attr('id')).hide();
												}else {
													$("#vconf"+row.attr('id')).show();
												}

												// 문서유형 속성 GRID 값 초기화
												$("#attrViewList").setRowData(row.attr('id'),{ 'has_item_list': '' });
												$("#attrViewList").setRowData(row.attr('id'),{ 'default_item_index': '' });
											}}]
									}
								},
								{name:'config',index:'config',width:50, editable:false,sortable:true,resizable:true,align:'center'
									,formatter: function (cellValue, option,rowObject) {
										return "<button type='button' id='vconf"+option.rowId+"' style='display:none;' onclick='javascript:exsoftAdminTypeFunc.ui.attrConfig(\""+option.rowId+"\",\"attrViewList\")'><span>설정</span></button>";
									}
								},
								{name:'has_item',index:'has_item',width:10, editable:false,sortable:true,resizable:true,align:'center',hidden:true},
								{name:'has_item_list',index:'has_item_list',width:10, editable:false,sortable:true,resizable:true,align:'center',hidden:true},
								{name:'default_item_index',index:'default_item_index',width:10, editable:false,sortable:true,resizable:true,align:'center',hidden:true},
								{name:'is_locked',index:'is_locked',width:10, editable:false,sortable:true,resizable:true,align:'center',hidden:true}
							   ],
					autowidth:true,
					height:"auto",
					viewrecords: true,
					multiselect:true,
					sortable: true,
					shrinkToFit:true,
					gridview: true,
					postData : {type_id:exsoftAdminTypeFunc.gTypeId},
					emptyDataText: "데이터가 없습니다.",
					caption:'문서유형 목록 조회'
					,loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('attrViewList');
						exsoft.util.grid.gridNoDataMsgInit('attrViewList');
					}
					,loadComplete: function(data) {

						if ($("#attrViewList").getGridParam("records") ==0) {
							exsoft.util.grid.gridNoDataMsg('attrViewList','nolayer_data');
							exsoftAdminTypeFunc.mRowId = 0;
						}else {
							var rowIDs = $("#attrViewList").jqGrid('getDataIDs');
							exsoftAdminTypeFunc.mRowId = rowIDs[rowIDs.length-1];
						}

						exsoftAdminTypeFunc.mList = exsoftAdminTypeFunc.mRowId;		// atrr_id 수정못하게 처리위한 변수
						exsoftAdminTypeFunc.ui.isConfigProc();										// 문서유형 함목 설정 SHOW/HIDE
						exsoft.util.grid.gridInputInit(false);
						$(".jqgfirstrow" ).addClass('hide');
					}
					,onCellSelect: function(rowid, iCol,cellcontent,e){
						exsoft.util.grid.checkBox(e);
						if(!exsoftAdminTypeFunc.ui.isSystem(exsoftAdminTypeFunc.gTypeId))	{
							exsoftAdminTypeFunc.ui.gridNoEditColum('attrViewList','attr_id',rowid);
			             	$('#attrViewList').editRow(rowid,false);
						}
				 	}
					,onSelectRow: function(rowid,status,e){

						if(!exsoftAdminTypeFunc.ui.isSystem(exsoftAdminTypeFunc.gTypeId))	{

							exsoftAdminTypeFunc.ui.gridNoEditColum('attrViewList','attr_id',rowid);

							// false 이면 row 저장처리
							if(!status) {
								$('#attrViewList').jqGrid('saveRow', rowid, exsoftAdminTypeFunc.gParameters );
							}else {
								if(exsoft.util.grid.gridEditMode('attrViewList',rowid) == "0") {
									$('#attrViewList').editRow(rowid,false);
								}
							}
						}
					}

				});

				 // Grid 컬럼정렬 처리
				var headerData = '{"attr_id":"속성ID","attr_name":"속성명","attr_size":"길이","sort_index":"정렬순서","is_mandatory":"필수","is_editable":"편집","is_search":"검색","display_type":"입력유형","config":"항목설정"}';
				exsoft.util.grid.gridColumHeader('attrViewList',headerData,'center');

			},

			// 문서유형 등록 속성 GRID
			fAttrGridList : function() {
				$('#attrGridList').jqGrid({
					mtype:"post",
					datatype:'json',
					colNames:['attr_id','attr_name','attr_size','sort_index','is_mandatory','is_editable','is_search','display_type','config','has_item','default_item_index'],
					colModel:[
						{name:'attr_id',index:'attr_id',width:80, editable:true,sortable:false,resizable:true,align:'center',edittype:'text',
							editoptions: {
								dataInit: function (elem) {
			                         $(elem).keypress(function(){
			                        	 return exsoft.util.filter.inputBoxFilter('^[A-Za-z0-9_]');
									 })

			                         $(elem).keyup(function(){
										 // chrome version
										 elem.value = elem.value.replace(/[^a-zA-Z0-9_]/g,'')
			                        	 elem.value = elem.value.toUpperCase();
									});

			                         $(elem).focusout(function () {
			                        		elem.value = elem.value.replace(/[^a-zA-Z0-9_]/g,'')
									});

								},size:'15', maxlength:'20'
							}
						},
						{name:'attr_name',index:'attr_name',width:80, editable:true,sortable:false,resizable:true,align:'center',
							editoptions: {
								dataInit: function (elem) {
									  $(elem).keypress(function(){
											// 특수문자 입력불가
										  return exsoft.util.filter.inputBoxFilter('[A-Za-z0-9]');
										 })
								},size:'15',maxlength:'20'
							}
						},
						{name:'attr_size',index:'attr_size',width:45, editable:true,sortable:false,resizable:true,align:'center',
							editoptions: {
								dataInit: function (elem) {

									// chrome version
									 $(elem).keyup(function(){
			                        	 elem.value =  elem.value.replace(/[^0-9]/g,'')
									});

									// chrome version
									 $(elem).focusout(function () {
									 	elem.value = elem.value.replace(/[^0-9]/g,'')
									 });

								},size:'5',maxlength:'4'
							}
						},
						{name:'sort_index',index:'sort_index',width:45, editable:true,sortable:false,resizable:true,align:'center',
							editoptions: {
								size:'5',maxlength:'2',
								dataInit: function (elem) {
									 // chrome version
									 $(elem).keyup(function(){
			                       	 elem.value =  elem.value.replace(/[^0-9]/g,'')
									});

									// chrome version
									 $(elem).focusout(function () {
									 	elem.value = elem.value.replace(/[^0-9]/g,'')
									 });
								}
							}
						},
						{name:'is_mandatory',index:'is_mandatory',width:40, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',editoptions:{value:"T:T;F:F"}},
						{name:'is_editable',index:'is_editable',width:40, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',editoptions:{value:"T:T;F:F"}},
						{name:'is_search',index:'is_search',width:40, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',editoptions:{value:"T:T;F:F"}},
						{name:'display_type',index:'display_type',width:80, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',
							editoptions:{
								value:"RADIO:RADIO;INPUT:INPUT;CHECK:CHECK;SELECT:SELECT"
									,dataEvents:[{ type:'change', fn: function(e){

										var row = jQuery(e.target).closest('tr.jqgrow');
										var rowid = row.attr('id');
										var option = $(e.target).val();

										if(option == "INPUT") {
											$("#conf"+rowid).hide();
										}else {
											$("#conf"+rowid).show();
										}

										// 문서유형 속성 GRID 값 초기화
										$("#attrGridList").setRowData(rowid,{ 'has_item': '' });
										$("#attrGridList").setRowData(rowid,{ 'default_item_index': '' });
									}}]
							}
						},
						{name:'config',index:'config',width:50, editable:false,sortable:true,resizable:true,align:'center'
							,formatter: function (cellValue, option,rowObject) {
								return "<button type='button' id='conf"+option.rowId+"' style='display:none;' onclick='javascript:exsoftAdminTypeFunc.ui.attrConfig(\""+option.rowId+"\",\"attrGridList\")'><span>설정</span></button>";
							}
						},
						{name:'has_item',index:'has_item',width:10, editable:false,sortable:true,resizable:true,align:'center',hidden:true},
						{name:'default_item_index',index:'default_item_index',width:50, editable:false,sortable:true,resizable:true,align:'center',hidden:true}
					   ],
					autowidth:true,
					height:"auto",
					viewrecords: true,
					multiselect:true,
					sortable: true,
					shrinkToFit:true,
					gridview: true,
					caption:'문서유형 등록'
					,loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('attrGridList');
					}
					,loadComplete: function() {
						exsoft.util.grid.gridInputInit(false);
					}
					,onCellSelect: function(rowid, iCol,cellcontent,e){
						exsoft.util.grid.checkBox(e);
			             $('#attrGridList').editRow(rowid,false);
				 	}
					,onSelectRow: function(rowid,status,e){
						// 에디터모드인지 체크
						var edited = exsoft.util.grid.gridEditMode('attrGridList',rowid);

						// false 이면 row 저장처리
						if(!status) {
							$('#attrGridList').jqGrid('saveRow', rowid, exsoftAdminTypeFunc.gParameters );
						}else {
							if(edited == "0") {
								$('#attrGridList').editRow(rowid,false);
							}
						}
				     },

				});

				 // Grid 컬럼정렬 처리
				var headerData = '{"attr_id":"속성ID","attr_name":"속성명","attr_size":"길이","sort_index":"정렬순서","is_mandatory":"필수","is_editable":"편집","is_search":"검색","display_type":"입력유형","config":"항목설정"}';
				exsoft.util.grid.gridColumHeader('attrGridList',headerData,'center');

				// 문서유형 속성 삭제처리( 초기화)
				var rowIDs = $("#attrGridList").jqGrid('getDataIDs');
				for (var i = 0; i < rowIDs.length ; i++) {
					$("#attrGridList").jqGrid("delRowData",rowIDs[i]);
				}
				exsoftAdminTypeFunc.gRowId = 0;

			},

			// 페이지이동 처리(공통)
			gridPage : function(nPage)	{
				$(exsoftAdminTypeFunc.gridId).setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
			},

			// 문서유형 검색처리
			searchFunc : function() {
				var postData = {strKeyword:$("#strKeyword").val(),strIndex:'TYPE_NAME',is_search:'true'} ;
				exsoft.util.grid.gridPostDataRefresh(exsoftAdminTypeFunc.gridId,exsoft.contextRoot+exsoftAdminTypeFunc.gridUrl,postData);
			},

			// 문서유형 삭제처리
			typeDel : function() {

				var jsonArr = [];
				var jsonArrIndex = 0;

				if(!exsoft.util.grid.gridSelectCheck('typeGridList'))	{
					jAlert("삭제할 문서유형을 선택하세요.",'확인',0);
					return false;
				}else {

					var id = $("#typeGridList").getGridParam('selarrrow');

					for (var i = 0; i < id.length; i++) {

						var rowData = {type_id:"",};
						var rowId = $("#typeGridList").getRowData(id[i]);

						if(rowId.is_system == 'T') {
							jAlert("시스템제공 문서유형은 삭제할 수 없습니다.","확인",0);
							return false;
						}

						rowData['type_id'] = rowId.type_id;			// jsonObject

						if(rowData.type_id){
							jsonArr[jsonArrIndex] = rowData;
							jsonArrIndex++;
						}

					}

					if (id.length > 0) {
						jConfirm('선택한  문서유형을 삭제하시겠습니까?', '확인',0,
							function(r){
								var jsonObject = { "type":"delete", "typeIdList":JSON.stringify(jsonArr)};
								if(r) {
									exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot + "/admin/typeControl.do" , "typeDelete",
											function(data,e) {
												if(data.result == "true"){
													exsoft.util.grid.gridRefresh('typeGridList',exsoft.contextRoot+exsoftAdminTypeFunc.gridUrl);
												}else {
													jAlert(data.message,'확인',0);
												}

										});
								}
							});
					 }

				}


			},

			// 문서유형 저장처리
			applyTypeWrite : function(type) {

				//  XR_TYPE 속성체크
				var gridIds = "";
				var objForm = null;

				if(type == "regist")	{
					objForm = document.frm;
					gridIds = "attrGridList";
				}else {
					objForm = document.frmView;
					gridIds = "attrViewList";
				}

				 if (objForm.type_id.value.length == 0) {
				    	jAlert("문서유형 ID를 입력하세요.","확인",0);
						return false;
				    }

				    if (objForm.type_name.value.length == 0) {
				    	jAlert("문서유형명을 입력하세요.","확인",0);
						return false;
				    }

				    if (objForm.sortIndex.value.length == 0) {
				    	jAlert("정렬순서를 입력하세요.","확인",0);
						return false;
				    }

					// XR_ATTR & XR_ATTRITEM 속성체크
					if(exsoft.util.grid.gridEditRowCnt(gridIds) == 0){
						jAlert("문서유형 속성을 입력하세요.","확인",0);
						return false;
					}

					// 문서유형 속성 Valid
					var chkVal = exsoftAdminTypeFunc.ui.validAttrItem(gridIds);
					if(chkVal) {
						// 문서유형 속성 Array 생성
						var jsonArr = exsoftAdminTypeFunc.ui.returnAttrItem(gridIds);

						// 문서유형 속성 입력값 중복체크 처리
						if(exsoft.util.check.inputArrayValid(jsonArr,'attr_id')) {
							jAlert("중복된 속성ID를 입력하셨습니다.");
							return false;
						}else if(exsoft.util.check.inputArrayValid(jsonArr,'attr_name')) {
							jAlert("중복된 속성명을 입력하셨습니다.");
							return false;
						}

						// 서버 데이터 전송처리
						exsoftAdminTypeFunc.binder.set("type_id",objForm.type_id.value);
						exsoftAdminTypeFunc.binder.set("type_name",objForm.type_name.value);
						exsoftAdminTypeFunc.binder.set("sortIndex",objForm.sortIndex.value);
						exsoftAdminTypeFunc.binder.set("attrArrayList",JSON.stringify(jsonArr));

						if(type == "regist")	{
							exsoftAdminTypeFunc.binder.set("type","insert");
							exsoftAdminTypeFunc.binder.set("is_hidden",exsoft.util.layout.getSelectBox('is_hiddenC','option'));
							exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(exsoftAdminTypeFunc.binder.getDataToJson(),exsoft.contextRoot +'/admin/typeControl.do','typeInsert',
									exsoftAdminTypeFunc.callback.returnAjaxDataFunction);
						}else {
							exsoftAdminTypeFunc.binder.set("type","update");
							exsoftAdminTypeFunc.binder.set("is_hidden",exsoft.util.layout.getSelectBox('is_hiddenU','option'));
							exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(exsoftAdminTypeFunc.binder.getDataToJson(),exsoft.contextRoot +'/admin/typeControl.do','typeUpdate',
									exsoftAdminTypeFunc.callback.returnAjaxDataFunction);
						}

					}

			},

			// 조회화면에서 문서유형 삭제처리
			typeViewDel : function() {

				var jsonArr = [];
				var rowData = {type_id:"",};

				rowData['type_id'] = $("#type_id").val();
				if(rowData.type_id){
					jsonArr[0] = rowData;
				}

				jConfirm('선택한  문서유형을 삭제하시겠습니까?', 'Confirm',0,
				function(r){
					var jsonObject = { "type":"delete", "typeIdList":JSON.stringify(jsonArr)};
					if(r) {
						exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot + "/admin/typeControl.do" , "typeDelete",
								function(data,e) {
									if(data.result == "true"){
										exsoft.util.grid.gridRefresh('typeGridList',exsoft.contextRoot+exsoftAdminTypeFunc.gridUrl);
									}else {
										jAlert(data.message,'확인',0);
									}

							});
					}
				});

			}

		},

		ui : {

			// 수정화면 취소 처리
			cancelProc : function() {
				exsoftAdminTypeFunc.ui.attrDetailCall(exsoftAdminTypeFunc.gTypeId);
			},

			// 상세화면 보기
			typeViewRefresh : function(gridIds,rowid) {
				exsoftAdminTypeFunc.gTypeId = $("#"+gridIds).getRowData(rowid).type_id;
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({ "type_id":exsoftAdminTypeFunc.gTypeId}, exsoft.contextRoot + "/admin/typeInfo.do" , "typeDetail",exsoftAdminTypeFunc.callback.returnAjaxDataFunction);
			},

			// 문서유형 수정 후 상세보기 갱신
			attrDetailCall : function(type_id) {
				exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback({ "type_id":type_id}, exsoft.contextRoot + "/admin/typeInfo.do" , "typeDetail",exsoftAdminTypeFunc.callback.returnAjaxDataFunction);
			},

			// 속성ID 컬럼 수정못하게 처리
			gridNoEditColum : function(gridIds,column,rowid) {

				var rowData = $('#'+gridIds).jqGrid('getRowData',rowid);

				if(rowData.is_locked != null && rowData.is_locked == 'T')	{
					$('#'+gridIds).setColProp(column,{editable:false});
				}else {
					$('#'+gridIds).setColProp(column,{editable:true});
				}
			},

			// 문서유형 함목 설정 SHOW/HIDE
			isConfigProc : function() {

				var rowIDs = $("#attrViewList").jqGrid('getDataIDs');

				for (var i = 0; i < rowIDs.length ; i++) {

					var row =$("#attrViewList").getRowData(rowIDs[i]);

					if(row.is_editable == 'F') {							// 편집불가항목 수정못함.
						$('#'+rowIDs[i], '#attrViewList').addClass('not-editable-row');
						$("#vconf"+rowIDs[i]).hide();
					}else {

						if((row.display_type  != null
								&& (row.display_type == "INPUT" || row.display_type == "") )) {
							$("#vconf"+rowIDs[i]).hide();
						}else {
							$("#vconf"+rowIDs[i]).show();
						}
					}

				}
			},

			isSystem : function(type_id)	{

				var rowIDs = $("#typeGridList").jqGrid('getDataIDs');

				for (var i = 0; i < rowIDs.length ; i++) {

					var rowID = rowIDs[i];
					var row =$("#typeGridList").getRowData(rowID);

					if(type_id == row.type_id && row.is_system == "T" )	{
						return true;
					}
				}
				return false;
			},

			// 문서유형 ROW ADD
			addRowReg : function(type) {

				if(type == "regist"){		// 등록화면
					exsoftAdminTypeFunc.gRowId++;

					for (var i = 0; i <= exsoftAdminTypeFunc.gRowData.length; i++) {
						$("#attrGridList").jqGrid('addRowData', exsoftAdminTypeFunc.gRowId, exsoftAdminTypeFunc.gRowData[i]);
						$('#attrGridList').editRow(exsoftAdminTypeFunc.gRowId,false);
					}

				}else {						// 조회/수정화면

					exsoftAdminTypeFunc.mRowId++;

					// 신규추가항목은 attr_id 수정모드로 변경.
					$('#attrViewList').jqGrid('setColProp', 'attr_id', {editable:true});

					for (var i = 0; i <= exsoftAdminTypeFunc.gRowData.length; i++) {
						$("#attrViewList").jqGrid('addRowData', exsoftAdminTypeFunc.mRowId, exsoftAdminTypeFunc.gRowData[i]);
						$('#attrViewList').editRow(exsoftAdminTypeFunc.mRowId,false);
					}
				}

			},

			// 문서유형 ROW DEL
			delRowReg : function(type,gridIds) {
				exsoft.util.grid.gridDeleteRow(gridIds, null, null, true);
				exsoft.util.grid.gridCheckBoxInit(gridIds);
			},

			// 문서유형속성값 유효성 체크
			validAttrItem : function(gridIds)	{

				var rowIDs = $("#"+gridIds).jqGrid('getDataIDs');

				for (var i = 0; i < rowIDs.length ; i++) {

					$('#'+gridIds).jqGrid('saveRow', rowIDs[i], exsoftAdminTypeFunc.gParameters );		// row 자동저장 처리

					var rowId =$("#"+gridIds).getRowData( rowIDs[i]);

					if(rowId.attr_id.length == 0 || rowId.attr_id.indexOf("input name") != -1) {
						jAlert("속성ID를 입력하세요.","확인",0);
						return false;
					}

					// 컬럼명은 영문으로 시작해야함 **
					if(rowId.attr_id.substring(0,1) == "_" || rowId.attr_id.substring(0,1) == "0" || rowId.attr_id.substring(0,1) == "1" || rowId.attr_id.substring(0,1) == "2" || rowId.attr_id.substring(0,1) == "3"
						|| rowId.attr_id.substring(0,1) == "4" || rowId.attr_id.substring(0,1) == "5" || rowId.attr_id.substring(0,1) == "6" || rowId.attr_id.substring(0,1) == "7" || rowId.attr_id.substring(0,1) == "8"
							|| rowId.attr_id.substring(0,1) == "9" ) {
						jAlert("속성ID는 반드시 영문자로 시작해야 됩니다.","확인",0);
						return false;
					}

					if(rowId.attr_name.length == 0 || rowId.attr_name.indexOf("input name") != -1) {
						jAlert("속성명을 입력하세요.","확인",0);
						return false;
					}

					if(rowId.attr_size.length == 0 || rowId.attr_size.indexOf("input name") != -1) {
						jAlert("길이를 입력하세요.","확인",0);
						return false;
					}

					if(rowId.sort_index.length == 0 || rowId.sort_index.indexOf("input name") != -1) {
						jAlert("정렬순서를 입력하세요.","확인",0);
						return false;
					}

				}

				return true;

			},

			// 문서유형 속성 Array 생성
			returnAttrItem : function(gridIds) {

				var jsonArr = [];
				var jsonArrIndex = 0;
				var rowIDs = $("#"+gridIds).jqGrid('getDataIDs');

				for (var i = 0; i < rowIDs.length ; i++) {

					var rowData = {attr_id:"",attr_name:"",attr_size:"",sort_index:"",is_mandatory:"",is_editable:"",is_search:"",display_type:"",has_item:"",default_item_index:""};
					var rowId =$("#"+gridIds).getRowData( rowIDs[i]);

					rowData['attr_id'] = rowId.attr_id;
					rowData['attr_name'] = rowId.attr_name;
					rowData['attr_size'] = rowId.attr_size;
					rowData['sort_index'] = rowId.sort_index;
					rowData['is_mandatory'] = rowId.is_mandatory;
					rowData['is_editable'] = rowId.is_editable;
					rowData['is_search'] = rowId.is_search;
					rowData['display_type'] = rowId.display_type;

					if(gridIds == "attrGridList")	{
						rowData['has_item'] = rowId.has_item;
					}else {
						rowData['has_item'] = rowId.has_item_list;
					}

					rowData['default_item_index'] = rowId.default_item_index;

					if(rowData.attr_id){
						jsonArr[jsonArrIndex] = rowData;
						jsonArrIndex++;
					}
				}

				return jsonArr;

			},

			// 세부항목등록
			attrItemAdd : function() {

				 var buffer = "";
				 var addFlag = false;

				 // 이전 컬럼에 값이 입력되었는지 체크한다.

				 $('input[name="item_name[]"]').each(function() {
						var aValue = $(this).val();
						if( $(this).val().length == 0){
							addFlag = true;
							return;
						}
					});

				 if(!addFlag)	{		// 기존 항목 입력완료 이후에 추가할 수 있다.

					 exsoftAdminTypeFunc.gAttrItemIdx++;

					 buffer += "<tr id='itemIdx"+exsoftAdminTypeFunc.gAttrItemIdx+"' >";
					 buffer += "<td><input type='checkbox' id='itemIdx' name='itemIdx' value='itemIdx"+exsoftAdminTypeFunc.gAttrItemIdx+"' /></td>";
					 buffer += "<td><input type='text' class='line'  name='item_name[]' id='item_name[]' size='20' maxlength='20' onkeypress='return exsoft.util.filter.inputBoxFilter(\""+"[A-Za-z0-9]"+"\");' /></td>";
					 buffer += "<td><input type='radio' class='line' name='is_default' id='is_default' value='"+exsoftAdminTypeFunc.gAttrItemIdx+"' /></td>";
					 buffer += "<input type='hidden' name='item_index[]' id='item_index[]' value='"+exsoftAdminTypeFunc.gAttrItemIdx+"'/>";
					 buffer += "</tr>";

					 $("#attrItemList").append(buffer);
				 }

			},

			//세부항목삭제
			attrItemDel : function() {
				$('input[name=itemIdx]:checked').each(function() {
					$("#"+$(this).val()).remove();
				});
			},

			//세부항목설정
			attrConfig : function(rowid,gridIds) {

				var buffer = "";
				var has_item = "";

				exsoftAdminTypeFunc.gRowItemId = rowid;

				exsoft.util.layout.divLayerOpen('register_docuCateType_wrapper', 'register_docuCateType');


				// 기 등록된 정보가 있는지 체크한다.
				if(gridIds == "attrGridList")	{
					has_item = $("#"+gridIds).getRowData(rowid).has_item;				// 등록
					exsoftAdminTypeFunc.gAttrItemType = "regist";
				}else {
					has_item = $("#"+gridIds).getRowData(rowid).has_item_list;			// 조회
					exsoftAdminTypeFunc.gAttrItemType = "update";
				}

				var default_item_index = $("#"+gridIds).getRowData(rowid).default_item_index;

				$('#attrItemList tr:gt(0)').remove();

				if(has_item != null && has_item.length != 0)		{

					// has_item =1234:1,5678:2
					// default_item_index=1

					var result = has_item.split(",");

					for (var i = 0; i < result.length; i++) {

						var attrItems = result[i].split(":");

						buffer += "<tr id='itemIdx"+attrItems[1]+"' >";
						buffer += "<td><input type='checkbox' id='itemIdx' name='itemIdx' value='itemIdx"+attrItems[1]+"' /></td>";
						buffer += "<td><input type='text' class='line'  name='item_name[]' id='item_name[]' size='20' maxlength='20' value='"+attrItems[0]+"' onkeypress='return exsoft.util.filter.inputBoxFilter(\""+"[A-Za-z0-9]"+"\");' /></td>";
						if(attrItems[1] == default_item_index) {
						 	buffer += "<td class='center'><input type='radio' class='line' name='is_default' id='is_default' value='"+attrItems[1]+"' checked/></td>";
						}else {
							 buffer += "<td class='center'><input type='radio' class='line' name='is_default' id='is_default' value='"+attrItems[1]+"' /></td>";
						}
						buffer += "<input type='hidden' name='item_index[]' id='item_index[]' value='"+attrItems[1]+"'/>";
						buffer += "</tr>";

						$("#attrItemList").append(buffer);
						exsoftAdminTypeFunc.gAttrItemIdx = attrItems[1];
						buffer = "";
					}

				}else {
					exsoftAdminTypeFunc.gAttrItemIdx = 0;
				}

				exsoft.util.common.checkboxInit('itemCheck');

			},

			 // 문서유형 항목설정 등록||수정 처리
			applyAttrItem : function() {

				// 기본여부 선택
				 if( $("input[name='is_default']:checked").length == 0 ) {
					    jAlert("기본여부를 선택하세요.","확인",0);
					    return false;
				}

				// default_item_index
				var defaultIdx = $("input[name='is_default']:checked").val();

				// has_item
				$('input[name="item_name[]"]').each(function() {
					if( $(this).val().length == 0){
					    jAlert("항목명을 입력하세요.","확인",0);
					    return false;
					}
				});

				var has_item = [];

				$('input[name="item_name[]"]').each(function(index) {

					var name = $("input[name='item_name[]']").get(index);
					var index = $("input[name='item_index[]']").get(index);
					has_item.push(name.value+":"+index.value);
				});

				// 문서유형 속성 GRID 값 변경적용
				if(exsoftAdminTypeFunc.gAttrItemType == "regist")	{
					$("#attrGridList").setRowData(exsoftAdminTypeFunc.gRowItemId,{ 'has_item': has_item });
					$("#attrGridList").setRowData(exsoftAdminTypeFunc.gRowItemId,{ 'default_item_index': defaultIdx });
				}else {
					$("#attrViewList").setRowData(exsoftAdminTypeFunc.gRowItemId,{ 'has_item_list': has_item });
					$("#attrViewList").setRowData(exsoftAdminTypeFunc.gRowItemId,{ 'default_item_index': defaultIdx });
				}

				// 문서유형속성 아이템창 닫기
				exsoft.util.layout.divLayerClose('register_docuCateType_wrapper','register_docuCateType');

			}

		},

		callback : {

			returnAjaxDataFunction : function(data,param) {

				if(param == 'typeInsert'){
					if(data.result == "true") {
						exsoft.util.grid.gridRefresh('typeGridList',exsoft.contextRoot+exsoftAdminTypeFunc.gridUrl);
						exsoft.util.layout.divLayerClose('register_docuType_wrapper','register_docuType');
					}else {
						jAlert(data.message,"확인",0);
					}
				}else if(param == 'typeUpdate'){
					if(data.result == "true") {
						jAlert("수정완료 되었습니다.","확인",0);
						exsoft.util.grid.gridRefresh('typeGridList',exsoft.contextRoot+exsoftAdminTypeFunc.gridUrl);
						exsoftAdminTypeFunc.ui.attrDetailCall(exsoftAdminTypeFunc.gTypeId);
					}else {
						jAlert(data.message,"확인",0);
					}
				}else if(param == 'typeDetail'){

					if(data.result == "true"){

						// 조회 내용 출력
						exsoftAdminTypeFunc.binder.set("attrTitle",data.typeVO.type_name);
						exsoftAdminTypeFunc.binder.set("type_id",data.typeVO.type_id);
						exsoftAdminTypeFunc.binder.set("type_name",data.typeVO.type_name);
						exsoftAdminTypeFunc.binder.set("sort_index",data.typeVO.sort_index);
						exsoftAdminTypeFunc.binder.set("create_date",data.typeVO.create_date);
						exsoftAdminTypeFunc.binder.set("is_hiddenU",data.typeVO.is_hidden);


						// BUTTON 처리 :: 기본문서형인 경우 비활성화 처리한다.
						exsoftAdminTypeFunc.init.btnStateChange(data.typeVO.is_system);

						// XR_ATTR 속성 리스트
						if($('#attrViewList')[0].grid != undefined)	{
							var postData = {type_id:exsoftAdminTypeFunc.gTypeId} ;
							exsoft.util.grid.gridPostDataRefresh('#attrViewList',exsoft.contextRoot+'/admin/attrList.do',postData);
						}else {
							exsoftAdminTypeFunc.event.fAttrViewList();
						}

					}else {
						jAlert(data.message,'확인',0);
					}
				}

			}
		}
}