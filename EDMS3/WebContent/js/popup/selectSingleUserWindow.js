var selectSingleUserWindow = {
	callbackFunction : null,
	treeObject : null,
	
    // 0. 초기화
    init : {
    	initSingleUserWindow : function(callback) {
    		
    		//내 소유 문서 >  소유권이전 - 창 닫기
    	    $('.grant_transfer_close').bind("click", function(e){
    	    	e.preventDefault();
    	    	$(this).parents('.grant_transfer').addClass('hide');
    	    	$('.grant_transfer_wrapper').addClass('hide');
    	    });

    	    //내 소유 문서 > 소유권이전 창 닫기 : 음영진 부분 클릭 시 닫기
    	    $('.grant_transfer_wrapper').bind("click", function(){
    	    	$(this).addClass('hide');
    	    	$('.grant_transfer').addClass('hide');
    	    });
    		
    		// 콜백 함수 저장
    		selectSingleUserWindow.callbackFunction = callback;
    		
    		// 팝업창 오픈
    		selectSingleUserWindow.open.layerOpen();
    		
    		// 1. 트리 초기화
    		if (selectSingleUserWindow.treeObject == undefined) {
    			var treeOption = {
    					divId : "#transfer_left_tree",
    					context : exsoft.contextRoot,
    					url : "/group/groupList.do"
    			};
    			selectSingleUserWindow.treeObject = new XFTree(treeOption);
    			selectSingleUserWindow.treeObject.callbackSelectNode = function(e, data) {
    				// 검색 옵션 초기화
    				$("#pop_sg_groupName").val("");
    				$("#pop_sg_userName").val("");
    				
    				var param = {
    						groupName : "",
    						userName : "",					
    						groupId : data.node.id
    				}
    				
    				// 부서 사용자 목록 조회
    				exsoft.util.grid.gridPostDataRefresh('#pop_searchUserList',exsoft.contextRoot + '/user/searchUserList.do', param);
    			}
    			selectSingleUserWindow.treeObject.init();
    		} else {
    			selectSingleUserWindow.treeObject.refresh();
    		}
    		
    		// 2. 사용자 목록 그리드 초기화
    		if ($("#pop_searchUserList")[0].grid == undefined) {
    			$('#pop_searchUserList').jqGrid({
    				url:exsoft.contextRoot + '/user/searchUserList.do',
    				mtype:"post",
    				datatype:'json',		
    				jsonReader:{
    					page:'page',total:'total',root:'list'
    				},		
    				colNames:['','group_nm','user_name_ko','position_nm','role_nm','email','user_status_nm'],
    				colModel:[
    					{name:'user_id',index:'user_id',width:5, editable:false,sortable:false,resizable:true,align:'center',key:true,edittype:'radio',
    						formatter:function(cellValue, option) {
    							   return '<input type="radio" name="radio_'+option.gid+'" value="'+cellValue
    							   +'" onclick="javascript:selectSingleUserWindow.event.selectUser(\''+cellValue+'\')"/>';
    						   },hidden:false
    					},
    					{name:'group_nm',index:'group_nm',width:60, editable:false,sortable:true,resizable:true,hidden:false,align:'center'},
    					{name:'user_name_ko',index:'user_name_ko',width:60, editable:false,sortable:false,resizable:true,hidden:false,align:'center'},
    					{name:'position_nm',index:'position_nm',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
    					{name:'role_nm',index:'role_nm',width:10, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
    					{name:'email',index:'email',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
    					{name:'user_status_nm',index:'user_status_nm',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'}
    				], 
    				autowidth:true,
    				height:200,
    				viewrecords: true,multiselect:false,sortable: true,shrinkToFit:true,gridview: true,
    				sortname : "user_name_ko",			
    				sortorder:"asc",
    				scroll:true, // virtual Scrolling
    				scrollOffset : 0,  
    				rowNum : 10,						
    				rowList : exsoft.util.grid.listArraySize(),
    				emptyDataText: "데이터가 없습니다.",				
    				caption:'사용자 목록',	
    				loadError:function(xhr, status, error) {       
    					exsoft.util.error.isErrorChk(xhr);
    				 }
    				,loadBeforeSend: function() {					
    					exsoft.util.grid.gridTitleBarHide('pop_searchUserList');	
    				}
    				,loadComplete: function() {							
    					exsoft.util.grid.gridInputInit(false);
    				}
    				,beforeSelectRow: function(rowid, e)
    			    {		       
    			        var $radio = $(e.target).closest('tr').find('input[type="radio"]');
    			        $radio.prop('checked', 'checked');
    			        // 라디오 버튼만 눌렀을 경우 rowid값이 셋팅 안되어 강제로 set
    			        $("#pop_searchUserList").jqGrid('setSelection',rowid);
    			        return true; // allow row selection
    			    }
    				
    			});
    			
    			// Grid 컬럼정렬 처리
    			var headerData = '{"group_nm":"그룹명","user_name_ko":"사용자명"}';
    			exsoft.util.grid.gridColumHeader('pop_searchUserList',headerData,'center');
    			
    			headerData = null;
    			currentGrid = null;
    		}
    	},
    },

    // 1. 팝업
    open : {
    	layerOpen : function() {
    		exsoft.util.layout.divLayerOpen("grant_transfer_wrapper", "grant_transfer");
    	},
    },

    //2. layer + show
    layer : {
    },

    //3. 닫기 + hide
    close : {
    	layerClose : function() {
    		exsoft.util.layout.divLayerClose("grant_transfer_wrapper", "grant_transfer");
    	},
    },

    //4. 화면 이벤트 처리
    event : {
    	
    	// 확인버튼 클릭시
    	okButtonClick : function() {
    		var rowId = $("#pop_searchUserList").getGridParam("selrow");
    		var rowData = $("#pop_searchUserList").getRowData(rowId);

    		rowData.user_id = rowId;
    		
    		if (rowId == null) {
    			jAlert("사용자를 선택해주세요");
    			return;
    		}
    		
    		selectSingleUserWindow.event.cancelButtonClick();
    		
    		selectSingleUserWindow.callbackFunction(rowData);
    	},
    	
    	// 취소버튼 클릭시
    	cancelButtonClick : function() {
    		selectSingleUserWindow.close.layerClose();
    	},
    	
    	// 사용자 검색
    	searchGroupUser : function() {
    		
    		var param = {
    				groupName : $("#pop_sg_groupName").val(),
    				userName : $("#pop_sg_userName").val(),
    	 			groupId : ''
    		}
    		exsoft.util.grid.gridPostDataRefresh('#pop_searchUserList',exsoft.contextRoot + '/user/searchUserList.do', param);
    	},
    	
    	//grid 라디오 버튼 클릭 시 이벤트
    	selectUser : function(rowid) {
    		// 현재값 셋팅
    		$("#pop_searchUserList").jqGrid('setSelection',rowid);
    	},
    	
    	// 엔터키 입력시
		enterKeyPress : function(e) {
			if (e.keyCode == 13) {
				selectSingleUserWindow.event.searchGroupUser();
				return false;
			}
		}
    },

    //5. 화면 UI 변경 처리
    ui : {
    },

    //6. callback 처리
    callback : {
    },

}
