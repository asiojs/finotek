var aclManager = {
		currentAclId : null,
		
		init : function(pageSize) {
			aclManager.grid.pageSize = pageSize;
			aclManager.grid.initAclGridList();
			aclManager.grid.initAclItemGridList();
			
			exsoft.util.common.ddslick('#strIndex', 'srch_type', '', 120, function(divId, selectedData){
			});
			
			exsoft.util.common.ddslick('#acl_type', 'srch_type', '', 120, function(divId, selectedData){
			});
			
		},
		
		ajax : {
			aclDetail : function(aclId) {
				exsoft.util.ajax.ajaxDataFunctionWithCallback({"acl_id" : aclId}, exsoft.contextRoot+"/admin/aclDetail.do", "", function(data, param) {
					//XR_ACL Info View
					$('#acl_id').val(data.aclDetail.acl_id);				// hidden
					$('#src_acl_name').val(data.aclDetail.acl_name);		// hidden
					$('#open_id').val(data.aclDetail.open_id);				// hidden
					$('#open_isgroup').val(data.aclDetail.open_isgroup);	// hidden
					$('#creator_id').val(data.aclDetail.creator_id);		// hidden
					$('#acl_sort_index').text(data.aclDetail.sort_index);	// hidden
					
					$('#acl_name_title').text(data.aclDetail.acl_name);	
					$('#acl_name').val(data.aclDetail.acl_name);
					
//					$('#acl_type').val(data.aclDetail.acl_type);
					exsoft.util.layout.setSelectBox('acl_type', data.aclDetail.acl_type);
					$('#open_name').val(data.aclDetail.open_name);
					$('#create_date').text(data.aclDetail.create_date);
					$('#creator_name').text(data.aclDetail.creator_name);
					
					// XR_ACLITEM 속성 리스트
					aclManager.grid.refreshAclItemList();
					
//					if($('#aclItemGridList')[0].grid != undefined)	{
//						var postData = {acl_id:aclManager.currentAclId} ;
//						exsoft.util.grid.gridPostDataRefresh('aclItemGridList',exsoft.contextRoot+'/permission/aclItemList.do',postData);
//					}else {
//						aclItemGridList();
//					}		
				});
			}
		},
		
		grid : {
			pageSize : 0,
			mRowId : 0,			// 수정컬럼ID
			mList : 0,
			paging : function (pageNum) {
//				$("#mypageDocList").setGridParam({page:nPage,postData:{is_search:'false',page_init:'false'}}).trigger("reloadGrid");
			},
			refreshAclList : function() {
				exsoft.util.grid.gridRefresh('aclGridList', exsoft.contextRoot + '/permission/aclList.do');
			},
			refreshAclItemList : function() {
				if($('#aclItemGridList')[0].grid != undefined)	{
					exsoft.util.grid.gridPostDataRefresh('aclItemGridList',exsoft.contextRoot+'/permission/aclItemList.do', {acl_id:aclManager.currentAclId});
				}
			},
			initAclGridList : function() {
				$('#aclGridList').jqGrid({		
					url: exsoft.contextRoot + '/permission/aclList.do',
					mtype:"post",
					datatype:'json',		
					jsonReader:{
						page:'page',total:'total',root:'list'
					},		
					colNames:['acl_id','권한명','공개대상','공유범위','공유범위','정렬'],
					colModel:[
						{name:'acl_id',index:'acl_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},      
						{name:'acl_name',index:'acl_name',width:130, editable:false,sortable:true,resizable:true},
						{name:'open_name',index:'open_name',width:70, editable:false,sortable:true,resizable:true,align:'center'},
						{name:'acl_type_name',index:'acl_type_name',width:5, editable:false,sortable:false,resizable:true,align:'center',hidden:true},
						{name:'acl_type',index:'acl_type',width:70, editable:false,sortable:true,resizable:true,align:'center',
							 formatter:function(cellValue, option) {
								 switch(cellValue){
								 	case 'ALL' : return '전사'; break;
								 	case 'DEPT' : return '하위부서포함'; break;
								 	case 'TEAM' : return '부서'; break;
								 	case 'PRIVATE' : return '공유안함'; break;
								 };
							   }
						},
						{name:'sort_index',index:'sort_index',width:3, editable:false,hidden:true},
					], 
					autowidth:true,
					viewrecords: true,multiselect:true,sortable:true,shrinkToFit:true,gridview: true,
					scrollOffset:0,
					sortname:"sort_index", // 최초 정렬은 하위부서포함>부서>전사>개인			
					sortorder:"asc",
					multikey: "ctrlKey",
					viewsortcols:'vertical',
					rowNum : aclManager.grid.pageSize,						
					rowList:exsoft.util.grid.listArraySize(),
					emptyDataText: "데이터가 없습니다.",	
					caption:'권한 목록'
					,onCellSelect : function(rowid,iCol,cellcontent,e){
			            
						if(iCol == 0){
							// 체크시 row값을 set한다.(선택시 : rowid셋팅, 해제시 : rowid제거)
							$("#aclGridList").jqGrid('setSelection',rowid);
						} else {
							aclManager.currentAclId = rowid;
							aclManager.ajax.aclDetail(rowid);
						}
						
					}
					,loadBeforeSend: function() {						
						exsoft.util.grid.gridNoDataMsgInit('aclGridList');
						exsoft.util.grid.gridTitleBarHide('aclGridList');
					}
					,loadComplete: function(data) {
						
						if ($("#aclGridList").getGridParam("records")==0) {		
							exsoft.util.grid.gridNoRecords('aclGridList','nolayer_data');				
						}else {
							console.log("records != 0");
							exsoft.util.grid.gridViewRecords('aclGridList');
//							exsoft.util.grid.gridPagerViewHide('aclGridList');
//							exsoft.util.grid.gridPagerShow('aclGridList');
							
							// 조회화면 DISPLAY
							var rowId = $("#aclGridList").getDataIDs()[0];
							aclManager.currentAclId = $("#aclGridList").getRowData(rowId).acl_id;					
							aclManager.ajax.aclDetail(aclManager.currentAclId);
						}
						
						exsoft.util.grid.gridPager("#aclGridPager",data);
						
						exsoft.util.grid.gridInputInit(false); // 페이지 창 숫자만  test
//						exsoft.util.grid.gridResize('aclGridList','targetAclGrid',55); //페이지 div 맨 하단에
					}		
					,loadError:function(xhr, status, error) {       
						exsoft.util.error.isErrorChk(xhr);
					 }		
//					,onPaging: function (pgButton) {
//						// 사용자 입력한 페이지 숫자
//						var pagerId = this.p.pager.substr(1); 
//						var inputPage = $('input.ui-pg-input', "#pg_" + $.jgrid.jqID(pagerId)).val();
//						exsoft.util.grid.onPager('aclGridList',inputPage,pgButton);
//					 }
				});
			},
			
			initAclItemGridList : function() {

				$('#aclItemGridList').jqGrid({		
//					url:exsoft.contextRoot+'/permission/aclItemList.do',
					list : "",
					mtype:"post",
					datatype:'json',
					jsonReader:{				
						root:'list'
					},
					colNames:['accessor_id', 'accessor_isgroup', 'accessor_isalias', '접근자','기본권한','폴더등록','권한변경','기본권한','문서등록','반출취소','권한변경'],
					colModel:[
						{name:'accessor_id',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'accessor_isgroup',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},
						{name:'accessor_isalias',index:'accessor_id',width:5, align:'center',editable:false,sortable:false,key:true,hidden:true},      
						{name:'accessor_name',index:'accessor_name',width:30, editable:false,sortable:false,resizable:true,align:'center',hidden:false},
						{name:'fol_default_acl',index:'fol_default_acl',width:30, editable:true,sortable:false,resizable:true,align:'center',edittype:'select',
							editoptions:{
								value:"DELETE:삭제;UPDATE:수정;READ:조회;BROWSE:목록"
							},formatter:'select' //formatter의 역활은 value값으로 grid에 표시함.
						},
						{name:'fol_act_create',index:'fol_act_create',width:30, editable:true,sortable:false,resizable:true,align:'center',
							edittype:'checkbox',
							editoptions:{value:'T:F'},
							fomatter:'checkbox'
						},
						{name:'fol_act_change_permission',index:'fol_act_change_permission',width:30, editable:true,sortable:false,resizable:true,align:'center',
							edittype:'checkbox',
							editoptions:{value:'T:F'},
							fomatter:'checkbox'
						},
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
					scrollOffset: 0,
					gridview: true,
					postData : {acl_id:aclManager.currentAclId},				
					emptyDataText: "데이터가 없습니다.",	
					caption:'접근자 목록'
					,loadBeforeSend: function() {			
						exsoft.util.grid.gridNoDataMsgInit('aclItemGridList');				
						exsoft.util.grid.gridTitleBarHide('aclItemGridList');
					}
					,loadComplete: function(data) {
						
						if ($("#aclItemGridList").getGridParam("records") ==0) {						
							exsoft.util.grid.gridNoDataMsg('aclItemGridList','nolayer_data');
							aclManager.grid.mRowId = 0;
						}else {
							
							var rowIDs = $("#aclItemGridList").jqGrid('getDataIDs');
							aclManager.grid.mRowId = rowIDs[rowIDs.length-1];
						}
						
						aclManager.grid.mList = aclManager.grid.mRowId;		// atrr_id 수정못하게 처리위한 변수			
						
					}
					,onCellSelect: function(rowid, iCol,cellcontent,e){
						if(base.gridIsRowDataExist('aclItemGridList', rowid, 'accessor_id', 'OWNER')) {
							jAlert('소유자는 수정할 수 없습니다.');
							$('#aclItemGridList').jqGrid('setSelection',rowid,false); ////checkbox 해제
						} else {
							$('#aclItemGridList').editRow(rowid,false);
						}
				 	}
					,onSelectRow: function(rowid,status,e){
						if(!base.gridIsRowDataExist('aclItemGridList', rowid, 'accessor_id', 'OWNER')) {
							// 에디터모드인지 체크
							var edited = base.gridEditMode('aclItemGridList',rowid);
							// false 이면 row 저장처리
							if(!status) {											
								$('#aclItemGridList').jqGrid('saveRow', rowid, gParameters );
							}else {
								if(edited == "0") {
									$('#aclItemGridList').editRow(rowid,false);
								}
							}
						}
				     }
				});
				
				// 헤더 colspan
				jQuery("#aclItemGridList").jqGrid('setGroupHeaders', {
				  useColSpanStyle: true, 
				  groupHeaders:[
					{startColumnName: 'fol_default_acl', numberOfColumns: 3, titleText: '폴더권한'},
					{startColumnName: 'doc_default_acl', numberOfColumns: 4, titleText: '문서권한'}
				  ]	
				});
				
			}
		},
		
		event : {
			searchAclList : function() {
				var postData = {
						strIndex: exsoft.util.common.getDdslick("#strIndex"),
						strKeyword1:$("#strKeyword1").val(),
						strKeyword2:$("#strKeyword2").val(),
						is_search:'true'
				};
				exsoft.util.grid.gridPostDataRefresh('aclGridList',exsoft.contextRoot+'/permission/aclList.do',postData);
			}
		},
		
		ui : {
			
		},
		
		callbackFunctions : {
			
		}
}