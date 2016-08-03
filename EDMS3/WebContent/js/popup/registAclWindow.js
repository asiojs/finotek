var registAclWindow = {
		// Members
		
		callback : null,
		popType : null,	// create, modify, copy
		gridParameters : {
			"successfunc" : null,
		    "url" : 'clientArray',
		    "extraparam" : {},
		    "aftersavefunc" : function( response ) {},
		    "errorfunc": null,
		    "afterrestorefunc" : null,
		    "restoreAfterError" : true,
		    "mtype" : "POST"
		},
		postData : {
			acl_id : null,
			acl_name : null,
			acl_type : null,
			src_acl_name : null,
			open_id : null,
			open_name : null,
			open_isgroup : null,
			sort_index : null,
			type : null,
			aclItemArrayList : null
		},
		
		reset : function() {
			registAclWindow.popType = null;
			registAclWindow.postData = {
					acl_id : null,
					acl_name : null,
					acl_type : null,
					src_acl_name : null,
					open_id : null,
					open_name : null,
					open_isgroup : null,
					sort_index : null,
					type : null,
					aclItemArrayList : null
			};
			registAclWindow.grid.clearAclItemGrid();
		},
		
		init : function(callback, popType, aclDetail, aclItems) {
			registAclWindow.callback = callback;	// callback 저장
			registAclWindow.popType = popType;		// 등록 / 수정 구분 저장
			
			registAclWindow.ui.setTitle();			// popType에 따라 타이틀 변경 (등록 / 수정)
			registAclWindow.grid.initAclItemGrid();	// 그리드 초기화
			registAclWindow.ui.setOpenRangeRole();	// 사용자 ROLE에 따라 Radio 버튼 초기화
			
			switch (popType) {
				case "create" :
					registAclWindow.grid.addOwner();
					registAclWindow.ui.resetAclName();
					registAclWindow.ui.setOpenUser();
					break;
				case "modify" :
					registAclWindow.ui.setModifyAcl(aclDetail, aclItems);
					break;
				case "copy" :
					registAclWindow.ui.resetAclName();
					registAclWindow.ui.setCopyAcl(aclDetail, aclItems);
					break;
				default :
					jAlert("popup type이 올바르지 않습니다.");
					return;
			}
			
			registAclWindow.open();
		},
		
		open : function() {
			exsoft.util.layout.divLayerOpen("subFolder_authModifyCopy_wrapper", "subFolder_authModifyCopy");
		},
		
		close : function() {
			exsoft.util.layout.divLayerClose("subFolder_authModifyCopy_wrapper", "subFolder_authModifyCopy");
		},
		
		ajax : {
			
		},
		
		functions : {
			
		},
		
		grid : {
			addOwner : function() {
				var rowData = {
						accessor_id : 'OWNER',
						accessor_isgroup : 'F',
						accessor_isalias : 'T',
						accessor_name : '소유자',
						fol_default_acl : 'DELETE',
						fol_act_create : 'T',
						fol_act_change_permission : 'T',
						doc_default_acl : 'DELETE',
						doc_act_create : 'T',
						doc_act_cancel_checkout : 'T',
						doc_act_change_permission : 'T'
				}
				
				$("#registAclItemGridList").jqGrid("addRowData", rowData.accessor_id, rowData);
			},
			
			addRowData : function(rowData) {
				$("#registAclItemGridList").jqGrid("addRowData", rowData.accessor_id, rowData);
				
				if (this.accessor_id != "OWNER") {
					$("#registAclItemGridList").editRow(rowData.accessor_id, false);
				}
			},
			
			getSelectedItems : function(gridId) {
				var retArr = [];
				var rowIds = $("#" + gridId).jqGrid("getDataIDs");
				var _cnt = rowIds.length;
				
				for (var i = 0; i < _cnt; i++) {
					// save 후 rowId에 대한 값을 불러 온다.
					$('#'+gridId).jqGrid('saveRow', rowIds[i], registAclWindow.gridParameters );
					var _row =$("#"+gridId).getRowData(rowIds[i]);
					
					retArr[i] = {
					 		accessor_id : _row.accessor_id,
					 		accessor_isgroup : _row.accessor_isgroup,
					 		accessor_isalias : _row.accessor_isalias,
					 		accessor_name : _row.accessor_name,
					 		fol_default_acl : _row.fol_default_acl,
					 		fol_act_create : _row.fol_act_create,
					 		fol_act_change_permission : _row.fol_act_change_permission,
					 		doc_default_acl : _row.doc_default_acl,
					 		doc_act_create : _row.doc_act_create,
					 		doc_act_cancel_checkout : _row.doc_act_cancel_checkout,
					 		doc_act_change_permission : _row.doc_act_change_permission
					}
				}
				
				return retArr;
			},
			
			clearAclItemGrid : function() {
				$("#registAclItemGridList").jqGrid("clearGridData");
			},
			
			initAclItemGrid : function() {
				if ($("#registAclItemGridList")[0].grid != undefined) {
					$('#registAclItemGridList').jqGrid('GridUnload');
				}
				
				$('#registAclItemGridList').jqGrid({		
					datatype:'json',
					colNames:['accessor_id', 'accessor_isgroup', 'accessor_isalias', '접근자','기본권한','폴더등록','권한변경','기본권한','문서등록','반출취소','권한변경'],
					colModel:[
						{name:'accessor_id',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'accessor_isgroup',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'accessor_isalias',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},      
						{name:'accessor_name',index:'accessor_name',width:80, editable:false,sortable:false,resizable:true,align:'center'},
						{name:'fol_default_acl',index:'fol_default_acl',width:65, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',
							editoptions:{
								value:"DELETE:삭제;UPDATE:수정;READ:조회;BROWSE:목록"
							},formatter:'select' //formatter의 역활은 value값으로 grid에 표시함.
						},
						{name:'fol_act_create',index:'fol_act_create',width:65, editable:true,sortable:false,resizable:true,align:'center',
							edittype:'checkbox',
							editoptions:{value:'T:F'},
							fomatter:'checkbox'
						},
						{name:'fol_act_change_permission',index:'fol_act_change_permission',width:65, editable:true,sortable:false,resizable:true,align:'center',
							edittype:'checkbox',
							editoptions:{value:'T:F'},
							fomatter:'checkbox'
						},
						{name:'doc_default_acl',index:'doc_default_acl',width:65, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',
							editoptions:{
								value:"NONE:없음;DELETE:삭제;UPDATE:수정;READ:조회;BROWSE:목록"
							},formatter:'select'
						},
						{name:'doc_act_create',index:'doc_act_create',width:65, editable:true,sortable:false,resizable:true,align:'center',
							edittype:'checkbox',
							editoptions:{value:'T:F'},
							fomatter:'checkbox'
						},
						{name:'doc_act_cancel_checkout',index:'doc_act_cancel_checkout',width:65, editable:true,sortable:false,resizable:true,align:'center',
							edittype:'checkbox',
							editoptions:{value:'T:F'},
							fomatter:'checkbox'	
						},
						{name:'doc_act_change_permission',index:'doc_act_change_permission',width:65, editable:true,sortable:false,resizable:true,align:'center',
							edittype:'checkbox',
							editoptions:{value:'T:F'},
							fomatter:'checkbox'	
						},
					], 
					autowidth:true,
//					width:"500px",
					height:"auto",
					viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					caption:'접근자 목록'
					,loadBeforeSend: function() {					
						exsoft.util.grid.gridTitleBarHide('registAclItemGridList');
					}
					,gridComplete : function() {
						// url을 서버로 호출하지 않기에 loadComplete event는 발생 안함
//						exsoft.util.grid.gridInputInit(false);
//						
//						$("span.ui-jqgrid-resize.ui-jqgrid-resize-ltr").each(function(){
//							if($(this).css("height") == '48px'){
//								$(this).css("height", "");
//							}
//						});
					}
					,onCellSelect: function(rowid, iCol,cellcontent,e){
						if(exsoft.util.grid.gridIsRowDataExist('registAclItemGridList', rowid, 'accessor_id', 'OWNER')) {
							jAlert('소유자는 수정할 수 없습니다.');
							$('#registAclItemGridList').jqGrid('setSelection',rowid,false); ////checkbox 해제
						} else {
							$('#registAclItemGridList').editRow(rowid,false);
						}
				 	}
					,onSelectRow: function(rowid,status,e){
						if(!exsoft.util.grid.gridIsRowDataExist('registAclItemGridList', rowid, 'accessor_id', 'OWNER')) {
							// 에디터모드인지 체크
							var edited = exsoft.util.grid.gridEditMode('registAclItemGridList',rowid);
							// false 이면 row 저장처리
							if(!status) {											
								$('#registAclItemGridList').jqGrid('saveRow', rowid, registAclWindow.gridParameters);
							}else {
								if(edited == "0") {
									$('#registAclItemGridList').editRow(rowid,false);
								}
							}
						}
				     }
				});
				
				// 헤더 colspan
				$("#registAclItemGridList").jqGrid('destroyGroupHeader');
				$("#registAclItemGridList").jqGrid('setGroupHeaders', {
				  useColSpanStyle: true, 
				  groupHeaders:[
					{startColumnName: 'fol_default_acl', numberOfColumns: 3, titleText: '폴더권한'},
					{startColumnName: 'doc_default_acl', numberOfColumns: 4, titleText: '문서권한'}
				  ]	
				});
			}
		},
		
		event : {
			// 공유 범위 대상 라디오 클릭
			selectOpenRange : function(obj) {
				registAclWindow.postData.acl_type = $(obj).val();
				var _vars = []; // 0:open_id 1:open_name 2:open_isgroup 3:sort_index
				
				switch ($(obj).val()) {
					case Constant.ACL.TYPE_ALL :		// 전사
						_vars = ["ALL", "전사", "F", "3"]
						break;
					case Constant.ACL.TYPE_DEPT :		// 하위부서 포함
						_vars = ["", "찾기 버튼을 이용하여 공유대상을 선택하세요", "T", "1"]
						break;
					case Constant.ACL.TYPE_TEAM :		// 부서
						if (exsoft.user.acl_menu_part == Constant.MENU.MENU_TEAM) {
							_vars = [exsoft.user.manage_group_id, exsoft.user.manage_group_nm, "T", "2"];
						} else {
							_vars = ["", "찾기 버튼을 이용하여 공유대상을 선택하세요", "T", "2"];
						}
						break;
					case Constant.ACL.TYPE_PRIVATE :	// 공유 안함
						_vars = [exsoft.user.user_id, exsoft.user.user_name, "F", "4"]
						break;
				}
				
				registAclWindow.postData.open_id = _vars[0];
				registAclWindow.postData.open_name = _vars[1];
				registAclWindow.postData.open_isgroup = _vars[2];
				registAclWindow.postData.sort_index = _vars[3];
				$("#registAclWindowOpenName").val(_vars[1]);
			},
			
			// 공유 대상 선택 팝업
			selectShareTargetWindow : function() {
				// 공유 범위에 따른 분기 (전사/부서/하위부서포함/공유안함)
				switch (registAclWindow.postData.acl_type) {
				case Constant.Acl.TYPE_ALL :
					jAlert("공유범위가 '전사'는 공유대상이 전사로 지정됩니다");
					break;
				case Constant.Acl.TYPE_DEPT :
				case Constant.Acl.TYPE_TEAM :
					if (exsoft.user.acl_menu_part == Constant.MENU.MENU_GROUP) {
						// 그룹윈도우 팝업(관리대상부서)
						selectSingleFolderWindow.init(function(){}, "GROUP", exsoft.user.manage_group_id);
					} else {
						selectSingleFolderWindow.init(function(){}, "GROUP");
					}
					break;
				case Constant.Acl.TYPE_PRIVATE :
					selectSingleUserWindow.init.initSingleUserWindow()
					break;
				}
			},
			
			// AclItem 추가 (접근자 선택 팝업)
			addAclItemRow : function() {
				selectAccessorWindow.init(registAclWindow.callbackFunctions.selectAccessorCallback, "registAclItemGridList");

			},
			
			// AclItem 제외
			deleteAclItemRow : function() {
				exsoft.util.grid.gridDeleteRow('registAclItemGridList', 'OWNER', '소유자는 삭제할 수 없습니다.', false);
				exsoft.util.grid.gridCheckBoxInit('registAclItemGridList');
			},
			
			submit : function() {
				if ($("#registAclWindowAclName").val().length == 0) {
					jAlert("권한명을 입력하세요.");
					return;
				}
				
				if (registAclWindow.postData.open_id == 0 || registAclWindow.postData.open_name == 0) {
					jAlert("공개범위를 선택하세요");
					return;
				}
				
				registAclWindow.postData.acl_name = $("#registAclWindowAclName").val();
				registAclWindow.postData.aclItemArrayList = JSON.stringify(registAclWindow.grid.getSelectedItems("registAclItemGridList"));
				
				switch (registAclWindow.popType) {
					case "create" :
					case "copy" :
						registAclWindow.postData.type = "insert";
						exsoft.util.ajax.ajaxDataFunctionWithCallback(registAclWindow.postData, exsoft.contextRoot+"/permission/aclControl.do", "create", registAclWindow.callback);
						break;
					case "modify" :
						registAclWindow.postData.type = "update";
						exsoft.util.ajax.ajaxDataFunctionWithCallback(registAclWindow.postData, exsoft.contextRoot+"/permission/aclControl.do", "modify", registAclWindow.callback);
						break;
				}

				registAclWindow.reset();
				registAclWindow.close();
			},
			
			cancel : function() {
				registAclWindow.reset();
				registAclWindow.close();
			}
		},
		
		ui : {
			resetAclName : function() {
				$("#registAclWindowAclName").val("");
			},
			
			setTitle : function() {
				if (registAclWindow.popType == "create" || registAclWindow.popType == "copy") {
					$("#registAclWindowTitle").text("[권한] 등록");
				} else {
					$("#registAclWindowTitle").text("[권한] 수정");					
				}
			},
			
			setOpenUser : function() {
				registAclWindow.postData.acl_type = "PRIVATE";
				registAclWindow.postData.open_id = exsoft.user.user_id;
				registAclWindow.postData.open_name = exsoft.user.user_name;
				registAclWindow.postData.open_isgroup = "F";
				registAclWindow.postData.sort_index = "4";
				
				$("#registAclWindowOpenName").val(exsoft.user.user_name);
			},
			
			setModifyAcl : function(aclDetail, aclItems) {
				registAclWindow.postData.acl_id = aclDetail.acl_id;
				registAclWindow.postData.acl_type = aclDetail.acl_type;
				registAclWindow.postData.acl_name = aclDetail.acl_name;
				registAclWindow.postData.src_acl_name = aclDetail.acl_name;
				registAclWindow.postData.open_id = aclDetail.open_id;
				registAclWindow.postData.open_name = aclDetail.open_name;
				registAclWindow.postData.open_isgroup = aclDetail.open_isgroup;
				registAclWindow.postData.sort_index = aclDetail.sort_index;
				
				$("input[name=registAclWindowOpenRange][value='{0}'".format(aclDetail.acl_type)).prop("checked", true);
				$("#registAclWindowOpenName").val(aclDetail.open_name);
				$("#registAclWindowAclName").val(aclDetail.acl_name);
				registAclWindow.grid.clearAclItemGrid();
				
				$(aclItems).each(function() {
					registAclWindow.grid.addRowData(this);
				});
			},

			setCopyAcl : function(aclDetail, aclItems) {
				registAclWindow.postData.acl_type = aclDetail.acl_type;
				registAclWindow.postData.open_id = aclDetail.open_id;
				registAclWindow.postData.open_name = aclDetail.open_name;
				registAclWindow.postData.open_isgroup = aclDetail.open_isgroup;
				registAclWindow.postData.sort_index = aclDetail.sort_index;
				
				$("input[name=registAclWindowOpenRange][value='{0}'".format(aclDetail.acl_type)).prop("checked", true);
				$("#registAclWindowOpenName").val(aclDetail.open_name);
				
				registAclWindow.grid.clearAclItemGrid();
				
				$(aclItems).each(function() {
					registAclWindow.grid.addRowData(this);
				});
			},
			
			setOpenRangeRole : function() {
				var disableArr = [];
				
				switch (exsoft.user.acl_menu_part) {
					case Constant.ROLE.SYSTEM_ROLE :
					case Constant.MENU.MENU_ALL :
						disableArr = [true, true, true, true, true];
						break;
					case Constant.MENU.MENU_GROUP :
						disableArr = [false, false, false, true, true];
						break;
					case Constant.MENU.MENU_TEAM :
						disableArr = [false, false, true, true, false];
						break;
					default :
						disableArr = [false, true, true, true, false];
						break;
				}
				
				$("input[name=registAclWindowOpenRange][value='PRIVATE'").attr("disabled", disableArr[0]);
				$("input[name=registAclWindowOpenRange][value='TEAM'").attr("disabled", disableArr[1]);
				$("input[name=registAclWindowOpenRange][value='DEPT'").attr("disabled", disableArr[2]);
				$("input[name=registAclWindowOpenRange][value='ALL'").attr("disabled", disableArr[3]);
				
				registAclWindow.ui.setVisibleGroupSearch(disableArr[4]);
			},
			
			setVisibleGroupSearch : function(isVisible) {
				if (isVisible) {
					$("#registAclWindowSearch").show();
				} else {
					$("#registAclWindowSearch").hide();
				}
			}
		},
		
		callbackFunctions : {
			selectGroup : function(groupInfo) {
				if (groupInfo[0].mapId == "PROJECT" && registAclWindow.postData.acl_type == Constant.ACL.TYPE) {
					jAlert("프로젝트 그룹은 하위부서 포함 공개범위를 선택할 수 없습니다.");
					return;
				}
				
				registAclWindow.postData.open_id = returnGroup[0].id;
				registAclWindow.postData.open_name = returnGroup[0].text;
				registAclWindow.postData.open_isgroup = "T";
				
				$("#registAclWindowOpenName").val(returnGroup[0].text);
			},
			
			selectUser : function(userInfo) {
				registAclWindow.postData.open_id = userInfo.user_id;
				registAclWindow.postData.open_name = userInfo.user_name_ko;
				registAclWindow.postData.open_isgroup = "F";
				
				$("#registAclWindowOpenName").val(userInfo.user_name_ko);
			},
			
			selectAccessorCallback : function(aclItemRowList) {
				exsoft.util.grid.gridSetAclItemAddCallback("registAclItemGridList", aclItemRowList);
			}
		}
}