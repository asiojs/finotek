/**
 * 환경설정 관련 스크립트
 */
var exsoftAdminMenuFunc = {

		pageSize : "",
		gRoleId : "",
		gRoleNm : "",
		gridId : "",
		gBrowser : exsoft.util.layout.browser(),

		init : {

			initPage : function(pageSize,gridId) {

				exsoftAdminMenuFunc.pageSize = pageSize;
				exsoftAdminMenuFunc.gridId = gridId;
			}

		},

		open : {

			// ROLE ADD
			writeFunc : function() {

				exsoft.util.layout.divLayerOpen('user_role_wrapper', 'user_role');
				exsoft.util.common.formClear('frm');
				$("#type").val('insert');
				$("#code_id").removeClass("readonly");
				$("#code_id").prop("readonly",false);
			}

		},

		layer : {

		},

		close : {

			// ROLE DEL
			delFunc : function() {

				var id = $("#roleGrid").getGridParam('selarrrow');
				var data = "";

				for (var i = 0; i < id.length; i++) {
					var rowdata = $("#roleGrid").getRowData(id[i]);
					if(rowdata.is_sys == 'Y') {
						jAlert("시스템제공 작업권한은 삭제할 수 없습니다.",'알림',0);
						return false;
					}

					data += rowdata.code_id + ',';                 // 선택된 데이터를 변수에 넣어서 "," 를 붙여가면서

				}	// String 객체를 만든다. (ex: id1,id3,id7,id8)

				 if(id.length==0){
					 jAlert("삭제할 Role ID를 선택하세요",'알림',0);
					 return false;
				 }else {
			 		if (id.length > 0) {
						jConfirm('선택한 Role ID를 삭제하시겠습니까?', 'Role삭제',0,
							function(r){
								var jsonObject = { "type":"delete", "inputStr":data};
								if(r) exsoft.util.ajax.ajaxDataFunctionNoLodingWithCallback(jsonObject, exsoft.contextRoot+"/admin/codeDelete.do", 'roleDelete',
										function(data, e){
											if(data.result == "true")	{
												exsoft.util.grid.gridRefresh('roleGrid',exsoft.contextRoot+'/admin/codePage.do');
											}else {
												jAlert(data.message,'확인',0);
											}
									});
							}
						);
					 }
				 }
			},

		},

		event : {

			// 작업권한(Role) 유형 TreeGrid
			roleGridList : function() {
				$('#roleGrid').jqGrid({
					url:exsoft.contextRoot+'/admin/codePage.do',
					mtype:"post",
					datatype:'json',
					jsonReader:{page:'page',total:'total',root:'list'},
					colNames:['code_id','code_nm','is_sys'],
					colModel:[
						{name:'code_nm',index:'code_nm',width:50, editable:false,sortable:true,resizable:true},
						{name:'code_id',index:'code_id',width:60, editable:false,sortable:true,resizable:true,hidden:false},
						{name:'is_sys',index:'is_sys',width:10, editable:false,sortable:false,resizable:true,align:'center',hidden:true}
					],
					autowidth:true,
					height:"auto",
					viewrecords: true,
					multiselect:true,
					sortable: true,
					shrinkToFit:true,
					gridview: true,
					sortname : "code_nm",
					sortorder:"desc",
					scrollOffset:0,
					viewsortcols:'vertical',
					rowNum : exsoftAdminMenuFunc.pageSize,
					multikey: "ctrlKey",
					emptyDataText: "데이터가 없습니다.",
					caption:'작업권한 유형'
					,ondblClickRow: function(rowid,iRow,iCol,e){

						if(iCol == 0){
							$("#roleGrid").jqGrid('setSelection',rowid);
						}else {

							// 수정FORM
							exsoft.util.layout.divLayerOpen('user_role_wrapper', 'user_role');
							exsoft.util.common.formClear('frm');

							$("#type").val("update");
							$("#code_id").val($("#roleGrid").getRowData(rowid).code_id);
							$("#code_nm").val(exsoft.util.common.stripHTMLtag($("#roleGrid").getRowData(rowid).code_nm));
							$("#code_id").addClass("readonly");
							$("#code_id").prop("readonly",true);
							$("#code_nm").focus();
						}
					}
					,onCellSelect : function(rowid,iCol,cellcontent,e){

						exsoft.util.grid.checkBox(e);			// CheckBox Browser Bug Fix

						if(iCol == 0){
							$("#roleGrid").jqGrid('setSelection',rowid);

						}else {

							exsoftAdminMenuFunc.gRoleId = $("#roleGrid").getRowData(rowid).code_id;
							exsoftAdminMenuFunc.gRoleNm = $("#roleGrid").getRowData(rowid).code_nm;

							exsoft.util.grid.gridPostDataRefresh('menuAuthGrid',exsoft.contextRoot+'/admin/menuAuth.do', {role_id:exsoftAdminMenuFunc.gRoleId});

							// 접근권한,메뉴목록 체크박스 초기화
							if(exsoftAdminMenuFunc.gBrowser.name == "mozilla")	{
								exsoft.util.grid.treeGridChckAllReleaseFF('checkAll');
								exsoft.util.grid.treeGridChckAllReleaseFF('menuAllCheck');
								exsoft.util.grid.gridAllCheckFF('menuAllCheck','midx');
							}else {
								exsoft.util.grid.treeGridChckAllRelease(event,'checkAll');
								exsoft.util.grid.treeGridChckAllRelease(event,'menuAllCheck');
								exsoft.util.grid.gridAllCheck(event,'menuAllCheck','midx');
							}
							// 선택된 row '>' 표시
							//console.log("rowid======="+rowid);
				            //$("#select_list").remove();
				            //$("#"+rowid).find('td:eq(1)').prepend("<span id='select_list' class='select_list_icon'></span>");
						}

					}
					,loadBeforeSend: function() {
						exsoft.util.grid.gridTitleBarHide('roleGrid');
						exsoft.util.grid.gridNoDataMsgInit('roleGrid');
					}
					,loadComplete: function(data) {

						exsoft.util.grid.gridPagerViewHide('roleGrid');

						if (this.p.records === 0) {
							exsoft.util.grid.gridNoRecords('roleGrid','no_data');
			           		postData = {role_id:'NoData'} ;
			           		exsoftAdminMenuFunc.gRoleNm = "데이터가 없습니다";
						}else {
							exsoft.util.grid.gridPagerShow('roleGrid');
							var rowId = $("#roleGrid").getDataIDs()[0];
							var gRoleId = $("#roleGrid").getRowData(rowId).code_id;
							exsoftAdminMenuFunc.gRoleNm = $("#roleGrid").getRowData(rowId).code_nm;
							postData = {role_id:exsoftAdminMenuFunc.gRoleId} ;
						}

						// 메뉴권한목록 refresh
						exsoft.util.grid.gridPostDataRefresh('menuAuthGrid',exsoft.contextRoot+'/admin/menuAuth.do',postData);

						// 메뉴권한목록 체크박스 초기화
						if(exsoftAdminMenuFunc.gBrowser.name == "mozilla")	{
							exsoft.util.grid.treeGridChckAllReleaseFF('checkAll');
							exsoft.util.grid.treeGridChckAllReleaseFF('menuAllCheck');
							exsoft.util.grid.gridAllCheckFF('menuAllCheck','midx');
						}else {
							exsoft.util.grid.treeGridChckAllRelease(event,'checkAll');
							exsoft.util.grid.treeGridChckAllRelease(event,'menuAllCheck');
							exsoft.util.grid.gridAllCheck(event,'menuAllCheck','midx');
						}

						exsoft.util.grid.gridInputInit(false);
						exsoft.util.grid.gridPager("#rolePager",data);
					}
					,gridComplete : function() {

						var rowId = $("#roleGrid").getDataIDs()[0];

						exsoftAdminMenuFunc.gRoleId = $("#roleGrid").getRowData(rowId).code_id;
						exsoftAdminMenuFunc.gRoleNm = $("#roleGrid").getRowData(rowId).code_nm;
						exsoftAdminMenuFunc.event.menuAuthGridList(exsoftAdminMenuFunc.gRoleId);
					}
					 ,loadError:function(xhr, status, error) {
						 exsoft.util.error.isErrorChk(xhr);
					 }
				});

				 // Grid 컬럼정렬 처리
				 var headerData = '{"code_nm":"역할명","code_id":"역할ID"}';
				 exsoft.util.grid.gridColumHeader('roleGrid',headerData,'center');

			},

			// 메뉴목록 TreeGrid
			menuGridList : function() {

				var checkOption = "";

				$('#menuGrid').jqGrid({
					url:exsoft.contextRoot+'/admin/menuList.do',
					mtype:"post",
					datatype: "json",
			        autowidth:true,
			        height:"auto",
					colNames:['<input type="checkbox" id="menuAllCheck" onclick="exsoft.util.grid.gridAllCheck(event,\'menuAllCheck\'\,\'midx\')" />','menu_cd','menu_nm'],
					colModel:[
						{name:'checkBox',index:'checkBox',width:10, editable:false,sortable:false,align:'center',
							formatter: function (cellValue, option) {
								return '<input type="checkbox" id="midx" name="midx" value="' +cellValue +  '" onclick="javascript:exsoftAdminMenuFunc.ui.checkBoxChanged('+option.rowId+',\'midx\',event);">';


						}},
						{name:'menu_cd',index:'menu_cd',width:1, editable:false,sortable:false,key:true,align:'center',hidden:true},
						{name:'menu_nm',index:'menu_nm',width:130, editable:false,sortable:false},
							   ],
					treeReader: {
						level_field:        	"level",
						parent_id_field:    "parent",
						leaf_field:            "isLeaf",
						expanded_field:   	"expanded"
					},
					jsonReader: {
					repeatitems: false,
				         	root:'list'
			        },
			        gridview: true,
			        treeGrid: true,
			        loadonce: true,
			        scrollOffset:0,
			        treeGridModel: 'adjacency',
			        ExpandColumn: 'menu_nm',
			        caption:'메뉴목록',
			        treeIcons: {leaf:'ui-icon-document'},
			        loadBeforeSend: function() {
			        	exsoft.util.grid.gridTitleBarHide('menuGrid');
					}
			        ,loadComplete: function() {
			        	exsoft.util.grid.gridInputInit(false);
			        }
				});

				 // Grid 컬럼정렬 처리
				 var headerData = '{"menu_nm":"메뉴명"}';
				 exsoft.util.grid.gridColumHeader('menuGrid',headerData,'center');


			},

			// 접근권한 TreeGrid
			menuAuthGridList : function(role_id)	{

				var checkOption = "";
				var radioDisable = "";

				$('#menuAuthGrid').jqGrid({
					url:exsoft.contextRoot+'/admin/menuAuth.do',
					mtype:"post",
					datatype: "json",
					height: "auto",
					autowidth: true,
					colNames:['<input type="checkbox" id="checkAll"  onclick="exsoft.util.grid.gridAllCheck(event,\'checkAll\'\,\'idx\')"  />','menu_cd','menu_nm','part','all','group','team','role_id','gcode_id'],
					colModel:[
								{name:'checkBox',index:'checkBox',width:15, editable:false,sortable:false,align:'center'
									,formatter: function (cellValue, option) {
										return '<input type="checkbox" id="idx" name="idx" value="' +cellValue +  '"  onclick="javascript:exsoftAdminMenuFunc.ui.checkBoxChanged('+option.rowId+',\'idx\',event);">';
									}},
								{name:'menu_cd',index:'menu_cd',width:50, align:'center',editable:false,sortable:false,key:true,hidden:true},
								{name:'menu_nm',index:'menu_nm',width:120, editable:false,sortable:false},
							    {name:'part',index:'part',width:30, align:'center',editable:false,sortable:false,hidden:true},
							    {name: 'all', index: 'all', width: 60, align: 'center', editable:false, edittype:"radio", sortable:false,classes:'pss-jqgrid-pointer-nodecoration',
							    	formatter: function (cellValue, option,rowObject) {
							    		return exsoft.util.grid.radioFormatter(rowObject.menu_level,option.rowId,cellValue,"ALL");
							    	}},
							    {name: 'group', index: 'group', width: 60, align: 'center', editable:false, edittype:"radio", sortable:false,classes:'pss-jqgrid-pointer-nodecoration',
							    	formatter: function (cellValue, option,rowObject) {
							    		return exsoft.util.grid.radioFormatter(rowObject.menu_level,option.rowId,cellValue,"GROUP");
							    	}},
							    {name: 'team', index: 'team', width: 60, align: 'center', editable:false, edittype:"radio", sortable:false,classes:'pss-jqgrid-pointer-nodecoration',
							    	formatter: function (cellValue, option,rowObject) {
							    		return exsoft.util.grid.radioFormatter(rowObject.menu_level,option.rowId,cellValue,"TEAM");
									}} ,
								{name:'role_id',index:'menu_nm',width:150, editable:false,sortable:false,hidden:true},
								{name:'gcode_id',index:'menu_nm',width:150, editable:false,sortable:false,hidden:true}
							   ],
					treeReader: {
						level_field:        	"level",
						parent_id_field:    "parent",
						leaf_field:            "isLeaf",
						expanded_field:   	"expanded"
					},
					jsonReader: {
			            repeatitems: false,
			          	root:'list'
			        },
			        gridview: true,
			        treeGrid: true,
			        loadonce: true,
			        treeGridModel: 'adjacency',
			        ExpandColumn: 'menu_nm',
			        emptyDataText: "데이터가 없습니다.",
			        caption: '접근권한',
			        postData: {role_id:role_id},
			        treeIcons: {leaf:'ui-icon-document'}
			    	,loadBeforeSend: function() {
			    		exsoft.util.grid.gridTitleBarHide('menuAuthGrid');
			    		exsoft.util.grid.gridNoDataMsgInit('menuAuthGrid');
					}
			    	,loadComplete: function() {
			    		if ($("#menuAuthGrid").getGridParam("records")==0) {
			    			exsoft.util.grid.gridNoDataMsg('menuAuthGrid','nolayer_data');
			    		}
			    		// 선택 접근권한 명 변경처리
			    		$("#roleTitle").html(exsoftAdminMenuFunc.gRoleNm);

			    		exsoft.util.grid.gridInputInit(false);
			    	}
				});

				 var headerData = '{"menu_nm":"사용가능메뉴","all":"전사","group":"하위부서포함","team":"소속부서"}';
				 exsoft.util.grid.gridColumHeader('menuAuthGrid',headerData,'center');
			},


			// 	페이지이동 처리(공통)
			gridPage : function(nPage)	{
				$(exsoftAdminMenuFunc.gridId).setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
			},

			// 권한 등록/수정 처리
			managerFunc : function() {

				var objForm = document.frm;

				if(objForm.code_id.value.length == 0){
					objForm.code_id.focus();
					jAlert('Role ID를 입력하세요','확인',0);
					return false;
				}

				if(objForm.code_nm.value.length == 0){
					objForm.code_nm.focus();
					jAlert('Role 명을 입력하세요','확인',0);
					return false;
				}

				if($("#type").val() == "update") {
					exsoft.util.ajax.ajaxFunctionWithCallback('frm',exsoft.contextRoot+'/admin/codeWrite.do','update',exsoftAdminMenuFunc.callback.returnAjaxFunction);
				}else {
					exsoft.util.ajax.ajaxFunctionWithCallback('frm',exsoft.contextRoot+'/admin/codeWrite.do','insert',exsoftAdminMenuFunc.callback.returnAjaxFunction);
				}

			},

			// 검색처리
			searchFunc :function() {
				exsoft.util.grid.gridPostDataRefresh('roleGrid',exsoft.contextRoot+'/admin/codePage.do',{strKeyword:$("#strKeyword").val(),is_search:'true'});
			},

			// 접근권한 수정하기
			modifyFunc : function() {

				var data = "";
				var cnt = 0;

				$('input[name=idx]:checked').each(function() {

					var str = $(this).val()
					var dotPos = str.split("#");
					var menu_cd = dotPos[2];
					var selectVal = $("input:radio[name=radio_"+menu_cd+"]:checked").val();
					var lastStr = str + "#"+selectVal;

					data += lastStr + ",";
					cnt++;

				});

				if(cnt == 0)	{
					jAlert('수정할 권한을 선택하세요.');
					return false;
				}

				var jsonObject = { "type":"update", "inputStr":data};
				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject,exsoft.contextRoot+'/admin/menuAuthManager.do','menuAuthUpdate',exsoftAdminMenuFunc.callback.returnAjaxFunction);
			}

		},

		ui : {

			// 대문자
			upper : function(obj) {
				var code = obj.value;
			    code=code.replace(/[^A-Z]/g,"");
			    obj.value=code;
			},

			// 메뉴목록 체크박스
			checkBoxChanged : function(rowId,checkNm,e) {
				e = e || event;
				e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;

				$("input:checkbox[name='"+checkNm+"']").each(function() {
					if($(this).val().indexOf(rowId) != -1) {
						$(this).prop("checked",true);
					}
			    });
			},

			// 메뉴권한등록처리
			treeGridAdd : function() {

				var data = "";
				var cnt = 0;

				if(exsoftAdminMenuFunc.gRoleId == '')	{
					jAlert('등록할 권한목록을 선택하세요','확인',0);
					return false;
				}

				$('input[name=midx]:checked').each(function() {
					var str = $(this).val()
					data += str + ",";
					cnt++;
				});

				if(cnt == 0)	{
					jAlert('등록할 메뉴를 선택하세요.','확인',0);
					return false;
				}

				var jsonObject = { "type":"insert", "inputStr":data,"role_id":exsoftAdminMenuFunc.gRoleId};
				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject,exsoft.contextRoot+'/admin/menuAuthManager.do','menuAuthInsert',exsoftAdminMenuFunc.callback.returnAjaxFunction);
			},

			// 메뉴권한삭제처리
			treeGridDel : function() {

				var data = "";
				var cnt = 0;

				$('input[name=idx]:checked').each(function() {
					var str = $(this).val()
					data += str + ",";
					cnt++;
				});

				if(cnt == 0)	{
					jAlert('삭제할 권한을 선택하세요.','확인',0);
					return false;
				}

				var jsonObject = { "type":"delete", "inputStr":data};

				exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject,exsoft.contextRoot+'/admin/menuAuthManager.do','menuAuthDelete',exsoftAdminMenuFunc.callback.returnAjaxFunction);

				if(exsoftAdminMenuFunc.gBrowser.name == "mozilla")	{
					exsoft.util.grid.treeGridChckAllReleaseFF('checkAll');
				}else {
					exsoft.util.grid.treeGridChckAllRelease(event,'checkAll');
				}

			}

		},

		callback : {

			// CallBack 처리
			returnAjaxFunction : function(data,param)	{

				if(param == 'insert'){
					if(data.result == "true") {
						var postData = {strKeyword : $("#strKeyword").val() } ;
						exsoft.util.grid.gridPostDataRefresh('roleGrid',exsoft.contextRoot+'/admin/codePage.do',postData);
						exsoft.util.layout.divLayerClose('user_role_wrapper', 'user_role');
					}else {
						jAlert(data.message,'확인',0);
					}
				}else if(param == 'update'){
					if(data.result == "true") {
						var postData = {strKeyword : $("#strKeyword").val() } ;
						exsoft.util.grid.gridPostDataRefresh('roleGrid',exsoft.contextRoot+'/admin/codePage.do',postData);
						exsoft.util.layout.divLayerClose('user_role_wrapper', 'user_role');
					}else {
						jAlert(data.message,'확인',0);
					}
				}else if(param == 'menuAuthInsert'){
					// 접근권한 등록처리
					if(data.result == "true")	{
						exsoft.util.grid.gridRefresh('menuAuthGrid',exsoft.contextRoot+'/admin/menuAuth.do');
						if(exsoftAdminMenuFunc.gBrowser.name == "mozilla")	{
							exsoft.util.grid.treeGridChckAllReleaseFF('menuAllCheck');
							exsoft.util.grid.gridAllCheckFF('menuAllCheck','midx');
						}else {
							exsoft.util.grid.treeGridChckAllRelease(event,'menuAllCheck');
							exsoft.util.grid.gridAllCheck(event,'menuAllCheck','midx');
						}
					}else {
						jAlert(data.message,'확인',0);
					}
				}else if(param == 'menuAuthDelete')	{
					// 메뉴권한삭제처리
					if(data.result == "true")	{
						exsoft.util.grid.gridRefresh('menuAuthGrid',exsoft.contextRoot+'/admin/menuAuth.do');
					}else {
						jAlert(data.message,'확인',0);
					}
				}else if(param == "menuAuthUpdate") {
					// 접근권한 수정처리
					if(data.result == "true")	{
						exsoft.util.grid.gridRefresh('menuAuthGrid',exsoft.contextRoot+'/admin/menuAuth.do');
					}else {
						jAlert(data.message,'확인',0);
					}
				}
			}

		}
}