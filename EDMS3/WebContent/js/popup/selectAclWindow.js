var selectAclWindow = {
		// Members
		type : null,			// 권한 선택 팝업 타입 "FOLDER", "DOC" 두개의 타입으로 Show/Hide할 Div를 결정함
		aclType : "public",		// 공개권한(public) / 개인권한(private) 탭 분리
		srcAclId : null,		// 원본 Acl ID
		callback : null,		// 반환함수
		selAclInfo : {
			aclId : null,		// 선택 Acl ID
			aclItems : null,	// 선택 Acl Item 목록
			aclDetail : null,	// 상세 Acl 정보
			extAclItems : null,	// 추가 접근자 Acl Item 목록
		},
		extParams : null,
		
		// 0. 초기화
        init : function(aclId, type, callback){
        	selectAclWindow.type = type;
        	selectAclWindow.callback = callback;
        	selectAclWindow.srcAclId = aclId;
        	selectAclWindow.selAclInfo.aclId = aclId;
        	selectAclWindow.open();
        	
        	selectAclWindow.grid.aclList();
        	selectAclWindow.ajax.getAclDetail(aclId);
        	
        	selectAclWindow.ui.setEnableRadio(aclId == "" ? true : false);
        },
        initDocument : function(aclId, type, docId, callback) {
        	selectAclWindow.init(aclId, type, callback);
        	
        	selectAclWindow.ajax.getExtAclList(docId);
        },
        
        initInherit : function(obj) {
        	/**
        	 * obj parameters
        	 * - current_acl_id
        	 * - current_acl_name
        	 * - parent_folder_id
        	 * - folder_id
        	 * - type
        	 */
        	
        	
        	var jo = {
        			type : obj.type,
        			folder_id : obj.folder_id,
        			parent_folder_id : obj.parent_folder_id
        	};
        	
        	exsoft.util.ajax.ajaxDataFunctionWithCallback(jo, exsoft.contextRoot+"/permission/aclInheritDetail.do", obj, function(data, param){
        		var _tarFormId = selectAclWindow.functions.getTargetFormId(); 
        		$(_tarFormId + " #pCurAclName").text(param.current_acl_name);
        		$(_tarFormId + " #pInheritAclName").text(data.aclDetail.acl_name);
        		$(_tarFormId + " #pCurAclId").val(param.current_acl_id);
        		$(_tarFormId + " #pInheritAclId").val(data.aclDetail.acl_id);
        	})
        },

        // 1. 팝업
        open : function() {
        	if (selectAclWindow.functions.isDocType())
        		exsoft.util.layout.divLayerOpen("folder_authModify_wrapper", "doc_folder_authModify");
        	else if (selectAclWindow.functions.isFolderType())
        		exsoft.util.layout.divLayerOpen("subFolder_authModify_wrapper", "subFolder_authModify");
        },

        // 2. 닫기 + hide
        close : function() {
        	if (selectAclWindow.functions.isDocType())
        		exsoft.util.layout.divLayerClose("folder_authModify_wrapper", "doc_folder_authModify");
        	else if (selectAclWindow.functions.isFolderType())
        		exsoft.util.layout.divLayerClose("subFolder_authModify_wrapper", "subFolder_authModify");
        },

        // Ajax
        ajax : {
        	getAclDetail : function(aclId) {
        		if(aclId == "")	return;
        		
				exsoft.util.ajax.ajaxDataFunctionWithCallback({"acl_id" : aclId}, exsoft.contextRoot+"/permission/aclItemList.do", "", function(data, param) {
					selectAclWindow.selAclInfo.aclItems = data.list;
					selectAclWindow.selAclInfo.aclDetail = data.aclDetail;
					
					if (selectAclWindow.functions.isDocType()) {
						exsoft.util.table.tableDocumentAclItemPrintList(selectAclWindow.functions.getTargetAclDetailTableId(true), data.list);
					} else if (selectAclWindow.functions.isFolderType()){
						exsoft.util.table.tableFolderAclItemPrintList(selectAclWindow.functions.getTargetAclDetailTableId(true), data.list);
					}
				});
			},
			getExtAclList : function(docId) {
				if(docId == "") {
					selectAclWindow.grid.extAclList("");
					return;
				}
				
				exsoft.util.ajax.ajaxDataFunctionWithCallback({"doc_id" : docId}, exsoft.contextRoot+"/permission/exAclItemList.do", "", function(data, param) {
					selectAclWindow.selAclInfo.extAclItems = data.list;
					selectAclWindow.grid.extAclList(data.list);
				});
			}
        },
        
        // 함수 목록
        functions : {
        	isDocType : function() {
        		if (selectAclWindow.type === Constant.ACL.TYPE_DOC) {
        			return true;
        		}
        		return false;
        	},
        	
        	isFolderType : function() {
        		if (selectAclWindow.type === Constant.ACL.TYPE_FOLDER) {
        			return true;
        		}
        		return false;
        	},
        	
        	getTargetAclTableId : function(isSelector) {
        		var prefix = isSelector ? "#" : "";
        		if (selectAclWindow.functions.isDocType()) {
        			return prefix + "selAclWindowDocAclList";
        		} else if (selectAclWindow.functions.isFolderType()) {
        			return prefix + "selAclWindowFolderAclList"
        		}
        	},
        	
        	getTargetAclDetailTableId : function(isSelector) {
        		var prefix = isSelector ? "#" : "";
        		if (selectAclWindow.functions.isDocType()) {
        			return prefix + "selAclWindowDocAclItemList";
        		} else if (selectAclWindow.functions.isFolderType()) {
        			return prefix + "selAclWindowFolderAclItemList"
        		}
        	},
        	
        	getTargetFormId : function() {
        		if (selectAclWindow.functions.isDocType()) {
        			return "#pRegDoc";
        		} else if (selectAclWindow.functions.isFolderType()) {
        			return "#pRegFolder";
        		}
        	}
        },
        
        grid : {
        	aclListRefresh : function() {
        		exsoft.util.grid.gridRefresh(selectAclWindow.functions.getTargetAclTableId(), exsoft.contextRoot + '/permission/aclList.do');
        	},
        	
        	// 사용 가능한 ACL 목록
        	aclList : function() {
        		$(selectAclWindow.functions.getTargetAclTableId(true)).jqGrid({
        			url: exsoft.contextRoot+'/permission/aclList.do',
        			mtype:"post",
        			datatype:'json',		
        			jsonReader:{
        				root:'list'
        			},
        			postData:{
        				acl_id : selectAclWindow.selAclInfo.aclId,
        				type : selectAclWindow.aclType
        			},
        			colNames:['','권한명','공개대상','공개범위','정렬','공개범위ID','공개대상ID','그룹여부','등록자'],
        			colModel:[
        				{name:'acl_id',index:'acl_id',width:5, align:'center',editable:false,sortable:false,key:true,edittype:'radio',
        					   formatter:function(cellValue, option) {
        						   return '<input type="radio" name="radio_selectAcl" value="'+cellValue+'" onclick="selectAclWindow.event.selectAclList(\'' + cellValue + '\')"/>';
        					   },hidden:false
        				},      
        				{name:'acl_name',index:'acl_name',width:80, editable:false,sortable:true,resizable:true},
        				{name:'open_name',index:'open_name',width:50, editable:false,sortable:true,resizable:true,align:'center'},
        				{name:'acl_type_name',index:'acl_type_name',width:30, editable:false,sortable:false,resizable:true,align:'center',hidden:true},
        				{name:'sort_index',index:'sort_index',width:3, editable:false,hidden:true},
        				{name:'acl_type',index:'acl_type',width:30, editable:false,hidden:false,align:'center',
        					formatter:function(cellValue, option) {
        						 switch(cellValue){
        						 	case 'ALL' : return '전사'; break;
        						 	case 'DEPT' : return '하위부서포함'; break;
        						 	case 'TEAM' : return '부서'; break;
        						 	case 'PRIVATE' : return '공유안함'; break;
        						 };
        					   }
        				},
        				{name:'open_id',index:'open_id',width:3, editable:false,hidden:true},
        				{name:'open_isgroup',index:'open_isgroup',width:3, editable:false,hidden:true},
        				{name:'creator_id',index:'creator_id',width:3, editable:false,hidden:true},
        			], 
        			autowidth:true,
        			height:190,
        			viewrecords: true,multiselect:false,sortable:true,shrinkToFit:true,gridview: true,
        			sortname:"sort_index", // 최초 정렬은 하위부서포함>부서>전사>개인			
        			sortorder:"asc",
//        			scroll:true, // virtual Scrolling
        			scrollOffset : 0,
        			viewsortcols:'vertical',
        			rowNum :50,						
        			emptyDataText: "데이터가 없습니다.",	
        			caption:'권한 목록'
        			,onSelectRow: function(rowid,status){ 	// stat = true/false		
        				selectAclWindow.event.selectAclList(rowid);
        			}
        			,beforeSelectRow: function(rowid, e) {
        				var $radio = $(e.target).closest('tr').find('input[type="radio"]');
        		        $radio.prop('checked', 'checked');
        		        // 라디오 버튼만 눌렀을 경우 rowid값이 셋팅 안되어 강제로 set
        		        $(selectAclWindow.functions.getTargetAclTableId(true)).jqGrid('setSelection',rowid);
        		        return true; // allow row selection
        			}
        			,loadBeforeSend: function() {						
        				exsoft.util.grid.gridNoDataMsgInit(selectAclWindow.functions.getTargetAclTableId());
        				exsoft.util.grid.gridTitleBarHide(selectAclWindow.functions.getTargetAclTableId());
        			}
        			,loadComplete: function() {
        				if ($(selectAclWindow.functions.getTargetAclTableId(true)).getGridParam("records")==0) {
        					exsoft.util.grid.gridNoDataMsg(selectAclWindow.functions.getTargetAclTableId(),'no_data');				
        				}
        				
        				exsoft.util.grid.gridInputInit(false);
        			}		
        			,loadError:function(xhr, status, error) {       
        				exsoft.util.error.isErrorChk(xhr);
        			}
        		});
        	},
        	
        	extAclList : function(list) {
        		$('#selAclWindowDocExtAclItemList').jqGrid('GridUnload');
        		$("#selAclWindowDocExtAclItemList").jqGrid({
        			data: list,
        			datatype:'local',
        			colNames:['accessor_id', 'accessor_isgroup', 'accessor_isalias', '접근자','기본권한','문서등록','반출취소','권한변경'],
        			colModel:[
        						{name:'accessor_id',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},
        						{name:'accessor_isgroup',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,hidden:true},
        						{name:'accessor_isalias',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,hidden:true},      
        						{name:'accessor_name',index:'accessor_name',width:30, editable:false,sortable:false,resizable:true,align:'center'},
        						{name:'doc_default_acl',index:'doc_default_acl',width:30, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',
        							editoptions:{
        								value:"NONE:없음;DELETE:삭제;UPDATE:수정;READ:조회;BROWSE:목록"
        							},formatter:'select'
        						},
        						{name:'doc_act_create',index:'doc_act_create',width:30, editable:true,sortable:false,resizable:true,align:'center',
        							edittype:'checkbox',
        							editoptions:{value:'T:F'},
        							fomatter:'checkbox'
        						},
        						{name:'doc_act_cancel_checkout',index:'doc_act_cancel_checkout',width:30, editable:true,sortable:false,resizable:true,align:'center',
        							edittype:'checkbox',
        							editoptions:{value:'T:F'},
        							fomatter:'checkbox'	
        						},
        						{name:'doc_act_change_permission',index:'doc_act_change_permission',width:30, editable:true,sortable:false,resizable:true,align:'center',
        							edittype:'checkbox',
        							editoptions:{value:'T:F'},
        							fomatter:'checkbox'	
        						},
        					], 
        			autowidth:true,
        			viewrecords: true,
        			multiselect:true,
        			sortable: true,
        			shrinkToFit:true,
        			scrollOffset: 0,  // 스크롤 위치 조정. 0으로 지정 안할 경우 테이블 우측에 여백 생김
        			gridview: true,
        			emptyDataText: "데이터가 없습니다.",	
        			caption:'추가 접근자 목록'
        			,loadBeforeSend: function() {			
        				exsoft.util.grid.gridNoDataMsgInit('selAclWindowDocExtAclItemList');				
        				exsoft.util.grid.gridTitleBarHide('selAclWindowDocExtAclItemList');
        			}
        			,loadComplete: function(data) {
        				if ($("#selAclWindowDocExtAclItemList").getGridParam("records") ==0) {	
        					exsoft.util.grid.gridNoDataMsg('selAclWindowDocExtAclItemList','nolayer_data');
        					mRowId = 0;
        				}else {
        					// 편집모드로 변경
        					var rowIDs = $("#selAclWindowDocExtAclItemList").jqGrid('getDataIDs');
        					for(var i=0; i<rowIDs.length; i++){
        						$('#selAclWindowDocExtAclItemList').editRow(rowIDs[i],true);
        					}
        				}
        				
        				exsoft.util.grid.gridInputInit(true); // 시차 문제로 gridTitleBarHide 적용 안될 시 true
        			}
        			,loadError: function (jqXHR, textStatus, errorThrown) {
        				exsoft.util.error.isErrorChk(jqXHR);
        		    }

        			,onCellSelect: function(rowid, iCol,cellcontent,e){
        				$('#selAclWindowDocExtAclItemList').editRow(rowid,false);
        		 	}
        			,onSelectRow: function(rowid,status,e){
        				var edited = exsoft.util.grid.gridEditMode('selAclWindowDocExtAclItemList',rowid);
        				// false 이면 row 저장처리
        				if (!status) {											
        					$('#selAclWindowDocExtAclItemList').jqGrid('saveRow', rowid, selectAclWindow.extParams );
        				} else {
        					if(edited == "0") {
        						$('#selAclWindowDocExtAclItemList').editRow(rowid,false);
        					}
        				}
        		     }
        			
        		});
        	}
        },
        
        // 3. 화면 이벤트 처리
        event : {
        	// 현재권한 / 상속권한 범위 설정 처리
        	selectAclScope : function(scope) {
        		selectAclWindow.selAclInfo.aclId = scope.value;
        		selectAclWindow.event.selectAclList(scope.value);
        	},
        	
        	// ACL 목록에서 ACL을 선택했을때 처리
        	selectAclList : function(aclId) {
        		selectAclWindow.selAclInfo.aclId = aclId;
        		selectAclWindow.ajax.getAclDetail(aclId);
        	},
        	
        	searchAcl : function() {
        		var _post = {
        				strKeyword2 : $(selectAclWindow.functions.getTargetFormId() + " #strKeyword2").val(),
        				is_search : "true"
        		};
        		exsoft.util.grid.gridPostDataRefresh(selectAclWindow.functions.getTargetAclTableId(true), exsoft.contextRoot + '/permission/aclList.do', _post);
        	},
        	
        	// 확인
        	submit : function() {
        		// selectAclWindow.selAclInfo.extAclItems 추가 접근자 값 설정해야함
        		selectAclWindow.callback(selectAclWindow.selAclInfo)
        		selectAclWindow.close();
        	},
			
			// 탭변경
			changeTab : function(obj) {
				
				var _tab = $(obj);
				
				if ($(_tab).hasClass("selected")) {
					return;
				}
				
				selectAclWindow.aclType = _tab.attr("name");
				
				$(_tab.parent().children()).each(function() {
					if (this == obj) {
						$(this).addClass("selected");
					} else {
						$(this).removeClass("selected");
					}
				});
				
				exsoft.util.grid.gridPostDataRefresh(selectAclWindow.functions.getTargetAclTableId(true), exsoft.contextRoot + '/permission/aclList.do', {type:selectAclWindow.aclType});
			},
			
			// 권한 등록
			createAclPopup : function() {
				registAclWindow.init(selectAclWindow.callbackFunctions.registAclWindowCallback, "create");
			},
			
			// 권한 수정
			modifyAclPopup : function() {
				//등록자와 로그인한 사용자가 동일한지 체크 한다.
				if(selectAclWindow.selAclInfo.aclDetail.creator_id != exsoft.user.user_id){
					jAlert('권한 수정은 권한 등록자만 수정 가능합니다.');
					return;
				}
				
				registAclWindow.init(selectAclWindow.callbackFunctions.registAclWindowCallback, "modify", selectAclWindow.selAclInfo.aclDetail, selectAclWindow.selAclInfo.aclItems);
			},
			
			// 권한 삭제
			deleteAclPopup : function() {
				var id = $(selectAclWindow.functions.getTargetAclTableId(true)).jqGrid("getGridParam", "selrow");
				var jsonArr = [];
				var rowData = {acl_id:""};
				
				if (id == null) {
					jAlert("'현재 적용 권한' 및 '상속 권한'은 삭제할 수 없습니다.");
					return;
				} else {
					rowData['acl_id'] = id;	
					jsonArr[0] = rowData;
					
					jConfirm('권한을 삭제하시겠습니까?', '권한 삭제', 0, function(isConfirm){
						try {
							var jsonObject = { "type":"delete", "acl_idList":JSON.stringify(jsonArr)};
							if(isConfirm) {
								exsoft.util.ajax.ajaxDataFunctionWithCallback(jsonObject, exsoft.contextRoot + '/permission/aclControl.do', id, function(data, param){
									if(data.result == "true") {
										exsoft.util.grid.gridRefresh(selectAclWindow.functions.getTargetAclTableId(), exsoft.contextRoot + '/permission/aclList.do');
										selectAclWindow.ui.setDefaultAclScope();
									} else {
										jAlert(data.message);
									}
								});
							}
						}finally{
							jsonObject = null;
							jsonArr = null;
						}
					});
				}
			},
			
			// 권한 복사
			copyAclPopup : function() {
				// 호출하는 페이지에서 registAclWindow.jsp include 해야 함.
				var acl_id = $(selectAclWindow.functions.getTargetFormId() + " input[name=radio_selectAcl]:checked").val();

				if(acl_id == undefined || acl_id == null)
					acl_id = $(selectAclWindow.functions.getTargetAclTableId(true)).jqGrid("getGridParam", "selrow" );
				
				exsoft.util.ajax.ajaxDataFunctionWithCallback({'acl_id' : acl_id}, exsoft.contextRoot + "/permission/aclItemList.do", acl_id, function(data, param) {
					registAclWindow.init(selectAclWindow.callbackFunctions.registAclWindowCallback, "copy", data.aclDetail, data.list);
				});
			},

			
			// 접근자 추가
			addAccessor : function() {
				// 접근자 추가 윈도우 팝업
				selectAccessorWindow.init(selectAclWindow.callbackFunctions.addAccessor, "selAclWindowDocExtAclItemList", selectAclWindow.type);
			},
			
			// 접근자 제거
			removeAccessor : function() {
				exsoft.util.grid.gridDeleteRow('selAclWindowDocExtAclItemList', '','', true);
			}
        },

        // 4. 화면 UI 변경 처리
        ui : {
        	// "현재 적용 권한"으로 라디오 버튼 초기화
        	setDefaultAclScope : function() {
        		
        		var _formId = selectAclWindow.functions.getTargetFormId(true);
        		var _selector = _formId + " #pCurAclId";
        		
        		$(_selector).prop("checked", true);
        		
        	},
        	
        	// "현재 적용 권한", "상속 권한" 라디어 버튼 언체크
        	setUncheckAclScope : function() {
        		var _formId = selectAclWindow.functions.getTargetFormId(true);
        		var _selector = "{0} {1}, {0} {2}".format(_formId, "#pCurAclId", "#pInheritAclName");
        		
        		$(_selector).prop("checked", false);
        	},
        	
        	// "현재 적용 권한", 상속 권한" 라디오 버튼 활성 / 비활성
        	setEnableRadio : function(bEnabled) {
        		var _formId = selectAclWindow.functions.getTargetFormId(true);
        		var _selector = "{0} {1}, {0} {2}".format(_formId, "#pCurAclId", "#pInheritAclName");
        		
        		$(_selector).prop("disabled", bEnabled);
        	}
        	
        },
        
        // 5. 콜백
        callbackFunctions : {
        	// 권한 등록 콜백
        	registAclWindowCallback : function(data, param) {
        		if (data.result != "true") {
        			jAlert(data.message);
        			return;
        		}
        		
        		var _msg = "[{0}] 권한 {1}했습니다.".format(data.acl_name, param == "create" ? "등록" : "수정");
        		
        		jAlert(_msg);
        		exsoft.util.grid.gridRefresh(selectAclWindow.functions.getTargetAclTableId(), exsoft.contextRoot + "/permission/aclList.do");
        		selectAclWindow.ui.setDefaultAclScope();
        	},
        	
        	// 접근자 추가 선택 윈도우 콜백
        	addAccessor : function(extAclList) {
        		console.info("[selectAclWindow]");
        		console.info(extAclList);
        		exsoft.util.grid.gridSetAclItemAddCallback('selAclWindowDocExtAclItemList', extAclList);
        	},
        	
        }
        
}