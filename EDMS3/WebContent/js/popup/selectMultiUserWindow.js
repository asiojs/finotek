var selectMultiUserWindow = {
		
		callbackFunction : null, 	// 확인
		treeObject : null,			// 부서 트리 객체
		pageSize : 0,
		
		// 0. 초기화
		init : function(pageSize, callback) {
			
			//그룹관리 > 구성원추가 - 창 닫기
    	    $('.grpDeptUser_add_close').bind("click", function(e){
    	    	e.preventDefault();
    	    	$(this).parents('.grpDeptUser_add').addClass('hide');
    	    	$('.grpDeptUser_add_wrapper').addClass('hide');
    	    });

    	    //그룹관리 > 구성원추가 창 닫기 : 음영진 부분 클릭 시 닫기
    	    $('.grpDeptUser_add_wrapper').bind("click", function(){
    	    	$(this).addClass('hide');
    	    	$('.grpDeptUser_add').addClass('hide');
    	    });
    	    
			// 콜백 함수 저장
			selectMultiUserWindow.callbackFunction = callback;
			
			selectMultiUserWindow.open();
			
			// 1. 트리 초기화
			if (selectMultiUserWindow.treeObject == undefined) {
				treeOption = {
    					divId : "#pop_groupTree",
    					context : exsoft.contextRoot,
    					url : "/group/groupList.do",
    					treeType : "admin"
    			};
				
				selectMultiUserWindow.treeObject = new XFTree(treeOption);
				selectMultiUserWindow.treeObject.callbackSelectNode = function(e, data) {
					// 부서 사용자 목록 조회
					exsoft.util.grid.gridPostDataRefresh('#pop_userList', exsoft.contextRoot + '/admin/groupUserList.do', {groupId:data.node.id});
				}
				selectMultiUserWindow.treeObject.init();
			} else {
				selectMultiUserWindow.treeObject.refresh();
			}
			
			// 2. 검색 결과 테이블 초기화
			if ($("#pop_userList")[0].grid == undefined) {
				$('#pop_userList').jqGrid({		
					url: exsoft.contextRoot + '/user/searchUserList.do',
					mtype:"post",
					datatype:'json',		
					jsonReader:{
						page:'page',total:'total',root:'list'
					},		
					colNames:['group_nm','user_name_ko','user_id','position_nm','role_nm','email','user_status_nm'],
					colModel:[
						{name:'group_nm',index:'group_nm',width:30, editable:false,sortable:true,resizable:true,hidden:false,align:'center'},
						{name:'user_name_ko',index:'user_name_ko',width:50, editable:false,sortable:false,resizable:true,hidden:false,align:'center'},
						{name:'user_id',index:'user_id',width:50, editable:false,sortable:false,resizable:true,hidden:false,align:'center'},
						{name:'position_nm',index:'position_nm',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
						{name:'role_nm',index:'role_nm',width:10, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
						{name:'email',index:'email',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
						{name:'user_status_nm',index:'user_status_nm',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'}
					], 
					autowidth:true,
					viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					sortname : "group_nm",			
					sortorder:"desc",
					scroll: true,
					scrollOffset: 0,
					rowNum : selectMultiUserWindow.pageSize,						
					emptyDataText: "데이터가 없습니다.",				
					caption:'사용자 목록',	
					pagerpos: 'center',  
				    pginput: true,
					loadError:function(xhr, status, error) {       
						exsoft.util.error.isErrorChk(xhr);
					 }
					,loadBeforeSend: function() {					
						exsoft.util.grid.gridNoDataMsgInit('pop_userList');
						exsoft.util.grid.gridTitleBarHide('pop_userList');	
					}
					,loadComplete: function() {
						exsoft.util.grid.gridInputInit(false);
					}
				});
				
				// Grid 컬럼정렬 처리
				var headerData = '{"group_nm":"그룹명","user_name_ko":"사용자명", "user_id":"사용자 ID"}';
				exsoft.util.grid.gridColumHeader('pop_userList',headerData,'center');

				headerData = null;
			}
			
			// 3. 그룹의 기존 멤버를 설정함
			if ($("#memberList")[0].grid == undefined) {
				$('#memberList').jqGrid({		
					url: exsoft.contextRoot + '/user/searchUserList.do',
					mtype:"post",
					datatype:'json',		
					jsonReader:{
						page:'page',total:'total',root:'list'
					},		
					colNames:['group_nm','user_name_ko','user_id','position_nm','role_nm','email','user_status_nm'],
					colModel:[
						{name:'group_nm',index:'group_nm',width:30, editable:false,sortable:true,resizable:true,hidden:false,align:'center'},
						{name:'user_name_ko',index:'user_name_ko',width:50, editable:false,sortable:false,resizable:true,hidden:false,align:'center'},
						{name:'user_id',index:'user_id',width:50, editable:false,sortable:false,resizable:true,hidden:false,align:'center'},
						{name:'position_nm',index:'position_nm',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
						{name:'role_nm',index:'role_nm',width:10, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
						{name:'email',index:'email',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'},
						{name:'user_status_nm',index:'user_status_nm',width:30, editable:false,sortable:true,resizable:true,hidden:true,align:'center'}
					], 
					autowidth:true,
					viewrecords: true,multiselect:true,sortable: true,shrinkToFit:true,gridview: true,
					sortname : "group_nm",			
					sortorder:"desc",
					scroll: true,
					scrollOffset: 0,
					rowNum : selectMultiUserWindow.pageSize,						
					emptyDataText: "데이터가 없습니다.",				
					caption:'사용자 목록',	
					pagerpos: 'center',  
				    pginput: true,
					loadError:function(xhr, status, error) {       
						exsoft.util.error.isErrorChk(xhr);
					 }
					,loadBeforeSend: function() {					
						exsoft.util.grid.gridNoDataMsgInit('memberList');
						exsoft.util.grid.gridTitleBarHide('memberList');	
					}
					,loadComplete: function() {
						// 기존 데이터를 삭제함
						$("#memberList").jqGrid("clearGridData");
						exsoft.util.grid.gridInputInit(false);
					}
				});
				
				// Grid 컬럼정렬 처리
				var headerData = '{"group_nm":"그룹명","user_name_ko":"사용자명", "user_id":"사용자 ID"}';
				exsoft.util.grid.gridColumHeader('memberList',headerData,'center');

				headerData = null;
			} else {
				// 기존 데이터를 삭제함
				$("#memberList").jqGrid("clearGridData");
			}
		},

		// 1. 팝업
		open : function() {
			exsoft.util.layout.divLayerOpen("grpDeptUser_add_wrapper", "grpDeptUser_add");
		},

		//2. layer + show
		layer : {
		},

		//3. 닫기 + hide
		close : function() {
			exsoft.util.layout.divLayerClose("grpDeptUser_add_wrapper", "grpDeptUser_add");
		},

		//4. 화면 이벤트 처리
		event : {
			// 선택된 사용자를 목록에 추가
			appendUser : function() {
				if (!exsoft.util.grid.gridSelectCheck("pop_userList")) {
        			jAlert("추가할 사용자를 선택해주세요.", "구성원추가", 0);
        			return;
        		}
				
				var rowDataList = new Array();
				var selectedUserList = $("#pop_userList").getGridParam("selarrrow");
				var memberIdList = exsoft.util.grid.gridSelectArrayDataAllRow("memberList", "user_id", "user_id");
				
				// 1. 선택된 사용자를 추가한다
				$(selectedUserList).each(function() {
					var row = $("#pop_userList").getRowData(this);
					var isDuplicate = false;
					
					// 1-1. 이미 있는 사용자인지 체크한다
					$(memberIdList).each(function() {
						if (this.user_id == row.user_id) {
							isDuplicate = true; 
						}
					});
					
					if (!isDuplicate) {
						$("#memberList").jqGrid("addRowData", row.user_id, row);
					}
				});
				
			},

			// 목록에서 사용자를 제거
			removeUser : function() {
				if (!exsoft.util.grid.gridSelectCheck("memberList")) {
        			jAlert("제외할 사용자를 선택해주세요.", "구성원추가", 0);
        			return;
        		}
				
				exsoft.util.grid.gridDeleteRow("memberList", null, null, true);
			},
			
			// 부서/사용자 조건 검색
			searchGroupUser : function() {
				if ($("#strKeyword").val().length == 0) {
					alert("검색어를 입력해주세요", "구성원추가", 0);
					return;
				}
				
				exsoft.util.grid.gridPostDataInitRefresh('pop_userList', exsoft.contextRoot + '/user/searchUserList.do', {userName : $("#strKeyword").val()});
			},
			
			// 엔터키 입력시
			enterKeyPress : function(e) {
				if (e.keyCode == 13) {
					selectMultiUserWindow.event.searchGroupUser();
					return false;
				}
			},
			
			// 확인버튼 클릭시
			submit : function() {
				// 최종 사용자 목록 설정
				var userIdList = $("#memberList").jqGrid("getDataIDs");
				var rowDataList = new Array();
				
				$(userIdList).each(function(i) {
					if (this != "") {
						var row = $("#memberList").getRowData(this);
						rowDataList.push(row);
					}
				})
				
				// 콜백함수로 리턴
				selectMultiUserWindow.callbackFunction(rowDataList);
				
				selectMultiUserWindow.close();
			}
			
		},

		//5. 화면 UI 변경 처리
		ui : {
		},

		//6. callback 처리
		callback : {
		},
}